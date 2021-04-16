package utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.metadata.CoordinateSystem;
import nikita.common.model.noark5.v5.metadata.Country;
import nikita.common.model.noark5.v5.nationalidentifier.*;

import java.io.IOException;
import java.io.StringWriter;

import static nikita.common.config.N5ResourceMappings.*;
import static utils.TestConstants.*;

public final class NationalIdentifierCreator {

    public static String getTestBuildingAsJson() throws IOException {
        Building building = createBuilding();
        JsonFactory factory = new JsonFactory();
        StringWriter jsonBuildingWriter = new StringWriter();
        JsonGenerator jsonBuilding =
                factory.createGenerator(jsonBuildingWriter);
        jsonBuilding.writeStartObject(BUILDING);
        jsonBuilding.writeNumberField(BUILDING_NUMBER,
                building.getBuildingNumber());
        jsonBuilding.writeNumberField(BUILDING_CHANGE_NUMBER,
                building.getRunningChangeNumber());
        jsonBuilding.writeEndObject();
        jsonBuilding.close();
        return jsonBuildingWriter.toString();
    }

    public static Building createBuilding() {
        Building building = new Building();
        building.setBuildingNumber(99);
        building.setRunningChangeNumber(898);
        return building;
    }

    public static String getTestPlanAsJson() throws IOException {
        Plan plan = createPlan();
        JsonFactory factory = new JsonFactory();
        StringWriter jsonPlanWriter = new StringWriter();
        JsonGenerator jsonPlan =
                factory.createGenerator(jsonPlanWriter);
        jsonPlan.writeStartObject(PLAN);
        jsonPlan.writeStringField(PLAN_IDENTIFICATION,
                plan.getPlanIdentification());
        jsonPlan.writeStringField(COUNTY_NUMBER, plan.getCountyNumber());
        jsonPlan.writeStringField(MUNICIPALITY_NUMBER,
                plan.getMunicipalityNumber());
        jsonPlan.writeObjectFieldStart(COUNTRY_CODE);
        jsonPlan.writeStringField(CODE, plan.getCountry().getCode());
        jsonPlan.writeStringField(CODE_NAME, plan.getCountry().getCodeName());
        jsonPlan.writeEndObject();
        jsonPlan.writeEndObject();
        jsonPlan.close();
        return jsonPlanWriter.toString();
    }

    public static Plan createPlan() {
        Plan plan = new Plan();
        Country country = new Country();
        country.setCode(PLAN_COUNTRY_CODE_VALUE);
        country.setCodeName(PLAN_COUNTRY_CODE_NAME_VALUE);
        plan.setCountry(country);
        plan.setPlanIdentification(PLAN_IDENTIFICATION_VALUE);
        plan.setCountyNumber(PLAN_COUNTY_NUMBER_VALUE);
        plan.setMunicipalityNumber(PLAN_MUNICIPALITY_NUMBER_VALUE);
        return plan;
    }

    public static String getTestPositionAsJson() throws IOException {
        Position position = createPosition();
        JsonFactory factory = new JsonFactory();
        StringWriter jsonPositionWriter = new StringWriter();
        JsonGenerator jsonPosition =
                factory.createGenerator(jsonPositionWriter);
        jsonPosition.writeStartObject(POSITION);
        jsonPosition.writeObjectFieldStart(COORDINATE_SYSTEM);
        jsonPosition.writeStringField(CODE,
                position.getCoordinateSystem().getCode());
        jsonPosition.writeStringField(CODE_NAME,
                position.getCoordinateSystem().getCodeName());
        jsonPosition.writeEndObject();
        jsonPosition.writeNumberField(X, position.getX());
        jsonPosition.writeNumberField(Y, position.getY());
        jsonPosition.writeNumberField(Z, position.getZ());
        jsonPosition.writeEndObject();
        jsonPosition.close();
        return jsonPositionWriter.toString();
    }

    public static Position createPosition() {
        Position position = new Position();
        CoordinateSystem coordinateSystem = new CoordinateSystem();
        coordinateSystem.setCode(POSITION_CODE_VALUE);
        coordinateSystem.setCodeName(POSITION_CODE_NAME_VALUE);
        position.setCoordinateSystem(coordinateSystem);
        position.setX(1.01);
        position.setY(2.02);
        position.setZ(3.03);
        return position;
    }

