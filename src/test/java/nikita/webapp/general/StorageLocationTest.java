package nikita.webapp.general;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.webapp.spring.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.SELF;
import static nikita.common.config.N5ResourceMappings.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.DocumentDescriptionCreator.createDocumentDescription;
import static utils.DocumentDescriptionCreator.createDocumentDescriptionAsJSON;
import static utils.DocumentDescriptionValidator.*;
import static utils.StorageLocationCreator.createStorageLocationAsJSON;
import static utils.StorageLocationCreator.createUpdatedStorageLocationAsJSON;
import static utils.StorageLocationValidator.*;
import static utils.TestConstants.STORAGE_LOCATION_TEST_UPDATED;
import static utils.TestConstants.TITLE_TEST_UPDATED;

public class StorageLocationTest
        extends BaseTest {

    /**
     * Check that it is possible to add a StorageLocation to an existing
     * DocumentDescription
     * <p>
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/storage_location.sql"})
    @WithMockCustomUser
    public void addStorageLocationWhenCreatingDocumentDescription()
            throws Exception {
        // First get template to create / POST DocumentDescription
        String urlNewDocumentDescription = "/noark5v5/api/arkivstruktur/registrering" +
                "/dc600862-3298-4ec0-8541-3e51fb900054/ny-dokumentbeskrivelse";

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlNewDocumentDescription)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk());
        validateDocumentDescriptionTemplate(resultActions);
        printDocumentation(resultActions);

        // Create a JSON object to POST
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewDocumentDescription)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createDocumentDescriptionAsJSON()));

        resultActions.andExpect(status().isCreated());
        validateDocumentDescription(resultActions);
        printDocumentation(resultActions);

        // Retrieve an identified StorageLocation
        String urlStorageLocation = getHref(SELF, resultActions);
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlStorageLocation)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk());
        validateStorageLocationForDocumentDescription(resultActions);
        printDocumentation(resultActions);

        MockHttpServletResponse response = resultActions.andReturn()
                .getResponse();
        String etag = response.getHeader("ETAG");

        DocumentDescription documentDescription = createDocumentDescription();
        documentDescription.setStorageLocation(STORAGE_LOCATION_TEST_UPDATED);
        documentDescription.setTitle(TITLE_TEST_UPDATED);

        // Update an StorageLocation that is part of a DocumentDescription
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put(urlStorageLocation)
                .contextPath(contextPath)
                .header("ETAG", etag)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createDocumentDescriptionAsJSON(documentDescription)));
        resultActions.andExpect(status().isOk());
        validateUpdatedDocumentDescription(resultActions);
        printDocumentation(resultActions);

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlStorageLocation)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk());
        validateUpdatedDocumentDescription(resultActions);

        // OData search for a File based on StorageLocation
        String odata = "?$filter=" + STORAGE_LOCATION + " eq '" +
                STORAGE_LOCATION_TEST_UPDATED + "'";
        String urlDocDescSearch = contextPath + "/odata/api/arkivstruktur/" +
                DOCUMENT_DESCRIPTION + odata;
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlDocDescSearch)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)));
        printDocumentation(resultActions);

        // OData search for a StorageLocation associated with a
        // DocumentDescription
        odata = "?$filter=" + STORAGE_LOCATION + " eq " +
                "'" + STORAGE_LOCATION_TEST_UPDATED + "'";
        String urlStorageLocationSearch = contextPath + "/odata/api/arkivstruktur/" +
                DOCUMENT_DESCRIPTION + odata;
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlStorageLocationSearch)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        response = resultActions.andReturn().getResponse();
        System.out.println(response.getContentAsString());
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)))
                .andExpect(jsonPath("$.results[0]." + SYSTEM_ID).exists());
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to add a StorageLocation to an existing
     * File
     * <p>
     * 1. Check that the GET ny-oppbevaringssted works
     * 2. POST ny-oppbevaringssted and check value and self REL
     * 3. Check that the storageLocation object can be retrieved
     * 4. Check that the retrieved storageLocation object can be updated
     * 5. Check that OData query on mappe/oppbevaringssted works
     * 6. Check that OData query on oppbevaringssted works
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/storage_location.sql"})
    @WithMockCustomUser
    public void addStorageLocationWhenCreatingFile() throws Exception {
        // First get template to create / POST StorageLocation
        String urlNewStorageLocation = "/noark5v5/api/arkivstruktur/" +
                "mappe/a9145ece-13ee-4d51-a880-0879ed225302/" +
                NEW_STORAGE_LOCATION;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlNewStorageLocation)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        // We do not do anything else with the result. Just make sure the
        // call works
        resultActions.andExpect(status().isOk());
        printDocumentation(resultActions);

        // Create an StorageLocation object associated with the File
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewStorageLocation)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createStorageLocationAsJSON()));
        resultActions.andExpect(status().isCreated());
        validateStorageLocationForFile(resultActions);
        printDocumentation(resultActions);

        // Retrieve an identified StorageLocation
        String urlStorageLocation = getHref(SELF, resultActions);
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlStorageLocation)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk());
        validateStorageLocationForFile(resultActions);
        printDocumentation(resultActions);

        // Update an identified StorageLocation
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put(urlStorageLocation)
                .contextPath(contextPath)
                .header("ETAG", "\"0\"")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createUpdatedStorageLocationAsJSON()));
        resultActions.andExpect(status().isOk());
        validateUpdatedStorageLocation(resultActions);
        printDocumentation(resultActions);

        // OData search for a File based on StorageLocation
        String odata = "?$filter=oppbevaringssted/oppbevaringssted eq '" +
                STORAGE_LOCATION_TEST_UPDATED + "'";
        String urlDocDescSearch = contextPath + "/odata/api/arkivstruktur/" +
                FILE + odata;
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlDocDescSearch)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)));
        printDocumentation(resultActions);

        // OData search for StorageLocation
        odata = "?$filter=oppbevaringssted eq '" + STORAGE_LOCATION_TEST_UPDATED + "'";
        String urlStorageLocationSearch = contextPath + "/odata/api/arkivstruktur/" +
                STORAGE_LOCATION + odata;
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlStorageLocationSearch)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)));
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to add a StorageLocation to an existing
     * Series
     * <p>
     * 1. Check that the GET ny-oppbevaringssted works
     * 2. POST ny-oppbevaringssted and check value and self REL
     * 3. Check that the storageLocation object can be retrieved
     * 4. Check that the retrieved storageLocation object can be updated
     * 5. Check that OData query on arkivdel/oppbevaringssted works
     * 6. Check that OData query on oppbevaringssted works
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/storage_location.sql"})
    @WithMockCustomUser
    public void addStorageLocationWhenCreatingSeries() throws Exception {
        // First get template to create / POST StorageLocation
        String urlNewStorageLocation = "/noark5v5/api/arkivstruktur/" +
                "arkivdel/312ea7a2-f570-4183-a4b4-b30adf3b62dd/" + NEW_STORAGE_LOCATION;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlNewStorageLocation)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        // We do not do anything else with the result. Just make sure the
        // call works
        resultActions.andExpect(status().isOk());
        printDocumentation(resultActions);

        // Create an StorageLocation object associated with the Series
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewStorageLocation)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createStorageLocationAsJSON()));
        resultActions.andExpect(status().isCreated());
        validateStorageLocationForSeries(resultActions);
        printDocumentation(resultActions);

        // Retrieve an identified StorageLocation
        String urlStorageLocation = getHref(SELF, resultActions);
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlStorageLocation)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk());
        validateStorageLocationForSeries(resultActions);
        printDocumentation(resultActions);

        // Update an identified StorageLocation
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put(urlStorageLocation)
                .contextPath(contextPath)
                .header("ETAG", "\"0\"")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createUpdatedStorageLocationAsJSON()));
        resultActions.andExpect(status().isOk());
        validateUpdatedStorageLocation(resultActions);
        printDocumentation(resultActions);

        // OData search for a Series based on StorageLocation
        String odata = "?$filter=oppbevaringssted/oppbevaringssted eq '" +
                STORAGE_LOCATION_TEST_UPDATED + "'";
        String urlDocDescSearch = contextPath + "/odata/api/arkivstruktur/" +
                SERIES + odata;
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlDocDescSearch)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)));
        printDocumentation(resultActions);

        // OData search for a given StorageLocation
        odata = "?$filter=oppbevaringssted eq '" + STORAGE_LOCATION_TEST_UPDATED + "'";
        String urlStorageLocationSearch = contextPath + "/odata/api/arkivstruktur/" +
                STORAGE_LOCATION + odata;
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlStorageLocationSearch)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)));
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to add a StorageLocation to an existing
     * Record.
     * <p>
     * 1. Check that the GET ny-oppbevaringssted works
     * 2. POST ny-oppbevaringssted and check value and self REL
     * 3. Check that the storageLocation object can be retrieved
     * 4. Check that the retrieved storageLocation object can be updated
     * 5. Check that OData query on registrering/oppbevaringssted works
     * 6. Check that OData query on oppbevaringssted works
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/storage_location.sql"})
    @WithMockCustomUser
    public void addStorageLocationWhenCreatingRecord() throws Exception {
        // First get template to create / POST StorageLocation
        String urlNewStorageLocation = "/noark5v5/api/arkivstruktur/" +
                "registrering/4b063ea2-227e-4a39-82bb-66d590ce4ebf/" +
                NEW_STORAGE_LOCATION;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlNewStorageLocation)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        // We do not do anything else with the result. Just make sure the
        // call works
        resultActions.andExpect(status().isOk());
        printDocumentation(resultActions);

        // Create an StorageLocation object associated with the Record
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(urlNewStorageLocation)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createStorageLocationAsJSON()));
        resultActions.andExpect(status().isCreated());
        validateStorageLocationForRecord(resultActions);
        printDocumentation(resultActions);

        // Retrieve an identified StorageLocation
        String urlStorageLocation = getHref(SELF, resultActions);
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlStorageLocation)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk());
        validateStorageLocationForRecord(resultActions);
        printDocumentation(resultActions);

        // Update an identified StorageLocation
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put(urlStorageLocation)
                .contextPath(contextPath)
                .header("ETAG", "\"0\"")
                .accept(NOARK5_V5_CONTENT_TYPE_JSON)
                .contentType(NOARK5_V5_CONTENT_TYPE_JSON)
                .content(createUpdatedStorageLocationAsJSON()));
        resultActions.andExpect(status().isOk());
        validateUpdatedStorageLocation(resultActions);
        printDocumentation(resultActions);

        // OData search for a Record based on StorageLocation
        String odata = "?$filter=oppbevaringssted/oppbevaringssted eq '" +
                STORAGE_LOCATION_TEST_UPDATED + "'";
        String urlDocDescSearch = contextPath + "/odata/api/arkivstruktur/" +
                RECORD + odata;
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlDocDescSearch)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)));
        printDocumentation(resultActions);

        // OData search for a given StorageLocation
        odata = "?$filter=oppbevaringssted eq '" + STORAGE_LOCATION_TEST_UPDATED + "'";
        String urlStorageLocationSearch = contextPath + "/odata/api/arkivstruktur/" +
                STORAGE_LOCATION + odata;
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlStorageLocationSearch)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)));
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to delete a storageLocation by its systemID and that
     * the related file has no associated storageLocation afterwards
     * <p>
     * 1. Get associated File, make sure StorageLocation rel/href is in _links
     * 2. Delete StorageLocation using it's systemID
     * 3. Get previous associated File, make sure StorageLocation rel/href is
     * not in _links
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/storage_location.sql"})
    @WithMockCustomUser
    public void deleteStorageLocationObjectWithFile() throws Exception {
        // 1. Get associated File, make sure StorageLocation rel/href is in _links
        String urlFile = contextPath + "/api/arkivstruktur/" +
                "mappe/a9145ece-13ee-4d51-a880-0879ed225302/";
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlFile)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$._links.['" + REL_FONDS_STRUCTURE_STORAGE_LOCATION + "']")
                        .exists());

        // 2. Delete StorageLocation using it's systemID
        String urlStorageLocation = contextPath + "/api/arkivstruktur/" +
                "oppbevaringssted/81cea881-1203-4e3f-943c-c0294e81e528/";
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlStorageLocation)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);

        // 3. Get previous associated File, make sure StorageLocation rel/href
        // is not in _links
        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(urlFile)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$._links.['" + REL_FONDS_STRUCTURE_STORAGE_LOCATION + "']")
                        .doesNotExist());
    }

    /**
     * Check that it is possible to delete a Fonds that has a StorageLocation
     * associated with it. The test is that referential integrity should
     * not be an issue.
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/storage_location.sql"})
    @WithMockCustomUser
    public void deleteFondsWithStorageLocation() throws Exception {
        String urlFonds = "/noark5v5/api/arkivstruktur/" +
                "arkiv/388a1d7e-de4d-4da5-bbd8-5a0f1b9c8843/";
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlFonds)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to delete a Series that has a StorageLocation
     * associated with it. The test is that referential integrity should
     * not be an issue.
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/storage_location.sql"})
    @WithMockCustomUser
    public void deleteSeriesWithStorageLocation() throws Exception {
        String urlSeries = "/noark5v5/api/arkivstruktur/" +
                "arkivdel/312ea7a2-f570-4183-a4b4-b30adf3b62dd/";
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlSeries)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to delete a File that has a StorageLocation
     * associated with it. The test is that referential integrity should
     * not be an issue.
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/storage_location.sql"})
    @WithMockCustomUser
    public void deleteFileWithStorageLocation() throws Exception {
        String urlFile = "/noark5v5/api/arkivstruktur/" +
                "arkivdel/312ea7a2-f570-4183-a4b4-b30adf3b62dd/";
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlFile)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);
    }

    /**
     * Check that it is possible to delete a Record that has a StorageLocation
     * associated with it. The test is that referential integrity should
     * not be an issue.
     *
     * @throws Exception Serialising or validation exception
     */
    @Test
    @Sql({"/db-tests/basic_structure.sql", "/db-tests/storage_location.sql"})
    @WithMockCustomUser
    public void deleteRecordWithStorageLocation() throws Exception {
        String urlRecord = "/noark5v5/api/arkivstruktur/" +
                "registrering/4b063ea2-227e-4a39-82bb-66d590ce4ebf/";
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete(urlRecord)
                .contextPath(contextPath)
                .accept(NOARK5_V5_CONTENT_TYPE_JSON));
        resultActions.andExpect(status().isNoContent());
        printDocumentation(resultActions);
    }
}
