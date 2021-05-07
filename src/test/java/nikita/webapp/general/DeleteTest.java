package nikita.webapp.general;

import nikita.webapp.spring.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static nikita.common.config.Constants.NOARK5_V5_CONTENT_TYPE_JSON;
import static nikita.common.config.Constants.REL_CASE_HANDLING_PRECEDENCE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteTest
        extends BaseTest {

    /**
     * Check that it is possible to delete a precedence associated with a
     * RegistryEntry
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/precedence.sql"})
    @WithMockCustomUser
    public void deletePrecedenceWithRegistryEntry() throws Exception {
        String urlRegistryEntry = "/noark5v5/api/sakarkiv/" +
                "journalpost/16c2f0af-c684-49e8-bc4f-bc21de1578bb/";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlRegistryEntry)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to delete a precedence associated with a
     * CaseFile
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/precedence.sql"})
    @WithMockCustomUser
    public void deletePrecedenceWithCaseFile() throws Exception {
        String urlCaseFile = "/noark5v5/api/sakarkiv/" +
                "saksmappe/ccefaca8-4eda-4164-84c8-4f2176312f29/";
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlCaseFile)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to delete a precedence by its systemID and that
     * the related caseFile has no associated precedence afterwards
     * <p>
     * 1. Get associated CaseFile, make sure Precedence rel/href is in _links
     * 2. Delete Precedence using it's systemID
     * 3. Get previous associated CaseFile, make sure Precedence rel/href is
     * not in _links
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/precedence.sql"})
    @WithMockCustomUser
    public void deletePrecedenceObjectWithCaseFile() throws Exception {

        // 1. Get associated CaseFile, make sure Precedence rel/href is in _links
        String urlCaseFile = contextPath + "/api/sakarkiv/" +
                "saksmappe/ccefaca8-4eda-4164-84c8-4f2176312f29/";
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlCaseFile)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$._links.['" + REL_CASE_HANDLING_PRECEDENCE + "']")
                        .exists());

        // 2. Delete Precedence using it's systemID
        String urlPrecedence = contextPath + "/api/sakarkiv/" +
                "presedens/e23cc0fe-d03f-4d6b-87e9-898deeb7d7da/";
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlPrecedence)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);

        // 3. Get previous associated CaseFile, make sure Precedence rel/href
        // is not in _links
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlCaseFile)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$._links.['" + REL_CASE_HANDLING_PRECEDENCE + "']")
                        .doesNotExist());
    }

    /**
     * Check that it is possible to delete a precedence by its systemID and that
     * the related registryEntry has no associated precedence afterwards
     * <p>
     * 1. Get associated RegistryEntry, make sure Precedence rel/href is in _links
     * 2. Delete Precedence using it's systemID
     * 3. Get previous associated RegistryEntry, make sure Precedence rel/href is
     * not in _links
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/precedence.sql"})
    @WithMockCustomUser
    public void deletePrecedenceObjectWithRegistryEntry() throws Exception {

        // 1. Get associated RegistryEntry, make sure Precedence rel/href is in _links
        String urlRegistryEntry = contextPath + "/api/sakarkiv/" +
                "journalpost/16c2f0af-c684-49e8-bc4f-bc21de1578bb/";
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlRegistryEntry)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$._links.['" + REL_CASE_HANDLING_PRECEDENCE + "']")
                        .exists());

        // 2. Delete Precedence using it's systemID
        String urlPrecedence = contextPath + "/api/sakarkiv/" +
                "presedens/137dbc34-5669-4fdf-867e-985c3f1de60f/";
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlPrecedence)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);

        // 3. Get previous associated RegistryEntry, make sure Precedence rel/href
        // is not in _links
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlRegistryEntry)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$._links.['" + REL_CASE_HANDLING_PRECEDENCE + "']")
                        .doesNotExist());
    }

    /**
     * Check that it is possible to delete a DocumentFlow associated with a
     * RegistryEntry
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/document_flow.sql"})
    @WithMockCustomUser
    public void deleteDocumentFlowWithRegistryEntry() throws Exception {
        String urlRegistryEntry = "/noark5v5/api/sakarkiv/" +
                "journalpost/8f6b084f-d727-4b46-bbe2-14bed2135fa9/";
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlRegistryEntry)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to delete a DocumentFlow associated with a
     * RecordNote
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/document_flow.sql"})
    @WithMockCustomUser
    public void deleteDocumentFlowWithRecordNote() throws Exception {
        String urlRecordNote = "/noark5v5/api/sakarkiv/" +
                "arkivnotat/11b32a9e-802d-43de-9bb5-c951e3bbe95b";
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlRecordNote)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);
    }
}
