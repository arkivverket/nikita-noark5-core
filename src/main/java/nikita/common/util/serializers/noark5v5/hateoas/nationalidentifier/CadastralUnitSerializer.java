package nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
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
            INoarkEntity noarkSystemIdEntity,
            HateoasNoarkObject cadastralUnitHateoas,
            JsonGenerator jgen) throws IOException {

        CadastralUnit cadastralUnit = (CadastralUnit) noarkSystemIdEntity;
        jgen.writeStartObject();
        printSystemIdEntity(jgen, cadastralUnit);

        printNullable(jgen, MUNICIPALITY_NUMBER,
                      cadastralUnit.getMunicipalityNumber());
        printNullable(jgen, HOLDING_NUMBER,
                      cadastralUnit.getHoldingNumber());
        printNullable(jgen, SUB_HOLDING_NUMBER,
                      cadastralUnit.getSubHoldingNumber());
        printNullable(jgen, LEASE_NUMBER,
                      cadastralUnit.getLeaseNumber());
        printNullable(jgen, SECTION_NUMBER,
                      cadastralUnit.getSectionNumber());

        printHateoasLinks(jgen, cadastralUnitHateoas.getLinks(cadastralUnit));
        jgen.writeEndObject();
    }
}
