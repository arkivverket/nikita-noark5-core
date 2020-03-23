package nikita.common.util.deserialisers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.ChangeLog;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

public class ChangeLogDeserializer
        extends JsonDeserializer {

    private static final Logger logger =
            LoggerFactory.getLogger(ChangeLogDeserializer.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void
    deserializeChangeLog(ChangeLog changeLog, ObjectNode objectNode,
                         StringBuilder errors)
            throws IOException {

        deserialiseSystemIdEntity(changeLog, objectNode, errors);

        UUID referenceArchiveUnit =
            deserializeUUID(REFERENCE_ARCHIVE_UNIT,
                            objectNode, errors, false);
        if (null != referenceArchiveUnit) {
            changeLog.setReferenceArchiveUnitSystemId
                (referenceArchiveUnit);
        }

        JsonNode currentNode = objectNode.get(REFERENCE_METADATA);
        if (null != currentNode) {
            changeLog.setReferenceMetadata(currentNode.textValue());
            objectNode.remove(REFERENCE_METADATA);
        }

        changeLog.setChangedDate
            (deserializeDateTime(CHANGED_DATE, objectNode, errors));

        currentNode = objectNode.get(CHANGED_BY);
        if (null != currentNode) {
            changeLog.setChangedBy(currentNode.textValue());
            objectNode.remove(CHANGED_BY);
        }

        currentNode = objectNode.get(REFERENCE_CHANGED_BY);
        if (null != currentNode) {
            changeLog.setReferenceChangedBy(currentNode.textValue());
            objectNode.remove(REFERENCE_CHANGED_BY);
        }

        currentNode = objectNode.get(OLD_VALUE);
        if (null != currentNode) {
            changeLog.setOldValue(currentNode.textValue());
            objectNode.remove(OLD_VALUE);
        }

        currentNode = objectNode.get(NEW_VALUE);
        if (null != currentNode) {
            changeLog.setNewValue(currentNode.textValue());
            objectNode.remove(NEW_VALUE);
        }
    }

    @Override
    public ChangeLog deserialize(JsonParser jsonParser, DeserializationContext dc)
            throws IOException {
        StringBuilder errors = new StringBuilder();

        ChangeLog changeLog = new ChangeLog();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        deserializeChangeLog(changeLog, objectNode, errors);

        JsonNode currentNode = objectNode.get(LINKS);
        if (null != currentNode) {
            logger.debug("Payload contains " + LINKS + ". " +
                    "This value is being ignored.");
            objectNode.remove(LINKS);
        }
        // Check that there are no additional values left after processing the tree
        // If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The " + CHANGE_LOG + " entry you tried to create " +
                          "is malformed. The following fields are not " +
                          "recognised as " + CHANGE_LOG + " fields [" +
                    CommonUtils.Hateoas.Deserialize.checkNodeObjectEmpty(objectNode) + "]. ");
        }

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return changeLog;
    }
}
