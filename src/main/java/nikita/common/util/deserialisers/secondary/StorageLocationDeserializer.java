package nikita.common.util.deserialisers.secondary;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.secondary.StorageLocation;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.STORAGE_LOCATION;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

public class StorageLocationDeserializer
        extends JsonDeserializer<StorageLocation> {

    private static final Logger logger =
            LoggerFactory.getLogger(StorageLocationDeserializer.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public StorageLocation deserialize(
            JsonParser jsonParser, DeserializationContext dc)
            throws IOException {

        StringBuilder errors = new StringBuilder();
        StorageLocation storageLocation = new StorageLocation();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialize systemID
        deserialiseNoarkSystemIdEntity(storageLocation, objectNode);
        deserialiseNoarkCreateEntity(storageLocation, objectNode, errors);

        // Deserialize oppbevaringssted
        JsonNode currentNode = objectNode.get(STORAGE_LOCATION);
        if (null != currentNode) {
            storageLocation.setStorageLocation(currentNode.textValue());
            objectNode.remove(STORAGE_LOCATION);
        }
        currentNode = objectNode.get(LINKS);
        if (null != currentNode) {
            logger.debug("Payload contains " + LINKS + ". " +
                    "This value is being ignored.");
            objectNode.remove(LINKS);
        }

        // Check that there are no additional values left after processing the
        // tree. If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The storageLocation you tried to create is malformed. The");
            errors.append(" following fields are not recognised as storageLocation ");
            errors.append(" fields [");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("]. ");
        }
        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return storageLocation;
    }
}
