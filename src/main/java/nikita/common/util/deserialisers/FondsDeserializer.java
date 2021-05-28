package nikita.common.util.deserialisers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.metadata.FondsStatus;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nikita.common.config.ErrorMessagesConstants.MALFORMED_PAYLOAD;
import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.FONDS;
import static nikita.common.config.N5ResourceMappings.FONDS_STATUS;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

/**
 * Deserialise an incoming Fonds JSON object.
 */
public class FondsDeserializer
        extends JsonDeserializer<Fonds> {

    private static final Logger logger =
            LoggerFactory.getLogger(FondsDeserializer.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Fonds deserialize(JsonParser jsonParser, DeserializationContext dc)
            throws IOException {
        StringBuilder errors = new StringBuilder();
        Fonds fonds = new Fonds();
        ObjectNode objectNode = mapper.readTree(jsonParser);
        // Deserialise general properties
        deserialiseNoarkGeneralEntity(fonds, objectNode, errors);
        deserialiseDocumentMedium(fonds, objectNode, errors);
        deserialiseStorageLocation(fonds, objectNode);
        // Deserialize seriesStatus
        FondsStatus fondsStatus = (FondsStatus)
                deserialiseMetadataValue(
                        objectNode,
                        FONDS_STATUS,
                        new FondsStatus(),
                        errors, false);
        fonds.setFondsStatus(fondsStatus);
        JsonNode currentNode = objectNode.get(LINKS);
        if (null != currentNode) {
            logger.debug("Payload contains " + LINKS + ". " +
                    "This value is being ignored.");
            objectNode.remove(LINKS);
        }
        // Check that there are no additional values left after processing the tree
        // If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append(String.format(MALFORMED_PAYLOAD,
                    FONDS, checkNodeObjectEmpty(objectNode)));
        }
        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());
        return fonds;
    }
}
