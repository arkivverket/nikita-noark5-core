package nikita.webapp.general;

import nikita.webapp.spring.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static nikita.common.config.Constants.NOARK5_V5_CONTENT_TYPE_JSON;
import static nikita.common.config.HATEOASConstants.SELF;
import static nikita.common.config.N5ResourceMappings.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.AuthorCreator.createAuthorAsJSON;
import static utils.AuthorCreator.createUpdatedAuthorAsJSON;
import static utils.AuthorValidator.*;
import static utils.TestConstants.AUTHOR_TEST_UPDATED;

public class AuthorTest
        extends BaseTest {

    /**
     * Check that it is possible to add a Author to an existing
     * DocumentDescription.
     * <p>
     * 1. Check that the GET ny-forfatter works
     * 2. POST ny-forfatter and check value and self REL
     * 3. Check that the author object can be retrieved
     * 4. Check that the retrieved author object can be updated
     * 5. Check that OData query on dokumentbeskrivelse/forfatter works
     * 6. Check that OData query on forfatter works
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    @WithMockCustomUser
    public void addAuthorWhenCreatingDocumentDescription() throws Exception {
        // First get template to create / POST Author
        String urlNewAuthor = "/noark5v5/api/arkivstruktur/" +
                "dokumentbeskrivelse/66b92e78-b75d-4b0f-9558-4204ab31c2d1/" +
                NEW_AUTHOR;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlNewAuthor)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        // We do not do anything else with the result. Just make sure the
        // call works
        resultActions.andExpect(status().isOk());
        printDocumentation(resultActions);

        // Create an Author object associated with the DocumentDescription
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewAuthor)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createAuthorAsJSON()));
        resultActions.andExpect(status().isCreated());
        validateAuthorForDocumentDescription(resultActions);
        printDocumentation(resultActions);

        // Retrieve an identified Author
        String urlAuthor = getHref(SELF, resultActions);
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlAuthor)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk());
        validateAuthorForDocumentDescription(resultActions);
        printDocumentation(resultActions);

        // Update an identified Author
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put(urlAuthor)
                .contextPath(contextPath)
                .header("ETAG", "\"0\"")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createUpdatedAuthorAsJSON()));
        resultActions.andExpect(status().isOk());
        validateUpdatedAuthor(resultActions);
        printDocumentation(resultActions);

        // OData search for a DocumentDescription based on Author
        String odata = "?$filter=forfatter/forfatter eq '" +
                AUTHOR_TEST_UPDATED + "'";
        String urlDocDescSearch = contextPath + "/odata/api/arkivstruktur/" +
                DOCUMENT_DESCRIPTION + odata;
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlDocDescSearch)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)));
        printDocumentation(resultActions);

        // OData search for a given Author
        odata = "?$filter=forfatter eq '" + AUTHOR_TEST_UPDATED + "'";
        String urlAuthorSearch = contextPath + "/odata/api/arkivstruktur/" +
                AUTHOR + odata;
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlAuthorSearch)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)));
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to add a Author to an existing
     * Record.
     * <p>
     * 1. Check that the GET ny-forfatter works
     * 2. POST ny-forfatter and check value and self REL
     * 3. Check that the author object can be retrieved
     * 4. Check that the retrieved author object can be updated
     * 5. Check that OData query on registrering/forfatter works
     * 6. Check that OData query on forfatter works
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    @WithMockCustomUser
    public void addAuthorWhenCreatingRecord() throws Exception {
        // First get template to create / POST Author
        String urlNewAuthor = "/noark5v5/api/arkivstruktur/" +
                "registrering/dc600862-3298-4ec0-8541-3e51fb900054/" +
                NEW_AUTHOR;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlNewAuthor)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        // We do not do anything else with the result. Just make sure the
        // call works
        resultActions.andExpect(status().isOk());
        printDocumentation(resultActions);

        // Create an Author object associated with the Record
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewAuthor)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createAuthorAsJSON()));
        resultActions.andExpect(status().isCreated());
        validateAuthorForRecord(resultActions);
        printDocumentation(resultActions);

        // Retrieve an identified Author
        String urlAuthor = getHref(SELF, resultActions);
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlAuthor)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk());
        validateAuthorForRecord(resultActions);
        printDocumentation(resultActions);

        // Update an identified Author
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put(urlAuthor)
                .contextPath(contextPath)
                .header("ETAG", "\"0\"")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createUpdatedAuthorAsJSON()));
        resultActions.andExpect(status().isOk());
        validateUpdatedAuthor(resultActions);
        printDocumentation(resultActions);

        // OData search for a Record based on Author
        String odata = "?$filter=forfatter/forfatter eq '" +
                AUTHOR_TEST_UPDATED + "'";
        String urlDocDescSearch = contextPath + "/odata/api/arkivstruktur/" +
                RECORD + odata;
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlDocDescSearch)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)));
        printDocumentation(resultActions);

        // OData search for a given Author
        odata = "?$filter=forfatter eq '" + AUTHOR_TEST_UPDATED + "'";
        String urlAuthorSearch = contextPath + "/odata/api/arkivstruktur/" +
                AUTHOR + odata;
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlAuthorSearch)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)));
        printDocumentation(resultActions);
    }
}
