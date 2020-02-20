package nikita.common.util.deserialisers.secondary;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.secondary.Author;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.AUTHOR;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

public class AuthorDeserializer
        extends JsonDeserializer {
    private static final Logger logger =
            LoggerFactory.getLogger(AuthorDeserializer.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Author deserialize(
            JsonParser jsonParser, DeserializationContext dc)
            throws IOException {

        StringBuilder errors = new StringBuilder();
        Author author = new Author();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialize systemID
        deserialiseNoarkSystemIdEntity(author, objectNode, errors);
        deserialiseNoarkCreateEntity(author, objectNode, errors);

        // Deserialize forfatter
        JsonNode currentNode = objectNode.get(AUTHOR);
        if (null != currentNode) {
            author.setAuthor(currentNode.textValue());
            objectNode.remove(AUTHOR);
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
            errors.append("The author you tried to create is malformed. The");
            errors.append(" following fields are not recognised as author ");
            errors.append(" fields [");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("]. ");
        }
        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return author;
    }
}
