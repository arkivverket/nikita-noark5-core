package nikita.common.util.deserialisers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.bsm.BSM;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.metadata.AssociatedWithRecordAs;
import nikita.common.model.noark5.v5.metadata.DocumentStatus;
import nikita.common.model.noark5.v5.metadata.DocumentType;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nikita.common.config.ErrorMessagesConstants.MALFORMED_PAYLOAD;
import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

/**
 * Deserialise an incoming DocumentDescription JSON object.
 */
public class DocumentDescriptionDeserializer
        extends JsonDeserializer<DocumentDescription> {

    private static final Logger logger =
            LoggerFactory.getLogger(DocumentDescriptionDeserializer.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public DocumentDescription deserialize(
            JsonParser jsonParser, DeserializationContext dc)
            throws IOException {
        StringBuilder errors = new StringBuilder();
        DocumentDescription documentDescription = new DocumentDescription();
        ObjectNode objectNode = mapper.readTree(jsonParser);
        // Deserialise general record properties
        deserialiseSystemIdEntity(documentDescription, objectNode, errors);
        deserialiseNoarkTitleDescriptionEntity(documentDescription,
                objectNode, errors);
        // Deserialize documentType
        DocumentType documentType = (DocumentType)
                deserialiseMetadataValue(objectNode,
                        DOCUMENT_DESCRIPTION_DOCUMENT_TYPE,
                        new DocumentType(),
                        errors, true);
        documentDescription.setDocumentType(documentType);
        // Deserialize documentStatus
        DocumentStatus documentStatus = (DocumentStatus)
                deserialiseMetadataValue(objectNode,
                        DOCUMENT_DESCRIPTION_STATUS,
                        new DocumentStatus(),
                        errors, true);
        documentDescription.setDocumentStatus(documentStatus);
        // Deserialize associatedWithRecordAs
        AssociatedWithRecordAs associatedWithRecordAs = (AssociatedWithRecordAs)
                deserialiseMetadataValue(objectNode,
                        DOCUMENT_DESCRIPTION_ASSOCIATED_WITH_RECORD_AS,
                        new AssociatedWithRecordAs(),
                        errors, true);
        documentDescription.setAssociatedWithRecordAs(associatedWithRecordAs);
        // Deserialize documentNumber
        documentDescription.setDocumentNumber
                (deserializeInteger(DOCUMENT_DESCRIPTION_DOCUMENT_NUMBER,
                        objectNode, errors, false));
        // Deserialize associationDate
        documentDescription.setAssociationDate(
                deserializeDateTime(DOCUMENT_DESCRIPTION_ASSOCIATED_DATE,
                        objectNode, errors));
        // Deserialize associatedBy
        JsonNode currentNode =
                objectNode.get(DOCUMENT_DESCRIPTION_ASSOCIATED_BY);
        if (null != currentNode) {
            documentDescription.setAssociatedBy(currentNode.textValue());
            objectNode.remove(DOCUMENT_DESCRIPTION_ASSOCIATED_BY);
        }
        // Deserialize storageLocation, not using
        // deserialiseStorageLocation from CommonUtils, as it is a
        // single string here, while deserialiseStorageLocation()
        // expect a list of strings.
        currentNode = objectNode.get(STORAGE_LOCATION);
        if (null != currentNode) {
            documentDescription.setStorageLocation(currentNode.textValue());
            objectNode.remove(STORAGE_LOCATION);
        }
        // Deserialize general documentDescription properties
        deserialiseDocumentMedium(documentDescription, objectNode, errors);
        currentNode = objectNode.get(DOCUMENT_DESCRIPTION_EXTERNAL_REFERENCE);
        if (null != currentNode) {
            documentDescription.setExternalReference(currentNode.textValue());
            objectNode.remove(DOCUMENT_DESCRIPTION_EXTERNAL_REFERENCE);
        }
        documentDescription.setDisposalUndertaken(
                deserialiseDisposalUndertaken(objectNode, errors));
        documentDescription.setReferenceDisposal(
                deserialiseDisposal(objectNode, errors));
        documentDescription.setReferenceDeletion(
                deserialiseDeletion(objectNode, errors));
        documentDescription.setReferenceScreening(
                deserialiseScreening(objectNode, errors));
        documentDescription.setReferenceClassified(
                deserialiseClassified(objectNode, errors));
        documentDescription.setReferenceElectronicSignature(
                deserialiseElectronicSignature(objectNode, errors));
        // Deserialize businessSpecificMetadata (virksomhetsspesifikkeMetadata)
        currentNode = objectNode.get(BSM_DEF);
        if (null != currentNode) {
            BSM base = mapper.readValue(currentNode.traverse(), BSM.class);
            documentDescription.addReferenceBSMBase(base.getReferenceBSMBase());
            objectNode.remove(BSM_DEF);
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
            errors.append(String.format(MALFORMED_PAYLOAD,
                    DOCUMENT_DESCRIPTION, checkNodeObjectEmpty(objectNode)));
        }
        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());
        return documentDescription;
    }
}
