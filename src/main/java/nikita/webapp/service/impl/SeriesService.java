package nikita.webapp.service.impl;

import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.ClassificationSystem;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.hateoas.*;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.ScreeningMetadataHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.StorageLocationHateoas;
import nikita.common.model.noark5.v5.metadata.DocumentMedium;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.model.noark5.v5.metadata.SeriesStatus;
import nikita.common.model.noark5.v5.secondary.Screening;
import nikita.common.model.noark5.v5.secondary.StorageLocation;
import nikita.common.repository.n5v5.ISeriesRepository;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.common.util.exceptions.NoarkEntityEditWhenClosedException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.ISeriesHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IScreeningMetadataHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.interfaces.ICaseFileService;
import nikita.webapp.service.interfaces.IClassificationSystemService;
import nikita.webapp.service.interfaces.IFileService;
import nikita.webapp.service.interfaces.ISeriesService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.odata.IODataService;
import nikita.webapp.service.interfaces.secondary.IScreeningMetadataService;
import nikita.webapp.service.interfaces.secondary.IStorageLocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static java.util.List.copyOf;
import static nikita.common.config.Constants.INFO_CANNOT_ASSOCIATE_WITH_CLOSED_OBJECT;
import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.*;

@Service
public class SeriesService
        extends NoarkService
        implements ISeriesService {

    private static final Logger logger =
            LoggerFactory.getLogger(SeriesService.class);

    private final IMetadataService metadataService;
    private final IFileService fileService;
    private final ICaseFileService caseFileService;
    private final IClassificationSystemService classificationSystemService;
    private final ISeriesRepository seriesRepository;
    private final ISeriesHateoasHandler seriesHateoasHandler;
    private final IScreeningMetadataService screeningMetadataService;
    private final IStorageLocationService storageLocationService;
    private final IScreeningMetadataHateoasHandler screeningMetadataHateoasHandler;

    public SeriesService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IODataService odataService,
            IPatchService patchService,
            IClassificationSystemService classificationSystemService,
            IFileService fileService,
            ICaseFileService caseFileService,
            IMetadataService metadataService,
            ISeriesRepository seriesRepository,
            ISeriesHateoasHandler seriesHateoasHandler,
            IScreeningMetadataService screeningMetadataService,
            IStorageLocationService storageLocationService,
            IScreeningMetadataHateoasHandler screeningMetadataHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.metadataService = metadataService;
        this.fileService = fileService;
        this.caseFileService = caseFileService;
        this.seriesRepository = seriesRepository;
        this.classificationSystemService = classificationSystemService;
        this.seriesHateoasHandler = seriesHateoasHandler;
        this.screeningMetadataService = screeningMetadataService;
        this.storageLocationService = storageLocationService;
        this.screeningMetadataHateoasHandler = screeningMetadataHateoasHandler;
    }

    // All CREATE operations
    @Override
    @Transactional
    public CaseFileHateoas createCaseFileAssociatedWithSeries(
            @NotNull final UUID systemId,
            @NotNull final CaseFile caseFile) {
        Series series = getSeriesOrThrow(systemId);
        checkOpenOrThrow(series);
        caseFile.setReferenceSeries(series);
        return caseFileService.save(caseFile);
    }

    @Override
    @Transactional
    public StorageLocationHateoas createStorageLocationAssociatedWithSeries(
            UUID systemId, StorageLocation storageLocation) {
        Series series = getSeriesOrThrow(systemId);
        checkOpenOrThrow(series);
        return storageLocationService
                .createStorageLocationAssociatedWithSeries(
                        storageLocation, series);
    }

    @Override
    @Transactional
    public FileHateoas createFileAssociatedWithSeries(
            @NotNull final UUID systemId,
            @NotNull final File file) {
        Series series = getSeriesOrThrow(systemId);
        checkOpenOrThrow(series);
        file.setReferenceSeries(series);
        return fileService.createFile(file);
    }

    @Override
    @Transactional
    public SeriesHateoas save(Series series) {
        validateDocumentMedium(metadataService, series);
        validateDeletion(series.getReferenceDeletion());
        validateScreening(metadataService, series);
        if (null == series.getSeriesStatus()) {
            checkSeriesStatusUponCreation(series);
        }
        return packAsHateoas(seriesRepository.save(series));
    }

    @Override
    @Transactional
    public ClassificationSystemHateoas createClassificationSystem(
            UUID systemId, ClassificationSystem classificationSystem) {
        Series series = getSeriesOrThrow(systemId);
        series.addClassificationSystem(classificationSystem);
        return classificationSystemService.save(classificationSystem);
    }

    @Override
    public ScreeningMetadataHateoas createScreeningMetadataAssociatedWithSeries(
            UUID systemId, Metadata screeningMetadata) {
        Series series = getSeriesOrThrow(systemId);
        if (null == series.getReferenceScreening()) {
            throw new NoarkEntityNotFoundException(INFO_CANNOT_FIND_OBJECT +
                    " Screening, associated with Series with systemId " +
                    systemId);
        }
        return screeningMetadataService.createScreeningMetadata(
                series.getReferenceScreening(), screeningMetadata);
    }

    // All READ operations
    @Override
    public SeriesHateoas findAll() {
        return (SeriesHateoas) odataService.processODataQueryGet();
    }

    @Override
    public CaseFileHateoas findCaseFilesBySeries(
            @NotNull final UUID systemId) {
        // Make sure Series exists
        getSeriesOrThrow(systemId);
        return (CaseFileHateoas) odataService.processODataQueryGet();
    }

    @Override
    public RecordHateoas findAllRecordAssociatedWithSeries(
            @NotNull final UUID systemId) {
        // Make sure Series exists
        getSeriesOrThrow(systemId);
        return (RecordHateoas) odataService.processODataQueryGet();
    }

    @Override
    public FileHateoas findAllFileAssociatedWithSeries(
            @NotNull final UUID systemId) {
        // Make sure Series exists
        getSeriesOrThrow(systemId);
        return (FileHateoas) odataService.processODataQueryGet();
    }

    /**
     * Retrieve the list of ClassificationSystemHateoas object associated with
     * the Series object identified by systemId
     *
     * @param systemId The systemId of the Series object to retrieve the
     *                 associated ClassificationSystemHateoas
     * @return A ClassificationSystemHateoas list
     */
    @Override
    public ClassificationSystemHateoas
    findClassificationSystemAssociatedWithSeries(
            @NotNull final UUID systemId) {
        // Make sure Series exists
        getSeriesOrThrow(systemId);
        return (ClassificationSystemHateoas) odataService.processODataQueryGet();
    }

    /**
     * Retrieve the list of FondsHateoas object associated with
     * the Series object identified by systemId
     *
     * @param systemId The systemId of the Series object to retrieve the
     *                 associated FondsHateoas
     * @return A FondsHateoas list
     */
    @Override
    public FondsHateoas findFondsAssociatedWithSeries(
            @NotNull final UUID systemId) {
        getSeriesOrThrow(systemId);
        return (FondsHateoas) odataService.processODataQueryGet();
    }

    // systemId
    @Override
    public SeriesHateoas findBySystemId(@NotNull final UUID systemId) {
        return packAsHateoas(getSeriesOrThrow(systemId));
    }


    @Override
    public ScreeningMetadataHateoas
    getScreeningMetadataAssociatedWithSeries(@NotNull final UUID systemId) {
        Screening screening = getSeriesOrThrow(systemId)
                .getReferenceScreening();
        if (null == screening) {
            throw new NoarkEntityNotFoundException(
                    INFO_CANNOT_FIND_OBJECT + " Screening, using systemId " +
                            systemId);
        }
        return packAsHateoas(new NikitaPage(copyOf(
                screening.getReferenceScreeningMetadata())));
    }

    // All UPDATE operations

    /**
     * Update an existing Series object.
     * <p>
     * Note: title is not nullable
     *
     * @param systemId       systemId of Series to update
     * @param version        ETAG value
     * @param incomingSeries the incoming series
     * @return the updated series object after it is persisted
     */
    @Override
    @Transactional
    public SeriesHateoas handleUpdate(@NotNull final UUID systemId,
                                      @NotNull final Long version,
                                      @NotNull final Series incomingSeries) {
        Series existingSeries = getSeriesOrThrow(systemId);
        // Here copy all the values you are allowed to copy ....
        updateDeletion(incomingSeries, existingSeries);
        updateTitleAndDescription(incomingSeries, existingSeries);
        if (null != incomingSeries.getDocumentMedium()) {
            existingSeries.setDocumentMedium(
                    incomingSeries.getDocumentMedium());
        }
        existingSeries.setSeriesStatus(incomingSeries.getSeriesStatus());

        existingSeries.setReferencePrecursorSystemID
                (incomingSeries.getReferencePrecursorSystemID());
        existingSeries.setReferenceSuccessorSystemID
                (incomingSeries.getReferenceSuccessorSystemID());
        updateSeriesReferences(existingSeries);

        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingSeries.setVersion(version);
        return packAsHateoas(existingSeries);
    }

    @Override
    @Transactional
    public void updateSeriesReferences(Series series) {
        if (null != series.getReferencePrecursorSystemID()) {
            Series referenceSeries = seriesRepository.
                    findBySystemId(series.getReferencePrecursorSystemID());
            if (null != referenceSeries) {
                if (null != referenceSeries.getReferenceSuccessorSystemID()) {
                    String info = "not allowed to set precursor to series with existing successor";
                    throw new NikitaMalformedInputDataException(info);
                }
                referenceSeries
                        .setReferenceSuccessorSystemID(series.getSystemId());
                referenceSeries.setReferenceSuccessor(series);
            }
            // Will set reference to null if series with SystemID not found
            series.setReferencePrecursor(referenceSeries);
        } else {
            series.setReferencePrecursor(null);
        }

        if (null != series.getReferenceSuccessorSystemID()) {
            Series referenceSeries = seriesRepository.
                    findBySystemId(series.getReferenceSuccessorSystemID());
            if (null != referenceSeries) {
                if (null != referenceSeries.getReferencePrecursorSystemID()) {
                    String info = "not allowed to set successor to series with existing precursor";
                    throw new NikitaMalformedInputDataException(info);
                }
                referenceSeries
                        .setReferencePrecursorSystemID(series.getSystemId());
                referenceSeries.setReferencePrecursor(series);
            }
            // Will set reference to null if series with SystemID not found
            series.setReferenceSuccessor(referenceSeries);
        } else {
            series.setReferenceSuccessor(null);
        }
    }

    // All DELETE operations
    @Override
    @Transactional
    public void deleteEntity(@NotNull final UUID systemId) {
        deleteEntity(getSeriesOrThrow(systemId));
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     */
    @Override
    @Transactional
    public void deleteAllByOwnedBy() {
        seriesRepository.deleteByOwnedBy(getUser());
    }

    public SeriesHateoas generateDefaultSeries(@NotNull final UUID systemId) {
        Series defaultSeries = new Series();
        SeriesStatus seriesStatus = (SeriesStatus)
                metadataService.findValidMetadataByEntityTypeOrThrow
                        (SERIES_STATUS, SERIES_STATUS_ACTIVE_CODE, null);
        defaultSeries.setSeriesStatus(seriesStatus);
        DocumentMedium documentMedium = (DocumentMedium)
                metadataService.findValidMetadataByEntityTypeOrThrow
                        (DOCUMENT_MEDIUM, DOCUMENT_MEDIUM_ELECTRONIC_CODE, null);
        defaultSeries.setDocumentMedium(documentMedium);
        defaultSeries.setVersion(-1L, true);
        return packAsHateoas(defaultSeries);
    }

    @Override
    public ScreeningMetadataHateoas getDefaultScreeningMetadata(
            @NotNull final UUID systemId) {
        return screeningMetadataService.getDefaultScreeningMetadata(systemId);
    }

    @Override
    public StorageLocationHateoas getDefaultStorageLocation(
            @NotNull final UUID systemId) {
        return storageLocationService.getDefaultStorageLocation(systemId);
    }

    // Helper methods

    public ScreeningMetadataHateoas packAsHateoas(NikitaPage page) {
        ScreeningMetadataHateoas screeningMetadataHateoas =
                new ScreeningMetadataHateoas(page);
        applyLinksAndHeader(screeningMetadataHateoas,
                screeningMetadataHateoasHandler);
        return screeningMetadataHateoas;
    }

    public SeriesHateoas packAsHateoas(@NotNull final Series series) {
        SeriesHateoas seriesHateoas = new SeriesHateoas(series);
        applyLinksAndHeader(seriesHateoas, seriesHateoasHandler);
        return seriesHateoas;
    }

    /**
     * Internal helper method. Check that the status of the CaseFile is set to
     * open or throw an exception.
     *
     * @param series The series object to check if it open
     */
    private void checkOpenOrThrow(@NotNull Series series) {
        if (null != series.getSeriesStatus() &&
                SERIES_STATUS_CLOSED_CODE.equals(series.getSeriesStatus().getCode())) {
            String info = INFO_CANNOT_ASSOCIATE_WITH_CLOSED_OBJECT +
                    ". Series with systemId " + series.getSystemIdAsString() +
                    " has status code " + SERIES_STATUS_CLOSED_CODE;
            logger.info(info);
            throw new NoarkEntityEditWhenClosedException(info);
        }
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid Series back. If there is no valid
     * Series, an exception is thrown
     *
     * @param systemId systemId of the series object to retrieve
     * @return the Series object
     */
    private Series getSeriesOrThrow(@NotNull final UUID systemId) {
        Series series = seriesRepository.
                findBySystemId(systemId);
        if (series == null) {
            throw new NoarkEntityNotFoundException(
                    INFO_CANNOT_FIND_OBJECT + " Series, using systemId "
                            + systemId);
        }
        return series;
    }

    /**
     * Internal helper method. Verify the SeriesStatus code provided is
     * in the metadata catalog, and copy the code name associated with
     * the code from the metadata catalog into the Series.
     * <p>
     * If the SeriesStatus code is unknown, a
     * NoarkEntityNotFoundException exception is thrown that the
     * caller has to deal with.  If the code and code name provided do
     * not match the entries in the metadata catalog, a
     * NoarkInvalidStructureException exception is thrown.
     *
     * @param series The series object
     */
    public void checkSeriesStatusUponCreation(Series series) {
        if (series.getSeriesStatus() != null) {
            SeriesStatus seriesStatus = (SeriesStatus) metadataService
                    .findValidMetadata(series.getSeriesStatus());
            series.setSeriesStatus(seriesStatus);
        }
    }
}
