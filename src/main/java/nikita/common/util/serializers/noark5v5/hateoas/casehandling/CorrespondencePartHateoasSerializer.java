package nikita.common.util.serializers.noark5v5.hateoas.casehandling;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartInternal;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ICorrespondencePartPersonEntity;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ICorrespondencePartUnitEntity;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

/**
 * Serialise an outgoing CorrespondencePart object as JSON.
 * <p>
 * Having an own serializer is done to have more fine grained control over
 * the output. We need to be able to especially control the HATEOAS links and
 * the actual format of the HATEOAS links might change over time with the
 * standard. This allows us to be able to easily adapt to any changes
 * <p>
 * Note. Only values that are part of the standard are included in the JSON.
 * Properties like 'id' or 'deleted' are not exported
 */
public class CorrespondencePartHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INoarkEntity noarkSystemIdEntity,
            HateoasNoarkObject correspondencePartHateoas, JsonGenerator jgen)
            throws IOException {

        CorrespondencePart correspondencePart =
                (CorrespondencePart) noarkSystemIdEntity;

        jgen.writeStartObject();

        if (correspondencePart instanceof CorrespondencePartPerson) {
            printCorrespondencePartPerson(jgen,
                    (ICorrespondencePartPersonEntity) correspondencePart);
        }
        if (correspondencePart instanceof CorrespondencePartUnit) {
            printCorrespondencePartUnit(jgen,
                    (ICorrespondencePartUnitEntity) correspondencePart);
        }
        if (correspondencePart instanceof CorrespondencePartInternal) {
            //printCorrespondencePartInterna(jgen, correspondencePart);
        }

        printHateoasLinks(jgen, correspondencePartHateoas.
                getLinks(correspondencePart));
        jgen.writeEndObject();
    }
}
