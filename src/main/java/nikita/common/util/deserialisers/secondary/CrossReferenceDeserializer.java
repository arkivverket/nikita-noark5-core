package nikita.common.util.deserialisers.secondary;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.secondary.CrossReference;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static java.util.UUID.fromString;
import static nikita.common.config.ErrorMessagesConstants.MALFORMED_PAYLOAD;
import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

public class CrossReferenceDeserializer
        extends JsonDeserializer<CrossReference> {

    private static final Logger logger =
            LoggerFactory.getLogger(CrossReferenceDeserializer.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public CrossReference deserialize(
            JsonParser jsonParser, DeserializationContext dc)
            throws IOException {

        StringBuilder errors = new StringBuilder();
        CrossReference crossReference = new CrossReference();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // deserialize systemID
        deserialiseNoarkSystemIdEntity(crossReference, objectNode);
        deserialiseNoarkCreateEntity(crossReference, objectNode, errors);

        // deserialize fraSystemID
        JsonNode currentNode = objectNode.get(FROM_SYSTEM_ID);
        if (null != currentNode) {
            crossReference.setFromSystemId(fromString(currentNode.textValue()));
            objectNode.remove(FROM_SYSTEM_ID);
        }
        // deserialize fraSystemID
        currentNode = objectNode.get(TO_SYSTEM_ID);
        if (null != currentNode) {
            crossReference.setToSystemId(fromString(currentNode.textValue()));
            objectNode.remove(TO_SYSTEM_ID);
        }
        // deserialize referanseType
        currentNode = objectNode.get(REFERENCE_TYPE);
        if (null != currentNode) {
            crossReference.setReferenceType(currentNode.textValue());
            objectNode.remove(REFERENCE_TYPE);
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
            errors.append(String.format(MALFORMED_PAYLOAD,
                    CROSS_REFERENCE, checkNodeObjectEmpty(objectNode)));
        }
        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return crossReference;
    }
}
