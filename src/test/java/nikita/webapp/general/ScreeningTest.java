package nikita.webapp.general;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.jayway.jsonpath.JsonPath;
import nikita.N5CoreApp;
import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.*;
import nikita.common.model.noark5.v5.metadata.*;
import nikita.common.model.noark5.v5.secondary.Screening;
import nikita.common.model.noark5.v5.secondary.ScreeningMetadataLocal;
import nikita.webapp.spring.TestSecurityConfiguration;
import nikita.webapp.spring.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.io.StringWriter;

import static java.time.OffsetDateTime.parse;
import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.HREF;
import static nikita.common.config.HATEOASConstants.SELF;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(classes = N5CoreApp.class,
        webEnvironment = RANDOM_PORT)
@ContextConfiguration(classes = {TestSecurityConfiguration.class})
@AutoConfigureRestDocs(outputDir = "target/snippets")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class ScreeningTest {

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .build();
    }

    /**
     * Check that it is possible to add a Screening to a File when creating
     * the File. Then check that it is possible to associate
     * ScreeningMetadata with file.
     * <p>
     * The following steps are taken:
     * 1. Get File template (GET ny-mappe)
     * 2. Create file (POST ny-mappe)
     * 3. Get created file (GET mappe) and check Screening attributes and
     * REL/HREF
     * 4. Create ScreeningMetadata (POST ny-skjermingmetadata)
     * 5. Create ScreeningMetadata (POST ny-skjermingmetadata)
     * 6. Update the first ScreeningMetadata (PUT skjermingmetadata)
     * 7. Delete the first ScreeningMetadata (DELETE skjermingmetadata)
     * The test creates a chain of requests that is expected to be applicable
     * for File/Screening/ScreeningMetadata
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    @WithMockCustomUser
    public void addScreeningToFile() throws Exception {
        // First get template to create / POST file
        String urlNewFile = "/noark5v5/api/arkivstruktur/arkivdel" +
                "/f1102ae8-6c4c-4d93-aaa5-7c6220e50c4d/ny-mappe";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlNewFile)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$._links.['" +
                                REL_METADATA_SCREENING_METADATA + "'].['" +
                                HREF + "']").exists());

        // Next create a File object with an associated Screening
        // Note: We are not using the result of the GET ny-mappe, but want the
        // test to check tht it works

        File file = new File();
        file.setTitle("Title of file");

        // Create Metadata objects
        AccessRestriction accessRestriction =
                new AccessRestriction("P", "Personalsaker");
        ScreeningDocument screeningDocument = new ScreeningDocument(
                "H", "Skjerming av hele dokumentet");

        // Create a screening object and associate it with the File
        Screening screening = new Screening();
        screening.setAccessRestriction(accessRestriction);
        screening.setScreeningDocument(screeningDocument);
        screening.setScreeningAuthority("Unntatt etter Offentleglova");
        screening.setScreeningExpiresDate(parse("1942-07-25T00:00:00Z"));
        screening.setScreeningDuration(60);
        file.setReferenceScreening(screening);

        // Create a JSON object to POST
        JsonFactory factory = new JsonFactory();
        StringWriter jsonFileWriter = new StringWriter();
        JsonGenerator jsonFile = factory.createGenerator(jsonFileWriter);
        jsonFile.writeStartObject();
        printFileEntity(jsonFile, file);
        printScreening(jsonFile, file);
        jsonFile.writeEndObject();
        jsonFile.close();

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewFile)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonFileWriter.toString()));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$." + SYSTEM_ID)
                        .exists())
                .andExpect(jsonPath("$." + TITLE)
                        .value("Title of file"));
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();

        // Make sure we can retrieve the file and that the Screening
        // attributes are present
        String urlFile = "/noark5v5/api/arkivstruktur/mappe/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlFile)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        response = resultActions.andReturn().getResponse();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + SCREENING + "." +
                        ACCESS_RESTRICTION +
                        "." +
                        CODE).value("P"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        ACCESS_RESTRICTION + "." +
                        CODE_NAME).value("Personalsaker"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_SCREENING_DOCUMENT + "." +
                        CODE).value("H"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_SCREENING_DOCUMENT + "." +
                        CODE_NAME)
                        .value("Skjerming av hele dokumentet"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_AUTHORITY)
                        .value("Unntatt etter Offentleglova"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_DURATION)
                        .value(60))
                // Not picking them explicitly out as [0] [1] objects in the
                // array as the order might change later and then the test
                // will fail unnecessary
                .andExpect(jsonPath("$._links.['" +
                        REL_METADATA_SCREENING_METADATA + "'].href").exists())
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_EXPIRES_DATE).value(
                        anyOf(
                                is("1942-07-25T00:00:00Z"),
                                is("1942-07-25T00:00:00+00:00"))))
                .andExpect(jsonPath("$." + TITLE)
                        .value("Title of file"));
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));


        // Next, Create a ScreeningMetadata and POST it
        String urlNewScreeningMetadata = "/noark5v5/api/arkivstruktur/mappe/" +
                JsonPath.read(response.getContentAsString(),
                        "$." + SYSTEM_ID) + "/" + NEW_SCREENING_METADATA + "/";
        // Make a note of the url for ScreeningMetadata associated with the
        // file. Will be used later.
        String urlScreeningMetadata = "/noark5v5/api/arkivstruktur/mappe/" +
                JsonPath.read(response.getContentAsString(),
                        "$." + SYSTEM_ID) + "/" + SCREENING_METADATA + "/";

        ScreeningMetadataLocal screeningMetadata = new ScreeningMetadataLocal();
        screeningMetadata.setCode("NA");
        screeningMetadata.setCodeName("Skjerming navn avsender");

        // Create a JSON object to POST
        StringWriter jsonScreeningMetadataWriter = new StringWriter();
        JsonGenerator jsonScreeningMetadata =
                factory.createGenerator(jsonScreeningMetadataWriter);
        printScreeningMetadata(jsonScreeningMetadata, screeningMetadata);
        jsonScreeningMetadata.close();

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewScreeningMetadata)
                .contextPath("/noark5v5")
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonScreeningMetadataWriter.toString())
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$." + CODE)
                        .exists())
                .andExpect(jsonPath("$." + CODE_NAME)
                        .exists())
                .andExpect(jsonPath("$._links.['" + SELF + "'].href")
                        .exists())
                .andExpect(jsonPath(
                        "$._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA +
                                "'].['" + HREF + "']").exists());
        ;

        // Create another ScreeningMetadata and POST it
        screeningMetadata.setCode("TKL");
        screeningMetadata.setCodeName("Skjerming tittel klasse");

        // Create a JSON object to POST
        jsonScreeningMetadataWriter = new StringWriter();
        jsonScreeningMetadata = factory
                .createGenerator(jsonScreeningMetadataWriter);
        printScreeningMetadata(jsonScreeningMetadata, screeningMetadata);
        jsonScreeningMetadata.close();

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewScreeningMetadata)
                .contextPath("/noark5v5")
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonScreeningMetadataWriter.toString())
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$." + CODE)
                        .exists())
                .andExpect(jsonPath("$." + CODE_NAME)
                        .exists())
                .andExpect(jsonPath("$._links.['" + SELF + "'].href")
                        .exists())
                .andExpect(jsonPath(
                        "$._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA +
                                "'].['" + HREF + "']").exists());
        ;

        // Check that it is possible to retrieve the two ScreeningMetadata
        // objects
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlScreeningMetadata)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        response = resultActions.andReturn().getResponse();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(2)))
                .andExpect(jsonPath("$.results.[0]." + CODE)
                        .exists())
                .andExpect(jsonPath("$.results.[0]." + CODE_NAME)
                        .exists())
                .andExpect(jsonPath("$.results.[1]." + CODE)
                        .exists())
                .andExpect(jsonPath("$.results.[1]." + CODE_NAME)
                        .exists())
                .andExpect(jsonPath("$.results.[0]._links.['" +
                        SELF + "'].href")
                        .exists())
                .andExpect(jsonPath(
                        "$.results.[0]._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA + "'].['" +
                                HREF + "']").exists())
                .andExpect(jsonPath("$.results.[1]._links.['" +
                        SELF + "'].href")
                        .exists())
                .andExpect(jsonPath(
                        "$.results.[1]._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA + "'].['" +
                                HREF + "']").exists());

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        // Next, see that we can update the ScreeningMetadata
        String urlComplete = JsonPath.read(response.getContentAsString(),
                "$.results.[0]._links.self.href");
        urlScreeningMetadata = "/noark5v5/api/arkivstruktur" +
                "/skjermingmetadata" + urlComplete.split("/noark5v5/api" +
                "/arkivstruktur/skjermingmetadata")[1];

        // Create a ScreeningMetadata object different to the previous ones
        // that we can use to override the data in nikita
        ScreeningMetadata screeningMetadata2 = new ScreeningMetadata();
        screeningMetadata2.setCode("TM1");
        screeningMetadata2.setCodeName("Skjerming tittel mappe - unntatt første linje");

        // Create a JSON object to POST
        StringWriter jsonScreeningWriter = new StringWriter();
        JsonGenerator jsonScreening =
                factory.createGenerator(jsonScreeningWriter);
        printNullableMetadata(jsonScreening, SCREENING_SCREENING_DOCUMENT,
                screeningMetadata2, false);
        jsonScreening.close();

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put(urlScreeningMetadata)
                .contextPath("/noark5v5")
                .header("ETAG", "\"0\"")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonScreeningWriter.toString()));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['" + CODE + "']")
                        .value("TM1"))
                .andExpect(jsonPath("$.['" + CODE_NAME + "']")
                        .value("Skjerming tittel mappe - unntatt første linje"))
                .andExpect(jsonPath(
                        "$._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA + "'].['" +
                                HREF + "']").exists())
                .andExpect(jsonPath(
                        "$._links.['" + SELF + "'].['" + HREF + "']").exists());

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        // Next, check it is possible to delete the ScreeningMetadata
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlScreeningMetadata)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isNoContent());
    }

    /**
     * Check that it is possible to add a Screening to a DocumentDescription when creating
     * the DocumentDescription. Then check that it is possible to associate
     * ScreeningMetadata with documentDescription.
     * <p>
     * The following steps are taken:
     * 1. Get DocumentDescription template (GET ny-dokumentbeskrivelse)
     * 2. Create documentDescription (POST ny-dokumentbeskrivelse)
     * 3. Get created documentDescription (GET dokumentbeskrivelse) and check Screening attributes and
     * REL/HREF
     * 4. Create ScreeningMetadata (POST ny-skjermingmetadata)
     * 5. Create ScreeningMetadata (POST ny-skjermingmetadata)
     * 6. Update the first ScreeningMetadata (PUT skjermingmetadata)
     * 7. Delete the first ScreeningMetadata (DELETE skjermingmetadata)
     * The test creates a chain of requests that is expected to be applicable
     * for DocumentDescription/Screening/ScreeningMetadata
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    @WithMockCustomUser
    public void addScreeningToDocumentDescription() throws Exception {
        // First get template to create / POST documentDescription
        String urlNewDocumentDescription = "/noark5v5/api/arkivstruktur" +
                "/registrering/dc600862-3298-4ec0-8541-3e51fb900054/" +
                "ny-dokumentbeskrivelse";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlNewDocumentDescription)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$._links.['" +
                                REL_METADATA_SCREENING_METADATA + "'].['" +
                                HREF + "']").exists());

        // Next create a DocumentDescription object with an associated Screening
        // Note: We are not using the result of the GET ny-dokumentbeskrivelse, but want the
        // test to check tht it works

        DocumentDescription documentDescription = new DocumentDescription();
        documentDescription.setTitle("Title of documentDescription");

        // Create Metadata objects
        AccessRestriction accessRestriction =
                new AccessRestriction("P", "Personalsaker");
        ScreeningDocument screeningDocument = new ScreeningDocument(
                "H", "Skjerming av hele dokumentet");

        // Create a screening object and associate it with the DocumentDescription
        Screening screening = new Screening();
        screening.setAccessRestriction(accessRestriction);
        screening.setScreeningDocument(screeningDocument);
        screening.setScreeningAuthority("Unntatt etter Offentleglova");
        screening.setScreeningExpiresDate(parse("1942-07-25T00:00:00Z"));
        screening.setScreeningDuration(60);
        documentDescription.setReferenceScreening(screening);


        AssociatedWithRecordAs associatedWithRecordAs =
                new AssociatedWithRecordAs();
        associatedWithRecordAs.setCode("H");
        associatedWithRecordAs.setCodeName("Hoveddokument");
        documentDescription.setAssociatedWithRecordAs(associatedWithRecordAs);

        DocumentStatus documentStatus = new DocumentStatus();
        documentStatus.setCode("B");
        documentStatus.setCodeName("Dokumentet er under redigering");
        documentDescription.setDocumentStatus(documentStatus);

        DocumentType documentType = new DocumentType();
        documentType.setCode("B");
        documentType.setCodeName("Brev");
        documentDescription.setDocumentType(documentType);
        // Create a JSON object to POST
        JsonFactory factory = new JsonFactory();
        StringWriter jsonDocumentDescriptionWriter = new StringWriter();
        JsonGenerator jsonDocumentDescription =
                factory.createGenerator(jsonDocumentDescriptionWriter);
        jsonDocumentDescription.writeStartObject();
        printTitleAndDescription(jsonDocumentDescription, documentDescription);
        printNullableMetadata(jsonDocumentDescription,
                DOCUMENT_DESCRIPTION_DOCUMENT_TYPE,
                documentDescription.getDocumentType());
        printNullableMetadata(jsonDocumentDescription,
                DOCUMENT_DESCRIPTION_STATUS,
                documentDescription.getDocumentStatus());
        printNullableMetadata(jsonDocumentDescription,
                DOCUMENT_DESCRIPTION_ASSOCIATED_WITH_RECORD_AS,
                documentDescription.getAssociatedWithRecordAs());
        printScreening(jsonDocumentDescription, documentDescription);
        jsonDocumentDescription.writeEndObject();
        jsonDocumentDescription.close();

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewDocumentDescription)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonDocumentDescriptionWriter.toString()));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$." + SYSTEM_ID)
                        .exists())
                .andExpect(jsonPath("$." + TITLE)
                        .value("Title of documentDescription"));
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();

        // Make sure we can retrieve the documentDescription and that the Screening
        // attributes are present
        String urlDocumentDescription = "/noark5v5/api/arkivstruktur/dokumentbeskrivelse/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlDocumentDescription)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        response = resultActions.andReturn().getResponse();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + SCREENING + "." +
                        ACCESS_RESTRICTION +
                        "." +
                        CODE).value("P"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        ACCESS_RESTRICTION + "." +
                        CODE_NAME).value("Personalsaker"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_SCREENING_DOCUMENT + "." +
                        CODE).value("H"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_SCREENING_DOCUMENT + "." +
                        CODE_NAME)
                        .value("Skjerming av hele dokumentet"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_AUTHORITY)
                        .value("Unntatt etter Offentleglova"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_DURATION)
                        .value(60))
                // Not picking them explicitly out as [0] [1] objects in the
                // array as the order might change later and then the test
                // will fail unnecessary
                .andExpect(jsonPath("$._links.['" +
                        REL_METADATA_SCREENING_METADATA + "'].href").exists())
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_EXPIRES_DATE).value(
                        anyOf(
                                is("1942-07-25T00:00:00Z"),
                                is("1942-07-25T00:00:00+00:00"))))
                .andExpect(jsonPath("$." + TITLE)
                        .value("Title of documentDescription"));
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));


        // Next, Create a ScreeningMetadata and POST it
        String urlNewScreeningMetadata = "/noark5v5/api/arkivstruktur/dokumentbeskrivelse/" +
                JsonPath.read(response.getContentAsString(),
                        "$." + SYSTEM_ID) + "/" + NEW_SCREENING_METADATA + "/";
        // Make a note of the url for ScreeningMetadata associated with the
        // documentDescription. Will be used later.
        String urlScreeningMetadata = "/noark5v5/api/arkivstruktur/dokumentbeskrivelse/" +
                JsonPath.read(response.getContentAsString(),
                        "$." + SYSTEM_ID) + "/" + SCREENING_METADATA + "/";

        ScreeningMetadataLocal screeningMetadata = new ScreeningMetadataLocal();
        screeningMetadata.setCode("NA");
        screeningMetadata.setCodeName("Skjerming navn avsender");

        // Create a JSON object to POST
        StringWriter jsonScreeningMetadataWriter = new StringWriter();
        JsonGenerator jsonScreeningMetadata =
                factory.createGenerator(jsonScreeningMetadataWriter);
        printScreeningMetadata(jsonScreeningMetadata, screeningMetadata);
        jsonScreeningMetadata.close();

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewScreeningMetadata)
                .contextPath("/noark5v5")
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonScreeningMetadataWriter.toString())
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$." + CODE)
                        .exists())
                .andExpect(jsonPath("$." + CODE_NAME)
                        .exists())
                .andExpect(jsonPath("$._links.['" + SELF + "'].href")
                        .exists())
                .andExpect(jsonPath(
                        "$._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA +
                                "'].['" + HREF + "']").exists());
        ;

        // Create another ScreeningMetadata and POST it
        screeningMetadata.setCode("TKL");
        screeningMetadata.setCodeName("Skjerming tittel klasse");

        // Create a JSON object to POST
        jsonScreeningMetadataWriter = new StringWriter();
        jsonScreeningMetadata = factory
                .createGenerator(jsonScreeningMetadataWriter);
        printScreeningMetadata(jsonScreeningMetadata, screeningMetadata);
        jsonScreeningMetadata.close();

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewScreeningMetadata)
                .contextPath("/noark5v5")
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonScreeningMetadataWriter.toString())
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$." + CODE)
                        .exists())
                .andExpect(jsonPath("$." + CODE_NAME)
                        .exists())
                .andExpect(jsonPath("$._links.['" + SELF + "'].href")
                        .exists())
                .andExpect(jsonPath(
                        "$._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA +
                                "'].['" + HREF + "']").exists());
        ;

        // Check that it is possible to retrieve the two ScreeningMetadata
        // objects
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlScreeningMetadata)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        response = resultActions.andReturn().getResponse();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(2)))
                .andExpect(jsonPath("$.results.[0]." + CODE)
                        .exists())
                .andExpect(jsonPath("$.results.[0]." + CODE_NAME)
                        .exists())
                .andExpect(jsonPath("$.results.[1]." + CODE)
                        .exists())
                .andExpect(jsonPath("$.results.[1]." + CODE_NAME)
                        .exists())
                .andExpect(jsonPath("$.results.[0]._links.['" +
                        SELF + "'].href")
                        .exists())
                .andExpect(jsonPath(
                        "$.results.[0]._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA + "'].['" +
                                HREF + "']").exists())
                .andExpect(jsonPath("$.results.[1]._links.['" +
                        SELF + "'].href")
                        .exists())
                .andExpect(jsonPath(
                        "$.results.[1]._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA + "'].['" +
                                HREF + "']").exists());

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        // Next, see that we can update the ScreeningMetadata
        String urlComplete = JsonPath.read(response.getContentAsString(),
                "$.results.[0]._links.self.href");
        urlScreeningMetadata = "/noark5v5/api/arkivstruktur" +
                "/skjermingmetadata" + urlComplete.split("/noark5v5/api" +
                "/arkivstruktur/skjermingmetadata")[1];

        // Create a ScreeningMetadata object different to the previous ones
        // that we can use to override the data in nikita
        ScreeningMetadata screeningMetadata2 = new ScreeningMetadata();
        screeningMetadata2.setCode("TM1");
        screeningMetadata2.setCodeName("Skjerming tittel mappe - unntatt første linje");

        // Create a JSON object to POST
        StringWriter jsonScreeningWriter = new StringWriter();
        JsonGenerator jsonScreening =
                factory.createGenerator(jsonScreeningWriter);
        printNullableMetadata(jsonScreening, SCREENING_SCREENING_DOCUMENT,
                screeningMetadata2, false);
        jsonScreening.close();

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put(urlScreeningMetadata)
                .contextPath("/noark5v5")
                .header("ETAG", "\"0\"")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonScreeningWriter.toString()));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['" + CODE + "']")
                        .value("TM1"))
                .andExpect(jsonPath("$.['" + CODE_NAME + "']")
                        .value("Skjerming tittel mappe - unntatt første linje"))
                .andExpect(jsonPath(
                        "$._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA + "'].['" +
                                HREF + "']").exists())
                .andExpect(jsonPath(
                        "$._links.['" + SELF + "'].['" + HREF + "']").exists());

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        // Next, check it is possible to delete the ScreeningMetadata
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlScreeningMetadata)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isNoContent());
    }

    /**
     * Check that it is possible to add a Screening to a Class when creating
     * the Class. Then check that it is possible to associate
     * ScreeningMetadata with class.
     * <p>
     * The following steps are taken:
     * 1. Get Class template (GET ny-klasse)
     * 2. Create class (POST ny-klasse)
     * 3. Get created class (GET klasse) and check Screening attributes and
     * REL/HREF
     * 4. Create ScreeningMetadata (POST ny-skjermingmetadata)
     * 5. Create ScreeningMetadata (POST ny-skjermingmetadata)
     * 6. Update the first ScreeningMetadata (PUT skjermingmetadata)
     * 7. Delete the first ScreeningMetadata (DELETE skjermingmetadata)
     * The test creates a chain of requests that is expected to be applicable
     * for Class/Screening/ScreeningMetadata
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    @WithMockCustomUser
    public void addScreeningToClass() throws Exception {
        // First get template to create / POST class
        String urlNewClass = "/noark5v5/api/arkivstruktur" +
                "/" + CLASSIFICATION_SYSTEM +
                "/2d0b2dc1-f3bb-4239-bf04-582b1085581c/ny-klasse";
/*
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlNewClass)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$._links.['" +
                                REL_METADATA_SCREENING_METADATA + "'].['" +
                                HREF + "']").exists());
*/
        // Next create a Class object with an associated Screening
        // Note: We are not using the result of the GET ny-klasse, but want the
        // test to check tht it works

        Class klass = new Class();
        klass.setTitle("Title of class");
        klass.setClassId("CLASSID");
        // Create Metadata objects
        AccessRestriction accessRestriction =
                new AccessRestriction("P", "Personalsaker");
        ScreeningDocument screeningDocument = new ScreeningDocument(
                "H", "Skjerming av hele dokumentet");

        // Create a screening object and associate it with the Class
        Screening screening = new Screening();
        screening.setAccessRestriction(accessRestriction);
        screening.setScreeningDocument(screeningDocument);
        screening.setScreeningAuthority("Unntatt etter Offentleglova");
        screening.setScreeningExpiresDate(parse("1942-07-25T00:00:00Z"));
        screening.setScreeningDuration(60);
        klass.setReferenceScreening(screening);

        // Create a JSON object to POST
        JsonFactory factory = new JsonFactory();
        StringWriter jsonClassWriter = new StringWriter();
        JsonGenerator jsonClass =
                factory.createGenerator(jsonClassWriter);
        jsonClass.writeStartObject();
        printTitleAndDescription(jsonClass, klass);
        printScreening(jsonClass, klass);
        jsonClass.writeEndObject();
        jsonClass.close();
        System.out.println(jsonClassWriter.toString());
        ResultActions resultActions =
                mockMvc.perform(MockMvcRequestBuilders
                        .post(urlNewClass)
                        .contextPath("/noark5v5")
                        .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                        .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                        .content(jsonClassWriter.toString()));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$." + SYSTEM_ID)
                        .exists())
                .andExpect(jsonPath("$." + TITLE)
                        .value("Title of class"));
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();

        // Make sure we can retrieve the class and that the Screening
        // attributes are present
        String urlClass = "/noark5v5/api/arkivstruktur/klasse/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlClass)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        response = resultActions.andReturn().getResponse();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + SCREENING + "." +
                        ACCESS_RESTRICTION +
                        "." +
                        CODE).value("P"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        ACCESS_RESTRICTION + "." +
                        CODE_NAME).value("Personalsaker"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_SCREENING_DOCUMENT + "." +
                        CODE).value("H"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_SCREENING_DOCUMENT + "." +
                        CODE_NAME)
                        .value("Skjerming av hele dokumentet"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_AUTHORITY)
                        .value("Unntatt etter Offentleglova"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_DURATION)
                        .value(60))
                // Not picking them explicitly out as [0] [1] objects in the
                // array as the order might change later and then the test
                // will fail unnecessary
                .andExpect(jsonPath("$._links.['" +
                        REL_METADATA_SCREENING_METADATA + "'].href").exists())
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_EXPIRES_DATE).value(
                        anyOf(
                                is("1942-07-25T00:00:00Z"),
                                is("1942-07-25T00:00:00+00:00"))))
                .andExpect(jsonPath("$." + TITLE)
                        .value("Title of class"));
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));


        // Next, Create a ScreeningMetadata and POST it
        String urlNewScreeningMetadata = "/noark5v5/api/arkivstruktur/klasse/" +
                JsonPath.read(response.getContentAsString(),
                        "$." + SYSTEM_ID) + "/" + NEW_SCREENING_METADATA + "/";
        // Make a note of the url for ScreeningMetadata associated with the
        // class. Will be used later.
        String urlScreeningMetadata = "/noark5v5/api/arkivstruktur/klasse/" +
                JsonPath.read(response.getContentAsString(),
                        "$." + SYSTEM_ID) + "/" + SCREENING_METADATA + "/";

        ScreeningMetadataLocal screeningMetadata = new ScreeningMetadataLocal();
        screeningMetadata.setCode("NA");
        screeningMetadata.setCodeName("Skjerming navn avsender");

        // Create a JSON object to POST
        StringWriter jsonScreeningMetadataWriter = new StringWriter();
        JsonGenerator jsonScreeningMetadata =
                factory.createGenerator(jsonScreeningMetadataWriter);
        printScreeningMetadata(jsonScreeningMetadata, screeningMetadata);
        jsonScreeningMetadata.close();

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewScreeningMetadata)
                .contextPath("/noark5v5")
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonScreeningMetadataWriter.toString())
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$." + CODE)
                        .exists())
                .andExpect(jsonPath("$." + CODE_NAME)
                        .exists())
                .andExpect(jsonPath("$._links.['" + SELF + "'].href")
                        .exists())
                .andExpect(jsonPath(
                        "$._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA +
                                "'].['" + HREF + "']").exists());
        ;

        // Create another ScreeningMetadata and POST it
        screeningMetadata.setCode("TKL");
        screeningMetadata.setCodeName("Skjerming tittel klasse");

        // Create a JSON object to POST
        jsonScreeningMetadataWriter = new StringWriter();
        jsonScreeningMetadata = factory
                .createGenerator(jsonScreeningMetadataWriter);
        printScreeningMetadata(jsonScreeningMetadata, screeningMetadata);
        jsonScreeningMetadata.close();

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewScreeningMetadata)
                .contextPath("/noark5v5")
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonScreeningMetadataWriter.toString())
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$." + CODE)
                        .exists())
                .andExpect(jsonPath("$." + CODE_NAME)
                        .exists())
                .andExpect(jsonPath("$._links.['" + SELF + "'].href")
                        .exists())
                .andExpect(jsonPath(
                        "$._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA +
                                "'].['" + HREF + "']").exists());
        ;

        // Check that it is possible to retrieve the two ScreeningMetadata
        // objects
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlScreeningMetadata)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        response = resultActions.andReturn().getResponse();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(2)))
                .andExpect(jsonPath("$.results.[0]." + CODE)
                        .exists())
                .andExpect(jsonPath("$.results.[0]." + CODE_NAME)
                        .exists())
                .andExpect(jsonPath("$.results.[1]." + CODE)
                        .exists())
                .andExpect(jsonPath("$.results.[1]." + CODE_NAME)
                        .exists())
                .andExpect(jsonPath("$.results.[0]._links.['" +
                        SELF + "'].href")
                        .exists())
                .andExpect(jsonPath(
                        "$.results.[0]._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA + "'].['" +
                                HREF + "']").exists())
                .andExpect(jsonPath("$.results.[1]._links.['" +
                        SELF + "'].href")
                        .exists())
                .andExpect(jsonPath(
                        "$.results.[1]._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA + "'].['" +
                                HREF + "']").exists());

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        // Next, see that we can update the ScreeningMetadata
        String urlComplete = JsonPath.read(response.getContentAsString(),
                "$.results.[0]._links.self.href");
        urlScreeningMetadata = "/noark5v5/api/arkivstruktur" +
                "/skjermingmetadata" + urlComplete.split("/noark5v5/api" +
                "/arkivstruktur/skjermingmetadata")[1];

        // Create a ScreeningMetadata object different to the previous ones
        // that we can use to override the data in nikita
        ScreeningMetadata screeningMetadata2 = new ScreeningMetadata();
        screeningMetadata2.setCode("TM1");
        screeningMetadata2.setCodeName("Skjerming tittel mappe - unntatt første linje");

        // Create a JSON object to POST
        StringWriter jsonScreeningWriter = new StringWriter();
        JsonGenerator jsonScreening =
                factory.createGenerator(jsonScreeningWriter);
        printNullableMetadata(jsonScreening, SCREENING_SCREENING_DOCUMENT,
                screeningMetadata2, false);
        jsonScreening.close();

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put(urlScreeningMetadata)
                .contextPath("/noark5v5")
                .header("ETAG", "\"0\"")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonScreeningWriter.toString()));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['" + CODE + "']")
                        .value("TM1"))
                .andExpect(jsonPath("$.['" + CODE_NAME + "']")
                        .value("Skjerming tittel mappe - unntatt første linje"))
                .andExpect(jsonPath(
                        "$._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA + "'].['" +
                                HREF + "']").exists())
                .andExpect(jsonPath(
                        "$._links.['" + SELF + "'].['" + HREF + "']").exists());

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        // Next, check it is possible to delete the ScreeningMetadata
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlScreeningMetadata)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isNoContent());
    }


    /**
     * Check that it is possible to add a Screening to a Series when creating
     * the Series. Then check that it is possible to associate
     * ScreeningMetadata with series.
     * <p>
     * The following steps are taken:
     * 1. Get Series template (GET ny-arkivdel)
     * 2. Create series (POST ny-arkivdel)
     * 3. Get created series (GET arkivdel) and check Screening attributes and
     * REL/HREF
     * 4. Create ScreeningMetadata (POST ny-skjermingmetadata)
     * 5. Create ScreeningMetadata (POST ny-skjermingmetadata)
     * 6. Update the first ScreeningMetadata (PUT skjermingmetadata)
     * 7. Delete the first ScreeningMetadata (DELETE skjermingmetadata)
     * The test creates a chain of requests that is expected to be applicable
     * for Series/Screening/ScreeningMetadata
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    @WithMockCustomUser
    public void addScreeningToSeries() throws Exception {
        // First get template to create / POST series
        String urlNewSeries = "/noark5v5/api/arkivstruktur" +
                "/arkiv/3318a63f-11a7-4ec9-8bf1-4144b7f281cf/ny-arkivdel";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlNewSeries)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$._links.['" +
                                REL_METADATA_SCREENING_METADATA + "'].['" +
                                HREF + "']").exists());

        // Next create a Series object with an associated Screening
        // Note: We are not using the result of the GET ny-arkivdel, but want the
        // test to check tht it works

        Series series = new Series();
        series.setTitle("Title of series");

        // Create Metadata objects
        AccessRestriction accessRestriction =
                new AccessRestriction("P", "Personalsaker");
        ScreeningDocument screeningDocument = new ScreeningDocument(
                "H", "Skjerming av hele dokumentet");

        // Create a screening object and associate it with the Series
        Screening screening = new Screening();
        screening.setAccessRestriction(accessRestriction);
        screening.setScreeningDocument(screeningDocument);
        screening.setScreeningAuthority("Unntatt etter Offentleglova");
        screening.setScreeningExpiresDate(parse("1942-07-25T00:00:00Z"));
        screening.setScreeningDuration(60);
        series.setReferenceScreening(screening);

        SeriesStatus seriesStatus = new SeriesStatus();
        seriesStatus.setCode("A");
        seriesStatus.setCodeName("Aktiv periode");
        series.setSeriesStatus(seriesStatus);

        // Create a JSON object to POST
        JsonFactory factory = new JsonFactory();
        StringWriter jsonSeriesWriter = new StringWriter();
        JsonGenerator jsonSeries =
                factory.createGenerator(jsonSeriesWriter);
        jsonSeries.writeStartObject();
        printTitleAndDescription(jsonSeries, series);
        printNullableMetadata(jsonSeries,
                SERIES_STATUS,
                series.getSeriesStatus());
        printScreening(jsonSeries, series);
        jsonSeries.writeEndObject();
        jsonSeries.close();

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewSeries)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonSeriesWriter.toString()));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$." + SYSTEM_ID)
                        .exists())
                .andExpect(jsonPath("$." + TITLE)
                        .value("Title of series"));
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();

        // Make sure we can retrieve the series and that the Screening
        // attributes are present
        String urlSeries = "/noark5v5/api/arkivstruktur/arkivdel/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlSeries)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        response = resultActions.andReturn().getResponse();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + SCREENING + "." +
                        ACCESS_RESTRICTION +
                        "." +
                        CODE).value("P"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        ACCESS_RESTRICTION + "." +
                        CODE_NAME).value("Personalsaker"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_SCREENING_DOCUMENT + "." +
                        CODE).value("H"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_SCREENING_DOCUMENT + "." +
                        CODE_NAME)
                        .value("Skjerming av hele dokumentet"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_AUTHORITY)
                        .value("Unntatt etter Offentleglova"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_DURATION)
                        .value(60))
                // Not picking them explicitly out as [0] [1] objects in the
                // array as the order might change later and then the test
                // will fail unnecessary
                .andExpect(jsonPath("$._links.['" +
                        REL_METADATA_SCREENING_METADATA + "'].href").exists())
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_EXPIRES_DATE).value(
                        anyOf(
                                is("1942-07-25T00:00:00Z"),
                                is("1942-07-25T00:00:00+00:00"))))
                .andExpect(jsonPath("$." + TITLE)
                        .value("Title of series"));
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));


        // Next, Create a ScreeningMetadata and POST it
        String urlNewScreeningMetadata = "/noark5v5/api/arkivstruktur/arkivdel/" +
                JsonPath.read(response.getContentAsString(),
                        "$." + SYSTEM_ID) + "/" + NEW_SCREENING_METADATA + "/";
        // Make a note of the url for ScreeningMetadata associated with the
        // series. Will be used later.
        String urlScreeningMetadata = "/noark5v5/api/arkivstruktur/arkivdel/" +
                JsonPath.read(response.getContentAsString(),
                        "$." + SYSTEM_ID) + "/" + SCREENING_METADATA + "/";

        ScreeningMetadataLocal screeningMetadata = new ScreeningMetadataLocal();
        screeningMetadata.setCode("NA");
        screeningMetadata.setCodeName("Skjerming navn avsender");

        // Create a JSON object to POST
        StringWriter jsonScreeningMetadataWriter = new StringWriter();
        JsonGenerator jsonScreeningMetadata =
                factory.createGenerator(jsonScreeningMetadataWriter);
        printScreeningMetadata(jsonScreeningMetadata, screeningMetadata);
        jsonScreeningMetadata.close();

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewScreeningMetadata)
                .contextPath("/noark5v5")
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonScreeningMetadataWriter.toString())
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$." + CODE)
                        .exists())
                .andExpect(jsonPath("$." + CODE_NAME)
                        .exists())
                .andExpect(jsonPath("$._links.['" + SELF + "'].href")
                        .exists())
                .andExpect(jsonPath(
                        "$._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA +
                                "'].['" + HREF + "']").exists());
        ;

        // Create another ScreeningMetadata and POST it
        screeningMetadata.setCode("TKL");
        screeningMetadata.setCodeName("Skjerming tittel klasse");

        // Create a JSON object to POST
        jsonScreeningMetadataWriter = new StringWriter();
        jsonScreeningMetadata = factory
                .createGenerator(jsonScreeningMetadataWriter);
        printScreeningMetadata(jsonScreeningMetadata, screeningMetadata);
        jsonScreeningMetadata.close();

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewScreeningMetadata)
                .contextPath("/noark5v5")
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonScreeningMetadataWriter.toString())
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$." + CODE)
                        .exists())
                .andExpect(jsonPath("$." + CODE_NAME)
                        .exists())
                .andExpect(jsonPath("$._links.['" + SELF + "'].href")
                        .exists())
                .andExpect(jsonPath(
                        "$._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA +
                                "'].['" + HREF + "']").exists());
        ;

        // Check that it is possible to retrieve the two ScreeningMetadata
        // objects
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlScreeningMetadata)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        response = resultActions.andReturn().getResponse();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(2)))
                .andExpect(jsonPath("$.results.[0]." + CODE)
                        .exists())
                .andExpect(jsonPath("$.results.[0]." + CODE_NAME)
                        .exists())
                .andExpect(jsonPath("$.results.[1]." + CODE)
                        .exists())
                .andExpect(jsonPath("$.results.[1]." + CODE_NAME)
                        .exists())
                .andExpect(jsonPath("$.results.[0]._links.['" +
                        SELF + "'].href")
                        .exists())
                .andExpect(jsonPath(
                        "$.results.[0]._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA + "'].['" +
                                HREF + "']").exists())
                .andExpect(jsonPath("$.results.[1]._links.['" +
                        SELF + "'].href")
                        .exists())
                .andExpect(jsonPath(
                        "$.results.[1]._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA + "'].['" +
                                HREF + "']").exists());

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        // Next, see that we can update the ScreeningMetadata
        String urlComplete = JsonPath.read(response.getContentAsString(),
                "$.results.[0]._links.self.href");
        urlScreeningMetadata = "/noark5v5/api/arkivstruktur" +
                "/skjermingmetadata" + urlComplete.split("/noark5v5/api" +
                "/arkivstruktur/skjermingmetadata")[1];

        // Create a ScreeningMetadata object different to the previous ones
        // that we can use to override the data in nikita
        ScreeningMetadata screeningMetadata2 = new ScreeningMetadata();
        screeningMetadata2.setCode("TM1");
        screeningMetadata2.setCodeName("Skjerming tittel mappe - unntatt første linje");

        // Create a JSON object to POST
        StringWriter jsonScreeningWriter = new StringWriter();
        JsonGenerator jsonScreening =
                factory.createGenerator(jsonScreeningWriter);
        printNullableMetadata(jsonScreening, SCREENING_SCREENING_DOCUMENT,
                screeningMetadata2, false);
        jsonScreening.close();

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put(urlScreeningMetadata)
                .contextPath("/noark5v5")
                .header("ETAG", "\"0\"")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonScreeningWriter.toString()));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['" + CODE + "']")
                        .value("TM1"))
                .andExpect(jsonPath("$.['" + CODE_NAME + "']")
                        .value("Skjerming tittel mappe - unntatt første linje"))
                .andExpect(jsonPath(
                        "$._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA + "'].['" +
                                HREF + "']").exists())
                .andExpect(jsonPath(
                        "$._links.['" + SELF + "'].['" + HREF + "']").exists());

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        // Next, check it is possible to delete the ScreeningMetadata
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlScreeningMetadata)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isNoContent());
    }

    /**
     * Check that it is possible to add a Screening to a Record when creating
     * the Record. Then check that it is possible to associate
     * ScreeningMetadata with record.
     * <p>
     * The following steps are taken:
     * 1. Get Record template (GET ny-registrering)
     * 2. Create record (POST ny-registrering)
     * 3. Get created record (GET registrering) and check Screening attributes and
     * REL/HREF
     * 4. Create ScreeningMetadata (POST ny-skjermingmetadata)
     * 5. Create ScreeningMetadata (POST ny-skjermingmetadata)
     * 6. Update the first ScreeningMetadata (PUT skjermingmetadata)
     * 7. Delete the first ScreeningMetadata (DELETE skjermingmetadata)
     * The test creates a chain of requests that is expected to be applicable
     * for Record/Screening/ScreeningMetadata
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    @WithMockCustomUser
    public void addScreeningToRecord() throws Exception {
        // First get template to create / POST record
        String urlNewRecord = "/noark5v5/api/arkivstruktur" +
                "/mappe/fed888c6-83e1-4ed0-922a-bd5770af3fad/ny-registrering";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlNewRecord)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$._links.['" +
                                REL_METADATA_SCREENING_METADATA + "'].['" +
                                HREF + "']").exists());

        // Next create a Record object with an associated Screening
        // Note: We are not using the result of the GET ny-registrering, but want the
        // test to check tht it works

        RecordEntity record = new RecordEntity();
        record.setTitle("Title of record");

        // Create Metadata objects
        AccessRestriction accessRestriction =
                new AccessRestriction("P", "Personalsaker");
        ScreeningDocument screeningDocument = new ScreeningDocument(
                "H", "Skjerming av hele dokumentet");

        // Create a screening object and associate it with the Record
        Screening screening = new Screening();
        screening.setAccessRestriction(accessRestriction);
        screening.setScreeningDocument(screeningDocument);
        screening.setScreeningAuthority("Unntatt etter Offentleglova");
        screening.setScreeningExpiresDate(parse("1942-07-25T00:00:00Z"));
        screening.setScreeningDuration(60);
        record.setReferenceScreening(screening);

        // Create a JSON object to POST
        JsonFactory factory = new JsonFactory();
        StringWriter jsonRecordWriter = new StringWriter();
        JsonGenerator jsonRecord =
                factory.createGenerator(jsonRecordWriter);
        jsonRecord.writeStartObject();
        printTitleAndDescription(jsonRecord, record);
        printRecordEntity(jsonRecord, record);
        printScreening(jsonRecord, record);
        jsonRecord.writeEndObject();
        jsonRecord.close();

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewRecord)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonRecordWriter.toString()));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$." + SYSTEM_ID)
                        .exists())
                .andExpect(jsonPath("$." + TITLE)
                        .value("Title of record"));
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();

        // Make sure we can retrieve the record and that the Screening
        // attributes are present
        String urlRecord = "/noark5v5/api/arkivstruktur/registrering/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlRecord)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        response = resultActions.andReturn().getResponse();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + SCREENING + "." +
                        ACCESS_RESTRICTION +
                        "." +
                        CODE).value("P"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        ACCESS_RESTRICTION + "." +
                        CODE_NAME).value("Personalsaker"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_SCREENING_DOCUMENT + "." +
                        CODE).value("H"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_SCREENING_DOCUMENT + "." +
                        CODE_NAME)
                        .value("Skjerming av hele dokumentet"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_AUTHORITY)
                        .value("Unntatt etter Offentleglova"))
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_DURATION)
                        .value(60))
                // Not picking them explicitly out as [0] [1] objects in the
                // array as the order might change later and then the test
                // will fail unnecessary
                .andExpect(jsonPath("$._links.['" +
                        REL_METADATA_SCREENING_METADATA + "'].href").exists())
                .andExpect(jsonPath("$." + SCREENING + "." +
                        SCREENING_EXPIRES_DATE).value(
                        anyOf(
                                is("1942-07-25T00:00:00Z"),
                                is("1942-07-25T00:00:00+00:00"))))
                .andExpect(jsonPath("$." + TITLE)
                        .value("Title of record"));
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));


        // Next, Create a ScreeningMetadata and POST it
        String urlNewScreeningMetadata = "/noark5v5/api/arkivstruktur/registrering/" +
                JsonPath.read(response.getContentAsString(),
                        "$." + SYSTEM_ID) + "/" + NEW_SCREENING_METADATA + "/";
        // Make a note of the url for ScreeningMetadata associated with the
        // record. Will be used later.
        String urlScreeningMetadata = "/noark5v5/api/arkivstruktur/registrering/" +
                JsonPath.read(response.getContentAsString(),
                        "$." + SYSTEM_ID) + "/" + SCREENING_METADATA + "/";

        ScreeningMetadataLocal screeningMetadata = new ScreeningMetadataLocal();
        screeningMetadata.setCode("NA");
        screeningMetadata.setCodeName("Skjerming navn avsender");

        // Create a JSON object to POST
        StringWriter jsonScreeningMetadataWriter = new StringWriter();
        JsonGenerator jsonScreeningMetadata =
                factory.createGenerator(jsonScreeningMetadataWriter);
        printScreeningMetadata(jsonScreeningMetadata, screeningMetadata);
        jsonScreeningMetadata.close();

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewScreeningMetadata)
                .contextPath("/noark5v5")
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonScreeningMetadataWriter.toString())
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$." + CODE)
                        .exists())
                .andExpect(jsonPath("$." + CODE_NAME)
                        .exists())
                .andExpect(jsonPath("$._links.['" + SELF + "'].href")
                        .exists())
                .andExpect(jsonPath(
                        "$._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA +
                                "'].['" + HREF + "']").exists());
        ;

        // Create another ScreeningMetadata and POST it
        screeningMetadata.setCode("TKL");
        screeningMetadata.setCodeName("Skjerming tittel klasse");

        // Create a JSON object to POST
        jsonScreeningMetadataWriter = new StringWriter();
        jsonScreeningMetadata = factory
                .createGenerator(jsonScreeningMetadataWriter);
        printScreeningMetadata(jsonScreeningMetadata, screeningMetadata);
        jsonScreeningMetadata.close();

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewScreeningMetadata)
                .contextPath("/noark5v5")
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonScreeningMetadataWriter.toString())
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$." + CODE)
                        .exists())
                .andExpect(jsonPath("$." + CODE_NAME)
                        .exists())
                .andExpect(jsonPath("$._links.['" + SELF + "'].href")
                        .exists())
                .andExpect(jsonPath(
                        "$._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA +
                                "'].['" + HREF + "']").exists());
        ;

        // Check that it is possible to retrieve the two ScreeningMetadata
        // objects
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlScreeningMetadata)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        response = resultActions.andReturn().getResponse();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(2)))
                .andExpect(jsonPath("$.results.[0]." + CODE)
                        .exists())
                .andExpect(jsonPath("$.results.[0]." + CODE_NAME)
                        .exists())
                .andExpect(jsonPath("$.results.[1]." + CODE)
                        .exists())
                .andExpect(jsonPath("$.results.[1]." + CODE_NAME)
                        .exists())
                .andExpect(jsonPath("$.results.[0]._links.['" +
                        SELF + "'].href")
                        .exists())
                .andExpect(jsonPath(
                        "$.results.[0]._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA + "'].['" +
                                HREF + "']").exists())
                .andExpect(jsonPath("$.results.[1]._links.['" +
                        SELF + "'].href")
                        .exists())
                .andExpect(jsonPath(
                        "$.results.[1]._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA + "'].['" +
                                HREF + "']").exists());

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        // Next, see that we can update the ScreeningMetadata
        String urlComplete = JsonPath.read(response.getContentAsString(),
                "$.results.[0]._links.self.href");
        urlScreeningMetadata = "/noark5v5/api/arkivstruktur" +
                "/skjermingmetadata" + urlComplete.split("/noark5v5/api" +
                "/arkivstruktur/skjermingmetadata")[1];

        // Create a ScreeningMetadata object different to the previous ones
        // that we can use to override the data in nikita
        ScreeningMetadata screeningMetadata2 = new ScreeningMetadata();
        screeningMetadata2.setCode("TM1");
        screeningMetadata2.setCodeName("Skjerming tittel mappe - unntatt første linje");

        // Create a JSON object to POST
        StringWriter jsonScreeningWriter = new StringWriter();
        JsonGenerator jsonScreening =
                factory.createGenerator(jsonScreeningWriter);
        printNullableMetadata(jsonScreening, SCREENING_SCREENING_DOCUMENT,
                screeningMetadata2, false);
        jsonScreening.close();

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put(urlScreeningMetadata)
                .contextPath("/noark5v5")
                .header("ETAG", "\"0\"")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonScreeningWriter.toString()));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['" + CODE + "']")
                        .value("TM1"))
                .andExpect(jsonPath("$.['" + CODE_NAME + "']")
                        .value("Skjerming tittel mappe - unntatt første linje"))
                .andExpect(jsonPath(
                        "$._links.['" +
                                REL_FONDS_STRUCTURE_SCREENING_METADATA + "'].['" +
                                HREF + "']").exists())
                .andExpect(jsonPath(
                        "$._links.['" + SELF + "'].['" + HREF + "']").exists());

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        // Next, check it is possible to delete the ScreeningMetadata
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlScreeningMetadata)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isNoContent());
    }
}
