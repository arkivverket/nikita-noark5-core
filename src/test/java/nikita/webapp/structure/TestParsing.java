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

import nikita.common.model.noark5.v5.*;
import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.admin.*;
import nikita.common.model.noark5.v5.casehandling.*;
import nikita.common.model.noark5.v5.casehandling.secondary.*;
import nikita.common.model.noark5.v5.metadata.*;
import nikita.common.model.noark5.v5.nationalidentifier.*;
import nikita.common.model.noark5.v5.secondary.*;
import nikita.common.util.deserialisers.*;
import nikita.common.util.deserialisers.admin.*;
import nikita.common.util.deserialisers.casehandling.*;
import nikita.common.util.deserialisers.nationalidentifier.*;
import nikita.common.util.deserialisers.secondary.*;

import java.util.UUID;

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
            String json = "{ \"d\": \"" + teststr + "\"}";
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
            String json = "{ \"d\": \"" + teststr + "\"}";
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
            String json = "{ \"d\": \"" + teststr + "\"}";
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
            String json = "{ \"d\": \"" + teststr + "\"}";
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
        String systemID = UUID.randomUUID().toString();
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"administrativEnhetNavn\": \"Everywhere\" "
                + ", \"kortnavn\": \"everywhere\" "
                + ", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
                + ", \"opprettetAv\": \"Some Person\" "
                + ", \"avsluttetDato\": \"1863-10-10T00:00:00+00:00\" "
                + ", \"administrativEnhetsstatus\": \"Operativ\" "
                + ", \"referanseOverordnetEnhet\": \"23cf6934-307b-11ea-a93b-dfee364de9fe\" "
                //+", \"virksomhetsspesifikkeMetadata\": {} "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        AdministrativeUnitDeserializer administrativeUnitDeserializer = new AdministrativeUnitDeserializer();
        AdministrativeUnit unit =
                administrativeUnitDeserializer.deserialize(jsonParser,
                        null /* DeserializationContext */);
        assert (null != unit);
        assert (systemID.equals(unit.getSystemId()));
        assert ("Everywhere".equals(unit.getAdministrativeUnitName()));
    }

    @Test
    public void parseUserComplete() throws Exception {
        System.out.println("info: testing user parsing");
        String systemID = UUID.randomUUID().toString();
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"brukerNavn\": \"Isaac Asmiov\" "
                + ", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
                + ", \"opprettetAv\": \"Some Person\" "
                + ", \"avsluttetDato\": \"1863-10-10T00:00:00+00:00\" "
                //+", \"virksomhetsspesifikkeMetadata\": {} "
                //+", \"kortnavn\": \"asmiov\" "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        UserDeserializer userDeserializer = new UserDeserializer();
        User user =
                userDeserializer.deserialize(jsonParser,
                        null /* DeserializationContext */);
        assert (null != user);
        assert (systemID.equals(user.getSystemId()));
    }

    @Test
    public void parseClassificationSystemComplete() throws Exception {
        System.out.println("info: testing classificationSystem parsing");
        String systemID = UUID.randomUUID().toString();
        String title = "A classification System title";
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"oppdatertAv\": \"Some Person\" "
                + ", \"oppdatertDato\": \"1865-02-13T00:00:00+00:00\" "
                //+", \"referanseOppdatertAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"opprettetAv\": \"Some Person\" "
                + ", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
                //+", \"referanseOpprettetAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"klassifikasjonstype\": { \"kode\": \"KK\", \"kodenavn\": \"K-koder\" } "
                + ", \"tittel\": \"" + title + "\" "
                + ", \"beskrivelse\": \"A classification system description\" "
                + ", \"avsluttetDato\": \"1863-10-10T00:00:00+00:00\" "
                + ", \"avsluttetAv\": \"Another Person\" "
                //+", \"referanseAvsluttetAv\": \"4025f87a-3006-11ea-a626-53980911d4d2\" "
                + "}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        ClassificationSystemDeserializer classificationSystemDeserializer =
                new ClassificationSystemDeserializer();
        ClassificationSystem classificationSystem =
                classificationSystemDeserializer.deserialize(
                        jsonParser, null /* DeserializationContext */);
        assert (null != classificationSystem);
        assert (systemID.equals(classificationSystem.getSystemId()));
        assert (title.equals(classificationSystem.getTitle()));
    }

    @Test
    public void parseClassComplete() throws Exception {
        System.out.println("info: testing class parsing");
        String systemID = UUID.randomUUID().toString();
        String json = "{ "
                + "\"systemID\": \"" + systemID + "\" "
                + ", \"oppdatertAv\": \"Some Person\" "
                + ", \"oppdatertDato\": \"1865-02-13T00:00:00+00:00\" "
                //+", \"referanseOppdatertAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"klasseID\": \"a class id\" "
                + ", \"tittel\": \"A class title\" "
                + ", \"beskrivelse\": \"A class description\" "
                + ", \"noekkelord\": [ \"new\", \"inbox\" ] "
                + ", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
                + ", \"opprettetAv\": \"Some Person\" "
                + ", \"avsluttetDato\": \"1863-10-10T00:00:00+00:00\" "
                + ", \"avsluttetAv\": \"Another Person\" "
                + ", \"kassasjon\": { "
                + "    \"kassasjonsvedtak\": {"
                + "      \"kode\": \"K\", "
                + "      \"kodenavn\": \"Kasseres\""
                + "    }, "
                + "    \"kassasjonshjemmel\": \"enHjemmel\", "
                + "    \"bevaringstid\": 45, "
                + "    \"kassasjonsdato\": \"1942-07-25\" "
                + "  } "
                + ", \"skjerming\": { "
                + "    \"tilgangsrestriksjon\": {"
                + "      \"kode\": \"P\","
                + "      \"kodenavn\": \"Personalsaker\""
                + "    }, "
                + "    \"skjermingshjemmel\": \"Unntatt etter Offentleglova\", "
                + "    \"skjermingMetadata\": { \"kode\": \"S\", \"kodenavn\": \"Skjermet\" }, "
                + "    \"skjermingDokument\": { \"kode\": \"H\", \"kodenavn\": \"Skjerming av hele dokumentet\" }, "
                + "    \"skjermingsvarighet\": 60, "
                + "    \"skjermingOpphoererDato\": \"1942-07-25Z\" "
                + "} "
                + ", \"gradering\": { "
                + "    \"graderingskode\": { "
                + "      \"kode\":\"SH\","
                + "      \"kodenavn\":\"Strengt hemmelig (sikkerhetsgrad)\""
                + "    }, "
                + "    \"graderingsdato\": \"1865-02-13T00:00:00+00:00\", "
                + "    \"gradertAv\": \"PST\", "
                + "    \"nedgraderingsdato\": \"2070-02-13T12:00:00+00:00\", "
                + "    \"nedgradertAv\": \"PST\" "
                + "  } "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        ClassDeserializer classDeserializer = new ClassDeserializer();
        Class klass =
                classDeserializer.deserialize(jsonParser,
                        null /* DeserializationContext */);
        assert (null != klass);
        assert ("A class title".equals(klass.getTitle()));
        Screening screening = klass.getReferenceScreening();
        assert(null != screening);
        assert(60 == screening.getScreeningDuration().intValue());
        AccessRestriction accessRestriction = screening.getAccessRestriction();
        assert("P".equals(accessRestriction.getCode()));
        ScreeningDocument screeningDocument = screening.getScreeningDocument();
        assert("H".equals(screeningDocument.getCode()));
        assert("Skjerming av hele dokumentet"
               .equals(screeningDocument.getCodeName()));
    }

    @Test
    public void parseFondsCreatorComplete() throws Exception {
        System.out.println("info: testing fondsCreator parsing");
        String systemID = UUID.randomUUID().toString();
        String name = "The Fond Creator";
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"oppdatertAv\": \"Some Person\" "
                + ", \"oppdatertDato\": \"1865-02-13T00:00:00+00:00\" "
                //+", \"referanseOppdatertAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"opprettetAv\": \"Some Person\" "
                + ", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
                //+", \"referanseOpprettetAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"arkivskaperID\": \"The ID of the Fond Creator\" "
                + ", \"arkivskaperNavn\": \"" + name + "\" "
                + ", \"beskrivelse\": \"A fonds creator description\" "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        FondsCreatorDeserializer fondsCreatorDeserializer =
                new FondsCreatorDeserializer();
        FondsCreator fondsCreator =
                fondsCreatorDeserializer.deserialize(
                        jsonParser, null /* DeserializationContext */);
        assert (null != fondsCreator);
        assert (systemID.equals(fondsCreator.getSystemId()));
        assert (name.equals(fondsCreator.getFondsCreatorName()));
    }

    @Test
    public void parseFondsComplete() throws Exception {
        System.out.println("info: testing fonds parsing");
        String systemID = UUID.randomUUID().toString();
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"oppdatertAv\": \"Some Person\" "
                + ", \"oppdatertDato\": \"1865-02-13T00:00:00+00:00\" "
                //+", \"referanseOppdatertAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"opprettetAv\": \"Some Person\" "
                + ", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
                //+", \"referanseOpprettetAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"tittel\": \"A fonds title\" "
                + ", \"beskrivelse\": \"A fonds description\" "
                + ", \"arkivstatus\": { \"kode\": \"A\", \"kodenavn\": \"Avsluttet\" } "
                + ", \"dokumentmedium\": { \"kode\": \"E\", \"kodenavn\": \"Elektronisk arkiv\" } "
                + ", \"oppbevaringssted\": [ \"Over the rainbow\" ] "
                + ", \"avsluttetDato\": \"1863-10-10T00:00:00+00:00\" "
                + ", \"avsluttetAv\": \"Another Person\" "
                //+", \"referanseAvsluttetAv\": \"4025f87a-3006-11ea-a626-53980911d4d2\" "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        FondsDeserializer fondsDeserializer = new FondsDeserializer();
        Fonds fonds =
                fondsDeserializer.deserialize(jsonParser,
                        null /* DeserializationContext */);
        assert (null != fonds);
        assert ("A fonds title".equals(fonds.getTitle()));
    }

    @Test
    public void parseSeriesComplete() throws Exception {
        System.out.println("info: testing series parsing");
        String systemID = UUID.randomUUID().toString();
        String title = "A series title";
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"oppdatertAv\": \"Some Person\" "
                + ", \"oppdatertDato\": \"1865-02-13T00:00:00+00:00\" "
                //+", \"referanseOppdatertAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"tittel\": \"" + title + "\" "
                + ", \"beskrivelse\": \"A series description\" "
                + ", \"arkivdelstatus\": { \"kode\": \"P\", \"kodenavn\": \"Avsluttet periode\" } "
                + ", \"dokumentmedium\": { \"kode\": \"E\", \"kodenavn\": \"Elektronisk arkiv\" } "
                + ", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
                + ", \"opprettetAv\": \"Some Person\" "
                //+", \"referanseOpprettetAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"avsluttetDato\": \"1863-10-10T00:00:00+00:00\" "
                + ", \"avsluttetAv\": \"Another Person\" "
                //+", \"referanseAvsluttetAv\": \"4025f87a-3006-11ea-a626-53980911d4d2\" "
                + ", \"arkivperiodeStartDato\": \"1863-10-10+00:00\" "
                + ", \"arkivperiodeSluttDato\": \"1863-10-10+00:00\" "
                + ", \"referanseForloeper\": \"01ee04a6-52ea-11ea-a778-afa5de20a851\" "
                + ", \"referanseArvtaker\": \"02a66262-52ea-11ea-afac-93773525cb72\" "
                + ", \"kassasjon\": { "
                + "    \"kassasjonsvedtak\": {"
                + "      \"kode\": \"K\", "
                + "      \"kodenavn\": \"Kasseres\""
                + "    }, "
                + "    \"kassasjonshjemmel\": \"enHjemmel\", "
                + "    \"bevaringstid\": 45, "
                + "    \"kassasjonsdato\": \"1942-07-25\" "
                + "  } "
                + ", \"utfoertKassasjon\": { "
                + "    \"kassertDato\": \"1863-10-10T00:00:00+00:00\", "
                + "    \"kassertAv\": \"Ryddig Gutt\", "
                + "    \"referanseKassertAv\": \"434939b4-3005-11ea-af00-47e34fa533df\" "
                + "  } "
                + ", \"sletting\": { "
                + "    \"slettingstype\": {"
                + "      \"kode\": \"SP\","
                + "      \"kodenavn\": \"Sletting av produksjonsformat\""
                + "    }, "
                + "    \"slettetDato\": \"1863-10-10T00:00:00+00:00\", "
                + "    \"slettetAv\": \"Ryddig Gutt\" "
                //+ ",    \"referanseSlettetAv\": \"434939b4-3005-11ea-af00-47e34fa533df\" "
                + "} "
                + ", \"skjerming\": { "
                + "    \"tilgangsrestriksjon\": {"
                + "      \"kode\": \"P\","
                + "      \"kodenavn\": \"Personalsaker\""
                + "    }, "
                + "    \"skjermingshjemmel\": \"Unntatt etter Offentleglova\", "
                + "    \"skjermingMetadata\": { \"kode\": \"S\", \"kodenavn\": \"Skjermet\" }, "
                + "    \"skjermingDokument\": { \"kode\": \"H\", \"kodenavn\": \"Skjerming av hele dokumentet\" }, "
                + "    \"skjermingsvarighet\": 60, "
                + "    \"skjermingOpphoererDato\": \"1942-07-25Z\" "
                + "} "
                + ", \"gradering\": { "
                + "    \"graderingskode\": { "
                + "      \"kode\":\"SH\","
                + "      \"kodenavn\":\"Strengt hemmelig (sikkerhetsgrad)\""
                + "    }, "
                + "    \"graderingsdato\": \"1865-02-13T00:00:00+00:00\", "
                + "    \"gradertAv\": \"PST\", "
                + "    \"nedgraderingsdato\": \"2070-02-13T12:00:00+00:00\", "
                + "    \"nedgradertAv\": \"PST\" "
                + "  } "
                + "}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        SeriesDeserializer seriesDeserializer = new SeriesDeserializer();
        Series series =
                seriesDeserializer.deserialize(jsonParser,
                        null /* DeserializationContext */);
        assert (null != series);
        assert (systemID.equals(series.getSystemId()));
        assert (title.equals(series.getTitle()));
        Classified c = series.getReferenceClassified();
        assert ("SH".equals(c.getClassification().getCode()));
        assert ("Strengt hemmelig (sikkerhetsgrad)"
                .equals(c.getClassification().getCodeName()));
    }

    @Test
    public void parseFileComplete() throws Exception {
        System.out.println("info: testing file parsing");
        String systemID = UUID.randomUUID().toString();
        String title = "A file title";
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"oppdatertAv\": \"Some Person\" "
                + ", \"oppdatertDato\": \"1865-02-13T00:00:00+00:00\" "
                //+", \"referanseOppdatertAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"opprettetAv\": \"Some Person\" "
                + ", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
                //+", \"referanseOpprettetAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"mappeID\": \"1917/1\" "
                + ", \"tittel\": \"" + title + "\" "
                + ", \"offentligTittel\": \"A public file title\" "
                + ", \"beskrivelse\": \"En beskrivelse\" "
                + ", \"noekkelord\": [ \"new\", \"inbox\" ] "
                + ", \"dokumentmedium\": { \"kode\": \"E\", \"kodenavn\": \"Elektronisk arkiv\" } "
                + ", \"oppbevaringssted\": [ \"Over the rainbow\" ] "
                + ", \"avsluttetDato\": \"1863-10-10T00:00:00+00:00\" "
                + ", \"avsluttetAv\": \"Another Person\" "
                //+", \"referanseAvsluttetAv\": \"4025f87a-3006-11ea-a626-53980911d4d2\" "
                + ", \"kassasjon\": { "
                + "    \"kassasjonsvedtak\": {"
                + "      \"kode\": \"K\", "
                + "      \"kodenavn\": \"Kasseres\""
                + "    }, "
                + "    \"kassasjonshjemmel\": \"enHjemmel\", "
                + "    \"bevaringstid\": 45, "
                + "    \"kassasjonsdato\": \"1942-07-25\" "
                + "  } "
                + ", \"skjerming\": { "
                + "    \"tilgangsrestriksjon\": {"
                + "      \"kode\": \"P\","
                + "      \"kodenavn\": \"Personalsaker\""
                + "    }, "
                + "    \"skjermingshjemmel\": \"Unntatt etter Offentleglova\", "
                + "    \"skjermingMetadata\": { \"kode\": \"S\", \"kodenavn\": \"Skjermet\" }, "
                + "    \"skjermingDokument\": { \"kode\": \"H\", \"kodenavn\": \"Skjerming av hele dokumentet\" }, "
                + "    \"skjermingsvarighet\": 60, "
                + "    \"skjermingOpphoererDato\": \"1942-07-25Z\" "
                + "  } "
                + ", \"gradering\": { "
                + "    \"graderingskode\": { "
                + "      \"kode\":\"SH\","
                + "      \"kodenavn\":\"Strengt hemmelig (sikkerhetsgrad)\""
                + "    }, "
                + "    \"graderingsdato\": \"1865-02-13T00:00:00+00:00\", "
                + "    \"gradertAv\": \"PST\", "
                + "    \"nedgraderingsdato\": \"2070-02-13T12:00:00+00:00\", "
                + "    \"nedgradertAv\": \"PST\" "
                + "  } "
                //+", \"virksomhetsspesifikkeMetadata\": {} "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        FileDeserializer fileDeserializer = new FileDeserializer();
        File file = fileDeserializer.deserialize(
                jsonParser, null /* DeserializationContext */);
        assert (null != file);
        assert (systemID.equals(file.getSystemId()));
        assert (title.equals(file.getTitle()));
        Classified c = file.getReferenceClassified();
        assert ("SH".equals(c.getClassification().getCode()));
        assert ("Strengt hemmelig (sikkerhetsgrad)"
                .equals(c.getClassification().getCodeName()));
    }

    @Test
    public void parseCaseFileComplete() throws Exception {
        System.out.println("info: testing caseFile parsing");
        String systemID = UUID.randomUUID().toString();
        String title = "A case file title";
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"oppdatertAv\": \"Some Person\" "
                + ", \"oppdatertDato\": \"1865-02-13T00:00:00+00:00\" "
                //+", \"referanseOppdatertAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"opprettetAv\": \"Some Person\" "
                + ", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
                //+", \"referanseOpprettetAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"mappeID\": \"1917/1\" "
                + ", \"tittel\": \"" + title + "\" "
                + ", \"offentligTittel\": \"A public caseFile title\" "
                + ", \"beskrivelse\": \"A description\" "
                + ", \"noekkelord\": [ \"new\", \"inbox\" ] "
                + ", \"dokumentmedium\": { \"kode\": \"E\", \"kodenavn\": \"Elektronisk arkiv\" } "
                + ", \"oppbevaringssted\": [ \"Over the rainbow\" ] "
                + ", \"avsluttetDato\": \"1863-10-10T00:00:00+00:00\" "
                + ", \"avsluttetAv\": \"Another Person\" "
                //+", \"referanseAvsluttetAv\": \"4025f87a-3006-11ea-a626-53980911d4d2\" "
                + ", \"kassasjon\": { "
                + "    \"kassasjonsvedtak\": {"
                + "      \"kode\": \"K\", "
                + "      \"kodenavn\": \"Kasseres\""
                + "    }, "
                + "    \"kassasjonshjemmel\": \"enHjemmel\", "
                + "    \"bevaringstid\": 45, "
                + "    \"kassasjonsdato\": \"1942-07-25\" "
                + "  } "
                + ", \"skjerming\": { "
                + "    \"tilgangsrestriksjon\": {"
                + "      \"kode\": \"P\","
                + "      \"kodenavn\": \"Personalsaker\""
                + "    }, "
                + "    \"skjermingshjemmel\": \"Unntatt etter Offentleglova\", "
                + "    \"skjermingMetadata\": { \"kode\": \"S\", \"kodenavn\": \"Skjermet\" }, "
                + "    \"skjermingDokument\": { \"kode\": \"H\", \"kodenavn\": \"Skjerming av hele dokumentet\" }, "
                + "    \"skjermingsvarighet\": 60, "
                + "    \"skjermingOpphoererDato\": \"1942-07-25Z\" "
                + "  } "
                + ", \"gradering\": { "
                + "    \"graderingskode\": { "
                + "      \"kode\":\"SH\","
                + "      \"kodenavn\":\"Strengt hemmelig (sikkerhetsgrad)\""
                + "    }, "
                + "    \"graderingsdato\": \"1865-02-13T00:00:00+00:00\", "
                + "    \"gradertAv\": \"PST\", "
                + "    \"nedgraderingsdato\": \"2070-02-13T12:00:00+00:00\", "
                + "    \"nedgradertAv\": \"PST\" "
                + "  } "
                + ", \"saksaar\": 1942 "
                + ", \"sakssekvensnummer\": 1 "
                + ", \"saksdato\": \"1942-07-25Z\" "
                //+", \"administrativEnhet\": \"Management\" "
                + ", \"saksansvarlig\": \"Another Person\" "
                + ", \"journalenhet\": \"PR departement\" "
                + ", \"saksstatus\": { \"kode\": \"B\", \"kodenavn\": \"Under behandling\" } "
                + ", \"utlaantDato\": \"1942-07-25Z\" "
                + ", \"utlaantTil\": \"Another Person\" "
                //+", \"referanseUtlaantTil\": \"4025f87a-3006-11ea-a626-53980911d4d2\" "
                //+", \"virksomhetsspesifikkeMetadata\": {} "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        CaseFileDeserializer caseFileDeserializer = new CaseFileDeserializer();
        CaseFile caseFile = caseFileDeserializer.deserialize(
                jsonParser, null /* DeserializationContext */);
        assert (null != caseFile);
        assert (systemID.equals(caseFile.getSystemId()));
        assert (title.equals(caseFile.getTitle()));
        Classified c = caseFile.getReferenceClassified();
        assert ("SH".equals(c.getClassification().getCode()));
        assert ("Strengt hemmelig (sikkerhetsgrad)"
                .equals(c.getClassification().getCodeName()));
    }

    @Test
    public void parseRecordComplete() throws Exception {
        System.out.println("info: testing record parsing");
        String systemID = UUID.randomUUID().toString();
        String title = "A record title";
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"oppdatertAv\": \"Some Person\" "
                + ", \"oppdatertDato\": \"1865-02-13T00:00:00+00:00\" "
                //+", \"referanseOppdatertAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
                + ", \"opprettetAv\": \"Some Person\" "
                + ", \"arkivertDato\": \"1863-10-10T00:00:00+00:00\" "
                + ", \"arkivertAv\": \"Min Venn\" "
                + ", \"kassasjon\": { "
                + "    \"kassasjonsvedtak\": {"
                + "      \"kode\": \"K\", "
                + "      \"kodenavn\": \"Kasseres\""
                + "    }, "
                + "    \"kassasjonshjemmel\": \"enHjemmel\", "
                + "    \"bevaringstid\": 45, "
                + "    \"kassasjonsdato\": \"1942-07-25\" "
                + "  } "
                + ", \"skjerming\": { "
                + "    \"tilgangsrestriksjon\": {"
                + "      \"kode\": \"P\","
                + "      \"kodenavn\": \"Personalsaker\""
                + "    }, "
                + "    \"skjermingshjemmel\": \"Unntatt etter Offentleglova\", "
                + "    \"skjermingMetadata\": { \"kode\": \"S\", \"kodenavn\": \"Skjermet\" }, "
                + "    \"skjermingDokument\": { \"kode\": \"H\", \"kodenavn\": \"Skjerming av hele dokumentet\" }, "
                + "    \"skjermingsvarighet\": 60, "
                + "    \"skjermingOpphoererDato\": \"1942-07-25Z\" "
                + "  } "
                + ", \"gradering\": { "
                + "    \"graderingskode\": { "
                + "      \"kode\":\"SH\","
                + "      \"kodenavn\":\"Strengt hemmelig (sikkerhetsgrad)\""
                + "    }, "
                + "    \"graderingsdato\": \"1865-02-13T00:00:00+00:00\", "
                + "    \"gradertAv\": \"PST\", "
                + "    \"nedgraderingsdato\": \"2070-02-13T12:00:00+00:00\", "
                + "    \"nedgradertAv\": \"PST\" "
                + "  } "
                + ", \"registreringsID\": \"a record id\" "
                + ", \"tittel\": \"" + title + "\" "
                + ", \"offentligTittel\": \"A public record title\" "
                + ", \"beskrivelse\": \"En beskrivelse\" "
                + ", \"noekkelord\": [ \"ny\", \"inbox\" ] "
                + ", \"dokumentmedium\": { \"kode\": \"E\", \"kodenavn\": \"Elektronisk arkiv\" } "
                //+", \"virksomhetsspesifikkeMetadata\": {} "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        RecordDeserializer recordDeserializer = new RecordDeserializer();
        Record record =
                recordDeserializer.deserialize(jsonParser,
                        null /* DeserializationContext */);
        assert (null != record);
        assert (systemID.equals(record.getSystemId()));
        assert (title.equals(record.getTitle()));
        Classified c = record.getReferenceClassified();
        assert ("SH".equals(c.getClassification().getCode()));
        assert ("Strengt hemmelig (sikkerhetsgrad)"
                .equals(c.getClassification().getCodeName()));
    }

    @Test
    public void parseRegistryEntryComplete() throws Exception {
        System.out.println("info: testing registry entry parsing");
        String systemID = UUID.randomUUID().toString();
        String title = "A registry entry title";
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"oppdatertAv\": \"Some Person\" "
                + ", \"oppdatertDato\": \"1865-02-13T00:00:00+00:00\" "
                //+", \"referanseOppdatertAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
                + ", \"opprettetAv\": \"Some Person\" "
                //+", \"referanseOpprettetAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"arkivertDato\": \"1863-10-10T00:00:00+00:00\" "
                + ", \"arkivertAv\": \"Min Venn\" "
                + ", \"kassasjon\": { "
                + "    \"kassasjonsvedtak\": {"
                + "      \"kode\": \"K\", "
                + "      \"kodenavn\": \"Kasseres\""
                + "    }, "
                + "    \"kassasjonshjemmel\": \"enHjemmel\", "
                + "    \"bevaringstid\": 45, "
                + "    \"kassasjonsdato\": \"1942-07-25\" "
                + "  } "
                + ", \"skjerming\": { "
                + "    \"tilgangsrestriksjon\": {"
                + "      \"kode\": \"P\","
                + "      \"kodenavn\": \"Personalsaker\""
                + "    }, "
                + "    \"skjermingshjemmel\": \"Unntatt etter Offentleglova\", "
                + "    \"skjermingMetadata\": { \"kode\": \"S\", \"kodenavn\": \"Skjermet\" }, "
                + "    \"skjermingDokument\": { \"kode\": \"H\", \"kodenavn\": \"Skjerming av hele dokumentet\" }, "
                + "    \"skjermingsvarighet\": 60, "
                + "    \"skjermingOpphoererDato\": \"1942-07-25Z\" "
                + "  } "
                + ",  \"gradering\": { "
                + "    \"graderingskode\": { "
                + "      \"kode\":\"SH\","
                + "      \"kodenavn\":\"Strengt hemmelig (sikkerhetsgrad)\""
                + "    }, "
                + "    \"graderingsdato\": \"1865-02-13T00:00:00+00:00\", "
                + "    \"gradertAv\": \"PST\", "
                + "    \"nedgraderingsdato\": \"2070-02-13T12:00:00+00:00\", "
                + "    \"nedgradertAv\": \"PST\" "
                + "  } "
                + ", \"registreringsID\": \"a registryentry id\" "
                + ", \"tittel\": \"" + title + "\" "
                + ", \"offentligTittel\": \"A public registry entry title\" "
                + ", \"beskrivelse\": \"A description\" "
                //+", \"noekkelord\": [ \"new\", \"inbox\" ] "
                + ", \"dokumentmedium\": { \"kode\": \"E\", \"kodenavn\": \"Elektronisk arkiv\" } "
                + ", \"journalaar\": 1942 "
                + ", \"journalsekvensnummer\": 1 "
                + ", \"journalpostnummer\": 1 "
                + ", \"journalposttype\": { \"kode\": \"I\", \"kodenavn\": \"Inngående dokument\" } "
                + ", \"journalstatus\": { \"kode\": \"A\", \"kodenavn\": \"Arkivert\" } "
                + ", \"journaldato\": \"1942-07-25Z\" "
                + ", \"dokumentetsDato\": \"1942-07-25Z\" "
                + ", \"mottattDato\": \"2070-02-13T12:00:00+00:00\" "
                + ", \"sendtDato\": \"1942-07-25Z\" "
                + ", \"forfallsdato\": \"1942-07-25Z\" "
                + ", \"offentlighetsvurdertDato\": \"1942-07-25Z\" "
                + ", \"antallVedlegg\": 1 "
                + ", \"utlaantDato\": \"1942-07-25Z\" "
                + ", \"utlaantTil\": \"Another Person\" "
                //+", \"referanseUtlaantTil\": \"4025f87a-3006-11ea-a626-53980911d4d2\" "
                + ", \"journalenhet\": \"PR departement\" "
                + ", \"elektroniskSignatur\": { "
                + "    \"elektroniskSignaturSikkerhetsnivaa\": { "
                + "      \"kode\":\"PS\", "
                + "      \"kodenavn\":\"Sendt med PKI/\\\"person høy\\\"-sertifikat\""
                + "    }, "
                + "    \"elektroniskSignaturVerifisert\": { "
                + "      \"kode\":\"I\","
                + "      \"kodenavn\":\"Signatur påført og verifisert\""
                + "    }, "
                + "    \"verifisertDato\": \"2070-02-13+01:00\", "
                + "    \"verifisertAv\": \"PST\" "
                + "  } "
                //+", \"virksomhetsspesifikkeMetadata\": {} "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        RegistryEntryDeserializer registryEntryDeserializer = new RegistryEntryDeserializer();
        RegistryEntry registryEntry =
                registryEntryDeserializer.deserialize(jsonParser,
                        null /* DeserializationContext */);
        assert (null != registryEntry);
        assert (systemID.equals(registryEntry.getSystemId()));
        assert (title.equals(registryEntry.getTitle()));
        Classified c = registryEntry.getReferenceClassified();
        assert ("SH".equals(c.getClassification().getCode()));
        assert ("Strengt hemmelig (sikkerhetsgrad)"
                .equals(c.getClassification().getCodeName()));
    }

    @Test
    public void parseRecordNoteComplete() throws Exception {
        System.out.println("info: testing record note parsing");
        String systemID = UUID.randomUUID().toString();
        String title = "A record note title";
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"oppdatertAv\": \"Some Person\" "
                + ", \"oppdatertDato\": \"1865-02-13T00:00:00+00:00\" "
                //+", \"referanseOppdatertAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
                + ", \"opprettetAv\": \"Some Person\" "
                //+", \"referanseOpprettetAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"arkivertDato\": \"1863-10-10T00:00:00+00:00\" "
                + ", \"arkivertAv\": \"Min Venn\" "
                //+", \"referanseArkivertAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"kassasjon\": { "
                + "    \"kassasjonsvedtak\": {"
                + "      \"kode\": \"K\", "
                + "      \"kodenavn\": \"Kasseres\""
                + "    }, "
                + "    \"kassasjonshjemmel\": \"enHjemmel\", "
                + "    \"bevaringstid\": 45, "
                + "    \"kassasjonsdato\": \"1942-07-25\" "
                + "  } "
                + ", \"skjerming\": { "
                + "    \"tilgangsrestriksjon\": {"
                + "      \"kode\": \"P\","
                + "      \"kodenavn\": \"Personalsaker\""
                + "    }, "
                + "    \"skjermingshjemmel\": \"Unntatt etter Offentleglova\", "
                + "    \"skjermingMetadata\": { \"kode\": \"S\", \"kodenavn\": \"Skjermet\" }, "
                + "    \"skjermingDokument\": { \"kode\": \"H\", \"kodenavn\": \"Skjerming av hele dokumentet\" }, "
                + "    \"skjermingsvarighet\": 60, "
                + "    \"skjermingOpphoererDato\": \"1942-07-25Z\" "
                + "  } "
                + ",  \"gradering\": { "
                + "    \"graderingskode\": { "
                + "      \"kode\":\"SH\","
                + "      \"kodenavn\":\"Strengt hemmelig (sikkerhetsgrad)\""
                + "    }, "
                + "    \"graderingsdato\": \"1865-02-13T00:00:00+00:00\", "
                + "    \"gradertAv\": \"PST\", "
                + "    \"nedgraderingsdato\": \"2070-02-13T12:00:00+00:00\", "
                + "    \"nedgradertAv\": \"PST\" "
                + "  } "
                + ", \"registreringsID\": \"a recordNote id\" "
                + ", \"tittel\": \"" + title + "\" "
                + ", \"offentligTittel\": \"A public record note title\" "
                + ", \"beskrivelse\": \"A description\" "
                //+", \"noekkelord\": [ \"new\", \"inbox\" ] "
                + ", \"dokumentmedium\": { \"kode\": \"E\", \"kodenavn\": \"Elektronisk arkiv\" } "
                + ", \"dokumentetsDato\": \"1942-07-25Z\" "
                + ", \"mottattDato\": \"2070-02-13T12:00:00+00:00\" "
                + ", \"sendtDato\": \"1942-07-25Z\" "
                + ", \"forfallsdato\": \"1942-07-25Z\" "
                + ", \"offentlighetsvurdertDato\": \"1942-07-25Z\" "
                + ", \"antallVedlegg\": 1 "
                + ", \"utlaantDato\": \"1942-07-25Z\" "
                + ", \"utlaantTil\": \"Another Person\" "
                //+", \"referanseUtlaantTil\": \"4025f87a-3006-11ea-a626-53980911d4d2\" "
                //+", \"virksomhetsspesifikkeMetadata\": {} "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        RecordNoteDeserializer recordNoteDeserializer = new RecordNoteDeserializer();
        RecordNote recordNote =
                recordNoteDeserializer.deserialize(jsonParser,
                        null /* DeserializationContext */);
        assert (null != recordNote);
        assert (systemID.equals(recordNote.getSystemId()));
        assert (title.equals(recordNote.getTitle()));
        Classified c = recordNote.getReferenceClassified();
        assert ("SH".equals(c.getClassification().getCode()));
        assert ("Strengt hemmelig (sikkerhetsgrad)"
                .equals(c.getClassification().getCodeName()));
    }

    @Test
    public void parseDocumentDescriptionComplete() throws Exception {
        System.out.println("info: testing documentdescription parsing");
        String systemID = UUID.randomUUID().toString();
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"oppdatertAv\": \"Some Person\" "
                + ", \"oppdatertDato\": \"1865-02-13T00:00:00+00:00\" "
                //+", \"referanseOppdatertAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"opprettetAv\": \"Some Person\" "
                + ", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
                //+", \"referanseOpprettetAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"dokumenttype\": { \"kode\": \"B\", \"kodenavn\": \"Brev\" } "
                + ", \"dokumentstatus\": { \"kode\": \"F\", \"kodenavn\": \"Dokumentet er ferdigstilt\" } "
                + ", \"tittel\": \"A document description title\" "
                + ", \"beskrivelse\": \"A document description description\" "
                + ", \"dokumentmedium\": { \"kode\": \"E\", \"kodenavn\": \"Elektronisk arkiv\" } "
                + ", \"oppbevaringssted\": [ \"Over the rainbow\" ] "
                + ", \"tilknyttetRegistreringSom\": { \"kode\": \"H\", \"kodenavn\": \"Hoveddokument\" } "
                + ", \"dokumentnummer\": 1 "
                + ", \"tilknyttetAv\": \"Some Person\" "
                + ", \"tilknyttetDato\": \"1865-02-13T00:00:00+00:00\" "
                //+", \"referanseTilknyttetAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"kassasjon\": { "
                + "    \"kassasjonsvedtak\": {"
                + "      \"kode\": \"K\", "
                + "      \"kodenavn\": \"Kasseres\""
                + "    }, "
                + "    \"kassasjonshjemmel\": \"enHjemmel\", "
                + "    \"bevaringstid\": 45, "
                + "    \"kassasjonsdato\": \"1942-07-25Z\" "
                + "  } "
                + ", \"utfoertKassasjon\": { "
                + "    \"kassertDato\": \"1863-10-10T00:00:00+00:00\", "
                + "    \"kassertAv\": \"Ryddig Gutt\", "
                + "    \"referanseKassertAv\": \"434939b4-3005-11ea-af00-47e34fa533df\" "
                + "  } "
                + ", \"sletting\": { "
                + "    \"slettingstype\": {"
                + "      \"kode\": \"SP\","
                + "      \"kodenavn\": \"Sletting av produksjonsformat\""
                + "    }, "
                + "    \"slettetDato\": \"1863-10-10T00:00:00+00:00\", "
                + "    \"slettetAv\": \"Ryddig Gutt\" "
                //+ ",    \"referanseSlettetAv\": \"434939b4-3005-11ea-af00-47e34fa533df\" "
                + "} "
                + ", \"skjerming\": { "
                + "    \"tilgangsrestriksjon\": {"
                + "      \"kode\": \"P\","
                + "      \"kodenavn\": \"Personalsaker\""
                + "    }, "
                + "    \"skjermingshjemmel\": \"Unntatt etter Offentleglova\", "
                + "    \"skjermingMetadata\": { \"kode\": \"S\", \"kodenavn\": \"Skjermet\" }, "
                + "    \"skjermingDokument\": { \"kode\": \"H\", \"kodenavn\": \"Skjerming av hele dokumentet\" }, "
                + "    \"skjermingsvarighet\": 60, "
                + "    \"skjermingOpphoererDato\": \"1942-07-25Z\" "
                + "} "
                + ", \"gradering\": { "
                + "    \"graderingskode\": { "
                + "      \"kode\":\"SH\","
                + "      \"kodenavn\":\"Strengt hemmelig (sikkerhetsgrad)\""
                + "    }, "
                + "    \"graderingsdato\": \"1865-02-13T00:00:00+00:00\", "
                + "    \"gradertAv\": \"PST\", "
                + "    \"nedgraderingsdato\": \"2070-02-13T12:00:00+00:00\", "
                + "    \"nedgradertAv\": \"PST\" "
                + "  } "
                + ", \"elektroniskSignatur\": { "
                + "    \"elektroniskSignaturSikkerhetsnivaa\": { "
                + "      \"kode\":\"PS\","
                + "      \"kodenavn\":\"Sendt med PKI/\\\"person høy\\\"-sertifikat\""
                + "    }, "
                + "    \"elektroniskSignaturVerifisert\": { "
                + "      \"kode\":\"I\","
                + "      \"kodenavn\":\"Signatur påført og verifisert\""
                + "    }, "
                + "    \"verifisertDato\": \"2070-02-13+01:00\", "
                + "    \"verifisertAv\": \"PST\" "
                + "  } "
                + ", \"eksternReferanse\" : \"RT #1234\" "
                //+", \"virksomhetsspesifikkeMetadata\": {} "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        DocumentDescriptionDeserializer documentDescriptionDeserializer =
                new DocumentDescriptionDeserializer();
        DocumentDescription documentDescription =
                documentDescriptionDeserializer.deserialize(
                        jsonParser, null /* DeserializationContext */);
        assert (null != documentDescription);
        assert ("A document description title".equals(documentDescription.getTitle()));
        Classified c = documentDescription.getReferenceClassified();
        assert ("SH".equals(c.getClassification().getCode()));
        assert ("Strengt hemmelig (sikkerhetsgrad)"
                .equals(c.getClassification().getCodeName()));
    }

    @Test
    public void parseDocumentObjectComplete() throws Exception {
        System.out.println("info: testing documentobject parsing");
        String systemID = UUID.randomUUID().toString();
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"oppdatertAv\": \"Some Person\" "
                + ", \"oppdatertDato\": \"1865-02-13T00:00:00+00:00\" "
                //+", \"referanseOppdatertAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"opprettetAv\": \"Some Person\" "
                + ", \"opprettetDato\": \"1865-02-13T00:00:00+00:00\" "
                //+", \"referanseOpprettetAv\": \"36719e06-3006-11ea-928f-efccf0776eba\" "
                + ", \"versjonsnummer\": 1 "
                + ", \"variantformat\": { \"kode\": \"A\", \"kodenavn\": \"Arkivformat\" } "
                + ", \"format\": { \"kode\": \"fmt/95\" } "
                + ", \"formatDetaljer\": \"Innebygget 3D-modell\" "
                + ", \"filnavn\": \"fil.pdf\" "
                + ", \"sjekksum\": \"047571914ac9f62ce503224e8cc7350f8fca11cddf7bf7dbbc3289de0f56c4cf\" "
                + ", \"mimeType\": \"application/pdf\" "
                + ", \"sjekksumAlgoritme\": \"SHA-256 Person\" "
                + ", \"filstoerrelse\": 36 "
                + ", \"elektroniskSignatur\": { "
                + "    \"elektroniskSignaturSikkerhetsnivaa\": { "
                + "      \"kode\":\"PS\","
                + "      \"kodenavn\":\"Sendt med PKI/\\\"person høy\\\"-sertifikat\""
                + "    }, "
                + "    \"elektroniskSignaturVerifisert\": { "
                + "      \"kode\":\"I\","
                + "      \"kodenavn\":\"Signatur påført og verifisert\""
                + "    }, "
                + "    \"verifisertDato\": \"2070-02-13+01:00\", "
                + "    \"verifisertAv\": \"PST\" "
                + "  } "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        DocumentObjectDeserializer documentObjectDeserializer =
                new DocumentObjectDeserializer();
        DocumentObject documentObject =
                documentObjectDeserializer.deserialize(
                        jsonParser, null /* DeserializationContext */);
        assert (null != documentObject);
        assert ("fmt/95".equals(documentObject.getFormat().getCode()));
    }

    @Test
    public void parsePartPersonComplete() throws Exception {
        System.out.println("info: testing part person parsing");
        String systemID = UUID.randomUUID().toString();
        String name = "Michael Jordan";
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"partRolle\": { \"kode\": \"KLI\", \"kodenavn\": \"Klient\" } "
                + ", \"navn\": \"" + name + "\" "
                + ", \"personidentifikator\": { \"dNummer\": \"01010101011\" } "
                + ", \"postadresse\": { "
                + "     \"adresselinje1\": \"Blindveien 1\", "
                + "     \"adresselinje2\": \"c/o Donald Duck\", "
                + "     \"adresselinje3\": \"Innerst i gangen\", "
                + "     \"postnr\": { \"kode\": \"0101\", \"kodenavn\": \"Oslo\" }, "
                + "     \"poststed\": \"Oslo\", "
                + "     \"landkode\": { \"kode\": \"NO\", \"kodenavn\": \"Norge\" } "
                + "  } "
                + ", \"bostedsadresse\": { "
                + "     \"adresselinje1\": \"Blindveien 1\", "
                + "     \"adresselinje2\": \"c/o Donald Duck\", "
                + "     \"adresselinje3\": \"Innerst i gangen\", "
                + "     \"postnr\": { \"kode\": \"0101\", \"kodenavn\": \"Oslo\" }, "
                + "     \"poststed\": \"Oslo\", "
                + "     \"landkode\": { \"kode\": \"NO\", \"kodenavn\": \"Norge\" } "
                + "  } "
                + ", \"kontaktinformasjon\": {"
                + "     \"epostadresse\": \"admin@example.com\", "
                + "     \"mobiltelefon\": \"+47 222 22 222\", "
                + "     \"telefon\":      \"+47 900 00 000\" "
                + "  } "
                //+", \"virksomhetsspesifikkeMetadata\": {} "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        PartPersonDeserializer partPersonDeserializer =
                new PartPersonDeserializer();
        PartPerson partPerson =
                partPersonDeserializer.deserialize(
                        jsonParser, null /* DeserializationContext */);
        assert (null != partPerson);
        assert (systemID.equals(partPerson.getSystemId()));
        assert (name.equals(partPerson.getName()));
        ContactInformation c = partPerson.getContactInformation();
        assert ("+47 900 00 000".equals(c.getTelephoneNumber()));
        assert ("+47 222 22 222".equals(c.getMobileTelephoneNumber()));
        assert ("admin@example.com".equals(c.getEmailAddress()));
    }

    @Test
    public void parsePartUnitComplete() throws Exception {
        System.out.println("info: testing part unit parsing");
        String systemID = UUID.randomUUID().toString();
        String name = "Shoes and stuff";
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"partRolle\": { \"kode\": \"KLI\", \"kodenavn\": \"Klient\" } "
                + ", \"navn\": \"" + name + "\" "
                + ", \"enhetsidentifikator\": { \"organisasjonsnummer\": \"01010101011\" } "
                + ", \"forretningsadresse\": { "
                + "     \"adresselinje1\": \"Blindveien 1\", "
                + "     \"adresselinje2\": \"c/o Donald Duck\", "
                + "     \"adresselinje3\": \"Innerst i gangen\", "
                + "     \"postnr\": { \"kode\": \"0101\", \"kodenavn\": \"Oslo\" }, "
                + "     \"poststed\": \"Oslo\", "
                + "     \"landkode\": { \"kode\": \"NO\", \"kodenavn\": \"Norge\" } "
                + "  } "
                + ", \"postadresse\": { "
                + "     \"adresselinje1\": \"Blindveien 1\", "
                + "     \"adresselinje2\": \"c/o Donald Duck\", "
                + "     \"adresselinje3\": \"Innerst i gangen\", "
                + "     \"postnr\": { \"kode\": \"0101\", \"kodenavn\": \"Oslo\" }, "
                + "     \"poststed\": \"Oslo\", "
                + "     \"landkode\": { \"kode\": \"NO\", \"kodenavn\": \"Norge\" } "
                + "  } "
                + ", \"kontaktinformasjon\": {"
                + "     \"epostadresse\": \"admin@example.com\", "
                + "     \"mobiltelefon\": \"+47 222 22 222\", "
                + "     \"telefon\":      \"+47 900 00 000\" "
                + "  } "
                + ", \"kontaktperson\": \"Donald Duck\" "
                //+", \"virksomhetsspesifikkeMetadata\": {} "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        PartUnitDeserializer partUnitDeserializer =
                new PartUnitDeserializer();
        PartUnit partUnit =
                partUnitDeserializer.deserialize(
                        jsonParser, null /* DeserializationContext */);
        assert (null != partUnit);
        assert (systemID.equals(partUnit.getSystemId()));
        assert (name.equals(partUnit.getName()));
        ContactInformation c = partUnit.getContactInformation();
        assert ("+47 900 00 000".equals(c.getTelephoneNumber()));
        assert ("+47 222 22 222".equals(c.getMobileTelephoneNumber()));
        assert ("admin@example.com".equals(c.getEmailAddress()));
    }

    @Test
    public void parseCorrespondencePartPersonComplete() throws Exception {
        System.out.println("info: testing correspondencepart person parsing");
        String systemID = UUID.randomUUID().toString();
        String name = "Michael Jordan";
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"korrespondanseparttype\": { \"kode\": \"EM\", \"kodenavn\": \"Mottaker\" } "
                + ", \"navn\": \"" + name + "\" "
                //+", \"personidentifikator\": { \"dNummer\": \"01010101011\" } "
                + ", \"postadresse\": { "
                + "     \"adresselinje1\": \"Blindveien 1\", "
                + "     \"adresselinje2\": \"c/o Donald Duck\", "
                + "     \"adresselinje3\": \"Innerst i gangen\", "
                + "     \"postnr\": { \"kode\": \"0101\", \"kodenavn\": \"Oslo\" }, "
                + "     \"poststed\": \"Oslo\", "
                + "     \"landkode\": { \"kode\": \"NO\", \"kodenavn\": \"Norge\" } "
                + "  } "
                + ", \"bostedsadresse\": { "
                + "     \"adresselinje1\": \"Blindveien 1\", "
                + "     \"adresselinje2\": \"c/o Donald Duck\", "
                + "     \"adresselinje3\": \"Innerst i gangen\", "
                + "     \"postnr\": { \"kode\": \"0101\", \"kodenavn\": \"Oslo\" }, "
                + "     \"poststed\": \"Oslo\", "
                + "     \"landkode\": { \"kode\": \"NO\", \"kodenavn\": \"Norge\" } "
                + "  } "
                + ", \"kontaktinformasjon\": {"
                + "     \"epostadresse\": \"admin@example.com\", "
                + "     \"mobiltelefon\": \"+47 222 22 222\", "
                + "     \"telefon\":      \"+47 900 00 000\" "
                + "  } "
                //+", \"virksomhetsspesifikkeMetadata\": {} "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        CorrespondencePartPersonDeserializer correspondencePartPersonDeserializer =
                new CorrespondencePartPersonDeserializer();
        CorrespondencePartPerson correspondencePartPerson =
                correspondencePartPersonDeserializer.deserialize(
                        jsonParser, null /* DeserializationContext */);
        assert (null != correspondencePartPerson);
        assert (systemID.equals(correspondencePartPerson.getSystemId()));
        assert (name.equals(correspondencePartPerson.getName()));
        ContactInformation c = correspondencePartPerson.getContactInformation();
        assert ("+47 900 00 000".equals(c.getTelephoneNumber()));
        assert ("+47 222 22 222".equals(c.getMobileTelephoneNumber()));
        assert ("admin@example.com".equals(c.getEmailAddress()));
    }

    @Test
    public void parseCorrespondencePartUnitComplete() throws Exception {
        System.out.println("info: testing correspondencepart unit parsing");
        String systemID = UUID.randomUUID().toString();
        String name = "Shoes and stuff";
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"korrespondanseparttype\": { \"kode\": \"EA\", \"kodenavn\": \"Avsender\" } "
                + ", \"navn\": \"" + name + "\" "
                //+", \"enhetsidentifikator\": { \"organisasjonsnummer\": \"01010101011\" } "
                + ", \"forretningsadresse\": { "
                + "     \"adresselinje1\": \"Blindveien 1\", "
                + "     \"adresselinje2\": \"c/o Donald Duck\", "
                + "     \"adresselinje3\": \"Innerst i gangen\", "
                + "     \"postnr\": { \"kode\": \"0101\", \"kodenavn\": \"Oslo\" }, "
                + "     \"poststed\": \"Oslo\", "
                + "     \"landkode\": { \"kode\": \"NO\", \"kodenavn\": \"Norge\" } "
                + "  } "
                + ", \"postadresse\": { "
                + "     \"adresselinje1\": \"Blindveien 1\", "
                + "     \"adresselinje2\": \"c/o Donald Duck\", "
                + "     \"adresselinje3\": \"Innerst i gangen\", "
                + "     \"postnr\": { \"kode\": \"0101\", \"kodenavn\": \"Oslo\" }, "
                + "     \"poststed\": \"Oslo\", "
                + "     \"landkode\": { \"kode\": \"NO\", \"kodenavn\": \"Norge\" } "
                + "  } "
                + ", \"kontaktinformasjon\": {"
                + "     \"epostadresse\": \"admin@example.com\", "
                + "     \"mobiltelefon\": \"+47 222 22 222\", "
                + "     \"telefon\":      \"+47 900 00 000\" "
                + "  } "
                + ", \"kontaktperson\": \"Donald Duck\" "
                //+", \"virksomhetsspesifikkeMetadata\": {} "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        CorrespondencePartUnitDeserializer correspondencePartUnitDeserializer =
                new CorrespondencePartUnitDeserializer();
        CorrespondencePartUnit correspondencePartUnit =
                correspondencePartUnitDeserializer.deserialize(
                        jsonParser, null /* DeserializationContext */);
        assert (null != correspondencePartUnit);
        assert (systemID.equals(correspondencePartUnit.getSystemId()));
        assert (name.equals(correspondencePartUnit.getName()));
        ContactInformation c = correspondencePartUnit.getContactInformation();
        assert ("+47 900 00 000".equals(c.getTelephoneNumber()));
        assert ("+47 222 22 222".equals(c.getMobileTelephoneNumber()));
        assert ("admin@example.com".equals(c.getEmailAddress()));
    }


    @Test
    public void parseCorrespondencePartInternalComplete() throws Exception {
        System.out.println("info: testing correspondence part internal parsing");
        String systemID = UUID.randomUUID().toString();
        String name = "Management";
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"korrespondanseparttype\": { \"kode\": \"EK\", \"kodenavn\": \"Intern kopimottaker\" } "
                + ", \"administrativEnhet\": \"" + name + "\" "
                + ", \"saksbehandler\": \"Donald Duck\" "
                //+", \"virksomhetsspesifikkeMetadata\": {} "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        CorrespondencePartInternalDeserializer correspondencePartInternalDeserializer =
                new CorrespondencePartInternalDeserializer();
        CorrespondencePartInternal correspondencePartInternal =
                correspondencePartInternalDeserializer.deserialize(
                        jsonParser, null /* DeserializationContext */);
        assert (null != correspondencePartInternal);
        assert (systemID.equals(correspondencePartInternal.getSystemId()));
        assert (name.equals(correspondencePartInternal.getAdministrativeUnit()));
    }

    @Test
    public void parseNIBuildingComplete() throws Exception {
        System.out.println("info: testing building parsing");
        String systemID = UUID.randomUUID().toString();
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"bygningsnummer\": 1 "
                + ", \"endringsloepenummer\": 2 "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        BuildingDeserializer buildingDeserializer = new BuildingDeserializer();
        Building building =
                buildingDeserializer.deserialize(jsonParser,
                        null /* DeserializationContext */);
        assert (null != building);
        assert (systemID.equals(building.getSystemId()));
    }

    @Test
    public void parseNICadastralUnitComplete() throws Exception {
        System.out.println("info: testing cadastral unit parsing");
        String systemID = UUID.randomUUID().toString();
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"kommunenummer\": \"0101\" "
                + ", \"gaardsnummer\": 1 "
                + ", \"bruksnummer\": 2 "
                + ", \"festenummer\": 3 "
                + ", \"seksjonsnummer\": 4 "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        CadastralUnitDeserializer cadastralUnitDeserializer = new CadastralUnitDeserializer();
        CadastralUnit cadastralUnit =
                cadastralUnitDeserializer.deserialize(
                        jsonParser, null /* DeserializationContext */);
        assert (null != cadastralUnit);
        assert (systemID.equals(cadastralUnit.getSystemId()));
    }

    @Test
    public void parseNIDNumberComplete() throws Exception {
        System.out.println("info: testing dnumber parsing");
        String systemID = UUID.randomUUID().toString();
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"dNummer\": \"01010101011\" "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        DNumberDeserializer dNumberDeserializer = new DNumberDeserializer();
        DNumber dNumber =
                dNumberDeserializer.deserialize(jsonParser,
                        null /* DeserializationContext */);
        assert (null != dNumber);
        assert (systemID.equals(dNumber.getSystemId()));
    }

    @Test
    public void parseNIPlanComplete() throws Exception {
        System.out.println("info: testing plan parsing");
        String systemID = UUID.randomUUID().toString();
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"kommunenummer\": \"0101\" "
                + ", \"fylkesnummer\": \"01\" "
                + ", \"landkode\": { \"kode\": \"NO\", \"kodenavn\": \"Norge\" } "
                + ", \"planidentifikasjon\": \"10\" "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        PlanDeserializer planDeserializer = new PlanDeserializer();
        Plan plan =
                planDeserializer.deserialize(jsonParser,
                        null /* DeserializationContext */);
        assert (null != plan);
        assert (systemID.equals(plan.getSystemId()));
    }

    @Test
    public void parseNIPositionComplete() throws Exception {
        System.out.println("info: testing position parsing");
        String systemID = UUID.randomUUID().toString();
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"koordinatsystem\": { \"kode\": \"EPSG:4326\", \"kodenavn\": \"WGS84\" } "
                + ", \"x\": 1.0 "
                + ", \"y\": 2.0 "
                + ", \"z\": 3.0 "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        PositionDeserializer positionDeserializer = new PositionDeserializer();
        Position position =
                positionDeserializer.deserialize(jsonParser,
                        null /* DeserializationContext */);
        assert (null != position);
        assert (systemID.equals(position.getSystemId()));
    }

    @Test
    public void parseNISocialSecurityNumberComplete() throws Exception {
        System.out.println("info: testing social security number parsing");
        String systemID = UUID.randomUUID().toString();
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"foedselsnummer\": \"01010101011\" "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        SocialSecurityNumberDeserializer socialSecurityNumberDeserializer =
                new SocialSecurityNumberDeserializer();
        SocialSecurityNumber socialSecurityNumber =
                socialSecurityNumberDeserializer.deserialize(
                        jsonParser, null /* DeserializationContext */);
        assert (null != socialSecurityNumber);
        assert (systemID.equals(socialSecurityNumber.getSystemId()));
    }

    @Test
    public void parseNIUnitComplete() throws Exception {
        System.out.println("info: testing unit parsing");
        String systemID = UUID.randomUUID().toString();
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"organisasjonsnummer\": \"01010101011\" "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        UnitDeserializer unitDeserializer =
                new UnitDeserializer();
        Unit unit =
                unitDeserializer.deserialize(
                        jsonParser, null /* DeserializationContext */);
        assert (null != unit);
        assert (systemID.equals(unit.getSystemId()));
    }

    @Test
    public void parseConversionComplete() throws Exception {
        System.out.println("info: testing conversion parsing");
        String systemID = UUID.randomUUID().toString();
        String fromFormatCode = "fmt/95";
        String toFormatCode = "fmt/96";
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"konvertertDato\": \"2020-02-13T00:00:00+00:00\" "
                + ", \"konvertertAv\": \"Some One\" "
                + ", \"konvertertFraFormat\": { \"kode\": \"" + fromFormatCode + "\" } "
                + ", \"konvertertTilFormat\": { \"kode\": \"" + toFormatCode + "\" } "
                + ", \"konverteringsverktoey\": \"Some One\" "
                + ", \"konverteringskommentar\": \"Some One\" "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        ConversionDeserializer conversionDeserializer =
                new ConversionDeserializer();
        Conversion conversion =
                conversionDeserializer.deserialize(
                        jsonParser, null /* DeserializationContext */);
        assert (null != conversion);
        assert (systemID.equals(conversion.getSystemId()));
        assert (fromFormatCode.equals(conversion.getConvertedFromFormat().getCode()));
        assert (toFormatCode.equals(conversion.getConvertedToFormat().getCode()));
    }


    @Test
    public void parseCommentComplete() throws Exception {
        System.out.println("info: testing comment parsing");
        String systemID = UUID.randomUUID().toString();
        String json = "{"
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"merknadstekst\": \"A small note to the future\" "
                + ", \"merknadstype\": { \"kode\": \"MS\", \"kodenavn\": \"Merknad fra saksbehandler\" } "
                + ", \"merknadsdato\": \"2020-02-13T00:00:00+00:00\" "
                + ", \"merknadRegistrertAv\": \"Some One\" "
                //+", \"referanseMerknadRegistrertAv\": \"Some One\" "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        CommentDeserializer commentDeserializer = new CommentDeserializer();
        Comment comment =
                commentDeserializer.deserialize(
                        jsonParser, null /* DeserializationContext */);
        assert (null != comment);
        assert (systemID.equals(comment.getSystemId()));
    }

    @Test
    public void parseAuthorComplete() throws Exception {
        System.out.println("info: testing author parsing");
        String authorname = "Henrik Ibsen";
        String json = "{ "
                + "  \"forfatter\": \"" + authorname + "\" "
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        AuthorDeserializer authorDeserializer = new AuthorDeserializer();
        Author author =
                authorDeserializer.deserialize(
                        jsonParser, null /* DeserializationContext */);
        assert (null != author);
        assert (authorname.equals(author.getAuthor()));
    }

    @Test
    public void parseSignOffComplete() throws Exception {
        System.out.println("info: testing sign off parsing");
        String systemID = UUID.randomUUID().toString();
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"avskrevetAv\": \"Some Person\" "
                + ", \"avskrivningsdato\": \"1865-02-13T00:00:00+00:00\" "
                + ", \"avskrivningsmaate\": { \"kode\": \"BY\", \"kodenavn\": \"Besvart med brev\" } "
                //+", \"referanseAvskrevetAv\": \"?\" "
                +", \"referanseAvskrivesAvJournalpost\": \"909c1d62-5422-11ea-a529-9b91c0834dde\" "
                +", \"referanseAvskrivesAvKorrespondansepart\": \"95c230b0-5422-11ea-8738-37155656d12d\" "
                + "}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        SignOffDeserializer signOffDeserializer = new SignOffDeserializer();
        SignOff signOff = signOffDeserializer.deserialize
                (jsonParser, null /* DeserializationContext */);
        assert (null != signOff);
        assert (systemID.equals(signOff.getSystemId()));
        SignOffMethod m = signOff.getSignOffMethod();
        assert ("BY".equals(m.getCode()));
        assert ("Besvart med brev".equals(m.getCodeName()));
    }

    @Test
    public void parseDocumentFlowComplete() throws Exception {
        System.out.println("info: testing document flow parsing");
        String systemID = UUID.randomUUID().toString();
        String flowTo = "The Boss";
        String flowFrom = "Me";
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"flytTil\": \"" + flowTo + "\" "
                + ", \"referanseFlytTil\": \"8241d28c-566d-11ea-b6dd-372492b73cfc\" "
                + ", \"flytFra\": \"" + flowFrom + "\" "
                + ", \"referanseFlytFra\": \"86ecc936-566d-11ea-87e5-5f4424c512e8\" "
                + ", \"flytMottattDato\": \"1865-02-13T12:30:00+02:00\" "
                + ", \"flytSendtDato\": \"1865-02-13T12:00:00+02:00\" "
                + ", \"flytStatus\": { \"kode\": \"I\", \"kodenavn\": \"Ikke godkjent\" } "
                + ", \"flytMerknad\": \"Flown away\" "
                + "}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        DocumentFlowDeserializer documentFlowDeserializer =
            new DocumentFlowDeserializer();
        DocumentFlow documentFlow = documentFlowDeserializer.deserialize
                (jsonParser, null /* DeserializationContext */);
        assert (null != documentFlow);
        assert (systemID.equals(documentFlow.getSystemId()));
        assert (flowTo.equals(documentFlow.getFlowTo()));
        assert (flowFrom.equals(documentFlow.getFlowFrom()));
        FlowStatus m = documentFlow.getFlowStatus();
        assert ("I".equals(m.getCode()));
        assert ("Ikke godkjent".equals(m.getCodeName()));
    }

    @Test
    public void parsePrecedenceComplete() throws Exception {
        System.out.println("info: testing precedence parsing");
        String systemID = UUID.randomUUID().toString();
        String uuid = UUID.randomUUID().toString();
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"presedensDato\": \"1865-02-13Z\" "
                + ", \"opprettetDato\": \"1865-02-13T12:30:00+02:00\" "
                + ", \"opprettetAv\": \"Some One\" "
                //+ ", \"referanseOpprettetAv\": \"" + uuid + "\" "
                + ", \"tittel\": \"nice precedence\" "
                + ", \"beskrivelse\": \"nicer precedence\" "
                + ", \"presedensHjemmel\": \"nice law\" "
                + ", \"rettskildefaktor\": \"high\" "
                + ", \"presedensGodkjentDato\": \"1865-02-13T12:30:00+02:00\" "
                + ", \"presedensGodkjentAv\": \"Some One\" "
                + ", \"referansePresedensGodkjentAv\": \"" + uuid + "\" "
                + ", \"avsluttetDato\": \"1865-02-13T12:30:00+02:00\" "
                + ", \"avsluttetAv\": \"Some One\" "
                //+ ", \"referanseAvsluttetAv\": \"" + uuid + "\" "
                + ", \"presedensStatus\": { \"kode\": \"G\", \"kodenavn\": \"Gjeldende\" } "
                + "}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        PrecedenceDeserializer precedenceDeserializer =
            new PrecedenceDeserializer();
        Precedence precedence = precedenceDeserializer.deserialize
                (jsonParser, null /* DeserializationContext */);
        assert (null != precedence);
        assert (systemID.equals(precedence.getSystemId()));
        PrecedenceStatus m = precedence.getPrecedenceStatus();
        assert ("G".equals(m.getCode()));
        assert ("Gjeldende".equals(m.getCodeName()));
    }

    @Test
    public void parseChangeLogComplete() throws Exception {
        System.out.println("info: testing changelog parsing");
        String systemID = UUID.randomUUID().toString();
        String uuid = UUID.randomUUID().toString();
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"referanseArkivenhet\": \"" + uuid + "\" "
                + ", \"referanseMetadata\": \"metadata\" "
                + ", \"endretDato\": \"1865-02-13T12:30:00+02:00\" "
                + ", \"endretAv\": \"Some One\" "
                + ", \"referanseEndretAv\": \"" + uuid + "\" "
                + ", \"tidligereVerdi\": null "
                + ", \"nyVerdi\": \"who would know\" "
                + "}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        ChangeLogDeserializer changeLogDeserializer =
            new ChangeLogDeserializer();
        ChangeLog changeLog = changeLogDeserializer.deserialize
                (jsonParser, null /* DeserializationContext */);
        assert (null != changeLog);
        assert (systemID.equals(changeLog.getSystemId()));
        assert (uuid.equals(changeLog.getReferenceChangedBy()));
    }

    @Test
    public void parseEventLogComplete() throws Exception {
        System.out.println("info: testing eventlog parsing");
        String systemID = UUID.randomUUID().toString();
        String uuid = UUID.randomUUID().toString();
        String json = "{ "
                + "  \"systemID\": \"" + systemID + "\" "
                + ", \"referanseArkivenhet\": \"" + uuid + "\" "
                + ", \"referanseMetadata\": \"metadata\" "
                + ", \"endretDato\": \"1865-02-13T12:30:00+02:00\" "
                + ", \"endretAv\": \"Some One\" "
                + ", \"referanseEndretAv\": \"" + uuid + "\" "
                + ", \"tidligereVerdi\": null "
                + ", \"nyVerdi\": \"who would know\" "
                + ", \"hendelsetype\": {\"kode\": \"C\", \"kodenavn\": \"Opprett\" } "
                + ", \"beskrivelse\": \"beskrivelse\" "
                + ", \"hendelseDato\": \"1865-02-13T12:30:00+02:00\" "
                + "}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser =
                objectMapper.getFactory().createParser(json);
        EventLogDeserializer eventLogDeserializer =
            new EventLogDeserializer();
        EventLog eventLog = eventLogDeserializer.deserialize
                (jsonParser, null /* DeserializationContext */);
        assert (null != eventLog);
        assert (systemID.equals(eventLog.getSystemId()));
        assert (uuid.equals(eventLog.getReferenceChangedBy()));
        EventType m = eventLog.getEventType();
        assert ("C".equals(m.getCode()));
        assert ("Opprett".equals(m.getCodeName()));
    }
}
