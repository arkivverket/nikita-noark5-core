package nikita.common.util.deserialisers.casehandling;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.bsm.BSM;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartInternal;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nikita.common.config.ErrorMessagesConstants.MALFORMED_PAYLOAD;
import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.BSM_DEF;
import static nikita.common.config.N5ResourceMappings.CORRESPONDENCE_PART_INTERNAL;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

/**
 * Created by tsodring on 1/6/17.
 * <p>
 * Deserialise an incoming CorrespondencePart JSON object.
 * <p>
 * Detect if the CorrespondencePart is CorrespondencePartPerson,
 * CorrespondencePartInternal or CorrespondencePartInternal and returns an
 * object the appropriate type.
 * <p>
 * Note:
 * - Unknown property values in the JSON will trigger an exception
 * - Missing obligatory property values in the JSON will trigger an exception
 */
public class CorrespondencePartInternalDeserializer
        extends JsonDeserializer<CorrespondencePartInternal> {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    CorrespondencePartInternalDeserializer.class);


    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public CorrespondencePartInternal deserialize(JsonParser jsonParser,
                                                  DeserializationContext dc)
            throws IOException {
        StringBuilder errors = new StringBuilder();

        CorrespondencePartInternal correspondencePartInternal =
                new CorrespondencePartInternal();
        ObjectNode objectNode = mapper.readTree(jsonParser);
        deserialiseNoarkSystemIdEntity(
                correspondencePartInternal, objectNode);
        deserialiseCorrespondencePartInternalEntity(
                correspondencePartInternal, objectNode, errors);

        // Deserialize businessSpecificMetadata (virksomhetsspesifikkeMetadata)
        JsonNode currentNode = objectNode.get(BSM_DEF);
        if (null != currentNode) {
            BSM base = mapper.readValue(currentNode.traverse(), BSM.class);
            correspondencePartInternal.addReferenceBSMBase(
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
                    CORRESPONDENCE_PART_INTERNAL,
                    checkNodeObjectEmpty(objectNode)));
        }

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return correspondencePartInternal;
    }
}
