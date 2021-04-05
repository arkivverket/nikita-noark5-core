package nikita.webapp.bsm;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.jayway.jsonpath.JsonPath;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.bsm.BSM;
import nikita.common.model.noark5.bsm.BSMBase;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson;
import nikita.common.model.noark5.v5.metadata.PartRole;
import nikita.common.model.noark5.v5.secondary.PartPerson;
import nikita.webapp.spring.SpringSecurityWebAuxTestConfig;
import nikita.webapp.spring.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.deserializeDate;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.deserializeDateTime;
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
import static org.yaml.snakeyaml.util.UriEncoder.encode;
import static utils.CorrespondencePartCreator.*;
import static utils.CorrespondencePartValidator.*;
import static utils.TestConstants.*;

/**
 * Things to develop
 * 1) Check that when adding a VSM with PATCH, that the VSM attribute does not
 * already exist
 * 2) Make sure it is possible to search
 * 3) Add a PatchTest that checks for patching
 * <p>
 * <p>
 * Test used to show support for businessSpecificMetadata
 * (virksomhetsspesifikkeMetadata). The following is tested here:
 * addBSMToFileUpdate (BSM added after file created - PATCH)
 * addBSMToFileCreate (BSM added when file created - POST)
 * addBSMToRecordUpdate (BSM added after record created - PATCH)
 * addBSMToRecordCreate (BSM added when record created - POST)
 * addBSMToPartUpdate (BSM added after part created - PATCH)
 * addBSMToPartCreate (BSM added when part created - POST)
 * addBSMToCorrespondencePartUpdate (BSM added after correspondencePart created - PATCH)
 * addBSMToCorrespondencePartCreate (BSM added when correspondencePart created - POST)
 * addBSMToAdministrativeUnitUpdate (BSM added after administrativeUnit created - PATCH)
 * addBSMToAdministrativeUnitCreate (BSM added when administrativeUnit created - POST)
 * <p>
 * is setReferenceBSMBase being called multiple times
 */
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(classes = {SpringSecurityWebAuxTestConfig.class},
        webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class BSMTest {

    // Values used in multiple tests
    private final List<BSMBase> bsmObjects = new ArrayList<>();

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        createBSMObjects();
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
     * Register Metadata object: The following is an example of the type of
     * object to create:
     * "navn": "ppt-v1:skoleaar",
     * "type": "string",
     * "utdatert": true,
     * "beskrivelse": "Hvilket skoleår saken gjelder",
     * "kilde": "https://some/where/with/explanation"
     * <p>
     * The following attributes are added:
     * "ppt-v1:sakferdig": boolean,
     * "ppt-v1:datohenvist": date,
     * "ppt-v1:datotidvedtakferdig": datetime,
     * "ppt-v1:skolekontakt": String,
     * "ppt-v1:refSkole": URI,
     * "ppt-v1:snittKarakter": double,
     * "ppt-v1:antallDagerVurdert": integer
     *
     * @throws IOException exception if required
     */
    @Test
    @WithMockCustomUser
    public void registerMetadataBSMCheckTheyCanBeRetrieved() throws Exception {
        HashMap<String, List<String>> mdValues = new LinkedHashMap<>();
        mdValues.put("ppt-v1:skoleaar",
                new ArrayList<>(Arrays.asList(TYPE_STRING,
                        DESCRIPTION_TEST, BSM_URI_VALUE)));
        mdValues.put(BSM_BOOLEAN_NAME, new ArrayList<>(Arrays.asList(TYPE_BOOLEAN,
                DESCRIPTION_TEST, BSM_URI_VALUE)));
        mdValues.put(BSM_DATE_NAME,
                new ArrayList<>(Arrays.asList(TYPE_DATE,
                        DESCRIPTION_TEST, BSM_URI_VALUE)));
        mdValues.put(BSM_DATE_TIME_NAME,
                new ArrayList<>(Arrays.asList(TYPE_DATE_TIME,
                        DESCRIPTION_TEST, BSM_URI_VALUE)));
        mdValues.put(BSM_STRING_NAME, new ArrayList<>(Arrays.asList(TYPE_STRING,
                DESCRIPTION_TEST, BSM_URI_VALUE)));
        mdValues.put(BSM_URI_NAME, new ArrayList<>(Arrays.asList(TYPE_URI,
                DESCRIPTION_TEST, BSM_URI_VALUE)));
        mdValues.put(BSM_DOUBLE_NAME, new ArrayList<>(Arrays.asList(TYPE_DOUBLE,
                DESCRIPTION_TEST, BSM_URI_VALUE)));
        mdValues.put(BSM_INTEGER_NAME, new ArrayList<>(Arrays.asList(TYPE_INTEGER,
                DESCRIPTION_TEST, BSM_URI_VALUE)));

        String url = "/noark5v5/api/metadata/" + NEW_BSM_DEF;
        JsonFactory factory = new JsonFactory();
        String uuidCreatedBSMMetadata = "";
        for (Map.Entry<String, List<String>> entry : mdValues.entrySet()) {
            List values = entry.getValue();
            System.out.println(createBSMMetadata(entry.getKey(),
                    (String) values.get(0), false,
                    (String) values.get(1), (String) values.get(2),
                    factory).toString());
            ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                    .post(url)
                    .contextPath("/noark5v5")
                    .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                    .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                    .content(createBSMMetadata(entry.getKey(),
                            (String) values.get(0), false,
                            (String) values.get(1), (String) values.get(2),
                            factory).toString()));
            resultActions.andExpect(status().isCreated());
            MockHttpServletResponse response = resultActions.andReturn()
                    .getResponse();
            System.out.println(response.getContentAsString());

            validateBSMMetadata(entry.getKey(),
                    (String) values.get(0), false, (String) values.get(1),
                    (String) values.get(2), resultActions);
            response = resultActions.andReturn()
                    .getResponse();

            if ("ppt-v1:skolekontakt".equals(entry.getKey())) {
                uuidCreatedBSMMetadata = JsonPath.read(response.getContentAsString(),
                        "$." + SYSTEM_ID);
            }

            values.add("/noark5v5/api/metadata/" + BSM_DEF +
                    SLASH + JsonPath.read(response.getContentAsString(),
                    "$." + SYSTEM_ID));
            resultActions.andDo(document(
                    "{method-name}" + values.get(0),
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint())));
        }

        for (Map.Entry<String, List<String>> entry : mdValues.entrySet()) {
            List values = entry.getValue();
            url = (String) values.get(3);
            ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                    .get(url)
                    .contextPath("/noark5v5")
                    .accept(NOARK5_V5_CONTENT_TYPE_JSON));
            resultActions.andExpect(status().isOk());
            validateBSMMetadata(entry.getKey(),
                    (String) values.get(0), false, (String) values.get(1),
                    (String) values.get(2), resultActions);
        }

        url = "/noark5v5/api/metadata/" + BSM_DEF +
                "/" + uuidCreatedBSMMetadata;
        StringWriter jsonPatchWriter = new StringWriter();
        JsonGenerator jsonPatch = factory.createGenerator(jsonPatchWriter);
        jsonPatch.writeStartObject();
        jsonPatch.writeStringField(DESCRIPTION, "UPDATED DESCRIPTION");
        jsonPatch.writeStringField(SOURCE, "UPDATED SOURCE");
        jsonPatch.writeEndObject();
        jsonPatch.close();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .patch(url)
                .contextPath("/noark5v5")
                .header("ETAG", "\"0\"")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(CONTENT_TYPE_JSON_MERGE_PATCH)
                .content(jsonPatchWriter.toString()));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + DESCRIPTION)
                        .value("UPDATED DESCRIPTION"))
                .andExpect(jsonPath("$." + SOURCE)
                        .value("UPDATED SOURCE"))
                .andExpect(jsonPath("$._links.self")
                        .exists())
                .andExpect(jsonPath(
                        "$._links.['" + REL_METADATA_BSM + "']")
                        .exists());

        resultActions.andDo(document(
                "{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + DESCRIPTION)
                        .value("UPDATED DESCRIPTION"))
                .andExpect(jsonPath("$." + SOURCE)
                        .value("UPDATED SOURCE"))
                .andExpect(jsonPath("$._links.self")
                        .exists())
                .andExpect(jsonPath(
                        "$._links.['" + REL_METADATA_BSM + "']")
                        .exists());

        url = "/noark5v5/api/metadata/" + BSM_DEF;
        // Check the list can be retrieved
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(8)));
    }

    /**
     * Test that it is not possible to add BSM unless it is registered
     * {
     * "op": "add",
     * "path": ".../registrering/systemID",
     * "value": {
     * "virksomhetsspesifikkeMetadata": {
     * "non-existent:attribute": true,
     * }
     * }
     * }
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @WithMockCustomUser
    public void addNonregisteredBSM() throws Exception {
        String url = "/noark5v5/api/arkivstruktur/registrering/" +
                "dc600862-3298-4ec0-8541-3e51fb900054";
        String selfHref = "http://localhost:8092" + url;

        BSM bsm = new BSM();
        BSMBase bsmBoolean = new BSMBase("non-existent:attribute", true);
        ArrayList<BSMBase> badBSM = new ArrayList<>();
        badBSM.add(bsmBoolean);
        bsm.setReferenceBSMBase(badBSM);
        JsonFactory factory = new JsonFactory();
        StringWriter jsonPatchWriter = new StringWriter();
        JsonGenerator jsonPatch = factory.createGenerator(jsonPatchWriter);
        jsonPatch.writeStartObject();
        jsonPatch.writeStringField("op", "add");
        jsonPatch.writeStringField("path", selfHref);
        jsonPatch.writeObjectFieldStart("value");
        printBSM(jsonPatch, bsm);
        jsonPatch.writeEndObject();
        jsonPatch.writeEndObject();
        jsonPatch.close();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .patch(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonPatchWriter.toString()));

        resultActions.andExpect(status().isBadRequest());
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    /**
     * Update record with PATCH request
     * {
     * "op": "add",
     * "path": ".../registrering/systemID",
     * "value": {
     * "virksomhetsspesifikkeMetadata": {
     * "ppt-v1:sakferdig": true,
     * "ppt-v1:datohenvist": "2020-06-30+02:00",
     * "ppt-v1:datotidvedtakferdig": "2020-06-30T15:35:43.128158+02:00",
     * "ppt-v1:skolekontakt": "Harald Harfarge",
     * "ppt-v1:refSkole": "https://skole.eksempel.com",
     * "ppt-v1:snittKarakter": 1.2,
     * "ppt-v1:antallDagerVurdert": 1
     * }
     * }
     * }
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/bsm/registered_bsm_values.sql"})
    @WithMockCustomUser
    public void addBSMToExistingRecord() throws Exception {
        String url = "/noark5v5/api/arkivstruktur/registrering/" +
                "dc600862-3298-4ec0-8541-3e51fb900054";
        String selfHref = "http://localhost:8092" + url;

        BSM bsm = new BSM();
        bsm.setReferenceBSMBase(bsmObjects);
        JsonFactory factory = new JsonFactory();
        StringWriter jsonPatchWriter = new StringWriter();
        JsonGenerator jsonPatch = factory.createGenerator(jsonPatchWriter);
        jsonPatch.writeStartObject();
        jsonPatch.writeStringField("op", "add");
        jsonPatch.writeStringField("path", selfHref);
        jsonPatch.writeObjectFieldStart("value");
        printBSM(jsonPatch, bsm);
        jsonPatch.writeEndObject();
        jsonPatch.writeEndObject();
        jsonPatch.close();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .patch(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonPatchWriter.toString()));

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());
        resultActions.andExpect(status().isOk());
        validateBSM(resultActions);
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    /**
     * add BSM when creating a new record
     * {
     * "virksomhetsspesifikkeMetadata": {
     * "ppt-v1:sakferdig": true,
     * "ppt-v1:datohenvist": "2020-06-30+02:00",
     * "ppt-v1:datotidvedtakferdig": "2020-06-30T15:35:43.128158+02:00",
     * "ppt-v1:skolekontakt": "Harald Harfarge",
     * "ppt-v1:refSkole": "https://skole.eksempel.com",
     * "ppt-v1:snittKarakter": 1.2,
     * "ppt-v1:antallDagerVurdert": 1
     * }
     * }
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    @WithMockCustomUser
    public void addBSMWithNewRecord() throws Exception {
        Record record = new Record();
        record.setReferenceBSMBase(bsmObjects);
        record.setTitle("Title of record");
        String url = "/noark5v5/api/arkivstruktur/mappe" +
                "/f1677c47-99e1-42a7-bda2-b0bbc64841b7/ny-registrering";

        JsonFactory factory = new JsonFactory();
        StringWriter jsonRecordWriter = new StringWriter();
        JsonGenerator jsonRecord = factory.createGenerator(jsonRecordWriter);
        jsonRecord.writeStartObject();
        printRecordEntity(jsonRecord, record);
        printBSM(jsonRecord, record);
        jsonRecord.writeEndObject();
        jsonRecord.close();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
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
        validateBSM(resultActions);
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        // We checked it was possible to create, now check that what we
        // created is possible to retrieve
        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        url = "/noark5v5/api/arkivstruktur/registrering/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + SYSTEM_ID)
                        .exists());
        validateBSM(resultActions);
    }

    /**
     * Update file with PATCH request
     * {
     * "op": "add",
     * "path": ".../mappe/systemID",
     * "value": {
     * "virksomhetsspesifikkeMetadata": {
     * "ppt-v1:sakferdig": true,
     * "ppt-v1:datohenvist": "2020-06-30+02:00",
     * "ppt-v1:datotidvedtakferdig": "2020-06-30T15:35:43.128158+02:00",
     * "ppt-v1:skolekontakt": "Harald Harfarge",
     * "ppt-v1:refSkole": "https://skole.eksempel.com",
     * "ppt-v1:snittKarakter": 1.2,
     * "ppt-v1:antallDagerVurdert": 1
     * }
     * }
     * }
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql",
            "/db-tests/bsm/registered_bsm_values.sql"})
    @WithMockCustomUser
    public void addBSMToExistingFile() throws Exception {
        String url = "/noark5v5/api/arkivstruktur/mappe/" +
                "f1677c47-99e1-42a7-bda2-b0bbc64841b7";
        String selfHref = "http://localhost:8092" + url;

        BSM bsm = new BSM();
        bsm.setReferenceBSMBase(bsmObjects);
        JsonFactory factory = new JsonFactory();
        StringWriter jsonPatchWriter = new StringWriter();
        JsonGenerator jsonPatch = factory.createGenerator(jsonPatchWriter);
        jsonPatch.writeStartObject();
        jsonPatch.writeStringField("op", "add");
        jsonPatch.writeStringField("path", selfHref);
        jsonPatch.writeObjectFieldStart("value");
        printBSM(jsonPatch, bsm);
        jsonPatch.writeEndObject();
        jsonPatch.writeEndObject();
        jsonPatch.close();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .patch(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonPatchWriter.toString()));

        resultActions.andExpect(status().isOk());
        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());

        validateBSM(resultActions);
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    /**
     * add BSM when creating a new file
     * {
     * "virksomhetsspesifikkeMetadata": {
     * "ppt-v1:sakferdig": true,
     * "ppt-v1:datohenvist": "2020-06-30+02:00",
     * "ppt-v1:datotidvedtakferdig": "2020-06-30T15:35:43.128158+02:00",
     * "ppt-v1:skolekontakt": "Harald Harfarge",
     * "ppt-v1:refSkole": "https://skole.eksempel.com",
     * "ppt-v1:snittKarakter": 1.2,
     * "ppt-v1:antallDagerVurdert": 1
     * }
     * }
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    @WithMockCustomUser
    public void addBSMWithNewFile() throws Exception {
        File file = new File();
        file.setReferenceBSMBase(bsmObjects);
        file.setTitle("Title of file");
        String url = "/noark5v5/api/arkivstruktur/arkivdel" +
                "/f1102ae8-6c4c-4d93-aaa5-7c6220e50c4d/ny-mappe";

        JsonFactory factory = new JsonFactory();
        StringWriter jsonFileWriter = new StringWriter();
        JsonGenerator jsonFile = factory.createGenerator(jsonFileWriter);
        jsonFile.writeStartObject();
        printFileEntity(jsonFile, file);
        printBSM(jsonFile, file);
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
        validateBSM(resultActions);
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        // We checked it was possible to create, now check that what we
        // created is possible to retrieve
        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        url = "/noark5v5/api/arkivstruktur/mappe/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + SYSTEM_ID)
                        .exists());
        validateBSM(resultActions);
    }

    /**
     * Update correspondencePart with PATCH request
     * {
     * "op": "add",
     * "path": ".../korrespondansepart/systemID",
     * "value": {
     * "virksomhetsspesifikkeMetadata": {
     * "ppt-v1:sakferdig": true,
     * "ppt-v1:datohenvist": "2020-06-30+02:00",
     * "ppt-v1:datotidvedtakferdig": "2020-06-30T15:35:43.128158+02:00",
     * "ppt-v1:skolekontakt": "Harald Harfarge",
     * "ppt-v1:refSkole": "https://skole.eksempel.com",
     * "ppt-v1:snittKarakter": 1.2,
     * "ppt-v1:antallDagerVurdert": 1
     * }
     * }
     * }
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql",
            "/db-tests/bsm/registered_bsm_values.sql"})
    @WithMockCustomUser
    public void addBSMToExistingCorrespondencePart() throws Exception {
        String url = "/noark5v5/api/arkivstruktur/korrespondansepartperson/" +
                "7f000101-730c-1c94-8173-0c0ded71003c";
        String selfHref = "http://localhost:8092" + url;

        BSM bsm = new BSM();
        bsm.setReferenceBSMBase(bsmObjects);
        JsonFactory factory = new JsonFactory();
        StringWriter jsonPatchWriter = new StringWriter();
        JsonGenerator jsonPatch = factory.createGenerator(jsonPatchWriter);
        jsonPatch.writeStartObject();
        jsonPatch.writeStringField("op", "add");
        jsonPatch.writeStringField("path", selfHref);
        jsonPatch.writeObjectFieldStart("value");
        printBSM(jsonPatch, bsm);
        jsonPatch.writeEndObject();
        jsonPatch.writeEndObject();
        jsonPatch.close();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .patch(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonPatchWriter.toString()));

        resultActions.andExpect(status().isOk());
        validateBSM(resultActions);
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        MockHttpServletResponse response = resultActions.andReturn()
                .getResponse();
        url = "/noark5v5/api/arkivstruktur/korrespondansepartperson/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        // We checked it was possible to create, now check that what we
        // created is possible to retrieve
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + SYSTEM_ID)
                        .exists());

        validateBSM(resultActions);
    }

    /**
     * add BSM when creating a new correspondencePart
     * {
     * "virksomhetsspesifikkeMetadata": {
     * "ppt-v1:sakferdig": true,
     * "ppt-v1:datohenvist": "2020-06-30+02:00",
     * "ppt-v1:datotidvedtakferdig": "2020-06-30T15:35:43.128158+02:00",
     * "ppt-v1:skolekontakt": "Harald Harfarge",
     * "ppt-v1:refSkole": "https://skole.eksempel.com",
     * "ppt-v1:snittKarakter": 1.2,
     * "ppt-v1:antallDagerVurdert": 1
     * }
     * }
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    @WithMockCustomUser
    public void addBSMWithNewCorrespondencePart() throws Exception {
        CorrespondencePartPerson correspondencePart =
                createCorrespondencePartPerson();
        correspondencePart.setReferenceBSMBase(bsmObjects);

        String url = "/noark5v5/api/arkivstruktur/registrering" +
                "/dc600862-3298-4ec0-8541-3e51fb900054/ny-korrespondansepartperson";

        JsonFactory factory = new JsonFactory();
        StringWriter jsonCorrespondencePartWriter = new StringWriter();
        JsonGenerator jsonCorrespondencePart = factory.createGenerator(jsonCorrespondencePartWriter);
        jsonCorrespondencePart.writeStartObject();
        printCorrespondencePartPerson(jsonCorrespondencePart,
                correspondencePart);
        printBSM(jsonCorrespondencePart, correspondencePart);
        jsonCorrespondencePart.writeEndObject();
        jsonCorrespondencePart.close();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonCorrespondencePartWriter.toString()));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$." + SYSTEM_ID)
                        .exists());

        validateCorrespondencePartPerson(resultActions);

        resultActions.andExpect(jsonPath("$." + N5ResourceMappings.NAME)
                .value(NAME_TEST));

        validateBSM(resultActions);

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        MockHttpServletResponse response = resultActions.andReturn()
                .getResponse();
        url = "/noark5v5/api/arkivstruktur/korrespondansepartperson/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        // We checked it was possible to create, now check that what we
        // created is possible to retrieve
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + SYSTEM_ID)
                        .exists());

        validateCorrespondencePartPerson(resultActions);
        resultActions.andExpect(jsonPath("$." + N5ResourceMappings.NAME)
                .value(NAME_TEST));
        validateBSM(resultActions);
    }

    /**
     * Update part with PATCH request
     * {
     * "op": "add",
     * "path": ".../part/systemID",
     * "value": {
     * "virksomhetsspesifikkeMetadata": {
     * "ppt-v1:sakferdig": true,
     * "ppt-v1:datohenvist": "2020-06-30+02:00",
     * "ppt-v1:datotidvedtakferdig": "2020-06-30T15:35:43.128158+02:00",
     * "ppt-v1:skolekontakt": "Harald Harfarge",
     * "ppt-v1:refSkole": "https://skole.eksempel.com",
     * "ppt-v1:snittKarakter": 1.2,
     * "ppt-v1:antallDagerVurdert": 1
     * }
     * }
     * }
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql",
            "/db-tests/bsm/registered_bsm_values.sql"})
    @WithMockCustomUser
    public void addBSMToExistingPart() throws Exception {
        String url = "/noark5v5/api/arkivstruktur/partperson/" +
                "8131049d-dcac-43d8-bee4-656e72842da9";
        String selfHref = "http://localhost:8092" + url;

        BSM bsm = new BSM();
        bsm.setReferenceBSMBase(bsmObjects);
        JsonFactory factory = new JsonFactory();
        StringWriter jsonPatchWriter = new StringWriter();
        JsonGenerator jsonPatch = factory.createGenerator(jsonPatchWriter);
        jsonPatch.writeStartObject();
        jsonPatch.writeStringField("op", "add");
        jsonPatch.writeStringField("path", selfHref);
        jsonPatch.writeObjectFieldStart("value");
        printBSM(jsonPatch, bsm);
        jsonPatch.writeEndObject();
        jsonPatch.writeEndObject();
        jsonPatch.close();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .patch(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonPatchWriter.toString()));

        MockHttpServletResponse response = resultActions.andReturn()
                .getResponse();

        resultActions.andExpect(status().isOk());
        validateBSM(resultActions);
        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        url = "/noark5v5/api/arkivstruktur/partperson/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        // We checked it was possible to create, now check that what we
        // created is possible to retrieve
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + SYSTEM_ID)
                        .exists());
        validateBSM(resultActions);
    }

    /**
     * add BSM when creating a new part
     * {
     * "virksomhetsspesifikkeMetadata": {
     * "ppt-v1:sakferdig": true,
     * "ppt-v1:datohenvist": "2020-06-30+02:00",
     * "ppt-v1:datotidvedtakferdig": "2020-06-30T15:35:43.128158+02:00",
     * "ppt-v1:skolekontakt": "Harald Harfarge",
     * "ppt-v1:refSkole": "https://skole.eksempel.com",
     * "ppt-v1:snittKarakter": 1.2,
     * "ppt-v1:antallDagerVurdert": 1
     * }
     * }
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql",
            "/db-tests/bsm/registered_bsm_values.sql"})
    @WithMockCustomUser
    public void addBSMWithNewPart() throws Exception {
        PartPerson part = createPartPerson();
        String url = "/noark5v5/api/arkivstruktur/registrering" +
                "/dc600862-3298-4ec0-8541-3e51fb900054/ny-partperson";

        JsonFactory factory = new JsonFactory();
        StringWriter jsonPartWriter = new StringWriter();
        JsonGenerator jsonPart = factory.createGenerator(jsonPartWriter);
        jsonPart.writeStartObject();
        printPartPerson(jsonPart, part);
        printBSM(jsonPart, part);
        jsonPart.writeEndObject();
        jsonPart.close();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonPartWriter.toString()));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$." + SYSTEM_ID)
                        .exists());

        validatePartPerson(resultActions);
        validateBSM(resultActions);

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        MockHttpServletResponse response = resultActions.andReturn()
                .getResponse();
        url = "/noark5v5/api/arkivstruktur/partperson/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        // We checked it was possible to create, now check that what we
        // created is possible to retrieve
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + SYSTEM_ID)
                        .exists());

        validatePartPerson(resultActions);
        validateBSM(resultActions);
    }

    /**
     * Try to register BSMMetadata but required fields are misssing
     * object to create:
     * "beskrivelse": "Hvilket skoleår saken gjelder",
     * "kilde": "https://some/where/with/explanation"
     *
     * @throws IOException if required
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    @WithMockCustomUser
    public void registerMetadataBSMMissingFields() throws Exception {
        String url = "/noark5v5/api/metadata/" + NEW_BSM_DEF;
        JsonFactory factory = new JsonFactory();
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createBSMMetadataAllowNull(null, null, null,
                        DESCRIPTION_TEST, SOURCE_TEST,
                        factory).toString()));
        resultActions.andExpect(status().isBadRequest());
    }

    /**
     * Make sure it is possible to search BSMMetadata using a OData query.
     * We want to retrieve files containing (Harfarge) stored using the
     * attribute  ppt-v1:skolekontakt.
     * "ppt-v1:skolekontakt": "Harald Harfarge",
     *
     * @throws IOException if required
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql",
            "/db-tests/bsm/registered_bsm_values.sql",
            "/db-tests/bsm/bsm_values_to_object.sql"})
    @WithMockCustomUser
    public void searchBSMMetadataUsingODataString() throws Exception {
        String url = "/noark5v5/odata/api/arkivstruktur/mappe" +
                "?$filter=contains(ppt-v1:skolekontakt, 'Harald')";
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk());
        MockHttpServletResponse response = resultActions.andReturn()
                .getResponse();
        System.out.println(response.getContentAsString());
        validateBSMArray(resultActions);
    }

    /**
     * Make sure it is possible to search BSMMetadata using a OData query.
     * We want to retrieve files where the attribute ppt-v1:avsluttet is
     * equal to true
     *
     * @throws IOException if required
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/bsm/registered_bsm_values.sql",
            "/db-tests/bsm/bsm_values_to_object.sql"})
    @WithMockCustomUser
    public void searchBSMMetadataUsingODataBoolean() throws Exception {
        String url = "/noark5v5/odata/api/arkivstruktur/mappe" +
                "?$filter=" + BSM_BOOLEAN_NAME + " eq true";
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        MockHttpServletResponse response = resultActions.andReturn()
                .getResponse();
        System.out.println(response.getContentAsString());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0]." + BSM_DEF)
                        .exists())
                .andExpect(jsonPath("$.results[0]." + BSM_DEF +
                        "." + BSM_BOOLEAN_NAME).value(true));
    }

    /**
     * Make sure it is possible to search BSMMetadata Integer type using a
     * OData query. We want to retrieve files where the the attribute
     * ppt-v1:antallDagerVurdert > 4.
     *
     * @throws IOException if required
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql",
            "/db-tests/bsm/bsm_values_to_object.sql"})
    @WithMockCustomUser
    public void searchBSMMetadataUsingODataInteger() throws Exception {
        String url = "/noark5v5/odata/api/arkivstruktur/mappe" +
                "?$filter=" + BSM_INTEGER_NAME + " gt 4";
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        MockHttpServletResponse response = resultActions.andReturn()
                .getResponse();
        System.out.println(response.getContentAsString());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0]." + BSM_DEF)
                        .exists())
                .andExpect(jsonPath("$.results[0]." + BSM_DEF +
                        "." + BSM_INTEGER_NAME).value(BSM_INTEGER_VALUE));
    }

    /**
     * Make sure it is possible to search BSMMetadata Double type using a
     * OData query. We want to retrieve files where the the attribute
     * ppt-v1:snitt <= 4.9.
     *
     * @throws IOException if required
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql",
            "/db-tests/bsm/bsm_values_to_object.sql"})
    @WithMockCustomUser
    public void searchBSMMetadataUsingODataDouble() throws Exception {
        String url = "/noark5v5/odata/api/arkivstruktur/mappe" +
                "?$filter=" + BSM_DOUBLE_NAME + " le 4.9";
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        MockHttpServletResponse response = resultActions.andReturn()
                .getResponse();
        System.out.println(response.getContentAsString());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0]." + BSM_DEF)
                        .exists())
                .andExpect(jsonPath("$.results[0]." + BSM_DEF +
                        "." + BSM_DOUBLE_NAME).value(BSM_DOUBLE_VALUE));
    }

    /**
     * Make sure it is possible to search BSMMetadata Date type using a
     * OData query. We want to retrieve files where the the attribute
     * ppt-v1:snitt <= 4.9.
     *
     * @throws IOException if required
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/bsm/registered_bsm_values.sql",
            "/db-tests/bsm/bsm_values_to_object.sql"})
    @WithMockCustomUser
    public void searchBSMMetadataUsingODataDate() throws Exception {
        String url = "/noark5v5/odata/api/arkivstruktur/mappe" +
                "?$filter=" + BSM_DATE_NAME +
                encode(" eq '" + BSM_DATE_VALUE + "'");
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        // Leaving this until after summer holiday
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0]." + BSM_DEF)
                        .exists())
                .andExpect(jsonPath("$.results[0]." + BSM_DEF +
                                "." + BSM_DATE_NAME,
                        anyOf(is(BSM_DATE_VALUE),
                                is(BSM_DATE_VALUE_Z))));
    }

    /**
     * Make sure it is possible to search BSMMetadata Date type using a
     * OData query. We want to retrieve files where the the attribute
     * ppt-v1:snitt <= 4.9.
     * <p>
     * The ppt-v1:datohenvist attribute is defined in the sql test script
     *
     * @throws IOException  if required
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/bsm/registered_bsm_values.sql",
            "/db-tests/bsm/bsm_values_to_object.sql"})
    @WithMockCustomUser
    public void searchBSMMetadataUsingODataDateTime() throws Exception {
        String url = "/noark5v5/odata/api/arkivstruktur/mappe" +
                "?$filter=" + BSM_DATE_TIME_NAME + encode(
                " eq '" + BSM_DATE_TIME_VALUE + "'");

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0]." + BSM_DEF)
                        .exists())
                .andExpect(jsonPath("$.results[0]." + BSM_DEF +
                                "." + BSM_DATE_TIME_NAME,
                        anyOf(is(BSM_DATE_TIME_VALUE),
                                is(BSM_DATE_TIME_VALUE_Z))));
    }

    private void validatePartPerson(ResultActions resultActions)
            throws Exception {
        validatePersonIdentifier(resultActions);
        resultActions
                .andExpect(jsonPath("$." + PART_ROLE_FIELD + "." + CODE)
                        .value(TEMPLATE_PART_ROLE_CODE))
                .andExpect(jsonPath("$." + PART_ROLE_FIELD + "." + CODE_NAME)
                        .value(TEMPLATE_PART_ROLE_NAME))
                .andExpect(jsonPath("$." + NAME)
                        .value(NAME_TEST));
        validateContactInformation(resultActions);
        validatePostAddress(resultActions);
        validateResidingAddress(resultActions);
    }

    private void validateBSM(ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(jsonPath("$." + BSM_DEF)
                        .exists())
                .andExpect(jsonPath("$." + BSM_DEF + "." + BSM_DATE_TIME_NAME)
                        .value(BSM_DATE_TIME_VALUE))
                .andExpect(jsonPath("$." + BSM_DEF + "." + BSM_DATE_NAME)
                        .value(BSM_DATE_VALUE))
                .andExpect(jsonPath("$." + BSM_DEF + "." + BSM_BOOLEAN_NAME)
                        .value(BSM_BOOLEAN_VALUE))
                .andExpect(jsonPath("$." + BSM_DEF + "." + BSM_STRING_NAME)
                        .value(BSM_STRING_VALUE))
                .andExpect(jsonPath("$." + BSM_DEF + "." + BSM_URI_NAME)
                        .value(BSM_URI_VALUE))
                .andExpect(jsonPath("$." + BSM_DEF + "." + BSM_INTEGER_NAME)
                        .value(BSM_INTEGER_VALUE))
                .andExpect(jsonPath("$." + BSM_DEF + "." + BSM_DOUBLE_NAME)
                        .value(BSM_DOUBLE_VALUE));
    }

    private void validateBSMArray(ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(jsonPath("$.results.[0]." + BSM_DEF)
                        .exists())
                .andExpect(jsonPath("$.results.[0]." + BSM_DEF + "." + BSM_DATE_TIME_NAME)
                        .value(anyOf(is(BSM_DATE_TIME_VALUE), is(BSM_DATE_TIME_VALUE_Z))))
                .andExpect(jsonPath("$.results.[0]." + BSM_DEF + "." + BSM_DATE_NAME)
                        .value(anyOf(is(BSM_DATE_VALUE), is(BSM_DATE_VALUE_Z))))
                .andExpect(jsonPath("$.results.[0]." + BSM_DEF + "." + BSM_BOOLEAN_NAME)
                        .value(BSM_BOOLEAN_VALUE))
                .andExpect(jsonPath("$.results.[0]." + BSM_DEF + "." + BSM_STRING_NAME)
                        .value(BSM_STRING_VALUE))
                .andExpect(jsonPath("$.results.[0]." + BSM_DEF + "." + BSM_URI_NAME)
                        .value(BSM_URI_VALUE))
                .andExpect(jsonPath("$.results.[0]." + BSM_DEF + "." + BSM_INTEGER_NAME)
                        .value(BSM_INTEGER_VALUE))
                .andExpect(jsonPath("$.results.[0]." + BSM_DEF + "." + BSM_DOUBLE_NAME)
                        .value(BSM_DOUBLE_VALUE));
    }

    private void createBSMObjects() {
        bsmObjects.add(new BSMBase(BSM_BOOLEAN_NAME, BSM_BOOLEAN_VALUE));
        bsmObjects.add(new BSMBase(BSM_DATE_NAME,
                deserializeDate(BSM_DATE_VALUE), true));
        bsmObjects.add(new BSMBase(BSM_DATE_TIME_NAME,
                deserializeDateTime(BSM_DATE_TIME_VALUE)));
        bsmObjects.add(new BSMBase(BSM_STRING_NAME, BSM_STRING_VALUE));
        BSMBase bsmBase = new BSMBase(BSM_URI_NAME);
        bsmBase.setUriValue(BSM_URI_VALUE);
        bsmObjects.add(bsmBase);
        bsmObjects.add(new BSMBase(BSM_DOUBLE_NAME, BSM_DOUBLE_VALUE));
        bsmObjects.add(new BSMBase(BSM_INTEGER_NAME, BSM_INTEGER_VALUE));
    }

    private PartPerson createPartPerson() {
        PartPerson part = new PartPerson();
        part.setName(NAME_TEST);
        part.setdNumber(D_NUMBER_TEST);
        part.setSocialSecurityNumber(SOCIAL_SECURITY_NUMBER_TEST);
        part.setPartRole(createPartRole());
        part.setPostalAddress(createPostalAddress());
        part.setResidingAddress(createResidingAddress());
        part.setContactInformation(createContactInformation());
        part.setReferenceBSMBase(bsmObjects);
        return part;
    }

    private PartRole createPartRole() {
        PartRole partRole = new PartRole();
        partRole.setCode(TEMPLATE_PART_ROLE_CODE);
        partRole.setCodeName(TEMPLATE_PART_ROLE_NAME);
        return partRole;
    }

    private StringWriter createBSMMetadata(String name, String type,
                                           Boolean outdated, String description,
                                           String source, JsonFactory factory)
            throws IOException {
        StringWriter jsonBSMWriter = new StringWriter();
        JsonGenerator jsonBSM = factory.createGenerator(jsonBSMWriter);
        jsonBSM.writeStartObject();
        jsonBSM.writeStringField(NAME, name);
        jsonBSM.writeStringField(TYPE, type);
        jsonBSM.writeBooleanField(OUTDATED, outdated);
        jsonBSM.writeStringField(DESCRIPTION, description);
        jsonBSM.writeStringField(SOURCE, source);
        jsonBSM.writeEndObject();
        jsonBSM.close();
        return jsonBSMWriter;
    }

    private StringWriter createBSMMetadataAllowNull(
            String name, String type, Boolean outdated, String description,
            String source, JsonFactory factory) throws IOException {
        StringWriter jsonBSMWriter = new StringWriter();
        JsonGenerator jsonBSM = factory.createGenerator(jsonBSMWriter);
        jsonBSM.writeStartObject();
        if (null != name) {
            jsonBSM.writeStringField(NAME, name);
        }
        if (null != type) {
            jsonBSM.writeStringField(TYPE, OUTDATED);
        }
        if (null != description) {
            jsonBSM.writeStringField(DESCRIPTION, description);
        }
        if (null != source) {
            jsonBSM.writeStringField(SOURCE, source);
        }
        if (null != outdated) {
            jsonBSM.writeBooleanField(OUTDATED, outdated);
        }
        jsonBSM.writeEndObject();
        jsonBSM.close();
        return jsonBSMWriter;
    }

    private void validateBSMMetadata(String name, String type,
                                     Boolean outdated, String description,
                                     String source, ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + NAME)
                        .value(name))
                .andExpect(jsonPath("$." + TYPE)
                        .value(type))
                .andExpect(jsonPath("$." + DESCRIPTION)
                        .value(description))
                .andExpect(jsonPath("$." + SOURCE)
                        .value(source))
                .andExpect(jsonPath("$._links.self")
                        .exists())
                .andExpect(jsonPath(
                        "$._links.['" + REL_METADATA_BSM + "']")
                        .exists());
    }
}
