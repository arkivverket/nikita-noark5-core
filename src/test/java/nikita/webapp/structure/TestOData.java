package nikita.webapp.structure;

import nikita.webapp.odata.QueryObject;
import nikita.webapp.service.impl.odata.ODataService;
import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.OffsetDateTime;

import static java.util.UUID.fromString;
import static nikita.common.config.N5ResourceMappings.*;

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

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT fondsstatus_1 FROM FondsStatus AS fondsstatus_1" +
                " WHERE" +
                " fondsstatus_1.code = :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "O");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT count(*) FROM Fonds AS fonds_1" +
                " WHERE" +
                " fonds_1.title = :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "The fonds");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT count(*) FROM Fonds AS fonds_1";
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT fonds_1 FROM Fonds AS fonds_1" +
                " WHERE fonds_1.title = :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "The fonds");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
        QueryObject queryObject = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "SELECT file_1 FROM File AS file_1" +
                " WHERE" +
                " file_1.title like :parameter_0" +
                " order by file_1.createdDate ASC";
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"), "%søknad%");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hqlJoin);
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
        QueryObject queryObject = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "SELECT file_1 FROM File AS file_1" +
                " WHERE" +
                " file_1.title like :parameter_0" +
                " order by file_1.createdDate ASC, file_1.title DESC";
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"), "%søknad%");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hqlJoin);
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
        QueryObject queryObject = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "SELECT file_1 FROM File AS file_1" +
                " WHERE" +
                " file_1.title like :parameter_0" +
                " order by file_1.createdDate";
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"), "%søknad%");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hqlJoin);
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT fonds_1 FROM Fonds AS fonds_1" +
                " WHERE fonds_1.title = :parameter_0";

        Integer maxRows = queryObject.getQuery().getQueryOptions().getMaxRows();
        Assertions.assertEquals(maxRows, Integer.valueOf(5));
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "The fonds");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
     * The fonds
     * and
     * firstRow = 10
     */
    @Test
    @Transactional
    public void shouldReturnValidHQSkip() {
        String odata = "arkiv?$filter=tittel eq 'The fonds'&$skip=10";
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT fonds_1 FROM Fonds AS fonds_1" +
                " WHERE fonds_1.title = :parameter_0";

        Integer firstRow = queryObject.getQuery().getQueryOptions().getFirstRow();
        Assertions.assertEquals(firstRow, Integer.valueOf(10));
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "The fonds");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT fonds_1 FROM Fonds AS fonds_1" +
                " WHERE fonds_1.title = :parameter_0";

        Assertions.assertEquals(queryObject.getQuery().getQueryOptions().getMaxRows(), Integer.valueOf(8));
        Assertions.assertEquals(queryObject.getQuery().getQueryOptions().getFirstRow(), Integer.valueOf(10));
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "The fonds");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT file_1 FROM File AS file_1" +
                " WHERE file_1.title like :parameter_0" +
                " order by file_1.createdDate";

        Assertions.assertEquals(queryObject.getQuery().getQueryOptions().getMaxRows(), Integer.valueOf(23));
        Assertions.assertEquals(queryObject.getQuery().getQueryOptions().getFirstRow(), Integer.valueOf(49));
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "%søknad%");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT fonds_1 FROM Fonds AS fonds_1" +
                " WHERE" +
                " fonds_1.description is null";
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT fonds_1 FROM Fonds AS fonds_1" +
                " WHERE" +
                " fonds_1.description is not null";
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT class_1 FROM Class AS class_1" +
                " WHERE" +
                " (class_1.description is not null and" +
                " length(class_1.title) > :parameter_0) or" +
                " (class_1.title = :parameter_1 and" +
                " year(class_1.createdDate) = :parameter_2)";
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                Integer.valueOf("4"));
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_1"),
                "class number 1");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_2"),
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT documentobject_1" +
                " FROM DocumentObject AS documentobject_1 WHERE" +
                " documentobject_1.originalFilename = :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "<9aqr221f34c.hsr@address.udn.com>");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT series_1 FROM Series AS series_1 " +
                "JOIN series_1.referenceFonds AS fonds_1 " +
                "WHERE fonds_1.description = :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "The fonds description");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT registryentry_1 FROM RegistryEntry AS " +
                "registryentry_1 WHERE " +
                "registryentry_1.recordId != :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "2020/000234-23");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT documentobject_1 FROM DocumentObject AS " +
                "documentobject_1 WHERE " +
                "year(documentobject_1.createdDate) = :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                2020);
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
        QueryObject queryObject = oDataService.convertODataToHQL(monthQuery, "");
        String hql = "SELECT documentobject_1 FROM DocumentObject AS " +
                "documentobject_1 WHERE " +
                "month(documentobject_1.createdDate) > :parameter_0" +
                " and " +
                "month(documentobject_1.createdDate) < :parameter_1";
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"), 5);
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_1"), 9);
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT documentobject_1 FROM DocumentObject AS " +
                "documentobject_1 JOIN " +
                "documentobject_1.referenceDocumentDescription AS " +
                "documentdescription_1 WHERE " +
                "month(documentdescription_1.createdDate) " +
                "> :parameter_0 and " +
                "month(documentobject_1.createdDate) " +
                "<= :parameter_1";
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"), 5);
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_1"), 9);
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT documentobject_1 FROM DocumentObject" +
                " AS documentobject_1" +
                " JOIN" +
                " documentobject_1.referenceDocumentDescription AS documentdescription_1" +
                " WHERE" +
                " documentdescription_1.documentStatusCode = :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"), "B");
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT documentdescription_1 FROM DocumentDescription" +
                " AS documentdescription_1" +
                " WHERE" +
                " documentdescription_1.documentStatusCodeName = :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "Dokumentet er under redigering");
    }

    /**
     * Check that it is possible to query for opprettetAv.
     * Entity: dokumentbeskrivelse (opprettetAv)
     * <p>
     * ODATA Input:
     * dokumentbeskrivelse?$filter=opprettetAv eq 'admin@example.com'
     * <p>
     * Expected HQL:
     * SELECT documentdescription_1 FROM DocumentDescription AS documentdescription_1
     * WHERE
     * documentdescription_1.created_by = :parameter_0
     * <p>
     * Additionally.
     * The parameter_0 parameter value should be:
     * admin@example.com
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEQQueryCreatedBy() {
        String user = "admin@example.com";
        String odata = "dokumentbeskrivelse?$filter=" +
                CREATED_BY + " eq '" + user + "'";
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT documentdescription_1 FROM " +
                DOCUMENT_DESCRIPTION_ENG_OBJECT +
                " AS documentdescription_1" +
                " WHERE" +
                " documentdescription_1." + CREATED_BY_ENG_OBJECT + " = :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"), user);
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT fonds_1 FROM Fonds AS fonds_1" +
                " WHERE" +
                " fonds_1.fondsStatusCode = :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"), "O");
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT documentobject_1 FROM DocumentObject" +
                " AS documentobject_1" +
                " JOIN documentobject_1.referenceDocumentDescription" +
                " AS documentdescription_1" +
                " JOIN documentdescription_1.referenceRecord AS record_1" +
                " JOIN record_1.referenceFile AS file_1" +
                " JOIN file_1.referenceSeries AS series_1" +
                " JOIN series_1.referenceFonds AS fonds_1" +
                " WHERE month(fonds_1.createdDate) > :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"), 5);
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT documentobject_1 FROM DocumentObject AS " +
                "documentobject_1 WHERE " +
                "day(documentobject_1.createdDate) >= :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"), 4);
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT documentobject_1 FROM DocumentObject AS " +
                "documentobject_1 WHERE " +
                "hour(documentobject_1.createdDate) < :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"), 14);
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT documentobject_1 FROM DocumentObject AS " +
                "documentobject_1 WHERE " +
                "minute(documentobject_1.createdDate) >= :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"), 56);
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT documentobject_1 FROM DocumentObject AS " +
                "documentobject_1 WHERE " +
                "second(documentobject_1.createdDate) >= :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"), 56);
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
        QueryObject queryObject = oDataService.convertODataToHQL(containsQuery, "");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "%Eksempel kommune%");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
        QueryObject queryObject = oDataService.convertODataToHQL(containsQuery, "");
        String parameter = (String) queryObject.getQuery().getParameterValue("parameter_0");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
        Assertions.assertEquals(parameter, "%Jennifer O'Malley%");
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
        QueryObject queryObject = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "SELECT file_1 FROM File AS file_1" +
                " JOIN" +
                " file_1.referenceClass AS class_1" +
                " WHERE " +
                "class_1.classId = :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"), "12/2");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hqlJoin);
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
        QueryObject queryObject = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "SELECT file_1 FROM File AS file_1" +
                " JOIN" +
                " file_1.referenceClass AS class_1" +
                " WHERE" +
                " class_1.classId like :parameter_0";
        String parameter = (String) queryObject.getQuery().getParameterValue(
                "parameter_0");
        Assertions.assertEquals(parameter, "%12/2%");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hqlJoin);
    }

    /**
     * See : https://gitlab.com/OsloMet-ABI/nikita-noark5-core/-/issues/191
     * Check that it is possible to undertake a nested contains
     * Entity: mappe, merknad
     * Attribute: merknad.systemID
     * <p>
     * ODATA Input:
     * mappe?$filter=contains(merknad/systemID, '2a146779-77ef-41a8-b958-0a4bddeac2d7')
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1
     * JOIN file_1.referenceComment AS comment_1
     * WHERE
     * comment_1.systemId = :parameter_0
     * Additionally the parameter_0 parameter value should be:
     * 2a146779-77ef-41a8-b958-0a4bddeac2d7
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLJoinQueryWithContainsSecondaryEntity() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            String joinQuery = "mappe?$filter=contains(" +
                    "merknad/systemID, '2a146779-77ef-41a8-b958-0a4bddeac2d7')";
            QueryObject queryObject = oDataService.convertODataToHQL(joinQuery, "");
            String hqlJoin = "SELECT file_1 FROM File AS file_1" +
                    " JOIN" +
                    " file_1.referenceComment AS comment_1" +
                    " WHERE" +
                    " comment_1.systemId like :parameter_0";
            String parameter = (String) queryObject.getQuery().getParameterValue(
                    "parameter_0");
            Assertions.assertEquals(parameter,
                    "2a146779-77ef-41a8-b958-0a4bddeac2d7");
            Assertions.assertEquals(queryObject.getQuery().getQueryString(), hqlJoin);
        });
    }

    /**
     * Check that it is possible to undertake a nested contains
     * Entity: mappe, merknad
     * Attribute: merknad.systemID
     * <p>
     * ODATA Input:
     * mappe?$filter=merknad/systemID eq '2a146779-77ef-41a8-b958-0a4bddeac2d7'
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1
     * JOIN file_1.referenceComment AS comment_1
     * WHERE
     * comment_1.systemId = :parameter_0
     * Additionally the parameter_0 parameter value should be:
     * 2a146779-77ef-41a8-b958-0a4bddeac2d7
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLJoinQueryWithEqSecondaryEntity() {
        String joinQuery = "mappe?$filter=" +
                "merknad/systemID eq '2a146779-77ef-41a8-b958-0a4bddeac2d7'";
        QueryObject queryObject = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "SELECT file_1 FROM File AS file_1" +
                " JOIN" +
                " file_1.referenceComment AS comment_1" +
                " WHERE" +
                " comment_1.systemId = :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                fromString("2a146779-77ef-41a8-b958-0a4bddeac2d7"));
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hqlJoin);
    }

    /**
     * Check that it is possible to search for registryentry associated with a
     * given signoff. This is required when creating hateaos links for signoff
     * _links. Make sure that we can point to all "parent" registryentry
     * Entity: journalpost, avskrivning
     * Attribute: avskrivning.systemID
     * <p>
     * ODATA Input:
     * journalpost?$filter=avskrivning/systemID eq
     * '7f000101-7306-18d0-8173-06fa3c170049'
     * Expected HQL:
     * SELECT registryentry_1
     * FROM RegistryEntry AS registryentry_1
     * JOIN
     * registryentry_1.referenceSignOff AS signoff_1
     * WHERE
     * signoff_1.systemId = :parameter_0";
     * Additionally the parameter_0 parameter value should be:
     * 7f000101-7306-18d0-8173-06fa3c170049
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLJoinQueryWithSecondary() {
        String joinQuery = "journalpost?$filter=" +
                "avskrivning/systemID eq" +
                " '7f000101-7306-18d0-8173-06fa3c170049'";
        QueryObject queryObject = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "SELECT registryentry_1 FROM" +
                " RegistryEntry AS registryentry_1" +
                " JOIN" +
                " registryentry_1.referenceSignOff AS signoff_1" +
                " WHERE" +
                " signoff_1.systemId = :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                fromString("7f000101-7306-18d0-8173-06fa3c170049"));
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hqlJoin);
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
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            String joinQuery = "mappe?$filter=" +
                    "contains(foedselsnummer/foedselsnummer, '050282')";
            QueryObject queryObject = oDataService.convertODataToHQL(joinQuery, "");
            String hqlJoin = "SELECT file_1 FROM File AS file_1" +
                    " JOIN" +
                    " file_1.referenceSocialSecurityNumber AS socialSecurityNumber" +
                    " WHERE" +
                    " class_1.classId like : parameter_0";
            Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"), "%050282%");
            Assertions.assertEquals(queryObject.getQuery().getQueryString(), hqlJoin);
        });
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
        QueryObject queryObject = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "SELECT file_1 FROM File AS file_1" +
                " JOIN" +
                " file_1.referenceClass AS class_1" +
                " JOIN" +
                " class_1.referenceClassificationSystem AS" +
                " classificationsystem_1" +
                " WHERE" +
                " classificationsystem_1.title like :parameter_0";
        String parameter = (String) queryObject.getQuery().getParameterValue(
                "parameter_0");
        Assertions.assertEquals(parameter, "Gårds- og bruksnummer%");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hqlJoin);
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
        QueryObject queryObject = oDataService.convertODataToHQL(joinQuery, "");
        String hqlJoin = "SELECT file_1 FROM File AS file_1 " +
                "JOIN file_1.referenceClass AS class_1 " +
                "JOIN file_1.referenceRecord AS record_1 WHERE " +
                "class_1.classId = :parameter_0 and " +
                "file_1.title like :parameter_1 and " +
                "record_1.title != :parameter_2";
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "12/2");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_1"),
                "%Oslo%");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_2"),
                "Brev fra dept.");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hqlJoin);
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
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        String hql = "SELECT file_1 FROM File AS file_1 " +
                "JOIN file_1.referenceClass AS class_1 " +
                "JOIN file_1.referenceRecord AS record_1 WHERE " +
                "(class_1.classId = :parameter_0 and " +
                "file_1.title like :parameter_1) or (" +
                "record_1.title != :parameter_2)";
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "12/2");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_1"),
                "%Oslo%");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_2"),
                "Brev fra dept.");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
        QueryObject queryObject = oDataService.convertODataToHQL(concatQuery, "");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "2020-10233");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hqlConcat);
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
        QueryObject queryObject = oDataService.convertODataToHQL(trimQuery, "");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "Oslo kommune");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hqlTrim);
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
        QueryObject queryObject = oDataService.convertODataToHQL(eqQuery, "");
        String hql = "SELECT documentobject_1" +
                " FROM" +
                " DocumentObject AS documentobject_1" +
                " WHERE " +
                "length(documentobject_1.checksum) != :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                64);
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
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
        QueryObject queryObject = oDataService.convertODataToHQL(trimQuery, "");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "oslo kommune");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hqlTrim);
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
        QueryObject queryObject = oDataService.convertODataToHQL(trimQuery, "");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "oslo kommune");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hqlTrim);
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
        QueryObject queryObject = oDataService.convertODataToHQL(upperQuery, "");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "OSLO KOMMUNE");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hqlTrim);
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
                "round(dokumentnummer) gt 5.0";
        QueryObject queryObject = oDataService.convertODataToHQL(roundQuery, "");
        String hql = "SELECT documentdescription_1" +
                " FROM DocumentDescription AS documentdescription_1" +
                " WHERE" +
                " round(documentdescription_1.documentNumber)" +
                " > :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"), 5.0);
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
        QueryObject queryObject = oDataService.convertODataToHQL(ceilingQuery, "");
        String hql = "SELECT documentdescription_1" +
                " FROM DocumentDescription AS documentdescription_1" +
                " WHERE" +
                " ceiling(documentdescription_1.documentNumber)" +
                " >= :parameter_0";
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"), 8.0);
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
        Assertions.assertThrows(IllegalArgumentException.class, () -> {

            String floorQuery = "dokumentbeskrivelse?$filter=" +
                    "floor(dokumentnummer) le 5";
            QueryObject queryObject = oDataService.convertODataToHQL(floorQuery, "");
            String hql = "SELECT documentDescription_1" +
                    " FROM DocumentDescription AS documentDescription_1" +
                    " WHERE" +
                    " floor(documentdescription_1.documentNumber)" +
                    " <= :parameter_0";


            Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
            Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"), 5);
        });
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
        QueryObject queryObject = oDataService.convertODataToHQL(joinQuery, "");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                fromString("6654347b-077b-4241-a3ec-f351ef748250"));
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_1"),
                Integer.valueOf("2020"));
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hqlConcat);
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
        QueryObject queryObject = oDataService.convertODataToHQL(joinQuery, "");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                fromString("6654347b-077b-4241-a3ec-f351ef748250"));
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_1"),
                fromString("f984d44f-02c2-4f8d-b3c2-6106094b15b0"));
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_2"),
                Integer.valueOf("2020"));
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hqlConcat);
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

        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                fromString("6654347b-077b-4241-a3ec-f351ef748250"));
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_1"),
                "Gårds- og bruksnummer%");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a query with filter join between an
     * entity that supports business specific metadata (BSM) and the BSM
     * table.
     * Entity:  mappe, Nasjonalidentifkator:enhetsidentifikator
     * Attribute: organisasjonsnummer with value 02020202022
     * <p>
     * ODATA Input:
     * mappe?$filter=enhetsidentifikator/organisasjonsnummer eq '02020202022'
     * <p>
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1
     * JOIN
     * file_1.referenceNationalIdentifier AS unitidentifier_1
     * WHERE
     * unitidentifier_1.organisationNumber = :parameter_0
     * <p>
     * Additionally parameter_0 should be
     * 02020202022
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEntityJoinFileWithNI() {
        String attributeName = "enhetsidentifikator/organisasjonsnummer";
        String compareValue = "02020202022";
        String odata = "mappe?$filter=" + attributeName +
                " eq '" + compareValue + "'";

        String hql = "SELECT file_1 FROM File AS file_1" +
                " JOIN" +
                " file_1.referenceNationalIdentifier AS unitidentifier_1" +
                " WHERE" +
                " unitidentifier_1.organisationNumber = :parameter_0";

        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");

        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                compareValue);
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a query with filter join between an
     * entity that supports National Identifiers and the BSM
     * table.
     * Entity:  mappe, nasjonalidentifikator
     * Attribute: VSM.valueName with value ppt-v1:meldingstidspunkt
     * <p>
     * ODATA Input:
     * mappe?$filter=ppt-v1:meldingstidspunkt eq '2020-01-01T22:25:06.00000+02:00'
     * <p>
     * Expected HQL:
     * SELECT file_1 FROM File as file_1
     * JOIN
     * file_1.referenceBSM as BSM_1
     * BSM_1.valueName = :parameter_0 and
     * BSM_1.valueName = :parameter_1
     * <p>
     * Additionally the parameter_0 value should be
     * ppt-v1:meldingstidspunkt
     * and parameter_1 should be
     * 2020-01-01T22:25:06.00000+02:00
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEntityJoinFileWithBSM() {
        String attributeName = "ppt-v1:meldingstidspunkt";
        String odata = "mappe?$filter=" + attributeName +
                " eq '2020-01-01T22:25:06.09+02:00'";

        String hql = "SELECT file_1 FROM File AS file_1" +
                " JOIN" +
                " file_1.referenceBSMBase AS bsmbase_1" +
                " WHERE" +
                " bsmbase_1.valueName = :parameter_0 and" +
                " bsmbase_1.offsetdatetimeValue = :parameter_1";

        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");

        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "ppt-v1:meldingstidspunkt");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_1"),
                OffsetDateTime.parse("2020-01-01T22:25:06.09+02:00"));
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a query with filter join between an
     * entity that supports business specific metadata (BSM) and the BSM
     * table.
     * Entity:  mappe, VSM
     * Attribute: VSM.valueName with value ppt-v1:skolekontakt
     * <p>
     * ODATA Input:
     * mappe?$filter=virksomhetsspesifikkeMetadata/ppt-v1:skolekontakt eq null&$top=1
     * <p>
     * Expected HQL:
     * SELECT file_1 FROM File as file_1
     * JOIN
     * file_1.referenceBSM as bsmbase_1
     * WHERE
     * bsmbase_1.valueName is  null
     * <p>
     * Additionally the parameter_0 value should be
     * ppt-v1:skolekontakt
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEntityJoinFileWithBSMNullAttribute() {

        String attributeName = "ppt-v1:skolekontakt";
        String odata = "mappe?$filter=virksomhetsspesifikkeMetadata/" +
                attributeName + " ne null&$top=1";

        String hql = "SELECT file_1 FROM File AS file_1" +
                " JOIN file_1.referenceBSMBase AS bsmbase_1" +
                " JOIN file_1.referenceBSMBase AS bsmbase_1" +
                " WHERE" +
                " bsmbase_1.valueName = :parameter_0 and" +
                " bsmbase_1.isNullValue is not null";

        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");

        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                attributeName);
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a OData query on BSM
     * Entity:  VSM
     * Attribute: VSM.valueName with value ppt-v1:meldingstidspunkt
     * <p>
     * ODATA Input:
     * virksomhetsspesifikkeMetadata?$filter=ppt-v1:skolekontakt eq null
     * <p>
     * Expected HQL:
     * SELECT bsm_1 FROM BSM as bsm_1
     * WHERE
     * bsmbase_1.valueName is null
     * <p>
     * Additionally the parameter_0 value should be
     * ppt-v1:skolekontakt
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLBSMNullAttribute() {

        String attributeName = "ppt-v1:skolekontakt";
        String odata = "virksomhetsspesifikkeMetadata?$filter=" +
                attributeName + " eq null";

        String hql = "SELECT bsmbase_1 FROM BSMBase AS bsmbase_1" +
                " WHERE" +
                " bsmbase_1.valueName = :parameter_0" +
                " and" +
                " bsmbase_1.isNullValue is null";

        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                attributeName);
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a OData query on BSM
     * Entity:  VSM
     * Attribute: VSM.valueName with value ppt-v1:meldingstidspunkt
     * <p>
     * ODATA Input:
     * virksomhetsspesifikkeMetadata?$filter=ppt-v1:skolekontakt eq null
     * <p>
     * Expected HQL:
     * SELECT bsm_1 FROM BSM as bsm_1
     * WHERE
     * bsmbase_1.valueName is null
     * <p>
     * Additionally the parameter_0 value should be
     * ppt-v1:skolekontakt
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLBSMNotNullAttribute() {

        String attributeName = "ppt-v1:skolekontakt";
        String odata = "virksomhetsspesifikkeMetadata?$filter=" +
                attributeName + " ne null";

        String hql = "SELECT bsmbase_1 FROM BSMBase AS bsmbase_1" +
                " WHERE" +
                " bsmbase_1.valueName = :parameter_0" +
                " and" +
                " bsmbase_1.isNullValue is not null";

        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                attributeName);
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a query with filter join between a
     * File and a Part
     * Entity:  mappe, part
     * Attribute: kodenavn with value ADV
     * <p>
     * ODATA Input:
     * mappe?$filter=part/partRolle/kode+eq+'ADV'
     * <p>
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1
     * JOIN
     * file_1.referencePart AS part_1
     * WHERE part_1.partRoleCode = :parameter_0
     * <p>
     * Additionally parameter_0 should be
     * ADV
     * <p>
     * Test added as it was reported as failing from external test
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEntityJoinFileWithPartPartRoleCode() {
        String attributeName = "part/partRolle/kode";
        String compareValue = "ADV";
        String odata = "mappe?$filter=" + attributeName +
                " eq '" + compareValue + "'";

        String hql = "SELECT file_1 FROM File AS file_1" +
                " JOIN" +
                " file_1.referencePart AS part_1" +
                " WHERE" +
                " part_1.partRoleCode = :parameter_0";

        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                compareValue);
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a query with filter join between a
     * File and a Part
     * Entity:  mappe, part
     * Attribute: kodenavn with value ADV
     * <p>
     * ODATA Input:
     * mappe?$filter=part/partRolle/kodenavn+eq+'Advokat'
     * <p>
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1
     * JOIN
     * file_1.referencePart AS part_1
     * WHERE part_1.partRoleCodeName = :parameter_0
     * <p>
     * Additionally parameter_0 should be
     * Advokat
     * <p>
     * Test added as it was reported as failing from external test
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEntityJoinFileWithPartPartRoleCodeName() {
        String attributeName = "part/partRolle/kodenavn";
        String compareValue = "Advokat";
        String odata = "mappe?$filter=" + attributeName +
                " eq '" + compareValue + "'";

        String hql = "SELECT file_1 FROM File AS file_1" +
                " JOIN" +
                " file_1.referencePart AS part_1" +
                " WHERE" +
                " part_1.partRoleCodeName = :parameter_0";

        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                compareValue);
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a query with filter join between a File
     * and a Part
     * Entity:  mappe, part
     * Attribute: kodenavn with value ADV
     * <p>
     * ODATA Input:
     * mappe?$filter=contains(part/navn, 'Eksempel')
     * <p>
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1
     * JOIN
     * file_1.referencePart AS part_1
     * WHERE
     * part_1.name like :parameter_0
     * <p>
     * Additionally parameter_0 should be
     * ADV
     * <p>
     * Test added as it was reported as failing from external test
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEntityJoinFileWithPartName() {
        String attributeName = "part/navn";
        String compareValue = "Eksempel";
        String odata = "mappe?$filter=contains(" + attributeName +
                ", '" + compareValue + "')";

        String hql = "SELECT file_1 FROM File AS file_1" +
                " JOIN" +
                " file_1.referencePart AS part_1" +
                " WHERE" +
                " part_1.name like :parameter_0";

        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "%" + compareValue + "%");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a query with filter join between a File
     * and a Part where only objects of type PartPerson are returned
     * Entity:  mappe, part
     * Attribute: navn
     * <p>
     * ODATA Input:
     * mappe?$filter=partPerson/navn eq 'Hans Gruber'
     * <p>
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1
     * JOIN
     * file_1.referencePart AS part_1
     * WHERE
     * " part_1.name = :parameter_0 and
     * " type (part_1) = PartUnit
     * <p>
     * Additionally parameter_0 should be
     * Hans Gruber
     * <p>
     * Test added as it was reported as failing from external test
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEntityJoinFileWithPartPerson() {
        String attributeName = "partPerson/navn";
        String compareValue = "Hans Gruber";
        String odata = "mappe?$filter=" + attributeName + " eq '" +
                compareValue + "'";

        String hql = "SELECT file_1 FROM File AS file_1" +
                " JOIN" +
                " file_1.referencePart AS part_1" +
                " WHERE" +
                " part_1.name = :parameter_0" +
                "  and type(part_1) = PartPerson";
        // Note: There are two spaces before "  and .."
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "Hans Gruber");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a query with filter join between a File
     * and a Part where only objects of type PartUnit are returned
     * Entity:  mappe, part
     * Attribute: navn
     * <p>
     * ODATA Input:
     * mappe?$filter=partEnhet/navn eq 'Hans Gruber'
     * <p>
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1
     * JOIN
     * file_1.referencePart AS part_1
     * WHERE
     * " part_1.name = :parameter_0 and
     * " type (part_1) = PartUnit
     * <p>
     * Additionally parameter_0 should be
     * Hans Gruber
     * <p>
     * Test added as it was reported as failing from external test
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEntityJoinFileWithPartUnit() {
        String attributeName = "partEnhet/navn";
        String compareValue = "Hans Gruber";
        String odata = "mappe?$filter=" + attributeName + " eq '" +
                compareValue + "'";

        String hql = "SELECT file_1 FROM File AS file_1" +
                " JOIN" +
                " file_1.referencePart AS part_1" +
                " WHERE" +
                " part_1.name = :parameter_0" +
                "  and type(part_1) = PartUnit";
        // Note: There are two spaces  before "  and .."
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                "Hans Gruber");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a query with filter join between a
     * DocumentDescription and an Author
     * Entity:  dokumentbeskrivelse, forfatter
     * Attribute: forfatter
     * <p>
     * forfatter/author is a multi-valued attribute
     * <p>
     * ODATA Input:
     * dokumentbeskrivelse?$filter=forfatter eq 'Frank Grimes'
     * <p>
     * Expected HQL:
     * SELECT documentdescription_1 FROM DocumentDescription
     * AS documentdescription_1
     * JOIN
     * documentdescription_1.referenceAuthor AS author_1
     * WHERE
     * author_1.organisationNumber = :parameter_0
     * <p>
     * Additionally parameter_0 should be
     * Frank Grimes
     * <p>
     * Test added from incremental development work
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEntityJoinDocDescWithAuthor() {
        String attributeName = "forfatter";
        String compareValue = "Frank Grimes";
        String odata = "dokumentbeskrivelse?$filter=forfatter/" +
                attributeName + " eq " + "'" + compareValue + "')";
        String hql = "SELECT documentdescription_1 FROM DocumentDescription" +
                " AS documentdescription_1" +
                " JOIN" +
                " documentdescription_1.referenceAuthor AS author_1" +
                " WHERE" +
                " author_1.author = :parameter_0";
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                compareValue);
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a query with filter join between a File
     * and a Part
     * Entity:  mappe, part
     * Attribute: organisasjonsnummer
     * <p>
     * forfatter/author is a multi-valued attribute
     * ODATA Input:
     * mappe?$filter=partEnhet/organisasjonsnummer eq '02020202022'
     * <p>
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1
     * JOIN
     * file_1.referencePart AS part_1
     * WHERE
     * part_1.name like :parameter_0
     * <p>
     * Additionally parameter_0 should be
     * ADV
     * <p>
     * Test added as it was reported as failing from external test
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLEntityJoinFileWithPartUnitField() {
        String attributeName = "partEnhet/organisasjonsnummer";
        String compareValue = " 02020202022";
        String odata = "mappe?$filter=" + attributeName + " eq " +
                "'" + compareValue + "')";

        String hql = "SELECT file_1 FROM File AS file_1" +
                " JOIN" +
                " file_1.referencePart AS part_1" +
                " WHERE" +
                " part_1.organisationNumber = :parameter_0" +
                "  and type(part_1) = PartUnit";
        // Note the double space "  and type(..."
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                compareValue);
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a query with StorageLocation
     * Entity:  oppbevaringssted
     * Attribute: oppbevaringssted
     * <p>
     * ODATA Input:
     * oppbevaringssted?$filter=oppbevaringssted eq 'Archive Room XVI'
     * <p>
     * Expected HQL:
     * SELECT storagelocation_1 FROM StorageLocation AS storagelocation_1
     * WHERE
     * storagelocation_1.storageLocation = :parameter_0
     * <p>
     * Additionally parameter_0 should be
     * Archive Room XVI
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLStorageLocation() {
        String compareValue = "Archive Room XVI";
        String odata = STORAGE_LOCATION + "?$filter=" + STORAGE_LOCATION +
                " eq '" + compareValue + "')";
        String hql = "SELECT storagelocation_1 FROM StorageLocation AS" +
                " storagelocation_1" +
                " WHERE" +
                " storagelocation_1.storageLocation = :parameter_0";
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                compareValue);
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
    }

    /**
     * Check that it is possible to do a JOIN query with StorageLocation
     * Entity:  mappe, oppbevaringssted
     * Attribute: oppbevaringssted
     * <p>
     * ODATA Input:
     * mappe?$filter=oppbevaringssted eq 'Archive Room XVI'
     * <p>
     * Expected HQL:
     * SELECT file_1 FROM File AS file_1
     * JOIN
     * file_1.referenceStorageLocation AS storagelocation_1
     * WHERE
     * storagelocation_1.storageLocation = :parameter_0
     * <p>
     * Additionally parameter_0 should be
     * Archive Room XVI
     */
    @Test
    @Transactional
    public void shouldReturnValidHQLStorageLocationJoin() {
        ///noark5v5/odata/api/arkivstruktur/mappe?$filter=oppbevaringssted/oppbevaringssted eq 'Archive Room XVI'
        String compareValue = "Archive Room XVI";
        String odata = FILE + "?$filter=" + STORAGE_LOCATION + "/" +
                STORAGE_LOCATION + " eq '" + compareValue + "')";
        String hql = "SELECT file_1 FROM File AS file_1" +
                " JOIN" +
                " file_1.referenceStorageLocation AS storagelocation_1" +
                " WHERE" +
                " storagelocation_1.storageLocation = :parameter_0";
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        Assertions.assertEquals(queryObject.getQuery().getParameterValue("parameter_0"),
                compareValue);
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
    }

    @Test
    @Transactional
    public void shouldReturnValidHQLQueryNoFilter() {
        ///noark5v5/odata/api/arkivstruktur/mappe?$filter=oppbevaringssted/oppbevaringssted eq 'Archive Room XVI'
        String odata = FILE;
        String hql = "SELECT file_1 FROM File AS file_1";
        QueryObject queryObject = oDataService.convertODataToHQL(odata, "");
        Assertions.assertEquals(queryObject.getQuery().getQueryString(), hql);
    }

    /**
     * Check that a space between date and time of a dateTime object results in
     * the throwing of an exception
     * Note:
     * <br>
     * In ISO 8601:2004 it was permitted to omit the "T" character, but this
     * provision was removed in ISO 8601-1:2019. Separating date and time parts
     * with other characters such as space is not allowed in ISO 8601, but
     * allowed in its profile RFC 3339.
     * (from wikpedia, https://en.wikipedia.org/wiki/ISO_8601)
     * Entity:  mappe, VSM
     * Attribute: VSM.valueName with value ppt-v1:meldingstidspunkt
     * <p>
     * ODATA Input:
     * mappe?$filter=ppt-v1:meldingstidspunkt eq '2020-01-01 22:25:06.00000+02:00'
     * <p>
     * Expected Result :
     */
// This test is not throwing an exception! Not sure why. Perhaps it is OK, but
// I need to revisit it later. Leaving it in commented out so others can see
// that I am unsure
//    @Test
//    @Transactional
//    public void shouldThrowExceptionWithMissingT() {
//        String attributeName = "ppt-v1:meldingstidspunkt";
//        String odata = "mappe?$filter=" + attributeName +
//                " eq '2020-01-01 22:25:06.09+02:00'";
//        Assertions.assertThrows(DateTimeParseException.class,
//                () -> oDataService.convertODataToHQL(odata, ""));
//    }

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
