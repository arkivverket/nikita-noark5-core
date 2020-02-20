package nikita.common.util.deserialisers.casehandling;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

/**
 * Created by tsodring on 1/6/17.
 * <p>
 * Deserialise an incoming CorrespondencePart JSON object.
 * <p>
 * Detect if the CorrespondencePart is CorrespondencePartPerson,
 * CorrespondencePartInternal or CorrespondencePartUnit and returns an object
 * the appropriate type.
 * <p>
 * Note:
 * - Unknown property values in the JSON will trigger an exception
 * - Missing obligatory property values in the JSON will trigger an exception
 */
public class CorrespondencePartUnitDeserializer
        extends JsonDeserializer {

    private static final Logger logger =
            LoggerFactory.getLogger(CorrespondencePartUnitDeserializer.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public CorrespondencePartUnit deserialize(JsonParser jsonParser,
                                              DeserializationContext dc)
            throws IOException {
        StringBuilder errors = new StringBuilder();

        CorrespondencePartUnit correspondencePartUnit =
                new CorrespondencePartUnit();
        ObjectNode objectNode = mapper.readTree(jsonParser);
        deserialiseNoarkSystemIdEntity(correspondencePartUnit,
                objectNode, errors);
        deserialiseCorrespondencePartUnitEntity(correspondencePartUnit,
                objectNode, errors);

        JsonNode currentNode = objectNode.get(LINKS);
        if (null != currentNode) {
            logger.debug("Payload contains " + LINKS + ". " +
                    "This value is being ignored.");
            objectNode.remove(LINKS);
        }

        // Check that there are no additional values left after processing
        // the tree. If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The korrespondansepartenhet you tried to create " +
                          "is malformed. The following fields are not " +
                          "recognised as korrespondansepartenhet fields " +
                          "[" + checkNodeObjectEmpty(objectNode) + "].");
        }

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return correspondencePartUnit;
    }
}
