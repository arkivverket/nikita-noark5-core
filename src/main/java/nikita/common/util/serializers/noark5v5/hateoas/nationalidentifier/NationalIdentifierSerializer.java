package nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier.IBuildingEntity;
import nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier.ICadastralUnitEntity;
import nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier.IDNumberEntity;
import nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier.IPlanEntity;
import nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier.IPositionEntity;
import nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier.ISocialSecurityNumberEntity;
import nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier.IUnitEntity;
import nikita.common.model.noark5.v5.nationalidentifier.Building;
import nikita.common.model.noark5.v5.nationalidentifier.CadastralUnit;
import nikita.common.model.noark5.v5.nationalidentifier.DNumber;
import nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier;
import nikita.common.model.noark5.v5.nationalidentifier.Plan;
import nikita.common.model.noark5.v5.nationalidentifier.Position;
import nikita.common.model.noark5.v5.nationalidentifier.SocialSecurityNumber;
import nikita.common.model.noark5.v5.nationalidentifier.Unit;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger =
            LoggerFactory.getLogger(NationalIdentifierSerializer.class);

    // TODO figure out how to avoid duplicating code with BuildingSerializer
    public void printBuilding(IBuildingEntity building,
                              HateoasNoarkObject buildingHateoas,
                              JsonGenerator jgen)
        throws IOException {
        jgen.writeStartObject();
        printSystemIdEntity(jgen, building);
        print(jgen, BUILDING_NUMBER, building.getBuildingNumber());
        printNullable(jgen, BUILDING_CHANGE_NUMBER,
                building.getRunningChangeNumber());
        printHateoasLinks(jgen, buildingHateoas.getLinks(building));
        jgen.writeEndObject();
    }

    // TODO figure out how to avoid duplicating code with CadastralUnitSerializer
    public void printCadastralUnit(ICadastralUnitEntity cadastralUnit,
                              HateoasNoarkObject cadastralUnitHateoas,
                              JsonGenerator jgen)
        throws IOException {
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

    // TODO figure out how to avoid duplicating code with DNumberSerializer
    public void printDNumber(IDNumberEntity dNumber,
                             HateoasNoarkObject dNumberHateoas,
                             JsonGenerator jgen)
        throws IOException {
        jgen.writeStartObject();
        printSystemIdEntity(jgen, dNumber);
        printNullable(jgen, D_NUMBER_FIELD, dNumber.getdNumber());
        printHateoasLinks(jgen, dNumberHateoas.getLinks(dNumber));
        jgen.writeEndObject();
    }

    // TODO figure out how to avoid duplicating code with PlanSerializer
    public void printPlan(IPlanEntity plan,
                          HateoasNoarkObject planHateoas,
                          JsonGenerator jgen)
        throws IOException {
        jgen.writeStartObject();
        printSystemIdEntity(jgen, plan);
        printNullable(jgen, MUNICIPALITY_NUMBER, plan.getMunicipalityNumber());
        printNullable(jgen, COUNTY_NUMBER, plan.getCountyNumber());
        printNullableMetadata(jgen, COUNTRY_CODE, plan.getCountry());
        printNullable(jgen, PLAN_IDENTIFICATION, plan.getPlanIdentification());
        printHateoasLinks(jgen, planHateoas.getLinks(plan));
        jgen.writeEndObject();
    }

    // TODO figure out how to avoid duplicating code with PositionSerializer
    private void printPosition(IPositionEntity position,
                               HateoasNoarkObject positionHateoas,
                               JsonGenerator jgen)
        throws IOException {
        jgen.writeStartObject();
        printSystemIdEntity(jgen, position);
        printNullableMetadata(jgen, COORDINATE_SYSTEM,
                              position.getCoordinateSystem());
        printNullable(jgen, X, position.getX());
        printNullable(jgen, Y, position.getY());
        printNullable(jgen, Z, position.getZ());
        printHateoasLinks(jgen, positionHateoas.getLinks(position));
        jgen.writeEndObject();
    }

    // TODO figure out how to avoid duplicating code with SocialSecurityNumberSerializer
    public void printSocialSecurityNumber
        (ISocialSecurityNumberEntity socialSecurityNumber,
         HateoasNoarkObject socialSecurityNumberHateoas,
         JsonGenerator jgen)
        throws IOException {
        jgen.writeStartObject();
        printSystemIdEntity(jgen, socialSecurityNumber);
        printNullable(jgen, SOCIAL_SECURITY_NUMBER,
                      socialSecurityNumber.getSocialSecurityNumber());
        printHateoasLinks(jgen, socialSecurityNumberHateoas.
                getLinks(socialSecurityNumber));
        jgen.writeEndObject();
    }

    // TODO figure out how to avoid duplicating code with UnitSerializer
    private void printUnit(IUnitEntity unit,
                               HateoasNoarkObject unitHateoas,
                               JsonGenerator jgen)
        throws IOException {
        jgen.writeStartObject();
        printSystemIdEntity(jgen, unit);
        printNullable(jgen, ORGANISATION_NUMBER, unit.getOrganisationNumber());
        printHateoasLinks(jgen, unitHateoas.getLinks(unit));
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
        } else if (id instanceof CadastralUnit) {
            printCadastralUnit((ICadastralUnitEntity)id, nationalIdentifierHateoas, jgen);
        } else if (id instanceof DNumber) {
            printDNumber((IDNumberEntity)id, nationalIdentifierHateoas, jgen);
        } else if (id instanceof Plan) {
            printPlan((IPlanEntity)id, nationalIdentifierHateoas, jgen);
        } else if (id instanceof Position) {
            printPosition((IPositionEntity)id, nationalIdentifierHateoas, jgen);
        } else if (id instanceof SocialSecurityNumber) {
            printSocialSecurityNumber
                ((ISocialSecurityNumberEntity)id, nationalIdentifierHateoas, jgen);
        } else if (id instanceof Unit) {
            printUnit((IUnitEntity)id, nationalIdentifierHateoas, jgen);
        } else {
            logger.warn("Unhandled national identifier "
                        + noarkSystemIdEntity.getBaseTypeName()
                        + " not serialized");
        }
    }
}
