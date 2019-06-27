package nikita.common.util.deserialisers.casehandling;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

/**
 * Created by tsodring on 29/05/19.
 * <p>
 * Deserialise an incoming RecordNote JSON object.
 * <p>
 * Note:
 * - Unknown property values in the JSON will trigger an exception
 * - Missing obligatory property values in the JSON will trigger an exception
 */
public class RecordNoteDeserializer
        extends JsonDeserializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public RecordNote deserialize(JsonParser jsonParser,
                                  DeserializationContext dc)
            throws IOException {
        StringBuilder errors = new StringBuilder();
        RecordNote recordNote = new RecordNote();

        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialise general recordNote properties
        deserialiseNoarkSystemIdEntity(recordNote, objectNode, errors);
        deserialiseNoarkCreateEntity(recordNote, objectNode, errors);

        // Deserialize archivedBy
        JsonNode currentNode = objectNode.get(RECORD_ARCHIVED_BY);
        if (null != currentNode) {
            recordNote.setArchivedBy(currentNode.textValue());
            objectNode.remove(RECORD_ARCHIVED_BY);
        }
        // Deserialize archivedDate
        recordNote.setArchivedDate(
                deserializeDateTime(RECORD_ARCHIVED_DATE, objectNode, errors));

        // Deserialize general Record properties
        // Deserialize recordId
        currentNode = objectNode.get(RECORD_ID);
        if (null != currentNode) {
            recordNote.setRecordId(currentNode.textValue());
            objectNode.remove(RECORD_ID);
        }
        // Deserialize title (not using utils to preserve order)
        currentNode = objectNode.get(TITLE);
        if (null != currentNode) {
            recordNote.setTitle(currentNode.textValue());
            objectNode.remove(TITLE);
        }
        // Deserialize  officialTitle
        currentNode = objectNode.get(FILE_PUBLIC_TITLE);
        if (null != currentNode) {
            recordNote.setOfficialTitle(currentNode.textValue());
            objectNode.remove(FILE_PUBLIC_TITLE);
        }
        // Deserialize description
        currentNode = objectNode.get(DESCRIPTION);
        if (null != currentNode) {
            recordNote.setDescription(currentNode.textValue());
            objectNode.remove(DESCRIPTION);
        }
        deserialiseDocumentMedium(recordNote, objectNode, errors);

        // Deserialize documentDate
        recordNote.setDocumentDate(
                deserializeDate(REGISTRY_ENTRY_DOCUMENT_DATE, objectNode,
                        errors));

        // Deserialize receivedDate
        recordNote.setReceivedDate(
                deserializeDate(REGISTRY_ENTRY_RECEIVED_DATE, objectNode,
                        errors));

        // Deserialize sentDate
        recordNote.setSentDate(
                deserializeDate(REGISTRY_ENTRY_SENT_DATE, objectNode, errors));

        // Deserialize dueDate
        recordNote.setDueDate(
                deserializeDate(REGISTRY_ENTRY_DUE_DATE, objectNode, errors));

        // Deserialize freedomAssessmentDate
        recordNote.setFreedomAssessmentDate(
                deserializeDate(REGISTRY_ENTRY_RECORD_FREEDOM_ASSESSMENT_DATE,
                        objectNode, errors));

        // Deserialize numberOfAttachments
        currentNode = objectNode.get(REGISTRY_ENTRY_NUMBER_OF_ATTACHMENTS);
        if (null != currentNode) {
            recordNote.setNumberOfAttachments(
                    currentNode.intValue());
            objectNode.remove(REGISTRY_ENTRY_NUMBER_OF_ATTACHMENTS);
        }
        // Deserialize loanedDate
        recordNote.setLoanedDate(
                deserializeDate(CASE_LOANED_DATE, objectNode, errors));

        // Deserialize loanedTo
        currentNode = objectNode.get(CASE_LOANED_TO);
        if (null != currentNode) {
            recordNote.setLoanedTo(currentNode.textValue());
            objectNode.remove(CASE_LOANED_TO);
        }

        // Check that there are no additional values left after processing
        // the tree. If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The arkivnotat you tried to create is malformed. ");
            errors.append("The following fields are not recognised as ");
            errors.append("arkivnotat fields [");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("]. ");
        }
        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return recordNote;
    }
}
