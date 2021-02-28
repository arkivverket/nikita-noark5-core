package nikita.common.util.deserialisers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.md_other.BSMMetadata;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;

public class BSMMetadataDeserialiser
        extends JsonDeserializer<BSMMetadata> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public BSMMetadata deserialize(
            JsonParser jsonParser,
            DeserializationContext deserializationContext)
            throws IOException {

        ObjectNode objectNode = mapper.readTree(jsonParser);
        BSMMetadata bsmMetadata = new BSMMetadata();
        JsonNode currentNode = objectNode.get(NAME);
        if (null != currentNode) {
            bsmMetadata.setName(currentNode.textValue());
            objectNode.remove(NAME);
        }
        currentNode = objectNode.get(TYPE);
        if (null != currentNode) {
            bsmMetadata.setType(currentNode.textValue());
            objectNode.remove(TYPE);
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
        return bsmMetadata;
    }
}
