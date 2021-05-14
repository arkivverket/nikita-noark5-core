package utils;

import org.springframework.test.web.servlet.ResultActions;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static utils.Validator.*;


public final class CrossReferenceValidator {

    public static void validateCrossReference(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + FROM_SYSTEM_ID)
                        .exists())
                .andExpect(jsonPath("$." + TO_SYSTEM_ID)
                        .exists())
                .andExpect(jsonPath("$." + REFERENCE_TYPE)
                        .exists());
        validateSELFLink(REL_FONDS_STRUCTURE_CROSS_REFERENCE, resultActions);
    }

    public static void validateCrossReferenceFromClassToClass(ResultActions resultActions)
            throws Exception {
        resultActions.andExpect(jsonPath("$." + REFERENCE_TYPE)
                .value(REFERENCE_TO_CLASS));
        validateCrossReference(resultActions);
        validateClassLink(resultActions);
    }

    public static void validateUpdatedCrossReferenceForClass(
            ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + FROM_SYSTEM_ID)
                        .value("06ae4940-3d86-4a69-a59f-f1a27b26f2a8"))
                .andExpect(jsonPath("$." + TO_SYSTEM_ID)
                        .value("d6b0d4ba-3b2d-4896-84fa-5d9ba1756cf4"))
                .andExpect(jsonPath("$." + REFERENCE_TYPE)
                        .value(REFERENCE_TO_CLASS));
        validateCrossReference(resultActions);
        validateClassLink(resultActions);
    }

    public static void validateCrossReferenceFromFileToFile(ResultActions resultActions)
            throws Exception {
        resultActions.andExpect(jsonPath("$." + REFERENCE_TYPE)
                .value(REFERENCE_TO_FILE));
        validateCrossReference(resultActions);
        validateFileLink(resultActions);
    }

    public static void validateUpdatedCrossReferenceForFile(
            ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + FROM_SYSTEM_ID)
                        .value("7e857df9-d453-48bf-ac4c-19beebf81091"))
                .andExpect(jsonPath("$." + TO_SYSTEM_ID)
                        .value("01d0ef0b-b043-457e-8a8b-4ac2128a6637"))
                .andExpect(jsonPath("$." + REFERENCE_TYPE)
                        .value(REFERENCE_TO_FILE));
        validateCrossReference(resultActions);
        validateFileLink(resultActions);
    }

    public static void validateCrossReferenceForRecord(ResultActions resultActions)
            throws Exception {
        resultActions.andExpect(jsonPath("$." + REFERENCE_TYPE)
                .value(REFERENCE_TO_REGISTRATION));
        validateCrossReference(resultActions);
        validateRecordLink(resultActions);
    }

    public static void validateUpdatedCrossReferenceFromRecordToRecord(
            ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + FROM_SYSTEM_ID)
                        .value("ba848de1-e1d7-42fe-9ea1-77127db206fd"))
                .andExpect(jsonPath("$." + TO_SYSTEM_ID)
                        .value("bde603e8-ed29-43a3-8b38-52b796ca0b73"))
                .andExpect(jsonPath("$." + REFERENCE_TYPE)
                        .value(REFERENCE_TO_REGISTRATION));
        validateCrossReference(resultActions);
        validateRecordLink(resultActions);
    }

    public static void validateUpdatedCrossReferenceFromFileToFile(
            ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + FROM_SYSTEM_ID)
                        .value("7e857df9-d453-48bf-ac4c-19beebf81091"))
                .andExpect(jsonPath("$." + TO_SYSTEM_ID)
                        .value("01d0ef0b-b043-457e-8a8b-4ac2128a6637"))
                .andExpect(jsonPath("$." + REFERENCE_TYPE)
                        .value(REFERENCE_TO_FILE));
        validateCrossReference(resultActions);
        validateFileLink(resultActions);
    }

    public static void validateCrossReferenceFromRecordToRecord(
            ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + FROM_SYSTEM_ID)
                        .value("ba848de1-e1d7-42fe-9ea1-77127db206fd"))
                .andExpect(jsonPath("$." + TO_SYSTEM_ID)
                        .value("92e53dff-bd83-4a39-8485-9e34942a1583"))
                .andExpect(jsonPath("$." + REFERENCE_TYPE)
                        .value(REFERENCE_TO_REGISTRATION));
        validateCrossReference(resultActions);
        validateRecordLink(resultActions);
    }

    public static void validateCrossReferenceFromRecordToFile(
            ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + FROM_SYSTEM_ID)
                        .value("ba848de1-e1d7-42fe-9ea1-77127db206fd"))
                .andExpect(jsonPath("$." + TO_SYSTEM_ID)
                        .value("b7da0a57-2983-4389-a65e-d87343da78eb"))
                .andExpect(jsonPath("$." + REFERENCE_TYPE)
                        .value(REFERENCE_TO_FILE));
        validateCrossReference(resultActions);
        validateRecordLink(resultActions);
    }

    public static void validateCrossReferenceFromFileToRecord(
            ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + FROM_SYSTEM_ID)
                        .value("7e857df9-d453-48bf-ac4c-19beebf81091"))
                .andExpect(jsonPath("$." + TO_SYSTEM_ID)
                        .value("bde603e8-ed29-43a3-8b38-52b796ca0b73"))
                .andExpect(jsonPath("$." + REFERENCE_TYPE)
                        .value(REFERENCE_TO_REGISTRATION));
        validateCrossReference(resultActions);
        validateFileLink(resultActions);
    }
}
