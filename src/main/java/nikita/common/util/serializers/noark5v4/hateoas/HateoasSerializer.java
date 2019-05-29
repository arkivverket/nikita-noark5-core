package nikita.common.util.serializers.noark5v4.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.util.CommonUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import static nikita.common.config.Constants.ENTITY_ROOT_NAME_LIST;


/**
 * Created by tsodring on 2/9/17.
 */
public class HateoasSerializer extends StdSerializer<HateoasNoarkObject> {

    public HateoasSerializer() {
        super(HateoasNoarkObject.class);
    }

    @Override
    public void serialize(HateoasNoarkObject hateoasObject, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        // For lists the output should be
        //  { "entity": [], "_links": [] }
        // An empty list should produce
        // { "entity": [], "_links": [] }
        // An entity should produce
        // { "field" : "value", "_links": [] }
        // No such thing as an empty entity
        List<INikitaEntity> list = hateoasObject.getList();
        if (list.size() > 0) {
            if (!hateoasObject.isSingleEntity()) {
                jgen.writeStartObject();
                jgen.writeFieldName(ENTITY_ROOT_NAME_LIST);
                jgen.writeStartArray();
            }
            for (INikitaEntity entity : list) {
                serializeNoarkEntity(entity, hateoasObject, jgen);
            }
            if (!hateoasObject.isSingleEntity()) {
                jgen.writeEndArray();
                CommonUtils.Hateoas.Serialize.printHateoasLinks(jgen, hateoasObject.getSelfLinks());
                jgen.writeEndObject();
            }
        }
        // It's an empty object, so just returning Hateoas self links
        else {
            jgen.writeStartObject();
            jgen.writeFieldName(ENTITY_ROOT_NAME_LIST);
            jgen.writeStartArray();
            jgen.writeEndArray();
            CommonUtils.Hateoas.Serialize.printHateoasLinks(jgen, hateoasObject.getSelfLinks());
            jgen.writeEndObject();
        }
    }

    protected void serializeNoarkEntity(INikitaEntity entity, HateoasNoarkObject hateoasObject,
                                        JsonGenerator jgen) throws IOException {
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        throw new UnsupportedOperationException();
    }
}
