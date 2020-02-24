package nikita.common.util.deserialisers.metadata;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.CODE;
import static nikita.common.config.N5ResourceMappings.CODE_NAME;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.checkNodeObjectEmpty;

public class MetadataDeserializer
        extends JsonDeserializer {

    private static final Logger logger =
            LoggerFactory.getLogger(MetadataDeserializer.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Metadata deserialize(JsonParser jsonParser,
                                DeserializationContext dc)
            throws IOException {
        StringBuilder errors = new StringBuilder();
        Metadata metadata = new Metadata();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        JsonNode node = objectNode.get(CODE);
        if (null != node) {
            metadata.setCode(node.textValue());
            objectNode.remove(CODE);
        } else {
            errors.append(CODE + " is missing. ");
        }
        node = objectNode.get(CODE_NAME);
        if (null != node) {
            metadata.setCodeName(node.textValue());
            objectNode.remove(CODE_NAME);
        }
        node = objectNode.get(LINKS);
        if (null != node) {
            logger.info("Payload contains " + LINKS + ". " +
                    "This value is being ignored.");
            objectNode.remove(LINKS);
        }
        // Check that there are no additional values left after processing the tree
        // If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The metadata you tried to create is malformed. The");
            errors.append(" following fields are not recognised as metadata ");
            errors.append("fields [");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("]. ");
        }

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return metadata;
    }
}
