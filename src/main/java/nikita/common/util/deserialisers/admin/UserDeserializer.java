package nikita.common.util.deserialisers.admin;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;

import java.io.IOException;
import java.util.UUID;

import static nikita.common.config.ErrorMessagesConstants.MALFORMED_PAYLOAD;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

/**
 * Deserialise an incoming User JSON object.
 */
public class UserDeserializer
        extends JsonDeserializer<User> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext dc)
            throws IOException {
        StringBuilder errors = new StringBuilder();
        User user = new User();
        ObjectNode objectNode = mapper.readTree(jsonParser);
        // Deserialize systemID
        JsonNode currentNode = objectNode.get(SYSTEM_ID);
        if (null != currentNode) {
            user.setSystemId(UUID.fromString(currentNode.textValue()));
            objectNode.remove(SYSTEM_ID);
        }
        // Deserialize username
        currentNode = objectNode.get(USER_NAME);
        if (null != currentNode) {
            user.setUsername(currentNode.textValue());
            objectNode.remove(USER_NAME);
        }
        // Deserialize password
        currentNode = objectNode.get(PASSWORD);
        if (null != currentNode) {
            user.setPassword(currentNode.textValue());
            objectNode.remove(PASSWORD);
        }
        // Deserialize firstname
        currentNode = objectNode.get(FIRST_NAME);
        if (null != currentNode) {
            user.setFirstname(currentNode.textValue());
            objectNode.remove(FIRST_NAME);
        }
        // Deserialize lastname
        currentNode = objectNode.get(SECOND_NAME);
        if (null != currentNode) {
            user.setLastname(currentNode.textValue());
            objectNode.remove(SECOND_NAME);
        }
        // Deserialize password
        currentNode = objectNode.get(PASSWORD);
        if (null != currentNode) {
            user.setPassword(currentNode.textValue());
            objectNode.remove(PASSWORD);
        }
        deserialiseNoarkCreateEntity(user, objectNode, errors);
        deserialiseNoarkFinaliseEntity(user, objectNode, errors);
        // Check that there are no additional values left after processing the
        // tree. If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append(String.format(MALFORMED_PAYLOAD,
                    USER, checkNodeObjectEmpty(objectNode)));
        }
        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());
        return user;
    }
}
