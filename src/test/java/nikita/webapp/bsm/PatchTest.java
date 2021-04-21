package nikita.webapp.bsm;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import nikita.common.model.nikita.PatchObject;
import nikita.common.model.nikita.PatchObjects;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PatchTest {

    @Test
    public void possibleToHaveMultiplePatchObjects()
            throws IOException {
        JsonFactory factory = new JsonFactory();

        StringWriter jsonPatchWriter = new StringWriter();
        JsonGenerator jsonPatch = factory.createGenerator(jsonPatchWriter);
        // Start first PatchObjects
        jsonPatch.writeStartArray();
        // Start first PatchObject
        jsonPatch.writeStartObject();
        jsonPatch.writeStringField("op", "add");
        jsonPatch.writeStringField("path", "Path 1");
        jsonPatch.writeObjectFieldStart("value");
        jsonPatch.writeEndObject();
        jsonPatch.writeEndObject();
        // Start first PatchObject
        jsonPatch.writeStartObject();
        jsonPatch.writeStringField("op", "add");
        jsonPatch.writeStringField("path", "Path 2");
        jsonPatch.writeObjectFieldStart("value");
        jsonPatch.writeEndObject();
        jsonPatch.writeEndObject();
        jsonPatch.writeEndArray();
        jsonPatch.close();

        PatchObjects patchObjects = new ObjectMapper()
                .readValue(jsonPatchWriter.toString(), PatchObjects.class);
        assertNotNull(patchObjects);
        List<PatchObject> list = patchObjects.getPatchObjects();
        assertThat(list, hasSize(2));
    }
}
