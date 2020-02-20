package nikita.common.util.deserialisers.secondary;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.secondary.PartPerson;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

/**
 * Created by tsodring on 11/07/18.
 * <p>
 * Deserialise an incoming PartPerson JSON object.
 */
public class PartPersonDeserializer
        extends JsonDeserializer {

    private static final Logger logger =
            LoggerFactory.getLogger(PartPersonDeserializer.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public PartPerson deserialize(
            JsonParser jsonParser, DeserializationContext dc)
            throws IOException {

        StringBuilder errors = new StringBuilder();
        PartPerson part = new PartPerson();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        deserialiseNoarkSystemIdEntity(part, objectNode, errors);
        deserialisePartPersonEntity(part, objectNode, errors);

        JsonNode currentNode = objectNode.get(LINKS);
        if (null != currentNode) {
            logger.debug("Payload contains " + LINKS + ". " +
                    "This value is being ignored.");
            objectNode.remove(LINKS);
        }

        // Check that there are no additional values left after processing
        // If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The partperson you tried to create is malformed. " +
                          "The following fields are not recognised as " +
                          "partperson fields " +
                          "[" + checkNodeObjectEmpty(objectNode) + "]. ");
        }

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return part;
    }
}
