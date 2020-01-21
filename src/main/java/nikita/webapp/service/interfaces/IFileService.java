package nikita.webapp.service.interfaces;


import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.PartPerson;
import nikita.common.model.noark5.v5.PartUnit;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.*;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.BuildingHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.CadastralUnitHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.DNumberHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.PlanHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.NationalIdentifierHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.PositionHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.SocialSecurityNumberHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.UnitHateoas;
import nikita.common.model.noark5.v5.nationalidentifier.Building;
import nikita.common.model.noark5.v5.nationalidentifier.CadastralUnit;
import nikita.common.model.noark5.v5.nationalidentifier.DNumber;
import nikita.common.model.noark5.v5.nationalidentifier.Plan;
import nikita.common.model.noark5.v5.nationalidentifier.Position;
import nikita.common.model.noark5.v5.nationalidentifier.SocialSecurityNumber;
import nikita.common.model.noark5.v5.nationalidentifier.Unit;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface IFileService {

    // -- All CREATE operations
    File createFile(File file);

    FileHateoas createFileAssociatedWithFile(String systemId, File file);

    PartPersonHateoas createPartPersonAssociatedWithFile(
            String systemID, PartPerson partPerson);

    PartUnitHateoas createPartUnitAssociatedWithFile(
            String systemID, PartUnit partUnit);

    FileHateoas save(File file);

    ResponseEntity<RecordHateoas> createRecordAssociatedWithFile(
            @NotNull final String fileSystemId,
            @NotNull final Record record);

    // -- All READ operations

    PartPersonHateoas generateDefaultPartPerson(String systemID);

    PartUnitHateoas generateDefaultPartUnit(String systemID);

    BuildingHateoas
    createBuildingAssociatedWithFile(
            @NotNull String systemID, @NotNull Building building);

    CadastralUnitHateoas
    createCadastralUnitAssociatedWithFile(
            @NotNull String systemID, @NotNull CadastralUnit cadastralUnit);

    DNumberHateoas
    createDNumberAssociatedWithFile(
            @NotNull String systemID, @NotNull DNumber dNumber);

    PlanHateoas
    createPlanAssociatedWithFile(
            @NotNull String systemID, @NotNull Plan plan);

    PositionHateoas
    createPositionAssociatedWithFile(
            @NotNull String systemID, @NotNull Position position);

    SocialSecurityNumberHateoas
    createSocialSecurityNumberAssociatedWithFile
        (@NotNull String systemID,
         @NotNull SocialSecurityNumber socialSecurityNumber);

    UnitHateoas
    createUnitAssociatedWithFile(
            @NotNull String systemID, @NotNull Unit unit);

    List<File> findAll();

    FileHateoas findAllChildren(@NotNull String systemId);

    File findBySystemId(String systemId);

    List<File> findByOwnedBy(String ownedBy);

    ResponseEntity<ClassHateoas>
    findClassAssociatedWithFile(@NotNull final String systemId);

    ResponseEntity<SeriesHateoas>
    findSeriesAssociatedWithFile(@NotNull final String systemId);

    PartHateoas getPartAssociatedWithFile(@NotNull final String systemID);

    NationalIdentifierHateoas getNationalIdentifierAssociatedWithFile
	(@NotNull final String systemID);

    BuildingHateoas generateDefaultBuilding();

    CadastralUnitHateoas generateDefaultCadastralUnit();

    DNumberHateoas generateDefaultDNumber();

    PlanHateoas generateDefaultPlan();

    PositionHateoas generateDefaultPosition();

    SocialSecurityNumberHateoas generateDefaultSocialSecurityNumber();

    UnitHateoas generateDefaultUnit();

    // -- All UPDATE operations
    File handleUpdate(@NotNull String systemId,
                      @NotNull Long version,
                      @NotNull File file);

    // -- All DELETE operations
    void deleteEntity(@NotNull String systemId);

    FileHateoas generateDefaultFile();

    long deleteAllByOwnedBy();
}
