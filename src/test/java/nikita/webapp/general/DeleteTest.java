package nikita.webapp.general;

import nikita.webapp.spring.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static nikita.common.config.Constants.NOARK5_V5_CONTENT_TYPE_JSON;
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
