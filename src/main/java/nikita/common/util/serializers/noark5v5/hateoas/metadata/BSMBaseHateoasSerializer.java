package nikita.common.util.serializers.noark5v5.hateoas.metadata;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.bsm.BSMBase;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.TYPE;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printHateoasLinks;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printSystemIdEntity;

public class BSMBaseHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INoarkEntity noarkEntity,
            HateoasNoarkObject bsmHateoas, JsonGenerator jgen)
            throws IOException {
        BSMBase bsm = (BSMBase) noarkEntity;
        jgen.writeStartObject();
        printSystemIdEntity(jgen, (ISystemId) bsm);
        String type = bsm.getDataType();
        printNullable(jgen, TYPE, bsm.getDataType());
        if (type.equals(TYPE_STRING)) {
            printNullable(jgen, bsm.getValueName(), bsm.getStringValue());
        } else if (type.equals(TYPE_BOOLEAN)) {
            printNullable(jgen, bsm.getValueName(), bsm.getBooleanValue());
        } else if (type.equals(TYPE_DOUBLE)) {
            printNullable(jgen, bsm.getValueName(), bsm.getDoubleValue());
        } else if (type.equals(TYPE_INTEGER)) {
            printNullable(jgen, bsm.getValueName(), bsm.getIntegerValue());
        } else if (type.equals(TYPE_DATE)) {
            printNullableDate(jgen, bsm.getValueName(), bsm.getDateTimeValue());
        } else if (type.equals(TYPE_DATE_TIME)) {
            printNullableDateTime(jgen, bsm.getValueName(),
                    bsm.getDateTimeValue());
        } else if (type.equals(TYPE_URI)) {
            printNullable(jgen, bsm.getValueName(), bsm.getUriValue());
        }
        printHateoasLinks(jgen, bsmHateoas.getLinks(bsm));
        jgen.writeEndObject();
    }
}
