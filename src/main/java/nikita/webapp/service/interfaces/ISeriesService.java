package nikita.webapp.service.interfaces;


import nikita.common.model.noark5.v5.ClassificationSystem;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.hateoas.*;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.ScreeningMetadataHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.StorageLocationHateoas;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.model.noark5.v5.secondary.StorageLocation;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface ISeriesService {

    // -- All CREATE operations
    SeriesHateoas save(@NotNull final Series series);

    StorageLocationHateoas createStorageLocationAssociatedWithSeries(
            @NotNull final UUID systemId,
            @NotNull final StorageLocation storageLocation);

    FileHateoas createFileAssociatedWithSeries(
            @NotNull final UUID systemId,
            @NotNull final File file);

    CaseFileHateoas createCaseFileAssociatedWithSeries(
            @NotNull final UUID systemId,
            @NotNull final CaseFile caseFile);

    ClassificationSystemHateoas createClassificationSystem(
            @NotNull final UUID systemId,
            @NotNull final ClassificationSystem classificationSystem);

    void checkSeriesStatusUponCreation(@NotNull final Series series);

    // -- All READ operations
    SeriesHateoas findAll();

    CaseFileHateoas
    findCaseFilesBySeries(
            @NotNull final UUID systemId);

    // systemId
    SeriesHateoas findBySystemId(
            @NotNull final UUID systemId);

    RecordHateoas findAllRecordAssociatedWithSeries(
            @NotNull final UUID systemId);

    FileHateoas findAllFileAssociatedWithSeries(
            @NotNull final UUID systemId);

    ClassificationSystemHateoas
    findClassificationSystemAssociatedWithSeries(
            @NotNull final UUID systemId);

    FondsHateoas findFondsAssociatedWithSeries(
            @NotNull final UUID systemId);

    StorageLocationHateoas findStorageLocationAssociatedWithSeries(
            @NotNull final UUID systemId);

    // All UPDATE operations
    SeriesHateoas handleUpdate(@NotNull final UUID systemId,
                               @NotNull final Long version,
                               @NotNull final Series incomingSeries);

    void updateSeriesReferences(@NotNull final Series series);

    // All DELETE operations
    void deleteEntity(@NotNull final UUID systemId);

    void deleteAllByOwnedBy();

    ScreeningMetadataHateoas getScreeningMetadataAssociatedWithSeries(
            @NotNull final UUID systemId);

    ScreeningMetadataHateoas createScreeningMetadataAssociatedWithSeries(
            @NotNull final UUID systemId,
            @NotNull final Metadata screeningMetadata);

    ScreeningMetadataHateoas getDefaultScreeningMetadata(
            @NotNull final UUID systemId);

    StorageLocationHateoas getDefaultStorageLocation(
            @NotNull final UUID systemId);

    SeriesHateoas generateDefaultSeries(@NotNull final UUID systemId);

}
