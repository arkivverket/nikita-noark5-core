package utils;

import org.springframework.test.web.servlet.ResultActions;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.HREF;
import static nikita.common.config.N5ResourceMappings.KEYWORD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static utils.TestConstants.KEYWORD_TEST;
import static utils.TestConstants.KEYWORD_TEST_UPDATED;
import static utils.Validator.validateSELFLink;


public final class KeywordValidator {

    /**
     * @param resultActions with request result
     * @throws Exception if there is a problem
     */
    public static void validateKeyword(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + KEYWORD).value(KEYWORD_TEST));
        validateSELFLink(REL_FONDS_STRUCTURE_KEYWORD, resultActions);
    }

    /**
     * @param resultActions with request result
     * @throws Exception if there is a problem
     */
    public static void validateUpdatedKeyword(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + KEYWORD)
                        .value(KEYWORD_TEST_UPDATED));
        validateSELFLink(REL_FONDS_STRUCTURE_KEYWORD, resultActions);
    }

    /**
     * @param resultActions with request result
     * @throws Exception if there is a problem
     */
    public static void validateKeywordForFile(
            ResultActions resultActions)
            throws Exception {
        validateKeyword(resultActions);
        validateFileLink(resultActions);
    }

    /**
     * @param resultActions with request result
     * @throws Exception if there is a problem
     */
    public static void validateKeywordForClass(
            ResultActions resultActions)
            throws Exception {
        validateKeyword(resultActions);
        validateClassLink(resultActions);
    }

    /**
     * @param resultActions with request result
     * @throws Exception if there is a problem
     */
    public static void validateKeywordForRecord(
            ResultActions resultActions)
            throws Exception {
        validateKeyword(resultActions);
        validateRecordLink(resultActions);
    }

    public static void validateFileLink(
            ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$._links['" +
                        REL_FONDS_STRUCTURE_FILE +
                        "']['" + HREF + "']").exists());
    }

    public static void validateClassLink(
            ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$._links['" +
                        REL_FONDS_STRUCTURE_CLASS +
                        "']['" + HREF + "']").exists());
    }

    public static void validateRecordLink(
            ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$._links['" +
                        REL_FONDS_STRUCTURE_RECORD +
                        "']['" + HREF + "']").exists());
    }
}
