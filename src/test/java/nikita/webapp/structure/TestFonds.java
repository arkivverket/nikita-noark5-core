package nikita.webapp.structure;


import com.fasterxml.jackson.databind.ObjectMapper;
import nikita.N5CoreApp;
import nikita.webapp.spring.TestSecurityConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.SELF;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(classes = N5CoreApp.class,
        webEnvironment = RANDOM_PORT)
@ContextConfiguration(classes = {TestSecurityConfiguration.class})
@ActiveProfiles("test")
public class TestFonds {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

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

    @Test
    public void contextLoads() {
    }

    //@Test
  //  @WithMockUser(roles=ROLE_ADMIN)
    public void applicationRootCheck() throws Exception {

        ResultActions actions =
                mockMvc.perform(get("/")
                        .accept(NOARK5_V5_CONTENT_TYPE_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType
                        (NOARK5_V5_CONTENT_TYPE_JSON))
                .andDo(document("home",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                                links(atomLinks(),
                                        linkWithRel(REL_LOGIN_OAUTH2).
                                                description("The login link")
                                )
                        ));

        MockHttpServletResponse response = actions.andReturn().getResponse();
        System.out.println(response.getContentAsString());
    }

    @Test
    public void oidcLinkPublished() throws Exception {

        ResultActions actions =
                mockMvc.perform(get("/")
                        .accept(NOARK5_V5_CONTENT_TYPE_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType
                                (NOARK5_V5_CONTENT_TYPE_JSON))
                        .andDo(document("home",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                links(halLinks(),
                                        linkWithRel
                                                (REL_LOGIN_OIDC).
                                                description("Get login " +
                                                        "information"),
                                        linkWithRel(SELF).
                                                description("Self REL")
                                )
                        ));

        MockHttpServletResponse response = actions.andReturn().getResponse();
        System.out.println(response.getContentAsString());
    }
}
