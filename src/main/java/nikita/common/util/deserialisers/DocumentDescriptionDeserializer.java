package nikita.common.util.deserialisers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.secondary.Author;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

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
        deserialiseNoarkSystemIdEntity(documentDescription, objectNode, errors);
        deserialiseNoarkCreateEntity(documentDescription, objectNode, errors);
        deserialiseNoarkTitleDescriptionEntity(documentDescription,
                objectNode, errors);

        // Deserialize documentType
        JsonNode currentNode = objectNode.get(
                DOCUMENT_DESCRIPTION_DOCUMENT_TYPE);
        if (null != currentNode) {
            documentDescription.setDocumentType(currentNode.textValue());
            objectNode.remove(DOCUMENT_DESCRIPTION_DOCUMENT_TYPE);
        }

        // Deserialize documentStatus
        currentNode = objectNode.get(DOCUMENT_DESCRIPTION_STATUS);
        if (null != currentNode) {
            documentDescription.setDocumentStatus(currentNode.textValue());
            objectNode.remove(DOCUMENT_DESCRIPTION_STATUS);
        }

        // Deserialize associatedWithRecordAs
        currentNode = objectNode.get(
                DOCUMENT_DESCRIPTION_ASSOCIATED_WITH_RECORD_AS);
        if (null != currentNode) {
            documentDescription.setAssociatedWithRecordAs(
                    currentNode.textValue());
            objectNode.remove(DOCUMENT_DESCRIPTION_ASSOCIATED_WITH_RECORD_AS);
        }

        // Deserialize documentNumber
        currentNode = objectNode.get(DOCUMENT_DESCRIPTION_DOCUMENT_NUMBER);
        if (null != currentNode) {
            documentDescription.setDocumentNumber(
                    currentNode.intValue());
            objectNode.remove(DOCUMENT_DESCRIPTION_DOCUMENT_NUMBER);
        }

        // Deserialize associationDate
        documentDescription.setAssociationDate(
                deserializeDate(DOCUMENT_DESCRIPTION_ASSOCIATION_DATE,
                objectNode, errors));

        // Deserialize associatedBy
        currentNode = objectNode.get(DOCUMENT_DESCRIPTION_ASSOCIATED_BY);
        if (null != currentNode) {
            documentDescription.setAssociatedBy(currentNode.textValue());
            objectNode.remove(DOCUMENT_DESCRIPTION_ASSOCIATED_BY);
        }

        // Deserialize storageLocation
        currentNode = objectNode.get(STORAGE_LOCATION);
        if (null != currentNode) {
            documentDescription.setStorageLocation(currentNode.textValue());
            objectNode.remove(STORAGE_LOCATION);
        }

        List<JsonNode> authors = objectNode.findValues(AUTHOR);
        for (JsonNode author : authors) {
            if (null != author) {
                Author authorObj = new Author();
                authorObj.setAuthor(author.textValue());
                documentDescription.addReferenceAuthor(authorObj);
                objectNode.remove(AUTHOR);
            }
        }

        // Deserialize general documentDescription properties
        deserialiseDocumentMedium(documentDescription, objectNode, errors);

        currentNode = objectNode.get(LINKS);
        if (null != currentNode) {
            logger.info("Payload contains " + currentNode.textValue() + ". " +
                    "This value is being ignored.");
        }

        // Check that there are no additional values left after processing the
        // tree. If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The dokumentbeskrivelse you tried to create is");
            errors.append("malformed. The following fields are not recognised");
            errors.append("as dokumentbeskrivelse fields[");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("]. ");
        }

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return documentDescription;
    }
}
