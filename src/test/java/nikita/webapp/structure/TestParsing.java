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
import nikita.common.model.noark5.v5.ClassificationSystem;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.FondsCreator;
import nikita.common.model.noark5.v5.PartPerson;
import nikita.common.model.noark5.v5.PartUnit;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.util.deserialisers.ClassDeserializer;
import nikita.common.util.deserialisers.ClassificationSystemDeserializer;
import nikita.common.util.deserialisers.DocumentDescriptionDeserializer;
import nikita.common.util.deserialisers.DocumentObjectDeserializer;
import nikita.common.util.deserialisers.FileDeserializer;
import nikita.common.util.deserialisers.FondsCreatorDeserializer;
import nikita.common.util.deserialisers.FondsDeserializer;
import nikita.common.util.deserialisers.PartPersonDeserializer;
import nikita.common.util.deserialisers.PartUnitDeserializer;
import nikita.common.util.deserialisers.RecordDeserializer;
import nikita.common.util.deserialisers.SeriesDeserializer;
import nikita.common.util.deserialisers.admin.AdministrativeUnitDeserializer;
import nikita.common.util.deserialisers.admin.UserDeserializer;
import nikita.common.util.deserialisers.casehandling.CaseFileDeserializer;
import nikita.common.util.deserialisers.casehandling.RecordNoteDeserializer;
import nikita.common.util.deserialisers.casehandling.RegistryEntryDeserializer;

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
    public void parseAdministrativeUnitComplete() throws Exception {
        System.out.println("info: testing administrative unit parsing");
        String systemID = "c093ea52-307a-11ea-bc98-bba88311e612";
        String json = "{ "
            +"  \"systemID\": \"" + systemID + "\" "
            +", \"administrativEnhetNavn\": \"Everywhere\" "
            +", \"kortnavn\": \"everywhere\" "
            +", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
            +", \"opprettetAv\": \"Some Person\" "
            +", \"avsluttetDato\": \"1863-10-10T00:00:00+00:00\" "
            +", \"administrativEnhetsstatus\": \"Operativ\" "
            +", \"referanseOverordnetEnhet\": \"23cf6934-307b-11ea-a93b-dfee364de9fe\" "
            //+", \"virksomhetsspesifikkeMetadata\": {} "
            +"}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
            objectMapper.getJsonFactory().createJsonParser(json);
        AdministrativeUnitDeserializer administrativeUnitDeserializer = new AdministrativeUnitDeserializer();
        AdministrativeUnit unit =
            administrativeUnitDeserializer.deserialize(jsonParser,
                                         null /* DeserializationContext */);
        assert(null != unit);
        assert(systemID.equals(unit.getSystemId()));
        assert("Everywhere".equals(unit.getAdministrativeUnitName()));
    }

    @Test
    public void parseUserComplete() throws Exception {
        System.out.println("info: testing user parsing");
        String systemID = "c24d1996-3079-11ea-83a5-f78c4a16ad93";
        String json = "{ "
            +"  \"systemID\": \"" + systemID + "\" "
            +", \"brukerNavn\": \"Isaac Asmiov\" "
            +", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
            +", \"opprettetAv\": \"Some Person\" "
            +", \"avsluttetDato\": \"1863-10-10T00:00:00+00:00\" "
            //+", \"virksomhetsspesifikkeMetadata\": {} "
            //+", \"kortnavn\": \"asmiov\" "
            +"}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
            objectMapper.getJsonFactory().createJsonParser(json);
        UserDeserializer userDeserializer = new UserDeserializer();
        User user =
            userDeserializer.deserialize(jsonParser,
                                         null /* DeserializationContext */);
        assert(null != user);
        assert(systemID.equals(user.getSystemId()));
    }

    @Test
    public void parseClassificationSystemComplete() throws Exception {
	System.out.println("info: testing classificationSystem parsing");
        String systemID = "a36ff3f6-3086-11ea-b94d-eb793f1c877e";
        String title = "A classification System title";
        String json = "{ "
            +"  \"systemID\": \"" + systemID + "\" "
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
            +", \"klassifikasjonstype\": { \"kode\": \"KK\", \"kodenavn\": \"K-koder\" } "
            +", \"tittel\": \"" + title + "\" "
            +", \"beskrivelse\": \"A classification system description\" "
            +", \"avsluttetDato\": \"1863-10-10T00:00:00+00:00\" "
            +", \"avsluttetAv\": \"Another Person\" "
	    /*
            +", \"referanseAvsluttetAv\": \"4025f87a-3006-11ea-a626-53980911d4d2\" "
	    */
            +"}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
            objectMapper.getJsonFactory().createJsonParser(json);
        ClassificationSystemDeserializer classificationSystemDeserializer =
            new ClassificationSystemDeserializer();
        ClassificationSystem classificationSystem =
            classificationSystemDeserializer.deserialize(
                jsonParser, null /* DeserializationContext */);
        assert(null != classificationSystem);
        assert(systemID.equals(classificationSystem.getSystemId()));
        assert(title.equals(classificationSystem.getTitle()));
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
	    +"    \"skjermingOpphoererDato\": \"1942-07-25Z\" "
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
	    +"    \"nedgraderingsdato\": \"2070-02-13T12:00:00+00:00\", "
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
    public void parseFondsCreatorComplete() throws Exception {
        System.out.println("info: testing fondsCreator parsing");
        String systemID = "f44ad6ca-308f-11ea-bd07-ebb04f04b0cd";
        String name = "The Fond Creator";
        String json = "{ "
            +"  \"systemID\": \"" + systemID + "\" "
            /*
            +", \"oppdatertAv\": \"Some Person\" "
            +", \"oppdatertDato\": \"1865-02-13T00:00:00+00:00\" "
            +", \"referanseOppdatertAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
            +", \"opprettetAv\": \"Some Person\" "
            +", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
            +", \"referanseOpprettetAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
            */
            +", \"arkivskaperID\": \"The ID of the Fond Creator\" "
            +", \"arkivskaperNavn\": \"" + name + "\" "
            +", \"beskrivelse\": \"A fonds creator description\" "
            +"}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
            objectMapper.getJsonFactory().createJsonParser(json);
        FondsCreatorDeserializer fondsCreatorDeserializer =
            new FondsCreatorDeserializer();
        FondsCreator fondsCreator =
            fondsCreatorDeserializer.deserialize(
                jsonParser, null /* DeserializationContext */);
        assert(null != fondsCreator);
        assert(systemID.equals(fondsCreator.getSystemId()));
        assert(name.equals(fondsCreator.getFondsCreatorName()));
    }

    @Test
    public void parseFondsComplete() throws Exception {
        System.out.println("info: testing fonds parsing");
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
            +", \"tittel\": \"A fonds title\" "
            +", \"beskrivelse\": \"A fonds description\" "
            +", \"arkivstatus\": { \"kode\": \"A\", \"kodenavn\": \"Avsluttet\" } "
            +", \"dokumentmedium\": \"Elektronisk arkiv\" "
            +", \"oppbevaringssted\": [ \"Over the rainbow\" ] "
            +", \"avsluttetDato\": \"1863-10-10T00:00:00+00:00\" "
            +", \"avsluttetAv\": \"Another Person\" "
            /*
            +", \"referanseAvsluttetAv\": \"4025f87a-3006-11ea-a626-53980911d4d2\" "
            */
            +"}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
            objectMapper.getJsonFactory().createJsonParser(json);
        FondsDeserializer fondsDeserializer = new FondsDeserializer();
        Fonds fonds =
            fondsDeserializer.deserialize(jsonParser,
                                          null /* DeserializationContext */);
        assert(null != fonds);
        assert("A fonds title".equals(fonds.getTitle()));
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
    public void parseFileComplete() throws Exception {
        System.out.println("info: testing file parsing");
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
            +", \"mappeID\": \"1917/1\" "
            +", \"tittel\": \"A file title\" "
            +", \"offentligTittel\": \"A public file title\" "
            +", \"beskrivelse\": \"En beskrivelse\" "
            +", \"noekkelord\": [ \"new\", \"inbox\" ] "
            +", \"dokumentmedium\": \"Elektronisk arkiv\" "
            +", \"oppbevaringssted\": [ \"Over the rainbow\" ] "
            +", \"avsluttetDato\": \"1863-10-10T00:00:00+00:00\" "
            +", \"avsluttetAv\": \"Another Person\" "
            /*
            +", \"referanseAvsluttetAv\": \"4025f87a-3006-11ea-a626-53980911d4d2\" "
            */
            +", \"kassasjon\": { "
            +"    \"kassasjonsvedtak\": {"
            +"      \"kode\": \"K\", "
            +"      \"kodenavn\": \"Kasseres\""
            +"    }, "
            +"    \"kassasjonshjemmel\": \"enHjemmel\", "
            +"    \"bevaringstid\": 45, "
            +"    \"kassasjonsdato\": \"1942-07-25\" "
            +"  } "
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
            +"  } "
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
            //+", \"virksomhetsspesifikkeMetadata\": {} "
            +"}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
            objectMapper.getJsonFactory().createJsonParser(json);
        FileDeserializer fileDeserializer = new FileDeserializer();
        File file = fileDeserializer.deserialize(
                jsonParser, null /* DeserializationContext */);
        assert(null != file);
        assert("A file title".equals(file.getTitle()));
    }

    @Test
    public void parseCaseFileComplete() throws Exception {
        System.out.println("info: testing caseFile parsing");
        String systemID = "de2b388c-3051-11ea-a4a3-ffcaf5680dd8";
        String title = "A case file title";
        String json = "{ "
            +"  \"systemID\": \""+ systemID + "\" "
            //+", \"oppdatertAv\": \"Some Person\" "
            //+", \"oppdatertDato\": \"1865-02-13T00:00:00+00:00\" "
            //+", \"referanseOppdatertAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
            +", \"opprettetAv\": \"Some Person\" "
            +", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
            //+", \"referanseOpprettetAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
            +", \"mappeID\": \"1917/1\" "
            +", \"tittel\": \"" + title + "\" "
            +", \"offentligTittel\": \"A public caseFile title\" "
            +", \"beskrivelse\": \"A description\" "
            +", \"noekkelord\": [ \"new\", \"inbox\" ] "
            +", \"dokumentmedium\": \"Elektronisk arkiv\" "
            +", \"oppbevaringssted\": [ \"Over the rainbow\" ] "
            +", \"avsluttetDato\": \"1863-10-10T00:00:00+00:00\" "
            +", \"avsluttetAv\": \"Another Person\" "
            //+", \"referanseAvsluttetAv\": \"4025f87a-3006-11ea-a626-53980911d4d2\" "
            +", \"kassasjon\": { "
            +"    \"kassasjonsvedtak\": {"
            +"      \"kode\": \"K\", "
            +"      \"kodenavn\": \"Kasseres\""
            +"    }, "
            +"    \"kassasjonshjemmel\": \"enHjemmel\", "
            +"    \"bevaringstid\": 45, "
            +"    \"kassasjonsdato\": \"1942-07-25\" "
            +"  } "
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
            +"  } "
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
            +", \"saksaar\": 1942 "
            +", \"sakssekvensnummer\": 1 "
            +", \"saksdato\": \"1942-07-25Z\" "
            //+", \"administrativEnhet\": \"Management\" "
            +", \"saksansvarlig\": \"Another Person\" "
            +", \"journalenhet\": \"PR departement\" "
            +", \"saksstatus\": { \"kode\": \"B\", \"kodenavn\": \"Under behandling\" } "
            +", \"utlaantDato\": \"1942-07-25Z\" "
            +", \"utlaantTil\": \"Another Person\" "
            //+", \"referanseUtlaantTil\": \"4025f87a-3006-11ea-a626-53980911d4d2\" "
            //+", \"virksomhetsspesifikkeMetadata\": {} "
            +"}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
            objectMapper.getJsonFactory().createJsonParser(json);
        CaseFileDeserializer caseFileDeserializer = new CaseFileDeserializer();
        CaseFile caseFile = caseFileDeserializer.deserialize(
                jsonParser, null /* DeserializationContext */);
        assert(null != caseFile);
        assert(systemID.equals(caseFile.getSystemId()));
        assert(title.equals(caseFile.getTitle()));
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
	    +"  \"skjermingOpphoererDato\": \"1942-07-25Z\" "
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
	    +"  \"nedgraderingsdato\": \"2070-02-13T12:00:00+00:00\", "
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
            //+", \"virksomhetsspesifikkeMetadata\": {} "
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
    public void parseRegistryEntryComplete() throws Exception {
        System.out.println("info: testing registry entry parsing");
        String systemID = "cee54630-2fc3-11ea-b478-6b8131698ea5";
        String title = "A registry entry title";
        String json = "{ "
            +"  \"systemID\": \"" + systemID + "\" "
            //+", \"oppdatertAv\": \"Some Person\" "
            //+", \"oppdatertDato\": \"1865-02-13T00:00:00+00:00\" "
            //+", \"referanseOppdatertAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
            +", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
            +", \"opprettetAv\": \"Some Person\" "
            //+", \"referanseOpprettetAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
            +", \"arkivertDato\": \"1863-10-10T00:00:00+00:00\" "
            +", \"arkivertAv\": \"Min Venn\" "
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
            +"    \"skjermingOpphoererDato\": \"1942-07-25Z\" "
            +"  } "
            */
            /*
            +",  \"gradering\": { "
            +"    \"graderingskode\": { "
            +"      \"kode\":\"SH\","
            +"      \"kodenavn\":\"Strengt hemmelig (sikkerhetsgrad)\""
            +"    }, "
            +"    \"graderingsdato\": \"1865-02-13T00:00:00+00:00\", "
            +"    \"gradertAv\": \"PST\", "
            +"    \"nedgraderingsdato\": \"2070-02-13T12:00:00+00:00\", "
            +"    \"nedgradertAv\": \"PST\" "
            +"  } "
            */
            +", \"registreringsID\": \"a registryentry id\" "
            +", \"tittel\": \"" + title + "\" "
            +", \"offentligTittel\": \"A public registry entry title\" "
            +", \"beskrivelse\": \"A description\" "
            //+", \"noekkelord\": [ \"new\", \"inbox\" ] "
            //+", \"forfatter\": [ \"Isaac Asimov\" ] "
            +", \"dokumentmedium\": \"Elektronisk arkiv\" "
            +", \"journalaar\": 1942 "
            +", \"journalsekvensnummer\": 1 "
            +", \"journalpostnummer\": 1 "
            +", \"journalposttype\": { \"kode\": \"I\", \"kodenavn\": \"Inngående dokument\" } "
            +", \"journalstatus\": { \"kode\": \"A\", \"kodenavn\": \"Arkivert\" } "
            +", \"journaldato\": \"1942-07-25Z\" "
            +", \"dokumentetsDato\": \"1942-07-25Z\" "
            +", \"mottattDato\": \"2070-02-13T12:00:00+00:00\" "
            +", \"sendtDato\": \"1942-07-25Z\" "
            +", \"forfallsdato\": \"1942-07-25Z\" "
            +", \"offentlighetsvurdertDato\": \"1942-07-25Z\" "
            +", \"antallVedlegg\": 1 "
            +", \"utlaantDato\": \"1942-07-25Z\" "
            +", \"utlaantTil\": \"Another Person\" "
            //+", \"referanseUtlaantTil\": \"4025f87a-3006-11ea-a626-53980911d4d2\" "
            +", \"journalenhet\": \"PR departement\" "
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
            //+", \"virksomhetsspesifikkeMetadata\": {} "
            +"}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
            objectMapper.getJsonFactory().createJsonParser(json);
        RegistryEntryDeserializer registryEntryDeserializer = new RegistryEntryDeserializer();
        RegistryEntry registryEntry =
            registryEntryDeserializer.deserialize(jsonParser,
                                           null /* DeserializationContext */);
        assert(null != registryEntry);
        assert(systemID.equals(registryEntry.getSystemId()));
        assert(title.equals(registryEntry.getTitle()));
    }

    @Test
    public void parseRecordNoteComplete() throws Exception {
        System.out.println("info: testing record note parsing");
        String systemID = "cee54630-2fc3-11ea-b478-6b8131698ea5";
        String title = "A record note title";
        String json = "{ "
            +"  \"systemID\": \"" + systemID + "\" "
            //+", \"oppdatertAv\": \"Some Person\" "
            //+", \"oppdatertDato\": \"1865-02-13T00:00:00+00:00\" "
            //+", \"referanseOppdatertAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
            +", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
            +", \"opprettetAv\": \"Some Person\" "
            //+", \"referanseOpprettetAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
            +", \"arkivertDato\": \"1863-10-10T00:00:00+00:00\" "
            +", \"arkivertAv\": \"Min Venn\" "
            //+", \"referanseArkivertAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
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
            +"    \"skjermingOpphoererDato\": \"1942-07-25Z\" "
            +"  } "
            */
            /*
            +",  \"gradering\": { "
            +"    \"graderingskode\": { "
            +"      \"kode\":\"SH\","
            +"      \"kodenavn\":\"Strengt hemmelig (sikkerhetsgrad)\""
            +"    }, "
            +"    \"graderingsdato\": \"1865-02-13T00:00:00+00:00\", "
            +"    \"gradertAv\": \"PST\", "
            +"    \"nedgraderingsdato\": \"2070-02-13T12:00:00+00:00\", "
            +"    \"nedgradertAv\": \"PST\" "
            +"  } "
            */
            +", \"registreringsID\": \"a recordNote id\" "
            +", \"tittel\": \"" + title + "\" "
            +", \"offentligTittel\": \"A public record note title\" "
            +", \"beskrivelse\": \"A description\" "
            //+", \"noekkelord\": [ \"new\", \"inbox\" ] "
            //+", \"forfatter\": [ \"Isaac Asimov\" ] "
            +", \"dokumentmedium\": \"Elektronisk arkiv\" "
            +", \"dokumentetsDato\": \"1942-07-25Z\" "
            +", \"mottattDato\": \"2070-02-13T12:00:00+00:00\" "
            +", \"sendtDato\": \"1942-07-25Z\" "
            +", \"forfallsdato\": \"1942-07-25Z\" "
            +", \"offentlighetsvurdertDato\": \"1942-07-25Z\" "
            +", \"antallVedlegg\": 1 "
            +", \"utlaantDato\": \"1942-07-25Z\" "
            +", \"utlaantTil\": \"Another Person\" "
            //+", \"referanseUtlaantTil\": \"4025f87a-3006-11ea-a626-53980911d4d2\" "
            //+", \"virksomhetsspesifikkeMetadata\": {} "
            +"}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
            objectMapper.getJsonFactory().createJsonParser(json);
        RecordNoteDeserializer recordNoteDeserializer = new RecordNoteDeserializer();
        RecordNote recordNote =
            recordNoteDeserializer.deserialize(jsonParser,
                                           null /* DeserializationContext */);
        assert(null != recordNote);
        assert(systemID.equals(recordNote.getSystemId()));
        assert(title.equals(recordNote.getTitle()));
    }

    @Test
    public void parseDocumentDescriptionComplete() throws Exception {
	System.out.println("info: testing documentdescription parsing");
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
            +", \"dokumenttype\": { \"kode\": \"B\", \"kodenavn\": \"Brev\" } "
            +", \"dokumentstatus\": { \"kode\": \"F\", \"kodenavn\": \"Dokumentet er ferdigstilt\" } "
	    +", \"tittel\": \"A document description title\" "
	    +", \"beskrivelse\": \"A document description description\" "
	    +", \"forfatter\": [ \"Isaac Asimov\" ] "
	    +", \"dokumentmedium\": \"Elektronisk arkiv\" "
	    +", \"oppbevaringssted\": [ \"Over the rainbow\" ] "
            +", \"tilknyttetRegistreringSom\": { \"kode\": \"H\", \"kodenavn\": \"Hoveddokument\" } "
	    +", \"dokumentnummer\": 1 "
            +", \"tilknyttetAv\": \"Some Person\" "
            +", \"tilknyttetDato\": \"1865-02-13T00:00:00+00:00\" "
	    /*
            +", \"referanseTilknyttetAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
	    */
	    /*
	    +", \"kassasjon\": { "
	    +"    \"kassasjonsvedtak\": {"
	    +"      \"kode\": \"K\", "
	    +"      \"kodenavn\": \"Kasseres\""
	    +"    }, "
	    +"    \"kassasjonshjemmel\": \"enHjemmel\", "
	    +"    \"bevaringstid\": 45, "
	    +"    \"kassasjonsdato\": \"1942-07-25Z\" "
	    +"  } "
	    */
	    /*
	    +", \"utfoertKassasjon\": { "
	    +"    \"kassertDato\": \"1863-10-10T00:00:00+00:00\", "
	    +"    \"kassertAv\": \"Ryddig Gutt\", "
	    +"    \"referanseKassertAv\": \"434939b4-3005-11ea-af00-47e34fa533df\" "
	    +"  } "
	    */
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
	    +"    \"skjermingOpphoererDato\": \"1942-07-25Z\" "
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
	    +"    \"nedgraderingsdato\": \"2070-02-13T12:00:00+00:00\", "
	    +"    \"nedgradertAv\": \"PST\" "
	    +"  } "
	    */
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
	    /*
	    +", \"eksternReferanse\" : \"RT #1234\" "
	    */
            //+", \"virksomhetsspesifikkeMetadata\": {} "
            +"}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
            objectMapper.getJsonFactory().createJsonParser(json);
        DocumentDescriptionDeserializer documentDescriptionDeserializer =
            new DocumentDescriptionDeserializer();
        DocumentDescription documentDescription =
            documentDescriptionDeserializer.deserialize(
                jsonParser, null /* DeserializationContext */);
        assert(null != documentDescription);
        assert("A document description title".equals(documentDescription.getTitle()));
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

    @Test
    public void parsePartPersonComplete() throws Exception {
        System.out.println("info: testing part person parsing");
        String systemID = "c093ea52-307a-11ea-bc98-bba88311e612";
        String name = "Michael Jordan";
        String json = "{ "
            +"  \"systemID\": \"" + systemID + "\" "
            +", \"partRolle\": { \"kode\": \"KLI\", \"kodenavn\": \"Klient\" } "
            +", \"navn\": \"" + name + "\" "
            //+", \"personidentifikator\": { \"dNummer\": \"01010101011\" } "
            +", \"postadresse\": { "
            +"     \"adresselinje1\": \"Blindveien 1\", "
            +"     \"adresselinje2\": \"c/o Donald Duck\", "
            +"     \"adresselinje3\": \"Innerst i gangen\", "
            +"     \"postnr\": { \"kode\": \"0101\", \"kodenavn\": \"Oslo\" }, "
            +"     \"poststed\": \"Oslo\", "
            +"     \"landkode\": { \"kode\": \"NO\", \"kodenavn\": \"Norge\" } "
            +"  } "
            +", \"bostedsadresse\": { "
            +"     \"adresselinje1\": \"Blindveien 1\", "
            +"     \"adresselinje2\": \"c/o Donald Duck\", "
            +"     \"adresselinje3\": \"Innerst i gangen\", "
            +"     \"postnr\": { \"kode\": \"0101\", \"kodenavn\": \"Oslo\" }, "
            +"     \"poststed\": \"Oslo\", "
            +"     \"landkode\": { \"kode\": \"NO\", \"kodenavn\": \"Norge\" } "
            +"  } "
            +", \"kontaktinformasjon\": {"
            +"     \"epostadresse\": \"admin@example.com\", "
            +"     \"mobiltelefon\": \"+47 222 22 222\", "
            +"     \"telefon\":      \"+47 900 00 000\" "
            +"  } "
            //+", \"virksomhetsspesifikkeMetadata\": {} "
            +"}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
            objectMapper.getJsonFactory().createJsonParser(json);
        PartPersonDeserializer partPersonDeserializer =
            new PartPersonDeserializer();
        PartPerson partPerson =
            partPersonDeserializer.deserialize(
                jsonParser, null /* DeserializationContext */);
        assert(null != partPerson);
        assert(systemID.equals(partPerson.getSystemId()));
        assert(name.equals(partPerson.getName()));
    }

    @Test
    public void parsePartUnitComplete() throws Exception {
        System.out.println("info: testing part unit parsing");
        String systemID = "c093ea52-307a-11ea-bc98-bba88311e612";
        String name = "Shoes and stuff";
        String json = "{ "
            +"  \"systemID\": \"" + systemID + "\" "
            +", \"partRolle\": { \"kode\": \"KLI\", \"kodenavn\": \"Klient\" } "
            +", \"navn\": \"" + name + "\" "
            //+", \"enhetsidentifikator\": { \"organisasjonsnummer\": \"01010101011\" } "
            +", \"forretningsadresse\": { "
            +"     \"adresselinje1\": \"Blindveien 1\", "
            +"     \"adresselinje2\": \"c/o Donald Duck\", "
            +"     \"adresselinje3\": \"Innerst i gangen\", "
            +"     \"postnr\": { \"kode\": \"0101\", \"kodenavn\": \"Oslo\" }, "
            +"     \"poststed\": \"Oslo\", "
            +"     \"landkode\": { \"kode\": \"NO\", \"kodenavn\": \"Norge\" } "
            +"  } "
            +", \"postadresse\": { "
            +"     \"adresselinje1\": \"Blindveien 1\", "
            +"     \"adresselinje2\": \"c/o Donald Duck\", "
            +"     \"adresselinje3\": \"Innerst i gangen\", "
            +"     \"postnr\": { \"kode\": \"0101\", \"kodenavn\": \"Oslo\" }, "
            +"     \"poststed\": \"Oslo\", "
            +"     \"landkode\": { \"kode\": \"NO\", \"kodenavn\": \"Norge\" } "
            +"  } "
            +", \"kontaktinformasjon\": {"
            +"     \"epostadresse\": \"admin@example.com\", "
            +"     \"mobiltelefon\": \"+47 222 22 222\", "
            +"     \"telefon\":      \"+47 900 00 000\" "
            +"  } "
            +", \"kontaktperson\": \"Donald Duck\" "
            //+", \"virksomhetsspesifikkeMetadata\": {} "
            +"}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
            objectMapper.getJsonFactory().createJsonParser(json);
        PartUnitDeserializer partUnitDeserializer =
            new PartUnitDeserializer();
        PartUnit partUnit =
            partUnitDeserializer.deserialize(
                jsonParser, null /* DeserializationContext */);
        assert(null != partUnit);
        assert(systemID.equals(partUnit.getSystemId()));
        assert(name.equals(partUnit.getName()));
    }
}
