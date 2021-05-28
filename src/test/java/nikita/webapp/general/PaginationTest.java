package nikita.webapp.general;

import nikita.webapp.spring.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.HREF;
import static nikita.common.config.HATEOASConstants.SELF;
import static nikita.common.config.ODataConstants.NEXT;
import static nikita.common.config.ODataConstants.PREVIOUS;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The tests in this class cover the following pagination functionality:
 * - count value contains a count of all results, not just the results in this
 * page
 * - previous and next links are present if expected
 * - previous and next links are not present if not expected
 * - check self href is correct if absolute max results value for a query is
 * attempted
 * - check that it is possible to retrieve results with a different page ize
 * than 10
 */
public class PaginationTest
        extends BaseTest {

    /**
     * Get all File objects. This should produce the following result:
     * Note: There are 42 File objects in the database.
     * 39 from pagination sql and 3 from basic_structure.sql
     * {
     * "count" : 42
     * "results" : [
     * left out for brevity
     * ],
     * "_links":
     * {
     * "self":
     * {
     * "href":
     * "http://localhost:8092/noark5v5/odata/api/arkivstruktur/mappe?$filter=contains(tittel,%20'test')"
     * },
     * "next":
     * {
     * "href":
     * "http://localhost:8092/noark5v5/odata/api/arkivstruktur/mappe?$filter=contains(tittel,%20'test')&$skip=10"
     * }
     * }
     * }
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/pagination.sql"})
    @WithMockCustomUser
    public void getAllFilesCheckPaginationCorrect()
            throws Exception {
        String urlAllFile = contextPath + "/odata/" + HREF_BASE_FILE +
                "?$filter=contains(tittel, 'test')";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlAllFile)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());

        resultActions
                .andExpect(status().isOk())
                // 42 is the number of File objects declared between
                // basic_structure.sql and pagination.sql
                .andExpect(jsonPath("$." + ENTITY_ROOT_NAME_LIST_COUNT)
                        .value(42))
                // The page size is set to 10
                .andExpect(jsonPath("$.results", hasSize(10)))
                .andExpect(jsonPath("$._links.['"
                                + SELF + "']." + HREF,
                        endsWith("mappe?$filter=contains(tittel,%20'test')")))
                .andExpect(jsonPath("$._links.['"
                                + NEXT + "']." + HREF,
                        endsWith("mappe?$filter=contains(tittel,%20'test')" +
                                "&$skip=10")))
                .andExpect(jsonPath("$._links.['"
                        + PREVIOUS + "']")
                        .doesNotExist());
    }

    /**
     * Get all File objects. This should produce the following result:
     * Note: There are 42 File objects in the database.
     * 39 from pagination sql and 3 from basic_structure.sql
     * {
     * "count" : 42
     * "results" : [
     * left out for brevity
     * ],
     * "_links":
     * {
     * "self":
     * {
     * "href":
     * "http://localhost:8092/noark5v5/odata/api/arkivstruktur/mappe?$filter=contains(tittel,%20'test')"
     * },
     * "next":
     * {
     * "href":
     * "http://localhost:8092/noark5v5/odata/api/arkivstruktur/mappe?$filter=contains(tittel,%20'test')&$skip=10"
     * }
     * }
     * }
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/pagination.sql"})
    @WithMockCustomUser
    public void getSubsetFilesGreaterThanPageSizeCheckPaginationCorrect()
            throws Exception {
        String urlAllFile = contextPath + "/odata/" + HREF_BASE_FILE +
                "?$filter=contains(tittel, 'test')&$top=18";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlAllFile)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());

        resultActions
                .andExpect(status().isOk())
                // 42 is the number of File objects declared between
                // basic_structure.sql and pagination.sql
                .andExpect(jsonPath("$." + ENTITY_ROOT_NAME_LIST_COUNT)
                        .value(42))
                // The page size is set to 10
                .andExpect(jsonPath("$.results", hasSize(18)))
                .andExpect(jsonPath("$._links.['"
                                + SELF + "']." + HREF,
                        endsWith("mappe?$filter=contains(tittel,%20'test')&$top=18")))
                .andExpect(jsonPath("$._links.['"
                                + NEXT + "']." + HREF,
                        endsWith("mappe?$filter=contains(tittel,%20'test')" +
                                "&$top=18&$skip=18")))
                .andExpect(jsonPath("$._links.['"
                        + PREVIOUS + "']").doesNotExist());
    }

    /**
     * Get all File objects. This should produce the following result:
     * Note: There are 42 File objects in the database.
     * 39 from pagination sql and 3 from basic_structure.sql
     * {
     * "count" : 42
     * "results" : [
     * left out for brevity
     * ],
     * "_links":
     * {
     * "self":
     * {
     * "href":
     * "http://localhost:8092/noark5v5/odata/api/arkivstruktur/mappe?$filter=contains(tittel,%20'test')"
     * },
     * "next":
     * {
     * "href":
     * "http://localhost:8092/noark5v5/odata/api/arkivstruktur/mappe?$filter=contains(tittel,%20'test')&$skip=10"
     * }
     * }
     * }
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/pagination.sql"})
    @WithMockCustomUser
    public void getAllFilesWithTopToLargeCheckPaginationCorrect()
            throws Exception {
        String urlAllFile = contextPath + "/odata/" + HREF_BASE_FILE +
                "?$filter=contains(tittel, 'test')&$top=100";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlAllFile)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());

        resultActions
                .andExpect(status().isOk())
                // 42 is the number of File objects declared between
                // basic_structure.sql and pagination.sql
                .andExpect(jsonPath("$." + ENTITY_ROOT_NAME_LIST_COUNT)
                        .value(42))
                // The page size is set to 10
                .andExpect(jsonPath("$.results", hasSize(42)))
                .andExpect(jsonPath("$._links.['"
                                + SELF + "']." + HREF,
                        endsWith("mappe?$filter=contains(tittel,%20'test')" +
                                "&$top=100")))
                .andExpect(jsonPath("$._links.['"
                        + PREVIOUS + "']").doesNotExist())
                .andExpect(jsonPath("$._links.['"
                        + NEXT + "']").doesNotExist());
    }

    /**
     * Get all File objects. This should produce the following result:
     * Note: There are 42 File objects in the database.
     * 39 from pagination sql and 3 from basic_structure.sql
     * {
     * "count" : 42
     * "results" : [
     * left out for brevity
     * ],
     * "_links":
     * {
     * "self":
     * {
     * "href":
     * "http://localhost:8092/noark5v5/odata/api/arkivstruktur/mappe?$filter=contains(tittel,%20'test')"
     * }
     * }
     * }
     * Note: The value for top should not be changed from 1003 til 1000
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/pagination.sql"})
    @WithMockCustomUser
    public void getAllFilesWithTopAboveMaxCheckPaginationCorrect()
            throws Exception {
        String urlAllFile = contextPath + "/odata/" + HREF_BASE_FILE +
                "?$filter=contains(tittel, 'test')&$top=1003";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlAllFile)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());

        resultActions
                .andExpect(status().isOk())
                // 42 is the number of File objects declared between
                // basic_structure.sql and pagination.sql
                .andExpect(jsonPath("$." + ENTITY_ROOT_NAME_LIST_COUNT)
                        .value(42))
                // The page size is set to 10
                .andExpect(jsonPath("$.results", hasSize(42)))
                .andExpect(jsonPath("$._links.['"
                                + SELF + "']." + HREF,
                        endsWith("mappe?$filter=contains(tittel,%20'test')" +
                                "&$top=1003")))
                .andExpect(jsonPath("$._links.['"
                        + PREVIOUS + "']").doesNotExist())
                .andExpect(jsonPath("$._links.['"
                        + NEXT + "']").doesNotExist());
    }

    /**
     * Get all File objects. This should produce the following result:
     * Note: There are 42 File objects in the database. 37 from pagination
     * .sql and 5 from basic_structure.sql
     * {
     * "count" : 42
     * "results" : [
     * left out for brevity
     * ],
     * "_links" : {
     * <p>
     * }
     * }
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/pagination.sql"})
    @WithMockCustomUser
    public void getAllFilesSecondPageCheckPaginationCorrect()
            throws Exception {
        String urlAllFile = contextPath + "/odata/" + HREF_BASE_FILE +
                "?$filter=contains(tittel, 'test')" +
                "&$top=10&$skip=10&$orderby=title";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlAllFile)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());
        resultActions.andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                // 42 is the number of File objects declared between
                // basic_structure.sql and pagination.sql
                .andExpect(jsonPath("$." + ENTITY_ROOT_NAME_LIST_COUNT)
                        .value(42))
                // The page size is set to 10
                .andExpect(jsonPath("$.results", hasSize(10)));
    }

    /**
     * Get all File objects. This should produce the following result:
     * <p>
     * {
     * "count" : 5
     * "results" : [
     * left out for brevity
     * ],
     * "_links":
     * {
     * "self":
     * {
     * "href":"http://localhost:8092/noark5v5/odata/api/arkivstruktur/mappe?$filter=contains(description,%20'apple')&$top=10&$orderby=title"
     * }
     * }
     * <p>
     * Note: There are 5 File objects in the database that match the query.
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/pagination.sql"})
    @WithMockCustomUser
    public void getSubsetFilesCheckPaginationCorrect()
            throws Exception {
        String urlAllFile = contextPath + "/odata/" + HREF_BASE_FILE +
                "?$filter=contains(description, 'apple')" +
                "&$top=10&$orderby=title";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlAllFile)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());
        resultActions.andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + ENTITY_ROOT_NAME_LIST_COUNT)
                        .value(5))
                // The page size is set to 10
                .andExpect(jsonPath("$.results", hasSize(5)))
                .andExpect(jsonPath("$._links.['"
                                + SELF + "']." + HREF,
                        endsWith("mappe?$filter=contains(description,%20'apple')&$top=10&$orderby=title")))
                .andExpect(jsonPath("$._links.['"
                        + NEXT + "']").doesNotExist())
                .andExpect(jsonPath("$._links.['"
                        + PREVIOUS + "']").doesNotExist());
    }
}
