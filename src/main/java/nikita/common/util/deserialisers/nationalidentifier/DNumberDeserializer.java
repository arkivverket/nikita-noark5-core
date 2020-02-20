package nikita.common.util.deserialisers.nationalidentifier;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.nationalidentifier.DNumber;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.D_NUMBER_FIELD;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.checkNodeObjectEmpty;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.deserialiseNoarkSystemIdEntity;

public class DNumberDeserializer
        extends JsonDeserializer {

    private static final Logger logger =
            LoggerFactory.getLogger(DNumberDeserializer.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public DNumber deserialize(
            JsonParser jsonParser, DeserializationContext dc)
            throws IOException {

        StringBuilder errors = new StringBuilder();
        DNumber dNumber = new DNumber();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialize systemID
        deserialiseNoarkSystemIdEntity(dNumber, objectNode, errors);

        // Deserialize dNummer
        JsonNode currentNode = objectNode.get(D_NUMBER_FIELD);
        if (null != currentNode) {
            dNumber.setdNumber(currentNode.textValue());
            objectNode.remove(D_NUMBER_FIELD);
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
            errors.append("The dNumber you tried to create is malformed. The");
            errors.append(" following fields are not recognised as dNumber ");
            errors.append(" fields [");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("]. ");
        }
        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return dNumber;
    }
}
