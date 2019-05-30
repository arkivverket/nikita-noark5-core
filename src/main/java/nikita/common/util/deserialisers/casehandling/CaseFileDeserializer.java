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

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

/**
 * Created by tsodring on 1/6/17.
 * <p>
 * Deserialise an incoming CaseFile JSON object.
 * <p>
 * Having a own deserialiser is done to have more fine grained control over
 * the input.
 * <p>
 * Note. Currently we do not include 'id' or 'deleted' properties. 'id' is a
 * primary key and it is assumed this is taken care of by the DBMS and
 * 'deleted' is a field internal to the core to handle soft delete. Importing
 * soft deleted objects is something we do not consider necessary.
 * <p>
 * Note:
 * - Unknown property values in the JSON will trigger an exception
 * - Missing obligatory property values in the JSON will trigger an exception
 */
public class CaseFileDeserializer extends JsonDeserializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public CaseFile deserialize(JsonParser jsonParser,
                                DeserializationContext dc)
            throws IOException {
        StringBuilder errors = new StringBuilder();
        CaseFile caseFile = new CaseFile();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialise properties for File
        deserialiseNoarkEntity(caseFile, objectNode, errors);
        deserialiseDocumentMedium(caseFile, objectNode, errors);
        deserialiseStorageLocation(caseFile, objectNode, errors);
        deserialiseKeyword(caseFile, objectNode, errors);

        // Deserialize fileId
        JsonNode currentNode = objectNode.get(FILE_ID);
        if (null != currentNode) {
            caseFile.setFileId(currentNode.textValue());
            objectNode.remove(FILE_ID);
        }

        // Deserialize officialTitle
        currentNode = objectNode.get(FILE_PUBLIC_TITLE);
        if (null != currentNode) {
            caseFile.setOfficialTitle(currentNode.textValue());
            objectNode.remove(FILE_PUBLIC_TITLE);
        }
        caseFile.setReferenceCrossReference(
                deserialiseCrossReferences(objectNode, errors));
        deserialiseComments(caseFile, objectNode, errors);
        caseFile.setReferenceDisposal(
                deserialiseDisposal(objectNode, errors));
        caseFile.setReferenceScreening(
                deserialiseScreening(objectNode, errors));
        caseFile.setReferenceClassified(
                deserialiseClassified(objectNode, errors));

        // Deserialise general properties for CaseFile
        // Deserialize caseYear
        currentNode = objectNode.get(CASE_YEAR);
        if (null != currentNode) {
            caseFile.setCaseYear(currentNode.intValue());
            objectNode.remove(CASE_YEAR);
        }

        // Deserialize caseSequenceNumber
        currentNode = objectNode.get(CASE_SEQUENCE_NUMBER);
        if (null != currentNode) {
            caseFile.setCaseSequenceNumber(currentNode.intValue());
            objectNode.remove(CASE_SEQUENCE_NUMBER);
        }

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

        // Deserialize caseStatus
        currentNode = objectNode.get(CASE_STATUS);
        if (null != currentNode) {
            caseFile.setCaseStatus(currentNode.textValue());
            objectNode.remove(CASE_STATUS);
        }

        // Deserialize loanedDate
        caseFile.setLoanedDate(deserializeDate(
                CASE_LOANED_DATE, objectNode, errors));

        // Deserialize loanedTo
        currentNode = objectNode.get(CASE_LOANED_TO);
        if (null != currentNode) {
            caseFile.setLoanedTo(currentNode.textValue());
            objectNode.remove(CASE_LOANED_TO);
        }

        // Deserialize referenceSeries
        currentNode = objectNode.get(REFERENCE_SERIES);
        if (null != currentNode) {
            Series series = new Series();
            String systemID = currentNode.textValue();
            if (systemID != null) {
                series.setSystemId(systemID);
            }
            caseFile.setReferenceSeries(series);
            objectNode.remove(REFERENCE_SERIES);
        }

        // Check that there are no additional values left after processing the
        // tree. If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The saksmappe object you tried to create is  ");
            errors.append("malformed. The following fields are not recognised");
            errors.append("as saksmappe fields [");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("].");
        }

        caseFile.setReferenceParty(
                deserialiseCaseParties(objectNode, errors));
        caseFile.setReferencePrecedence(
                deserialisePrecedences(objectNode, errors));

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return caseFile;
    }
}
