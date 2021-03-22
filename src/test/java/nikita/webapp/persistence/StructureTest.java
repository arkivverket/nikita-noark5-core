package nikita.webapp.persistence;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import nikita.N5CoreApp;
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

import java.io.StringWriter;

import static com.jayway.jsonpath.JsonPath.read;
import static nikita.common.config.Constants.NOARK5_V5_CONTENT_TYPE_JSON;
import static nikita.common.config.MetadataConstants.CORRESPONDENCE_PART_CODE_EA;
import static nikita.common.config.MetadataConstants.CORRESPONDENCE_PART_DESCRIPTION_EA;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests compliance with the data model for creation  / updating og domain
 * objects
 */

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(classes = N5CoreApp.class,
        webEnvironment = RANDOM_PORT)
@ContextConfiguration(classes = {TestSecurityConfiguration.class})
@ActiveProfiles("test")
@AutoConfigureRestDocs(outputDir = "target/snippets")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
     * @throws Exception
     */

    @Test
    @Sql("/db-tests/bsm.sql")
    public void addCorrespondencePartUnitToExistingRecord() throws Exception {
        String url = "/noark5v5/api/arkivstruktur/registrering/" +
                "dc600862-3298-4ec0-8541-3e51fb900054/" +
                "ny-korrespondansepartenhet";
        String selfHref = "http://localhost:8092" + url;

        JsonFactory factory = new JsonFactory();
        StringWriter jsonWriter = new StringWriter();
        JsonGenerator json = factory.createGenerator(jsonWriter);
        json.writeStartObject();
        json.writeObjectFieldStart(CORRESPONDENCE_PART_TYPE);
        json.writeStringField(CODE, CORRESPONDENCE_PART_CODE_EA);
        json.writeStringField(CODE_NAME, CORRESPONDENCE_PART_DESCRIPTION_EA);
        json.writeEndObject(); // correspondence part type
        json.writeStringField(CONTACT_PERSON, "Hans Gruber");
        json.writeStringField(NAME, "Gruber Industries");
        json.writeStringField(UNIT_IDENTIFIER, "123456789");
        json.writeObjectFieldStart(POSTAL_ADDRESS);
        json.writeStringField(ADDRESS_LINE_1, "30th floor");
        json.writeStringField(ADDRESS_LINE_2, "Nakatomi Plaza");
        json.writeStringField(ADDRESS_LINE_3, "New York Place");
        json.writeStringField(POSTAL_NUMBER, "0001");
        json.writeStringField(POSTAL_TOWN, "Oslo");
        json.writeStringField(COUNTRY_CODE, "no");
        json.writeEndObject(); // postal address
        json.writeObjectFieldStart(BUSINESS_ADDRESS);
        json.writeStringField(ADDRESS_LINE_1, "31st floor");
        json.writeStringField(ADDRESS_LINE_2, "Nakatomi Plaza");
        json.writeStringField(ADDRESS_LINE_3, "Kongens gate 33");
        json.writeStringField(POSTAL_NUMBER, "0002");
        json.writeStringField(POSTAL_TOWN, "Oslo");
        json.writeStringField(COUNTRY_CODE, "no");
        json.writeEndObject(); // business address
        json.writeObjectFieldStart(CONTACT_INFORMATION);
        json.writeStringField(TELEPHONE_NUMBER, "+4700001111");
        json.writeStringField(EMAIL_ADDRESS, "hans.gruber@example.com");
        json.writeStringField(MOBILE_TELEPHONE_NUMBER, "+4711110000");
        json.writeEndObject(); // contact information
        json.writeEndObject(); // json object
        json.close();

        System.out.println(jsonWriter.toString());

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(jsonWriter.toString())
                .with(user(nikitaUserDetailsService
                        .loadUserByUsername("admin@example.com"))));

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());

        resultActions.andExpect(status().isCreated());
        validateCorrespondencePartUnit(resultActions);

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
        validateCorrespondencePartUnit(resultActions);
    }

    private void validateCorrespondencePartUnit(ResultActions resultActions)
            throws Exception {

        resultActions
                .andExpect(jsonPath("$." + CORRESPONDENCE_PART_TYPE +
                        "." + CODE).value(CORRESPONDENCE_PART_CODE_EA))
                .andExpect(jsonPath("$." + CORRESPONDENCE_PART_TYPE +
                        "." + CODE_NAME)
                        .value(CORRESPONDENCE_PART_DESCRIPTION_EA))
                .andExpect(jsonPath("$." + CONTACT_PERSON)
                        .value("Hans Gruber"))
                .andExpect(jsonPath("$." + NAME)
                        .value("Gruber Industries"))
                .andExpect(jsonPath("$." + UNIT_IDENTIFIER + "." +
                        ORGANISATION_NUMBER).value("123456789"))
                .andExpect(jsonPath("$." + POSTAL_ADDRESS + "." +
                        ADDRESS_LINE_1).value("30th floor"))
                .andExpect(jsonPath("$." + POSTAL_ADDRESS + "." +
                        ADDRESS_LINE_2).value("Nakatomi Plaza"))
                .andExpect(jsonPath("$." + POSTAL_ADDRESS + "." +
                        ADDRESS_LINE_3).value("New York Place"))
                .andExpect(jsonPath("$." + POSTAL_ADDRESS + "." +
                        POSTAL_NUMBER).value("0001"))
                .andExpect(jsonPath("$." + POSTAL_ADDRESS + "." +
                        POSTAL_TOWN).value("Oslo"))
                .andExpect(jsonPath("$." + POSTAL_ADDRESS + "." +
                        COUNTRY_CODE).value("no"))
                .andExpect(jsonPath("$." + BUSINESS_ADDRESS + "." +
                        ADDRESS_LINE_1).value("31st floor"))
                .andExpect(jsonPath("$." + BUSINESS_ADDRESS + "." +
                        ADDRESS_LINE_2).value("Nakatomi Plaza"))
                .andExpect(jsonPath("$." + BUSINESS_ADDRESS + "." +
                        ADDRESS_LINE_3).value("Kongens gate 33"))
                .andExpect(jsonPath("$." + BUSINESS_ADDRESS + "." +
                        POSTAL_NUMBER).value("0002"))
                .andExpect(jsonPath("$." + BUSINESS_ADDRESS + "." +
                        POSTAL_TOWN).value("Oslo"))
                .andExpect(jsonPath("$." + BUSINESS_ADDRESS + "." +
                        COUNTRY_CODE).value("no"))
                .andExpect(jsonPath("$." + CONTACT_INFORMATION + "." +
                        TELEPHONE_NUMBER).value("+4700001111"))
                .andExpect(jsonPath("$." + CONTACT_INFORMATION + "." +
                        EMAIL_ADDRESS).value("hans.gruber@example.com"))
                .andExpect(jsonPath("$." + CONTACT_INFORMATION + "." +
                        MOBILE_TELEPHONE_NUMBER).value("+4711110000"));
    }
}

