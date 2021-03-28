package utils;

import org.springframework.test.web.servlet.ResultActions;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static utils.TestConstants.*;

public final class NationalIdentifierValidator {

    public static void validateBuilding(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + SYSTEM_ID).exists())
                .andExpect(jsonPath("$." + BUILDING_NUMBER)
                        .value(BUILDING_NUMBER_VALUE))
                .andExpect(jsonPath("$." + BUILDING_CHANGE_NUMBER)
                        .value(BUILDING_CHANGE_NUMBER_VALUE));
    }

    public static void validateBuildingLinks(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$._links" + REL_FONDS_STRUCTURE_PLAN)
                        .exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    public static void validatePlan(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + SYSTEM_ID).exists())
                .andExpect(jsonPath("$." + PLAN_IDENTIFICATION)
                        .value(PLAN_IDENTIFICATION_VALUE))
                .andExpect(jsonPath("$." + COUNTY_NUMBER)
                        .value(PLAN_COUNTY_NUMBER_VALUE))
                .andExpect(jsonPath("$." + MUNICIPALITY_NUMBER)
                        .value(PLAN_MUNICIPALITY_NUMBER_VALUE))
                .andExpect(jsonPath("$." + COUNTRY_CODE + "." + CODE)
                        .value(PLAN_COUNTRY_CODE_VALUE))
                .andExpect(jsonPath("$." + COUNTRY_CODE + "." + CODE_NAME)
                        .value(PLAN_COUNTRY_CODE_NAME_VALUE));
    }

    public static void validatePlanLinks(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$._links" + REL_FONDS_STRUCTURE_PLAN)
                        .exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    public static void validatePosition(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + SYSTEM_ID).exists())
                .andExpect(jsonPath("$." + X)
                        .value(POSITION_X_VALUE))
                .andExpect(jsonPath("$." + Y)
                        .value(POSITION_Y_VALUE))
                .andExpect(jsonPath("$." + Z)
                        .value(POSITION_Z_VALUE))
                .andExpect(jsonPath("$." + COORDINATE_SYSTEM + "." + CODE)
                        .value(POSITION_CODE_VALUE))
                .andExpect(jsonPath("$." + COORDINATE_SYSTEM + "." + CODE_NAME)
                        .value(POSITION_CODE_NAME_VALUE));
    }

    public static void validatePositionLinks(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$._links" +
                        REL_FONDS_STRUCTURE_POSITION)
                        .exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    public static void validateUnit(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + SYSTEM_ID).exists())
                .andExpect(jsonPath("$." + ORGANISATION_NUMBER)
                        .value(ORGANISATION_NUMBER_VALUE));
    }

    public static void validateUnitLinks(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$._links" +
                        REL_FONDS_STRUCTURE_NI_UNIT).exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    public static void validateDNumber(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + SYSTEM_ID).exists())
                .andExpect(jsonPath("$." + D_NUMBER_FIELD)
                        .value(D_NUMBER_VALUE));
    }

    public static void validateDNumberLinks(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$._links" +
                        REL_FONDS_STRUCTURE_D_NUMBER).exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    public static void validateSocialSecurityNumber(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + SYSTEM_ID).exists())
                .andExpect(jsonPath("$." + SOCIAL_SECURITY_NUMBER)
                        .value(SS_NUMBER_VALUE));
    }

    public static void validateSocialSecurityNumberLinks(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$._links" +
                        REL_FONDS_STRUCTURE_SOCIAL_SECURITY_NUMBER)
                        .exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    public static void validateCadastralUnit(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + SYSTEM_ID).exists())
                .andExpect(jsonPath("$." + MUNICIPALITY_NUMBER)
                        .value(MUNICIPALITY_NUMBER_VALUE))
                .andExpect(jsonPath("$." + HOLDING_NUMBER)
                        .value(HOLDING_NUMBER_VALUE))
                .andExpect(jsonPath("$." + SUB_HOLDING_NUMBER)
                        .value(SUB_HOLDING_NUMBER_VALUE))
                .andExpect(jsonPath("$." + LEASE_NUMBER)
                        .value(LEASE_NUMBER_VALUE))
                .andExpect(jsonPath("$." + SECTION_NUMBER)
                        .value(SECTION_NUMBER_VALUE));
    }

    public static void validateCadastralUnitLinks(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$._links" +
                        REL_FONDS_STRUCTURE_CADASTRAL_UNIT)
                        .exists())
                .andExpect(jsonPath("$._links.self").exists());
    }
}
