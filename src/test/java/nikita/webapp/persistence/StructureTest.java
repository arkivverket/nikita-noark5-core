package nikita.webapp.persistence;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import nikita.N5CoreApp;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson;
import nikita.webapp.spring.TestSecurityConfiguration;
import nikita.webapp.spring.security.NikitaUserDetailsService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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

import static com.jayway.jsonpath.JsonPath.read;
import static nikita.common.config.Constants.NOARK5_V5_CONTENT_TYPE_JSON;
import static nikita.common.config.N5ResourceMappings.SYSTEM_ID;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printCorrespondencePartPerson;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printCorrespondencePartUnit;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.CorrespondencePartCreator.createCorrespondencePartPerson;
import static utils.CorrespondencePartCreator.createCorrespondencePartUnit;
import static utils.CorrespondencePartValidator.*;

/**
 * Tests compliance with the data model for creation  / updating og domain
 * objects. Tests will be added here as needed.
 */

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(classes = N5CoreApp.class,
        webEnvironment = RANDOM_PORT)
@ContextConfiguration(classes = {TestSecurityConfiguration.class})
@ActiveProfiles("test")
@AutoConfigureRestDocs(outputDir = "target/snippets")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class StructureTest {

    private MockMvc mockMvc;

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
     * Test that it is possible to create a CorrespondencePartUnit and test
     * that the values and _links are correct. Then retrieve the systemID and
     * make sure the object can be retrieved independently from the database.
     *
     * @throws Exception if required
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    public void addCorrespondencePartUnitToExistingRecord() throws Exception {
        String url = "/noark5v5/api/arkivstruktur/registrering/" +
                "dc600862-3298-4ec0-8541-3e51fb900054/" +
                "ny-korrespondansepartenhet";

        JsonFactory factory = new JsonFactory();
        StringWriter jsonCorrespondencePartWriter = new StringWriter();
        JsonGenerator jsonCorrespondencePart =
                factory.createGenerator(jsonCorrespondencePartWriter);
        jsonCorrespondencePart.writeStartObject();
        printCorrespondencePartUnit(jsonCorrespondencePart,
                createCorrespondencePartUnit());
        jsonCorrespondencePart.writeEndObject();
        jsonCorrespondencePart.close();
        jsonCorrespondencePartWriter.close();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonCorrespondencePartWriter.toString())
                .with(user(nikitaUserDetailsService
                        .loadUserByUsername("admin@example.com"))));

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());

        resultActions.andExpect(status().isCreated());
        validateCorrespondencePartUnit(resultActions);
        validateCorrespondencePartUnitLink(resultActions);

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        url = "/noark5v5/api/arkivstruktur/korrespondansepartenhet/" +
                read(response.getContentAsString(), "$." + SYSTEM_ID);

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .with(user(nikitaUserDetailsService
                        .loadUserByUsername("admin@example.com"))));
        resultActions.andExpect(status().isOk());
        response = resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());

        validateCorrespondencePartUnit(resultActions);
        validateCorrespondencePartUnitLink(resultActions);
    }

    /**
     * Test that it is possible to create a CorrespondencePartPerson and test
     * that the values and _links are correct. Then retrieve the systemID and
     * make sure the object can be retrieved independently from the database.
     *
     * @throws Exception if required
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    public void addCorrespondencePartPersonToExistingRecord() throws Exception {
        String url = "/noark5v5/api/arkivstruktur/registrering/" +
                "dc600862-3298-4ec0-8541-3e51fb900054/" +
                "ny-korrespondansepartperson";

        CorrespondencePartPerson correspondencePart =
                createCorrespondencePartPerson();

        JsonFactory factory = new JsonFactory();
        StringWriter jsonCorrespondencePartWriter = new StringWriter();
        JsonGenerator jsonCorrespondencePart =
                factory.createGenerator(jsonCorrespondencePartWriter);
        jsonCorrespondencePart.writeStartObject();
        printCorrespondencePartPerson(jsonCorrespondencePart,
                correspondencePart);
        jsonCorrespondencePart.writeEndObject();
        jsonCorrespondencePart.close();
        jsonCorrespondencePartWriter.close();

        System.out.println(jsonCorrespondencePartWriter.toString());

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonCorrespondencePartWriter.toString())
                .with(user(nikitaUserDetailsService
                        .loadUserByUsername("admin@example.com"))));

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());

        resultActions.andExpect(status().isCreated());
        validateCorrespondencePartPerson(resultActions);
        validateCorrespondencePartPersonLink(resultActions);

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        url = "/noark5v5/api/arkivstruktur/korrespondansepartperson/" +
                read(response.getContentAsString(), "$." + SYSTEM_ID);

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .with(user(nikitaUserDetailsService
                        .loadUserByUsername("admin@example.com"))));
        resultActions.andExpect(status().isOk());
        validateCorrespondencePartPerson(resultActions);
        validateCorrespondencePartPersonLink(resultActions);
    }
}

