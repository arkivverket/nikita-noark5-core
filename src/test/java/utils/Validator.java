package utils;

import org.springframework.test.web.servlet.ResultActions;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.HREF;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public final class Validator {

    public static void validateSELFLink(String rel,
                                        ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$._links.self")
                        .exists())
                .andExpect(jsonPath("$._links.['" + rel + "']")
                        .exists());
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
