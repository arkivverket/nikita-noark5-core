package nikita.common.util.deserialisers.secondary;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.secondary.Conversion;
import nikita.common.model.noark5.v5.metadata.Format;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

public class ConversionDeserializer
        extends JsonDeserializer {

    private static final Logger logger =
            LoggerFactory.getLogger(ConversionDeserializer.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Conversion deserialize(
            JsonParser jsonParser, DeserializationContext dc)
            throws IOException {

        StringBuilder errors = new StringBuilder();
        Conversion conversion = new Conversion();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialize systemID
        deserialiseNoarkSystemIdEntity(conversion, objectNode, errors);
        deserialiseNoarkCreateEntity(conversion, objectNode, errors);
        // Deserialize konvertertDato
        conversion.setConvertedDate(deserializeDateTime(
                CONVERTED_DATE, objectNode, errors));
        // Deserialize konvertertAv
        JsonNode currentNode = objectNode.get(CONVERTED_BY);
        if (null != currentNode) {
            conversion.setConvertedBy(currentNode.textValue());
            objectNode.remove(CONVERTED_BY);
        }
        // Deserialize konvertertFraFormat
        Format fromFormat = (Format)
            deserialiseMetadataValue(
                objectNode,
                CONVERTED_FROM_FORMAT,
                new Format(),
                errors, true);
        if (null != fromFormat.getCode())
            conversion.setConvertedFromFormat(fromFormat);
        // Deserialize konvertertTilFormat
        Format toFormat = (Format)
            deserialiseMetadataValue(
                objectNode,
                CONVERTED_TO_FORMAT,
                new Format(),
                errors, true);
        if (null != toFormat.getCode())
            conversion.setConvertedToFormat(toFormat);
	// Deserialize konverteringsverktoey
        currentNode = objectNode.get(CONVERSION_TOOL);
        if (null != currentNode) {
            conversion.setConversionTool(currentNode.textValue());
            objectNode.remove(CONVERSION_TOOL);
        }
	// Deserialize konverteringskommentar
        currentNode = objectNode.get(CONVERSION_COMMENT);
        if (null != currentNode) {
            conversion.setConversionComment(currentNode.textValue());
            objectNode.remove(CONVERSION_COMMENT);
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
            errors.append("The conversion you tried to create is malformed. The");
            errors.append(" following fields are not recognised as conversion ");
            errors.append(" fields [");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("]. ");
        }
        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return conversion;
    }
}
