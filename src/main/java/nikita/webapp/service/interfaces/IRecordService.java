package nikita.webapp.service.interfaces;

import nikita.common.model.nikita.PatchObjects;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartInternal;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit;
import nikita.common.model.noark5.v5.hateoas.*;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartInternalHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartPersonHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartUnitHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.*;
import nikita.common.model.noark5.v5.hateoas.secondary.*;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.model.noark5.v5.nationalidentifier.*;
import nikita.common.model.noark5.v5.secondary.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IRecordService {

    // -- All CREATE operations
    RecordHateoas save(@NotNull final Record record);

    DocumentDescriptionHateoas createDocumentDescriptionAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final DocumentDescription documentDescription);

    CommentHateoas createCommentAssociatedWithRecord
            (@NotNull final UUID systemId, Comment comment);

    StorageLocationHateoas createStorageLocationAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final StorageLocation storageLocation);

    CrossReferenceHateoas createCrossReferenceAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final CrossReference crossReference);

    RecordHateoas generateDefaultRecord(@NotNull final UUID systemId);

    CommentHateoas generateDefaultComment(@NotNull final UUID systemId);

    CorrespondencePartInternalHateoas generateDefaultCorrespondencePartInternal(
            @NotNull final UUID systemId);

    CorrespondencePartPersonHateoas generateDefaultCorrespondencePartPerson(
            @NotNull final UUID systemId);

    CorrespondencePartUnitHateoas generateDefaultCorrespondencePartUnit(
            @NotNull final UUID systemId);

    CommentHateoas getCommentAssociatedWithRecord(@NotNull final UUID systemId);

    DocumentDescriptionHateoas getDocumentDescriptionAssociatedWithRecord(
            @NotNull final UUID systemId);

    CorrespondencePartHateoas getCorrespondencePartAssociatedWithRecord(
            @NotNull final UUID systemId);

    PartHateoas getPartAssociatedWithRecord(@NotNull final UUID systemId);

    NationalIdentifierHateoas getNationalIdentifierAssociatedWithRecord(
            @NotNull final UUID systemId);

    PartPersonHateoas generateDefaultPartPerson(@NotNull final UUID systemId);

    PartUnitHateoas generateDefaultPartUnit(@NotNull final UUID systemId);

    CorrespondencePartPersonHateoas
    createCorrespondencePartPersonAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final CorrespondencePartPerson correspondencePart);

    CorrespondencePartUnitHateoas
    createCorrespondencePartUnitAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final CorrespondencePartUnit correspondencePart);

    CorrespondencePartInternalHateoas
    createCorrespondencePartInternalAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final CorrespondencePartInternal correspondencePart);

    PartPersonHateoas createPartPersonAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final PartPerson partPerson);

    PartUnitHateoas createPartUnitAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final PartUnit partUnit);

    BuildingHateoas createBuildingAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final Building building);

    CadastralUnitHateoas createCadastralUnitAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final CadastralUnit cadastralUnit);

    DNumberHateoas createDNumberAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final DNumber dNumber);

    PlanHateoas createPlanAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final Plan plan);

    PositionHateoas createPositionAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final Position position);

    SocialSecurityNumberHateoas createSocialSecurityNumberAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final SocialSecurityNumber socialSecurityNumber);

    UnitHateoas createUnitAssociatedWithRecord(
            @NotNull final UUID systemId, @NotNull final Unit unit);

    KeywordHateoas createKeywordAssociatedWithRecord(
            @NotNull final UUID systemId, @NotNull final Keyword keyword);

    // -- All READ operations

    FileHateoas
    findFileAssociatedWithRecord(@NotNull final UUID systemId);

    ClassHateoas
    findClassAssociatedWithRecord(@NotNull final UUID systemId);

    SeriesHateoas
    findSeriesAssociatedWithRecord(@NotNull final UUID systemId);

    RecordHateoas findBySystemId(@NotNull final UUID systemId);

    RecordHateoas findAll();

    AuthorHateoas associateAuthorWithRecord(
            @NotNull final UUID systemId,
            @NotNull final Author author);

    // -- All UPDATE operations
    RecordHateoas handleUpdate(@NotNull final UUID systemId,
                               @NotNull final Long version,
                               @NotNull final Record record);

    // -- All DELETE operations
    void deleteRecord(@NotNull final UUID systemId);

    void deleteAllByOwnedBy();

    AuthorHateoas findAllAuthorWithRecordBySystemId(
            @NotNull final UUID systemId);

    AuthorHateoas generateDefaultAuthor(@NotNull final UUID systemId);

    BuildingHateoas generateDefaultBuilding(@NotNull final UUID systemId);

    CadastralUnitHateoas generateDefaultCadastralUnit(
            @NotNull final UUID systemId);

    DNumberHateoas generateDefaultDNumber(@NotNull final UUID systemId);

    PlanHateoas generateDefaultPlan(@NotNull final UUID systemId);

    PositionHateoas generateDefaultPosition(@NotNull final UUID systemId);

    SocialSecurityNumberHateoas generateDefaultSocialSecurityNumber(
            @NotNull final UUID systemId);

    UnitHateoas generateDefaultUnit(@NotNull final UUID systemId);

    RecordHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final PatchObjects patchObjects);

    ScreeningMetadataHateoas createScreeningMetadataAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final Metadata screeningMetadata);

    ScreeningMetadataHateoas getScreeningMetadataAssociatedWithRecord(
            @NotNull final UUID systemId);

    ScreeningMetadataHateoas getDefaultScreeningMetadata(
            @NotNull final UUID systemId);

    KeywordHateoas generateDefaultKeyword(@NotNull final UUID systemId);

    StorageLocationHateoas getDefaultStorageLocation(
            @NotNull final UUID systemId);

    CrossReferenceHateoas getDefaultCrossReference(@NotNull final UUID systemId);
}
