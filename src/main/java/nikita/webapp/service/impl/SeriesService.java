package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.ClassificationSystem;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.hateoas.*;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.SeriesStatus;
import nikita.common.repository.n5v5.ISeriesRepository;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.common.util.exceptions.NoarkEntityEditWhenClosedException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.*;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.ICaseFileService;
import nikita.webapp.service.interfaces.IClassificationSystemService;
import nikita.webapp.service.interfaces.IFileService;
import nikita.webapp.service.interfaces.ISeriesService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_ASSOCIATE_WITH_CLOSED_OBJECT;
import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.N5ResourceMappings.SERIES_STATUS;
import static nikita.common.config.N5ResourceMappings.SERIES_STATUS_CLOSED_CODE;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.*;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
@SuppressWarnings("unchecked")
public class SeriesService
        extends NoarkService
        implements ISeriesService {

    private static final Logger logger =
            LoggerFactory.getLogger(SeriesService.class);

    private IMetadataService metadataService;
    private IFileService fileService;
    private ICaseFileService caseFileService;
    private IClassificationSystemService classificationSystemService;
    private ISeriesRepository seriesRepository;
    private ISeriesHateoasHandler seriesHateoasHandler;
    private IRecordHateoasHandler recordHateoasHandler;
    private IFileHateoasHandler fileHateoasHandler;
    private IFondsHateoasHandler fondsHateoasHandler;
    private IClassificationSystemHateoasHandler
            classificationSystemHateoasHandler;

    public SeriesService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IClassificationSystemService classificationSystemService,
            IFileService fileService,
            ICaseFileService caseFileService,
            IMetadataService metadataService,
            ISeriesRepository seriesRepository,
            ISeriesHateoasHandler seriesHateoasHandler,
            IRecordHateoasHandler recordHateoasHandler,
            IFileHateoasHandler fileHateoasHandler,
            IFondsHateoasHandler fondsHateoasHandler,
            IClassificationSystemHateoasHandler
                    classificationSystemHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.metadataService = metadataService;
        this.fileService = fileService;
        this.caseFileService = caseFileService;
        this.metadataService = metadataService;
        this.seriesRepository = seriesRepository;
        this.classificationSystemService = classificationSystemService;
        this.seriesHateoasHandler = seriesHateoasHandler;
        this.recordHateoasHandler = recordHateoasHandler;
        this.fileHateoasHandler = fileHateoasHandler;
        this.fondsHateoasHandler = fondsHateoasHandler;
        this.classificationSystemHateoasHandler =
                classificationSystemHateoasHandler;
    }

    // All CREATE operations
    @Override
    public CaseFile createCaseFileAssociatedWithSeries(String seriesSystemId,
                                                       CaseFile caseFile) {
        Series series = getSeriesOrThrow(seriesSystemId);
        checkOpenOrThrow(series);
        caseFile.setReferenceSeries(series);
        return caseFileService.save(caseFile);
    }

    @Override
    public File createFileAssociatedWithSeries(String seriesSystemId,
                                               File file) {
        Series series = getSeriesOrThrow(seriesSystemId);
        checkOpenOrThrow(series);
        file.setReferenceSeries(series);
        return fileService.createFile(file);
    }

    @Override
    public Series save(Series series) {
        validateDocumentMedium(metadataService, series);
        validateDeletion(series.getReferenceDeletion());
        if (null == series.getSeriesStatus()) {
            checkSeriesStatusUponCreation(series);
        }
        return seriesRepository.save(series);
    }

    @Override
    public ClassificationSystemHateoas createClassificationSystem(
            String systemId, ClassificationSystem classificationSystem) {
        Series series = getSeriesOrThrow(systemId);
        series.addClassificationSystem(classificationSystem);
        return classificationSystemService.save(classificationSystem);
    }

    // All READ operations
    @Override
    public ResponseEntity<SeriesHateoas> findAll() {
        SeriesHateoas seriesHateoas = new
                SeriesHateoas((List<INoarkEntity>) (List)
                seriesRepository.findByOwnedBy(getUser()));
        seriesHateoasHandler.addLinksOnRead(seriesHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .body(seriesHateoas);
    }

    @Override
    public ResponseEntity<CaseFileHateoas> findCaseFilesBySeries(
            @NotNull String systemId) {
        return caseFileService.findAllCaseFileBySeries(
                getSeriesOrThrow(systemId));
    }

    @Override
    public ResponseEntity<RecordHateoas> findAllRecordAssociatedWithSeries(
            String systemId) {
        RecordHateoas recordHateoas = new RecordHateoas(
                (List<INoarkEntity>) (List)
                        getSeriesOrThrow(systemId).getReferenceRecord());
        recordHateoasHandler.addLinks(recordHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .body(recordHateoas);
    }

    @Override
    public ResponseEntity<FileHateoas> findAllFileAssociatedWithSeries(
            String systemId) {
        FileHateoas fileHateoas = new FileHateoas(
                (List<INoarkEntity>) (List)
                        getSeriesOrThrow(systemId).getReferenceFile());
        fileHateoasHandler.addLinks(fileHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .body(fileHateoas);
    }

    /**
     * Retrieve the list of ClassificationSystemHateoas object associated with
     * the Series object identified by systemId
     *
     * @param systemId The systemId of the Series object to retrieve the
     *                 associated ClassificationSystemHateoas
     * @return A ClassificationSystemHateoas list packed as a ResponseEntity
     */
    @Override
    public ResponseEntity<ClassificationSystemHateoas>
    findClassificationSystemAssociatedWithSeries(
            @NotNull final String systemId) {
        ClassificationSystemHateoas classificationSystemHateoas =
                new ClassificationSystemHateoas(
                        (List<INoarkEntity>) (List)
                                getSeriesOrThrow(systemId).
                                        getReferenceClassificationSystem());
        classificationSystemHateoasHandler.addLinks(classificationSystemHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .body(classificationSystemHateoas);
    }

    /**
     * Retrieve the list of FondsHateoas object associated with
     * the Series object identified by systemId
     *
     * @param systemId The systemId of the Series object to retrieve the
     *                 associated FondsHateoas
     * @return A FondsHateoas list packed as a ResponseEntity
     */
    @Override
    public ResponseEntity<FondsHateoas> findFondsAssociatedWithSeries(
            @NotNull final String systemId) {
        FondsHateoas fondsHateoas =
                new FondsHateoas(
                        getSeriesOrThrow(systemId).
                                getReferenceFonds());
        fondsHateoasHandler.addLinks(fondsHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(fondsHateoas.getEntityVersion().toString())
                .body(fondsHateoas);
    }

    // systemId
    @Override
    public ResponseEntity<SeriesHateoas> findBySystemId(String systemId) {
        SeriesHateoas seriesHateoas =
                new SeriesHateoas(getSeriesOrThrow(systemId));
        seriesHateoasHandler.addLinks(seriesHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(seriesHateoas.getEntityVersion().toString())
                .body(seriesHateoas);
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
    public Series handleUpdate(@NotNull final String systemId,
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

        existingSeries.setReferencePrecursorSystemID
            (incomingSeries.getReferencePrecursorSystemID());
        existingSeries.setReferenceSuccessorSystemID
            (incomingSeries.getReferenceSuccessorSystemID());
        updateSeriesReferences(existingSeries);

        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingSeries.setVersion(version);
        seriesRepository.save(existingSeries);
        return existingSeries;
    }

    @Override
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
                    .setReferenceSuccessorSystemID(series.getId());
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
                    .setReferencePrecursorSystemID(series.getId());
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
    public void deleteEntity(@NotNull String seriesSystemId) {
        deleteEntity(getSeriesOrThrow(seriesSystemId));
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     *
     * @return the number of objects deleted
     */
    @Override
    public long deleteAllByOwnedBy() {
        long count = seriesRepository.count();
        seriesRepository.deleteAll();
        logger.info("Deleted [" + count + "] Series objects");
        return count;
    }

    // Helper methods

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
                    ". Series with systemId " + series.getSystemId() +
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
     * @param seriesSystemId systemId of the series object to retrieve
     * @return the Series object
     */
    private Series getSeriesOrThrow(@NotNull String seriesSystemId) {
        Series series = seriesRepository.
                findBySystemId(UUID.fromString(seriesSystemId));
        if (series == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " Series, using systemId "
                    + seriesSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
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
                    .findValidMetadataByEntityTypeOrThrow(
                            SERIES_STATUS,
                            series.getSeriesStatus());
            series.setSeriesStatus(seriesStatus);
        }
    }
}
