package nikita.common.util.serializers.noark5v5.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.exceptions.NikitaMisconfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.util.List;

import static nikita.common.config.Constants.ENTITY_ROOT_NAME_LIST;
import static nikita.common.config.Constants.ENTITY_ROOT_NAME_LIST_COUNT;
import static nikita.common.util.CommonUtils.Hateoas.Serialize;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printCode;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printHateoasLinks;


/**
 * Created by tsodring on 2/9/17.
 */
public class HateoasSerializer
        extends StdSerializer<HateoasNoarkObject> {

    private static final Logger logger =
            LoggerFactory.getLogger(HateoasSerializer.class);

    public HateoasSerializer() {
        super(HateoasNoarkObject.class);
    }

    @Override
    public void serialize(
            HateoasNoarkObject hateoasObject, JsonGenerator jgen,
            SerializerProvider provider)
            throws IOException {
        // For lists the output should be
        //  { "count": N, "results": [], "_links": [] }
        // An empty list should produce
        // { "count": 0, "_links": [] }
        // An entity should produce
        // { "field" : "value", "_links": [] }
        // No such thing as an empty entity
        List<INoarkEntity> list = hateoasObject.getList();
        if (list.size() > 0) {
            if (!hateoasObject.isSingleEntity()) {
                jgen.writeStartObject();
                jgen.writeNumberField(ENTITY_ROOT_NAME_LIST_COUNT, list.size());
                jgen.writeFieldName(ENTITY_ROOT_NAME_LIST);
                jgen.writeStartArray();
            }
            for (INoarkEntity entity : list) {
                serializeNoarkEntity(entity, hateoasObject, jgen);
            }
            if (!hateoasObject.isSingleEntity()) {
                jgen.writeEndArray();
                printHateoasLinks(jgen, hateoasObject.getSelfLinks());
                jgen.writeEndObject();
            }
        }
        // It's an empty object, so just returning Hateoas self links
        else {
            jgen.writeStartObject();
            jgen.writeNumberField(ENTITY_ROOT_NAME_LIST_COUNT, 0);
            printHateoasLinks(jgen, hateoasObject.getSelfLinks());
            jgen.writeEndObject();
        }
    }

    protected void serializeNoarkEntity(
            INoarkEntity entity, HateoasNoarkObject hateoasObject,
            JsonGenerator jgen) throws IOException {
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        throw new UnsupportedOperationException();
    }


    protected void printNullable(JsonGenerator jgen,
                                 String fieldName, String value)
            throws IOException {
        if (null != value)
            jgen.writeStringField(fieldName, value);
    }

    protected void printNullable(JsonGenerator jgen,
                                 String fieldName, Integer value)
            throws IOException {
        if (null != value)
            jgen.writeNumberField(fieldName, value);
    }

    protected void printNullable(JsonGenerator jgen,
                                 String fieldName, Long value)
            throws IOException {
        if (null != value)
            jgen.writeNumberField(fieldName, value);
    }

    protected void printNullable(JsonGenerator jgen,
                                 String fieldName, Double value)
            throws IOException {
        if (null != value)
            jgen.writeNumberField(fieldName, value);
    }

    protected void printNullableMetadataCode
        (JsonGenerator jgen, String fieldName, String code, String codeName)
            throws IOException {
        if (null != code) {
            jgen.writeObjectFieldStart(fieldName);
            printCode(jgen, code, codeName);
            jgen.writeEndObject();
        }
    }

    protected void printNullableDate(JsonGenerator jgen,
                                     String fieldName, OffsetDateTime value)
            throws IOException {
        if (null != value)
            jgen.writeStringField(fieldName, Serialize.formatDate(value));
    }

    protected void printNullableDateTime(JsonGenerator jgen,
				 String fieldName, OffsetDateTime value)
            throws IOException {
        if (null != value)
	    jgen.writeStringField(fieldName, Serialize.formatDateTime(value));
    }

    /**
     * Print a field / value pair, but throw exception if the value is
     * mandatory. This is required so that we can capture illegal payloads being
     * generated by nikita.
     *
     * @param jgen
     * @param fieldName
     * @param value
     * @throws IOException
     */
    protected void print(JsonGenerator jgen,
                         String fieldName, @NotNull String value)
            throws IOException {
        checkNull(fieldName, value);
        jgen.writeStringField(fieldName, value);
    }

    protected void print(JsonGenerator jgen,
                         String fieldName, @NotNull Integer value)
            throws IOException {
        checkNull(fieldName, value);
        jgen.writeNumberField(fieldName, value);
    }

    protected void print(JsonGenerator jgen,
                         String fieldName, @NotNull Double value)
            throws IOException {
        checkNull(fieldName, value);
        jgen.writeNumberField(fieldName, value);
    }

    protected void printMetadataCode(JsonGenerator jgen, String fieldName,
                                     String code, String codeName)
            throws IOException {
        checkNull(fieldName, code);
        jgen.writeObjectFieldStart(fieldName);
        printCode(jgen, code, codeName);
        jgen.writeEndObject();
    }

    protected void printDate(JsonGenerator jgen,
			      String fieldName, OffsetDateTime value)
            throws IOException {
        checkNull(fieldName, value);
        jgen.writeStringField(fieldName, Serialize.formatDate(value));
    }

    protected void printDateTime(JsonGenerator jgen,
				 String fieldName, OffsetDateTime value)
            throws IOException {
        checkNull(fieldName, value);
        jgen.writeStringField(fieldName, Serialize.formatDateTime(value));
    }

    private void checkNull(String fieldName, Object value) {
        if (null == value) {
            String msg = "When serialising [" + fieldName + "], there was no " +
                    "associated value. This field is mandatory";
            logger.error(msg);
            throw new NikitaMisconfigurationException(msg);
        }
    }
}
