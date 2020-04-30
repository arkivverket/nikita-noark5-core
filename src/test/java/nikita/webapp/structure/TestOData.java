package nikita.webapp.structure;

import nikita.webapp.service.impl.odata.ODataService;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestOData {

    private final String EXAMPLE_FONDS_TITLE = "Example fonds title";
    private final String SQL_NOT_EQUAL_HQL = "from Fonds x where x.title != '"
            + EXAMPLE_FONDS_TITLE + "'";
    private final String SQL_EQUAL_HQL = "from Fonds x where x.title = '"
            + EXAMPLE_FONDS_TITLE + "'";
    private final String FILTER_EQ = "arkivstruktur/arkiv?$filter=tittel eq '"
            + EXAMPLE_FONDS_TITLE + "'";
    private final String FILTER_NEQ = "arkivstruktur/arkiv?$filter=tittel ne '"
            + EXAMPLE_FONDS_TITLE + "'";
    @Autowired
    ODataService oDataService;
    @Autowired
    private EntityManager emf;
    private SessionFactory sessionFactory;

    @BeforeEach
    public void init() {
        System.out.println("in test " + emf.toString());
        sessionFactory = emf.getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        System.out.println("in test " + sessionFactory.toString());
    }

    @Test
    public void shouldReturnValidHQLEQQueryString() {
        assertEquals(oDataService.convertODataToHQL(FILTER_NEQ, "")
                .getQueryString(), SQL_NOT_EQUAL_HQL);
    }

    @Test
    public void shouldReturnValidHQLNotEQQueryString() {
        assertEquals(oDataService.convertODataToHQL(FILTER_EQ, "")
                .getQueryString(), SQL_EQUAL_HQL);
    }

    /**
     * Check that it is possible to to do a contains query with a quoted string
     * <p>
     * ODATA Input:
     * arkivstruktur/arkiv?$filter=contains(tittel, 'Example fonds title')";
     * Expected HQL:
     * from Fonds x where x.title like :containsParameter_0
     * Additionally the parameter value should be
     * Example fonds title
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLContainsQuotedString() {
        String FILTER_CONTAINS = "arkivstruktur/arkiv?$filter=contains(tittel, '" +
                EXAMPLE_FONDS_TITLE + "')";
        String SQL_EQUAL_HQL = "from Fonds x where x.title like :containsParameter_0";
        Query query = oDataService.convertODataToHQL(FILTER_CONTAINS, "");
        String parameter = (String) query.getParameterValue("containsParameter_0");
        assertEquals(query.getQueryString(), SQL_EQUAL_HQL);
        assertEquals(parameter, "%" + EXAMPLE_FONDS_TITLE + "%");
    }

    /**
     * Check that it is possible to to do a contains query with a string
     * <p>
     * ODATA Input:
     * arkivstruktur/arkiv?$filter=contains(tittel, Example)";
     * Expected HQL:
     * from Fonds x where x.title like :containsParameter_0
     * Additionally the parameter value should be
     * Example
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLContainsString() {
        String FILTER_CONTAINS = "arkivstruktur/arkiv?$filter=contains(tittel, "
                + "Example)";
        String SQL_EQUAL_HQL = "from Fonds x where x.title like :containsParameter_0";
        Query query = oDataService.convertODataToHQL(FILTER_CONTAINS, "");
        String parameter = (String) query.getParameterValue("containsParameter_0");
        assertEquals(query.getQueryString(), SQL_EQUAL_HQL);
        assertEquals(parameter, "%Example%");
    }

    /**
     * Check that it is possible to to do a contains query with a string
     * <p>
     * ODATA Input:
     * arkivstruktur/arkiv?$filter=contains(tittel, Example)";
     * Expected HQL:
     * from Fonds x where x.title like :containsParameter_0
     * Additionally the parameter value should be
     * Example
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLContainsQuotedStringWithEscapedQuote() {
        String FILTER_CONTAINS = "arkivstruktur/arkiv?$filter=contains(tittel, "
                + "'Jennifer O''Malley')";
        String SQL_EQUAL_HQL = "from Fonds x where x.title like :containsParameter_0";
        Query query = oDataService.convertODataToHQL(FILTER_CONTAINS, "");
        String parameter = (String) query.getParameterValue("containsParameter_0");
        assertEquals(query.getQueryString(), SQL_EQUAL_HQL);
        assertEquals(parameter, "%Jennifer O'Malley%");
    }

    /**
     * Show problem with lexer
     * <p>
     * ODATA Input:
     * arkivstruktur/saksmappe/?$filter=saksaar eq 2020
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEQInteger() {
        String yearQuery = "arkivstruktur/saksmappe?$filter=saksaar eq 2020";
        Query query = oDataService.convertODataToHQL(yearQuery, "");
        String hqlEQ = "from CaseFile x where x.caseYear = 2020";
        assertEquals(query.getQueryString(), hqlEQ);
    }
}