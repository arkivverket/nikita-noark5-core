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

    // FIXME figure out how to avoid duplicating code with CadastralUnitSerializer
    public void printCadastralUnit(ICadastralUnitEntity cadastralUnit,
                              HateoasNoarkObject cadastralUnitHateoas,
                              JsonGenerator jgen)
        throws IOException {
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

    // FIXME figure out how to avoid duplicating code with DNumberSerializer
    public void printDNumber(IDNumberEntity dNumber,
                             HateoasNoarkObject dNumberHateoas,
                             JsonGenerator jgen)
        throws IOException {
        jgen.writeStartObject();
        printSystemIdEntity(jgen, dNumber);
        jgen.writeStringField(D_NUMBER_FIELD, dNumber.getdNumber());
        printHateoasLinks(jgen, dNumberHateoas.getLinks(dNumber));
        jgen.writeEndObject();
    }

    // FIXME figure out how to avoid duplicating code with PlanSerializer
    public void printPlan(IPlanEntity plan,
                          HateoasNoarkObject planHateoas,
                          JsonGenerator jgen)
        throws IOException {
        jgen.writeStartObject();
        printSystemIdEntity(jgen, plan);
        if (null != plan.getMunicipalityNumber()) {
            jgen.writeStringField(MUNICIPALITY_NUMBER,
                    plan.getMunicipalityNumber());
        }
        if (null != plan.getCountyNumber()) {
            jgen.writeStringField(COUNTY_NUMBER, plan.getCountyNumber());
        }
        if (null != plan.getCountry()) {
            jgen.writeStringField(COUNTRY, plan.getCountry().getCode());
        }
        jgen.writeStringField(PLAN_IDENTIFICATION,
                plan.getPlanIdentification());
        printHateoasLinks(jgen, planHateoas.getLinks(plan));
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

    // FIXME figure out how to avoid duplicating code with SocialSecurityNumberSerializer
    public void printSocialSecurityNumber
        (ISocialSecurityNumberEntity socialSecurityNumber,
         HateoasNoarkObject socialSecurityNumberHateoas,
         JsonGenerator jgen)
        throws IOException {
        jgen.writeStartObject();
        printSystemIdEntity(jgen, socialSecurityNumber);
        jgen.writeStringField(SOCIAL_SECURITY_NUMBER,
                socialSecurityNumber.getSocialSecurityNumber());
        printHateoasLinks(jgen, socialSecurityNumberHateoas.
                getLinks(socialSecurityNumber));
        jgen.writeEndObject();
    }

    // FIXME figure out how to avoid duplicating code with UnitSerializer
    private void printUnit(IUnitEntity unit,
                               HateoasNoarkObject unitHateoas,
                               JsonGenerator jgen)
        throws IOException {
        jgen.writeStartObject();
        printSystemIdEntity(jgen, unit);
        print(jgen, ORGANISATION_NUMBER, unit.getOrganisationNumber());
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
