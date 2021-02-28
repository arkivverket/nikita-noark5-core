package nikita.common.model.nikita;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.util.patch.PatchMergeDeserializer;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonDeserialize(using = PatchMergeDeserializer.class)
public class PatchMerge {

    private final Map<String, Object> map = new LinkedHashMap<>();

    public PatchMerge() {
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void addValue(String key, Object object) {
        map.putIfAbsent(key, object);
    }

    public Object getValue(String key) {
        return map.get(key);
    }
}
