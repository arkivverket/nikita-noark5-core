package nikita.webapp.structure;


import nikita.N5CoreApp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.NEW_AUTHOR;
import static nikita.common.config.N5ResourceMappings.RECORD;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class tests that it is possible to add a Author to the following
 * entities: Record and DocumentDescription
 * <p>
 * It
 */

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(classes = N5CoreApp.class)
public class TestNikitaEntities {

    private MockMvc mockMvc;
    private String recordUUID = "71491ebc-e8ba-4477-844a-1d3086e29447";

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .apply(springSecurity())
                .alwaysDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .build();
    }

//    @Test
//    @Sql("/db-tests/testNikitaEntities.sql")
public void addAuthorToRecord() throws Exception {

    String url = HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH +
            SLASH + RECORD + SLASH + recordUUID + SLASH + NEW_AUTHOR;
    ResultActions actions =
            mockMvc.perform(post(url)
                    .param("forfatter", "Ole Olsen")
                    .contentType(NOARK5_V5_CONTENT_TYPE_JSON +
                            ";charset=UTF-8")
                    .accept(NOARK5_V5_CONTENT_TYPE_JSON))
                    .andExpect(status().isCreated());

        MockHttpServletResponse response = actions.andReturn().getResponse();
        System.out.println(response.getContentAsString());
    }

}
