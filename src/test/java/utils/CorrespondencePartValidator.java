package utils;

import org.springframework.test.web.servlet.ResultActions;

import static nikita.common.config.Constants.*;
import static nikita.common.config.MetadataConstants.CORRESPONDENCE_PART_CODE_EA;
import static nikita.common.config.MetadataConstants.CORRESPONDENCE_PART_DESCRIPTION_EA;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static utils.TestConstants.*;


public final class CorrespondencePartValidator {

    /**
     * @param resultActions
     * @throws Exception
     */
    public static void validateCorrespondencePartUnit(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + CORRESPONDENCE_PART_TYPE +
                        "." + CODE).value(CORRESPONDENCE_PART_CODE_EA))
                .andExpect(jsonPath("$." + CORRESPONDENCE_PART_TYPE +
                        "." + CODE_NAME)
                        .value(CORRESPONDENCE_PART_DESCRIPTION_EA))
                .andExpect(jsonPath("$." + CONTACT_PERSON)
                        .value(CONTACT_NAME_TEST))
                .andExpect(jsonPath("$." + NAME)
                        .value(NAME_TEST))
                .andExpect(jsonPath("$." + UNIT_IDENTIFIER + "." +
                        ORGANISATION_NUMBER).value(UNIT_NUMBER_TEST))
                .andExpect(jsonPath("$." + POSTAL_ADDRESS + "." +
                        ADDRESS_LINE_1).value(POSTAL_ADDRESS_LINE_1_TEST))
                .andExpect(jsonPath("$." + POSTAL_ADDRESS + "." +
                        ADDRESS_LINE_2).value(POSTAL_ADDRESS_LINE_2_TEST))
                .andExpect(jsonPath("$." + POSTAL_ADDRESS + "." +
                        ADDRESS_LINE_3).value(POSTAL_ADDRESS_LINE_3_TEST))
                .andExpect(jsonPath("$." + POSTAL_ADDRESS + "." +
                        POSTAL_NUMBER).value(POSTAL_POSTAL_CODE_TEST))
                .andExpect(jsonPath("$." + POSTAL_ADDRESS + "." +
                        POSTAL_TOWN).value(POSTAL_POSTAL_TOWN_TEST))
                .andExpect(jsonPath("$." + POSTAL_ADDRESS + "." +
                        COUNTRY_CODE).value(COUNTRY_CODE_TEST))
                .andExpect(jsonPath("$." + BUSINESS_ADDRESS + "." +
                        ADDRESS_LINE_1).value(BUSINESS_ADDRESS_LINE_1_TEST))
                .andExpect(jsonPath("$." + BUSINESS_ADDRESS + "." +
                        ADDRESS_LINE_2).value(BUSINESS_ADDRESS_LINE_2_TEST))
                .andExpect(jsonPath("$." + BUSINESS_ADDRESS + "." +
                        ADDRESS_LINE_3).value(BUSINESS_ADDRESS_LINE_3_TEST))
                .andExpect(jsonPath("$." + BUSINESS_ADDRESS + "." +
                        POSTAL_NUMBER).value(BUSINESS_POSTAL_CODE_TEST))
                .andExpect(jsonPath("$." + BUSINESS_ADDRESS + "." +
                        POSTAL_TOWN).value(BUSINESS_POSTAL_TOWN_TEST))
                .andExpect(jsonPath("$." + BUSINESS_ADDRESS + "." +
                        COUNTRY_CODE).value(COUNTRY_CODE_TEST))
                .andExpect(jsonPath("$." + CONTACT_INFORMATION + "." +
                        TELEPHONE_NUMBER).value(TELEPHONE_NUMBER_TEST))
                .andExpect(jsonPath("$." + CONTACT_INFORMATION + "." +
                        EMAIL_ADDRESS).value(EMAIL_ADDRESS_TEST))
                .andExpect(jsonPath("$." + CONTACT_INFORMATION + "." +
                        MOBILE_TELEPHONE_NUMBER).value(MOBILE_NUMBER_TEST));
    }

    public static void validateCorrespondencePartUnitLink(
            ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.['" +
                        REL_FONDS_STRUCTURE_CORRESPONDENCE_PART_UNIT + "']")
                        .exists())
                .andExpect(jsonPath("$._links.['" +
                        REL_METADATA_CORRESPONDENCE_PART_TYPE + "']")
                        .exists())
                .andExpect(jsonPath("$._links.['" +
                        REL_FONDS_STRUCTURE_RECORD + "']")
                        .exists());
    }

    public static void validateCorrespondencePartPerson(
            ResultActions resultActions) throws Exception {
        validatePersonIdentifier(resultActions);
        resultActions
                .andExpect(jsonPath("$." + CORRESPONDENCE_PART_TYPE
                        + "." + CODE).value(CORRESPONDENCE_PART_CODE_EA))
                .andExpect(jsonPath("$." + CORRESPONDENCE_PART_TYPE
                        + "." + CODE_NAME)
                        .value(CORRESPONDENCE_PART_DESCRIPTION_EA));
        validateContactInformation(resultActions);
        validatePostAddress(resultActions);
        validateResidingAddress(resultActions);
    }

    public static void validatePostAddress(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." +
                        POSTAL_ADDRESS + "." + ADDRESS_LINE_1)
                        .value(POSTAL_ADDRESS_LINE_1_TEST))
                .andExpect(jsonPath("$." +
                        POSTAL_ADDRESS + "." + ADDRESS_LINE_2)
                        .value(POSTAL_ADDRESS_LINE_2_TEST))
                .andExpect(jsonPath("$." +
                        POSTAL_ADDRESS + "." + ADDRESS_LINE_3)
                        .value(POSTAL_ADDRESS_LINE_3_TEST))
                .andExpect(jsonPath("$." +
                        POSTAL_ADDRESS + "." + POSTAL_NUMBER)
                        .value(POSTAL_POSTAL_CODE_TEST))
                .andExpect(jsonPath("$." +
                        POSTAL_ADDRESS + "." + POSTAL_TOWN)
                        .value(POSTAL_POSTAL_TOWN_TEST));
    }

    public static void validateResidingAddress(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." +
                        RESIDING_ADDRESS + "." + ADDRESS_LINE_1)
                        .value(RESIDING_ADDRESS_LINE_1_TEST))
                .andExpect(jsonPath("$." +
                        RESIDING_ADDRESS + "." + ADDRESS_LINE_2)
                        .value(RESIDING_ADDRESS_LINE_2_TEST))
                .andExpect(jsonPath("$." +
                        RESIDING_ADDRESS + "." + ADDRESS_LINE_3)
                        .value(RESIDING_ADDRESS_LINE_3_TEST))
                .andExpect(jsonPath("$." +
                        RESIDING_ADDRESS + "." + POSTAL_NUMBER)
                        .value(RESIDING_POSTAL_CODE_TEST))
                .andExpect(jsonPath("$." +
                        RESIDING_ADDRESS + "." + POSTAL_TOWN)
                        .value(RESIDING_POSTAL_TOWN_TEST));
    }

    public static void validateContactInformation(
            ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(jsonPath("$." +
                        CONTACT_INFORMATION + "." + EMAIL_ADDRESS)
                        .value(EMAIL_ADDRESS_TEST))
                .andExpect(jsonPath("$." +
                        CONTACT_INFORMATION + "." + MOBILE_TELEPHONE_NUMBER)
                        .value(MOBILE_NUMBER_TEST))
                .andExpect(jsonPath("$." +
                        CONTACT_INFORMATION + "." + TELEPHONE_NUMBER)
                        .value(TELEPHONE_NUMBER_TEST));
    }

    public static void validatePersonIdentifier(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + PERSON_IDENTIFIER + "." +
                        SOCIAL_SECURITY_NUMBER)
                        .value(SOCIAL_SECURITY_NUMBER_TEST))
                .andExpect(jsonPath("$." + PERSON_IDENTIFIER +
                        "." + D_NUMBER_FIELD).value(D_NUMBER_TEST));
    }
}
