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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.List;

import static nikita.common.config.N5ResourceMappings.*;
import static org.apache.commons.lang3.CharEncoding.UTF_8;


@RestController
@RequestMapping(value = "odata")
public class OdataTest {


    private EntityManager entityManager;

    public OdataTest(EntityManager entityManager) {
        this.entityManager = entityManager;
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

        HateoasNoarkObject noarkObject;
        HateoasHandler handler;

        // Yes it's ugly but it's temporary code until I figure out to
        // do reflection here, or something smarter

        switch (entity) {
            case FONDS:
                noarkObject =
                        new FondsHateoas((List<INikitaEntity>) (List) list);
                handler = new FondsHateoasHandler();
                break;
            case SERIES:
                noarkObject =
                        new SeriesHateoas((List<INikitaEntity>) (List) list);
                handler = new SeriesHateoasHandler();
                break;
            case CLASSIFICATION_SYSTEM:
                noarkObject =
                        new ClassificationSystemHateoas((List<INikitaEntity>)
                                (List) list);
                handler = new ClassificationSystemHateoasHandler();
                break;
            case CLASS:
                noarkObject =
                        new ClassHateoas((List<INikitaEntity>) (List) list);
                handler = new ClassHateoasHandler();
                break;
            case FILE:
                noarkObject =
                        new FileHateoas((List<INikitaEntity>) (List) list);
                handler = new FileHateoasHandler();
                break;
            case REGISTRATION:
                noarkObject =
                        new RecordHateoas((List<INikitaEntity>) (List) list);
                handler = new RecordHateoasHandler();
                break;
            case DOCUMENT_DESCRIPTION:
                noarkObject =
                        new DocumentDescriptionHateoas((List<INikitaEntity>)
                                (List) list);
                handler = new DocumentDescriptionHateoasHandler();
                break;
            case DOCUMENT_OBJECT:
                noarkObject =
                        new DocumentObjectHateoas((List<INikitaEntity>)
                                (List) list);
                handler = new DocumentObjectHateoasHandler();
                break;
            default:
                noarkObject =
                        new HateoasNoarkObject((List<INikitaEntity>)
                                (List) list, entity);
                handler = new HateoasHandler();
                break;
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
