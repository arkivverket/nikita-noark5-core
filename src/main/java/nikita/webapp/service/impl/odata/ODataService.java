package nikita.webapp.service.impl.odata;

import nikita.common.model.nikita.Count;
import nikita.common.model.noark5.v4.NoarkEntity;
import nikita.common.model.noark5.v4.NoarkGeneralEntity;
import nikita.common.model.noark5.v4.hateoas.HateoasNoarkObject;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.odata.NikitaODataToHQLWalker;
import nikita.webapp.odata.ODataLexer;
import nikita.webapp.odata.ODataParser;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.odata.IODataService;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import nikita.webapp.web.controller.hateoas.odata.ODataController;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nikita.common.config.Constants.REGEX_UUID;
import static org.apache.commons.lang3.CharEncoding.UTF_8;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
public class ODataService
        implements IODataService {

    private final Logger logger =
            LoggerFactory.getLogger(ODataController.class);
    private final Pattern pairRegex = Pattern.compile(REGEX_UUID);

    private final EntityManager entityManager;

    public ODataService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public ResponseEntity<Count> processODataQueryDelete
            (HttpServletRequest request) throws UnsupportedEncodingException {
        Query query = convertODataToHQL(request, "delete");
        int result = query.executeUpdate();
        logger.info(result + " records deleted for OData Query [" +
                query.getQueryString() + "]");
        return ResponseEntity.status(NO_CONTENT)
                .body(new Count(result));
    }

    @Override
    public ResponseEntity<HateoasNoarkObject> processODataQueryGet
            (HttpServletRequest request) throws Exception {
        Query query = convertODataToHQL(request, null);
        return packAsHateoasObject(query.getResultList());
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
        } else {
            noarkObject = new HateoasNoarkObject();
            handler = new HateoasHandler();
        }

        handler.addLinks(noarkObject, new Authorisation());
        return ResponseEntity.status(OK)
                .body(noarkObject);
    }

    /**
     * Convert OData query parameters to a HQL Query.
     *
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    private Query convertODataToHQL(@NotNull HttpServletRequest request,
                                    String command)
            throws UnsupportedEncodingException {

        StringBuffer originalRequest = request.getRequestURL();
        originalRequest.append("?");
        originalRequest.append(
                URLDecoder.decode(request.getQueryString(), UTF_8));

        ODataLexer lexer = new ODataLexer(
                CharStreams.fromString(originalRequest.toString()));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ODataParser parser = new ODataParser(tokens);
        ParseTree tree = parser.odataURL();
        ParseTreeWalker walker = new ParseTreeWalker();

        // Make the HQL Statement
        NikitaODataToHQLWalker hqlWalker = new NikitaODataToHQLWalker(command);
        walker.walk(hqlWalker, tree);

        Matcher matcher = pairRegex.matcher(originalRequest);
        // This is a OData query on a child e.g.
        // arkivdel/8628ae60-7d50-474e-82e0-74a348a99647/mappe?
        if (matcher.find()) {
            hqlWalker.setParentIdPrimaryKey(matcher.group());
        }

        return hqlWalker.getHqlStatment(entityManager.unwrap(Session.class));
    }
}
