package nikita.common.util.deserialisers.casehandling;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.bsm.BSM;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nikita.common.config.ErrorMessagesConstants.MALFORMED_PAYLOAD;
import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.BSM_DEF;
import static nikita.common.config.N5ResourceMappings.CORRESPONDENCE_PART_UNIT;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

/**
 * Deserialise an incoming CorrespondencePart JSON object.
 */
public class CorrespondencePartUnitDeserializer
        extends JsonDeserializer<CorrespondencePartUnit> {

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
                objectNode);
        deserialiseCorrespondencePartUnitEntity(correspondencePartUnit,
                objectNode, errors);

        // Deserialize businessSpecificMetadata (virksomhetsspesifikkeMetadata)
        JsonNode currentNode = objectNode.get(BSM_DEF);
        if (null != currentNode) {
            BSM base = mapper.readValue(currentNode.traverse(), BSM.class);
            correspondencePartUnit.addReferenceBSMBase(
                    base.getReferenceBSMBase());
            objectNode.remove(BSM_DEF);
        }

        currentNode = objectNode.get(LINKS);
        if (null != currentNode) {
            logger.debug("Payload contains " + LINKS + ". " +
                    "This value is being ignored.");
            objectNode.remove(LINKS);
        }

        // Check that there are no additional values left after processing
        // the tree. If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append(String.format(MALFORMED_PAYLOAD,
                    CORRESPONDENCE_PART_UNIT, checkNodeObjectEmpty(objectNode)));
        }

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return correspondencePartUnit;
    }
}
