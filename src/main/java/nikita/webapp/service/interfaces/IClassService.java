package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.hateoas.ClassHateoas;
import nikita.common.model.noark5.v5.hateoas.ClassificationSystemHateoas;
import nikita.common.model.noark5.v5.hateoas.FileHateoas;
import nikita.common.model.noark5.v5.hateoas.RecordHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.CrossReferenceHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.KeywordHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.ScreeningMetadataHateoas;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.model.noark5.v5.secondary.CrossReference;
import nikita.common.model.noark5.v5.secondary.Keyword;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IClassService {

	// -- All CREATE operations
    ClassHateoas save(@NotNull final Class klass);

    ClassHateoas createClassAssociatedWithClass(
            @NotNull final UUID systemId, @NotNull final Class klass);

    FileHateoas createFileAssociatedWithClass(
            @NotNull final UUID systemId, @NotNull final File file);

    CaseFileHateoas createCaseFileAssociatedWithClass(
            @NotNull final UUID systemId, @NotNull final CaseFile caseFile);

    ScreeningMetadataHateoas createScreeningMetadataAssociatedWithClass(
            @NotNull final UUID systemId,
            @NotNull final Metadata screeningMetadata);

    RecordHateoas createRecordAssociatedWithClass(
            @NotNull final UUID systemId, @NotNull final Record record);

    CrossReferenceHateoas createCrossReferenceAssociatedWithClass(
            @NotNull final UUID systemId,
            @NotNull final CrossReference crossReference);

    KeywordHateoas createKeywordAssociatedWithClass(
            @NotNull final UUID systemId, @NotNull final Keyword keyword);

    ClassHateoas generateDefaultSubClass(@NotNull final UUID systemId);

    ClassHateoas generateDefaultClass(@NotNull final UUID systemId);

    // -- All READ operations
    ClassHateoas findAll();

    ClassHateoas findSingleClass(@NotNull final UUID systemId);

    ClassHateoas findAllChildren(@NotNull final UUID systemId);

    ScreeningMetadataHateoas getScreeningMetadataAssociatedWithClass(
            @NotNull final UUID systemId);

    ClassHateoas findClassAssociatedWithClass(@NotNull final UUID systemId);

    ClassificationSystemHateoas findClassificationSystemAssociatedWithClass(
            @NotNull final UUID systemId);

    FileHateoas findAllFileAssociatedWithClass(
            @NotNull final UUID systemId);

    RecordHateoas findAllRecordAssociatedWithClass(
            @NotNull final UUID systemId);

    // All UPDATE operations
    ClassHateoas handleUpdate(@NotNull final UUID systemId,
                              @NotNull final Long version,
                              @NotNull final Class klass);

    // All DELETE operations

    void deleteEntity(@NotNull final UUID systemId);

    void deleteAllByOwnedBy();

    // All TEMPLATE operations

    ScreeningMetadataHateoas getDefaultScreeningMetadata(
            @NotNull final UUID systemId);

    KeywordHateoas generateDefaultKeyword(@NotNull final UUID systemId);

    CrossReferenceHateoas getDefaultCrossReference(@NotNull final UUID systemId);
}
