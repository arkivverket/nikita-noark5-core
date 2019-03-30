package nikita.webapp.web.controller.hateoas.odata;

import nikita.common.model.noark5.v4.NoarkEntity;
import nikita.common.model.noark5.v4.hateoas.*;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.webapp.hateoas.*;
import nikita.webapp.odata.NikitaODataToHQLWalker;
import nikita.webapp.odata.ODataLexer;
import nikita.webapp.odata.ODataParser;
import nikita.webapp.security.Authorisation;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.util.List;

import static nikita.common.config.N5ResourceMappings.*;
import static org.apache.commons.lang3.CharEncoding.UTF_8;


@RestController
@RequestMapping(value = "odata")
public class OdataTest {


    private EntityManager entityManager;
    private ConfigurableApplicationContext context;

    public OdataTest(EntityManager entityManager,
                     ConfigurableApplicationContext context) {
        this.entityManager = entityManager;
        this.context = context;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "arkivstruktur/{\\w*}"
//            value = "{\\w*}"
    )

    @SuppressWarnings("unchecked")
    public ResponseEntity<HateoasNoarkObject> testOdata(HttpServletRequest request)
            throws Exception {

        String uqueryString = request.getQueryString();
        String decoded = URLDecoder.decode(uqueryString, UTF_8);

        String entity = findEntity(request.getRequestURL());
        StringBuffer originalRequest = request.getRequestURL();
        originalRequest.append("?");
        originalRequest.append(decoded);
        CharStream stream = CharStreams.fromString(originalRequest.toString());

        ODataLexer lexer = new ODataLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ODataParser parser = new ODataParser(tokens);
        ParseTree tree = parser.odataURL();
        ParseTreeWalker walker = new ParseTreeWalker();

        // Make the HQL Statement
        NikitaODataToHQLWalker hqlWalker = new NikitaODataToHQLWalker();
        walker.walk(hqlWalker, tree);

        Session session = entityManager.unwrap(org.hibernate.Session.class);
        Query query = hqlWalker.getHqlStatment(session);
        String queryString = query.getQueryString();
        System.out.println(queryString);
        List<NoarkEntity> list = query.getResultList();

        // Yes it's ugly but it's temporary code until I figure out to
        // do reflection here, or something smarter

        String address = request.getHeader("X-Forwarded-Host");
        String protocol = request.getHeader("X-Forwarded-Proto");
        String contextPath;

        if (address != null && protocol != null) {
            contextPath = protocol + "://" + address + "/";
        } else {
            Environment env = context.getEnvironment();
            contextPath = "http://" + InetAddress.getLocalHost().getHostAddress();
            if (env.getProperty("server.port") != null) {
                contextPath += ":" + env.getProperty("server.port");
            }
            contextPath += env.getProperty("server.servlet.context-path");
            contextPath += "/";
        }
        HateoasNoarkObject noarkObject;
        HateoasHandler handler = new HateoasHandler();
        if (entity.equals(FONDS)) {
            noarkObject =
                    new FondsHateoas((List<INikitaEntity>) (List) list);
            handler = new FondsHateoasHandler(contextPath);
        } else if (entity.equals(SERIES)) {
            noarkObject =
                    new SeriesHateoas((List<INikitaEntity>) (List) list);
            handler = new SeriesHateoasHandler(contextPath);
        } else if (entity.equals(CLASSIFICATION_SYSTEM)) {
            noarkObject =
                    new ClassificationSystemHateoas((List<INikitaEntity>) (List) list);
            handler = new ClassificationSystemHateoasHandler(contextPath);
        } else if (entity.equals(CLASS)) {
            noarkObject =
                    new ClassHateoas((List<INikitaEntity>) (List) list);
            handler = new ClassHateoasHandler(contextPath);
        } else if (entity.equals(FILE)) {
            noarkObject =
                    new FileHateoas((List<INikitaEntity>) (List) list);
            handler = new FileHateoasHandler(contextPath);
        } else if (entity.equals(REGISTRATION)) {
            noarkObject =
                    new RecordHateoas((List<INikitaEntity>) (List) list);
            handler = new RecordHateoasHandler(contextPath);
        } else if (entity.equals(DOCUMENT_DESCRIPTION)) {
            noarkObject =
                    new DocumentDescriptionHateoas((List<INikitaEntity>) (List) list);
            handler = new DocumentDescriptionHateoasHandler(contextPath);
        } else if (entity.equals(DOCUMENT_OBJECT)) {
            noarkObject =
                    new DocumentObjectHateoas((List<INikitaEntity>) (List) list);
            handler = new DocumentObjectHateoasHandler(contextPath);
        } else {
            noarkObject =
                    new HateoasNoarkObject((List<INikitaEntity>) (List) list, entity);
            handler = new HateoasHandler(contextPath);
        }

        handler.addLinks(noarkObject, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .body(noarkObject);
    }

    private String findEntity(StringBuffer url) {
        int lastSlash = url.lastIndexOf("/");
        return url.substring(lastSlash + 1);
    }
}
