package nikita.webapp.service.impl.odata;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.odata.NikitaODataToHQL;
import nikita.webapp.odata.ODataToHQL;
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
import java.util.ArrayList;
import java.util.List;

import static java.net.URLDecoder.decode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static nikita.common.config.Constants.DELETE_RESPONSE;
import static nikita.common.config.Constants.HATEOAS_API_PATH;
import static org.antlr.v4.runtime.CharStreams.fromString;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
public class ODataService
        implements IODataService {

    private final Logger logger =
            LoggerFactory.getLogger(ODataService.class);
    private final EntityManager entityManager;
    private final AddressComponent address;

    public ODataService(EntityManager entityManager,
                        AddressComponent address) {
        this.entityManager = entityManager;
        this.address = address;
    }

    @Override
    public ResponseEntity<String> processODataQueryDelete
            (HttpServletRequest request) {
        Query query = convertODataToHQL(request, "delete");
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
        List<NoarkEntity> listResults =
                convertODataToHQL(request, "").getResultList();
        return packAsHateoasObject(listResults);
    }

    // Internal helper methods
    private ResponseEntity<HateoasNoarkObject> packAsHateoasObject(
            List<NoarkEntity> list) throws Exception {
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
                    new HateoasNoarkObject(new ArrayList<>(),
                            "unknown");
            handler = new HateoasHandler();
            handler.setPublicAddress(address.getAddress());
            handler.setContextPath(address.getContextPath());
        }
        handler.addLinks(noarkObject, new Authorisation());
        return ResponseEntity.status(OK)
                .body(noarkObject);
    }

    private Query convertODataToHQL(@NotNull HttpServletRequest request,
                                    String dmlStatementType) {
        StringBuffer requestURL = request.getRequestURL();
        StringBuilder queryString = new StringBuilder(
                decode(request.getQueryString(), UTF_8));
        //  remove everything up to api/packagename/
        int apiLength = (HATEOAS_API_PATH + "/").length();
        int startApi = requestURL.lastIndexOf(HATEOAS_API_PATH + "/");
        int endPackageName = requestURL.indexOf("/", startApi + apiLength);
        String odataQuery = requestURL.substring(endPackageName + 1);
        // Add owned by to the query as first parameter
        addOwnedBy(queryString);
        odataQuery += "?" + queryString;
        return convertODataToHQL(odataQuery, dmlStatementType);
    }

    private void addOwnedBy(StringBuilder originalRequest) {
        int start =
                originalRequest.lastIndexOf("$filter=") + "$filter=".length();
        originalRequest.insert(start, "eier eq '" + getUser() + "' and ");
    }

    public Query convertODataToHQL(String request, String dmlStatementType) {
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

    protected String getUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
