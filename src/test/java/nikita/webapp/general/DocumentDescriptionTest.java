package nikita.webapp.general;

import nikita.webapp.spring.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static nikita.common.config.Constants.NOARK5_V5_CONTENT_TYPE_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.DocumentDescriptionCreator.createDocumentDescriptionAsJSON;
import static utils.DocumentDescriptionValidator.validateDocumentDescription;
import static utils.DocumentDescriptionValidator.validateDocumentDescriptionTemplate;

public class DocumentDescriptionTest
        extends BaseTest {

    /**
     * Check that it is possible to create a DocumentDescription
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql("/db-tests/basic_structure.sql")
    @WithMockCustomUser
    public void addAuthorWhenCreatingDocumentDescription() throws Exception {
        // First get template to create / POST DocumentDescription
        String urlNewDocumentDescription = "/noark5v5/api/arkivstruktur/registrering" +
                "/dc600862-3298-4ec0-8541-3e51fb900054/ny-dokumentbeskrivelse";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlNewDocumentDescription)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        validateDocumentDescriptionTemplate(resultActions);

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        // Create a JSON object to POST
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewDocumentDescription)
                .contextPath("/noark5v5")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createDocumentDescriptionAsJSON()));

        resultActions.andExpect(status().isCreated());
        validateDocumentDescription(resultActions);

        resultActions.andDo(document("home",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }
}
