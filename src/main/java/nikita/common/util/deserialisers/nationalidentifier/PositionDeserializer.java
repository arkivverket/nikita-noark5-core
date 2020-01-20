package nikita.common.util.deserialisers.nationalidentifier;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.metadata.CoordinateSystem;
import nikita.common.model.noark5.v5.nationalidentifier.Position;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

public class PositionDeserializer
        extends JsonDeserializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Position deserialize(
            JsonParser jsonParser, DeserializationContext dc)
            throws IOException {

        StringBuilder errors = new StringBuilder();
        Position position = new Position();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialize systemID
        deserialiseNoarkSystemIdEntity(position, objectNode, errors);

        // Deserialize koordinatsystem
        IMetadataEntity entity =
            deserialiseMetadataValue(
                objectNode,
                COORDINATE_SYSTEM,
                new CoordinateSystem(),
                errors, true);
        position.setCoordinateSystemCode(entity.getCode());
        position.setCoordinateSystemCodeName(entity.getCodeName());

        // Deserialize
        JsonNode currentNode = objectNode.get(X);
        if (null != currentNode) {
            position.setX(currentNode.doubleValue());
            objectNode.remove(X);
        }

        // Deserialize
        currentNode = objectNode.get(Y);
        if (null != currentNode) {
            position.setY(currentNode.doubleValue());
            objectNode.remove(Y);
        }

        // Deserialize
        currentNode = objectNode.get(Z);
        if (null != currentNode) {
            position.setZ(currentNode.doubleValue());
            objectNode.remove(Z);
        }

        // Check that there are no additional values left after processing the
        // tree. If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The position you tried to create is malformed. The");
            errors.append(" following fields are not recognised as position ");
            errors.append(" fields [");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("]. ");
        }
        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return position;
    }
}
