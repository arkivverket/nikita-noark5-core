package nikita.webapp.service.impl;

import nikita.common.model.noark5.v4.ClassificationSystem;
import nikita.common.model.noark5.v4.File;
import nikita.common.model.noark5.v4.Series;
import nikita.common.model.noark5.v4.casehandling.CaseFile;
import nikita.common.model.noark5.v4.hateoas.ClassificationSystemHateoas;
import nikita.common.model.noark5.v4.hateoas.FileHateoas;
import nikita.common.model.noark5.v4.hateoas.RecordHateoas;
import nikita.common.model.noark5.v4.hateoas.SeriesHateoas;
import nikita.common.model.noark5.v4.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.common.repository.n5v4.ISeriesRepository;
import nikita.common.util.exceptions.NoarkEntityEditWhenClosedException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IFileHateoasHandler;
import nikita.webapp.hateoas.interfaces.IRecordHateoasHandler;
import nikita.webapp.hateoas.interfaces.ISeriesHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.ICaseFileService;
import nikita.webapp.service.interfaces.IClassificationSystemService;
import nikita.webapp.service.interfaces.IFileService;
import nikita.webapp.service.interfaces.ISeriesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static nikita.common.config.Constants.INFO_CANNOT_ASSOCIATE_WITH_CLOSED_OBJECT;
import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.N5ResourceMappings.STATUS_CLOSED;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.checkDocumentMediumValid;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.setNoarkEntityValues;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
@SuppressWarnings("unchecked")
public class SeriesService
        extends NoarkService
        implements ISeriesService {

    private static final Logger logger =
            LoggerFactory.getLogger(SeriesService.class);

    private IFileService fileService;
    private ICaseFileService caseFileService;
    private IClassificationSystemService classificationSystemService;
    private ISeriesRepository seriesRepository;
    private ISeriesHateoasHandler seriesHateoasHandler;
    private IRecordHateoasHandler recordHateoasHandler;
    private IFileHateoasHandler fileHateoasHandler;

    public SeriesService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IClassificationSystemService classificationSystemService,
            IFileService fileService,
            ICaseFileService caseFileService,
            ISeriesRepository seriesRepository,
            ISeriesHateoasHandler seriesHateoasHandler,
            IRecordHateoasHandler recordHateoasHandler,
            IFileHateoasHandler fileHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.fileService = fileService;
        this.caseFileService = caseFileService;
        this.seriesRepository = seriesRepository;
        this.classificationSystemService = classificationSystemService;
        this.seriesHateoasHandler = seriesHateoasHandler;
        this.recordHateoasHandler = recordHateoasHandler;
        this.fileHateoasHandler = fileHateoasHandler;

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
        setNoarkEntityValues(series);
        checkDocumentMediumValid(series);
        return seriesRepository.save(series);
    }

    @Override
    public ClassificationSystemHateoas createClassificationSystem(
            String systemId, ClassificationSystem classificationSystem) {
        Series series = getSeriesOrThrow(systemId);
        series.setReferenceClassificationSystem(classificationSystem);
        return classificationSystemService.save(classificationSystem);
    }

    // All READ operations
    @Override
    public ResponseEntity<SeriesHateoas> findAll() {
        SeriesHateoas seriesHateoas = new
                SeriesHateoas((List<INikitaEntity>) (List)
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
                (List<INikitaEntity>) (List)
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
                (List<INikitaEntity>) (List)
                        getSeriesOrThrow(systemId).getReferenceFile());
        fileHateoasHandler.addLinks(fileHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .body(fileHateoas);
    }

    // id
    @Override
    public Optional<Series> findById(Long id) {
        return seriesRepository.findById(id);
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
        updateTitleAndDescription(incomingSeries, existingSeries);
        if (null != incomingSeries.getDocumentMedium()) {
            existingSeries.setDocumentMedium(incomingSeries.getDocumentMedium());
        }
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingSeries.setVersion(version);
        seriesRepository.save(existingSeries);
        return existingSeries;
    }

    // All DELETE operations
    @Override
    public int deleteEntity(@NotNull String seriesSystemId) {
        seriesRepository.delete(getSeriesOrThrow(seriesSystemId));
        return 1;
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
        if (series.getSeriesStatus().equals(STATUS_CLOSED)) {
            String info = INFO_CANNOT_ASSOCIATE_WITH_CLOSED_OBJECT +
                    ". Series with systemId " + series.getSystemId() +
                    " has status " + STATUS_CLOSED;
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
        Series series = seriesRepository.findBySystemId(seriesSystemId);
        if (series == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " Series, using systemId "
                    + seriesSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return series;
    }
}
