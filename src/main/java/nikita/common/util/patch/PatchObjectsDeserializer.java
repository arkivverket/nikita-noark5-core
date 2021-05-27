package nikita.common.util.patch;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import nikita.common.model.nikita.PatchObject;
import nikita.common.model.nikita.PatchObjects;

import java.io.IOException;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY;

public class PatchObjectsDeserializer
        extends JsonDeserializer<PatchObjects> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public PatchObjects deserialize(JsonParser jsonParser,
                                    DeserializationContext dc)
            throws IOException {
        mapper.configure(ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        PatchObjects patchObjects = new PatchObjects();
        for (PatchObject patchObject : mapper.readValue(
                jsonParser, PatchObject[].class)) {
            patchObjects.add(patchObject);
        }
        return patchObjects;
    }
}
