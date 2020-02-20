package nikita.common.util.deserialisers.nationalidentifier;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.nationalidentifier.SocialSecurityNumber;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.SOCIAL_SECURITY_NUMBER;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.checkNodeObjectEmpty;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.deserialiseNoarkSystemIdEntity;

public class SocialSecurityNumberDeserializer
        extends JsonDeserializer {

    private static final Logger logger =
            LoggerFactory.getLogger(SocialSecurityNumberDeserializer.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public SocialSecurityNumber deserialize(
            JsonParser jsonParser, DeserializationContext dc)
            throws IOException {

        StringBuilder errors = new StringBuilder();
        SocialSecurityNumber socialSecurityNumber = new SocialSecurityNumber();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialize systemID
        deserialiseNoarkSystemIdEntity(socialSecurityNumber, objectNode, errors);

        // Deserialize foedselsnummer
        JsonNode currentNode = objectNode.get(SOCIAL_SECURITY_NUMBER);
        if (null != currentNode) {
            socialSecurityNumber.setSocialSecurityNumber(
                    currentNode.textValue());
            objectNode.remove(SOCIAL_SECURITY_NUMBER);
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
            errors.append("The socialSecurityNumber you tried to create is ");
            errors.append("malformed. The following fields are not recognised");
            errors.append(" as socialSecurityNumber fields [");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("]. ");
        }
        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return socialSecurityNumber;
    }
}
