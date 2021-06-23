package nikita.webapp.service.impl.odata;

import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.hateoas.DocumentObjectHateoas;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.webapp.hateoas.DocumentObjectHateoasHandler;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.odata.NikitaODataToHQL;
import nikita.webapp.odata.ODataToHQL;
import nikita.webapp.odata.QueryObject;
import nikita.webapp.odata.base.ODataLexer;
import nikita.webapp.odata.base.ODataParser;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.odata.IODataService;
import nikita.webapp.util.AddressComponent;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.hibernate.Hibernate;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.query.Query;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialClob;
import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static java.lang.Integer.MAX_VALUE;
import static java.net.URLDecoder.decode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static nikita.common.config.Constants.DELETE_RESPONSE;
import static nikita.common.config.Constants.HATEOAS_API_PATH;
import static nikita.common.util.CommonUtils.FileUtils.getClassFromName;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestAsListOrThrow;
import static org.antlr.v4.runtime.CharStreams.fromString;
import static org.hibernate.ScrollMode.SCROLL_INSENSITIVE;
import static org.springframework.http.HttpHeaders.ALLOW;


@Service
public class ODataService
        implements IODataService {

    private final Logger logger =
            LoggerFactory.getLogger(ODataService.class);
    private final EntityManager entityManager;
    private final AddressComponent address;
    /**
     * if a query does not have a page size set (e.g., $top=20) then use the
     * value set from yml file. If yml value missing fall back to 10
     */
    @Value("${nikita.application.pagination.max-page-size:10}")
    protected int maxResults;

    /**
     * To prevent resource starvation set an absolute maximum size for a
     * query. If e.g., a client calls a request with $top=90000 then use the
     * value set from yml file. If yml value missing fall back to 1000
     */
    @Value("${nikita.application.pagination.absolute-max-page-size:1000}")
    protected int absoluteMaxResults;

    public ODataService(EntityManager entityManager,
                        AddressComponent address) {
        this.entityManager = entityManager;
        this.address = address;
    }

    @Override
    @Transactional
    public String processODataQueryDelete() {
        QueryObject queryObject = convertODataToHQL("");
        Query<INoarkEntity> query = queryObject.getQuery();
        int result = query.executeUpdate();
        logger.info(result + " records deleted for OData Query [" +
                query.getQueryString() + "]");
        return DELETE_RESPONSE;
    }

    @Override
    public HateoasNoarkObject processODataQueryGet() {
        return processODataQueryGet("");
    }

    /**
     * Note: This method has an obvious flaw:
     * .search(DocumentObject.class)
     * Currently we only allow $search to work on document contents
     * (documentTokens). This is because hibernate is generating indexes per
     * object type (Fonds.class, DocumentObject.class). When we make
     * hibernate work with a single index, we can search across all values.
     * The point of the code at the moment is just to get fulltext working.
     */
    @Override
    public HateoasNoarkObject processODataSearchQuery(
            String search, int fetchCount, int from) {
        try {
            SerialClob clobQuery = new SerialClob(search.toCharArray());
            SearchResult<DocumentObject> results = Search.session(entityManager)
                    .search(DocumentObject.class)
                    .where(f -> f.match()
                            .fields("documentTokens")
                            .matching(clobQuery))
                    .fetch(fetchCount);
            long totalRows = results.total().hitCount();
            List<INoarkEntity> resultsDocObject = List.copyOf(results.hits());
            NikitaPage page = new NikitaPage(resultsDocObject, totalRows,
                    fetchCount, from);
            DocumentObjectHateoasHandler handler = new DocumentObjectHateoasHandler();
            DocumentObjectHateoas documentObjectHateoas =
                    new DocumentObjectHateoas(page);
            handler.setPublicAddress(address.getAddress());
            handler.setContextPath(address.getContextPath());
            handler.addLinks(documentObjectHateoas, new Authorisation());
            setOutgoingRequestHeaderList();
            return documentObjectHateoas;
        } catch (SQLException e) {
            logger.error(e.toString());
        }
        return null;
    }

    @Override
    public HateoasNoarkObject processODataQueryGet(String odataAppend) {
        try {
            QueryObject queryObject = convertODataToHQL(odataAppend);
            String fromEntity = queryObject.getFromEntity();
            Query<INoarkEntity> query = queryObject.getQuery();

            int totalRows = getCountFromQuery(query);
            checkMaxResultsChangeIfRequired(query);
            int top = query.getMaxResults();
            int skip = query.getFirstResult();
            // Execute the SELECT query and return the query results
            List<INoarkEntity> entityList = query.getResultList();
            // Unproxy objects in the list. It was noticed that (nikita) Class
            // objects had proxied values in some cases. It is unclear why as
            // the list is a direct query to pull out all Class objects. I
            // reason that it may be due to a class having a child and parent.
            // This is relevant then for File and Fonds. Basically make
            // sure the list only contains a list of unproxied objects
            for (int i = 0; i < entityList.size(); i++) {
                if (entityList.get(i) instanceof HibernateProxy) {
                    entityList.set(i, (INoarkEntity)
                            Hibernate.unproxy(entityList.get(i)));
                }
            }
            NikitaPage page = new NikitaPage(entityList, totalRows, top, skip);
            HateoasNoarkObject noarkObject;
            HateoasHandler handler;

            //if (entityList.size() > 0) {
            Class<INoarkEntity> cls = getClassFromName(fromEntity);
            HateoasPacker packer = cls.getAnnotation(HateoasPacker.class);
            HateoasObject hateoasObject = cls.getAnnotation(HateoasObject.class);
            // TODO: add if null
            // class supports a Page rather than a list. But it is only
            // intended to be here temporarily. Once this works all Hateoas
            // classes should be forced to work with a Page rather than a List
            boolean classSupportsPage = true;
            try {
                hateoasObject.using()
                        .getDeclaredConstructor(NikitaPage.class);
            } catch (NoSuchMethodException exception) {
                classSupportsPage = false;
            }

            if (classSupportsPage) {
                noarkObject = hateoasObject.using()
                        .getDeclaredConstructor(NikitaPage.class)
                        .newInstance(page);
            } // TODO : Is it safe to remove this else ??
            else {
                noarkObject = hateoasObject.using()
                        .getDeclaredConstructor(List.class)
                        .newInstance(entityList);
            }
            handler = packer.using().getConstructor().newInstance();
            // } else {
            //     noarkObject = new HateoasNoarkObject(new ArrayList<>(),
            //            "unknown");
            //    handler = new EmptyListHateoasHandler();
            // }
            handler.setPublicAddress(address.getAddress());
            handler.setContextPath(address.getContextPath());
            handler.addLinks(noarkObject, new Authorisation());
            setOutgoingRequestHeaderList();
            return noarkObject;
        } catch (Exception e) {
            throw new NikitaMalformedInputDataException(e.getMessage());
        }
    }

    private QueryObject convertODataToHQL(@NotNull final String additionalOdata) {
        StringBuffer requestURL = getRequest().getRequestURL();

        // Remove trailing / as it causes confusion for parsing. See also:
        // https://gitlab.com/OsloMet-ABI/nikita-noark5-core/-/issues/200
        if (requestURL.charAt(requestURL.length() - 1) == '/') {
            requestURL.setLength(requestURL.length() - 1);
        }
        //  remove everything up to api/packagename/
        int apiLength = (HATEOAS_API_PATH + "/").length();
        int startApi = requestURL.lastIndexOf(HATEOAS_API_PATH + "/");
        int endPackageName = requestURL.indexOf("/", startApi + apiLength);
        String odataQuery = requestURL.substring(endPackageName + 1);
        // Add owned by to the query as first parameter
        String qS = getRequest().getQueryString();
        if (qS != null) {
            qS = decode(getRequest().getQueryString(), UTF_8);
            // TODO: Find out why decode is not replacing %20
            qS = qS.replaceAll("%20", " ");
            qS = qS.replaceAll("%2B", "+");
        } else {
            qS = "";
        }

        StringBuilder queryString = new StringBuilder(qS);
        addOwnedBy(queryString);
        odataQuery += "?" + queryString;
        if (!additionalOdata.isBlank()) {
            odataQuery += " and " + additionalOdata;
        }
        String dmlStatementType = "";

        if (getRequest().getMethod().equalsIgnoreCase("delete")) {
            dmlStatementType = "delete";
        }
        return convertODataToHQL(odataQuery, dmlStatementType);
    }

    private void addOwnedBy(StringBuilder originalRequest) {
        int start = 0;
        if (originalRequest.length() > 0) {
            start = originalRequest.lastIndexOf("$filter=")
                    + "$filter=".length();
        }
        originalRequest.insert(start, "eier eq '" + getUser() + "' and ");
    }

    public QueryObject convertODataToHQL(String odataQuery,
                                         String dmlStatementType) {
        ODataLexer lexer = new ODataLexer(fromString(odataQuery));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ODataParser parser = new ODataParser(tokens);
        ParseTree tree = parser.odataRelativeUri();
        ParseTreeWalker walker = new ParseTreeWalker();
        // Make the HQL Statement
        ODataToHQL hqlWalker = new NikitaODataToHQL(dmlStatementType);
        walker.walk(hqlWalker, tree);
        return new QueryObject(hqlWalker.getHqlStatement(
                entityManager.unwrap(Session.class)), hqlWalker.getEntity());
    }

    // All helper methods

    /**
     * To avoid resource starvation a client is prohibited from running
     * SQL queries that produce large result sets. Rather pagination should
     * be used. The upper value for absoluteMaxResults is configurable in yml
     * property files and by default is set to 1000. This number is arbitrary.
     *
     * @param query the query object
     */
    private void checkMaxResultsChangeIfRequired(Query<INoarkEntity> query) {
        // Integer.MAX_VALUE is used by hibernate to indicate no value has
        // been set
        if (query.getMaxResults() == MAX_VALUE) {
            query.setMaxResults(maxResults);
        }
        if (query.getMaxResults() > absoluteMaxResults) {
            query.setMaxResults(absoluteMaxResults);
        }
    }

    /**
     * Find the total number of rows that this query contains. This is a
     * subtle hack where we run to the end of the cursor list and check how
     * many results there are. We note how many rows, then close the cursor
     * list and  the query can continue using maxResults (count) and
     * firstResult (offset). Note the efficiency of this approach will vary
     * between DBMS.
     * Note: if we don't "reset" the maxResults and firstResult values before
     * using the cursor approach, we will not have correct values for count.
     * Seeing as the API is stateless then a query will have to be called
     * multiple times. Hopefully the hibernate cache will take care of this
     * so there aren't performance issues.
     *
     * @param query the query object
     * @return the number rows that make up the count field
     */
    private int getCountFromQuery(Query<INoarkEntity> query) {
        // "reset" the maxResult value to ensure we get a proper count value
        int maxResult = query.getMaxResults();
        query.setMaxResults(Integer.MAX_VALUE);
        // "reset" the firstResult value to ensure we get a proper count value
        int firstResult = query.getFirstResult();
        query.setFirstResult(0);
        ScrollableResults cursor = query.scroll(SCROLL_INSENSITIVE);
        cursor.last();
        int totalRows = cursor.getRowNumber() == 0 ? 1 :
                cursor.getRowNumber() + 1;
        cursor.close();
        // Set the values for maxResult and offset back to what they were
        // initially so the results returned later will be correct
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResult);
        return totalRows;
    }

    private HttpServletRequest getRequest() {
        assert RequestContextHolder.getRequestAttributes() != null;
        return ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
    }

    /**
     * Set the outgoing ALLOW header
     */
    protected void setOutgoingRequestHeaderList() {
        HttpServletResponse response = ((ServletRequestAttributes)
                Objects.requireNonNull(
                        RequestContextHolder.getRequestAttributes()))
                .getResponse();
        HttpServletRequest request =
                ((ServletRequestAttributes)
                        RequestContextHolder
                                .getRequestAttributes()).getRequest();
        response.addHeader(ALLOW, getMethodsForRequestAsListOrThrow(
                request.getServletPath()));
    }

    protected String getUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
