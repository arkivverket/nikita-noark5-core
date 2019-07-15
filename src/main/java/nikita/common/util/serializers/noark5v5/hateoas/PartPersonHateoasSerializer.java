package nikita.common.util.serializers.noark5v5.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.PartPerson;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.util.CommonUtils.Hateoas.Serialize.printHateoasLinks;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printPartPerson;

/**
 * Serialise an outgoing PartPerson object as JSON.
 * <p>
 * Having an own serializer is done to have more fine grained control over
 * the output. We need to be able to especially control the HATEOAS links and
 * the actual format of the HATEOAS links might change over time with the
 * standard. This allows us to be able to easily adapt to any changes
 */
public class PartPersonHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INikitaEntity noarkSystemIdEntity,
            HateoasNoarkObject partPersonHateoas, JsonGenerator jgen)
            throws IOException {

        PartPerson partPerson = (PartPerson) noarkSystemIdEntity;
        jgen.writeStartObject();
        printPartPerson(jgen, partPerson);
        printHateoasLinks(jgen, partPersonHateoas.getLinks(partPerson));
        jgen.writeEndObject();
    }
}
