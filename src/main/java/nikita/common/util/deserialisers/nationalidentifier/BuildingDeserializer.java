package nikita.common.util.deserialisers.nationalidentifier;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.nationalidentifier.Building;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.BUILDING_CHANGE_NUMBER;
import static nikita.common.config.N5ResourceMappings.BUILDING_NUMBER;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;


public class BuildingDeserializer
        extends JsonDeserializer {

    private static final Logger logger =
            LoggerFactory.getLogger(BuildingDeserializer.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Building deserialize(
            JsonParser jsonParser, DeserializationContext dc)
            throws IOException {

        StringBuilder errors = new StringBuilder();
        Building building = new Building();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialize systemID
        deserialiseNoarkSystemIdEntity(building, objectNode, errors);

        // Deserialize bygningsnummer
        building.setBuildingNumber
            (deserializeInteger(BUILDING_NUMBER,
                                objectNode, errors, true));

        // Deserialize endringsloepenummer
        building.setRunningChangeNumber
            (deserializeInteger(BUILDING_CHANGE_NUMBER,
                                objectNode, errors, false));

        JsonNode currentNode = objectNode.get(LINKS);
        if (null != currentNode) {
            logger.debug("Payload contains " + LINKS + ". " +
                    "This value is being ignored.");
            objectNode.remove(LINKS);
        }

        // Check that there are no additional values left after processing the
        // tree. If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The building you tried to create is malformed. The");
            errors.append(" following fields are not recognised as building ");
            errors.append(" fields [");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("]. ");
        }
        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return building;
    }
}
