package nikita.webapp.general;

import nikita.webapp.spring.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static nikita.common.config.Constants.NOARK5_V5_CONTENT_TYPE_JSON;
import static nikita.common.config.Constants.REL_FONDS_STRUCTURE_KEYWORD;
import static nikita.common.config.HATEOASConstants.SELF;
import static nikita.common.config.N5ResourceMappings.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.KeywordCreator.createKeywordAsJSON;
import static utils.KeywordCreator.createUpdatedKeywordAsJSON;
import static utils.KeywordValidator.*;
import static utils.TestConstants.KEYWORD_TEST_UPDATED;

public class KeywordTest
        extends BaseTest {

    /**
     * Check that it is possible to add a Keyword to an existing
     * File
     * <p>
     * 1. Check that the GET ny-noekkelord works
     * 2. POST ny-noekkelord and check value and self REL
     * 3. Check that the keyword object can be retrieved
     * 4. Check that the retrieved keyword object can be updated
     * 5. Check that OData query on mappe/noekkelord works
     * 6. Check that OData query on noekkelord works
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/keyword.sql"})
    @WithMockCustomUser
    public void addKeywordWhenCreatingFile() throws Exception {
        // First get template to create / POST Keyword
        String urlNewKeyword = "/noark5v5/api/arkivstruktur/" +
                "mappe/48c81365-7193-4481-bc84-b025248fb310/" +
                NEW_KEYWORD;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlNewKeyword)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        // We do not do anything else with the result. Just make sure the
        // call works
        resultActions.andExpect(status().isOk());
        printDocumentation(resultActions);

        // Create an Keyword object associated with the File
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewKeyword)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createKeywordAsJSON()));
        resultActions.andExpect(status().isCreated());
        validateKeywordForFile(resultActions);
        printDocumentation(resultActions);

        // Retrieve an identified Keyword
        String urlKeyword = getHref(SELF, resultActions);
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlKeyword)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk());
        validateKeywordForFile(resultActions);
        printDocumentation(resultActions);

        // Update an identified Keyword
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put(urlKeyword)
                .contextPath(contextPath)
                .header("ETAG", "\"0\"")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createUpdatedKeywordAsJSON()));
        resultActions.andExpect(status().isOk());
        validateUpdatedKeyword(resultActions);
        printDocumentation(resultActions);

        // OData search for a File based on Keyword
        String odata = "?$filter=noekkelord/noekkelord eq '" +
                KEYWORD_TEST_UPDATED + "'&$top=1";
        String urlDocDescSearch = contextPath + "/odata/api/arkivstruktur/" +
                FILE + odata;
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlDocDescSearch)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)));
        printDocumentation(resultActions);

        // OData search for a given Keyword
        odata = "?$filter=noekkelord eq '" + KEYWORD_TEST_UPDATED + "'";
        String urlKeywordSearch = contextPath + "/odata/api/arkivstruktur/" +
                KEYWORD + odata;
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlKeywordSearch)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)));
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to add a Keyword to an existing
     * Class
     * <p>
     * 1. Check that the GET ny-noekkelord works
     * 2. POST ny-noekkelord and check value and self REL
     * 3. Check that the keyword object can be retrieved
     * 4. Check that the retrieved keyword object can be updated
     * 5. Check that OData query on mappe/noekkelord works
     * 6. Check that OData query on noekkelord works
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/keyword.sql"})
    @WithMockCustomUser
    public void addKeywordWhenCreatingClass() throws Exception {
        // First get template to create / POST Keyword
        String urlNewKeyword = "/noark5v5/api/arkivstruktur/" +
                "klasse/596c85fb-a6c4-4381-86b4-81df05234028/" + NEW_KEYWORD;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlNewKeyword)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        // We do not do anything else with the result. Just make sure the
        // call works
        resultActions.andExpect(status().isOk());
        printDocumentation(resultActions);

        // Create an Keyword object associated with the Class
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewKeyword)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createKeywordAsJSON()));
        resultActions.andExpect(status().isCreated());
        validateKeywordForClass(resultActions);
        printDocumentation(resultActions);

        // Retrieve an identified Keyword
        String urlKeyword = getHref(SELF, resultActions);
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlKeyword)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk());
        validateKeywordForClass(resultActions);
        printDocumentation(resultActions);

        // Update an identified Keyword
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put(urlKeyword)
                .contextPath(contextPath)
                .header("ETAG", "\"0\"")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createUpdatedKeywordAsJSON()));
        resultActions.andExpect(status().isOk());
        validateUpdatedKeyword(resultActions);
        printDocumentation(resultActions);

        // OData search for a Class based on Keyword
        String odata = "?$filter=noekkelord/noekkelord eq '" +
                KEYWORD_TEST_UPDATED + "'";
        String urlDocDescSearch = contextPath + "/odata/api/arkivstruktur/" +
                CLASS + odata;
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlDocDescSearch)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)));
        printDocumentation(resultActions);

        // OData search for a given Keyword
        odata = "?$filter=noekkelord eq '" + KEYWORD_TEST_UPDATED + "'";
        String urlKeywordSearch = contextPath + "/odata/api/arkivstruktur/" +
                KEYWORD + odata;
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlKeywordSearch)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)));
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to add a Keyword to an existing
     * Record.
     * <p>
     * 1. Check that the GET ny-noekkelord works
     * 2. POST ny-noekkelord and check value and self REL
     * 3. Check that the keyword object can be retrieved
     * 4. Check that the retrieved keyword object can be updated
     * 5. Check that OData query on registrering/noekkelord works
     * 6. Check that OData query on noekkelord works
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/keyword.sql"})
    @WithMockCustomUser
    public void addKeywordWhenCreatingRecord() throws Exception {
        // First get template to create / POST Keyword
        String urlNewKeyword = "/noark5v5/api/arkivstruktur/" +
                "registrering/99c2f1af-dd84-19e8-dd4f-cc21fe1578ff/" +
                NEW_KEYWORD;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlNewKeyword)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        // We do not do anything else with the result. Just make sure the
        // call works
        resultActions.andExpect(status().isOk());
        printDocumentation(resultActions);

        // Create an Keyword object associated with the Record
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewKeyword)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createKeywordAsJSON()));
        resultActions.andExpect(status().isCreated());
        validateKeywordForRecord(resultActions);
        printDocumentation(resultActions);

        // Retrieve an identified Keyword
        String urlKeyword = getHref(SELF, resultActions);
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlKeyword)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk());
        validateKeywordForRecord(resultActions);
        printDocumentation(resultActions);

        // Update an identified Keyword
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put(urlKeyword)
                .contextPath(contextPath)
                .header("ETAG", "\"0\"")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createUpdatedKeywordAsJSON()));
        resultActions.andExpect(status().isOk());
        validateUpdatedKeyword(resultActions);
        printDocumentation(resultActions);

        // OData search for a Record based on Keyword
        String odata = "?$filter=noekkelord/noekkelord eq '" +
                KEYWORD_TEST_UPDATED + "'";
        String urlDocDescSearch = contextPath + "/odata/api/arkivstruktur/" +
                RECORD + odata;
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlDocDescSearch)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)));
        printDocumentation(resultActions);

        // OData search for a given Keyword
        odata = "?$filter=noekkelord eq '" + KEYWORD_TEST_UPDATED + "'";
        String urlKeywordSearch = contextPath + "/odata/api/arkivstruktur/" +
                KEYWORD + odata;
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlKeywordSearch)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)));
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to delete a keyword by its systemID and that
     * the related file has no associated keyword afterwards
     * <p>
     * 1. Get associated File, make sure Keyword rel/href is in _links
     * 2. Delete Keyword using it's systemID
     * 3. Get previous associated File, make sure Keyword rel/href is
     * not in _links
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/keyword.sql"})
    @WithMockCustomUser
    public void deleteKeywordObjectWithFile() throws Exception {
        // 1. Get associated File, make sure Keyword rel/href is in _links
        String urlFile = contextPath + "/api/arkivstruktur/" +
                "mappe/48c81365-7193-4481-bc84-b025248fb310/";
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlFile)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$._links.['" + REL_FONDS_STRUCTURE_KEYWORD + "']")
                        .exists());

        // 2. Delete Keyword using it's systemID
        String urlKeyword = contextPath + "/api/arkivstruktur/" +
                "noekkelord/81cea881-1203-4e3f-943c-c0294e81e528/";
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlKeyword)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);

        // 3. Get previous associated File, make sure Keyword rel/href
        // is not in _links
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlFile)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$._links.['" + REL_FONDS_STRUCTURE_KEYWORD + "']")
                        .doesNotExist());
    }

    /**
     * Check that it is possible to delete a Record that has a Keyword
     * associated with it. The test is that referential integrity should
     * not be an issue.
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/keyword.sql"})
    @WithMockCustomUser
    public void deleteRecordWithKeyword() throws Exception {
        String urlRecord = "/noark5v5/api/arkivstruktur/" +
                "registrering/99c2f1af-dd84-19e8-dd4f-cc21fe1578ff/";
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlRecord)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to delete a File that has a Keyword associated
     * with it File. The test is that referential integrity should not be an
     * issue.
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/keyword.sql"})
    @WithMockCustomUser
    public void deleteFileWithKeyword() throws Exception {
        String urlFile = "/noark5v5/api/arkivstruktur/" +
                "mappe/48c81365-7193-4481-bc84-b025248fb310/";
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlFile)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to delete a Class that has a Keyword
     * associated with it. The test is that referential integrity should
     * not be an issue.
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/keyword.sql"})
    @WithMockCustomUser
    public void deleteClassWithKeyword() throws Exception {
        String urlClass = "/noark5v5/api/arkivstruktur/" +
                "klasse/596c85fb-a6c4-4381-86b4-81df05234028/";
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlClass)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);
    }
}
