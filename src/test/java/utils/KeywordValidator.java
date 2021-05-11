package utils;

import org.springframework.test.web.servlet.ResultActions;

import static nikita.common.config.Constants.REL_FONDS_STRUCTURE_KEYWORD;
import static nikita.common.config.N5ResourceMappings.KEYWORD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static utils.TestConstants.KEYWORD_TEST;
import static utils.TestConstants.KEYWORD_TEST_UPDATED;
import static utils.Validator.*;


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
}
