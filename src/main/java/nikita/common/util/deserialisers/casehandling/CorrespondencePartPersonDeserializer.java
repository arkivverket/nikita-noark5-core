package nikita.common.util.deserialisers.casehandling;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.v4.casehandling.secondary.CorrespondencePartPerson;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;

import java.io.IOException;

import static nikita.common.util.CommonUtils.Hateoas.Deserialize.deserialiseCorrespondencePartPersonEntity;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.deserialiseNoarkSystemIdEntity;

/**
 * Created by tsodring on 1/6/17.
 * <p>
 * Deserialise an incoming CorrespondencePartPerson JSON object.
 * <p>
 * Detect if the CorrespondencePart is CorrespondencePartPerson, CorrespondencePartInternal or
 * CorrespondencePartUnit and returns an object the appropriate type.
 */
public class CorrespondencePartPersonDeserializer extends JsonDeserializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public CorrespondencePartPerson deserialize(JsonParser jsonParser, DeserializationContext dc)
            throws IOException {
        StringBuilder errors = new StringBuilder();

        CorrespondencePartPerson correspondencePart =
                new CorrespondencePartPerson();
        ObjectNode objectNode = mapper.readTree(jsonParser);


        deserialiseNoarkSystemIdEntity(correspondencePart, objectNode, errors);
        deserialiseCorrespondencePartPersonEntity(
                correspondencePart, objectNode, errors);

        // Check that there are no additional values left after processing the tree
        // If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The korrespondansepartperson you tried to create is malformed. The " +
                    "following fields are not recognised as korrespondansepartperson fields [" +
                    CommonUtils.Hateoas.Deserialize.checkNodeObjectEmpty(objectNode) + "]. ");
        }

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return correspondencePart;
    }
}
