package nikita.webapp.service.impl.odata;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.odata.NikitaODataToHQL;
import nikita.webapp.odata.NikitaObjectWorker;
import nikita.webapp.odata.ODataRefHandler;
import nikita.webapp.odata.ODataToHQL;
import nikita.webapp.odata.base.ODataLexer;
import nikita.webapp.odata.base.ODataParser;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.odata.IODataService;
import nikita.webapp.spring.ODataRedirectFilter;
import nikita.webapp.util.AddressComponent;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.net.URLDecoder.decode;
import static nikita.common.config.Constants.*;
import static nikita.common.config.ODataConstants.ODATA_DELETE_REF;
import static org.antlr.v4.runtime.CharStreams.fromString;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
public class ODataService
        implements IODataService {

    private final Logger logger =
            LoggerFactory.getLogger(ODataService.class);

    private final Pattern pairRegex = Pattern.compile("\\/" + REGEX_UUID + "\\/");
    private final EntityManager entityManager;
    private final AddressComponent address;
    private ODataRefHandler refHandler;
    private NikitaObjectWorker objectWorker;

    public ODataService(EntityManager entityManager,
                        AddressComponent address,
                        ODataRefHandler refHandler,
                        NikitaObjectWorker objectWorker) {
        this.entityManager = entityManager;
        this.address = address;
        this.refHandler = refHandler;
        this.objectWorker = objectWorker;
    }

    @Override
    public ResponseEntity<String> processODataQueryDelete
            (HttpServletRequest request) throws UnsupportedEncodingException {
        Query query = convertODataToHQL(request, "delete");
        int result = query.executeUpdate();
        logger.info(result + " records deleted for OData Query [" +
                query.getQueryString() + "]");
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    @Override
    public ResponseEntity<HateoasNoarkObject> processODataQueryGet
            (HttpServletRequest request) throws Exception {
        List<NoarkEntity> listResults =
                convertODataToHQL(request, "").getResultList();
        return packAsHateoasObject(listResults);
    }

    // Internal helper methods

    /**
     * @param list
     * @return
     * @throws Exception
     */
    private ResponseEntity<HateoasNoarkObject>
    packAsHateoasObject(List<NoarkEntity> list)
            throws Exception {
        HateoasNoarkObject noarkObject;
        HateoasHandler handler;
        if (list.size() > 0) {
            Class<? extends NoarkEntity> cls = list.get(0).getClass();
            HateoasPacker packer = cls.getAnnotation(HateoasPacker.class);
            HateoasObject hateoasObject = cls.getAnnotation(HateoasObject.class);
            noarkObject =
                    hateoasObject.using().getDeclaredConstructor(List.class)
                            .newInstance(list);
            handler = packer.using().getConstructor().newInstance();
            handler.setPublicAddress(address.getAddress());
            handler.setContextPath(address.getContextPath());
        } else {
            noarkObject =
                    new HateoasNoarkObject(new ArrayList<INoarkEntity>(),
                            "unknown");
            handler = new HateoasHandler();
            handler.setPublicAddress(address.getAddress());
            handler.setContextPath(address.getContextPath());
        }
        handler.addLinks(noarkObject, new Authorisation());
        return ResponseEntity.status(OK)
                .body(noarkObject);
    }

    /**
     * Convert OData query parameters to a HQL Query.
     * <p>
     * Note, this appends an and to the OData query identifying the logged in
     * user
     *
     * @param request
     * @param dmlStatementType value delete, if HQL statement is delete,
     *                         otherwise null
     * @return
     * @throws UnsupportedEncodingException
     */
    private Query convertODataToHQL(@NotNull HttpServletRequest request,
                                    String dmlStatementType)
            throws UnsupportedEncodingException {

        StringBuffer originalRequest = request.getRequestURL();
        originalRequest.append("?");
        originalRequest.append(decode(request.getQueryString(), StandardCharsets.UTF_8));
        // Add owned by to the query as first parameter
        addOwnedBy(originalRequest);
        int start = originalRequest.indexOf("api/");
        start += "api/".length();
        String odataCommand = originalRequest.substring(start);
        return convertODataToHQL(odataCommand, dmlStatementType);
    }

    private void addOwnedBy(StringBuffer originalRequest) {
        int start =
                originalRequest.lastIndexOf("$filter=") + "$filter=".length();
        originalRequest.insert(start, " eier eq '" + getUser() + "' and ");
    }

    @Override
    public ResponseEntity<HateoasNoarkObject> processODataRefRequestUpdate
            (HttpServletRequest request) throws UnsupportedEncodingException {
        String url = ((ODataRedirectFilter.ODataFilteredRequest) request).
                getURLSanitised();
        String queryString = request.getQueryString();
        logger.info("Request has following query string: " + queryString);
        logger.info("Request has following URL: " + url);
        return null;
    }

    @Override
    public ResponseEntity<HateoasNoarkObject> processODataRefRequestCreate
            (HttpServletRequest request) throws UnsupportedEncodingException {

        String url = decode(((ODataRedirectFilter.ODataFilteredRequest) request).
                getURLSanitised(), StandardCharsets.UTF_8);
        String queryString = request.getQueryString();
        logger.info("Request has following query string: " + queryString);
        logger.info("Request has following URL: " + url);
/*
        Ref ref = retrieveRefValues(url + "?" + DOLLAR_ID + "=" + queryString);
        NoarkEntity fromEntity = refHandler.getFromEntity(ref);
        NoarkEntity toEntity = refHandler.getToEntity(ref);
        objectWorker.handleCreateReference(fromEntity, toEntity, ref
                .getEntity());
        logger.info(ref.toString());
 */
        return null;
    }

    @Override
    public ResponseEntity<HateoasNoarkObject> processODataRefRequestDelete
            (HttpServletRequest request) throws UnsupportedEncodingException {

        String url = decode(((ODataRedirectFilter.ODataFilteredRequest) request).
                getURLSanitised(), StandardCharsets.UTF_8);
        String queryString = request.getQueryString();
        logger.info("Request has following query string: " + queryString);
        logger.info("Request has following URL: " + url);
        Query query = convertODataRefToHQL(url + "?id=" + queryString,
                ODATA_DELETE_REF);
        return null;
    }

    public Query convertODataToHQL(String request, String dmlStatementType) {

        if (request.startsWith(NOARK_FONDS_STRUCTURE_PATH + "/")) {
            request = request.substring(
                    (NOARK_FONDS_STRUCTURE_PATH + "/").length());
        }
        if (request.startsWith(NOARK_CASE_HANDLING_PATH + "/")) {
            request = request.substring(
                    (NOARK_CASE_HANDLING_PATH + "/").length());
        }

        ODataLexer lexer = new ODataLexer(fromString(request));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ODataParser parser = new ODataParser(tokens);
        ParseTree tree = parser.odataRelativeUri();
        ParseTreeWalker walker = new ParseTreeWalker();
        // Make the HQL Statement
        ODataToHQL hqlWalker = new NikitaODataToHQL(dmlStatementType);
        walker.walk(hqlWalker, tree);
        return hqlWalker.getHqlStatement(entityManager.unwrap(Session.class));
    }

    private ODataToHQL getWalker(String request) {
        CommonTokenStream tokens =
                new CommonTokenStream(new ODataLexer(fromString(request)));
        ODataParser parser = new ODataParser(tokens);
        ParseTreeWalker walker = new ParseTreeWalker();
        // Make the HQL Statement
        ODataToHQL hqlWalker = new ODataToHQL();
        walker.walk(hqlWalker, parser.odataRelativeUri());
        return hqlWalker;
    }

    public Query convertODataRefToHQL(String request, String dmlStatementType) {
        CommonTokenStream tokens =
                new CommonTokenStream(new ODataLexer(fromString(request)));
        ODataParser parser = new ODataParser(tokens);
        ParseTreeWalker walker = new ParseTreeWalker();

        // Make the HQL Statement
        ODataToHQL hqlWalker = new
                ODataToHQL(dmlStatementType);
        walker.walk(hqlWalker, parser.odataRelativeUri());

        Matcher matcher = pairRegex.matcher(request);
        // This is a OData query on a child e.g.
        // arkivdel/8628ae60-7d50-474e-82e0-74a348a99647/mappe?
        if (matcher.find()) {
            //hqlWalker.setParentIdPrimaryKey(matcher.group());
        }
        return hqlWalker.getHqlStatement(entityManager.unwrap(Session.class));
    }

    protected String getUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
