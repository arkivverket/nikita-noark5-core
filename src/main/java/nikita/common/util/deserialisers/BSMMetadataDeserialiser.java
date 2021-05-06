package nikita.common.util.deserialisers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.md_other.BSMMetadata;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.checkNodeObjectEmpty;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.deserialiseSystemIdEntity;

public class BSMMetadataDeserialiser
        extends JsonDeserializer<BSMMetadata> {

    private static final Logger logger =
            LoggerFactory.getLogger(BSMMetadataDeserialiser.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public BSMMetadata deserialize(
            JsonParser jsonParser,
            DeserializationContext deserializationContext)
            throws IOException {

        StringBuilder errors = new StringBuilder();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        BSMMetadata bsmMetadata = new BSMMetadata();
        deserialiseSystemIdEntity(bsmMetadata, objectNode, errors);
        JsonNode currentNode = objectNode.get(NAME);
        if (null != currentNode) {
            bsmMetadata.setName(currentNode.textValue());
            objectNode.remove(NAME);
        } else {
            errors.append(NAME + " is missing. ");
        }
        currentNode = objectNode.get(TYPE);
        if (null != currentNode) {
            bsmMetadata.setType(currentNode.textValue());
            objectNode.remove(TYPE);
        } else {
            errors.append(TYPE + " is missing. ");
        }
        currentNode = objectNode.get(OUTDATED);
        if (null != currentNode) {
            bsmMetadata.setOutdated(currentNode.booleanValue());
            objectNode.remove(OUTDATED);
        }
        currentNode = objectNode.get(DESCRIPTION);
        if (null != currentNode) {
            bsmMetadata.setDescription(currentNode.textValue());
            objectNode.remove(DESCRIPTION);
        }
        currentNode = objectNode.get(SOURCE);
        if (null != currentNode) {
            bsmMetadata.setSource(currentNode.textValue());
            objectNode.remove(SOURCE);
        }

        currentNode = objectNode.get(LINKS);
        if (null != currentNode) {
            logger.debug("Payload contains " + LINKS + ". " +
                    "This value is being ignored.");
            objectNode.remove(LINKS);
        }
        // Check that there are no additional values left after
        // processing the tree. If there are additional throw a
        // malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The virksomhetsspesifikkeMetadata object you tried");
            errors.append(" to create is malformed. The following fields are");
            errors.append(" not recognised as virksomhetsspesifikkeMetadata");
            errors.append(" fields  [");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("]");
        }

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return bsmMetadata;
    }
}
