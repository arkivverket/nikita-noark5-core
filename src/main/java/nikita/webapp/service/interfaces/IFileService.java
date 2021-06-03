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

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public interface IFileService {

    // -- All CREATE operations
    FileHateoas createFile(@NotNull final File file);

    CaseFileHateoas expandToCaseFile(
            @NotNull final UUID systemId,
            @NotNull final PatchMerge patchMerge);

    FileHateoas createFileAssociatedWithFile(
            @NotNull final UUID systemId,
            @NotNull final File file);

    CommentHateoas createCommentAssociatedWithFile
            (@NotNull final UUID systemId,
             @NotNull final Comment comment);

    PartPersonHateoas createPartPersonAssociatedWithFile(
            @NotNull final UUID systemId,
            @NotNull final PartPerson partPerson);

    PartUnitHateoas createPartUnitAssociatedWithFile(
            @NotNull final UUID systemId,
            @NotNull final PartUnit partUnit);

    KeywordHateoas createKeywordAssociatedWithFile(
            @NotNull final UUID systemId,
            @NotNull final Keyword keyword);

    FileHateoas save(@NotNull final File file);

    RecordHateoas createRecordAssociatedWithFile(
            @NotNull final UUID systemId,
            @NotNull final Record record);

    StorageLocationHateoas createStorageLocationAssociatedWithFile(
            @NotNull final UUID systemId,
            @NotNull final StorageLocation storageLocation);

    // -- All READ operations

    PartPersonHateoas generateDefaultPartPerson(@NotNull final UUID systemId);

    PartUnitHateoas generateDefaultPartUnit(@NotNull final UUID systemId);

    CaseFileExpansionHateoas generateDefaultValuesToExpandToCaseFile(
            @NotNull final UUID systemId);

    BuildingHateoas
    createBuildingAssociatedWithFile(
            @NotNull final UUID systemId, @NotNull Building building);

    CadastralUnitHateoas
    createCadastralUnitAssociatedWithFile(
            @NotNull final UUID systemId,
            @NotNull final CadastralUnit cadastralUnit);

    DNumberHateoas
    createDNumberAssociatedWithFile(
            @NotNull final UUID systemId,
            @NotNull final DNumber dNumber);

    PlanHateoas
    createPlanAssociatedWithFile(
            @NotNull final UUID systemId,
            @NotNull final Plan plan);

    PositionHateoas
    createPositionAssociatedWithFile(
            @NotNull final UUID systemId,
            @NotNull final Position position);

    SocialSecurityNumberHateoas
    createSocialSecurityNumberAssociatedWithFile
            (@NotNull final UUID systemId,
             @NotNull final SocialSecurityNumber socialSecurityNumber);

    UnitHateoas
    createUnitAssociatedWithFile(
            @NotNull final UUID systemId, @NotNull final Unit unit);

    FileHateoas findAllChildren(@NotNull final UUID systemId);

    RecordHateoas findAllRecords(@NotNull final UUID systemId);

    FileHateoas findBySystemId(@NotNull final UUID systemId);

    FileHateoas findAll();

    ClassHateoas
    findClassAssociatedWithFile(@NotNull final UUID systemId);

    SeriesHateoas
    findSeriesAssociatedWithFile(@NotNull final UUID systemId);

    CommentHateoas getCommentAssociatedWithFile(
            @NotNull final UUID systemId);

    PartHateoas getPartAssociatedWithFile(@NotNull final UUID systemId);

    KeywordHateoas findKeywordAssociatedWithFile(@NotNull final UUID systemId);

    StorageLocationHateoas getStorageLocationAssociatedWithFile(
            @NotNull final UUID systemId);

    NationalIdentifierHateoas getNationalIdentifierAssociatedWithFile(
            @NotNull final UUID systemId);

    ScreeningMetadataHateoas getScreeningMetadataAssociatedWithFile(
            @NotNull final UUID systemId);

    CommentHateoas generateDefaultComment(@NotNull final UUID systemId);

    BuildingHateoas generateDefaultBuilding(@NotNull final UUID systemId);

    CadastralUnitHateoas generateDefaultCadastralUnit(@NotNull final UUID systemId);

    DNumberHateoas generateDefaultDNumber(@NotNull final UUID systemId);

    PlanHateoas generateDefaultPlan(@NotNull final UUID systemId);

    PositionHateoas generateDefaultPosition(@NotNull final UUID systemId);

    Object associateBSM(@NotNull final UUID systemId,
                        @NotNull final List<BSMBase> bsm);

    SocialSecurityNumberHateoas generateDefaultSocialSecurityNumber(
            @NotNull final UUID systemId);

    UnitHateoas generateDefaultUnit(@NotNull final UUID systemId);

    // -- All UPDATE operations
    FileHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final Long version,
            @NotNull final File file);

    // -- All DELETE operations
    void deleteEntity(@NotNull final UUID systemId);

    FileHateoas generateDefaultFile(@NotNull final UUID systemId);

    void deleteAllByOwnedBy();

    FileHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final PatchObjects patchObjects);

    ScreeningMetadataHateoas createScreeningMetadataAssociatedWithFile(
            @NotNull final UUID systemId,
            @NotNull final Metadata screeningMetadata);

    CrossReferenceHateoas createCrossReferenceAssociatedWithFile(
            @NotNull final UUID systemId,
            @NotNull final CrossReference crossReference);

    ScreeningMetadataHateoas getDefaultScreeningMetadata(
            @NotNull final UUID systemId);

    KeywordHateoas generateDefaultKeyword(@NotNull final UUID systemId);

    StorageLocationHateoas getDefaultStorageLocation(
            @NotNull final UUID systemId);

    CrossReferenceHateoas getDefaultCrossReference(@NotNull final UUID systemId);
}
