package nikita.webapp.service.interfaces;


import nikita.common.model.nikita.PatchMerge;
import nikita.common.model.nikita.PatchObjects;
import nikita.common.model.noark5.bsm.BSMBase;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.ClassHateoas;
import nikita.common.model.noark5.v5.hateoas.FileHateoas;
import nikita.common.model.noark5.v5.hateoas.RecordHateoas;
import nikita.common.model.noark5.v5.hateoas.SeriesHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileExpansionHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.*;
import nikita.common.model.noark5.v5.hateoas.secondary.*;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.model.noark5.v5.nationalidentifier.*;
import nikita.common.model.noark5.v5.secondary.*;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public interface IFileService {

    // -- All CREATE operations
    File createFile(File file);

    CaseFileHateoas expandToCaseFile(
            @NotNull final UUID systemId, @NotNull final PatchMerge patchMerge);

    FileHateoas createFileAssociatedWithFile(String systemId, File file);

    CommentHateoas createCommentAssociatedWithFile
            (String systemID, Comment comment);

    PartPersonHateoas createPartPersonAssociatedWithFile(
            String systemID, PartPerson partPerson);

    PartUnitHateoas createPartUnitAssociatedWithFile(
            String systemID, PartUnit partUnit);

    KeywordHateoas createKeywordAssociatedWithFile(
            @NotNull final UUID systemId, @NotNull final Keyword keyword);

    FileHateoas save(File file);

    ResponseEntity<RecordHateoas> createRecordAssociatedWithFile(
            @NotNull final String fileSystemId,
            @NotNull final Record record);

    StorageLocationHateoas createStorageLocationAssociatedWithFile(
            @NotNull final UUID systemId,
            @NotNull final StorageLocation storageLocation);

    // -- All READ operations

    PartPersonHateoas generateDefaultPartPerson(String systemID);

    PartUnitHateoas generateDefaultPartUnit(String systemID);

    CaseFileExpansionHateoas generateDefaultValuesToExpandToCaseFile(
            @NotNull final UUID systemId);

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

    File getFileOrThrow(@NotNull String systemId);

    File getFileOrThrow(@NotNull UUID systemId);

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

    ScreeningMetadataHateoas getScreeningMetadataAssociatedWithFile(
            @NotNull final UUID fileSystemId);

    CommentHateoas generateDefaultComment();

    BuildingHateoas generateDefaultBuilding();

    CadastralUnitHateoas generateDefaultCadastralUnit();

    DNumberHateoas generateDefaultDNumber();

    PlanHateoas generateDefaultPlan();

    PositionHateoas generateDefaultPosition();

    Object associateBSM(@NotNull UUID systemId,
                        @NotNull List<BSMBase> bsm);

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

    ResponseEntity<FileHateoas> handleUpdate(
            UUID systemID, PatchObjects patchObjects);

    ScreeningMetadataHateoas createScreeningMetadataAssociatedWithFile(
            @NotNull final UUID systemID,
            @NotNull final Metadata screeningMetadata);

    ScreeningMetadataHateoas getDefaultScreeningMetadata(
            @NotNull final UUID systemID);

    KeywordHateoas generateDefaultKeyword();

    StorageLocationHateoas getDefaultStorageLocation(
            @NotNull final UUID systemID);
}
