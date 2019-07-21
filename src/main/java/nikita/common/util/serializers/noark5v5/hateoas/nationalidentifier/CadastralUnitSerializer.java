package nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.nationalidentifier.CadastralUnit;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printHateoasLinks;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printSystemIdEntity;

public class CadastralUnitSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INikitaEntity noarkSystemIdEntity,
            HateoasNoarkObject cadastralUnitHateoas,
            JsonGenerator jgen) throws IOException {

        CadastralUnit cadastralUnit = (CadastralUnit) noarkSystemIdEntity;
        jgen.writeStartObject();
        printSystemIdEntity(jgen, cadastralUnit);

        jgen.writeStringField(MUNICIPALITY_NUMBER,
                cadastralUnit.getMunicipalityNumber());

        jgen.writeNumberField(HOLDING_NUMBER,
                cadastralUnit.getHoldingNumber());

        jgen.writeNumberField(SUB_HOLDING_NUMBER,
                cadastralUnit.getSubHoldingNumber());

        if (null != cadastralUnit.getLeaseNumber()) {
            jgen.writeNumberField(LEASE_NUMBER,
                    cadastralUnit.getLeaseNumber());
        }

        if (null != cadastralUnit.getSectionNumber()) {
            jgen.writeNumberField(SECTION_NUMBER,
                    cadastralUnit.getSectionNumber());
        }

        printHateoasLinks(jgen, cadastralUnitHateoas.getLinks(cadastralUnit));
        jgen.writeEndObject();
    }
}
