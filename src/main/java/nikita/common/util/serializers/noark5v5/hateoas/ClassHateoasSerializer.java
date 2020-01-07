package nikita.common.util.serializers.noark5v5.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import static nikita.common.config.N5ResourceMappings.CLASS_ID;
import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.util.CommonUtils;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

/**
 * Serialise an outgoing Class object as JSON.
 * <p>
 * Having an own serializer is done to have more fine grained control over the output. We need to be able to especially
 * control the HATEOAS links and the actual format of the HATEOAS links might change over time with the standard. This
 * allows us to be able to easily adapt to any changes
 * <p>
 * Only Norwegian property names are used on the outgoing JSON property names and are in accordance with the Noark
 * standard.
 * <p>
 * Note. Only values that are part of the standard are included in the JSON. Properties like 'id' or 'deleted' are not
 * exported
 */
public class ClassHateoasSerializer extends HateoasSerializer implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(INikitaEntity noarkSystemIdEntity,
                                     HateoasNoarkObject classHateoas, JsonGenerator jgen) throws IOException {

        Class klass = (Class) noarkSystemIdEntity;

        jgen.writeStartObject();
        CommonUtils.Hateoas.Serialize.printNikitaEntity(jgen, klass);
        if (klass.getClassId() != null) {
            jgen.writeStringField(CLASS_ID, klass.getClassId());
        }
        CommonUtils.Hateoas.Serialize.printTitleAndDescription(jgen, klass);
        CommonUtils.Hateoas.Serialize.printKeyword(jgen, klass);
        CommonUtils.Hateoas.Serialize.printFinaliseEntity(jgen, klass);
        // TODO: Fix this ! CommonCommonUtils.Hateoas.Serialize.printCrossReference(jgen, klass);
        CommonUtils.Hateoas.Serialize.printDisposal(jgen, klass);
        CommonUtils.Hateoas.Serialize.printScreening(jgen, klass);
        CommonUtils.Hateoas.Serialize.printClassified(jgen, klass);
        CommonUtils.Hateoas.Serialize.printHateoasLinks(jgen, classHateoas.getLinks(klass));
        jgen.writeEndObject();
    }
}
