package nikita.common.util.deserialisers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.metadata.Format;
import nikita.common.model.noark5.v5.metadata.VariantFormat;
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
 * Deserialise an incoming DocumentObject JSON object.
 * <p>
 * Note:
 * - Unknown property values in the JSON will trigger an exception
 * - Missing obligatory property values in the JSON will trigger an exception
 * - DocumentObject has no obligatory values required to be present at
 * instantiation time
 */
public class DocumentObjectDeserializer
        extends JsonDeserializer {

    private static final Logger logger =
            LoggerFactory.getLogger(DocumentObjectDeserializer.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public DocumentObject deserialize(JsonParser jsonParser,
                                      DeserializationContext dc)
            throws IOException {
        StringBuilder errors = new StringBuilder();
        DocumentObject documentObject = new DocumentObject();

        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialise general DocumentObject properties
        deserialiseSystemIdEntity(documentObject, objectNode, errors);
        // Deserialize versionNumber
        documentObject.setVersionNumber
            (deserializeInteger(DOCUMENT_OBJECT_VERSION_NUMBER, objectNode,
                                errors, false));
        // Deserialize variantFormat
        IMetadataEntity entity =
            deserialiseMetadataValue(objectNode,
                                     DOCUMENT_OBJECT_VARIANT_FORMAT,
                                     new VariantFormat(),
                                     errors, true);
        documentObject.setVariantFormat((VariantFormat)entity);
        // Deserialize format
        entity = deserialiseMetadataValue(objectNode, DOCUMENT_OBJECT_FORMAT,
                                     new Format(), errors, false);
        documentObject.setFormat((Format)entity);
        // Deserialize formatDetails
        JsonNode currentNode = objectNode.get(DOCUMENT_OBJECT_FORMAT_DETAILS);
        if (null != currentNode) {
            documentObject.setFormatDetails(currentNode.textValue());
            objectNode.remove(DOCUMENT_OBJECT_FORMAT_DETAILS);
        }
        // Deserialize checksum
        currentNode = objectNode.get(DOCUMENT_OBJECT_CHECKSUM);
        if (null != currentNode) {
            documentObject.setChecksum(currentNode.textValue());
            objectNode.remove(DOCUMENT_OBJECT_CHECKSUM);
        }
        // Deserialize checksumAlgorithm
        currentNode = objectNode.get(DOCUMENT_OBJECT_CHECKSUM_ALGORITHM);
        if (null != currentNode) {
            documentObject.setChecksumAlgorithm(currentNode.textValue());
            objectNode.remove(DOCUMENT_OBJECT_CHECKSUM_ALGORITHM);
        }
        // Deserialize fileSize
        documentObject.setFileSize
            (deserializeLong(DOCUMENT_OBJECT_FILE_SIZE, objectNode,
                                errors, false));
        // Deserialize filename
        currentNode = objectNode.get(DOCUMENT_OBJECT_FILE_NAME);
        if (null != currentNode) {
            documentObject.setOriginalFilename(currentNode.textValue());
            objectNode.remove(DOCUMENT_OBJECT_FILE_NAME);
        }
        // Deserialize mimeType
        currentNode = objectNode.get(DOCUMENT_OBJECT_MIME_TYPE);
        if (null != currentNode) {
            documentObject.setMimeType(currentNode.textValue());
            objectNode.remove(DOCUMENT_OBJECT_MIME_TYPE);
        }

        documentObject.setReferenceElectronicSignature(
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
            errors.append("The dokumentobjekt you tried to create is " +
                          "malformed. The following fields are not " +
                          "recognised as dokumentobjekt fields " +
                          "[" + checkNodeObjectEmpty(objectNode) + "].");
        }

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return documentObject;
    }
}
