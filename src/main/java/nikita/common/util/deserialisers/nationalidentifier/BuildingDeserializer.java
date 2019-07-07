package nikita.common.util.deserialisers.nationalidentifier;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.nationalidentifier.Building;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;

import java.io.IOException;
import java.util.UUID;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.checkNodeObjectEmpty;

public class BuildingDeserializer
        extends JsonDeserializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Building deserialize(
            JsonParser jsonParser, DeserializationContext dc)
            throws IOException {

        StringBuilder errors = new StringBuilder();
        Building building = new Building();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialize systemID
        JsonNode currentNode = objectNode.get(SYSTEM_ID);
        if (null != currentNode) {
            building.setSystemId(UUID.fromString(currentNode.textValue()));
            objectNode.remove(SYSTEM_ID);
        }

        // Deserialize bygningsnummer
        currentNode = objectNode.get(BUILDING_NUMBER);
        if (null != currentNode) {
            building.setBuildingNumber(currentNode.intValue());
            objectNode.remove(BUILDING_NUMBER);
        }

        // Deserialize endringsloepenummer
        currentNode = objectNode.get(BUILDING_CHANGE_NUMBER);
        if (null != currentNode) {
            building.setContinuousNumberingOfBuildingChange(
                    currentNode.intValue());
            objectNode.remove(BUILDING_CHANGE_NUMBER);
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
