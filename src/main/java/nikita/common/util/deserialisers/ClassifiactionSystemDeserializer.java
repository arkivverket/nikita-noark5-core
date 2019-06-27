package nikita.common.util.deserialisers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v5.ClassificationSystem;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;

import java.io.IOException;

import static nikita.common.util.CommonUtils.Hateoas.Deserialize.checkNodeObjectEmpty;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.deserialiseNoarkEntity;

/**
 * Created by tsodring on 1/6/17.
 * <p>
 * Deserialise an incoming ClassificationSystem JSON object.
 *
 * Note:
 * - Unknown property values in the JSON will trigger an exception
 * - Missing obligatory property values in the JSON will trigger an exception
 */
public class ClassifiactionSystemDeserializer extends JsonDeserializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public ClassificationSystem deserialize(JsonParser jsonParser,
                                            DeserializationContext dc)
            throws IOException {
        StringBuilder errors = new StringBuilder();

        ClassificationSystem classificationSystem = new ClassificationSystem();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialise general properties
        deserialiseNoarkEntity(classificationSystem, objectNode, errors);

        // Check that there are no additional values left after processing the tree
        // If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The klassifikasjonssystem you tried to create is ");
            errors.append("malformed. The following fields are not recognised");
            errors.append(" as klassifikasjonssystem fields [");
            errors.append(checkNodeObjectEmpty(objectNode));
            errors.append("].");
        }

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return classificationSystem;
    }
}
