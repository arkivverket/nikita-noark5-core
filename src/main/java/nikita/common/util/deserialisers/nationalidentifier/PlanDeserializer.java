package nikita.common.util.deserialisers.nationalidentifier;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.metadata.Country;
import nikita.common.model.noark5.v5.nationalidentifier.Plan;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.checkNodeObjectEmpty;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.deserialiseNoarkSystemIdEntity;

public class PlanDeserializer
        extends JsonDeserializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Plan deserialize(
            JsonParser jsonParser, DeserializationContext dc)
            throws IOException {

        StringBuilder errors = new StringBuilder();
        Plan plan = new Plan();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialize systemID
        deserialiseNoarkSystemIdEntity(plan, objectNode, errors);

        // Deserialize kommunenummer
        JsonNode currentNode = objectNode.get(MUNICIPALITY_NUMBER);
        if (null != currentNode) {
            plan.setMunicipalityNumber(currentNode.textValue());
            objectNode.remove(MUNICIPALITY_NUMBER);
        }

        // Deserialize fylkesnummer
        currentNode = objectNode.get(COUNTY_NUMBER);
        if (null != currentNode) {
            plan.setCountyNumber(currentNode.textValue());
            objectNode.remove(COUNTY_NUMBER);
        }

        // Deserialize land
        currentNode = objectNode.get(COUNTRY);
        if (null != currentNode) {
            Country country = new Country();
            country.setCode(currentNode.textValue());
            plan.setCountry(country);
            objectNode.remove(COUNTRY);
        }

        // Deserialize planidentifikasjon
        currentNode = objectNode.get(PLAN_IDENTIFICATION);
        if (null != currentNode) {
            plan.setPlanIdentification(currentNode.textValue());
            objectNode.remove(PLAN_IDENTIFICATION);
        }

        // Check that there are no additional values left after processing the
        // tree. If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The plan you tried to create is malformed. The");
            errors.append(" following fields are not recognised as plan ");
            errors.append(" fields [");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("]. ");
        }
        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return plan;
    }
}
