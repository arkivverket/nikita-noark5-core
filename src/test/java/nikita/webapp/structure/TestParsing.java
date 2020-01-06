package nikita.webapp.structure;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nikita.N5CoreApp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.Series;
import nikita.common.util.deserialisers.ClassDeserializer;
import nikita.common.util.deserialisers.DocumentObjectDeserializer;
import nikita.common.util.deserialisers.FondsDeserializer;
import nikita.common.util.deserialisers.RecordDeserializer;
import nikita.common.util.deserialisers.SeriesDeserializer;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(classes = N5CoreApp.class)
//@AutoConfigureRestDocs(outputDir = "target/snippets")
public class TestParsing {

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
    }

    @Test
    public void validdates() throws Exception {
	System.out.println("info: testing date and datetime parsing");
	String[] datetimemust = {
            "1997-07-16T19:20+01:00",
            "1997-07-16T19:20:30+01:00",
            "1997-07-16T19:20:30.45+01:00",
            "2012-10-10T15:00:00Z",
            "2014-11-22T15:15:02.956+01:00",

            // These could be handled too
            "1997-07-16Z",
            "1865-02-13T00:00:00Z",
            "1865-02-13T00:00:00+00:00",
            "1865-02-13T00:00:00+01:00",
            "1865-02-13T00:00:00-01:00",
            "1864-10-09T00:00:00Z",
            "1864-10-09T00:00:00+00:00",
        };
        String[] datetimereject = {
            "1997-07-16T19:20+0100",
            "1997-07-16T19:20-0100",
            "1997-07-16T19:20+01",
            "1997-07-16T19:20-01",
            "1997",
            "1997-07",
            "1997-07-16",
        };
	for (String teststr : datetimemust) {
	    StringBuilder errors = new StringBuilder();
	    String json = "{ \"d\": \"" + teststr+ "\"}";
	    ObjectNode objectNode =
		(ObjectNode) new ObjectMapper().readTree(json);
	    OffsetDateTime d =
		deserializeDateTime("d", objectNode, errors);
	    if (null != d) {
		System.out.println("success: datetime '" + teststr + "' parsed to " + d);
		String str = formatDateTime(d);
		if (str.equals(teststr)) {
		    System.out.println("success: datetime serialized back to '" + str + "'");
		} else {
		    // Some datetime are not expected to stay
		    // unchanged, like 19:30 -> 19:30:00, so only flag
		    // warning, not error.
		    System.out.println("warning: serialized datetime changed to '" + str + "'");
		}
	    } else {
		System.out.println("error: unable to parse datetime '" + teststr + "'");
	    }
	}
	for (String teststr : datetimereject) {
	    StringBuilder errors = new StringBuilder();
	    String json = "{ \"d\": \"" + teststr+ "\"}";
	    ObjectNode objectNode =
		(ObjectNode) new ObjectMapper().readTree(json);
	    OffsetDateTime d =
		deserializeDateTime("d", objectNode, errors);
	    if (null != d) {
		System.out.println("error: datetime '" + teststr + "' parsed to " + d);
	    } else {
		System.out.println("success: unable to parse datetime '" + teststr + "'");
	    }
	}

	String[] datemust = {
            "1997-07-16+01:00",
            "1997-07-16-01:00",
            "1997-07-16Z",
            "1865-02-13Z",
	};
	String[] datereject = {
            "1997-07-16+0100",
            "1997-07-16-0100",
            "1997-07-16+01",
            "1997-07-16-01",
            "1997-07-16",
	};
	for (String teststr : datemust) {
	    StringBuilder errors = new StringBuilder();
	    String json = "{ \"d\": \"" + teststr+ "\"}";
	    ObjectNode objectNode =
		(ObjectNode) new ObjectMapper().readTree(json);
	    OffsetDateTime d =
		deserializeDate("d", objectNode, errors);
	    if (null != d) {
		System.out.println("success: date '" + teststr + "' parsed to " + d);
		String str = formatDate(d);
		if (str.equals(teststr)) {
		    System.out.println("success: date serialized back to '" + str + "'");
		} else {
		    System.out.println("error: serialized date changed to '" + str + "'");
		}
	    } else {
		System.out.println("error: unable to parse date '" + teststr + "'");
	    }
	}
	for (String teststr : datereject) {
	    StringBuilder errors = new StringBuilder();
	    String json = "{ \"d\": \"" + teststr+ "\"}";
	    ObjectNode objectNode =
		(ObjectNode) new ObjectMapper().readTree(json);
	    OffsetDateTime d =
		deserializeDate("d", objectNode, errors);
	    if (null != d) {
		System.out.println("error: date '" + teststr + "' parsed to " + d);
	    } else {
		System.out.println("success: unable to parse date '" + teststr + "'");
	    }
	}
    }

    @Test
    public void parseClassComplete() throws Exception {
	System.out.println("info: testing class parsing");
	String json = "{ "
	    +"\"systemID\": \"cee54630-2fc3-11ea-b478-6b8131698ea5\" "
	    +", \"klasseID\": \"a class id\" "
	    +", \"tittel\": \"A class title\" "
	    +", \"beskrivelse\": \"A class description\" "
	    +", \"noekkelord\": [ \"new\", \"inbox\" ] "
	    +", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
	    +", \"opprettetAv\": \"Some Person\" "
	    +", \"avsluttetDato\": \"1863-10-10T00:00:00+00:00\" "
	    +", \"avsluttetAv\": \"Another Person\" "
	    /*
	    +", \"kassasjon\": { "
	    +"    \"kassasjonsvedtak\": {"
	    +"      \"kode\": \"K\", "
	    +"      \"kodenavn\": \"Kasseres\""
	    +"    }, "
	    +"    \"kassasjonshjemmel\": \"enHjemmel\", "
	    +"    \"bevaringstid\": 45, "
	    +"    \"kassasjonsdato\": \"1942-07-25\" "
	    +"  } "
	    */
	    /*
	    +", \"skjerming\": { "
	    +"    \"tilgangsrestriksjon\": {"
	    +"      \"kode\": \"P\","
	    +"      \"kodenavn\": \"Personalsaker\""
	    +"    }, "
	    +"    \"skjermingshjemmel\": \"Unntatt etter Offentleglova\", "
	    +"    \"skjermingMetadata\": { \"kode\": \"S\", \"kodenavn\": \"Skjermet\" }, "
	    +"    \"skjermingDokument\": { \"kode\": \"H\", \"kodenavn\": \"Skjerming av hele dokumentet\" }, "
	    +"    \"skjermingsvarighet\": 60, "
	    +"    \"skjermingOpphoererDato\": \"1942-07-25\" "
	    +"} "
	    */
	    /*
	    +", \"gradering\": { "
	    +"    \"graderingskode\": { "
	    +"      \"kode\":\"SH\","
	    +"      \"kodenavn\":\"Strengt hemmelig (sikkerhetsgrad)\""
	    +"    }, "
	    +"    \"graderingsdato\": \"1865-02-13T00:00:00+00:00\", "
	    +"    \"gradertAv\": \"PST\", "
	    +"    \"nedgraderingsdato\": \"2070-02-13T12:00:00\", "
	    +"    \"nedgradertAv\": \"PST\" "
	    +"  } "
	    */
	    +"}";

	ObjectMapper objectMapper = new ObjectMapper();
	JsonParser jsonParser =
	    objectMapper.getJsonFactory().createJsonParser(json);
	ClassDeserializer classDeserializer = new ClassDeserializer();
	Class klass =
	    classDeserializer.deserialize(jsonParser,
					  null /* DeserializationContext */);
	assert(null != klass);
	assert("A class title".equals(klass.getTitle()));
    }

    @Test
    public void parseFonds() throws Exception {
	System.out.println("info: testing fonds parsing");
	String json = "{ \"tittel\": \"A fonds title\" }";
	ObjectMapper objectMapper = new ObjectMapper();
	JsonParser jsonParser =
	    objectMapper.getJsonFactory().createJsonParser(json);
	FondsDeserializer fondsDeserializer = new FondsDeserializer();
	Fonds fonds =
	    fondsDeserializer.deserialize(jsonParser,
					  null /* DeserializationContext */);
	assert(null != fonds);
	assert("A fonds title".equals(fonds.getTitle()));
	/*
	assertNotNull("able to create default fonds from json", fonds);
	assertEquals("fonds title matches input",
		     "En tittel", fonds.getTitle());
	*/
    }

    @Test
    public void parseSeriesComplete() throws Exception {
	System.out.println("info: testing series parsing");
	String json = "{ "
	    +"\"systemID\": \"cee54630-2fc3-11ea-b478-6b8131698ea5\" "
	    +", \"tittel\": \"A series title\" "
	    +", \"beskrivelse\": \"A series description\" "
	    +", \"arkivdelstatus\": { \"kode\": \"P\", \"kodenavn\": \"Avsluttet periode\" } "
	    +", \"dokumentmedium\": \"Elektronisk arkiv\" "
	    +", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
	    +", \"opprettetAv\": \"Some Person\" "
	    /*
	    +", \"referanseOpprettetAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
	    */
	    +", \"avsluttetDato\": \"1863-10-10T00:00:00+00:00\" "
	    +", \"avsluttetAv\": \"Another Person\" "
	    /*
	    +", \"referanseAvsluttetAv\": \"4025f87a-3006-11ea-a626-53980911d4d2\" "
	    */
	    +", \"arkivperiodeStartDato\": \"1863-10-10+00:00\" "
	    +", \"arkivperiodeSluttDato\": \"1863-10-10+00:00\" "
	    +", \"kassasjon\": { "
	    +"    \"kassasjonsvedtak\": {"
	    +"      \"kode\": \"K\", "
	    +"      \"kodenavn\": \"Kasseres\""
	    +"    }, "
	    +"    \"kassasjonshjemmel\": \"enHjemmel\", "
	    +"    \"bevaringstid\": 45, "
	    +"    \"kassasjonsdato\": \"1942-07-25\" "
	    +"  } "
	    +", \"utfoertKassasjon\": { "
	    +"    \"kassertDato\": \"1863-10-10T00:00:00+00:00\", "
	    +"    \"kassertAv\": \"Ryddig Gutt\", "
	    +"    \"referanseKassertAv\": \"434939b4-3005-11ea-af00-47e34fa533df\" "
	    +"  } "
	    /*
	    +", \"sletting\": { "
	    +"    \"slettingstype\": {"
	    +"      \"kode\": \"SP\","
	    +"      \"kodenavn\": \"Sletting av produksjonsformat\""
	    +"    }, "
	    +"    \"slettetDato\": \"1863-10-10T00:00:00+00:00\", "
	    +"    \"slettetAv\": \"Ryddig Gutt\", "
	    +"    \"referanseSlettetAv\": \"434939b4-3005-11ea-af00-47e34fa533df\" "
	    +"} "
	    */
	    +", \"skjerming\": { "
	    +"    \"tilgangsrestriksjon\": {"
	    +"      \"kode\": \"P\","
	    +"      \"kodenavn\": \"Personalsaker\""
	    +"    }, "
	    +"    \"skjermingshjemmel\": \"Unntatt etter Offentleglova\", "
	    +"    \"skjermingMetadata\": { \"kode\": \"S\", \"kodenavn\": \"Skjermet\" }, "
	    +"    \"skjermingDokument\": { \"kode\": \"H\", \"kodenavn\": \"Skjerming av hele dokumentet\" }, "
	    +"    \"skjermingsvarighet\": 60, "
	    +"    \"skjermingOpphoererDato\": \"1942-07-25Z\" "
	    +"} "
	    +", \"gradering\": { "
	    +"    \"graderingskode\": { "
	    +"      \"kode\":\"SH\","
	    +"      \"kodenavn\":\"Strengt hemmelig (sikkerhetsgrad)\""
	    +"    }, "
	    +"    \"graderingsdato\": \"1865-02-13T00:00:00+00:00\", "
	    +"    \"gradertAv\": \"PST\", "
	    +"    \"nedgraderingsdato\": \"2070-02-13T12:00:00+00:00\", "
	    +"    \"nedgradertAv\": \"PST\" "
	    +"  } "
	    +"}";
	ObjectMapper objectMapper = new ObjectMapper();
	JsonParser jsonParser =
	    objectMapper.getJsonFactory().createJsonParser(json);
	SeriesDeserializer seriesDeserializer = new SeriesDeserializer();
	Series series =
	    seriesDeserializer.deserialize(jsonParser,
					  null /* DeserializationContext */);
	assert(null != series);
	assert("A series title".equals(series.getTitle()));
    }

    @Test
    public void parseRecord() throws Exception {
	System.out.println("info: testing record parsing");
	String json = "{ "
	    +"\"tittel\": \"A record title\", "
	    + "\"forfatter\": [\"Isac Asimov\"] "+
	    "}";
	ObjectMapper objectMapper = new ObjectMapper();
	JsonParser jsonParser =
	    objectMapper.getJsonFactory().createJsonParser(json);
	RecordDeserializer recordDeserializer = new RecordDeserializer();
	Record record =
	    recordDeserializer.deserialize(jsonParser,
					   null /* DeserializationContext */);
	assert(null != record);
	assert("A record title".equals(record.getTitle()));
    }

    @Test
    public void parseRecordComplete() throws Exception {
	System.out.println("info: testing record parsing");
	String json = "{ "
	    +"\"systemID\": \"cee54630-2fc3-11ea-b478-6b8131698ea5\", "
	    +"\"opprettetDato\": \"1865-02-13T00:00:00+00:00\", "
	    +"\"opprettetAv\": \"Some Person\", "
	    +"\"arkivertDato\": \"1863-10-10T00:00:00+00:00\", "
	    +"\"arkivertAv\": \"Min Venn\", "
	    /*
	    +"\"kassasjon\": { "
	    +"  \"kassasjonsvedtak\": {"
	    +"    \"kode\": \"K\", "
	    +"    \"kodenavn\": \"Kasseres\""
	    +"  }, "
	    +"  \"kassasjonshjemmel\": \"enHjemmel\", "
	    +"  \"bevaringstid\": 45, "
	    +"  \"kassasjonsdato\": \"1942-07-25\" "
	    +"}, "
	    */
	    /*
	    +"\"skjerming\": { "
	    +"  \"tilgangsrestriksjon\": {"
	    +"    \"kode\": \"P\","
	    +"    \"kodenavn\": \"Personalsaker\""
	    +"  }, "
	    +"  \"skjermingshjemmel\": \"Unntatt etter Offentleglova\", "
	    +"  \"skjermingMetadata\": { \"kode\": \"S\", \"kodenavn\": \"Skjermet\" }, "
	    +"  \"skjermingDokument\": { \"kode\": \"H\", \"kodenavn\": \"Skjerming av hele dokumentet\" }, "
	    +"  \"skjermingsvarighet\": 60, "
	    +"  \"skjermingOpphoererDato\": \"1942-07-25\" "
	    +"}, "
	    */
	    /*
	    +"\"gradering\": { "
	    +"  \"graderingskode\": { "
	    +"    \"kode\":\"SH\","
	    +"    \"kodenavn\":\"Strengt hemmelig (sikkerhetsgrad)\""
	    +"  }, "
	    +"  \"graderingsdato\": \"1865-02-13T00:00:00+00:00\", "
	    +"  \"gradertAv\": \"PST\", "
	    +"  \"nedgraderingsdato\": \"2070-02-13T12:00:00\", "
	    +"  \"nedgradertAv\": \"PST\" "
	    +"}, "
	    */
	    +"\"registreringsID\": \"a record id\", "
	    +"\"tittel\": \"A record title\", "
	    +"\"offentligTittel\": \"A public record title\", "
	    +"\"beskrivelse\": \"En beskrivelse\", "
	    +"\"noekkelord\": [ \"ny\", \"inbox\" ], "
	    +"\"forfatter\": [ \"Isaac Asimov\" ], "
	    +"\"dokumentmedium\": \"Elektronisk arkiv\" "
	    //+"\"virksomhetsspesifikkeMetadata" type="xs:anyType" minOccurs="0"/>
	    +"}";

	ObjectMapper objectMapper = new ObjectMapper();
	JsonParser jsonParser =
	    objectMapper.getJsonFactory().createJsonParser(json);
	RecordDeserializer recordDeserializer = new RecordDeserializer();
	Record record =
	    recordDeserializer.deserialize(jsonParser,
					   null /* DeserializationContext */);
	assert(null != record);
	assert("A record title".equals(record.getTitle()));
    }

    @Test
    public void parseDocumentObjectComplete() throws Exception {
	System.out.println("info: testing documentobject parsing");
        String json = "{ "
            +"  \"systemID\": \"de2b388c-3051-11ea-a4a3-ffcaf5680dd8\" "
            /*
            +", \"oppdatertAv\": \"Some Person\" "
            +", \"oppdatertDato\": \"1865-02-13T00:00:00+00:00\" "
            +", \"referanseOppdatertAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
            */
            +", \"opprettetAv\": \"Some Person\" "
            +", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
            /*
            +", \"referanseOpprettetAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
            */
            +", \"versjonsnummer\": 1 "
            +", \"variantformat\": { \"kode\": \"A\", \"kodenavn\": \"Arkivformat\" } "
            +", \"format\": \"fmt/95\" "
            +", \"formatDetaljer\": \"Innebygget 3D-modell\" "
            +", \"referanseDokumentfil\": \"DOKUMENT/fil.pdf\" "
            +", \"filnavn\": \"fil.pdf\" "
            +", \"sjekksum\": \"047571914ac9f62ce503224e8cc7350f8fca11cddf7bf7dbbc3289de0f56c4cf\" "
            +", \"mimeType\": \"application/pdf\" "
            +", \"sjekksumAlgoritme\": \"SHA-256 Person\" "
            +", \"filstoerrelse\": 36 "
            /*
            +", \"elektroniskSignatur\": { "
            +"    \"elektroniskSignaturSikkerhetsnivaa\": { "
            +"      \"kode\":\"PS\","
            +"      \"kodenavn\":\"Sendt med PKI/\\\"person høy\\\"-sertifikat\""
            +"    }, "
            +"    \"elektroniskSignaturVerifisert\": { "
            +"      \"kode\":\"I\","
            +"      \"kodenavn\":\"Signatur påført og verifisert\""
            +"    }, "
            +"    \"verifisertDato\": \"2070-02-13+01:00\", "
            +"    \"verifisertAv\": \"PST\" "
            +"  } "
            */
            +"}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
            objectMapper.getJsonFactory().createJsonParser(json);
        DocumentObjectDeserializer documentObjectDeserializer =
            new DocumentObjectDeserializer();
        DocumentObject documentObject =
            documentObjectDeserializer.deserialize(
                jsonParser, null /* DeserializationContext */);
        assert(null != documentObject);
        assert("fmt/95".equals(documentObject.getFormat()));
    }
}
