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
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public interface IRecordService {

    // -- All CREATE operations
    ResponseEntity<RecordHateoas> save(Record record);

    DocumentDescriptionHateoas createDocumentDescriptionAssociatedWithRecord(
            String systemId, DocumentDescription documentDescription);

    CommentHateoas createCommentAssociatedWithRecord
            (String systemId, Comment comment);

    StorageLocationHateoas createStorageLocationAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final StorageLocation storageLocation);

    CrossReferenceHateoas createCrossReferenceAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull @NotNull CrossReference crossReference);

    RecordHateoas generateDefaultRecord();

    CommentHateoas generateDefaultComment();

    CorrespondencePartInternalHateoas generateDefaultCorrespondencePartInternal(
            String recordSystemId);

    CorrespondencePartPersonHateoas generateDefaultCorrespondencePartPerson(
            String recordSystemId);

    CorrespondencePartUnitHateoas generateDefaultCorrespondencePartUnit(
            String recordSystemId);

    CommentHateoas getCommentAssociatedWithRecord
            (@NotNull final String systemId);

    CorrespondencePartHateoas getCorrespondencePartAssociatedWithRecord(
            final String systemId);

    PartHateoas getPartAssociatedWithRecord(final String systemId);

    NationalIdentifierHateoas getNationalIdentifierAssociatedWithRecord
            (@NotNull final String systemId);

    PartPersonHateoas generateDefaultPartPerson(
            String recordSystemId);

    PartUnitHateoas generateDefaultPartUnit(
            String recordSystemId);

    CorrespondencePartPersonHateoas
    createCorrespondencePartPersonAssociatedWithRecord(
            String systemId, CorrespondencePartPerson correspondencePart);

    CorrespondencePartUnitHateoas
    createCorrespondencePartUnitAssociatedWithRecord(
            String systemId, CorrespondencePartUnit correspondencePart);

    CorrespondencePartInternalHateoas
    createCorrespondencePartInternalAssociatedWithRecord(
            String systemId, CorrespondencePartInternal correspondencePart);

    PartPersonHateoas createPartPersonAssociatedWithRecord(
            String systemId, PartPerson partPerson);

    PartUnitHateoas createPartUnitAssociatedWithRecord(
            String systemId, PartUnit partUnit);

    BuildingHateoas createBuildingAssociatedWithRecord
            (@NotNull String systemId, @NotNull Building building);

    CadastralUnitHateoas createCadastralUnitAssociatedWithRecord
            (@NotNull String systemId, @NotNull CadastralUnit cadastralUnit);

    DNumberHateoas createDNumberAssociatedWithRecord
            (@NotNull String systemId, @NotNull DNumber dNumber);

    PlanHateoas createPlanAssociatedWithRecord
            (@NotNull String systemId, @NotNull Plan plan);

    PositionHateoas createPositionAssociatedWithRecord
            (@NotNull String systemId, @NotNull Position position);

    SocialSecurityNumberHateoas createSocialSecurityNumberAssociatedWithRecord
            (@NotNull String systemId,
             @NotNull SocialSecurityNumber socialSecurityNumber);

    UnitHateoas createUnitAssociatedWithRecord
            (@NotNull String systemId, @NotNull Unit unit);

    KeywordHateoas createKeywordAssociatedWithRecord(
            @NotNull final UUID systemId, @NotNull final Keyword keyword);

    // -- All READ operations
    List<Record> findAll();

    ResponseEntity<RecordHateoas>
    findByReferenceDocumentDescription(@NotNull final String systemId);

    ResponseEntity<FileHateoas>
    findFileAssociatedWithRecord(@NotNull final String systemId);

    ResponseEntity<ClassHateoas>
    findClassAssociatedWithRecord(@NotNull final String systemId);

    ResponseEntity<SeriesHateoas>
    findSeriesAssociatedWithRecord(@NotNull final String systemId);

    Record findBySystemId(String systemId);

    List<Record> findByOwnedBy();

    AuthorHateoas associateAuthorWithRecord(String systemId, Author author);

    // -- All UPDATE operations
    Record handleUpdate(@NotNull String systemId,
                        @NotNull Long version, @NotNull Record record);

    // -- All DELETE operations
    void deleteRecord(@NotNull UUID systemId);

    long deleteAllByOwnedBy();

    AuthorHateoas findAllAuthorWithRecordBySystemId(String systemId);

    AuthorHateoas generateDefaultAuthor(String systemId);

    BuildingHateoas generateDefaultBuilding();

    CadastralUnitHateoas generateDefaultCadastralUnit();

    DNumberHateoas generateDefaultDNumber();

    PlanHateoas generateDefaultPlan();

    PositionHateoas generateDefaultPosition();

    SocialSecurityNumberHateoas generateDefaultSocialSecurityNumber();

    UnitHateoas generateDefaultUnit();

    ResponseEntity<RecordHateoas> handleUpdate(
            UUID systemId, PatchObjects patchObjects);

    ScreeningMetadataHateoas createScreeningMetadataAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final Metadata screeningMetadata);

    ScreeningMetadataHateoas getScreeningMetadataAssociatedWithRecord(
            @NotNull final UUID systemId);

    ScreeningMetadataHateoas getDefaultScreeningMetadata(
            @NotNull final UUID systemId);

    KeywordHateoas generateDefaultKeyword();

    StorageLocationHateoas getDefaultStorageLocation(
            @NotNull final UUID systemID);

    CrossReferenceHateoas getDefaultCrossReference();
}
