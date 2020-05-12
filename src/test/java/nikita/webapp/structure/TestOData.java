package nikita.webapp.structure;

import nikita.webapp.service.impl.odata.ODataService;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;

/**
 * Test OData queries that are supported
 * <p>
 * The following OData queries are tested here:
 * <p>
 * Comparison Operators:
 * eq:
 * ne:
 * gt:
 * ge:
 * lt:
 * le:
 * Built-in Query Functions:
 * Date and Time Functions:
 * month
 * day
 * year
 * Join queries:
 * <p>
 * The following have not yet been implemented/tested:
 * Built-in Query Functions:
 * String and Collection Functions:
 * String Functions:
 * Date and Time Functions:
 * Arithmetic Functions:
 * Geo Functions:
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestOData {

    @Autowired
    ODataService oDataService;

    @Autowired
    private EntityManager emf;

    /**
     * Check that it is possible to do a eq query with a quoted string
     * <p>
     * Entity: arkiv
     * Attribute: tittel
     * ODATA Input:
     * arkivstruktur/arkiv?$filter=tittel eq 'The fonds'
     * <p>
     * Expected HQL:
     * FROM Fonds AS fonds_1
     * WHERE
     *   fonds_1.title = :comparisonParameter_0
     * <p>
     * Additionally the comparisonParameter_0 parameter value should be:
     *   The fonds
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEQQueryString() {
        String yearQuery = "arkivstruktur/arkiv?$filter=tittel eq 'The fonds'";
        Query query = oDataService.convertODataToHQL(yearQuery, "");
        String hql = "FROM Fonds AS fonds_1 WHERE fonds_1.title = " +
                ":comparisonParameter_0";
        assertEquals(query.getParameterValue("comparisonParameter_0"),
                "The fonds");
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a eq query with a quoted string. This
     * test is here to remind us that it creates a warning in the logfile that
     * should be addressed.
     * <p>
     * Entity: arkiv
     * Attribute: tittel
     * ODATA Input:
     * arkivstruktur/dokumentobjekt?$filter=filnavn eq '<9aqr221f34c.hsr@diskless
     * .uio
     * .no>'
     * <p>
     * Expected HQL:
     * FROM DocumentObject AS documentobject_1
     * WHERE
     * documentobject_1.originalFilename = :comparisonParameter_0
     * <p>
     * Additionally the comparisonParameter_0 parameter value should be:
     * <9aqr221f34c.hsr@diskless.uio.no>
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEQQueryStringWithChars() {
        String yearQuery = "arkivstruktur/dokumentobjekt?$filter=filnavn eq " +
                "'<9aqr221f34c.hsr@diskless.uio.no>'";
        Query query = oDataService.convertODataToHQL(yearQuery, "");
        String hql = "FROM DocumentObject AS documentobject_1 " +
                "WHERE " +
                "documentobject_1.originalFilename = :comparisonParameter_0";
        assertEquals(query.getParameterValue("comparisonParameter_0"),
                "<9aqr221f34c.hsr@address.udn.com>");
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a eq query with a quoted string and
     * nested join
     * Entity: arkivdel->arkiv
     * Attribute: arkiv.beskrivelse
     * <p>
     * ODATA Input:
     * arkivstruktur/arkivdel?$filter=arkiv/beskrivelse eq 'The fonds
     * description'
     * <p>
     * Expected HQL:
     * FROM Series AS series_1
     *   JOIN
     *     series_1.referenceFonds AS fonds_1
     * WHERE fonds_1.description = :comparisonParameter_0
     * <p>
     * Additionally the comparisonParameter_0 parameter value should be
     *    The fonds description
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEQQueryStringWithJON() {
        String yearQuery = "arkivstruktur/arkivdel?$filter=arkiv/beskrivelse " +
                "eq 'The fonds description'";
        Query query = oDataService.convertODataToHQL(yearQuery, "");
        String hql = "FROM Series AS series_1 JOIN series_1.referenceFonds " +
                "AS fonds_1 WHERE fonds_1.description = :comparisonParameter_0";
        assertEquals(query.getParameterValue("comparisonParameter_0"),
                "The fonds description");
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a ne query with a quoted string.
     * Entity: journalpost
     * Attribute: registreringsID
     * <p>
     * ODATA Input:
     * arkivstruktur/journalpost?$filter=registreringsID ne '2020/000234-23'
     * <p>
     * Expected HQL:
     * FROM RegistryEntry AS registryentry_1
     * WHERE registryentry_1.recordId != :comparisonParameter_0
     * <p>
     * Additionally the comparisonParameter_0 parameter value should be:
     *   2020/000234-23
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLNotEQQueryString() {
        String yearQuery = "arkivstruktur/journalpost?$filter=registreringsID " +
                "ne '2020/000234-23'";
        Query query = oDataService.convertODataToHQL(yearQuery, "");
        String hql = "FROM RegistryEntry AS registryentry_1 WHERE " +
                "registryentry_1.recordId != :comparisonParameter_0";
        assertEquals(query.getParameterValue("comparisonParameter_0"),
                "2020/000234-23");
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a year function query with a date
     * Entity: dokumentobjekt
     * Attribute: opprettetDato
     * <p>
     * ODATA Input:
     * arkivstruktur/dokumentobjekt?$filter=year(opprettetDato) eq 2020
     * <p>
     * Expected HQL:
     * FROM RegistryEntry AS registryentry_1
     * WHERE registryentry_1.recordId != :comparisonParameter_0
     * <p>
     * Additionally the comparisonParameter_0 parameter value should be:
     *   2020/000234-23
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLYearComparison() {
        String yearQuery = "arkivstruktur/dokumentobjekt?$filter=" +
                "year(opprettetDato) eq 2020";
        Query query = oDataService.convertODataToHQL(yearQuery, "");
        String hql = "FROM DocumentObject AS documentobject_1 WHERE year" +
                "(documentobject_1.createdDate) = :comparisonParameter_0";
        assertEquals(query.getParameterValue("comparisonParameter_0"),
                2020);
        assertEquals(query.getQueryString(), hql);
    }


    /**
     * Check that it is possible to do a month function query with a date
     * Entity: dokumentobjekt
     * Attribute: opprettetDato
     * <p>
     * ODATA Input:
     * arkivstruktur/dokumentobjekt?$filter=month(opprettetDato) gt 5 and
     * month(opprettetDato) lt 9
     * <p>
     * Expected HQL:
     * FROM DocumentObject AS documentobject_1
     * WHERE
     *   month(documentobject_1.createdDate) > :comparisonParameter_0 and
     *   month(documentobject_1.createdDate) < :comparisonParameter_1
     * <p>
     * Additionally.
     * The comparisonParameter_0 parameter value should be:
     *   5
     * The comparisonParameter_1 parameter value should be:
     *   9
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLMonthComparison() {
        String yearQuery = "arkivstruktur/dokumentobjekt?$filter=" +
                "month(opprettetDato) gt 5 and month(opprettetDato) lt 9";
        Query query = oDataService.convertODataToHQL(yearQuery, "");
        String hql = "FROM DocumentObject AS documentobject_1 WHERE " +
                "month(documentobject_1.createdDate) > :comparisonParameter_0" +
                " and " +
                "month(documentobject_1.createdDate) < :comparisonParameter_1";
        assertEquals(query.getParameterValue("comparisonParameter_0"), 5);
        assertEquals(query.getParameterValue("comparisonParameter_1"), 9);
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a month function query with a date
     * using a nested join
     * Entity: dokumentobjekt, dokumentbeskrivelse
     * Attribute: dokumentobjekt.opprettetDato, dokumentbeskrivelse.opprettetDato
     * <p>
     * ODATA Input:
     * arkivstruktur/dokumentobjekt?$filter=month(opprettetDato) gt 5 and
     * month(opprettetDato) lt 9
     * <p>
     * Expected HQL:
     * FROM DocumentObject AS documentobject_1
     *   JOIN
     *      documentobject_1.referenceDocumentDescription AS documentdescription_1
     * WHERE
     *   month(documentdescription_1.createdDate) > :comparisonParameter_0 and
     *   month(documentobject_1.createdDate) < :comparisonParameter_1
     * <p>
     * Additionally.
     * The comparisonParameter_0 parameter value should be:
     *   5
     * The comparisonParameter_1 parameter value should be:
     *   9
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLMonthComparisonWithJoin() {
        String yearQuery = "arkivstruktur/dokumentobjekt?$filter=" +
                "month(dokumentbeskrivelse/opprettetDato) gt 5 and " +
                "month(opprettetDato) le 9";
        Query query = oDataService.convertODataToHQL(yearQuery, "");
        String hql = "FROM DocumentObject AS documentobject_1 JOIN " +
                "documentobject_1.referenceDocumentDescription AS " +
                "documentdescription_1 WHERE " +
                "month(documentdescription_1.createdDate) " +
                "> :comparisonParameter_0 and " +
                "month(documentobject_1.createdDate) " +
                "<= :comparisonParameter_1";
        assertEquals(query.getQueryString(), hql);
        assertEquals(query.getParameterValue("comparisonParameter_0"), 5);
        assertEquals(query.getParameterValue("comparisonParameter_1"), 9);
    }

    /**
     * Check that it is possible to do a day function query with a date
     * Entity: dokumentobjekt
     * Attribute: opprettetDato
     * <p>
     * ODATA Input:
     * arkivstruktur/dokumentobjekt?$filter=day(opprettetDato) ge 4
     * <p>
     * Expected HQL:
     * FROM DocumentObject AS documentobject_1
     * WHERE
     *    day(documentobject_1.createdDate) > :comparisonParameter_0
     * <p>
     * Additionally.
     * The comparisonParameter_0 parameter value should be:
     *   4
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLDayComparison() {
        String yearQuery = "arkivstruktur/dokumentobjekt?$filter=" +
                "day(opprettetDato) ge 4";
        Query query = oDataService.convertODataToHQL(yearQuery, "");
        String hql = "FROM DocumentObject AS documentobject_1 WHERE day" +
                "(documentobject_1.createdDate) >= :comparisonParameter_0";
        assertEquals(query.getParameterValue("comparisonParameter_0"), 4);
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a contains query with a quoted string
     * Entity: dokumentobjekt
     * Attribute: opprettetDato
     * <p>
     * ODATA Input:
     * arkivstruktur/arkivskaper?$filter=contains(tittel, 'Eksempel kommune')
     * Expected HQL:
     * FROM FondsCreator AS fondscreator_1
     * WHERE fondscreator_1.title like :containsParameter_0
     * Additionally the parameter value should be:
     *    %Eksempel kommune%
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLContainsQuotedString() {
        String containsQuery = "arkivstruktur/arkivskaper?$filter=contains" +
                "(arkivskaperID, 'Eksempel kommune')";
        String hql = "FROM FondsCreator AS fondscreator_1 WHERE " +
                "fondscreator_1.fondsCreatorId like :containsParameter_0";
        Query query = oDataService.convertODataToHQL(containsQuery, "");
        assertEquals(query.getParameterValue("containsParameter_0"),
                "%Eksempel kommune%");
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a contains query with a string that
     * is escaped
     * Entity: arkiv
     * Attribute: tittel
     * <p>
     * ODATA Input:
     * arkivstruktur/arkiv?$filter=contains(tittel, 'Jennifer O''Malley')
     * Expected HQL:
     * FROM 
     *    Fonds AS fonds_1
     * WHERE 
     *   fonds_1.title like :containsParameter_0";
     * Additionally the containsParameter_0 parameter value should be
     *    %Jennifer O'Malley%
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLContainsQuotedStringWithEscapedQuote() {
        String FILTER_CONTAINS = "arkivstruktur/arkiv?$filter=contains(tittel, "
                + "'Jennifer O''Malley')";
        String SQL_EQUAL_HQL = "FROM Fonds AS fonds_1 " +
                "WHERE fonds_1.title like :containsParameter_0";
        Query query = oDataService.convertODataToHQL(FILTER_CONTAINS, "");
        String parameter = (String) query.getParameterValue("containsParameter_0");
        assertEquals(query.getQueryString(), SQL_EQUAL_HQL);
        assertEquals(parameter, "%Jennifer O'Malley%");
    }

    /**
     * Check that it is possible to do a simple join query between two
     * entities
     * Entity: mappe -> klasse
     * Attribute: klasse.klasseID
     * <p>
     * ODATA Input:
     * arkivstruktur/mappe?$filter=klasse/klasseID eq '12/2'
     * Expected HQL:
     * FROM File AS file_1
     * JOIN
     * file_1.referenceClass AS class_1
     * WHERE
     * class_1.classId = :comparisonParameter_0
     * Additionally the comparisonParameter_0 parameter value should be:
     * 12/2
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLJoinQuery() {
        String joinQuery = "arkivstruktur/mappe?$filter=" +
                "klasse/klasseID eq '12/2'";
        Query query = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "FROM File AS file_1 JOIN file_1.referenceClass AS " +
                "class_1 WHERE class_1.classId = :comparisonParameter_0";
        assertEquals(query.getParameterValue("comparisonParameter_0"), "12/2");
        assertEquals(query.getQueryString(), hqlJoin);
    }

    /**
     * Check that it is possible to undertake a nested contains
     * Entity: mappe, klasse
     * Attribute: klasse.klasseID
     * <p>
     * ODATA Input:
     * arkivstruktur/mappe?$filter=contains(klasse/klasseID, '12/2')
     * Expected HQL:
     * FROM File AS file_1
     * JOIN file_1.referenceClass AS class_1
     * WHERE
     * class_1.classId = :containsParameter_0
     * Additionally the containsParameter_0 parameter value should be:
     * 12/2
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLJoinQueryWithContains() {
        String joinQuery = "arkivstruktur/mappe?$filter=" +
                "contains(klasse/klasseID, '12/2')";
        Query query = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "FROM File AS file_1 JOIN " +
                "file_1.referenceClass AS class_1 " +
                "WHERE " +
                "class_1.classId like :containsParameter_0";
        String parameter = (String) query.getParameterValue(
                "containsParameter_0");
        assertEquals(parameter, "%12/2%");
        assertEquals(query.getQueryString(), hqlJoin);
    }

    /**
     * Check that it is possible to undertake comparisons with national
     * identifier classes
     * Entity: mappe, foedselsnummer
     * Attribute: foedselsnummer.foedselsnummer
     * <p>
     * Note: Currently this test is failing as national identifiers are not
     * implemented properly for File.java. This test is left as a reminder
     * until the domain model is updated to make sure File supports national
     * identifiers.
     * <p>
     * ODATA Input:
     * arkivstruktur/mappe?$filter=
     * contains(foedselsnummer/foedselsnummer, '050282')
     * Expected HQL:
     * FROM
     * File AS file_1
     * JOIN
     * file_1.referenceSocialSecurityNumber AS socialSecurityNumber
     * WHERE
     * class_1.classId like :containsParameter_0"
     * Additionally the containsParameter_0 parameter value should be:
     * 050282
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLJoinQueryWithContainsNationalIdentifier() {
        String joinQuery = "arkivstruktur/mappe?$filter=" +
                "contains(foedselsnummer/foedselsnummer, '050282')";
        Query query = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "FROM File AS file_1 JOIN " +
                "file_1.referenceSocialSecurityNumber AS socialSecurityNumber" +
                "WHERE " +
                "class_1.classId like : containsParameter_0";
        assertEquals(query.getParameterValue("containsParameter_0"), "%050282%");
        assertEquals(query.getQueryString(), hqlJoin);
    }

    /**
     * Check that it is possible to undertake comparisons with multiple nested
     * JOINs
     * Entity: mappe, foedselsnummer
     * Attribute: foedselsnummer.foedselsnummer
     * <p>
     * ODATA Input:
     * arkivstruktur/mappe?$filter=startswith(
     * klasse/klassifikasjonssystem/tittel, 'Gårds- og bruksnummer')
     * Expected HQL:
     * FROM
     * File AS file_1
     * JOIN file_1.referenceClass AS class_1
     * JOIN class_1.referenceClassificationSystem AS classificationsystem_1
     * WHERE
     * classificationsystem_1.title like :startsWithParameter_0
     * Additionally the containsParameter_0 parameter value should be:
     * Gårds- og bruksnummer%
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLNestedJoinQueryWithStartsWith() {
        String joinQuery = "arkivstruktur/mappe?$filter=" +
                "startswith(klasse/klassifikasjonssystem/tittel, " +
                "'Gårds- og bruksnummer')";
        Query query = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "FROM File AS file_1 JOIN file_1.referenceClass AS " +
                "class_1 JOIN class_1.referenceClassificationSystem AS " +
                "classificationsystem_1 WHERE classificationsystem_1.title " +
                "like :startsWithParameter_0";
        String parameter = (String) query.getParameterValue(
                "startsWithParameter_0");
        assertEquals(parameter, "Gårds- og bruksnummer%");
        assertEquals(query.getQueryString(), hqlJoin);
    }

    /**
     * Check that it is possible to undertake comparisons with multiple nested
     * JOINs from various entities
     * Entity: mappe, klasse, registrering
     * Attribute: klasse.klasseID, registrering.tittel, mappe.tittel
     * <p>
     * ODATA Input:
     * arkivstruktur/mappe?$filter=klasse/klasseID eq '12/2' and
     * contains(tittel, 'Oslo') and registrering/tittel ne 'Brev fra dept.'
     * Expected HQL:
     * FROM
     * File AS file_1
     * JOIN
     * file_1.referenceClass AS class_1
     * JOIN file_1.referenceRecord AS record_1
     * WHERE
     * class_1.classId = :comparisonParameter_0 and
     * file_1.title like :containsParameter_0 and
     * record_1.title != :comparisonParameter_1
     * Additionally.
     * The comparisonParameter_0 parameter value should be:
     * 12/2
     * The comparisonParameter_1 parameter value should be:
     * Brev fra dept.
     * The containsParameter_0 parameter value should be:
     * Oslo
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLJoinQueryWithAnd() {
        String joinQuery = "arkivstruktur/mappe?$filter=" +
                "klasse/klasseID eq '12/2' and contains(tittel, 'Oslo') and " +
                "registrering/tittel ne 'Brev fra dept.'";
        Query query = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "FROM File AS file_1 JOIN file_1.referenceClass AS " +
                "class_1 JOIN file_1.referenceRecord AS record_1 WHERE " +
                "class_1.classId = :comparisonParameter_0 and " +
                "file_1.title like :containsParameter_0 and " +
                "record_1.title != :comparisonParameter_1";
        assertEquals(query.getParameterValue("containsParameter_0"), "%Oslo%");
        assertEquals(query.getParameterValue("comparisonParameter_0"),
                "12/2");
        assertEquals(query.getParameterValue("comparisonParameter_1"),
                "Brev fra dept.");
        assertEquals(query.getQueryString(), hqlJoin);
    }

    /**
     * Just a placeholder to manually test HQL syntax and to check that the
     * query is possible given the Noark domain model.
     */
    @Test
    @Transactional
    public void testManualQuery() {
        String hqlTestJoin = "FROM File as file_1 JOIN " +
                "file_1.referenceClass AS class_1 JOIN file_1.referenceRecord" +
                " AS record_1 WHERE class_1.classId = '12/2' and " +
                "file_1.title like :containsParameter_0 and " +
                "record_1.title = 'Brev fra dept.' ";

        Session session = emf.unwrap(Session.class);
        session.createQuery(hqlTestJoin);
    }
}