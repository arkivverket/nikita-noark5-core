
package nikita.common.util.serializers.noark5v5.hateoas.secondary;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.secondary.Precedence;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

/**
 * Serialise an outgoing Precedence object as JSON.
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
public class PrecedenceHateoasSerializer extends HateoasSerializer implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(INoarkEntity noarkSystemIdEntity,
                                     HateoasNoarkObject precedenceHateoas, JsonGenerator jgen) throws IOException {
        Precedence precedence = (Precedence) noarkSystemIdEntity;

        jgen.writeStartObject();
        printSystemIdEntity(jgen, precedence);
        printNullableDate(jgen, PRECEDENCE_DATE,
                          precedence.getPrecedenceDate());
        printCreateEntity(jgen, precedence);
        printTitleAndDescription(jgen, precedence);
        printNullable(jgen, PRECEDENCE_AUTHORITY,
                      precedence.getPrecedenceAuthority());
        printNullable(jgen, PRECEDENCE_SOURCE_OF_LAW,
                      precedence.getSourceOfLaw());
        printNullableDateTime(jgen, PRECEDENCE_APPROVED_DATE,
                              precedence.getPrecedenceApprovedDate());
        printNullable(jgen, PRECEDENCE_APPROVED_BY,
                      precedence.getPrecedenceApprovedBy());
        printFinaliseEntity(jgen, precedence);
        printNullableMetadata(jgen, PRECEDENCE_PRECEDENCE_STATUS,
                              precedence.getPrecedenceStatus());
        printHateoasLinks(jgen, precedenceHateoas.getLinks(precedence));
        jgen.writeEndObject();
    }
}
