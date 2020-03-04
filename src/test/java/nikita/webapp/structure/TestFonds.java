package nikita.webapp.structure;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nikita.N5CoreApp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.SELF;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

/*import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.NoarkGeneralEntity;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkGeneralEntity;
import nikita.common.util.deserialisers.hateoas.HateoasDeserializer;
import nikita.webapp.model.Token;
import nikita.webapp.model.User;
import nikita.webapp.utils.NoarkGeneralEntitySerializer;
*/



@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(classes = N5CoreApp.class)
//@AutoConfigureRestDocs(outputDir = "target/snippets")
public class TestFonds {

    private HttpHeaders headers;
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
                        (NOARK5_V5_CONTENT_TYPE_JSON + ";charset=UTF-8"))
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
                                (NOARK5_V5_CONTENT_TYPE_JSON + ";charset=UTF-8"))
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

/*

                        responseHeaders(headerWithName("Content-Type").
                                description("The Content-Type of the payload," +
                                        " e.g. `application/hal+json`")),

responseFields(subsectionWithPath("_links").
                                        description("Links to other resources")),
                                responseHeaders(headerWithName("Content-Type").
                                        description("The Content-Type of the payload, e.g. `application/hal+json`"))

        @Before
    public void setup() {
        mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(NoarkGeneralEntity.class,
                new NoarkGeneralEntitySerializer());
        module.addDeserializer(HateoasNoarkObject.class,
                new HateoasDeserializer());
        mapper.registerModule(module);
    }

    @Test
    public void test_1_Login() {
        checkLoggedIn();
    }

    @Test
    public void test_2_createFonds() throws JsonProcessingException {

        assertTrue(checkLoggedIn());

        assertNotNull(headers);
        assertNotNull(headers.get("Authorization"));

        Fonds fonds = new Fonds();
        fonds.setDescription("description");
        fonds.setTitle("title");

        String serializedFonds = mapper.writeValueAsString(fonds);

        HttpEntity<String> request = new HttpEntity<>(serializedFonds, headers);

        ResponseEntity<HateoasNoarkObject> responseEntity =
                restTemplate.exchange("/api/arkivstruktur/ny-arkiv",
                        HttpMethod.POST,
                        request,
                        HateoasNoarkObject.class);


        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        HateoasNoarkObject result = responseEntity.getBody();

        ArrayList<INoarkEntity> entities = (ArrayList<INoarkEntity>)
                result.getList();
        assertNotNull(entities);
        assertEquals(entities.size(), 1);

        INoarkGeneralEntity returnedFonds =
                (INoarkGeneralEntity) entities.get(0);

        assertEquals("description", returnedFonds.getDescription());
        assertEquals("title", returnedFonds.getTitle());
    }

    private boolean checkLoggedIn() {

        // If we're not logged in, try logging in
        if (headers != null) {
            return true;
        } else {
            User admin = new User("admin", "password");
            ResponseEntity<Token> responseEntity =
                    restTemplate.postForEntity("/auth", admin, Token.class);
            Token token = responseEntity.getBody();
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertNotNull(token);
            headers = new HttpHeaders();
            headers.add("Accept", "application/vnd.noark5-v5+json");
            headers.add("Content-Type", "application/vnd.noark5-v5+json");
            headers.add("Authorization", token.getToken());
            return true;
        }
    }

    */
}
