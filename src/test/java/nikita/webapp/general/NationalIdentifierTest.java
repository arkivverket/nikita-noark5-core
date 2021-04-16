package nikita.webapp.general;

import com.jayway.jsonpath.JsonPath;
import nikita.N5CoreApp;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static nikita.common.config.Constants.NOARK5_V5_CONTENT_TYPE_JSON;
import static nikita.common.config.N5ResourceMappings.SYSTEM_ID;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.NationalIdentifierCreator.*;
import static utils.NationalIdentifierValidator.*;

/**
 * Tests to describe nationalidentifier syntax. This includes also includes
 * how to search nationalidentifiers with OData
 */
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(classes = N5CoreApp.class,
        webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureRestDocs(outputDir = "target/snippets")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class NationalIdentifierTest {

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
     * Test that it is possible to undertake an OData query on
     * NationalIdentifier:Unit associated with an existing file (mappe)
     * <p>
     * /noark5v5/odata/api/arkivstruktur/mappe?$filter=enhetsidentifikator/organisasjonsnummer eq '02020202022
     *
     * @throws Exception if required
     */
    @Test
    @WithMockCustomUser
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/ni/ni_values.sql"})
    public void checkODataSearchFileOnNationalIdentifierUnit() throws Exception {

        String url = "/noark5v5/odata/api/arkivstruktur/mappe?$filter=" +
                "enhetsidentifikator/organisasjonsnummer eq '02020202022'";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.count")
                        .value(1))
                .andExpect(jsonPath("$.results[0]." + SYSTEM_ID)
                        .value("43d305de-b3c8-4922-86fd-45bd26f3bf01"));
        MockHttpServletResponse response = resultActions.andReturn()
                .getResponse();
        System.out.println(response.getContentAsString());

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    /**
     * Test that it is possible to undertake an OData query on
     * NationalIdentifier:Unit associated with an existing file (mappe)
     * <p>
     * /noark5v5/odata/api/arkivstruktur/mappe?$filter=enhetsidentifikator/organisasjonsnummer eq '02020202022
     *
     * @throws Exception if required
     */
    @Test
    @WithMockCustomUser
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/ni/ni_values.sql"})
    public void checkODataSearchFileOnNationalIdentifierPlan() throws Exception {

        String url = "/noark5v5/odata/api/arkivstruktur/mappe?$filter=" +
                "enhetsidentifikator/organisasjonsnummer eq '02020202022'";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.count")
                        .value(1))
                .andExpect(jsonPath("$.results[0]." + SYSTEM_ID)
                        .value("43d305de-b3c8-4922-86fd-45bd26f3bf01"));
        MockHttpServletResponse response = resultActions.andReturn()
                .getResponse();
        System.out.println(response.getContentAsString());

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    /**
     * Test that it is possible to create a NationalIdentifier:Building and
     * associate it with an existing Record
     *
     * @throws Exception if required
     */
    @Test
    @WithMockCustomUser
    @Sql("/db-tests/basic_structure.sql")
    public void addNationalIdentifierBuildingToExistingRecord()
            throws Exception {
        String url = "/noark5v5/api/arkivstruktur/registrering/" +
                "dc600862-3298-4ec0-8541-3e51fb900054/ny-bygning";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(getTestBuildingAsJson()));

        resultActions.andExpect(status().isCreated());
        validateBuilding(resultActions);

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        url = "/noark5v5/api/arkivstruktur/bygning/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        // Check that the object is retrieveable
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        validateBuilding(resultActions);
    }

    /**
     * Test that it is possible to create a NationalIdentifier:Plan and
     * associate it with an existing Record
     *
     * @throws Exception if required
     */
    @Test
    @WithMockCustomUser
    @Sql("/db-tests/basic_structure.sql")
    public void addNationalIdentifierPlanToExistingRecord()
            throws Exception {
        String url = "/noark5v5/api/arkivstruktur/registrering/" +
                "dc600862-3298-4ec0-8541-3e51fb900054/ny-plan";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(getTestPlanAsJson()));

        resultActions.andExpect(status().isCreated());
        validatePlan(resultActions);

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        url = "/noark5v5/api/arkivstruktur/plan/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        // Check that the object is retrieveable
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        validatePlan(resultActions);
    }

    /**
     * Test that it is possible to create a NationalIdentifier:Position and
     * associate it with an existing Record
     *
     * @throws Exception if required
     */
    @Test
    @WithMockCustomUser
    @Sql("/db-tests/basic_structure.sql")
    public void addNationalIdentifierPositionToExistingRecord()
            throws Exception {
        String url = "/noark5v5/api/arkivstruktur/registrering/" +
                "dc600862-3298-4ec0-8541-3e51fb900054/ny-posisjon";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(getTestPositionAsJson()));

        resultActions.andExpect(status().isCreated());
        validatePosition(resultActions);

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        url = "/noark5v5/api/arkivstruktur/posisjon/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        // Check that the object is retrieveable
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        validatePosition(resultActions);
    }

    /**
     * Test that it is possible to create a NationalIdentifier:Unit
     * and associate it with an existing Record
     *
     * @throws Exception if required
     */
    @Test
    @WithMockCustomUser
    @Sql("/db-tests/basic_structure.sql")
    public void addNationalIdentifierUnitIdentifierToExistingRecord()
            throws Exception {
        String url = "/noark5v5/api/arkivstruktur/registrering/" +
                "dc600862-3298-4ec0-8541-3e51fb900054/ny-enhetsidentifikator";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(getTestUnitAsJson()));

        resultActions.andExpect(status().isCreated());
        validateUnit(resultActions);

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        url = "/noark5v5/api/arkivstruktur/enhetsidentifikator/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        // Check that the object is retrieveable
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        validateUnit(resultActions);
    }

    /**
     * Test that it is possible to create a NationalIdentifier:DNumber
     * and associate it with an existing Record
     *
     * @throws Exception if required
     */
    @Test
    @WithMockCustomUser
    @Sql("/db-tests/basic_structure.sql")
    public void addNationalIdentifierDNumberToExistingRecord()
            throws Exception {
        String url = "/noark5v5/api/arkivstruktur/registrering/" +
                "dc600862-3298-4ec0-8541-3e51fb900054/ny-dnummer";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(getTestDNumberAsJson()));

        resultActions.andExpect(status().isCreated());
        validateDNumber(resultActions);

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        url = "/noark5v5/api/arkivstruktur/dnummer/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        // Check that the object is retrieveable
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        validateDNumber(resultActions);
    }

    /**
     * Test that it is possible to create a NationalIdentifier :
     * SocialSecurityNumber and associate it with an existing Record
     *
     * @throws Exception if required
     */
    @Test
    @WithMockCustomUser
    @Sql("/db-tests/basic_structure.sql")
    public void addNationalIdentifierSocialSecurityNumberToExistingRecord()
            throws Exception {
        String url = "/noark5v5/api/arkivstruktur/registrering/" +
                "dc600862-3298-4ec0-8541-3e51fb900054/ny-foedselsnummer";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(getTestSocialSecurityNumberAsJson()));

        resultActions.andExpect(status().isCreated());
        validateSocialSecurityNumber(resultActions);

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        url = "/noark5v5/api/arkivstruktur/foedselsnummer/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        // Check that the object is retrieveable
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        validateSocialSecurityNumber(resultActions);
    }

    /**
     * Test that it is possible to create a NationalIdentifier :
     * CadastralUnit and associate it with an existing Record
     *
     * @throws Exception if required
     */
    @Test
    @WithMockCustomUser
    @Sql("/db-tests/basic_structure.sql")
    public void addNationalIdentifierCadastralUnitToExistingRecord()
            throws Exception {
        String url = "/noark5v5/api/arkivstruktur/registrering/" +
                "dc600862-3298-4ec0-8541-3e51fb900054/ny-matrikkel";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(getTestCadastralUnitAsJson()));

        resultActions.andExpect(status().isCreated());
        validateCadastralUnit(resultActions);

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        url = "/noark5v5/api/arkivstruktur/matrikkel/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        // Check that the object is retrieveable
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        validateCadastralUnit(resultActions);
    }

    /**
     * Test that it is possible to create a NationalIdentifier:Building and
     * associate it with an existing File
     *
     * @throws Exception if required
     */
    @Test
    @WithMockCustomUser
    @Sql("/db-tests/basic_structure.sql")
    public void addNationalIdentifierBuildingToExistingFile()
            throws Exception {
        String url = "/noark5v5/api/arkivstruktur/mappe/" +
                "43d305de-b3c8-4922-86fd-45bd26f3bf01/ny-bygning";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(getTestBuildingAsJson()));

        resultActions.andExpect(status().isCreated());
        validateBuilding(resultActions);

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        url = "/noark5v5/api/arkivstruktur/bygning/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        // Check that the object is retrieveable
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        validateBuilding(resultActions);
    }

    /**
     * Test that it is possible to create a NationalIdentifier:Plan and
     * associate it with an existing File
     *
     * @throws Exception if required
     */
    @Test
    @WithMockCustomUser
    @Sql("/db-tests/basic_structure.sql")
    public void addNationalIdentifierPlanToExistingFile()
            throws Exception {
        String url = "/noark5v5/api/arkivstruktur/mappe/" +
                "43d305de-b3c8-4922-86fd-45bd26f3bf01/ny-plan";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(getTestPlanAsJson()));

        resultActions.andExpect(status().isCreated());
        validatePlan(resultActions);

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        url = "/noark5v5/api/arkivstruktur/plan/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        // Check that the object is retrieveable
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        validatePlan(resultActions);
    }

    /**
     * Test that it is possible to create a NationalIdentifier:Position and
     * associate it with an existing File
     *
     * @throws Exception if required
     */
    @Test
    @WithMockCustomUser
    @Sql("/db-tests/basic_structure.sql")
    public void addNationalIdentifierPositionToExistingFile()
            throws Exception {
        String url = "/noark5v5/api/arkivstruktur/mappe/" +
                "43d305de-b3c8-4922-86fd-45bd26f3bf01/ny-posisjon";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(getTestPositionAsJson()));

        resultActions.andExpect(status().isCreated());
        validatePosition(resultActions);

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        url = "/noark5v5/api/arkivstruktur/posisjon/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        // Check that the object is retrieveable
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        validatePosition(resultActions);
    }

    /**
     * Test that it is possible to create a NationalIdentifier:Unit
     * and associate it with an existing File
     *
     * @throws Exception if required
     */
    @Test
    @WithMockCustomUser
    @Sql("/db-tests/basic_structure.sql")
    public void addNationalIdentifierUnitIdentifierToExistingFile()
            throws Exception {
        String url = "/noark5v5/api/arkivstruktur/mappe/" +
                "43d305de-b3c8-4922-86fd-45bd26f3bf01/ny-enhetsidentifikator";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(getTestUnitAsJson()));

        resultActions.andExpect(status().isCreated());
        validateUnit(resultActions);

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        url = "/noark5v5/api/arkivstruktur/enhetsidentifikator/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        // Check that the object is retrieveable
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        validateUnit(resultActions);
    }

    /**
     * Test that it is possible to create a NationalIdentifier:DNumber
     * and associate it with an existing File
     *
     * @throws Exception if required
     */
    @Test
    @WithMockCustomUser
    @Sql("/db-tests/basic_structure.sql")
    public void addNationalIdentifierDNumberToExistingFile()
            throws Exception {
        String url = "/noark5v5/api/arkivstruktur/mappe/" +
                "43d305de-b3c8-4922-86fd-45bd26f3bf01/ny-dnummer";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(getTestDNumberAsJson()));

        resultActions.andExpect(status().isCreated());
        validateDNumber(resultActions);

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        url = "/noark5v5/api/arkivstruktur/dnummer/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        // Check that the object is retrieveable
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        validateDNumber(resultActions);
    }

    /**
     * Test that it is possible to create a NationalIdentifier :
     * SocialSecurityNumber and associate it with an existing File
     *
     * @throws Exception if required
     */
    @Test
    @WithMockCustomUser
    @Sql("/db-tests/basic_structure.sql")
    public void addNationalIdentifierSocialSecurityNumberToExistingFile()
            throws Exception {
        String url = "/noark5v5/api/arkivstruktur/mappe/" +
                "43d305de-b3c8-4922-86fd-45bd26f3bf01/ny-foedselsnummer";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(getTestSocialSecurityNumberAsJson()));

        resultActions.andExpect(status().isCreated());
        validateSocialSecurityNumber(resultActions);

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        url = "/noark5v5/api/arkivstruktur/foedselsnummer/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        // Check that the object is retrieveable
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        validateSocialSecurityNumber(resultActions);
    }

    /**
     * Test that it is possible to create a NationalIdentifier :
     * CadastralUnit and associate it with an existing File
     *
     * @throws Exception if required
     */
    @Test
    @WithMockCustomUser
    @Sql("/db-tests/basic_structure.sql")
    public void addNationalIdentifierCadastralUnitToExistingFile()
            throws Exception {
        String url = "/noark5v5/api/arkivstruktur/mappe/" +
                "43d305de-b3c8-4922-86fd-45bd26f3bf01/ny-matrikkel";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(getTestCadastralUnitAsJson()));

        resultActions.andExpect(status().isCreated());
        validateCadastralUnit(resultActions);

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        url = "/noark5v5/api/arkivstruktur/matrikkel/" +
                JsonPath.read(response.getContentAsString(), "$." + SYSTEM_ID);

        // Check that the object is retrieveable
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        validateCadastralUnit(resultActions);
    }
}

