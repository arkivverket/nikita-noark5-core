package nikita.common.util.serializers.noark5v5.hateoas.casehandling;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.util.CommonUtils.Hateoas.Serialize.printCorrespondencePartUnit;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printHateoasLinks;

/**
 * Serialise an outgoing CorrespondencePart object as JSON.
 * <p>
 * Having an own serializer is done to have more fine grained control over
 * the output. We need to be able to especially control the HATEOAS links and
 * the actual format of the HATEOAS links might change over time with the
 * standard. This allows us to be able to easily adapt to any changes
 */
public class CorrespondencePartUnitHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INoarkEntity noarkSystemIdEntity,
            HateoasNoarkObject correspondencePartHateoas, JsonGenerator jgen)
            throws IOException {
        CorrespondencePartUnit correspondencePart =
                (CorrespondencePartUnit) noarkSystemIdEntity;
        jgen.writeStartObject();
        printCorrespondencePartUnit(jgen, correspondencePart);
        printHateoasLinks(jgen, correspondencePartHateoas.
                getLinks(correspondencePart));
        jgen.writeEndObject();
    }
}
