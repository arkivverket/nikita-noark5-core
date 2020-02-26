package nikita.common.util.deserialisers.secondary;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.metadata.FlowStatus;
import nikita.common.model.noark5.v5.secondary.DocumentFlow;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

public class DocumentFlowDeserializer
        extends JsonDeserializer {

    private static final Logger logger =
            LoggerFactory.getLogger(DocumentFlowDeserializer.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public DocumentFlow deserialize(
            JsonParser jsonParser, DeserializationContext dc)
            throws IOException {

        StringBuilder errors = new StringBuilder();
        DocumentFlow documentFlow = new DocumentFlow();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialise systemID
        deserialiseNoarkSystemIdEntity(documentFlow, objectNode, errors);
        // Deserialise flytTil
        JsonNode currentNode = objectNode.get(DOCUMENT_FLOW_FLOW_TO);
        if (null != currentNode) {
            documentFlow.setFlowTo(currentNode.textValue());
            objectNode.remove(DOCUMENT_FLOW_FLOW_TO);
        } else {
            errors.append(DOCUMENT_FLOW_FLOW_TO + " is missing. ");
	}
        // Deserialise referanseFlytTil
        currentNode = objectNode.get(DOCUMENT_FLOW_REFERENCE_FLOW_TO);
        if (null != currentNode) {
            documentFlow.setReferenceFlowToSystemID
                (UUID.fromString(currentNode.textValue()));
            objectNode.remove(DOCUMENT_FLOW_REFERENCE_FLOW_TO);
        }
        // Deserialise flytFra
        currentNode = objectNode.get(DOCUMENT_FLOW_FLOW_FROM);
        if (null != currentNode) {
            documentFlow.setFlowFrom(currentNode.textValue());
            objectNode.remove(DOCUMENT_FLOW_FLOW_FROM);
        } else {
            errors.append(DOCUMENT_FLOW_FLOW_FROM + " is missing. ");
        }
        // Deserialise referanseFlytFra
        currentNode = objectNode.get(DOCUMENT_FLOW_REFERENCE_FLOW_FROM);
        if (null != currentNode) {
            documentFlow.setReferenceFlowFromSystemID
                (UUID.fromString(currentNode.textValue()));
            objectNode.remove(DOCUMENT_FLOW_REFERENCE_FLOW_FROM);
        }
        // Deserialise flytMottattDato
        documentFlow.setFlowReceivedDate(deserializeDateTime(
                DOCUMENT_FLOW_FLOW_RECEIVED_DATE, objectNode, errors));
        // Deserialise flytSendtDato
        documentFlow.setFlowSentDate(deserializeDateTime(
                DOCUMENT_FLOW_FLOW_SENT_DATE, objectNode, errors));
        // Deserialise flytStatus
	FlowStatus entity = (FlowStatus)
	    deserialiseMetadataValue(objectNode,
				     DOCUMENT_FLOW_FLOW_STATUS,
				     new FlowStatus(),
				     errors, true);
	documentFlow.setFlowStatus(entity);
        // Deserialise flytMerknad
        currentNode = objectNode.get(DOCUMENT_FLOW_FLOW_COMMENT);
        if (null != currentNode) {
            documentFlow.setFlowComment(currentNode.textValue());
            objectNode.remove(DOCUMENT_FLOW_FLOW_COMMENT);
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
            errors.append("The documentFlow you tried to create is malformed.");
            errors.append(" The following fields are not recognised as");
            errors.append(" documentFlow fields [");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("]. ");
        }
        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return documentFlow;
    }
}
