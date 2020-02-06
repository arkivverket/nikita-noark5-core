package nikita.webapp.service.interfaces;


import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.*;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.*;
import nikita.common.model.noark5.v5.hateoas.secondary.CommentHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PartHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PartPersonHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PartUnitHateoas;
import nikita.common.model.noark5.v5.nationalidentifier.*;
import nikita.common.model.noark5.v5.secondary.Comment;
import nikita.common.model.noark5.v5.secondary.PartPerson;
import nikita.common.model.noark5.v5.secondary.PartUnit;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface IFileService {

    // -- All CREATE operations
    File createFile(File file);

    FileHateoas createFileAssociatedWithFile(String systemId, File file);

    CommentHateoas createCommentAssociatedWithFile
        (String systemID, Comment comment);

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

    CommentHateoas getCommentAssociatedWithFile
        (@NotNull final String systemID);

    PartHateoas getPartAssociatedWithFile(@NotNull final String systemID);

    NationalIdentifierHateoas getNationalIdentifierAssociatedWithFile
	(@NotNull final String systemID);

    CommentHateoas generateDefaultComment();

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
