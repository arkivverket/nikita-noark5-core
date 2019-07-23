package nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.nationalidentifier.Building;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.BUILDING_CHANGE_NUMBER;
import static nikita.common.config.N5ResourceMappings.BUILDING_NUMBER;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printHateoasLinks;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printSystemIdEntity;

public class BuildingSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INikitaEntity noarkSystemIdEntity,
            HateoasNoarkObject buildingHateoas,
            JsonGenerator jgen) throws IOException {

        Building building = (Building) noarkSystemIdEntity;
        jgen.writeStartObject();
        printSystemIdEntity(jgen, building);
        print(jgen, BUILDING_NUMBER, building.getBuildingNumber());
        printNullable(jgen, BUILDING_CHANGE_NUMBER,
                building.getContinuousNumberingOfBuildingChange());
        printHateoasLinks(jgen, buildingHateoas.getLinks(building));
        jgen.writeEndObject();
    }
}