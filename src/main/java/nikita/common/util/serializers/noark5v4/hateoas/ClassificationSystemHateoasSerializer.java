package nikita.common.util.serializers.noark5v4.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.ClassificationSystem;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.util.CommonUtils;
import nikita.common.util.serializers.noark5v4.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

/**
 * Serialise an outgoing ClassificationSystem object as JSON.
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
public class ClassificationSystemHateoasSerializer extends HateoasSerializer implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(INikitaEntity classificationSystem,
                                     HateoasNoarkObject classificationSystemHateoas, JsonGenerator jgen
    ) throws IOException {
        jgen.writeStartObject();
        CommonUtils.Hateoas.Serialize.printSystemIdEntity(jgen, classificationSystem);
        CommonUtils.Hateoas.Serialize.printTitleAndDescription(jgen, (ClassificationSystem) classificationSystem);
        CommonUtils.Hateoas.Serialize.printCreateEntity(jgen, (ClassificationSystem) classificationSystem);
        CommonUtils.Hateoas.Serialize.printFinaliseEntity(jgen, (ClassificationSystem) classificationSystem);
        CommonUtils.Hateoas.Serialize.printHateoasLinks(jgen, classificationSystemHateoas.getLinks(classificationSystem));
        jgen.writeEndObject();
    }
}
