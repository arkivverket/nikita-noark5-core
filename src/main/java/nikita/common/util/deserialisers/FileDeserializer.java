package nikita.common.util.deserialisers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.bsm.BSM;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Series;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

import static nikita.common.config.ErrorMessagesConstants.MALFORMED_PAYLOAD;
import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

/**
 * Deserialise an incoming File JSON object.
 * Note:
 * - Unknown property values in the JSON will trigger an exception
 * - Missing obligatory property values in the JSON will trigger an exception
 */
public class FileDeserializer
        extends JsonDeserializer<File> {

    private static final Logger logger =
            LoggerFactory.getLogger(FileDeserializer.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public File deserialize(JsonParser jsonParser, DeserializationContext dc)
            throws IOException {
        StringBuilder errors = new StringBuilder();
        File file = new File();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialise general properties
        deserialiseNoarkGeneralEntity(file, objectNode, errors);
        deserialiseDocumentMedium(file, objectNode, errors);
        deserialiseStorageLocation(file, objectNode);
        deserialiseKeyword(file, objectNode);

        // Deserialize fileId
        JsonNode currentNode = objectNode.get(FILE_ID);
        if (null != currentNode) {
            file.setFileId(currentNode.textValue());
            objectNode.remove(FILE_ID);
        }
        // Deserialize publicTitle
        currentNode = objectNode.get(FILE_PUBLIC_TITLE);
        if (null != currentNode) {
            file.setPublicTitle(currentNode.textValue());
            objectNode.remove(FILE_PUBLIC_TITLE);
        }
        // TODO: FIX THIS CommondeserialiseCrossReference(file, objectNode);
        file.setReferenceDisposal(deserialiseDisposal(objectNode, errors));
        file.setReferenceScreening(deserialiseScreening(objectNode, errors));
        file.setReferenceClassified(deserialiseClassified(objectNode, errors));

        // Deserialize referenceSeries
        currentNode = objectNode.get(REFERENCE_SERIES);
        if (null != currentNode) {
            Series series = new Series();
            String systemID = currentNode.textValue();
            if (systemID != null) {
                series.setSystemId(UUID.fromString(systemID));
            }
            file.setReferenceSeries(series);
            objectNode.remove(REFERENCE_SERIES);
        }

        // Deserialize businessSpecificMetadata (virksomhetsspesifikkeMetadata)
        currentNode = objectNode.get(BSM_DEF);
        if (null != currentNode) {
            nikita.common.model.noark5.bsm.BSM base =
                    mapper.readValue(currentNode.traverse(), BSM.class);
            file.addReferenceBSMBase(base.getReferenceBSMBase());
            objectNode.remove(BSM_DEF);
        }

        currentNode = objectNode.get(LINKS);
        if (null != currentNode) {
            logger.debug("Payload contains " + LINKS + ". " +
                    "This value is being ignored.");
            objectNode.remove(LINKS);
        }
        // Check that there are no additional values left after processing the tree
        // If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append(String.format(MALFORMED_PAYLOAD,
                    FILE, checkNodeObjectEmpty(objectNode)));
        }

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return file;
    }
}
