package nikita.common.util.deserialisers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.metadata.SeriesStatus;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

/**
 * Deserialise an incoming Series JSON object.
 * <p>
 * Note:
 * - Unknown property values in the JSON will trigger an exception
 * - Missing obligatory property values in the JSON will trigger an exception
 */
public class SeriesDeserializer
        extends JsonDeserializer {

    private static final Logger logger =
            LoggerFactory.getLogger(SeriesDeserializer.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Series deserialize(JsonParser jsonParser, DeserializationContext dc)
            throws IOException {
        StringBuilder errors = new StringBuilder();
        Series series = new Series();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialise general properties
        deserialiseNoarkGeneralEntity(series, objectNode, errors);
        deserialiseDocumentMedium(series, objectNode, errors);
        deserialiseStorageLocation(series, objectNode, errors);
        // Deserialize seriesStatus
        SeriesStatus seriesStatus = (SeriesStatus)
                deserialiseMetadataValue(objectNode, SERIES_STATUS,
                        new SeriesStatus(), errors, false);
        series.setSeriesStatus(seriesStatus);
        // Deserialize seriesStartDate
        series.setSeriesStartDate(deserializeDateTime(SERIES_START_DATE,
                objectNode, errors));
        // Deserialize seriesEndDate
        series.setSeriesEndDate(deserializeDateTime(SERIES_END_DATE,
                objectNode, errors));
        // Deserialize referencePrecursor
        JsonNode currentNode = objectNode.get(SERIES_ASSOCIATE_AS_PRECURSOR);
        if (null != currentNode) {
            series.setReferencePrecursorSystemID
                    (UUID.fromString(currentNode.textValue()));
            objectNode.remove(SERIES_ASSOCIATE_AS_PRECURSOR);
        }
        // Deserialize referenceSuccessor
        currentNode = objectNode.get(SERIES_ASSOCIATE_AS_SUCCESSOR);
        if (null != currentNode) {
            series.setReferenceSuccessorSystemID
                    (UUID.fromString(currentNode.textValue()));
            objectNode.remove(SERIES_ASSOCIATE_AS_SUCCESSOR);
        }
        series.setReferenceDisposal(deserialiseDisposal(objectNode, errors));
        series.setDisposalUndertaken(
                deserialiseDisposalUndertaken(objectNode, errors));
        series.setReferenceDeletion(deserialiseDeletion(objectNode, errors));
        series.setReferenceScreening(deserialiseScreening(objectNode, errors));
        series.setReferenceClassified(
                deserialiseClassified(objectNode, errors));

        currentNode = objectNode.get(LINKS);
        if (null != currentNode) {
            logger.debug("Payload contains " + LINKS + ". " +
                    "This value is being ignored.");
            objectNode.remove(LINKS);
        }

        // Check that there are no additional values left after processing the tree
        // If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The arkivdel you tried to create is malformed. ");
            errors.append("The following fields are not recognised as ");
            errors.append(" arkivdel fields [");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("]. ");
        }

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return series;
    }
}
