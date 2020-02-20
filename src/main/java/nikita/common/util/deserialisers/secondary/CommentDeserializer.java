package nikita.common.util.deserialisers.secondary;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.metadata.CommentType;
import nikita.common.model.noark5.v5.secondary.Comment;
import nikita.common.util.deserialisers.secondary.CommentDeserializer;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

public class CommentDeserializer
        extends JsonDeserializer {

    private static final Logger logger =
            LoggerFactory.getLogger(CommentDeserializer.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Comment deserialize(
            JsonParser jsonParser, DeserializationContext dc)
            throws IOException {

        StringBuilder errors = new StringBuilder();
        Comment comment = new Comment();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialize systemID
        deserialiseNoarkSystemIdEntity(comment, objectNode, errors);
        // Deserialize merknadstekst
        JsonNode currentNode = objectNode.get(COMMENT_TEXT);
        if (null != currentNode) {
            comment.setCommentText(currentNode.textValue());
            objectNode.remove(COMMENT_TEXT);
        } else {
            errors.append(COMMENT_TEXT + " is missing. ");
        }
        // Deserialize merknadstype
        CommentType commentType = (CommentType)
            deserialiseMetadataValue(
                objectNode,
                COMMENT_TYPE,
                new CommentType(),
                errors, false);
        if (null != commentType.getCode())
            comment.setCommentType(commentType);
        // Deserialize merknadsdato
        comment.setCommentDate
            (deserializeDateTime(COMMENT_DATE, objectNode, errors, false));
        // Deserialize merknadRegistrertAv
        currentNode = objectNode.get(COMMENT_REGISTERED_BY);
        if (null != currentNode) {
            comment.setCommentRegisteredBy(currentNode.textValue());
            objectNode.remove(COMMENT_REGISTERED_BY);
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
            errors.append("The comment you tried to create is malformed. The");
            errors.append(" following fields are not recognised as comment ");
            errors.append(" fields [");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("]. ");
        }
        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return comment;
    }
}
