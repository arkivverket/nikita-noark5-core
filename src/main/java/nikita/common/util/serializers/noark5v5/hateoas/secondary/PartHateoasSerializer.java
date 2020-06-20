package nikita.common.util.serializers.noark5v5.hateoas.secondary;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.secondary.Part;
import nikita.common.model.noark5.v5.secondary.PartPerson;
import nikita.common.model.noark5.v5.secondary.PartUnit;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IPartPersonEntity;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IPartUnitEntity;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

/**
 * Serialise an outgoing Part object as JSON.
 * <p>
 * Having an own serializer is done to have more fine grained control over
 * the output. We need to be able to especially control the HATEOAS links and
 * the actual format of the HATEOAS links might change over time with the
 * standard. This allows us to be able to easily adapt to any changes
 * <p>
 * Note. Only values that are part of the standard are included in the JSON.
 * Properties like 'id' or 'deleted' are not exported
 */
public class PartHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INoarkEntity noarkSystemIdEntity,
            HateoasNoarkObject partHateoas, JsonGenerator jgen)
            throws IOException {

        Part part = (Part) noarkSystemIdEntity;

        jgen.writeStartObject();
        if (part instanceof PartPerson) {
            printPartPerson(jgen, (IPartPersonEntity) part);
        }
        if (part instanceof PartUnit) {
            printPartUnit(jgen, (IPartUnitEntity) part);
        }
        printCreateEntity(jgen, part);
        printModifiedEntity(jgen, part);
        printHateoasLinks(jgen, partHateoas.
                getLinks(part));
        jgen.writeEndObject();
    }
}
