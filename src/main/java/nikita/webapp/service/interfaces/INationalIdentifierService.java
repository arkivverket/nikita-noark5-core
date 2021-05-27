package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.*;
import nikita.common.model.noark5.v5.nationalidentifier.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface INationalIdentifierService {

    BuildingHateoas createNewBuilding(
            @NotNull final Building building,
            @NotNull final Record record);

    BuildingHateoas createNewBuilding(
            @NotNull final Building building,
            @NotNull final File file);

    CadastralUnitHateoas createNewCadastralUnit(
            @NotNull final CadastralUnit unit,
            @NotNull final Record record);

    CadastralUnitHateoas createNewCadastralUnit(
            @NotNull final CadastralUnit unit,
            @NotNull final File file);

    DNumberHateoas createNewDNumber(
            @NotNull final DNumber dNumber,
            @NotNull final Record record);

    DNumberHateoas createNewDNumber(
            @NotNull final DNumber dNumber,
            @NotNull final File file);

    PlanHateoas createNewPlan(
            @NotNull final Plan plan,
            @NotNull final Record record);

    PlanHateoas createNewPlan(
            @NotNull final Plan plan,
            @NotNull final File file);

    PositionHateoas createNewPosition(
            @NotNull final Position position,
            @NotNull final Record record);

    PositionHateoas createNewPosition(
            @NotNull final Position position,
            @NotNull final File file);

    SocialSecurityNumberHateoas createNewSocialSecurityNumber(
            @NotNull final SocialSecurityNumber socialSecurityNumber,
            @NotNull final Record record);

    SocialSecurityNumberHateoas createNewSocialSecurityNumber(
            @NotNull final SocialSecurityNumber socialSecurityNumber,
            @NotNull final File file);

    UnitHateoas createNewUnit(
            @NotNull final Unit unit,
            @NotNull final Record record);

    UnitHateoas createNewUnit(
            @NotNull final Unit unit,
            @NotNull final File file);

    BuildingHateoas findBuildingBySystemId(@NotNull final UUID systemId);

    UnitHateoas findUnitBySystemId(@NotNull final UUID systemId);

    PositionHateoas findPositionBySystemId(@NotNull final UUID systemId);

    PlanHateoas findPlanBySystemId(@NotNull final UUID systemId);

    SocialSecurityNumberHateoas findSocialSecurityNumberBySystemId(
            @NotNull final UUID systemId);

    DNumberHateoas findDNumberBySystemId(@NotNull final UUID systemId);

    CadastralUnitHateoas findCadastralUnitBySystemId(
            @NotNull final UUID systemId);

    // All PUT/UPDATE operations

    BuildingHateoas updateBuilding(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final Building incomingBuilding);

    CadastralUnitHateoas updateCadastralUnit(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final CadastralUnit incomingCadastralUnit);

    DNumberHateoas updateDNumber(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final DNumber incomingDNumber);

    PlanHateoas updatePlan(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final Plan incomingPlan);

    PositionHateoas updatePosition(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final Position incomingPosition);

    SocialSecurityNumberHateoas updateSocialSecurityNumber(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final SocialSecurityNumber incomingSocialSecurityNumber);

    UnitHateoas updateUnit(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final Unit incomingUnit);

    // All DELETE operations

    void deleteBuilding(@NotNull final UUID systemId);

    void deleteCadastralUnit(@NotNull final UUID systemId);

    void deleteDNumber(@NotNull final UUID systemId);

    void deletePlan(@NotNull final UUID systemId);

    void deletePosition(@NotNull final UUID systemId);

    void deleteSocialSecurityNumber(@NotNull final UUID systemId);

    void deleteUnit(@NotNull final UUID systemId);

    // All template operations
    BuildingHateoas generateDefaultBuilding(@NotNull final UUID systemId);

    CadastralUnitHateoas generateDefaultCadastralUnit(
            @NotNull final UUID systemId);

    DNumberHateoas generateDefaultDNumber(@NotNull final UUID systemId);

    PlanHateoas generateDefaultPlan(@NotNull final UUID systemId);

    PositionHateoas generateDefaultPosition(@NotNull final UUID systemId);

    SocialSecurityNumberHateoas generateDefaultSocialSecurityNumber(
            @NotNull final UUID systemId);

    UnitHateoas generateDefaultUnit(@NotNull final UUID systemId);
}
