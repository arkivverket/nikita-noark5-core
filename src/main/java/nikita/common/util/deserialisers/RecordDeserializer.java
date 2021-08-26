package nikita.common.util.deserialisers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.bsm.BSM;
import nikita.common.model.noark5.v5.RecordEntity;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nikita.common.config.ErrorMessagesConstants.MALFORMED_PAYLOAD;
import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

/**
 * Deserialise an incoming Record JSON object.
 */
public class RecordDeserializer
        extends JsonDeserializer<RecordEntity> {

    private static final Logger logger =
            LoggerFactory.getLogger(RecordDeserializer.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public RecordEntity deserialize(JsonParser jsonParser, DeserializationContext dc)
            throws IOException {
        StringBuilder errors = new StringBuilder();
        RecordEntity record = new RecordEntity();

        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialise general properties
        deserialiseSystemIdEntity(record, objectNode, errors);
        // Deserialize archivedBy
        JsonNode currentNode = objectNode.get(RECORD_ARCHIVED_BY);
        if (currentNode != null) {
            record.setArchivedBy(currentNode.textValue());
            objectNode.remove(RECORD_ARCHIVED_BY);
        }
        // Deserialize archivedDate
        record.setArchivedDate(deserializeDateTime(
                RECORD_ARCHIVED_DATE, objectNode, errors));

        // Deserialize general Record properties
        // Deserialize recordId
        currentNode = objectNode.get(RECORD_ID);
        if (null != currentNode) {
            record.setRecordId(currentNode.textValue());
            objectNode.remove(RECORD_ID);
        }
        // Deserialize title (not using utils to preserve order)
        currentNode = objectNode.get(TITLE);
        if (null != currentNode) {
            record.setTitle(currentNode.textValue());
            objectNode.remove(TITLE);
        }
        // Deserialize  publicTitle
        currentNode = objectNode.get(FILE_PUBLIC_TITLE);
        if (null != currentNode) {
            record.setPublicTitle(currentNode.textValue());
            objectNode.remove(FILE_PUBLIC_TITLE);
        }
        // Deserialize description
        currentNode = objectNode.get(DESCRIPTION);
        if (null != currentNode) {
            record.setDescription(currentNode.textValue());
            objectNode.remove(DESCRIPTION);
        }

        deserialiseDocumentMedium(record, objectNode, errors);
        deserialiseKeyword(record, objectNode);
        record.setReferenceClassified(
                deserialiseClassified(objectNode, errors));

        record.setReferenceDisposal(deserialiseDisposal(objectNode, errors));
        record.setReferenceScreening(
                deserialiseScreening(objectNode, errors));

        // Deserialize businessSpecificMetadata (virksomhetsspesifikkeMetadata)
        currentNode = objectNode.get(BSM_DEF);
        if (null != currentNode) {
            BSM base = mapper.readValue(currentNode.traverse(), BSM.class);
            record.addReferenceBSMBase(base.getReferenceBSMBase());
            objectNode.remove(BSM_DEF);
        }

        currentNode = objectNode.get(LINKS);
        if (null != currentNode) {
            logger.debug("Payload contains " + LINKS + ". " +
                    "This value is being ignored.");
            objectNode.remove(LINKS);
        }

        // Check that there are no additional values left after processing
        // the tree. If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append(String.format(MALFORMED_PAYLOAD,
                    RECORD, checkNodeObjectEmpty(objectNode)));
        }

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return record;
    }
}
