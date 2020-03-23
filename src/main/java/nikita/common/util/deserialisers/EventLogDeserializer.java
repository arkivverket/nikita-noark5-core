package nikita.common.util.deserialisers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.EventLog;
import nikita.common.model.noark5.v5.metadata.EventType;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

public class EventLogDeserializer
        extends JsonDeserializer {

    private static final Logger logger =
            LoggerFactory.getLogger(EventLogDeserializer.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public EventLog deserialize(JsonParser jsonParser, DeserializationContext dc)
            throws IOException {
        StringBuilder errors = new StringBuilder();

        EventLog eventLog = new EventLog();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        ChangeLogDeserializer.deserializeChangeLog
            (eventLog, objectNode, errors);

        EventType eventType = (EventType)
            deserialiseMetadataValue(objectNode,
                                     EVENT_TYPE,
                                     new EventType(),
                                     errors, true);
        eventLog.setEventType(eventType);

        JsonNode currentNode = objectNode.get(DESCRIPTION);
        if (null != currentNode) {
            eventLog.setDescription(currentNode.textValue());
            objectNode.remove(DESCRIPTION);
        }

        eventLog.setEventDate
            (deserializeDateTime(EVENT_DATE, objectNode, errors));

        currentNode = objectNode.get(LINKS);
        if (null != currentNode) {
            logger.debug("Payload contains " + LINKS + ". " +
                    "This value is being ignored.");
            objectNode.remove(LINKS);
        }
        // Check that there are no additional values left after processing the tree
        // If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The " + EVENT_LOG + " entry you tried to create " +
                          "is malformed. The following fields are not " +
                          "recognised as " + EVENT_LOG + " fields [" +
                    CommonUtils.Hateoas.Deserialize.checkNodeObjectEmpty(objectNode) + "]. ");
        }

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return eventLog;
    }
}
