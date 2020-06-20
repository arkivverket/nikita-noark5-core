package nikita.common.util.serializers.noark5v5.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import static nikita.common.config.N5ResourceMappings.CLASS_ID;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

/**
 * Serialise an outgoing Class object as JSON.
 */
public class ClassHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INoarkEntity noarkSystemIdEntity, HateoasNoarkObject classHateoas
            , JsonGenerator jgen) throws IOException {

        Class klass = (Class) noarkSystemIdEntity;

        jgen.writeStartObject();
        printSystemIdEntity(jgen, klass);
        printNullable(jgen, CLASS_ID, klass.getClassId());
        printTitleAndDescription(jgen, klass);
        printKeyword(jgen, klass);
        printFinaliseEntity(jgen, klass);
        printCreateEntity(jgen, klass);
        printModifiedEntity(jgen, klass);
        // TODO: Fix this! Add printCrossReference(jgen, klass);
        printDisposal(jgen, klass);
        printScreening(jgen, klass);
        printClassified(jgen, klass);
        printHateoasLinks(jgen, classHateoas.getLinks(klass));
        jgen.writeEndObject();
    }
}
