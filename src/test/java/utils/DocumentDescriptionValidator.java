package utils;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.ResultActions;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.HREF;
import static nikita.common.config.N5ResourceMappings.*;
import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static utils.TestConstants.*;


public final class DocumentDescriptionValidator {

    /**
     * @param resultActions with request result
     * @throws Exception if there is a problem
     */
    public static void validateDocumentDescription(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + SYSTEM_ID).exists())
                .andExpect(jsonPath("$." + TITLE).value(TITLE_TEST))
                .andExpect(jsonPath("$." + STORAGE_LOCATION)
                        .value(STORAGE_LOCATION_TEST));
        // A test will expect the template content in addition to other values
        validateDocumentDescriptionTemplate(resultActions);
    }

    /**
     * @param resultActions with request result
     * @throws Exception if there is a problem
     */
    public static void validateUpdatedDocumentDescription(ResultActions resultActions)
            throws Exception {
        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());
        resultActions
                .andExpect(jsonPath("$." + SYSTEM_ID).exists())
                .andExpect(jsonPath("$." + TITLE)
                        .value(TITLE_TEST_UPDATED))
                .andExpect(jsonPath("$." + STORAGE_LOCATION)
                        .value(STORAGE_LOCATION_TEST_UPDATED));
        // A test will expect the template content in addition to other values
        validateDocumentDescriptionTemplate(resultActions);
    }

    /**
     * A DocumentDescription template should look like the following
     * {
     * "dokumenttype": {"kode":"B","kodenavn":"Brev"},
     * "dokumentstatus": {"kode":"F","kodenavn":"Dokumentet er ferdigstilt"},
     * "tilknyttetRegistreringSom":{"kode":"H","kodenavn":"Hoveddokument"},
     * "_links":{
     * "https://rel.arkivverket.no/noark5/v5/api/metadata/dokumentmedium/":{
     * "href":"http://localhost:8092/noark5v5/api/metadata/dokumentmedium"},
     * "https://rel.arkivverket.no/noark5/v5/api/metadata/dokumentstatus/":{
     * "href":"http://localhost:8092/noark5v5/api/metadata/dokumentstatus"},
     * "https://rel.arkivverket.no/noark5/v5/api/metadata/dokumenttype/":{
     * "href":"http://localhost:8092/noark5v5/api/metadata/dokumenttype"},
     * "https://rel.arkivverket.no/noark5/v5/api/metadata/skjermingmetadata/":{
     * "href":"http://localhost:8092/noark5v5/api/metadata/skjermingmetadata"}}}
     *
     * @param resultActions with subset of request result
     * @throws Exception if there is a problem
     */
    public static void validateDocumentDescriptionTemplate(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$." + DOCUMENT_TYPE +
                        "." + CODE).value(DOCUMENT_TYPE_CODE_TEST))
                .andExpect(jsonPath("$." + DOCUMENT_TYPE +
                        "." + CODE_NAME).value(DOCUMENT_TYPE_CODE_NAME_TEST))
                .andExpect(jsonPath("$." + DOCUMENT_STATUS +
                        "." + CODE).value(DOCUMENT_STATUS_CODE_TEST))
                .andExpect(jsonPath("$." + DOCUMENT_STATUS +
                        "." + CODE_NAME).value(DOCUMENT_STATUS_CODE_NAME_TEST))
                .andExpect(jsonPath("$." +
                        DOCUMENT_DESCRIPTION_ASSOCIATED_WITH_RECORD_AS +
                        "." + CODE).value(DOCUMENT_ASS_REC_CODE_TEST))
                .andExpect(jsonPath("$." +
                        DOCUMENT_DESCRIPTION_ASSOCIATED_WITH_RECORD_AS +
                        "." + CODE_NAME).value(DOCUMENT_ASS_REC_CODE_NAME_TEST));
        validateDocumentMediumLink(resultActions);
        validateDocumentStatusLink(resultActions);
        validateDocumentTypeLink(resultActions);
    }

    public static void validateDocumentMediumLink(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$._links['" +
                                REL_METADATA_DOCUMENT_MEDIUM + "']['" + HREF + "']",
                        endsWith(HREF_BASE_METADATA + SLASH + DOCUMENT_MEDIUM)));
    }

    public static void validateDocumentTypeLink(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$._links['" +
                                REL_METADATA_DOCUMENT_TYPE + "']['" + HREF + "']",
                        endsWith(HREF_BASE_METADATA + SLASH + DOCUMENT_TYPE)));
    }

    public static void validateDocumentStatusLink(ResultActions resultActions)
            throws Exception {
        resultActions
                .andExpect(jsonPath("$._links['" +
                                REL_METADATA_DOCUMENT_STATUS + "']['" + HREF + "']",
                        endsWith(HREF_BASE_METADATA + SLASH + DOCUMENT_STATUS)));
    }
}
