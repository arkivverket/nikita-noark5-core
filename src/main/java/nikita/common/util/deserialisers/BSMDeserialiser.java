package nikita.common.util.deserialisers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nikita.common.model.noark5.bsm.BSM;
import nikita.common.model.noark5.bsm.BSMBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static nikita.common.util.CommonUtils.DATE_PATTERN;
import static nikita.common.util.CommonUtils.DATE_TIME_PATTERN;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.deserializeDate;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.deserializeDateTime;

public class BSMDeserialiser
        extends JsonDeserializer<BSM> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public BSM deserialize(
            JsonParser jsonParser,
            DeserializationContext deserializationContext)
            throws IOException {

        ObjectNode objectNode = mapper.readTree(jsonParser);
        Iterator<Map.Entry<String, JsonNode>> entries = objectNode.fields();
        BSM bsm = new BSM();
        List<BSMBase> bsmList = new ArrayList<>();

        while (entries.hasNext()) {
            Map.Entry<String, JsonNode> entry = entries.next();
            JsonNode valueNode = entry.getValue();
            String key = entry.getKey();
            if (valueNode != null) {
                if (valueNode.isBoolean()) {
                    bsmList.add(new BSMBase(key, valueNode.booleanValue()));
                } else if (valueNode.isDouble()) {
                    bsmList.add(new BSMBase(key, valueNode.doubleValue()));
                } else if (valueNode.isInt()) {
                    bsmList.add(new BSMBase(key, valueNode.intValue()));
                } else if (valueNode.isTextual()) {
                    String value = valueNode.textValue();
                    if (value.startsWith("http://") ||
                            value.startsWith("https://")) {
                        BSMBase bsmBase = new BSMBase(key);
                        bsmBase.setUriValue(valueNode.textValue());
                        bsmList.add(bsmBase);
                    } else if (DATE_TIME_PATTERN.matcher(value).matches()) {
                        bsmList.add(new BSMBase(key,
                                deserializeDateTime(value)));
                    } else if (DATE_PATTERN.matcher(value).matches()) {
                        bsmList.add(new BSMBase(key,
                                deserializeDate(value), true));
                    } else {
                        bsmList.add(new BSMBase(key, valueNode.textValue()));
                    }
                }
            }
        }
        bsm.setReferenceBSMBase(bsmList);
        return bsm;
    }
}
