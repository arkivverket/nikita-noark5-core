package nikita.common.util.patch;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.nikita.PatchMerge;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static nikita.common.util.CommonUtils.DATE_PATTERN;
import static nikita.common.util.CommonUtils.DATE_TIME_PATTERN;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.deserializeDate;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.deserializeDateTime;

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
            Map.Entry<String, JsonNode> entry = nodes.next();
            if (entry.getValue() instanceof ObjectNode) {
                ObjectNode subObjectNode = (ObjectNode) entry.getValue();
                Iterator<Map.Entry<String, JsonNode>> subNodes =
                        subObjectNode.fields();
                Map<String, Object> values = new HashMap<>();
                while (subNodes.hasNext()) {
                    Map.Entry<String, JsonNode> subEntry = subNodes.next();
                    JsonNode nodeValue = subEntry.getValue();
                    values.put(subEntry.getKey(),
                            getValueWithType(nodeValue));
                }
                patchMerge.addValue(entry.getKey(), values);
            } else {
                patchMerge.addValue(entry.getKey(),
                        getValueWithType(entry.getValue()));
            }
        }
        return patchMerge;
    }

    /**
     * Accepting a null return possibility as if the type is something we
     * cannot handle, we need to find out.
     * @param node JsonNode to retrieve correct type and value
     * @return the value with correct type
     */
    private Object getValueWithType(@NotNull JsonNode node) {
        if (node.isBoolean()) {
            return node.booleanValue();
        } else if (node.isDouble()) {
            return node.doubleValue();
        } else if (node.isInt()) {
            return node.intValue();
        } else if (node.isTextual()) {
            String value = node.textValue();
            if (DATE_TIME_PATTERN.matcher(value).matches()) {
                return deserializeDateTime(value);
            } else if (DATE_PATTERN.matcher(value).matches()) {
                return deserializeDate(value);
            } else {
                return node.textValue();
            }
        }
        return null;
    }
}
