package nikita.webapp.odata;


import com.fasterxml.jackson.databind.ObjectMapper;
import nikita.N5CoreApp;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;

import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.FONDS;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(classes = N5CoreApp.class)
public class ODataIT {

    private HttpHeaders headers;
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;
    private String fonds1SystemId;
    private String fonds2SystemId;
    // Does not exist
    private String fonds3SystemId;

    public ODataIT() {
    }

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

    @Before
    public void setUp() {
        fonds1SystemId = "3318a63f-11a7-4ec9-8bf1-4144b7f281cf";
        fonds2SystemId = "f1677c47-99e1-42a7-bda2-b0bbc64841b7";
        fonds3SystemId = "0c5e864c-3269-4e01-9430-17d55291dae7";
    }
//
// arkivstruktur/dokumentbeskrivelse/cf8e1d0d-e94d-4d07-b5ed-46ba2df0465e/dokumentobjekt?$filter=contains(filnavn, '<20190803045988.RT234511@mail.redemash.com>')

    /**
     * Check to see if a known value 'alpha' is within the text of a tittel
     * of a fonds object
     */
    @Test
    @Sql("/db-tests/createFonds.sql")
    public void testContains() throws Exception {

        String accessToken = getAccessToken();
        String contains =
                encode("$filter", UTF_8.toString()) + "=" +
                        encode("contains(tittel, 'bravo')", UTF_8.toString());
        String baseAddress =
                "/odata/" + NOARK_FONDS_STRUCTURE_PATH + SLASH + FONDS;
        URI address = new URI(baseAddress + "?" + contains);

        ResultActions result =
                mockMvc.perform(get(address)
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(NOARK5_V5_CONTENT_TYPE_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType
                                (NOARK5_V5_CONTENT_TYPE_JSON + ";charset=UTF-8"))
                        .andDo(document("home",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                                /*,
                                links(atomLinks(),
                                        linkWithRel(SELF).
                                                description("The self link")
                                )*/
                        ));
        result.andExpect(jsonPath("$.*", hasSize(1)));
    }

    /**
     * Code copied / inspired from
     * https://www.baeldung.com/oauth-api-testing-with-spring-mvc
     *
     * @return
     * @throws Exception
     */
    private String getAccessToken()
            throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "nikita-client");
        params.add("username", "admin@example.com");
        params.add("password", "password");

        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic("nikita-client", "secret"))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }
}

