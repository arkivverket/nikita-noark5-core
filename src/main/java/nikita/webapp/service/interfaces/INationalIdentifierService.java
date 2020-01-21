package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.BuildingHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.CadastralUnitHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.DNumberHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.PlanHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.PositionHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.SocialSecurityNumberHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.UnitHateoas;
import nikita.common.model.noark5.v5.nationalidentifier.Building;
import nikita.common.model.noark5.v5.nationalidentifier.CadastralUnit;
import nikita.common.model.noark5.v5.nationalidentifier.DNumber;
import nikita.common.model.noark5.v5.nationalidentifier.Plan;
import nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier;
import nikita.common.model.noark5.v5.nationalidentifier.Position;
import nikita.common.model.noark5.v5.nationalidentifier.SocialSecurityNumber;
import nikita.common.model.noark5.v5.nationalidentifier.Unit;

import javax.validation.constraints.NotNull;

public interface INationalIdentifierService {

    BuildingHateoas createNewBuilding(
            @NotNull Building id,
            @NotNull Record record);

    BuildingHateoas createNewBuilding(
            @NotNull Building id,
            @NotNull File file);

    CadastralUnitHateoas createNewCadastralUnit(
            @NotNull CadastralUnit id,
            @NotNull Record record);

    CadastralUnitHateoas createNewCadastralUnit(
            @NotNull CadastralUnit id,
            @NotNull File file);

    DNumberHateoas createNewDNumber(
            @NotNull DNumber id,
            @NotNull Record record);

    DNumberHateoas createNewDNumber(
            @NotNull DNumber id,
            @NotNull File file);

    PlanHateoas createNewPlan(
            @NotNull Plan id,
            @NotNull Record record);

    PlanHateoas createNewPlan(
            @NotNull Plan id,
            @NotNull File file);

    PositionHateoas createNewPosition(
            @NotNull Position id,
            @NotNull Record record);

    PositionHateoas createNewPosition(
            @NotNull Position id,
            @NotNull File file);

    SocialSecurityNumberHateoas createNewSocialSecurityNumber(
            @NotNull SocialSecurityNumber id,
            @NotNull Record record);

    SocialSecurityNumberHateoas createNewSocialSecurityNumber(
            @NotNull SocialSecurityNumber id,
            @NotNull File file);

    UnitHateoas createNewUnit(
            @NotNull Unit id,
            @NotNull Record record);

    UnitHateoas createNewUnit(
            @NotNull Unit id,
            @NotNull File file);

    NationalIdentifier findBySystemId(@NotNull String systemID);

    // All PUT/UPDATE operations

    public Building updateBuilding(
            @NotNull String systemId, @NotNull Long version,
            @NotNull Building incomingBuilding);

    public CadastralUnit updateCadastralUnit(
            @NotNull String systemId, @NotNull Long version,
            @NotNull CadastralUnit incomingCadastralUnit);

    public DNumber updateDNumber(
            @NotNull String systemId, @NotNull Long version,
            @NotNull DNumber incomingDNumber);

    public Plan updatePlan(
            @NotNull String systemId, @NotNull Long version,
            @NotNull Plan incomingPlan);

    public Position updatePosition(
            @NotNull String systemId, @NotNull Long version,
            @NotNull Position incomingPosition);

    public SocialSecurityNumber updateSocialSecurityNumber(
            @NotNull String systemId, @NotNull Long version,
            @NotNull SocialSecurityNumber incomingSocialSecurityNumber);

    public Unit updateUnit(
            @NotNull String systemId, @NotNull Long version,
            @NotNull Unit incomingUnit);

    // All DELETE operations

    void deleteBuilding(@NotNull String systemID);

    void deleteCadastralUnit(@NotNull String systemID);

    void deleteDNumber(@NotNull String systemID);

    void deletePlan(@NotNull String systemID);

    void deletePosition(@NotNull String systemID);

    void deleteSocialSecurityNumber(@NotNull String systemID);

    void deleteUnit(@NotNull String systemID);

    // All template operations
    BuildingHateoas generateDefaultBuilding();

    CadastralUnitHateoas generateDefaultCadastralUnit();

    DNumberHateoas generateDefaultDNumber();

    PlanHateoas generateDefaultPlan();

    PositionHateoas generateDefaultPosition();

    SocialSecurityNumberHateoas generateDefaultSocialSecurityNumber();

    UnitHateoas generateDefaultUnit();
}
