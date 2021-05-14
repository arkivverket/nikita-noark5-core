package nikita.common.util.serializers.noark5v5.hateoas.secondary;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.secondary.CrossReferenceHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.secondary.CrossReference;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;
import nikita.webapp.hateoas.secondary.CrossReferenceHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;

import java.io.IOException;

import static nikita.common.util.CommonUtils.Hateoas.Serialize.printCrossReference;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printHateoasLinks;

/**
 * Serialise an outgoing CrossReference object as JSON.
 */
@HateoasPacker(using = CrossReferenceHateoasHandler.class)
@HateoasObject(using = CrossReferenceHateoas.class)
public class CrossReferenceHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INoarkEntity noarkSystemIdEntity,
            HateoasNoarkObject crossReferenceHateoas, JsonGenerator jgen)
            throws IOException {
        CrossReference crossReference = (CrossReference) noarkSystemIdEntity;
        jgen.writeStartObject();
        printCrossReference(jgen, crossReference);
        printHateoasLinks(jgen, crossReferenceHateoas.getLinks(crossReference));
        jgen.writeEndObject();
    }
}
