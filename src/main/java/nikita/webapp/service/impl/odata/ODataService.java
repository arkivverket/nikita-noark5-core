package nikita.webapp.service.impl.odata;

import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.webapp.hateoas.EmptyListHateoasHandler;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.odata.NikitaODataToHQL;
import nikita.webapp.odata.ODataToHQL;
import nikita.webapp.odata.QueryObject;
import nikita.webapp.odata.base.ODataLexer;
import nikita.webapp.odata.base.ODataParser;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.odata.IODataService;
import nikita.webapp.util.AddressComponent;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.MAX_VALUE;
import static java.net.URLDecoder.decode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static nikita.common.config.Constants.DELETE_RESPONSE;
import static nikita.common.config.Constants.HATEOAS_API_PATH;
import static nikita.common.util.CommonUtils.FileUtils.getClassFromName;
import static org.antlr.v4.runtime.CharStreams.fromString;
import static org.hibernate.ScrollMode.SCROLL_INSENSITIVE;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Service
public class ODataService
        extends NoarkService
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
    private int maxResults;

    /**
     * To prevent resource starvation set an absolute maximum size for a
     * query. If e.g., a client calls a request with $top=90000 then use the
     * value set from yml file. If yml value missing fall back to 1000
     */
    @Value("${nikita.application.pagination.absolute-max-page-size:1000}")
    private int absoluteMaxResults;

    public ODataService(EntityManager entityManager,
                        ApplicationEventPublisher applicationEventPublisher,
                        IPatchService patchService,
                        AddressComponent address) {
        super(entityManager, applicationEventPublisher, patchService);
        this.entityManager = entityManager;
        this.address = address;
    }

    @Override
    @Transactional
    public ResponseEntity<String> processODataQueryDelete
            (HttpServletRequest request) {
        QueryObject queryObject = convertODataToHQL(request, "delete");
        Query<INoarkEntity> query = queryObject.getQuery();
        int result = query.executeUpdate();
        logger.info(result + " records deleted for OData Query [" +
                query.getQueryString() + "]");
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResponseEntity<HateoasNoarkObject> processODataQueryGet
            (HttpServletRequest request) throws Exception {
        QueryObject queryObject = convertODataToHQL(request, "");
        String fromEntity = queryObject.getFromEntity();
        Query<INoarkEntity> query = queryObject.getQuery();

        int totalRows = getCountFromQuery(query);
        checkMaxResultsChangeIfRequired(query);
        int top = query.getMaxResults();
        int skip = query.getFirstResult();
        // Execute the SELECT query and return the query results
        List<INoarkEntity> entityList = query.getResultList();
        NikitaPage page = new NikitaPage(entityList, totalRows, top, skip);
        HateoasNoarkObject noarkObject;
        HateoasHandler handler;

        if (entityList.size() > 0) {
            Class<INoarkEntity> cls = getClassFromName(fromEntity);
            HateoasPacker packer = cls.getAnnotation(HateoasPacker.class);
            HateoasObject hateoasObject = cls.getAnnotation(HateoasObject.class);
            // Yes, this is an ugly approach to finding out if a Hateoas
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
            } else {
                noarkObject = hateoasObject.using()
                        .getDeclaredConstructor(List.class)
                        .newInstance(entityList);
            }
            handler = packer.using().getConstructor().newInstance();
        } else {
            noarkObject = new HateoasNoarkObject(new ArrayList<>(),
                    "unknown");
            handler = new EmptyListHateoasHandler();
        }
        handler.setPublicAddress(address.getAddress());
        handler.setContextPath(address.getContextPath());
        handler.addLinks(noarkObject, new Authorisation());
        return ResponseEntity.status(OK)
                .body(noarkObject);
    }

    private QueryObject convertODataToHQL(@NotNull HttpServletRequest request,
                                          String dmlStatementType) {
        StringBuffer requestURL = request.getRequestURL();
        //  remove everything up to api/packagename/
        int apiLength = (HATEOAS_API_PATH + "/").length();
        int startApi = requestURL.lastIndexOf(HATEOAS_API_PATH + "/");
        int endPackageName = requestURL.indexOf("/", startApi + apiLength);
        String odataQuery = requestURL.substring(endPackageName + 1);
        // Add owned by to the query as first parameter
        String qS = decode(request.getQueryString(), UTF_8);
        // TODO: Find out why decode is not working
        qS = qS.replaceAll("%20", " ");
        qS = qS.replaceAll("%2B", "+");
        StringBuilder queryString = new StringBuilder(qS);
        addOwnedBy(queryString);
        odataQuery += "?" + queryString;
        return convertODataToHQL(odataQuery, dmlStatementType);
    }

    private void addOwnedBy(StringBuilder originalRequest) {
        int start =
                originalRequest.lastIndexOf("$filter=") + "$filter=".length();
        originalRequest.insert(start, "eier eq '" + getUser() + "' and ");
    }

    public QueryObject convertODataToHQL(String request, String dmlStatementType) {
        ODataLexer lexer = new ODataLexer(fromString(request));
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
}
