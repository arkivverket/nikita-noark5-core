package nikita.webapp.util.serialisers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import nikita.webapp.application.ApplicationDetails;
import nikita.webapp.application.ConformityLevel;

import java.io.IOException;
import java.lang.reflect.Type;

import static nikita.common.config.HATEOASConstants.*;

public class ApplicationDetailsSerializer
        extends StdSerializer<ApplicationDetails> {

    public ApplicationDetailsSerializer() {
        super(ApplicationDetails.class);
    }

    @Override
    public void serialize(ApplicationDetails applicationDetails,
                          JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        jgen.writeStartObject();
        jgen.writeObjectFieldStart(LINKS);

        for (ConformityLevel conformityLevel :
                applicationDetails.getConformityLevels()) {
            jgen.writeObjectFieldStart(conformityLevel.getRel());
            jgen.writeStringField(HREF, conformityLevel.getHref());
            jgen.writeEndObject();
        }

        jgen.writeObjectFieldStart(SELF);
        jgen.writeStringField(HREF, applicationDetails.getSelfHref());
        jgen.writeEndObject();

        jgen.writeEndObject();
        jgen.writeEndObject();
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        throw new UnsupportedOperationException();
    }
}
