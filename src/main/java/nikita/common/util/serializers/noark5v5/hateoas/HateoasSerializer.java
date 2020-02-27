package nikita.common.util.serializers.noark5v5.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.exceptions.NikitaMisconfigurationException;
import nikita.webapp.hateoas.HateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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
                if (!hateoasObject.isSingleEntity()) {
		    /*
		     * Use HateoasHandler for the leaf class, not the
		     * base class, to ensure all _links entries for
		     * the leaf class show up in the list
		     */
                    HateoasNoarkObject noarkObject;
                    try {

                        Class<? extends INoarkEntity> cls = entity.getClass();
                        HateoasPacker packer = cls.getAnnotation(
                                HateoasPacker.class);
                        HateoasObject individualHateoasObject =
                                cls.getAnnotation(HateoasObject.class);

                        // Temp, allow Nullpointer to be taken by catch below
                        if (null == packer || null == individualHateoasObject) {
                            logger.error("Internal misconfiguration: Missing " +
                                    "annontations for " + entity.getClass()
                                    .getSimpleName());
                        }

                        noarkObject = individualHateoasObject
                                .using()
                                .getDeclaredConstructor(
                                        INoarkEntity.class)
                                .newInstance(entity);
                        HateoasHandler handler =
                                packer.using().getConstructor().newInstance();

                        handler.setPublicAddress(getAddress());
                        handler.setContextPath(getContextPath());
                        handler.addLinks(noarkObject, new Authorisation());
                    } catch (Exception e) {
                        String err = "Introspection failed while serialising" +
                                " list, " +
                                "using base HateoasHandler. (";
                        err += e.getMessage();
                        err += ")";
                        logger.error(err);
                        noarkObject = hateoasObject;
                    }
                    serializeNoarkEntity(entity, noarkObject, jgen);
                } else {

                    serializeNoarkEntity(entity, hateoasObject, jgen);
                }
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

    @Deprecated
    protected void printNullableMetadataCode
        (JsonGenerator jgen, String fieldName, String code, String codeName)
            throws IOException {
        if (null != code) {
            jgen.writeObjectFieldStart(fieldName);
            printCode(jgen, code, codeName);
            jgen.writeEndObject();
        }
    }

    protected void printNullableMetadata
        (JsonGenerator jgen, String fieldName, IMetadataEntity m)
            throws IOException {
        if (null != m && null != m.getCode()) {
            jgen.writeObjectFieldStart(fieldName);
            printCode(jgen, m);
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

    @Deprecated
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

    protected String getContextPath() {
        HttpServletRequest request = getRequest();
        return request.getContextPath();
    }

    protected String getAddress() {
        HttpServletRequest request = getRequest();
        String address = request.getHeader("X-Forwarded-Host");
        String scheme = request.getHeader("X-Forwarded-Proto");
        String port = request.getHeader("X-Forwarded-Port");


        if (address == null && scheme == null) {
            scheme = request.getScheme();
            address = request.getServerName();
            port = Integer.toString(request.getServerPort());
        }

        if (port != null) {
            return scheme + "://" + address + ":" + port;
        } else {
            return scheme + "://" + address;
        }
    }

    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes())
                .getRequest();
    }
}
