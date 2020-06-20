package nikita.common.util.serializers.noark5v5.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.ClassificationSystem;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

/**
 * Serialise an outgoing ClassificationSystem object as JSON.
 */
public class ClassificationSystemHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INoarkEntity classificationSystemEntity,
            HateoasNoarkObject classificationSystemHateoas, JsonGenerator jgen
    ) throws IOException {
        ClassificationSystem classificationSystem =
            (ClassificationSystem) classificationSystemEntity;

        jgen.writeStartObject();
        printClassificationSystemEntity(jgen, classificationSystem);
        printFinaliseEntity(jgen, classificationSystem);
        printCreateEntity(jgen, classificationSystem);
        printModifiedEntity(jgen, classificationSystem);
        printHateoasLinks(jgen, classificationSystemHateoas.
                getLinks(classificationSystem));
        jgen.writeEndObject();
    }
}
