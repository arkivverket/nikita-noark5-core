package nikita.webapp.structure;

import nikita.N5CoreApp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static nikita.common.config.Constants.NOARK_FONDS_STRUCTURE_PATH;
import static nikita.common.config.Constants.SLASH;
import static nikita.common.config.N5ResourceMappings.FONDS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})


@ActiveProfiles({"test"})
*/

//@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {N5CoreApp.class}, webEnvironment = RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.profiles.active=test", "nikita.startup.create-demo-users=true"})
public class TestOData {

    @Autowired
    private WebApplicationContext context;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        /*

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .build();

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .build();*/
    }

    @Test
//    @Sql({"/db-tests/empty-database.sql"})
    @WithMockUser(value = "admin")
    public void testODataFilterContains() throws Exception {
        String url = contextPath + SLASH + "api/arkivstruktur/" + FONDS;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(url));
        //.contextPath(contextPath)
        //.accept("application/vnd.noark5+json"));
        //.with(user(userDetailsService
        //        .loadUserByUsername("admin@example.com"))));

        MockHttpServletResponse response = resultActions
                .andReturn().getResponse();
        System.out.println(response.getContentAsString());

        resultActions
                .andExpect(status().isOk()
                );

        resultActions
                .andDo(document("home",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()
                        )
                ));
    }


    @Test
    public void testThis() {
        String contains = "$filter=contains(tittel, 'bravo')";
        String baseAddress = NOARK_FONDS_STRUCTURE_PATH + SLASH + FONDS;
        String address = baseAddress + "?" + contains;
//        oDataService.convertODataToHQL(address, null);
    }
}
