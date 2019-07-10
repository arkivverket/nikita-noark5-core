package nikita.webapp.util.serialisers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import nikita.webapp.application.ApplicationDetails;
import nikita.webapp.application.ConformityLevel;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Iterator;

import static nikita.common.config.HATEOASConstants.HREF;
import static nikita.common.config.HATEOASConstants.LINKS;

public class ApplicationDetailsSerializer extends StdSerializer<ApplicationDetails> {

    public ApplicationDetailsSerializer() {
        super(ApplicationDetails.class);
    }

    // TODO: Add some error handling, if application details is empty  etc
    @Override
    public void serialize(ApplicationDetails applicationDetails,
                          JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        jgen.writeStartObject();
        Iterator<ConformityLevel> iterator =
                applicationDetails.getConformityLevels().iterator();
        if (iterator.hasNext()) {
            jgen.writeObjectFieldStart(LINKS);
        }
        while (iterator.hasNext()) {
            ConformityLevel conformityLevel = iterator.next();
            jgen.writeObjectFieldStart(conformityLevel.getRel());
            jgen.writeStringField(HREF, conformityLevel.getHref());
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
