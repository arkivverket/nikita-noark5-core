package nikita.webapp.util.serialisers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import nikita.webapp.application.APIDetail;
import nikita.webapp.application.APIDetails;

import java.io.IOException;
import java.lang.reflect.Type;

import static nikita.common.config.HATEOASConstants.HREF;
import static nikita.common.config.HATEOASConstants.LINKS;

public class APIDetailsSerializer extends StdSerializer<APIDetails> {

    public APIDetailsSerializer() {
        super(APIDetails.class);
    }

    @Override
    public void serialize(APIDetails apiDetails,
                          JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        jgen.writeStartObject();
        jgen.writeObjectFieldStart(LINKS);

        for (APIDetail apiDetail : apiDetails.getApiDetails()) {
            jgen.writeObjectFieldStart(apiDetail.getRel());
            jgen.writeStringField(HREF, apiDetail.getHref());
            if (apiDetail.getTemplated()) {
                jgen.writeBooleanField("templated",
                        apiDetail.getTemplated());
            }
            jgen.writeEndObject();
        }
        jgen.writeEndObject();
        jgen.writeEndObject();
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        throw new UnsupportedOperationException();
    }
}
