package nikita.common.util.deserialisers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.PartPerson;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;

import java.io.IOException;

import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;

/**
 * Created by tsodring on 11/07/18.
 * <p>
 * Deserialise an incoming PartPerson JSON object.
 */
public class PartPersonDeserializer
        extends JsonDeserializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public PartPerson deserialize(
            JsonParser jsonParser, DeserializationContext dc)
            throws IOException {

        StringBuilder errors = new StringBuilder();
        PartPerson part = new PartPerson();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        deserialiseNoarkSystemIdEntity(part, objectNode, errors);
        deserialisePartPersonEntity(part, objectNode, errors);

        // Check that there are no additional values left after processing
        // If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The PartPerson you tried to create is malformed. ");
            errors.append("The following fields are not recognised as ");
            errors.append("PartPerson fields [");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("]. ");
        }

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return part;
    }
}