    public static String getTestUnitAsJson() throws IOException {
        Unit unit = createUnit();
        JsonFactory factory = new JsonFactory();
        StringWriter jsonUnitWriter = new StringWriter();
        JsonGenerator jsonUnit =
                factory.createGenerator(jsonUnitWriter);
        jsonUnit.writeStartObject(UNIT_IDENTIFIER);
        jsonUnit.writeStringField(ORGANISATION_NUMBER,
                unit.getUnitIdentifier());
        jsonUnit.writeEndObject();
        jsonUnit.close();
        return jsonUnitWriter.toString();
    }

    public static Unit createUnit() {
        Unit unit = new Unit();
        unit.setUnitIdentifier(ORGANISATION_NUMBER_VALUE);
        return unit;
    }

    public static String getTestDNumberAsJson() throws IOException {
        DNumber dNumber = createDNumber();
        JsonFactory factory = new JsonFactory();
        StringWriter jsonDNumberWriter = new StringWriter();
        JsonGenerator jsonDNumber =
                factory.createGenerator(jsonDNumberWriter);
        jsonDNumber.writeStartObject(D_NUMBER);
        jsonDNumber.writeStringField(D_NUMBER_FIELD, dNumber.getdNumber());
        jsonDNumber.writeEndObject();
        jsonDNumber.close();
        return jsonDNumberWriter.toString();
    }

    public static DNumber createDNumber() {
        DNumber dNumber = new DNumber();
        dNumber.setdNumber(D_NUMBER_VALUE);
        return dNumber;
    }

    public static String getTestSocialSecurityNumberAsJson() throws IOException {
        SocialSecurityNumber socialSecurityNumber = createSocialSecurityNumber();
        JsonFactory factory = new JsonFactory();
        StringWriter jsonSocialSecurityNumberWriter = new StringWriter();
        JsonGenerator jsonSocialSecurityNumber =
                factory.createGenerator(jsonSocialSecurityNumberWriter);
        jsonSocialSecurityNumber.writeStartObject(SOCIAL_SECURITY_NUMBER);
        jsonSocialSecurityNumber.writeStringField(SOCIAL_SECURITY_NUMBER,
                socialSecurityNumber.getSocialSecurityNumber());
        jsonSocialSecurityNumber.writeEndObject();
        jsonSocialSecurityNumber.close();
        return jsonSocialSecurityNumberWriter.toString();
    }

    public static SocialSecurityNumber createSocialSecurityNumber() {
        SocialSecurityNumber socialSecurityNumber = new SocialSecurityNumber();
        socialSecurityNumber.setSocialSecurityNumber(SS_NUMBER_VALUE);
        return socialSecurityNumber;
    }

    public static String getTestCadastralUnitAsJson() throws IOException {
        CadastralUnit cadastralUnit = createCadastralUnit();
        JsonFactory factory = new JsonFactory();
        StringWriter jsonCadastralUnitWriter = new StringWriter();
        JsonGenerator jsonCadastralUnit =
                factory.createGenerator(jsonCadastralUnitWriter);
        jsonCadastralUnit.writeStartObject(CADASTRAL_UNIT);
        jsonCadastralUnit.writeStringField(MUNICIPALITY_NUMBER,
                cadastralUnit.getMunicipalityNumber());
        jsonCadastralUnit.writeNumberField(HOLDING_NUMBER,
                cadastralUnit.getHoldingNumber());
        jsonCadastralUnit.writeNumberField(SUB_HOLDING_NUMBER,
                cadastralUnit.getSubHoldingNumber());
        jsonCadastralUnit.writeNumberField(LEASE_NUMBER,
                cadastralUnit.getLeaseNumber());
        jsonCadastralUnit.writeNumberField(SECTION_NUMBER,
                cadastralUnit.getSectionNumber());
        jsonCadastralUnit.writeEndObject();
        jsonCadastralUnit.close();
        return jsonCadastralUnitWriter.toString();
    }

    public static CadastralUnit createCadastralUnit() {
        CadastralUnit cadastralUnit = new CadastralUnit();
        cadastralUnit.setMunicipalityNumber(MUNICIPALITY_NUMBER_VALUE);
        cadastralUnit.setHoldingNumber(HOLDING_NUMBER_VALUE);
        cadastralUnit.setSubHoldingNumber(SUB_HOLDING_NUMBER_VALUE);
        cadastralUnit.setLeaseNumber(LEASE_NUMBER_VALUE);
        cadastralUnit.setSectionNumber(SECTION_NUMBER_VALUE);
        return cadastralUnit;
    }
}
