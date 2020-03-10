package nikita.common.util.deserialisers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.Record;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

/**
 * Created by tsodring on 1/6/17.
 * <p>
 * Deserialise an incoming Record JSON object.
 * <p>
 * Having a own deserialiser is done to have more fine grained control over the input. This allows us to be less strict
 * with property names, allowing for both English and Norwegian property names
 * <p>
 * <p>
 * <p>
 * Note this implementation expects that the Record object to deserialise is in compliance with the Noark standard where
 * certain properties i.e. createdBy and createdDate are set by the core, not the caller. This deserializer will not
 * enforce this and will deserialize a record object correctly. This is because e.g the import interface will require
 * such functionality.
 * <p>
 * - Testing of compliance of properties is handled by the core, either in RecordController or RecordService
 * <p>
 * Note. Currently we do not include 'id' or 'deleted' properties. 'id' is a primary key and it is assumed this is
 * taken care of by the DBMS and 'deleted' is a field internal to the core to handle soft delete. Importing soft deleted
 * objects is something we do not consider necessary.
 * <p>
 * Note:
 * - Unknown property values in the JSON will trigger an exception
 * - Missing obligatory property values in the JSON will trigger an exception
 * - Record has no obligatory values required to be present at instantiation time
 */
public class RecordDeserializer
        extends JsonDeserializer {

    private static final Logger logger =
            LoggerFactory.getLogger(RecordDeserializer.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Record deserialize(JsonParser jsonParser, DeserializationContext dc)
            throws IOException {
        StringBuilder errors = new StringBuilder();
        Record record = new Record();

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
        deserialiseKeyword(record, objectNode, errors);
        record.setReferenceClassified(
		deserialiseClassified(objectNode, errors));

        record.setReferenceDisposal(deserialiseDisposal(objectNode, errors));
        record.setReferenceScreening(
                deserialiseScreening(objectNode, errors));

        currentNode = objectNode.get(LINKS);
        if (null != currentNode) {
            logger.debug("Payload contains " + LINKS + ". " +
                    "This value is being ignored.");
            objectNode.remove(LINKS);
        }

        // Check that there are no additional values left after processing
        // the tree. If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The registrering you tried to create is " +
                          "malformed. The following fields are not " +
                          "recognised as registrering fields " +
                          "[" + checkNodeObjectEmpty(objectNode) + "].");
        }

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return record;
    }
}
