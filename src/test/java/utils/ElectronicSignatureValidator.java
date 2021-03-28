package utils;

import org.springframework.test.web.servlet.ResultActions;

import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static utils.TestConstants.*;


public final class ElectronicSignatureValidator {

    /**
     * @param resultActions
     * @throws Exception
     */
    public static void validateElectronicSignature(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + ELECTRONIC_SIGNATURE +
                        "." + ELECTRONIC_SIGNATURE_VERIFIED_DATE)
                        .value(VERIFIED_DATE_VALUE))
                .andExpect(jsonPath("$." + ELECTRONIC_SIGNATURE +
                        "." + ELECTRONIC_SIGNATURE_VERIFIED_BY)
                        .value(VERIFIED_BY_VALUE))
                .andExpect(jsonPath("$." + ELECTRONIC_SIGNATURE +
                        "." + ELECTRONIC_SIGNATURE_SECURITY_LEVEL + "." + CODE)
                        .value(VERIFIED_LEVEL_CODE_VALUE))
                .andExpect(jsonPath("$." + ELECTRONIC_SIGNATURE +
                        "." + ELECTRONIC_SIGNATURE_SECURITY_LEVEL + "." +
                        CODE_NAME)
                        .value(VERIFIED_LEVEL_CODE_NAME_VALUE))
                .andExpect(jsonPath("$." + ELECTRONIC_SIGNATURE +
                        "." + ELECTRONIC_SIGNATURE_VERIFIED + "." + CODE)
                        .value(VERIFIED_CODE_VALUE))
                .andExpect(jsonPath("$." + ELECTRONIC_SIGNATURE +
                        "." + ELECTRONIC_SIGNATURE_VERIFIED + "." + CODE_NAME)
                        .value(VERIFIED_CODE_NAME_VALUE));
    }
}
