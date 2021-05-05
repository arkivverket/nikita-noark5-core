package utils;

import org.springframework.test.web.servlet.ResultActions;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.HREF;
import static nikita.common.config.N5ResourceMappings.AUTHOR;
import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static utils.TestConstants.AUTHOR_TEST;
import static utils.TestConstants.AUTHOR_TEST_UPDATED;
import static utils.Validator.validateSELFLink;


public final class AuthorValidator {

    /**
     * @param resultActions with request result
     * @throws Exception if there is a problem
     */
    public static void validateAuthor(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + AUTHOR).value(AUTHOR_TEST));
        validateSELFLink(REL_FONDS_STRUCTURE_AUTHOR, resultActions);
    }

    /**
     * @param resultActions with request result
     * @throws Exception if there is a problem
     */
    public static void validateUpdatedAuthor(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + AUTHOR)
                        .value(AUTHOR_TEST_UPDATED));
        validateSELFLink(REL_FONDS_STRUCTURE_AUTHOR, resultActions);
    }

    /**
     * @param resultActions with request result
     * @throws Exception if there is a problem
     */
    public static void validateAuthorForDocumentDescription(
            ResultActions resultActions)
            throws Exception {
        validateAuthor(resultActions);
        validateDocumentDescriptionLink(resultActions);
    }

    /**
     * @param resultActions with request result
     * @throws Exception if there is a problem
     */
    public static void validateAuthorForRecord(
            ResultActions resultActions)
            throws Exception {
        validateAuthor(resultActions);
        validateRecordLink(resultActions);
    }

    public static void validateDocumentDescriptionLink(
            ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$._links['" +
                                REL_FONDS_STRUCTURE_DOCUMENT_DESCRIPTION +
                                "']['" + HREF + "']",
                        endsWith(HREF_BASE_DOCUMENT_DESCRIPTION + SLASH +
                                "66b92e78-b75d-4b0f-9558-4204ab31c2d1")));
    }

    public static void validateRecordLink(
            ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$._links['" +
                                REL_FONDS_STRUCTURE_RECORD +
                                "']['" + HREF + "']",
                        endsWith(HREF_BASE_RECORD + SLASH +
                                "dc600862-3298-4ec0-8541-3e51fb900054")));
    }
}
