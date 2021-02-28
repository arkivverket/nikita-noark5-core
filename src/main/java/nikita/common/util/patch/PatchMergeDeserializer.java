package nikita.common.util.patch;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.nikita.PatchMerge;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class PatchMergeDeserializer
        extends JsonDeserializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public PatchMerge deserialize(JsonParser jsonParser, DeserializationContext dc)
            throws IOException {
        PatchMerge patchMerge = new PatchMerge();
        ObjectNode objectNode = mapper.readTree(jsonParser);
        Iterator<Map.Entry<String, JsonNode>> nodes = objectNode.fields();
        while (nodes.hasNext()) {
            Map.Entry entry = nodes.next();
            patchMerge.addValue((String) entry.getKey(), entry.getValue());
        }
        return patchMerge;
    }
}
