package nikita.webapp.general;

import nikita.webapp.spring.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static java.util.UUID.fromString;
import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.SELF;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.CrossReferenceCreator.createCrossReferenceAsJSON;
import static utils.CrossReferenceValidator.*;

/**
 * The tests in this class cover the following:
 * Create a Cross-reference from
 * - Class to Class
 * - File to Record
 * - File to File
 * - Record to File
 * - Record to Record
 * Update a Cross-reference from
 * - Record to File
 * - File to Record
 * Possible to delete an individual cross-reference
 * Possible to delete an entity that has a cross-reference. The cross-reference
 * is deleted due to cascade REMOVE
 * <p>
 * Error checking includes:
 * - Incoming payload refers to a different systemID than URL - throw 400
 * - Check not possible to add to non cross-reference object e.g., Fonds
 * throw 400
 * - Check not possible to add to non-existing toSystemID
 * - Check that it is not possible to create a CrossReference from Series /
 * DocumentDescription / Fonds to a CrossReference object
 * - Check that it is not possible to create duplicate cross references
 */

public class CrossReferenceTest
        extends BaseTest {

    /**
     * Check that it is possible to add a Cross-reference from one class to
     * another
     * {
     * "fraSystemID": "bd303a71-48e9-4c5d-9ee8-7df7d514d4e1",
     * "tilSystemID": "06ae4940-3d86-4a69-a59f-f1a27b26f2a8"
     * }
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/cross_reference.sql"})
    @WithMockCustomUser
    public void addCrossReferenceFromClassToClassUpdateToClass()
            throws Exception {
        UUID fromUUID = fromString("06ae4940-3d86-4a69-a59f-f1a27b26f2a8");
        UUID toUUID = fromString("bd303a71-48e9-4c5d-9ee8-7df7d514d4e1");
        UUID toUpdatedUUID = fromString("d6b0d4ba-3b2d-4896-84fa-5d9ba1756cf4");
        String urlNewCrossReference = contextPath + "/" + HREF_BASE_CLASS +
                "/" + fromUUID + "/" + NEW_CROSS_REFERENCE;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        // We do not do anything else with the result. Just make sure the
        // call works
        resultActions.andExpect(status().isOk());
        printDocumentation(resultActions);

        // Check that you can create the object and values are correct
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(
                        fromUUID, toUUID, REFERENCE_TO_CLASS)));
        resultActions.andExpect(status().isCreated());
        validateCrossReferenceFromClassToClass(resultActions);
        printDocumentation(resultActions);

        // Check that you can retrieve the object and values are correct
        String urlCrossReference = getHref(SELF, resultActions);
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        resultActions.andExpect(status().isOk());
        validateCrossReferenceFromClassToClass(resultActions);
        printDocumentation(resultActions);

        // Check that the object can be updated
        String etag = response.getHeader("ETAG");
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put(urlCrossReference)
                .contextPath(contextPath)
                .header("ETAG", etag)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(
                        fromUUID, toUpdatedUUID, REFERENCE_TO_CLASS)));
        resultActions.andExpect(status().isOk());
        validateUpdatedCrossReferenceForClass(resultActions);
        printDocumentation(resultActions);

        // Check that you can retrieve the updated object and values are correct
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk());
        validateUpdatedCrossReferenceForClass(resultActions);

        // Check that delete works
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to add a Cross-reference from one file to
     * another
     * {
     * "fraSystemID": "7e857df9-d453-48bf-ac4c-19beebf81091",
     * "tilSystemID": "b7da0a57-2983-4389-a65e-d87343da78eb"
     * }
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/cross_reference.sql"})
    @WithMockCustomUser
    public void addCrossReferenceFromFileToFileUpdateToFile()
            throws Exception {
        UUID fromUUID = fromString("7e857df9-d453-48bf-ac4c-19beebf81091");
        UUID toUUID = fromString("b7da0a57-2983-4389-a65e-d87343da78eb");
        UUID toUpdatedUUID = fromString("01d0ef0b-b043-457e-8a8b-4ac2128a6637");
        String urlNewCrossReference = contextPath + "/" + HREF_BASE_FILE +
                "/" + fromUUID + "/" + NEW_CROSS_REFERENCE;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        // We do not do anything else with the result. Just make sure the
        // call works
        resultActions.andExpect(status().isOk());
        printDocumentation(resultActions);

        // Check that you can create the object and values are correct
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(
                        fromUUID, toUUID, REFERENCE_TO_FILE)));
        resultActions.andExpect(status().isCreated());
        validateCrossReferenceFromFileToFile(resultActions);
        printDocumentation(resultActions);

        // Check that you can retrieve the object and values are correct
        String urlCrossReference = getHref(SELF, resultActions);
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        resultActions.andExpect(status().isOk());
        validateCrossReferenceFromFileToFile(resultActions);
        printDocumentation(resultActions);

        // Check that the object can be updated
        String etag = response.getHeader("ETAG");
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put(urlCrossReference)
                .contextPath(contextPath)
                .header("ETAG", etag)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(
                        fromUUID, toUpdatedUUID, REFERENCE_TO_FILE)));
        resultActions.andExpect(status().isOk());
        validateUpdatedCrossReferenceForFile(resultActions);
        printDocumentation(resultActions);

        // Check that you can retrieve the updated object and values are correct
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk());
        validateUpdatedCrossReferenceForFile(resultActions);

        // Check that delete works
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to add a Cross-reference from one record to
     * another
     * JSON payload. To create a CrossReference, POST a payload adhering to the
     * PATCH (RFC 6902) standard with "fraSystemID" containing the UUID of the
     * from-object, while "tilSystemID" contains the UUID of the to-object
     * <p>
     * {
     * "fraSystemID": "bd303a71-48e9-4c5d-9ee8-7df7d514d4e1",
     * "tilSystemID": "06ae4940-3d86-4a69-a59f-f1a27b26f2a8"
     * }
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/cross_reference.sql"})
    @WithMockCustomUser
    public void addCrossReferenceFromRecordToRecordUpdateToRecord()
            throws Exception {
        UUID fromUUID = fromString("ba848de1-e1d7-42fe-9ea1-77127db206fd");
        UUID toUUID = fromString("92e53dff-bd83-4a39-8485-9e34942a1583");
        UUID toUpdatedUUID = fromString("bde603e8-ed29-43a3-8b38-52b796ca0b73");
        String urlNewCrossReference = contextPath + "/" + HREF_BASE_RECORD +
                "/" + fromUUID + "/" + NEW_CROSS_REFERENCE;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        // We do not do anything else with the result. Just make sure the
        // call works
        resultActions.andExpect(status().isOk());
        printDocumentation(resultActions);

        // Check that you can create the object and values are correct
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(
                        fromUUID, toUUID, REFERENCE_TO_REGISTRATION)));
        resultActions.andExpect(status().isCreated());
        validateCrossReferenceForRecord(resultActions);
        printDocumentation(resultActions);

        // Check that you can retrieve the object and values are correct
        String urlCrossReference = getHref(SELF, resultActions);
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        resultActions.andExpect(status().isOk());
        validateCrossReferenceForRecord(resultActions);
        printDocumentation(resultActions);

        // Check that the object can be updated
        String etag = response.getHeader("ETAG");
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put(urlCrossReference)
                .contextPath(contextPath)
                .header("ETAG", etag)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(
                        fromUUID, toUpdatedUUID, REFERENCE_TO_REGISTRATION)));
        resultActions.andExpect(status().isOk());
        validateUpdatedCrossReferenceFromRecordToRecord(resultActions);
        printDocumentation(resultActions);

        // Check that you can retrieve the updated object and values are correct
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk());
        validateUpdatedCrossReferenceFromRecordToRecord(resultActions);

        // Check that delete works
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to add a Cross-reference from a Record to
     * a File and update the reference to a Record
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/cross_reference.sql"})
    @WithMockCustomUser
    public void addCrossReferenceFromRecordToFileUpdateToRecord()
            throws Exception {
        // This is a Record
        UUID fromUUID = fromString("ba848de1-e1d7-42fe-9ea1-77127db206fd");
        // This is a File
        UUID toUUID = fromString("b7da0a57-2983-4389-a65e-d87343da78eb");
        // This is a Record
        UUID toUpdatedUUID = fromString("bde603e8-ed29-43a3-8b38-52b796ca0b73");
        String urlNewCrossReference = contextPath + "/" + HREF_BASE_RECORD +
                "/" + fromUUID + "/" + NEW_CROSS_REFERENCE;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        // We do not do anything else with the result. Just make sure the
        // call works
        resultActions.andExpect(status().isOk());
        printDocumentation(resultActions);

        // Check that you can create the object and values are correct
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(
                        fromUUID, toUUID, REFERENCE_TO_FILE)));
        resultActions.andExpect(status().isCreated());
        validateCrossReferenceFromRecordToFile(resultActions);
        printDocumentation(resultActions);

        // Check that you can retrieve the object and values are correct
        String urlCrossReference = getHref(SELF, resultActions);
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        resultActions.andExpect(status().isOk());
        validateCrossReferenceFromRecordToFile(resultActions);

        // Check that the object can be updated
        String etag = response.getHeader("ETAG");
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put(urlCrossReference)
                .contextPath(contextPath)
                .header("ETAG", etag)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(
                        fromUUID, toUpdatedUUID, REFERENCE_TO_REGISTRATION)));
        resultActions.andExpect(status().isOk());
        validateUpdatedCrossReferenceFromRecordToRecord(resultActions);
        printDocumentation(resultActions);

        // Check that you can retrieve the updated object and values are correct
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk());
        validateUpdatedCrossReferenceFromRecordToRecord(resultActions);

        // Check that delete works
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to add a Cross-reference from a Record to
     * a File and update the reference to a Record
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/cross_reference.sql"})
    @WithMockCustomUser
    public void addCrossReferenceFromFileToRecordUpdateToFile()
            throws Exception {
        // This is a File
        UUID fromUUID = fromString("7e857df9-d453-48bf-ac4c-19beebf81091");
        // This is a Record
        UUID toUUID = fromString("bde603e8-ed29-43a3-8b38-52b796ca0b73");
        // This is a File
        UUID toUpdatedUUID = fromString("01d0ef0b-b043-457e-8a8b-4ac2128a6637");
        String urlNewCrossReference = contextPath + "/" + HREF_BASE_FILE +
                "/" + fromUUID + "/" + NEW_CROSS_REFERENCE;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        // We do not do anything else with the result. Just make sure the
        // call works
        resultActions.andExpect(status().isOk());
        printDocumentation(resultActions);

        // Check that you can create the object and values are correct
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(
                        fromUUID, toUUID, REFERENCE_TO_REGISTRATION)));
        resultActions.andExpect(status().isCreated());
        validateCrossReferenceFromFileToRecord(resultActions);
        printDocumentation(resultActions);

        // Check that you can retrieve the object and values are correct
        String urlCrossReference = getHref(SELF, resultActions);
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        MockHttpServletResponse response =
                resultActions.andReturn().getResponse();
        resultActions.andExpect(status().isOk());
        validateCrossReferenceFromFileToRecord(resultActions);

        // Check that the object can be updated
        String etag = response.getHeader("ETAG");
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put(urlCrossReference)
                .contextPath(contextPath)
                .header("ETAG", etag)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(
                        fromUUID, toUpdatedUUID, REFERENCE_TO_FILE)));
        resultActions.andExpect(status().isOk());
        validateUpdatedCrossReferenceFromFileToFile(resultActions);
        printDocumentation(resultActions);

        // Check that you can retrieve the updated object and values are correct
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk());
        validateUpdatedCrossReferenceFromFileToFile(resultActions);

        // Check that delete works
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to add a Cross-reference from a Record and
     * that it is possible to delete the Record. Checks for referential
     * integrity
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/cross_reference.sql"})
    @WithMockCustomUser
    public void addCrossReferenceFromRecordThenDeleteRecord()
            throws Exception {
        UUID fromUUID = fromString("ba848de1-e1d7-42fe-9ea1-77127db206fd");
        UUID toUUID = fromString("92e53dff-bd83-4a39-8485-9e34942a1583");

        String urlRecord = contextPath + "/" + HREF_BASE_RECORD +
                "/" + fromUUID + "/";
        String urlNewCrossReference = urlRecord + NEW_CROSS_REFERENCE;

        // Check that you can create the object and values are correct
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(
                        fromUUID, toUUID, REFERENCE_TO_CLASS)));
        resultActions.andExpect(status().isCreated());
        validateCrossReferenceFromRecordToRecord(resultActions);

        // Check that it is possible to delete the record
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlRecord)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to add a Cross-reference from a Class and
     * that it is possible to delete the Class. Checks for referential
     * integrity
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/cross_reference.sql"})
    @WithMockCustomUser
    public void addCrossReferenceFromClassThenDeleteClass()
            throws Exception {
        UUID fromUUID = fromString("06ae4940-3d86-4a69-a59f-f1a27b26f2a8");
        UUID toUUID = fromString("bd303a71-48e9-4c5d-9ee8-7df7d514d4e1");

        String urlClass = contextPath + "/" + HREF_BASE_CLASS +
                "/" + fromUUID + "/";
        String urlNewCrossReference = urlClass + NEW_CROSS_REFERENCE;

        // Check that you can create the object and values are correct
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(fromUUID, toUUID,
                        REFERENCE_TO_CLASS)));
        resultActions.andExpect(status().isCreated());
        validateCrossReferenceFromClassToClass(resultActions);

        // Check that it is possible to delete the class
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlClass)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to add a Cross-reference from a File and
     * that it is possible to delete the File. Checks for referential
     * integrity
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/cross_reference.sql"})
    @WithMockCustomUser
    public void addCrossReferenceFromFileThenDeleteFile()
            throws Exception {
        UUID fromUUID = fromString("7e857df9-d453-48bf-ac4c-19beebf81091");
        UUID toUUID = fromString("b7da0a57-2983-4389-a65e-d87343da78eb");

        String urlFile = contextPath + "/" + HREF_BASE_FILE +
                "/" + fromUUID + "/";
        String urlNewCrossReference = urlFile + NEW_CROSS_REFERENCE;

        // Check that you can create the object and values are correct
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(fromUUID, toUUID,
                        REFERENCE_TO_FILE)));
        resultActions.andExpect(status().isCreated());
        validateCrossReferenceFromFileToFile(resultActions);

        // Check that it is possible to delete the file
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlFile)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);
    }

    /**
     * add a CrossReference from a Class to a Class and make sure the
     * fromSystemID tilSystemID is wrong. This should throw a 400 Bad Request
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/cross_reference.sql"})
    @WithMockCustomUser
    public void addCrossReferenceFromClassToClassWithBadPayload()
            throws Exception {
        UUID fromUUID = fromString("06ae4940-3d86-4a69-a59f-f1a27b26f2a8");
        UUID toUUID = fromString("bd303a71-48e9-4c5d-9ee8-7df7d514d4e1");
        UUID badUUID = fromString("605fe898-5acd-47ed-9504-4341ba7d1cec");

        String urlNewCrossReference = contextPath + "/" + HREF_BASE_CLASS +
                "/" + fromUUID + "/" + NEW_CROSS_REFERENCE;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(
                        badUUID, toUUID, REFERENCE_TO_CLASS)));
        resultActions.andExpect(status().isBadRequest());
    }


    /**
     * add a CrossReference from a File to a File and make sure the
     * fromSystemID value is wrong. This should throw a 400 Bad Request
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/cross_reference.sql"})
    @WithMockCustomUser
    public void addCrossReferenceFromFileToFileWithBadPayload()
            throws Exception {
        UUID fromUUID = fromString("b7da0a57-2983-4389-a65e-d87343da78eb");
        UUID toUUID = fromString("01d0ef0b-b043-457e-8a8b-4ac2128a6637");
        UUID badUUID = fromString("605fe898-5acd-47ed-9504-4341ba7d1cec");

        String urlNewCrossReference = contextPath + "/" + HREF_BASE_FILE +
                "/" + fromUUID + "/" + NEW_CROSS_REFERENCE;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(
                        badUUID, toUUID, REFERENCE_TO_FILE)));
        resultActions.andExpect(status().isBadRequest());
    }

    /**
     * add a CrossReference from a Record to a Record and make sure the
     * fromSystemID value is wrong. This should throw a 400 Bad Request
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/cross_reference.sql"})
    @WithMockCustomUser
    public void addCrossReferenceFromRecordToRecordWithBadPayload()
            throws Exception {
        UUID fromUUID = fromString("ba848de1-e1d7-42fe-9ea1-77127db206fd");
        UUID toUUID = fromString("92e53dff-bd83-4a39-8485-9e34942a1583");
        UUID badUUID = fromString("605fe898-5acd-47ed-9504-4341ba7d1cec");

        String urlRecord = contextPath + "/" + HREF_BASE_RECORD +
                "/" + fromUUID + "/";
        String urlNewCrossReference = urlRecord + NEW_CROSS_REFERENCE;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(
                        badUUID, toUUID, REFERENCE_TO_REGISTRATION)));
        resultActions.andExpect(status().isBadRequest());
    }

    /**
     * Try to add a CrossReference from a Class to a DocumentDescription
     * (or any entity that it should not be  possible to add a reference to).
     * This should throw a 400 Bad Request.
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/cross_reference.sql"})
    @WithMockCustomUser
    public void addCrossReferenceFromClassToDocumentDescription()
            throws Exception {
        UUID fromUUID = fromString("bd303a71-48e9-4c5d-9ee8-7df7d514d4e1");
        UUID toUUID = fromString("dc600862-3298-4ec0-8541-3e51fb900054");

        String urlNewCrossReference = contextPath + "/" + HREF_BASE_CLASS +
                "/" + fromUUID + "/" + NEW_CROSS_REFERENCE;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(
                        fromUUID, toUUID, REFERENCE_TO_CLASS)));
        resultActions.andExpect(status().isBadRequest());
    }

    /**
     * Try to add a CrossReference from a File to a Series (or any entity
     * that it should not be  possible to add a reference to). This
     * should throw a 400 Bad Request.
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/cross_reference.sql"})
    @WithMockCustomUser
    public void addCrossReferenceFromFileToSeries()
            throws Exception {
        UUID fromUUID = fromString("b7da0a57-2983-4389-a65e-d87343da78eb");
        UUID toUUID = fromString("f32c1fa0-8e42-4236-8f40-e006940ea70b");

        String urlNewCrossReference = contextPath + "/" + HREF_BASE_FILE +
                "/" + fromUUID + "/" + NEW_CROSS_REFERENCE;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(
                        fromUUID, toUUID, REFERENCE_TO_FILE)));
        resultActions.andExpect(status().isBadRequest());
    }

    /**
     * Try to add a CrossReference from a Record to a Fonds (or any entity
     * that it should not be  possible to add a reference to). This
     * should throw a 400 Bad Request.
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/cross_reference.sql"})
    @WithMockCustomUser
    public void addCrossReferenceFromRecordToFonds()
            throws Exception {
        UUID fromUUID = fromString("ba848de1-e1d7-42fe-9ea1-77127db206fd");
        UUID toUUID = fromString("3318a63f-11a7-4ec9-8bf1-4144b7f281cf");

        String urlNewCrossReference = contextPath + "/" + HREF_BASE_RECORD +
                "/" + fromUUID + "/" + NEW_CROSS_REFERENCE;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(
                        fromUUID, toUUID, REFERENCE_TO_REGISTRATION)));
        resultActions.andExpect(status().isBadRequest());
    }

    /**
     * Try to add a CrossReference from a Fonds to a Record. This
     * should throw a 400 Bad Request. Links that would make such a
     * request possible are not published so this would be a client
     * deliberately trying something that should not be possible.
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/cross_reference.sql"})
    @WithMockCustomUser
    public void addCrossReferenceFromFondsToRecord()
            throws Exception {
        UUID fromUUID = fromString("3318a63f-11a7-4ec9-8bf1-4144b7f281cf");
        UUID toUUID = fromString("ba848de1-e1d7-42fe-9ea1-77127db206fd");

        String urlNewCrossReference = contextPath + "/" + HREF_BASE_RECORD +
                "/" + fromUUID + "/" + NEW_CROSS_REFERENCE;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(
                        fromUUID, toUUID, REFERENCE_TO_REGISTRATION)));
        resultActions.andExpect(status().isBadRequest());
    }

    /**
     * Try to add a CrossReference from a Class to a DocumentDescription
     * (or any entity that it should not be  possible to add a reference to).
     * This should throw a 400 Bad Request.
     * Note: It uses the file endpoint, but tries to get in a cross reference.
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/cross_reference.sql"})
    @WithMockCustomUser
    public void addCrossReferenceFromDocumentDescriptionToRecordClass()
            throws Exception {
        UUID fromUUID = fromString("dc600862-3298-4ec0-8541-3e51fb900054");
        UUID toUUID = fromString("bd303a71-48e9-4c5d-9ee8-7df7d514d4e1");

        String urlNewCrossReference = contextPath + "/" + HREF_BASE_CLASS +
                "/" + fromUUID + "/" + NEW_CROSS_REFERENCE;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(
                        fromUUID, toUUID, REFERENCE_TO_REGISTRATION)));
        resultActions.andExpect(status().isBadRequest());
    }

    /**
     * Try to add a CrossReference from a File to a Series (or any entity
     * that it should not be  possible to add a reference to). This
     * should throw a 400 Bad Request.
     * Note: It uses the file endpoint, but tries to get in a cross reference.
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/cross_reference.sql"})
    @WithMockCustomUser
    public void addCrossReferenceFromSeriesToFile()
            throws Exception {
        UUID fromUUID = fromString("f32c1fa0-8e42-4236-8f40-e006940ea70b");
        UUID toUUID = fromString("b7da0a57-2983-4389-a65e-d87343da78eb");

        String urlNewCrossReference = contextPath + "/" + HREF_BASE_FILE +
                "/" + fromUUID + "/" + NEW_CROSS_REFERENCE;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewCrossReference)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createCrossReferenceAsJSON(
                        fromUUID, toUUID, REFERENCE_TO_FILE)));
        resultActions.andExpect(status().isBadRequest());
    }
}
