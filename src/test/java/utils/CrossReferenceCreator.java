package utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.secondary.CrossReference;

import java.io.IOException;
import java.io.StringWriter;
import java.util.UUID;

import static nikita.common.util.CommonUtils.Hateoas.Serialize.printCrossReference;

public final class CrossReferenceCreator {

    public static String createCrossReferenceAsJSON(
            UUID fromUUID, UUID toUUID, String referenceType) throws IOException {
        CrossReference crossReference = new CrossReference();
        crossReference.setFromSystemId(fromUUID);
        crossReference.setToSystemId(toUUID);
        crossReference.setReferenceType(referenceType);

        JsonFactory factory = new JsonFactory();
        StringWriter jsonWriter = new StringWriter();
        JsonGenerator jgen = factory.createGenerator(jsonWriter);
        jgen.writeStartObject();
        printCrossReference(jgen, crossReference);
        jgen.writeEndObject();
        jgen.close();
        return jsonWriter.toString();
    }
}
