package nikita.common.util.deserialisers.secondary;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.metadata.PrecedenceStatus;
import nikita.common.model.noark5.v5.secondary.Precedence;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

public class PrecedenceDeserializer
        extends JsonDeserializer {

    private static final Logger logger =
            LoggerFactory.getLogger(PrecedenceDeserializer.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Precedence deserialize(
            JsonParser jsonParser, DeserializationContext dc)
            throws IOException {

        StringBuilder errors = new StringBuilder();
        Precedence precedence = new Precedence();
        ObjectNode objectNode = mapper.readTree(jsonParser);
        deserialiseNoarkSystemIdEntity(precedence, objectNode, errors);
        deserialiseNoarkCreateEntity(precedence, objectNode, errors);
        deserialiseNoarkTitleDescriptionEntity(precedence, objectNode, errors);
        deserialiseNoarkFinaliseEntity(precedence, objectNode, errors);

        // Deserialize precedenceDate
        precedence.setPrecedenceDate(deserializeDate(PRECEDENCE_DATE, objectNode, errors, true));

        // Deserialize precedenceAuthority
        JsonNode currentNode = objectNode.get(PRECEDENCE_AUTHORITY);
        if (null != currentNode) {
            precedence.setPrecedenceAuthority(currentNode.textValue());
            objectNode.remove(PRECEDENCE_AUTHORITY);
        }
        // Deserialize sourceOfLaw
        currentNode = objectNode.get(PRECEDENCE_SOURCE_OF_LAW);
        if (null != currentNode) {
            precedence.setSourceOfLaw(currentNode.textValue());
            objectNode.remove(PRECEDENCE_SOURCE_OF_LAW);
        } else {
            errors.append(PRECEDENCE_SOURCE_OF_LAW + " is missing. ");
        }
        // Deserialize precedenceApprovedBy
        currentNode = objectNode.get(PRECEDENCE_APPROVED_BY);
        if (null != currentNode) {
            precedence.setPrecedenceApprovedBy(currentNode.textValue());
            objectNode.remove(PRECEDENCE_APPROVED_BY);
        }
        // Deserialize referansePresedensGodkjentAv
        UUID referencePrecedenceApprovedBy =
            deserializeUUID(PRECEDENCE_REFERENCE_APPROVED_BY,
                            objectNode, errors, false);
        if (null != referencePrecedenceApprovedBy) {
            precedence.setReferencePrecedenceApprovedBySystemID
                (referencePrecedenceApprovedBy);
        }
        // Deserialize presedensStatus
        PrecedenceStatus precedenceStatus = (PrecedenceStatus)
            deserialiseMetadataValue(
                objectNode,
                PRECEDENCE_PRECEDENCE_STATUS,
                new PrecedenceStatus(),
                errors, false);
        precedence.setPrecedenceStatus(precedenceStatus);
        // Deserialize precedenceApprovedDate
        precedence.setPrecedenceApprovedDate(deserializeDateTime(PRECEDENCE_APPROVED_DATE, objectNode, errors));

        currentNode = objectNode.get(LINKS);
        if (null != currentNode) {
            logger.debug("Payload contains " + LINKS + ". " +
                    "This value is being ignored.");
            objectNode.remove(LINKS);
        }

        // Check that there are no additional values left after processing the
        // tree. If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The precedence you tried to create is malformed.");
            errors.append(" The following fields are not recognised as");
            errors.append(" precedence fields [");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("]. ");
        }
        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return precedence;
    }
}
