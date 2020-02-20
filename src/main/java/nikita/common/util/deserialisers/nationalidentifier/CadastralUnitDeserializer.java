package nikita.common.util.deserialisers.nationalidentifier;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.nationalidentifier.CadastralUnit;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nikita.common.config.HATEOASConstants.LINKS;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

public class CadastralUnitDeserializer
        extends JsonDeserializer {

    private static final Logger logger =
            LoggerFactory.getLogger(CadastralUnitDeserializer.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public CadastralUnit deserialize(
            JsonParser jsonParser, DeserializationContext dc)
            throws IOException {

        StringBuilder errors = new StringBuilder();
        CadastralUnit cadastralUnit = new CadastralUnit();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialize systemID
        deserialiseNoarkSystemIdEntity(cadastralUnit, objectNode, errors);

        // Deserialize kommunenummer
        JsonNode currentNode = objectNode.get(MUNICIPALITY_NUMBER);
        if (null != currentNode) {
            cadastralUnit.setMunicipalityNumber(currentNode.textValue());
            objectNode.remove(MUNICIPALITY_NUMBER);
        }

        // Deserialize gaardsnummer
        cadastralUnit.setHoldingNumber
            (deserializeInteger(HOLDING_NUMBER,
                                objectNode, errors, true));

        // Deserialize bruksnummer
        cadastralUnit.setSubHoldingNumber
            (deserializeInteger(SUB_HOLDING_NUMBER,
                                objectNode, errors, true));

        // Deserialize festenummer
        cadastralUnit.setLeaseNumber
            (deserializeInteger(LEASE_NUMBER,
                                objectNode, errors, false));

        // Deserialize seksjonsnummer
        cadastralUnit.setSectionNumber
            (deserializeInteger(SECTION_NUMBER,
                                objectNode, errors, false));

        currentNode = objectNode.get(LINKS);
        if (null != currentNode) {
            logger.debug("Payload contains " + LINKS + ". " +
                    "This value is being ignored.");
            objectNode.remove(LINKS);
        }

        // Check that there are no additional values left after processing the
        // tree. If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The cadastralUnit you tried to create is malformed");
            errors.append(". The following fields are not recognised as ");
            errors.append("cadastralUnit fields [");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("]. ");
        }
        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return cadastralUnit;
    }
}
