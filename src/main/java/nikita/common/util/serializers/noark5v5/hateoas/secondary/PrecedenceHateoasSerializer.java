
package nikita.common.util.serializers.noark5v5.hateoas.secondary;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.casehandling.Precedence;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

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
        printPrecedence(jgen, precedence);
        printHateoasLinks(jgen, precedenceHateoas.getLinks(precedence));
        jgen.writeEndObject();
    }
}
