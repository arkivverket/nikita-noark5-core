package utils;

import org.springframework.test.web.servlet.ResultActions;

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
}
