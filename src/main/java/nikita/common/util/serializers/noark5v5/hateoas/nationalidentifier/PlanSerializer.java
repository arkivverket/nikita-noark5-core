package nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.nationalidentifier.Plan;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printHateoasLinks;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printSystemIdEntity;

public class PlanSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INoarkEntity noarkSystemIdEntity,
            HateoasNoarkObject planHateoas,
            JsonGenerator jgen) throws IOException {

        Plan plan = (Plan) noarkSystemIdEntity;
        jgen.writeStartObject();
        printSystemIdEntity(jgen, plan);
        printNullable(jgen, MUNICIPALITY_NUMBER, plan.getMunicipalityNumber());
        printNullable(jgen, COUNTY_NUMBER, plan.getCountyNumber());
        printNullableMetadata(jgen, COUNTRY_CODE, plan.getCountry());
        printNullable(jgen, PLAN_IDENTIFICATION, plan.getPlanIdentification());
        printHateoasLinks(jgen, planHateoas.getLinks(plan));
        jgen.writeEndObject();
    }
}
