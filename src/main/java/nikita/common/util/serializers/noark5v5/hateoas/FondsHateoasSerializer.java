package nikita.common.util.serializers.noark5v5.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.CommonUtils;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.FONDS_STATUS;

/**
 * Serialise an outgoing Fonds object as JSON.
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
public class FondsHateoasSerializer extends HateoasSerializer implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(INoarkEntity noarkSystemIdEntity,
                                     HateoasNoarkObject fondsHateoas, JsonGenerator jgen) throws IOException {
        Fonds fonds = (Fonds) noarkSystemIdEntity;

        jgen.writeStartObject();
        CommonUtils.Hateoas.Serialize.printNikitaEntity(jgen, fonds);
        CommonUtils.Hateoas.Serialize.printTitleAndDescription(jgen, fonds);
        if (fonds.getFondsStatusCode() != null) {
            jgen.writeObjectFieldStart(FONDS_STATUS);
            CommonUtils.Hateoas.Serialize.printCode(jgen,
                    fonds.getFondsStatusCode(),
                    fonds.getFondsStatusCodeName());
            jgen.writeEndObject();
        }
        CommonUtils.Hateoas.Serialize.printDocumentMedium(jgen, fonds);
        CommonUtils.Hateoas.Serialize.printStorageLocation(jgen, fonds);
        CommonUtils.Hateoas.Serialize.printFinaliseEntity(jgen, fonds);
        CommonUtils.Hateoas.Serialize.printHateoasLinks(jgen, fondsHateoas.getLinks(fonds));
        jgen.writeEndObject();
    }
}
