package nikita.webapp.general;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.jayway.jsonpath.JsonPath;
import nikita.N5CoreApp;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.metadata.*;
import nikita.common.model.noark5.v5.secondary.Screening;
import nikita.common.model.noark5.v5.secondary.ScreeningMetadataLocal;
import nikita.webapp.spring.TestSecurityConfiguration;
import nikita.webapp.spring.WithMockCustomUser;
import nikita.webapp.spring.security.NikitaUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import static java.time.OffsetDateTime.now;
import static java.time.OffsetDateTime.parse;
import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.HREF;
import static nikita.common.config.HATEOASConstants.SELF;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.DocumentObjectCreator.createDocumentObjectAsJSON;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(classes = N5CoreApp.class,
        webEnvironment = RANDOM_PORT)
@ContextConfiguration(classes = {TestSecurityConfiguration.class})
@ActiveProfiles("test")
@AutoConfigureRestDocs(outputDir = "target/snippets")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class GeneralTest {

    private MockMvc mockMvc;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private NikitaUserDetailsService nikitaUserDetailsService;

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
     * opprettetDato not being serialised to JSON
     * <p>
     * There was a problem where opprettetDato was not being serialised to
     * JSON. This is described in:
     * <p>
     * See https://gitlab.com/OsloMet-ABI/nikita-noark5-core/-/issues/187
     * <p>
     * This was rectified in
     * https://gitlab.com/OsloMet-ABI/nikita-noark5-core/-/commit/aa5c42fa6afe346bb8ac599210f779e664fae3d7
     * <p>
     * Not sure if the test really is required but leaving it in as it has
     * been an undectected problem before
     *
     * @throws Exception Needed for mockMvc.perform
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    public void checkJSONValuesSet() throws Exception {
        String url = "/noark5v5/api/arkivstruktur/arkiv" +
                "/3318a63f-11a7-4ec9-8bf1-4144b7f281cf";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .with(user(nikitaUserDetailsService
                        .loadUserByUsername("admin@example.com"))));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + SYSTEM_ID)
                        .exists())
                .andExpect(jsonPath("$." + CREATED_DATE)
                        .exists())
                .andExpect(jsonPath("$." + CREATED_BY)
                        .exists())
                .andExpect(jsonPath("$." + TITLE)
                        .value("test title alpha"));
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    /**
     * There was a problem with MetadataService and the way metadata entities
     * are implemented. An earlier commit tidied up in the metadata code in
     * an attempt to reduce duplicated code. All the metadata classes were
     * practically the same code. It made sense then to create a base class
     * called Metadata and allow all the other metadata classes extend this
     * base class.
     * <p>
     * Issue 184 (https://gitlab.com/OsloMet-ABI/nikita-noark5-core/-/issues/184)
     * reports that this approach is not working correctly as an exception is
     * thrown when there is an attempt to persist two metadata entities
     * during a single session.
     * The problem was with the inheritance strategy. The test is being left
     * in case it sneaks in again later.
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    @WithMockCustomUser
    public void checkMultipleMetadataEntityPossible() throws Exception {
        DocumentDescription documentDescription = new DocumentDescription();
        documentDescription.setTitle("Test title");

        AssociatedWithRecordAs associatedWithRecordAs =
                new AssociatedWithRecordAs();
        associatedWithRecordAs.setCode("H");
        associatedWithRecordAs.setCodeName("Hoveddokument");
        documentDescription.setAssociatedWithRecordAs(associatedWithRecordAs);

        DocumentType documentType = new DocumentType();
        documentType.setCode("B");
        documentType.setCodeName("Brev");
        documentDescription.setDocumentType(documentType);

        DocumentStatus documentStatus = new DocumentStatus();
        documentStatus.setCode("B");
        documentStatus.setCodeName("Dokumentet er under redigering");
        documentDescription.setDocumentStatus(documentStatus);

        String url = "/noark5v5/api/arkivstruktur/registrering" +
                "/dc600862-3298-4ec0-8541-3e51fb900054/ny-dokumentbeskrivelse";

        JsonFactory factory = new JsonFactory();
        StringWriter jsonWriter = new StringWriter();
        JsonGenerator jgen = factory.createGenerator(jsonWriter);
        jgen.writeStartObject();
        printSystemIdEntity(jgen, documentDescription);
        printTitleAndDescription(jgen, documentDescription);
        printNullableMetadata(jgen,
                DOCUMENT_DESCRIPTION_DOCUMENT_TYPE,
                documentDescription.getDocumentType());
        printNullableMetadata(jgen,
                DOCUMENT_DESCRIPTION_STATUS,
                documentDescription.getDocumentStatus());
        printNullable(jgen, DOCUMENT_DESCRIPTION_DOCUMENT_NUMBER,
                documentDescription.getDocumentNumber());
        printNullableMetadata(jgen,
                DOCUMENT_DESCRIPTION_ASSOCIATED_WITH_RECORD_AS,
                documentDescription.getAssociatedWithRecordAs());
        jgen.writeEndObject();
        jgen.close();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonWriter.toString()));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$." + SYSTEM_ID)
                        .exists())
                .andExpect(jsonPath("$." + TITLE)
                        .value("Test title"));
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    /**
     * Check that it is possible to add a Screening to a File when creating
     * the File. Then check that it is possible to associate
     * ScreeningMetadata with file.
     * <p>
     * The following steps are taken:
     * 1. Get File template (GET ny-mappe)
     * 2. Create file (POST ny-mappe)
     * 3. Get created file (GET mappe) and check Screeing attributes and
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
     * nikita was returning the following JSON for en empty list:
     * <p>
     * {
     * "entityType":"unknown",
     * "selfLinks":[],
     * "singleEntity":false,
     * "list":[],
     * "entityVersion":-1
     * }
     * <p>
     * This is because there was a missing serialiser for empty list objets.
     * The actual JSON response for an empty list should, as per the
     * standard, be:
     * <p>
     * {
     * "count": 0,
     * "_links" : {
     * "self": {
     * "href": "https://n5.example.com/api/arkivstruktur/arkiv/"
     * },
     * "https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkiv/": {
     * "href": "https://n5.example.com/api/arkivstruktur/arkiv/$filter=tittel%20eq%20'Generate%20an%20empty%20list%20for%20this%20test'"
     * }
     * }
     * }
     * <p>
     * See also:
     * https://github.com/arkivverket/noark5-tjenestegrensesnitt-standard/blob/master/kapitler/06-konsepter_og_prinsipper.rst
     * https://gitlab.com/OsloMet-ABI/nikita-noark5-core/-/issues/152
     *
     * @throws Exception Needed for mockMvc.perform
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    @WithMockCustomUser
    public void checkODataSearchHasCorrectResponse() throws Exception {

        String url = "/noark5v5/odata/api/arkivstruktur/arkiv?$filter=" +
                "tittel eq 'Generate an empty list for this test'";

        String expectedUrl = "http://localhost:8092/noark5v5/odata/api" +
                "/arkivstruktur/arkiv?$filter=tittel%20eq%20'Generate%20an" +
                "%20empty%20list%20for%20this%20test'";

        String expectedRel = NOARK_BASE_REL + "arkivstruktur/arkiv/";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + ENTITY_ROOT_NAME_LIST_COUNT)
                        .value(0))
                .andExpect(jsonPath(
                        "$._links.['" + SELF + "'].['" + HREF + "']")
                        .value(expectedUrl))
                .andExpect(jsonPath(
                        "$._links.['" + expectedRel +
                                "'].['" + HREF + "']").value(expectedUrl));
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }


    /**
     * We are seeing a problem with an OData search for opprettetAv (createdBy)
     * that is throwing an exception. This test is to see if we can reproduce
     * the issue.
     * <p>
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    @WithMockCustomUser
    public void checkODataSearchForCreatedBy() throws Exception {
        String url = "/noark5v5/odata/api/arkivstruktur/dokumentbeskrivelse" +
                "?$filter=" + CREATED_BY + " eq 'admin@example.com'";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0]." + SYSTEM_ID)
                        .value("66b92e78-b75d-4b0f-9558-4204ab31c2d1"))
                .andExpect(jsonPath("$.results[0]." + TITLE)
                        .value("test title bravo"));
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    /**
     * Test that it is possible to upload a document and that nikita converts
     * it to an archive format (PDF)
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    @WithMockCustomUser
    public void checkPossibleToConvertDocumentToPDF() throws Exception {

        String url = "/noark5v5/api/arkivstruktur/dokumentbeskrivelse" +
                "/66b92e78-b75d-4b0f-9558-4204ab31c2d1/ny-dokumentobjekt";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createDocumentObjectAsJSON()));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$." + SYSTEM_ID)
                        .exists());
        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        String uuid = JsonPath.read(response.getContentAsString(),
                "$." + SYSTEM_ID);

        url = "/noark5v5/api/arkivstruktur/dokumentobjekt/" + uuid +
                "/referanseFil";

        PathMatchingResourcePatternResolver resolver =
                new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource(
                "classpath:test_document.odt");

        byte[] content;
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream
                (String.valueOf(resource.getFile().toPath())))) {
            content = inputStream.readAllBytes();
        }
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType("application/vnd.oasis.opendocument.text")
                .content(content));
        response = resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());

        // Note change of URL to get direct access to OData endpoint
        // Remember nikita redirects all odata queries to this endpoint
        url = "/noark5v5/odata/api/arkivstruktur/dokumentobjekt" +
                "?$filter=variantformat/kode eq 'A'";

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        response = resultActions.andReturn().getResponse();
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        System.out.println(response.getContentAsString());
        uuid = JsonPath.read(response.getContentAsString(),
                "$.results[0]." + SYSTEM_ID);

        url = "/noark5v5/api/arkivstruktur/dokumentobjekt/" +
                uuid + "/konvertering";

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        response = resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }


    /**
     * Test that it is possible to expand a File to a CaseFile
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    @WithMockCustomUser
    public void checkPossibleToExpandFileToCaseFile() throws Exception {
        // First create / POST file
        File file = new File();
        file.setTitle("Title of file");
        String url = "/noark5v5/api/arkivstruktur/arkivdel" +
                "/f1102ae8-6c4c-4d93-aaa5-7c6220e50c4d/ny-mappe";

        JsonFactory factory = new JsonFactory();
        StringWriter jsonFileWriter = new StringWriter();
        JsonGenerator jsonFile = factory.createGenerator(jsonFileWriter);
        jsonFile.writeStartObject();
        printFileEntity(jsonFile, file);
        jsonFile.writeEndObject();
        jsonFile.close();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
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

        // Next see if we can get this File
        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        url = "/noark5v5/api/arkivstruktur/mappe/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        // Next see if we can expand this File to a CaseFile
        response = resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());
        url = "/noark5v5/api/arkivstruktur/mappe/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID) +
                "/" + FILE_EXPAND_TO_CASE_FILE;

        // First get template of object for patch

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonFileWriter.toString()));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + CASE_STATUS + "." + CODE)
                        .value(TEMPLATE_CASE_STATUS_CODE))
                .andExpect(jsonPath("$." + CASE_STATUS + "." + CODE_NAME)
                        .value(TEMPLATE_CASE_STATUS_NAME))
                .andExpect(jsonPath(
                        "$._links.['" + REL_METADATA_CASE_STATUS
                                + "']['" + HREF + "']",
                        endsWith(NOARK_METADATA_PATH + SLASH +
                                CASE_STATUS + SLASH)));

        response = resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());

        String caseStatusCode = JsonPath.read(response.getContentAsString(),
                "$." + CASE_STATUS + "." + CODE);
        String caseStatusCodeName = JsonPath.read(response.getContentAsString(),
                "$." + CASE_STATUS + "." + CODE_NAME);
        String caseResponsible = JsonPath.read(response.getContentAsString(),
                "$." + CASE_RESPONSIBLE);
        String caseDate = JsonPath.read(response.getContentAsString(),
                "$." + CASE_DATE);


        // Then create a PATCH MERGE object
        StringWriter jsonPatchWriter = new StringWriter();
        JsonGenerator jsonPatch = factory.createGenerator(jsonPatchWriter);
        jsonPatch.writeStartObject();
        jsonPatch.writeObjectFieldStart(CASE_STATUS);
        jsonPatch.writeStringField(CODE, caseStatusCode);
        jsonPatch.writeStringField(CODE_NAME, caseStatusCodeName);
        jsonPatch.writeEndObject();
        jsonPatch.writeStringField(CASE_RESPONSIBLE, caseResponsible);
        jsonPatch.writeStringField(CASE_DATE, caseDate);
        jsonPatch.writeEndObject();
        jsonPatch.close();

        System.out.println(jsonPatchWriter);

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .patch(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonPatchWriter.toString()));

        response = resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + SYSTEM_ID)
                        .exists())
                .andExpect(jsonPath(
                        "$._links.['" + SELF + "']").exists())
                .andExpect(jsonPath(
                        "$._links.['" + REL_CASE_HANDLING_CASE_FILE + "']")
                        .exists())
                .andExpect(jsonPath(
                        "$._links.['" + REL_METADATA_CASE_STATUS
                                + "']['" + HREF + "']",
                        endsWith(NOARK_METADATA_PATH + SLASH +
                                CASE_STATUS + SLASH)));
    }

    /**
     * Test that it is possible to create sub CaseFile in a CaseFile
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    @WithMockCustomUser
    public void checkPossibleToMakeCaseFileSubCaseFile() throws Exception {
        // First create / POST file
        CaseFile subCaseFile = new CaseFile();
        subCaseFile.setTitle("Title of sub casefile");
        CaseStatus caseStatus = new CaseStatus(TEMPLATE_CASE_STATUS_CODE,
                TEMPLATE_CASE_STATUS_NAME);
        subCaseFile.setCaseStatus(caseStatus);
        subCaseFile.setCaseResponsible("admin@example.com");
        subCaseFile.setCaseDate(now());
        String url = "/noark5v5/api/sakarkiv/saksmappe" +
                "/fed888c6-83e1-4ed0-922a-bd5770af3fad/ny-saksmappe";

        JsonFactory factory = new JsonFactory();
        StringWriter jsonFileWriter = new StringWriter();
        JsonGenerator jsonFile = factory.createGenerator(jsonFileWriter);
        jsonFile.writeStartObject();
        printFileEntity(jsonFile, subCaseFile);
        printCaseFileEntity(jsonFile, subCaseFile);
        jsonFile.writeEndObject();
        jsonFile.close();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonFileWriter.toString()));

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$." + CASE_STATUS + "." + CODE)
                        .value(TEMPLATE_CASE_STATUS_CODE))
                .andExpect(jsonPath("$." + CASE_STATUS + "." + CODE_NAME)
                        .value(TEMPLATE_CASE_STATUS_NAME))
                .andExpect(jsonPath("$." + SYSTEM_ID)
                        .exists())
                .andExpect(jsonPath("$." + CREATED_DATE)
                        .exists())
                .andExpect(jsonPath("$." + CREATED_BY)
                        .exists())
                .andExpect(jsonPath("$." + TITLE)
                        .value("Title of sub casefile"))
                .andExpect(jsonPath(
                        "$._links.['" + REL_METADATA_CASE_STATUS
                                + "']['" + HREF + "']",
                        endsWith(NOARK_METADATA_PATH + SLASH +
                                CASE_STATUS + SLASH)));

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        // Next see if we can get this File
        response =
                resultActions.andReturn().getResponse();
        url = "/noark5v5/api/arkivstruktur/mappe/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        // Next see if we can expand this File to a CaseFile
        response = resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());
    }

    /**
     * Test that it is possible to move a File from one Series to another
     * mappe: f1677c47-99e1-42a7-bda2-b0bbc64841b7
     * from: f1102ae8-6c4c-4d93-aaa5-7c6220e50c4d
     * to: f32c1fa0-8e42-4236-8f40-e006940ea70b
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    @WithMockCustomUser
    public void checkMoveFileToAnotherSeries() throws Exception {

        String what = "f1677c47-99e1-42a7-bda2-b0bbc64841b7";
        String fromSeries = "f1102ae8-6c4c-4d93-aaa5-7c6220e50c4d";
        String toSeries = "f32c1fa0-8e42-4236-8f40-e006940ea70b";

        String url = "/noark5v5/api/arkivstruktur/mappe/" + what;

        JsonFactory factory = new JsonFactory();
        StringWriter jsonPatchWriter = new StringWriter();
        JsonGenerator jsonPatch = factory.createGenerator(jsonPatchWriter);
        jsonPatch.writeStartObject();
        jsonPatch.writeStringField("op", "move");
        jsonPatch.writeStringField("from", fromSeries);
        jsonPatch.writeStringField("path", toSeries);
        jsonPatch.writeEndObject();
        jsonPatch.close();
        System.out.println(jsonPatchWriter);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .patch(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonPatchWriter.toString()));

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + SYSTEM_ID)
                        .value(what))
                .andExpect(jsonPath("$._links.['"
                                + REL_FONDS_STRUCTURE_SERIES + "']." + HREF,
                        endsWith(toSeries)));
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        // Make sure the File has updated values
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + SYSTEM_ID)
                        .value(what))
                .andExpect(jsonPath("$._links.['"
                                + REL_FONDS_STRUCTURE_SERIES + "']." + HREF,
                        endsWith(toSeries)));
        response = resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());
    }
}
