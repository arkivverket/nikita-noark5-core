package nikita.util.deserialisers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.model.noark5.v4.ClassificationSystem;
import nikita.model.noark5.v4.interfaces.entities.INoarkGeneralEntity;
import nikita.util.CommonUtils;
import nikita.util.exceptions.NikitaMalformedInputDataException;

import java.io.IOException;

/**
 * Created by tsodring on 1/6/17.
 *
 * Deserialise an incoming ClassificationSystem JSON object.
 *
 * Having a own deserialiser is done to have more fine grained control over the input. This allows us to be less strict
 * with property names, allowing for both English and Norwegian property names
 *
 *
 * Note this implementation expects that the classificationSystem object to deserialise is in compliance with the Noark
 * standard where certain properties i.e. createdBy and createdDate are set by the core, not the caller. This
 * deserializer will not enforce this and will deserialize a classificationSystem object correctly. This is because e.g
 * the import interface will require such functionality.
 *
 *  - Testing of compliance of properties is handled by the core, either in ClassificationSystemController or
 *    ClassificationSystemService
 *
 * Note. Currently we do not include 'id' or 'deleted' properties. 'id' is a primary key and it is assumed this is
 * taken care of by the DBMS and 'deleted' is a field internal to the core to handle soft delete. Importing soft deleted
 * objects is something we do not consider necessary.
 *
 * Note:
 *  - Unknown property values in the JSON will trigger an exception
 *  - Missing obligatory property values in the JSON will trigger an exception
 */
public class ClassifiactionSystemDeserializer extends JsonDeserializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public ClassificationSystem deserialize(JsonParser jsonParser, DeserializationContext dc)
            throws IOException {

        ClassificationSystem classificationSystem = new ClassificationSystem();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // Deserialise general properties
        CommonUtils.Hateoas.Deserialize.deserialiseNoarkEntity(classificationSystem, objectNode);

        // Check that there are no additional values left after processing the tree
        // If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            throw new NikitaMalformedInputDataException("The klassifikasjonssystem you tried to create is " +
                    "malformed. The following fields are not recognised as klassifikasjonssystem fields [" +
                    CommonUtils.Hateoas.Deserialize.checkNodeObjectEmpty(objectNode) + "]");
        }
        return classificationSystem;
    }
}
