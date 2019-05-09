package nikita.webapp.service.impl;

import nikita.common.model.noark5.v4.NoarkEntity;
import nikita.common.model.noark5.v4.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.webapp.odata.NikitaODataToHQLWalker;
import nikita.webapp.odata.ODataLexer;
import nikita.webapp.odata.ODataParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.List;


public class NoarkService {

    private static final Logger logger =
            LoggerFactory.getLogger(NoarkService.class);

    protected EntityManager entityManager;

    public NoarkService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected NikitaODataToHQLWalker getHQLFromODataString(
            CharStream oDataString) {
        ODataLexer lexer = new ODataLexer(oDataString);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ODataParser parser = new ODataParser(tokens);
        ParseTree tree = parser.odataURL();
        ParseTreeWalker walker = new ParseTreeWalker();

        // Make the HQL Statement
        NikitaODataToHQLWalker hqlWalker = new NikitaODataToHQLWalker();
        walker.walk(hqlWalker, tree);
        return hqlWalker;
    }

    protected List<NoarkEntity> executeHQL(NikitaODataToHQLWalker hqlWalker) {
        Session session = entityManager.unwrap(Session.class);
        Query query = hqlWalker.getHqlStatment(session);
        String queryString = query.getQueryString();
        logger.info("Executing query " + queryString);
        return query.getResultList();
    }

    protected HateoasNoarkObject packResults(List<INikitaEntity> caseFileList,
                                             String outgoingAddress) {
        logger.error("Default HateoasNoarkObject packResults called. ");
        return null;
    }
}
