package nikita.common.util.deserialisers.secondary;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.metadata.SignOffMethod;
import nikita.common.model.noark5.v5.secondary.SignOff;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

public class SignOffDeserializer
        extends JsonDeserializer {

    private static final Logger logger =
            LoggerFactory.getLogger(SignOffDeserializer.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public SignOff deserialize(
            JsonParser jsonParser, DeserializationContext dc)
            throws IOException {

        StringBuilder errors = new StringBuilder();
        SignOff signOff = new SignOff();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialize systemID
        deserialiseNoarkSystemIdEntity(signOff, objectNode, errors);
        deserialiseNoarkCreateEntity(signOff, objectNode, errors);
        // Deserialize avskrivningsdato
        signOff.setSignOffDate(deserializeDateTime(
                SIGN_OFF_DATE, objectNode, errors));
        // Deserialize avskrevetAv
        JsonNode currentNode = objectNode.get(SIGN_OFF_BY);
        if (null != currentNode) {
            signOff.setSignOffBy(currentNode.textValue());
            objectNode.remove(SIGN_OFF_BY);
        }

	// Deserialize avskrivningsmetode
	SignOffMethod entity = (SignOffMethod)
	    deserialiseMetadataValue(objectNode,
				     SIGN_OFF_METHOD,
				     new SignOffMethod(),
				     errors, true);
	signOff.setSignOffMethod(entity);

        // TODO handle referanseAvskrevetAv

        // Deserialize referanseAvskrivesAvJournalpost
        UUID referenceSignedOffRecord =
            deserializeUUID(SIGN_OFF_REFERENCE_RECORD,
                            objectNode, errors, true);
        if (null != referenceSignedOffRecord) {
            signOff.setReferenceSignedOffRecordSystemID
                (referenceSignedOffRecord);
        }

        // Deserialize referanseAvskrivesAvKorrespondansepart.
        UUID referenceSignedOffCorrespondencePart =
            deserializeUUID(SIGN_OFF_REFERENCE_CORRESPONDENCE_PART,
                            objectNode, errors, false);
        if (null != referenceSignedOffCorrespondencePart) {
            signOff.setReferenceSignedOffCorrespondencePartSystemID
                (referenceSignedOffCorrespondencePart);
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
            errors.append("The signOff you tried to create is malformed. The");
            errors.append(" following fields are not recognised as signOff ");
            errors.append(" fields [");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("]. ");
        }
        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return signOff;
    }
}
