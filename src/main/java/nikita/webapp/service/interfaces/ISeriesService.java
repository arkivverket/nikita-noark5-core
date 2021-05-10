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
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface ISeriesService {


    // -- All CREATE operations
    Series save(Series series);

    StorageLocationHateoas createStorageLocationAssociatedWithSeries(
            @NotNull final UUID seriesSystemId,
            @NotNull final StorageLocation storageLocation);

    File createFileAssociatedWithSeries(String seriesSystemId, File file);

    CaseFile createCaseFileAssociatedWithSeries(String seriesSystemId,
                                                CaseFile caseFile);

    ClassificationSystemHateoas createClassificationSystem(
            String systemId,
            ClassificationSystem classificationSystem);

    void checkSeriesStatusUponCreation(Series series);

    // -- All READ operations
    ResponseEntity<SeriesHateoas> findAll();

    ResponseEntity<CaseFileHateoas>
    findCaseFilesBySeries(@NotNull String systemId);

    // systemId
    ResponseEntity<SeriesHateoas> findBySystemId(@NotNull String systemId);

    ResponseEntity<RecordHateoas> findAllRecordAssociatedWithSeries(
            String systemId);

    ResponseEntity<FileHateoas> findAllFileAssociatedWithSeries(
            String systemId);

    ResponseEntity<ClassificationSystemHateoas>
    findClassificationSystemAssociatedWithSeries(String systemId);

    ResponseEntity<FondsHateoas> findFondsAssociatedWithSeries(
            @NotNull final String systemId);

    // All UPDATE operations
    Series handleUpdate(@NotNull final String systemId,
                        @NotNull final Long version,
                        @NotNull final Series incomingSeries);

    void updateSeriesReferences(Series series);

    // All DELETE operations
    void deleteEntity(@NotNull String systemId);

    long deleteAllByOwnedBy();

    ScreeningMetadataHateoas getScreeningMetadataAssociatedWithSeries(
            @NotNull final UUID systemId);

    ScreeningMetadataHateoas createScreeningMetadataAssociatedWithSeries(
            @NotNull final UUID systemId,
            @NotNull final Metadata screeningMetadata);

    ScreeningMetadataHateoas getDefaultScreeningMetadata(
            @NotNull final UUID systemID);

    StorageLocationHateoas getDefaultStorageLocation(
            @NotNull final UUID systemID);
}
