package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.BuildingHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.PositionHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.UnitHateoas;
import nikita.common.model.noark5.v5.nationalidentifier.Building;
import nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier;
import nikita.common.model.noark5.v5.nationalidentifier.Position;
import nikita.common.model.noark5.v5.nationalidentifier.Unit;

import javax.validation.constraints.NotNull;

public interface INationalIdentifierService {

    BuildingHateoas createNewBuilding(
            @NotNull Building id,
            @NotNull Record record);

    PositionHateoas createNewPosition(
            @NotNull Position id,
            @NotNull Record record);

    UnitHateoas createNewUnit(
            @NotNull Unit id,
            @NotNull Record record);

    BuildingHateoas createNewBuilding(
            @NotNull Building id,
            @NotNull File file);

    PositionHateoas createNewPosition(
            @NotNull Position id,
            @NotNull File file);

    UnitHateoas createNewUnit(
            @NotNull Unit id,
            @NotNull File file);

    NationalIdentifier findBySystemId(@NotNull String systemID);

    // All PUT/UPDATE operations

    public Building updateBuilding(
            @NotNull String systemId, @NotNull Long version,
            @NotNull Building incomingBuilding);

    public Position updatePosition(
            @NotNull String systemId, @NotNull Long version,
            @NotNull Position incomingPosition);

    public Unit updateUnit(
            @NotNull String systemId, @NotNull Long version,
            @NotNull Unit incomingUnit);

    // All DELETE operations

    void deleteBuilding(@NotNull String systemID);

    void deletePosition(@NotNull String systemID);

    void deleteUnit(@NotNull String systemID);

    // All template operations
    BuildingHateoas generateDefaultBuilding();

    PositionHateoas generateDefaultPosition();

    UnitHateoas generateDefaultUnit();
}
