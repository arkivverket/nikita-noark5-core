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
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkGeneralEntity;
import nikita.common.util.deserialisers.hateoas.HateoasDeserializer;
import nikita.webapp.model.Token;
import nikita.webapp.model.User;
import nikita.webapp.utils.NoarkGeneralEntitySerializer;
*/



@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(classes = N5CoreApp.class)
//@AutoConfigureRestDocs(outputDir = "target/snippets")
public class FondsIT {

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

    @Test
    public void validdates() throws Exception {
	System.out.println("info: testing date and datetime parsing");
	String[] datetimemust = {
            "1997-07-16T19:20+01:00",
            "1997-07-16T19:20:30+01:00",
            "1997-07-16T19:20:30.45+01:00",
            "2012-10-10T15:00:00Z",
            "2014-11-22T15:15:02.956+01:00",

            // These could be handled too
            "1997-07-16Z",
            "1865-02-13T00:00:00Z",
            "1865-02-13T00:00:00+00:00",
            "1865-02-13T00:00:00+01:00",
            "1865-02-13T00:00:00-01:00",
            "1864-10-09T00:00:00Z",
            "1864-10-09T00:00:00+00:00",
        };
        String[] datetimereject = {
            "1997-07-16T19:20+0100",
            "1997-07-16T19:20-0100",
            "1997-07-16T19:20+01",
            "1997-07-16T19:20-01",
            "1997",
            "1997-07",
            "1997-07-16",
        };
	for (String teststr : datetimemust) {
	    StringBuilder errors = new StringBuilder();
	    String json = "{ \"d\": \"" + teststr+ "\"}";
	    ObjectNode objectNode =
		(ObjectNode) new ObjectMapper().readTree(json);
	    OffsetDateTime d =
		deserializeDateTime("d", objectNode, errors);
	    if (null != d) {
		System.out.println("success: datetime '" + teststr + "' parsed to " + d);
		String str = formatDateTime(d);
		if (str.equals(teststr)) {
		    System.out.println("success: datetime serialized back to '" + str + "'");
		} else {
		    // Some datetime are not expected to stay
		    // unchanged, like 19:30 -> 19:30:00, so only flag
		    // warning, not error.
		    System.out.println("warning: serialized datetime changed to '" + str + "'");
		}
	    } else {
		System.out.println("error: unable to parse datetime '" + teststr + "'");
	    }
	}
	for (String teststr : datetimereject) {
	    StringBuilder errors = new StringBuilder();
	    String json = "{ \"d\": \"" + teststr+ "\"}";
	    ObjectNode objectNode =
		(ObjectNode) new ObjectMapper().readTree(json);
	    OffsetDateTime d =
		deserializeDateTime("d", objectNode, errors);
	    if (null != d) {
		System.out.println("error: datetime '" + teststr + "' parsed to " + d);
	    } else {
		System.out.println("success: unable to parse datetime '" + teststr + "'");
	    }
	}

	String[] datemust = {
            "1997-07-16+01:00",
            "1997-07-16-01:00",
            "1997-07-16Z",
            "1865-02-13Z",
	};
	String[] datereject = {
            "1997-07-16+0100",
            "1997-07-16-0100",
            "1997-07-16+01",
            "1997-07-16-01",
            "1997-07-16",
	};
	for (String teststr : datemust) {
	    StringBuilder errors = new StringBuilder();
	    String json = "{ \"d\": \"" + teststr+ "\"}";
	    ObjectNode objectNode =
		(ObjectNode) new ObjectMapper().readTree(json);
	    OffsetDateTime d =
		deserializeDate("d", objectNode, errors);
	    if (null != d) {
		System.out.println("success: date '" + teststr + "' parsed to " + d);
		String str = formatDate(d);
		if (str.equals(teststr)) {
		    System.out.println("success: date serialized back to '" + str + "'");
		} else {
		    System.out.println("error: serialized date changed to '" + str + "'");
		}
	    } else {
		System.out.println("error: unable to parse date '" + teststr + "'");
	    }
	}
	for (String teststr : datereject) {
	    StringBuilder errors = new StringBuilder();
	    String json = "{ \"d\": \"" + teststr+ "\"}";
	    ObjectNode objectNode =
		(ObjectNode) new ObjectMapper().readTree(json);
	    OffsetDateTime d =
		deserializeDate("d", objectNode, errors);
	    if (null != d) {
		System.out.println("error: date '" + teststr + "' parsed to " + d);
	    } else {
		System.out.println("success: unable to parse date '" + teststr + "'");
	    }
	}
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
                restTemplate.exchange("/hateoas-api/arkivstruktur/ny-arkiv",
                        HttpMethod.POST,
                        request,
                        HateoasNoarkObject.class);


        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        HateoasNoarkObject result = responseEntity.getBody();

        ArrayList<INikitaEntity> entities = (ArrayList<INikitaEntity>)
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
