package nikita.common.util.patch;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY;

/**
 *
 * Simple implementation of a deserialiser for a PATCH object
 *
 * This has not been really well tested, so use with caution.
 */
public class PatchObjectsDeserializer
        extends JsonDeserializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public PatchObjects deserialize(JsonParser jsonParser, DeserializationContext dc)
            throws IOException {

        mapper.configure(ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        PatchObjects patchObjects = new PatchObjects();
        mapper.readValues(jsonParser, PatchObject.class);
        PatchObject[] myObjects = mapper.readValue(jsonParser,
                PatchObject[].class);

        for (int i=0; i<myObjects.length; i++) {
            patchObjects.add(myObjects[i]);
        }

        return patchObjects;
    }
}
