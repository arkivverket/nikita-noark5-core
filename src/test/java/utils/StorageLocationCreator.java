package utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.secondary.StorageLocation;

import java.io.IOException;
import java.io.StringWriter;

import static nikita.common.config.N5ResourceMappings.STORAGE_LOCATION;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printNullable;
import static utils.TestConstants.STORAGE_LOCATION_TEST;
import static utils.TestConstants.STORAGE_LOCATION_TEST_UPDATED;

public final class StorageLocationCreator {

    /**
     * Create a default StorageLocation for testing purposes
     *
     * @return a StorageLocation with all values set
     */
    public static StorageLocation createStorageLocation() {
        StorageLocation storageLocation = new StorageLocation();
        storageLocation.setStorageLocation(STORAGE_LOCATION_TEST);
        return storageLocation;
    }

    public static String createStorageLocationAsJSON() throws IOException {
        return createStorageLocationAsJSON(createStorageLocation());
    }

    public static String createUpdatedStorageLocationAsJSON() throws IOException {
        StorageLocation storageLocation = createStorageLocation();
        storageLocation.setStorageLocation(STORAGE_LOCATION_TEST_UPDATED);
        return createStorageLocationAsJSON(storageLocation);
    }

    public static String createStorageLocationAsJSON(
            StorageLocation storageLocation) throws IOException {
        JsonFactory factory = new JsonFactory();
        StringWriter jsonWriter = new StringWriter();
        JsonGenerator jgen = factory.createGenerator(jsonWriter);
        jgen.writeStartObject();
        printNullable(jgen, STORAGE_LOCATION, storageLocation.getStorageLocation());
        jgen.writeEndObject();
        jgen.close();
        return jsonWriter.toString();
    }
}
