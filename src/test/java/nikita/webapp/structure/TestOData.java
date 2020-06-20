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

import static java.util.UUID.fromString;
import static org.junit.Assert.assertEquals;

/**
 * Test OData queries that are supported
 * <p>
 * The following OData queries are tested here:
 * arkivstatus?$filter=kode eq 'O'
 * arkiv/$count?$filter=tittel eq 'The fonds'
 * arkiv/$count
 * arkiv?$filter=tittel eq 'The fonds'
 * arkiv?$filter=beskrivelse eq null
 * arkiv?$filter=beskrivelse ne null
 * arkiv?$filter=arkivstatus/kode eq 'O'
 * arkiv?$filter=tittel eq 'The fonds'&$top=5
 * arkiv?$filter=tittel eq 'The fonds'&$skip=10
 * arkiv?$filter=tittel eq 'The fonds'&$top=8&$skip=10
 * mappe?$filter=contains(tittel, 'søknad')&$top=8&$skip=10&$orderby=opprettetDato
 * mappe?$filter=contains(tittel, 'søknad')&$orderby=opprettetDato
 * mappe?$filter=contains(tittel, 'søknad')&$orderby=opprettetDato ASC
 * mappe?$filter=contains(tittel, 'søknad')&$orderby=opprettetDato ASC, tittel DESC
 * dokumentobjekt?$filter=dokumentbeskrivelse/dokumentstatus/kode eq 'B'
 * dokumentbeskrivelse?$filter=dokumentstatus/kodenavn eq 'Dokumentet er under redigering'
 * klasse?$filter=(beskrivelse ne null and length(tittel) gt 4) or (tittel eq 'class number 1' and year(opprettetDato) eq 2019)
 * dokumentobjekt?$filter=filnavn eq '<9aqr221f34c.hsr@address.udn.com>'
 * arkivdel?$filter=arkiv/beskrivelse eq 'The fonds description'
 * journalpost?$filter=registreringsID ne '2020/000234-23'
 * dokumentobjekt?$filter=year(opprettetDato) eq 2020
 * dokumentobjekt?$filter=month(opprettetDato) gt 5 and month(opprettetDato) lt 9
 * dokumentobjekt?$filter=month(dokumentbeskrivelse/opprettetDato) gt 5 and month(opprettetDato) le 9
 * dokumentobjekt?$filter=day(opprettetDato) ge 4
 * dokumentobjekt?$filter=hour(opprettetDato) lt 14
 * dokumentobjekt?$filter=minute(opprettetDato) lt 56
 * dokumentobjekt?$filter=second(opprettetDato) lt 56
 * arkivskaper?$filter=contains(tittel, 'Eksempel kommune')
 * arkiv?$filter=contains(tittel, 'Jennifer O''Malley')
 * mappe?$filter=klasse/klasseID eq '12/2'
 * mappe?$filter=contains(klasse/klasseID, '12/2')
 * EXPECTED FAIL mappe?$filter=contains(foedselsnummer/foedselsnummer, '050282')
 * mappe?$filter=startswith(klasse/klassifikasjonssystem/tittel, 'Gårds- og bruksnummer')
 * mappe?$filter=klasse/klasseID eq '12/2' and contains(tittel, 'Oslo') and registrering/tittel ne 'Brev fra dept.'
 * saksmappe?$filter= concat(concat(saksaar, '-'), sakssekvensnummer) eq '2020-10233'
 * arkivskaper?$filter=trim(arkivskaperNavn) eq 'Oslo kommune'
 * dokumentobjekt?$filter=length(sjekksum) ne 64
 * arkivskaper?$filter=tolower(arkivskaperNavn) eq 'oslo kommune'
 * arkivskaper?$filter=trim(toupper(tolower(arkivskaperNavn))) eq 'oslo kommune'
 * arkivskaper?$filter=toupper(arkivskaperNavn) eq 'OSLO KOMMUNE'
 * EXPECTED FAIL dokumentbeskrivelse?$filter=round(dokumentnummer) gt 5
 * EXPECTED FAIL dokumentbeskrivelse?$filter=ceiling(dokumentnummer) ge 8
 * EXPECTED FAIL dokumentbeskrivelse?$filter=floor(dokumentnummer) le 5
 * arkivdel/6654347b-077b-4241-a3ec-f351ef748250/mappe?$filter= year(opprettetDato) lt 2020
 * arkivdel/6654347b-077b-4241-a3ec-f351ef748250/mappe?$filter=startswith(klasse/klassifikasjonssystem/tittel, 'Gårds- og bruksnummer')
 * mappe?$filter=(klasse/klasseID eq '12/2' and contains(tittel, 'Oslo')) or (registrering/tittel ne 'Brev fra dept.')
 * arkiv/f984d44f-02c2-4f8d-b3c2-6106094b15b0/arkivdel/6654347b-077b-4241-a3ec-f351ef748250/mappe?$filter=year(opprettetDato) lt 2020
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
     * Entity: arkivstatus
     * Attribute: kode
     * ODATA Input:
     * arkivstatus?$filter=kode eq 'O'
     * <p>
     * Expected HQL:
     * SELECT fondsstatus_1 FROM FondsStatus AS fondsstatus_1
     * WHERE
     * fondsstatus_1.code = :parameter_0
     * <p>
     * Additionally the parameter_0 parameter value should be:
     * O
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEQQueryMetadataEntity() {
        String odata = "arkivstatus?$filter=kode eq 'O'";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT fondsstatus_1 FROM FondsStatus AS fondsstatus_1" +
                " WHERE" +
                " fondsstatus_1.code = :parameter_0";
        assertEquals(query.getParameterValue("parameter_0"),
                "O");
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a query with a $count
     * <p>
     * Entity: arkiv
     * Attribute: tittel
     * ODATA Input:
     * arkiv/$count?$filter=tittel eq 'The fonds'
     * <p>
     * Expected HQL:
     * SELECT count(*) FROM Fonds AS fonds_1
     * WHERE
     * fonds_1.title
     * <p>
     * Additionally the parameter_0 parameter value should be:
     * O
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEQQueryCountWithFilter() {
        String odata = "arkiv/$count?$filter=tittel eq 'The fonds'";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT count(*) FROM Fonds AS fonds_1" +
                " WHERE" +
                " fonds_1.title = :parameter_0";
        assertEquals(query.getParameterValue("parameter_0"),
                "The fonds");
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a query with a $count
     * <p>
     * Entity: arkiv
     * ODATA Input:
     * arkiv/$count
     * <p>
     * Expected HQL:
     * SELECT count(*) FROM Fonds AS fonds_1
     * WHERE
     * fonds_1.title
     * <p>
     * Additionally the parameter_0 parameter value should be:
     * O
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEQQueryCount() {
        String odata = "arkiv/$count";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT count(*) FROM Fonds AS fonds_1";
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a eq query with a quoted string
     * <p>
     * Entity: arkiv
     * Attribute: tittel
     * ODATA Input:
     * arkiv?$filter=tittel eq 'The fonds'
     * <p>
     * Expected HQL:
     * SELECT fonds_1 FROM Fonds AS fonds_1
     * WHERE
     * fonds_1.title = :parameter_0
     * <p>
     * Additionally the parameter_0 parameter value should be:
     * The fonds
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEQQueryString() {
        String odata = "arkiv?$filter=tittel eq 'The fonds'";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT fonds_1 FROM Fonds AS fonds_1" +
                " WHERE fonds_1.title = :parameter_0";
        assertEquals(query.getParameterValue("parameter_0"),
                "The fonds");
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a simple query with a order by clause
     * with sort order identified
     * Entity: mappe
     * Attribute: tittel
     * <p>
     * ODATA Input:
     * mappe?$filter=contains(tittel, 'søknad')&$orderby=opprettetDato ASC
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1
     * WHERE
     * file_1.title like :parameter_0
     * order by file_1.createdDate ASC
     * Additionally the parameter_0 parameter value should be:
     * %søknad%
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLOrderBySingleAttributeSortOrder() {
        String joinQuery = "mappe?$filter=" +
                "contains(tittel, 'søknad')&$orderby=opprettetDato ASC";
        Query query = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "SELECT file_1 FROM File AS file_1" +
                " WHERE" +
                " file_1.title like :parameter_0" +
                " order by file_1.createdDate ASC";
        assertEquals(query.getParameterValue("parameter_0"), "%søknad%");
        assertEquals(query.getQueryString(), hqlJoin);
    }

    /**
     * Check that it is possible to do a simple query with a order by clause
     * with multiple attributes and where sort order is identified
     * Entity: mappe
     * Attribute: tittel
     * <p>
     * ODATA Input:
     * mappe?$filter=contains(tittel, 'søknad')&$orderby=opprettetDato ASC,
     * tittel DESC
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1
     * WHERE
     * file_1.title like :parameter_0
     * order by file_1.createdDate ASC, file_1.title DESC,
     * Additionally the parameter_0 parameter value should be:
     * %søknad%
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLOrderBySingleAttributeMultipleSortOrder() {
        String joinQuery = "mappe?$filter=" +
                "contains(tittel, 'søknad')" +
                "&$orderby=opprettetDato ASC, tittel DESC";
        Query query = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "SELECT file_1 FROM File AS file_1" +
                " WHERE" +
                " file_1.title like :parameter_0" +
                " order by file_1.createdDate ASC, file_1.title DESC";
        assertEquals(query.getParameterValue("parameter_0"), "%søknad%");
        assertEquals(query.getQueryString(), hqlJoin);
    }

    /**
     * Check that it is possible to do a simple query with a order by clause
     * <p>
     * Entity: mappe
     * Attribute: tittel
     * <p>
     * ODATA Input:
     * mappe?$filter=contains(tittel, 'søknad')&$orderby=opprettetDato
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1
     * WHERE
     * file_1.title like :parameter_0
     * order by file_1.createdDate
     * Additionally the parameter_0 parameter value should be:
     * %søknad%
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLOrderBySingleAttribute() {
        String joinQuery = "mappe?$filter=" +
                "contains(tittel, 'søknad')&$orderby=opprettetDato";
        Query query = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "SELECT file_1 FROM File AS file_1" +
                " WHERE" +
                " file_1.title like :parameter_0" +
                " order by file_1.createdDate";
        assertEquals(query.getParameterValue("parameter_0"), "%søknad%");
        assertEquals(query.getQueryString(), hqlJoin);
    }

    /**
     * Check that it is possible to do a eq query with a quoted string
     * <p>
     * Entity: arkiv
     * Attribute: tittel
     * ODATA Input:
     * arkiv?$filter=tittel eq 'The fonds'&$top=5
     * <p>
     * Expected HQL:
     * SELECT fonds_1 FROM Fonds AS fonds_1
     * WHERE
     * fonds_1.title = :parameter_0
     * <p>
     * Additionally the parameter_0 parameter value should be:
     * The fonds
     * and
     * maxRows = 5
     */
    @Test
    @Transactional
    public void shouldReturnValidHQTop() {
        String odata = "arkiv?$filter=tittel eq 'The fonds'&$top=5";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT fonds_1 FROM Fonds AS fonds_1" +
                " WHERE fonds_1.title = :parameter_0";

        Integer maxRows = query.getQueryOptions().getMaxRows();
        assertEquals(maxRows, Integer.valueOf(5));
        assertEquals(query.getParameterValue("parameter_0"),
                "The fonds");
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a eq query $skip
     * <p>
     * Entity: arkiv
     * Attribute: tittel
     * ODATA Input:
     * arkiv?$filter=tittel eq 'The fonds'&$skip=10
     * <p>
     * Expected HQL:
     * SELECT fonds_1 FROM Fonds AS fonds_1
     * WHERE
     * fonds_1.title = :parameter_0
     * <p>
     * Additionally the parameter_0 parameter value should be:
     *   The fonds
     * and
     *  firstRow = 10
     */
    @Test
    @Transactional
    public void shouldReturnValidHQSkip() {
        String odata = "arkiv?$filter=tittel eq 'The fonds'&$skip=10";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT fonds_1 FROM Fonds AS fonds_1" +
                " WHERE fonds_1.title = :parameter_0";

        Integer firstRow = query.getQueryOptions().getFirstRow();
        assertEquals(firstRow, Integer.valueOf(10));
        assertEquals(query.getParameterValue("parameter_0"),
                "The fonds");
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a eq query with $top and $skip
     * <p>
     * Entity: arkiv
     * Attribute: tittel
     * ODATA Input:
     * arkiv?$filter=tittel eq 'The fonds'&$top=8&$skip=10
     * <p>
     * Expected HQL:
     * SELECT fonds_1 FROM Fonds AS fonds_1
     * WHERE
     * fonds_1.title = :parameter_0
     * <p>
     * Additionally the parameter_0 parameter value should be:
     * The fonds
     * and
     * maxRows = 8
     * and
     * firstRow = 10
     */
    @Test
    @Transactional
    public void shouldReturnValidHQTopSkip() {
        String odata = "arkiv?$filter=tittel eq 'The fonds'&$top=8&$skip=10";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT fonds_1 FROM Fonds AS fonds_1" +
                " WHERE fonds_1.title = :parameter_0";

        assertEquals(query.getQueryOptions().getMaxRows(), Integer.valueOf(8));
        assertEquals(query.getQueryOptions().getFirstRow(), Integer.valueOf(10));
        assertEquals(query.getParameterValue("parameter_0"),
                "The fonds");
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a eq query with $top and $skip
     * <p>
     * Entity: arkiv
     * Attribute: tittel
     * ODATA Input:
     * arkiv?$filter=tittel eq 'The fonds'&$top=8&$skip=10
     * <p>
     * Expected HQL:
     * SELECT fonds_1 FROM Fonds AS fonds_1
     * WHERE
     * fonds_1.title = :parameter_0
     * <p>
     * Additionally the parameter_0 parameter value should be:
     * The fonds
     * and
     * maxRows = 8
     * and
     * firstRow = 10
     */
    @Test
    @Transactional
    public void shouldReturnValidHQTopSkipOrderBy() {
        String odata = "mappe?$filter=contains(tittel, 'søknad')" +
                "&$top=23&$skip=49&$orderby=opprettetDato";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT file_1 FROM File AS file_1" +
                " WHERE file_1.title like :parameter_0" +
                " order by file_1.createdDate";

        assertEquals(query.getQueryOptions().getMaxRows(), Integer.valueOf(23));
        assertEquals(query.getQueryOptions().getFirstRow(), Integer.valueOf(49));
        assertEquals(query.getParameterValue("parameter_0"),
                "%søknad%");
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a eq query using null
     * <p>
     * Entity: arkiv
     * Attribute: beskrivelse
     * ODATA Input:
     * arkiv?$filter=beskrivelse eq null
     * <p>
     * Expected HQL:
     * SELECT fonds_1 FROM Fonds AS fonds_1
     * WHERE
     * fonds_1.description is null
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEQNullQuery() {
        String odata = "arkiv?$filter=beskrivelse eq null";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT fonds_1 FROM Fonds AS fonds_1" +
                " WHERE" +
                " fonds_1.description is null";
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a eq query using null
     * <p>
     * Entity: arkiv
     * Attribute: beskrivelse
     * ODATA Input:
     * arkiv?$filter=beskrivelse ne null
     * <p>
     * Expected HQL:
     * SELECT fonds_1 FROM Fonds AS fonds_1
     * WHERE
     * fonds_1.description is not null
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEQNotNullQuery() {
        String odata = "arkiv?$filter=beskrivelse ne null";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT fonds_1 FROM Fonds AS fonds_1" +
                " WHERE" +
                " fonds_1.description is not null";
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a eq query using null
     * <p>
     * Entity: klasse
     * Attribute: beskrivelse, tittel, opprettetDato
     * ODATA Input:
     * klasse?$filter=(beskrivelse ne null and length(tittel) gt 4) or (tittel eq 'class number 1' and year(opprettetDato) eq 2019)
     * <p>
     * Expected HQL:
     * SELECT class_1 FROM Class AS class_1
     * WHERE
     * (class_1.description is not null and length(class_1.title) > 4) or
     * (class_1.title eq 'class number 1' and year(opprettetDato) = 2019)
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEQQueryAndOr() {
        String odata = "klasse?$filter=(beskrivelse ne null and" +
                " length(tittel) gt 4) or" +
                " (tittel eq 'class number 1' and" +
                " year(opprettetDato) eq 2019)";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT class_1 FROM Class AS class_1" +
                " WHERE" +
                " (class_1.description is not null and" +
                " length(class_1.title) > :parameter_0) or" +
                " (class_1.title = :parameter_1 and" +
                " year(class_1.createdDate) = :parameter_2)";
        assertEquals(query.getQueryString(), hql);
        assertEquals(query.getParameterValue("parameter_0"),
                Integer.valueOf("4"));
        assertEquals(query.getParameterValue("parameter_1"),
                "class number 1");
        assertEquals(query.getParameterValue("parameter_2"),
                Integer.valueOf("2019"));

    }

    /**
     * Check that it is possible to do a eq query with a quoted string. This
     * test is here to remind us that it creates a warning in the logfile that
     * should be addressed.
     * <p>
     * Entity: arkiv
     * Attribute: tittel
     * ODATA Input:
     * dokumentobjekt?$filter=filnavn eq '<9aqr221f34c.hsr@diskless.uio.no>'
     * <p>
     * Expected HQL:
     * SELECT documentobject_1 FROM DocumentObject AS documentobject_1
     * WHERE
     * documentobject_1.originalFilename = :parameter_0
     * <p>
     * Additionally the parameter_0 parameter value should be:
     * <9aqr221f34c.hsr@diskless.uio.no>
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEQQueryStringWithChars() {
        String odata = "dokumentobjekt?$filter=" +
                "filnavn eq '<9aqr221f34c.hsr@address.udn.com>'";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT documentobject_1" +
                " FROM DocumentObject AS documentobject_1 WHERE" +
                " documentobject_1.originalFilename = :parameter_0";
        assertEquals(query.getParameterValue("parameter_0"),
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
     * arkivdel?$filter=arkiv/beskrivelse eq 'The fonds
     * description'
     * <p>
     * Expected HQL:
     * SELECT series_1 FROM Series AS series_1
     * JOIN
     * series_1.referenceFonds AS fonds_1
     * WHERE fonds_1.description = :parameter_0
     * <p>
     * Additionally the parameter_0 parameter value should be
     * The fonds description
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEQQueryStringWithJOIN() {
        String odata = "arkivdel?$filter=arkiv/beskrivelse " +
                "eq 'The fonds description'";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT series_1 FROM Series AS series_1 " +
                "JOIN series_1.referenceFonds AS fonds_1 " +
                "WHERE fonds_1.description = :parameter_0";
        assertEquals(query.getParameterValue("parameter_0"),
                "The fonds description");
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a ne query with a quoted string.
     * Entity: journalpost
     * Attribute: registreringsID
     * <p>
     * ODATA Input:
     * journalpost?$filter=registreringsID ne '2020/000234-23'
     * <p>
     * Expected HQL:
     * SELECT registryentry_1 FROM RegistryEntry AS registryentry_1
     * WHERE registryentry_1.recordId != :parameter_0
     * <p>
     * Additionally the parameter_0 parameter value should be:
     * 2020/000234-23
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLNotEQQueryString() {
        String odata = "journalpost?$filter=registreringsID " +
                "ne '2020/000234-23'";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT registryentry_1 FROM RegistryEntry AS " +
                "registryentry_1 WHERE " +
                "registryentry_1.recordId != :parameter_0";
        assertEquals(query.getParameterValue("parameter_0"),
                "2020/000234-23");
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a year function query with a date
     * Entity: dokumentobjekt
     * Attribute: opprettetDato
     * <p>
     * ODATA Input:
     * dokumentobjekt?$filter=year(opprettetDato) eq 2020
     * <p>
     * Expected HQL:
     * SELECT documentobject_1 FROM DocumentObject AS documentobject_1
     * WHERE year(documentobject_1.createdDate) = :parameter_0
     * <p>
     * Additionally the parameter_0 parameter value should be:
     * 2020
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLYearComparison() {
        String odata = "dokumentobjekt?$filter=" +
                "year(opprettetDato) eq 2020";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT documentobject_1 FROM DocumentObject AS " +
                "documentobject_1 WHERE " +
                "year(documentobject_1.createdDate) = :parameter_0";
        assertEquals(query.getParameterValue("parameter_0"),
                2020);
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a month function query with a date
     * Entity: dokumentobjekt
     * Attribute: opprettetDato
     * <p>
     * ODATA Input:
     * dokumentobjekt?$filter=month(opprettetDato) gt 5 and
     * month(opprettetDato) lt 9
     * <p>
     * Expected HQL:
     * SELECT documentobject_1 FROM DocumentObject AS documentobject_1
     * WHERE
     * month(documentobject_1.createdDate) > :parameter_0 and
     * month(documentobject_1.createdDate) < :parameter_1
     * <p>
     * Additionally.
     * The parameter_0 parameter value should be:
     * 5
     * The parameter_1 parameter value should be:
     * 9
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLMonthComparison() {
        String monthQuery = "dokumentobjekt?$filter=" +
                "month(opprettetDato) gt 5 and month(opprettetDato) lt 9";
        Query query = oDataService.convertODataToHQL(monthQuery, "");
        String hql = "SELECT documentobject_1 FROM DocumentObject AS " +
                "documentobject_1 WHERE " +
                "month(documentobject_1.createdDate) > :parameter_0" +
                " and " +
                "month(documentobject_1.createdDate) < :parameter_1";
        assertEquals(query.getParameterValue("parameter_0"), 5);
        assertEquals(query.getParameterValue("parameter_1"), 9);
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a month function query with a date
     * using a nested join
     * Entity: dokumentobjekt, dokumentbeskrivelse
     * Attribute: dokumentobjekt.opprettetDato, dokumentbeskrivelse.opprettetDato
     * <p>
     * ODATA Input:
     * dokumentobjekt?$filter=month(opprettetDato) gt 5 and
     * month(opprettetDato) lt 9
     * <p>
     * Expected HQL:
     * SELECT documentobject_1 FROM DocumentObject AS documentobject_1
     * JOIN
     * documentobject_1.referenceDocumentDescription AS documentdescription_1
     * WHERE
     * month(documentdescription_1.createdDate) > :parameter_0 and
     * month(documentobject_1.createdDate) < :parameter_1
     * <p>
     * Additionally.
     * The parameter_0 parameter value should be:
     * 5
     * The parameter_1 parameter value should be:
     * 9
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLMonthComparisonWithJoin() {
        String odata = "dokumentobjekt?$filter=" +
                "month(dokumentbeskrivelse/opprettetDato) gt 5 and " +
                "month(opprettetDato) le 9";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT documentobject_1 FROM DocumentObject AS " +
                "documentobject_1 JOIN " +
                "documentobject_1.referenceDocumentDescription AS " +
                "documentdescription_1 WHERE " +
                "month(documentdescription_1.createdDate) " +
                "> :parameter_0 and " +
                "month(documentobject_1.createdDate) " +
                "<= :parameter_1";
        assertEquals(query.getQueryString(), hql);
        assertEquals(query.getParameterValue("parameter_0"), 5);
        assertEquals(query.getParameterValue("parameter_1"), 9);
    }

    /**
     * Check that it is possible to do a metadata query
     * using a nested join
     * Entity: arkiv (arkivstatus)
     * Attribute: kode
     * <p>
     * ODATA Input:
     * arkiv?$filter=arkivstatus/kode eq 'O'
     * <p>
     * Expected HQL:
     * SELECT fonds_1 FROM Fonds AS fonds_1
     * WHERE
     * fonds_1.fondsStatusCode eq :parameter_0
     * <p>
     * Additionally.
     * The parameter_0 parameter value should be:
     * Dokumentet er under redigering
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLMetadataCodeDocumentStatusJoin() {
        String odata = "dokumentobjekt?$filter=dokumentbeskrivelse" +
                "/dokumentstatus/kode eq 'B'";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT documentobject_1 FROM DocumentObject" +
                " AS documentobject_1" +
                " JOIN" +
                " documentobject_1.referenceDocumentDescription AS documentdescription_1" +
                " WHERE" +
                " documentdescription_1.documentStatusCode = :parameter_0";
        assertEquals(query.getQueryString(), hql);
        assertEquals(query.getParameterValue("parameter_0"), "B");
    }

    /**
     * Check that it is possible to do a metadata query
     * using a nested join
     * Entity: arkiv (arkivstatus)
     * Attribute: kode
     * <p>
     * ODATA Input:
     * dokumentbeskrivelse?$filter=dokumentstatus/kodenavn eq
     * 'Dokumentet er under redigering'
     * <p>
     * Expected HQL:
     * SELECT documentdescription_1 FROM DocumentDescription AS documentdescription_1
     * WHERE
     * documentdescription_1.documentStatusCodeName = :parameter_0
     * <p>
     * Additionally.
     * The parameter_0 parameter value should be:
     * O
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLMetadataCodeDocumentStatusCodeName() {
        String odata = "dokumentbeskrivelse?$filter=" +
                "dokumentstatus/kodenavn eq 'Dokumentet er under redigering'";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT documentdescription_1 FROM DocumentDescription" +
                " AS documentdescription_1" +
                " WHERE" +
                " documentdescription_1.documentStatusCodeName = :parameter_0";
        assertEquals(query.getQueryString(), hql);
        assertEquals(query.getParameterValue("parameter_0"),
                "Dokumentet er under redigering");
    }

    /**
     * Check that it is possible to do a month function query with a date
     * using a nested join
     * Entity: arkiv (arkivstatus)
     * Attribute: kode
     * <p>
     * ODATA Input:
     * arkiv?$filter=arkivstatus/kode eq 'O'
     * <p>
     * Expected HQL:
     * SELECT fonds_1 FROM Fonds AS fonds_1
     * WHERE
     * fonds_1.fondsStatusCode eq :parameter_0
     * <p>
     * Additionally.
     * The parameter_0 parameter value should be:
     * O
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLMetadataCodeFondsStatus() {
        String odata = "arkiv?$filter=arkivstatus/kode eq 'O'";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT fonds_1 FROM Fonds AS fonds_1" +
                " WHERE" +
                " fonds_1.fondsStatusCode = :parameter_0";
        assertEquals(query.getQueryString(), hql);
        assertEquals(query.getParameterValue("parameter_0"), "O");
    }

    /**
     * Check that it is possible to do a month function query with a date
     * using a nested join
     * Entity: dokumentobjekt, dokumentbeskrivelse
     * Attribute: dokumentobjekt.opprettetDato, dokumentbeskrivelse.opprettetDato
     * <p>
     * ODATA Input:
     * dokumentobjekt?$filter=month(opprettetDato) gt 5 and
     * month(opprettetDato) lt 9
     * <p>
     * Expected HQL:
     * SELECT documentobject_1 FROM DocumentObject AS documentobject_1
     * JOIN
     * documentobject_1.referenceDocumentDescription AS documentdescription_1
     * WHERE
     * month(documentdescription_1.createdDate) > :parameter_0 and
     * month(documentobject_1.createdDate) < :parameter_1
     * <p>
     * Additionally.
     * The parameter_0 parameter value should be:
     * 5
     * The parameter_1 parameter value should be:
     * 9
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLMonthComparisonWithJoinIncludesManyToMany() {
        String odata = "dokumentobjekt?$filter=" +
                "month(dokumentbeskrivelse/registrering/mappe/arkivdel/arkiv" +
                "/opprettetDato) gt 5";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT documentobject_1 FROM DocumentObject" +
                " AS documentobject_1" +
                " JOIN documentobject_1.referenceDocumentDescription" +
                " AS documentdescription_1" +
                " JOIN documentdescription_1.referenceRecord AS record_1" +
                " JOIN record_1.referenceFile AS file_1" +
                " JOIN file_1.referenceSeries AS series_1" +
                " JOIN series_1.referenceFonds AS fonds_1" +
                " WHERE month(fonds_1.createdDate) > :parameter_0";
        assertEquals(query.getQueryString(), hql);
        assertEquals(query.getParameterValue("parameter_0"), 5);
    }

    /**
     * Check that it is possible to do a day function query with a date
     * Entity: dokumentobjekt
     * Attribute: opprettetDato
     * <p>
     * ODATA Input:
     * dokumentobjekt?$filter=day(opprettetDato) ge 4
     * <p>
     * Expected HQL:
     * SELECT documentobject_1 FROM DocumentObject AS documentobject_1
     * WHERE
     * day(documentobject_1.createdDate) >= :parameter_0
     * <p>
     * Additionally.
     * The parameter_0 parameter value should be:
     * 4
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLDayComparison() {
        String odata = "dokumentobjekt?$filter=" +
                "day(opprettetDato) ge 4";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT documentobject_1 FROM DocumentObject AS " +
                "documentobject_1 WHERE " +
                "day(documentobject_1.createdDate) >= :parameter_0";
        assertEquals(query.getParameterValue("parameter_0"), 4);
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do an hour function query with a date
     * Entity: dokumentobjekt
     * Attribute: opprettetDato
     * <p>
     * ODATA Input:
     * dokumentobjekt?$filter=hour(opprettetDato) lt 14
     * <p>
     * Expected HQL:
     * SELECT documentobject_1 FROM DocumentObject AS documentobject_1
     * WHERE
     * hour(documentobject_1.createdDate) < :parameter_0
     * <p>
     * Additionally.
     * The parameter_0 parameter value should be:
     * 14
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLHourComparison() {
        String odata = "dokumentobjekt?$filter=" +
                "hour(opprettetDato) lt 14";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT documentobject_1 FROM DocumentObject AS " +
                "documentobject_1 WHERE " +
                "hour(documentobject_1.createdDate) < :parameter_0";
        assertEquals(query.getParameterValue("parameter_0"), 14);
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do an minute function query with a date
     * Entity: dokumentobjekt
     * Attribute: opprettetDato
     * <p>
     * ODATA Input:
     * dokumentobjekt?$filter=minute(opprettetDato) lt 56
     * <p>
     * Expected HQL:
     * SELECT documentobject_1 FROM DocumentObject AS documentobject_1
     * WHERE
     * minute(documentobject_1.createdDate) < :parameter_0
     * <p>
     * Additionally.
     * The parameter_0 parameter value should be:
     * 56
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLMinuteComparison() {
        String odata = "dokumentobjekt?$filter=" +
                "minute(opprettetDato) ge 56";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT documentobject_1 FROM DocumentObject AS " +
                "documentobject_1 WHERE " +
                "minute(documentobject_1.createdDate) >= :parameter_0";
        assertEquals(query.getParameterValue("parameter_0"), 56);
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do an second function query with a date
     * Entity: dokumentobjekt
     * Attribute: opprettetDato
     * <p>
     * ODATA Input:
     * dokumentobjekt?$filter=second(opprettetDato) lt 56
     * <p>
     * Expected HQL:
     * SELECT documentobject_1 FROM DocumentObject AS documentobject_1
     * WHERE
     * second(documentobject_1.createdDate) < :parameter_0
     * <p>
     * Additionally.
     * The parameter_0 parameter value should be:
     * 56
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLsecondComparison() {
        String odata = "dokumentobjekt?$filter=" +
                "second(opprettetDato) ge 56";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT documentobject_1 FROM DocumentObject AS " +
                "documentobject_1 WHERE " +
                "second(documentobject_1.createdDate) >= :parameter_0";
        assertEquals(query.getParameterValue("parameter_0"), 56);
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a contains query with a quoted string
     * Entity: dokumentobjekt
     * Attribute: opprettetDato
     * <p>
     * ODATA Input:
     * arkivskaper?$filter=contains(tittel, 'Eksempel kommune')
     * Expected HQL:
     * SELECT fondscreator_1 FROM FondsCreator AS fondscreator_1
     * WHERE fondscreator_1.title like :parameter_0
     * Additionally the parameter value should be:
     * %Eksempel kommune%
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLContainsQuotedString() {
        String containsQuery = "arkivskaper?$filter=" +
                "contains(arkivskaperID, 'Eksempel kommune')";
        String hql = "SELECT fondscreator_1 FROM FondsCreator AS" +
                " fondscreator_1 WHERE" +
                " fondscreator_1.fondsCreatorId like :parameter_0";
        Query query = oDataService.convertODataToHQL(containsQuery, "");
        assertEquals(query.getParameterValue("parameter_0"),
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
     * arkiv?$filter=contains(tittel, 'Jennifer O''Malley')
     * Expected HQL:
     * SELECT fonds_1 FROM
     * Fonds AS fonds_1
     * WHERE
     * fonds_1.title like :parameter_0";
     * Additionally the parameter_0 parameter value should be
     * %Jennifer O'Malley%
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLContainsQuotedStringWithEscapedQuote() {
        String containsQuery = "arkiv?$filter=" +
                "contains(tittel, 'Jennifer O''Malley')";
        String hql = "SELECT fonds_1 FROM Fonds AS fonds_1" +
                " WHERE" +
                " fonds_1.title like :parameter_0";
        Query query = oDataService.convertODataToHQL(containsQuery, "");
        String parameter = (String) query.getParameterValue("parameter_0");
        assertEquals(query.getQueryString(), hql);
        assertEquals(parameter, "%Jennifer O'Malley%");
    }

    /**
     * Check that it is possible to do a simple join query between two
     * entities
     * Entity: mappe -> klasse
     * Attribute: klasse.klasseID
     * <p>
     * ODATA Input:
     * mappe?$filter=klasse/klasseID eq '12/2'
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1
     * JOIN
     * file_1.referenceClass AS class_1
     * WHERE
     * class_1.classId = :parameter_0
     * Additionally the parameter_0 parameter value should be:
     * 12/2
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLJoinQuery() {
        String joinQuery = "mappe?$filter=" +
                "klasse/klasseID eq '12/2'";
        Query query = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "SELECT file_1 FROM File AS file_1" +
                " JOIN" +
                " file_1.referenceClass AS class_1" +
                " WHERE " +
                "class_1.classId = :parameter_0";
        assertEquals(query.getParameterValue("parameter_0"), "12/2");
        assertEquals(query.getQueryString(), hqlJoin);
    }

    /**
     * Check that it is possible to undertake a nested contains
     * Entity: mappe, klasse
     * Attribute: klasse.klasseID
     * <p>
     * ODATA Input:
     * mappe?$filter=contains(klasse/klasseID, '12/2')
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1
     * JOIN file_1.referenceClass AS class_1
     * WHERE
     * class_1.classId = :parameter_0
     * Additionally the parameter_0 parameter value should be:
     * 12/2
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLJoinQueryWithContains() {
        String joinQuery = "mappe?$filter=" +
                "contains(klasse/klasseID, '12/2')";
        Query query = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "SELECT file_1 FROM File AS file_1" +
                " JOIN" +
                " file_1.referenceClass AS class_1" +
                " WHERE" +
                " class_1.classId like :parameter_0";
        String parameter = (String) query.getParameterValue(
                "parameter_0");
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
     * mappe?$filter=contains(foedselsnummer/foedselsnummer, '050282')
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1
     * JOIN
     * file_1.referenceSocialSecurityNumber AS socialSecurityNumber
     * WHERE
     * class_1.classId like :parameter_0"
     * Additionally the parameter_0 parameter value should be:
     * 050282
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLJoinQueryWithContainsNationalIdentifier() {
        String joinQuery = "mappe?$filter=" +
                "contains(foedselsnummer/foedselsnummer, '050282')";
        Query query = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "SELECT file_1 FROM File AS file_1" +
                " JOIN" +
                " file_1.referenceSocialSecurityNumber AS socialSecurityNumber" +
                " WHERE" +
                " class_1.classId like : parameter_0";
        assertEquals(query.getParameterValue("parameter_0"), "%050282%");
        assertEquals(query.getQueryString(), hqlJoin);
    }

    /**
     * Check that it is possible to undertake comparisons with multiple nested
     * JOINs
     * Entity: mappe, foedselsnummer
     * Attribute: foedselsnummer.foedselsnummer
     * <p>
     * ODATA Input:
     * mappe?$filter=startswith(
     * klasse/klassifikasjonssystem/tittel, 'Gårds- og bruksnummer')
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1
     * JOIN file_1.referenceClass AS class_1
     * JOIN class_1.referenceClassificationSystem AS classificationsystem_1
     * WHERE
     * classificationsystem_1.title like :parameter_0
     * Additionally the parameter_0 parameter value should be:
     * Gårds- og bruksnummer%
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLNestedJoinQueryWithStartsWith() {
        String joinQuery = "mappe?$filter=" +
                "startswith(klasse/klassifikasjonssystem/tittel, " +
                "'Gårds- og bruksnummer')";
        Query query = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "SELECT file_1 FROM File AS file_1" +
                " JOIN" +
                " file_1.referenceClass AS class_1" +
                " JOIN" +
                " class_1.referenceClassificationSystem AS" +
                " classificationsystem_1" +
                " WHERE" +
                " classificationsystem_1.title like :parameter_0";
        String parameter = (String) query.getParameterValue(
                "parameter_0");
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
     * mappe?$filter=klasse/klasseID eq '12/2' and
     * contains(tittel, 'Oslo') and registrering/tittel ne 'Brev fra dept.'
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1
     * JOIN
     * file_1.referenceClass AS class_1
     * JOIN file_1.referenceRecord AS record_1
     * WHERE
     * class_1.classId = :parameter_0 and
     * file_1.title like :parameter_0 and
     * record_1.title != :parameter_1
     * Additionally.
     * The parameter_0 parameter value should be:
     * 12/2
     * The parameter_1 parameter value should be:
     * Brev fra dept.
     * The parameter_0 parameter value should be:
     * Oslo
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLJoinQueryWithAnd() {
        String joinQuery = "mappe?$filter=" +
                "klasse/klasseID eq '12/2' and contains(tittel, 'Oslo') and " +
                "registrering/tittel ne 'Brev fra dept.'";
        Query query = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "SELECT file_1 FROM File AS file_1 " +
                "JOIN file_1.referenceClass AS class_1 " +
                "JOIN file_1.referenceRecord AS record_1 WHERE " +
                "class_1.classId = :parameter_0 and " +
                "file_1.title like :parameter_1 and " +
                "record_1.title != :parameter_2";
        assertEquals(query.getParameterValue("parameter_0"),
                "12/2");
        assertEquals(query.getParameterValue("parameter_1"),
                "%Oslo%");
        assertEquals(query.getParameterValue("parameter_2"),
                "Brev fra dept.");
        assertEquals(query.getQueryString(), hqlJoin);
    }

    /**
     * Check that it is possible to undertake comparisons with multiple nested
     * JOINs from various entities
     * Entity: mappe, klasse, registrering
     * Attribute: klasse.klasseID, registrering.tittel, mappe.tittel
     * <p>
     * ODATA Input:
     * mappe?$filter=klasse/klasseID eq '12/2' and
     * contains(tittel, 'Oslo') and registrering/tittel ne 'Brev fra dept.'
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1
     * JOIN
     * file_1.referenceClass AS class_1
     * JOIN file_1.referenceRecord AS record_1
     * WHERE
     * class_1.classId = :parameter_0 and
     * file_1.title like :parameter_0 and
     * record_1.title != :parameter_1
     * Additionally.
     * The parameter_0 parameter value should be:
     * 12/2
     * The parameter_1 parameter value should be:
     * Brev fra dept.
     * The parameter_0 parameter value should be:
     * Oslo
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLJoinQueryWithAndOrUsingBackets() {
        String odata = "mappe?$filter=" +
                "(klasse/klasseID eq '12/2' and contains(tittel, 'Oslo'))" +
                " or (registrering/tittel ne 'Brev fra dept.')";
        Query query = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT file_1 FROM File AS file_1 " +
                "JOIN file_1.referenceClass AS class_1 " +
                "JOIN file_1.referenceRecord AS record_1 WHERE " +
                "(class_1.classId = :parameter_0 and " +
                "file_1.title like :parameter_1) or (" +
                "record_1.title != :parameter_2)";
        assertEquals(query.getParameterValue("parameter_0"),
                "12/2");
        assertEquals(query.getParameterValue("parameter_1"),
                "%Oslo%");
        assertEquals(query.getParameterValue("parameter_2"),
                "Brev fra dept.");
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a concat eq query
     * Entity: saksmappe
     * Attribute: saksaar, sakssekvensnummer
     * <p>
     * ODATA Input:
     * saksmappe?$filter=
     * concat(concat(saksaar, '-'), sakssekvensnummer) eq '2020-10233'
     * Expected HQL:
     * SELECT caseFile_1 FROM
     * CaseFile AS caseFile_1
     * WHERE
     * concat(caseFile_1.saksaar, '-', sakssekvensnummer ) =
     * :parameter_0
     * Additionally the parameter_0 parameter value should be
     * 2020-10233
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLConcatExample() {
        String concatQuery = "saksmappe?$filter=" +
                "concat(concat(saksaar, '-'), sakssekvensnummer) " +
                "eq '2020-10233'";
        String hqlConcat = "SELECT casefile_1 FROM CaseFile AS casefile_1" +
                " WHERE" +
                " concat(casefile_1.caseYear,'-'," +
                "casefile_1.caseSequenceNumber) =" +
                " :parameter_0";
        Query query = oDataService.convertODataToHQL(concatQuery, "");
        assertEquals(query.getParameterValue("parameter_0"),
                "2020-10233");
        assertEquals(query.getQueryString(), hqlConcat);
    }

    /**
     * Check that it is possible to do a trim eq query
     * Entity: arkivskaper
     * Attribute: arkivskaperNavn
     * <p>
     * ODATA Input:
     * arkivskaper?$filter=trim(arkivskaperNavn) eq 'Oslo kommune'
     * <p>
     * Expected HQL:
     * SELECT fondscreator_1 FROM
     * FondsCreator AS fondscreator_1
     * WHERE
     * trim(fondscreator_1.fondsCreatorName) = :parameter_0
     * <p>
     * Additionally the parameter_0 parameter value should be
     * Oslo kommune
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLTrimExample() {
        String trimQuery = "arkivskaper?$filter= " +
                " trim(arkivskaperNavn)" +
                " eq 'Oslo kommune'";
        String hqlTrim = "SELECT fondscreator_1 FROM FondsCreator" +
                " AS fondscreator_1" +
                " WHERE" +
                " trim(fondscreator_1.fondsCreatorName) =" +
                " :parameter_0";
        Query query = oDataService.convertODataToHQL(trimQuery, "");
        assertEquals(query.getParameterValue("parameter_0"),
                "Oslo kommune");
        assertEquals(query.getQueryString(), hqlTrim);
    }

    /**
     * Check that it is possible to do a length ne query
     * Entity: dokumentobjekt
     * Attribute: sjekksum
     * Note: I would like have a test to test for the length of DNumber, but
     * until we figure out how ot handle dnumber we use this test.
     * <p>
     * ODATA Input:
     * dokumentobjekt?$filter=length(sjekksum) ne 64
     * Expected HQL:
     * SELECT documentObject_1 FROM
     * DocumentObject AS documentObject_1
     * WHERE
     * length(documentObject_1.checksum) != :parameter_0
     * Additionally the parameter_0 parameter value should be
     * 64
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLLength() {
        String eqQuery = "dokumentobjekt?$filter=" +
                " length(sjekksum) ne 64";
        Query query = oDataService.convertODataToHQL(eqQuery, "");
        String hql = "SELECT documentobject_1" +
                " FROM" +
                " DocumentObject AS documentobject_1" +
                " WHERE " +
                "length(documentobject_1.checksum) != :parameter_0";
        assertEquals(query.getParameterValue("parameter_0"),
                64);
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a tolower query
     * Entity: arkivskaper
     * Attribute: arkivskaperNavn
     * <p>
     * ODATA Input:
     * arkivskaper?$filter=tolower(arkivskaperNavn) eq 'oslo kommune'
     * Expected HQL:
     * SELECT fondscreator_1 FROM
     * FondsCreatorAS fondscreator_1
     * WHERE
     * lower(fondscreator_1.fondsCreatorName) = :parameter_0
     * Additionally the parameter_0 parameter value should be
     * oslo kommune
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLToLowerExample() {
        String trimQuery = "arkivskaper?$filter=" +
                "tolower(arkivskaperNavn) " +
                "eq 'oslo kommune'";
        String hqlTrim = "SELECT fondscreator_1 FROM FondsCreator" +
                " AS fondscreator_1" +
                " WHERE" +
                " lower(fondscreator_1.fondsCreatorName) =" +
                " :parameter_0";
        Query query = oDataService.convertODataToHQL(trimQuery, "");
        assertEquals(query.getParameterValue("parameter_0"),
                "oslo kommune");
        assertEquals(query.getQueryString(), hqlTrim);
    }

    /**
     * Check that it is possible to use multiple methods in a query
     * Entity: arkivskaper
     * Attribute: arkivskaperNavn
     * <p>
     * ODATA Input:
     * arkivskaper?$filter=trim(toupper(tolower(arkivskaperNavn))) eq
     * 'oslo kommune'
     * Expected HQL:
     * SELECT fondscreator_1 FROM
     * FondsCreatorAS fondscreator_1
     * WHERE
     * trim(upper(lower(fondscreator_1.fondsCreatorName))) =
     * :parameter_0
     * Additionally the parameter_0 parameter value should be
     * oslo kommune
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLMultipleMethods() {
        String trimQuery = "arkivskaper?$filter=" +
                "trim(toupper(tolower(arkivskaperNavn))) " +
                "eq 'oslo kommune'";
        String hqlTrim = "SELECT fondscreator_1 FROM FondsCreator" +
                " AS fondscreator_1" +
                " WHERE" +
                " trim(upper(lower(fondscreator_1.fondsCreatorName))) =" +
                " :parameter_0";
        Query query = oDataService.convertODataToHQL(trimQuery, "");
        assertEquals(query.getParameterValue("parameter_0"),
                "oslo kommune");
        assertEquals(query.getQueryString(), hqlTrim);
    }

    /**
     * Check that it is possible to do a toupper query
     * Entity: arkivskaper
     * Attribute: arkivskaperNavn
     * <p>
     * ODATA Input:
     * arkivskaper?$filter=toupper(arkivskaperNavn) eq
     * 'OSLO KOMMUNE'
     * Expected HQL:
     * SELECT fondsCreator FROM
     * FondsCreator AS fondsCreator
     * WHERE
     * upper(fondscreator_1.fondsCreatorName) = :parameter_0
     * Additionally the parameter_0 parameter value should be
     * OSLO KOMMUNE
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLToUpper() {
        String upperQuery = "arkivskaper?$filter=" +
                "toupper(arkivskaperNavn) " +
                "eq 'OSLO KOMMUNE'";
        String hqlTrim = "SELECT fondscreator_1 FROM FondsCreator" +
                " AS fondscreator_1" +
                " WHERE" +
                " upper(fondscreator_1.fondsCreatorName) =" +
                " :parameter_0";
        Query query = oDataService.convertODataToHQL(upperQuery, "");
        assertEquals(query.getParameterValue("parameter_0"),
                "OSLO KOMMUNE");
        assertEquals(query.getQueryString(), hqlTrim);
    }

    /**
     * Check that it is possible to do a round eq query
     * Entity: dokumentbeskrivelse
     * Attribute: dokumentnummer
     * Note: The Noark domain model does not contain any decimals so this
     * test may not be adequate
     * <p>
     * ODATA Input:
     * dokumentbeskrivelse?$filter=round(dokumentnummer) gt 5
     * Expected HQL:
     * SELECT documentObject_1 FROM
     * DocumentDescription AS documentDescription_1
     * WHERE
     * round(documentDescription_1.documentNumber) > :parameter_0
     * Additionally the parameter_0 parameter value should be
     * 5
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLRoundExample() {
        String roundQuery = "dokumentbeskrivelse?$filter=" +
                "round(dokumentnummer) gt 5";
        Query query = oDataService.convertODataToHQL(roundQuery, "");
        String hql = "SELECT documentDescription_1" +
                " FROM DocumentDescription AS documentDescription_1" +
                " WHERE" +
                " round(documentdescription_1.documentNumber)" +
                " > :parameter_0";
        assertEquals(query.getQueryString(), hql);
        assertEquals(query.getParameterValue("parameter_0"), 5);
    }

    /**
     * Check that it is possible to do a ceiling eq query
     * Entity: dokumentbeskrivelse
     * Attribute: dokumentnummer
     * Note: The Noark domain model does not contain any decimals so this
     * test may not be adequate
     * <p>
     * ODATA Input:
     * dokumentbeskrivelse?$filter=ceiling(dokumentnummer) ge 8
     * Expected HQL:
     * SELECT documentObject_1 FROM
     * DocumentDescription AS documentDescription_1
     * WHERE
     * ceiling(documentDescription_1.documentNumber) >= :parameter_0
     * Additionally the parameter_0 parameter value should be
     * 8
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLCeilingExample() {
        String ceilingQuery = "dokumentbeskrivelse?$filter=" +
                "ceiling(dokumentnummer) ge 8.0";
        Query query = oDataService.convertODataToHQL(ceilingQuery, "");
        String hql = "SELECT documentDescription_1" +
                " FROM DocumentDescription AS documentDescription_1" +
                " WHERE" +
                " ceiling(documentdescription_1.documentNumber)" +
                " >= :parameter_0";
        assertEquals(query.getQueryString(), hql);
        assertEquals(query.getParameterValue("parameter_0"), 8.0);
    }

    /**
     * Check that it is possible to do a floor eq query
     * Entity: dokumentbeskrivelse
     * Attribute: dokumentnummer
     * Note: The Noark domain model does not contain any decimals so this
     * test may not be adequate
     * <p>
     * ODATA Input:
     * dokumentbeskrivelse?$filter=floor(dokumentnummer) le 5
     * Expected HQL:
     * SELECT documentObject_1 FROM
     * DocumentDescription AS documentDescription_1
     * WHERE
     * floor(documentDescription_1.documentNumber) <= :parameter_0
     * Additionally the parameter_0 parameter value should be
     * 5
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLFloorExample() {
        String floorQuery = "dokumentbeskrivelse?$filter=" +
                "floor(dokumentnummer) le 5";
        Query query = oDataService.convertODataToHQL(floorQuery, "");
        String hql = "SELECT documentDescription_1" +
                " FROM DocumentDescription AS documentDescription_1" +
                " WHERE" +
                " floor(documentdescription_1.documentNumber)" +
                " <= :parameter_0";
        assertEquals(query.getQueryString(), hql);
        assertEquals(query.getParameterValue("parameter_0"), 5);
    }

    /**
     * Check that it is possible to do a query with filter after multiple
     * entities are identified
     * Entity: arkivdel, mappe
     * Attribute: mappe.opprettetDato
     * <p>
     * ODATA Input:
     * arkivdel/6654347b-077b-4241-a3ec-f351ef748250/mappe?$filter=
     * year(opprettetDato) lt 2020
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1
     * JOIN
     * file_1.referenceSeries AS series_1
     * WHERE
     * file_1.referenceSeries = :parameter_0 and
     * file_1.createdDate < :parameter_1
     * <p>
     * Additionally the parameter_0 parameter value should be
     * 6654347b-077b-4241-a3ec-f351ef748250
     * and parameter_1 should be
     * 2020
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEntityJoinBeforeFilter() {
        String joinQuery = "" +
                "arkivdel/6654347b-077b-4241-a3ec-f351ef748250/mappe?$filter=" +
                "year(opprettetDato) lt 2020";
        String hqlConcat = "SELECT file_1 FROM File AS file_1" +
                " JOIN" +
                " file_1.referenceSeries AS series_1" +
                " WHERE" +
                " series_1.systemId = :parameter_0 and" +
                " year(file_1.createdDate) < :parameter_1";
        Query query = oDataService.convertODataToHQL(joinQuery, "");
        assertEquals(query.getParameterValue("parameter_0"),
                fromString("6654347b-077b-4241-a3ec-f351ef748250"));
        assertEquals(query.getParameterValue("parameter_1"),
                Integer.valueOf("2020"));
        assertEquals(query.getQueryString(), hqlConcat);
    }

    /**
     * Check that it is possible to do a query with filter after multiple
     * entities are identified
     * Entity: arkiv/arkivdel, mappe
     * Attribute: mappe.opprettetDato
     * <p>
     * ODATA Input:
     * arkiv/f984d44f-02c2-4f8d-b3c2-6106094b15b0/arkivdel/6654347b-077b-4241-a3ec-f351ef748250/mappe?$filter=year(opprettetDato) lt 2020
     * <p>
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1 " +
     * JOIN file_1.referenceSeries AS series_1
     * JOIN series_1.referenceFonds AS fonds_1
     * WHERE
     * file_1.referenceSeries = :parameter_0 and
     * series_1.referenceFonds = :parameter_1 and
     * year(file_1.createdDate) < :parameter_2
     * Additionally the parameter_0 parameter value should be
     * 6654347b-077b-4241-a3ec-f351ef748250
     * and parameter_1 should be
     * f984d44f-02c2-4f8d-b3c2-6106094b15b0
     * and parameter_1 should be
     * 2020
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEntityJoinBeforeFilterThreeLevels() {
        String joinQuery = "" +
                "arkiv/f984d44f-02c2-4f8d-b3c2-6106094b15b0/" +
                "arkivdel/6654347b-077b-4241-a3ec-f351ef748250/" +
                "mappe?$filter=year(opprettetDato) lt 2020";
        String hqlConcat = "SELECT file_1 FROM File AS file_1" +
                " JOIN" +
                " file_1.referenceSeries AS series_1" +
                " JOIN" +
                " series_1.referenceFonds AS fonds_1" +
                " WHERE" +
                " series_1.systemId = :parameter_0 and" +
                " fonds_1.systemId = :parameter_1 and" +
                " year(file_1.createdDate) < :parameter_2";
        Query query = oDataService.convertODataToHQL(joinQuery, "");
        assertEquals(query.getParameterValue("parameter_0"),
                fromString("6654347b-077b-4241-a3ec-f351ef748250"));
        assertEquals(query.getParameterValue("parameter_1"),
                fromString("f984d44f-02c2-4f8d-b3c2-6106094b15b0"));
        assertEquals(query.getParameterValue("parameter_2"),
                Integer.valueOf("2020"));
        assertEquals(query.getQueryString(), hqlConcat);
    }

    /**
     * Check that it is possible to do a query with filter join after multiple
     * entities are identified
     * Entity: arkivdel, mappe, klasse, klassifikasjonssystem
     * Attribute: klassifikasjonssystem.tittel
     * <p>
     * ODATA Input:
     * arkivdel/6654347b-077b-4241-a3ec-f351ef748250/mappe?$filter=startswith(klasse/klassifikasjonssystem/tittel, 'Gårds- og bruksnummer')
     * <p>
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1
     * JOIN file_1.referenceSeries AS series_1" +
     * JOIN file_1.referenceClass AS class_1" +
     * JOIN class_1.referenceClassificationSystem AS classificationsystem_1" +
     * WHERE
     * series_1.systemId = :parameter_0 and
     * classificationsystem_1.title like :parameter_1
     * Additionally the parameter_0 parameter value should be
     * 6654347b-077b-4241-a3ec-f351ef748250
     * and parameter_1 should be
     * Gårds- og bruksnummer%
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEntityJoinBeforeFilterJoinAfterFilter() {
        String odata = "arkivdel/6654347b-077b-4241-a3ec-f351ef748250/" +
                "mappe?$filter=startswith(" +
                "klasse/klassifikasjonssystem/tittel, 'Gårds- og bruksnummer')";
        String hql = "SELECT file_1 FROM File AS file_1" +
                " JOIN file_1.referenceSeries AS series_1" +
                " JOIN file_1.referenceClass AS class_1" +
                " JOIN class_1.referenceClassificationSystem AS classificationsystem_1" +
                " WHERE series_1.systemId = :parameter_0 and" +
                " classificationsystem_1.title like :parameter_1";

        Query query = oDataService.convertODataToHQL(odata, "");
        assertEquals(query.getParameterValue("parameter_0"),
                fromString("6654347b-077b-4241-a3ec-f351ef748250"));
        assertEquals(query.getParameterValue("parameter_1"),
                "Gårds- og bruksnummer%");
        assertEquals(query.getQueryString(), hql);
    }

    /**
     * Just a placeholder to manually test HQL syntax and to check that the
     * query is possible given the Noark domain model.
     */
    @Test
    @Transactional
    public void testManualQuery() {
        // select concat(c.firstname, c.lastname) as fullname from Contact c
        String hqlTestJoin = "FROM File as file_1 JOIN " +
                "file_1.referenceClass AS class_1 JOIN file_1.referenceRecord" +
                " AS record_1 WHERE class_1.classId = '12/2' and " +
                "file_1.title like :parameter_0 and " +
                "record_1.title = 'Brev fra dept.' ";

        Session session = emf.unwrap(Session.class);
        session.createQuery(hqlTestJoin);
    }
}
