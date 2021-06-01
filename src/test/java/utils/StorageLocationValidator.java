package utils;

import org.springframework.test.web.servlet.ResultActions;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.HREF;
import static nikita.common.config.N5ResourceMappings.STORAGE_LOCATION;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static utils.TestConstants.STORAGE_LOCATION_TEST;
import static utils.TestConstants.STORAGE_LOCATION_TEST_UPDATED;
import static utils.Validator.validateSELFLink;


public final class StorageLocationValidator {

    /**
     * @param resultActions with request result
     * @throws Exception if there is a problem
     */
    public static void validateStorageLocation(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + STORAGE_LOCATION)
                        .value(STORAGE_LOCATION_TEST));
        validateSELFLink(REL_FONDS_STRUCTURE_STORAGE_LOCATION, resultActions);
    }

    /**
     * @param resultActions with request result
     * @throws Exception if there is a problem
     */
    public static void validateUpdatedStorageLocation(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + STORAGE_LOCATION)
                        .value(STORAGE_LOCATION_TEST_UPDATED));
        validateSELFLink(REL_FONDS_STRUCTURE_STORAGE_LOCATION, resultActions);
    }

    /**
     * @param resultActions with request result
     * @throws Exception if there is a problem
     */
    public static void validateStorageLocationForDocumentDescription(
            ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + STORAGE_LOCATION)
                        .value(STORAGE_LOCATION_TEST));
    }

    /**
     * @param resultActions with request result
     * @throws Exception if there is a problem
     */
    public static void validateStorageLocationForFile(
            ResultActions resultActions)
            throws Exception {
        validateStorageLocation(resultActions);
        validateFileLink(resultActions);
    }

    /**
     * @param resultActions with request result
     * @throws Exception if there is a problem
     */
    public static void validateStorageLocationForSeries(
            ResultActions resultActions)
            throws Exception {
        validateStorageLocation(resultActions);
        validateSeriesLink(resultActions);
    }

    /**
     * @param resultActions with request result
     * @throws Exception if there is a problem
     */
    public static void validateStorageLocationForRecord(
            ResultActions resultActions)
            throws Exception {
        validateStorageLocation(resultActions);
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

    public static void validateSeriesLink(
            ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$._links['" +
                        REL_FONDS_STRUCTURE_SERIES +
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
