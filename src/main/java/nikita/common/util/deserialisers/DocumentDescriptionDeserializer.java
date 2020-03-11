package nikita.common.util.deserialisers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.metadata.AssociatedWithRecordAs;
import nikita.common.model.noark5.v5.metadata.DocumentStatus;
import nikita.common.model.noark5.v5.metadata.DocumentType;
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
 * Deserialise an incoming DocumentDescription JSON object.
 * <p>
 * Having a own deserialiser is done to have more fine grained control over the
 * input.
 * <p>
 * Note this implementation expects that the DocumentDescription object to
 * deserialise is in compliance with the Noark standard where certain
 * properties i.e. createdBy and createdDate are set by the core, not the
 * caller. This deserializer will not enforce this and will deserialize a
 * documentDescription object correctly. This is because e.g the import
 * interface will require such functionality.
 * <p>
 * - Testing of compliance of properties is handled by the core, either in
 * DocumentDescriptionController or DocumentDescriptionService
 *
 * Note:
 * - Unknown property values in the JSON will trigger an exception
 * - Missing obligatory property values in the JSON will trigger an exception
 * - DocumentDescription has no obligatory values required to be present at
 *   instantiation time
 */
public class DocumentDescriptionDeserializer extends JsonDeserializer {

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
        documentDescription.setReferenceDisposalUndertaken(
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

        currentNode = objectNode.get(LINKS);
        if (null != currentNode) {
            logger.debug("Payload contains " + LINKS + ". " +
                    "This value is being ignored.");
            objectNode.remove(LINKS);
        }

        // Check that there are no additional values left after processing the
        // tree. If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The dokumentbeskrivelse you tried to create is " +
                          "malformed. The following fields are not recognised " +
                          "as dokumentbeskrivelse fields " +
                          "[" + checkNodeObjectEmpty(objectNode) + "].");
        }

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return documentDescription;
    }
}
