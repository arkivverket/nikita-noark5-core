package nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier.IBuildingEntity;
import nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier.IPositionEntity;
import nikita.common.model.noark5.v5.nationalidentifier.Building;
import nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier;
import nikita.common.model.noark5.v5.nationalidentifier.Position;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

/**
 * Serialise an outgoing NationalIdentifier object as JSON.
 * <p>
 * Having an own serializer is done to have more fine grained control over
 * the output. We need to be able to especially control the HATEOAS links and
 * the actual format of the HATEOAS links might change over time with the
 * standard. This allows us to be able to easily adapt to any changes
 * <p>
 * Note. Only values that are part of the standard are included in the JSON.
 * Properties like 'id' or 'deleted' are not exported
 */
public class NationalIdentifierSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    // FIXME figure out how to avoid duplicating code with BuildingSerializer
    public void printBuilding(IBuildingEntity building,
                              HateoasNoarkObject buildingHateoas,
                              JsonGenerator jgen)
        throws IOException {
        jgen.writeStartObject();
        printSystemIdEntity(jgen, building);
        print(jgen, BUILDING_NUMBER, building.getBuildingNumber());
        printNullable(jgen, BUILDING_CHANGE_NUMBER,
                building.getContinuousNumberingOfBuildingChange());
        printHateoasLinks(jgen, buildingHateoas.getLinks(building));
        jgen.writeEndObject();
    }

    // FIXME figure out how to avoid duplicating code with PositionSerializer
    private void printPosition(IPositionEntity position,
                               HateoasNoarkObject positionHateoas,
                               JsonGenerator jgen)
        throws IOException {
        jgen.writeStartObject();
        printSystemIdEntity(jgen, position);

        jgen.writeObjectFieldStart(COORDINATE_SYSTEM);
        printCode(jgen,
                  position.getCoordinateSystemCode(),
                  position.getCoordinateSystemCodeName());
        jgen.writeEndObject();

        jgen.writeNumberField(X, position.getX());
        jgen.writeNumberField(Y, position.getY());
        if (null != position.getZ()) {
            jgen.writeNumberField(Z, position.getZ());
        }
        printHateoasLinks(jgen, positionHateoas.getLinks(position));
        jgen.writeEndObject();
    }

    @Override
    public void serializeNoarkEntity(
            INoarkEntity noarkSystemIdEntity,
            HateoasNoarkObject nationalIdentifierHateoas,
            JsonGenerator jgen)
            throws IOException {

        NationalIdentifier id = (NationalIdentifier) noarkSystemIdEntity;

        if (id instanceof Building) {
            printBuilding((IBuildingEntity)id, nationalIdentifierHateoas, jgen);
        }
        if (id instanceof Position) {
            printPosition((IPositionEntity)id, nationalIdentifierHateoas, jgen);
        }
        // FIXME add the remaining identifiers
    }
}
