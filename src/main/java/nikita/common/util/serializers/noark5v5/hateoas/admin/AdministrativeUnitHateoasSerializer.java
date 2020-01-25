package nikita.common.util.serializers.noark5v5.hateoas.admin;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

/**
 * Serialise an outgoing AdministrativeUnit object as JSON.
 * <p>
 * Having an own serializer is done to have more fine grained control over the output. We need to be able to especially
 * control the HATEOAS links and the actual format of the HATEOAS links might change over time with the standard. This
 * allows us to be able to easily adapt to any changes
 * <p>
 * Note. Only values that are part of the standard are included in the JSON. Properties like 'id' or 'deleted' are not
 * exported
 */
public class AdministrativeUnitHateoasSerializer extends HateoasSerializer implements nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(INoarkEntity noarkSystemIdEntity,
                                     HateoasNoarkObject administrativeUnitHateoas, JsonGenerator jgen) throws IOException {
        AdministrativeUnit administrativeUnit = (AdministrativeUnit) noarkSystemIdEntity;

        jgen.writeStartObject();
        printSystemIdEntity(jgen, administrativeUnit);
        printNullable(jgen, ADMINISTRATIVE_UNIT_NAME,
                      administrativeUnit.getAdministrativeUnitName());
        printNullable(jgen, SHORT_NAME, administrativeUnit.getShortName());
        printFinaliseEntity(jgen, administrativeUnit);
        printNullable(jgen, ADMINISTRATIVE_UNIT_STATUS,
                      administrativeUnit.getAdministrativeUnitStatus());
        if (administrativeUnit.getParentAdministrativeUnit() != null) {
            jgen.writeStringField(ADMINISTRATIVE_UNIT_PARENT_REFERENCE,
                    administrativeUnit.getParentAdministrativeUnit().getSystemId());
        }
        printHateoasLinks(jgen, administrativeUnitHateoas
                          .getLinks(administrativeUnit));
        jgen.writeEndObject();
    }
}
