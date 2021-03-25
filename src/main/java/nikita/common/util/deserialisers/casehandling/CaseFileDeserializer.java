package nikita.common.util.deserialisers.casehandling;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

/**
 * Deserialise an incoming CaseFile JSON object.
 * Note:
 * - Unknown property values in the JSON will trigger an exception
 * - Missing obligatory property values in the JSON will trigger an exception
 */
public class CaseFileDeserializer
        extends JsonDeserializer {

    private static final Logger logger =
            LoggerFactory.getLogger(CaseFileDeserializer.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public CaseFile deserialize(JsonParser jsonParser,
                                DeserializationContext dc)
            throws IOException {
        StringBuilder errors = new StringBuilder();
        CaseFile caseFile = new CaseFile();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialise properties for File
        deserialiseNoarkGeneralEntity(caseFile, objectNode, errors);
        deserialiseDocumentMedium(caseFile, objectNode, errors);
        deserialiseStorageLocation(caseFile, objectNode, errors);

        // Deserialize fileId
        JsonNode currentNode = objectNode.get(FILE_ID);
        if (null != currentNode) {
            caseFile.setFileId(currentNode.textValue());
            objectNode.remove(FILE_ID);
        }

        // Deserialize publicTitle
        currentNode = objectNode.get(FILE_PUBLIC_TITLE);
        if (null != currentNode) {
            caseFile.setPublicTitle(currentNode.textValue());
            objectNode.remove(FILE_PUBLIC_TITLE);
        }

        // Deserialise general properties for CaseFile
        // Deserialize caseYear
        caseFile.setCaseYear
            (deserializeInteger(CASE_YEAR,
                                objectNode, errors, false));

        // Deserialize caseSequenceNumber
        caseFile.setCaseSequenceNumber
            (deserializeInteger(CASE_SEQUENCE_NUMBER,
                                objectNode, errors, false));

        // Deserialize caseDate
        caseFile.setCaseDate(deserializeDate(CASE_DATE, objectNode, errors));

        // Deserialize caseResponsible
        currentNode = objectNode.get(CASE_RESPONSIBLE);
        if (null != currentNode) {
            caseFile.setCaseResponsible(currentNode.textValue());
            objectNode.remove(CASE_RESPONSIBLE);
        }

        // Deserialize recordsManagementUnit
        currentNode = objectNode.get(CASE_RECORDS_MANAGEMENT_UNIT);
        if (null != currentNode) {
            caseFile.setRecordsManagementUnit(currentNode.textValue());
            objectNode.remove(CASE_RECORDS_MANAGEMENT_UNIT);
        }

        deserialiseCaseStatus(caseFile, objectNode, errors);

        // Deserialize loanedDate
        caseFile.setLoanedDate(deserializeDate(
                CASE_LOANED_DATE, objectNode, errors));

        // Deserialize loanedTo
        currentNode = objectNode.get(CASE_LOANED_TO);
        if (null != currentNode) {
            caseFile.setLoanedTo(currentNode.textValue());
            objectNode.remove(CASE_LOANED_TO);
        }

        caseFile.setReferenceCrossReference(
                deserialiseCrossReferences(caseFile, objectNode, errors));
        caseFile.setReferenceDisposal(
                deserialiseDisposal(objectNode, errors));
        caseFile.setReferenceScreening(
                deserialiseScreening(objectNode, errors));
        caseFile.setReferenceClassified(
                deserialiseClassified(objectNode, errors));


        // Deserialize referenceSeries
        currentNode = objectNode.get(REFERENCE_SERIES);
        if (null != currentNode) {
            Series series = new Series();
            String systemID = currentNode.textValue();
            if (systemID != null) {
                series.setSystemId(UUID.fromString(systemID));
            }
            caseFile.setReferenceSeries(series);
            objectNode.remove(REFERENCE_SERIES);
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
            errors.append("The saksmappe object you tried to create is ");
            errors.append("malformed. The following fields are not ");
            errors.append("recognised as saksmappe fields [");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("].");
        }

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return caseFile;
    }
}
