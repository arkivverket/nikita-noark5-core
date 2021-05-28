package nikita.common.util.deserialisers.admin;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;

import java.io.IOException;
import java.util.UUID;

import static nikita.common.config.ErrorMessagesConstants.MALFORMED_PAYLOAD;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

/**
 * Deserialise an incoming AdministrativeUnit JSON object.
 * <p>
 * Note:
 * - Unknown property values in the JSON will trigger an exception
 */
public class AdministrativeUnitDeserializer
        extends JsonDeserializer<AdministrativeUnit> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public AdministrativeUnit deserialize(JsonParser jsonParser,
                                          DeserializationContext dc)
            throws IOException {
        StringBuilder errors = new StringBuilder();

        AdministrativeUnit administrativeUnit = new AdministrativeUnit();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialise general properties
        deserialiseNoarkSystemIdEntity(administrativeUnit, objectNode);
        deserialiseNoarkCreateEntity(administrativeUnit, objectNode, errors);
        deserialiseNoarkFinaliseEntity(administrativeUnit, objectNode, errors);

        // Deserialize administrativeUnitStatus
        JsonNode currentNode = objectNode.get(ADMINISTRATIVE_UNIT_STATUS);
        if (currentNode != null) {
            administrativeUnit.setAdministrativeUnitStatus(currentNode.textValue());
            objectNode.remove(ADMINISTRATIVE_UNIT_STATUS);
        }

        // Deserialize administrativeUnitName
        currentNode = objectNode.get(ADMINISTRATIVE_UNIT_NAME);
        if (currentNode != null) {
            administrativeUnit.setAdministrativeUnitName(currentNode.textValue());
            objectNode.remove(ADMINISTRATIVE_UNIT_NAME);
        }

        // Deserialize shortName
        currentNode = objectNode.get(SHORT_NAME);
        if (currentNode != null) {
            administrativeUnit.setShortName(currentNode.textValue());
            objectNode.remove(SHORT_NAME);
        }

        // Deserialize referenceToParent
        currentNode = objectNode.get(ADMINISTRATIVE_UNIT_PARENT_REFERENCE);
        if (currentNode != null) {
            AdministrativeUnit parentAdministrativeUnit = administrativeUnit.
                    getParentAdministrativeUnit();
            // Will it not always be null??
            if (parentAdministrativeUnit == null) {
                parentAdministrativeUnit = new AdministrativeUnit();
                parentAdministrativeUnit.setSystemId(UUID.fromString(
                        currentNode.textValue()));
                parentAdministrativeUnit.getReferenceChildAdministrativeUnit().
                        add(administrativeUnit);
            }
            objectNode.remove(ADMINISTRATIVE_UNIT_PARENT_REFERENCE);
        }

        // Check that there are no additional values left after processing the tree
        // If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append(String.format(MALFORMED_PAYLOAD,
                    ADMINISTRATIVE_UNIT, checkNodeObjectEmpty(objectNode)));
        }

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return administrativeUnit;
    }
}

