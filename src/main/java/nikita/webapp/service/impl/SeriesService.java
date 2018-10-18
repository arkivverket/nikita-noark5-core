package nikita.webapp.service.impl;

import nikita.common.model.noark5.v4.File;
import nikita.common.model.noark5.v4.Series;
import nikita.common.model.noark5.v4.casehandling.CaseFile;
import nikita.common.repository.n5v4.ISeriesRepository;
import nikita.common.util.exceptions.NoarkEntityEditWhenClosedException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.service.interfaces.ICaseFileService;
import nikita.webapp.service.interfaces.IFileService;
import nikita.webapp.service.interfaces.ISeriesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static nikita.common.config.Constants.INFO_CANNOT_ASSOCIATE_WITH_CLOSED_OBJECT;
import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.N5ResourceMappings.STATUS_CLOSED;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.checkDocumentMediumValid;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.setNoarkEntityValues;

@Service
@Transactional
public class SeriesService
        implements ISeriesService {

    private static final Logger logger =
            LoggerFactory.getLogger(SeriesService.class);

    private IFileService fileService;
    private ICaseFileService caseFileService;
    private ISeriesRepository seriesRepository;

    public SeriesService(IFileService fileService,
                         ICaseFileService caseFileService,
                         ISeriesRepository seriesRepository) {
        this.fileService = fileService;
        this.caseFileService = caseFileService;
        this.seriesRepository = seriesRepository;
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

    // All READ operations
    // NOTE: I am leaving these methods here for another while. They will
    // probably be replaced by a single CriteriaBuilder approach, but for the
    // moment, they will be left here.
    @Override
    public List<Series> findAll() {
        return seriesRepository.findAll();
    }

    @Override
    public List<CaseFile> findAllCaseFileBySeries(String systemId) {
        Series series = getSeriesOrThrow(systemId);
        return caseFileService.findAllCaseFileBySeries(series);
    }

    // id
    @Override
    public Optional<Series> findById(Long id) {
        return seriesRepository.findById(id);
    }

    // systemId
    @Override
    public Series findBySystemId(String systemId) {
        return getSeriesOrThrow(systemId);
    }

    // ownedBy
    @Override
    public List<Series> findByOwnedBy(String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().
                getAuthentication().getName() : ownedBy;
        return seriesRepository.findByOwnedBy(ownedBy);
    }

    // All UPDATE operations
    @Override
    public Series handleUpdate(@NotNull String systemId, @NotNull Long version,
                               @NotNull Series incomingSeries) {
        Series existingSeries = getSeriesOrThrow(systemId);
        // Here copy all the values you are allowed to copy ....
        if (null != existingSeries.getDescription()) {
            existingSeries.setDescription(incomingSeries.getDescription());
        }
        if (null != incomingSeries.getTitle()) {
            existingSeries.setTitle(incomingSeries.getTitle());
        }
        if (null != incomingSeries.getDocumentMedium()) {
            existingSeries.setDocumentMedium(incomingSeries.getDocumentMedium());
        }
        existingSeries.setVersion(version);
        seriesRepository.save(existingSeries);
        return existingSeries;
    }

    // All DELETE operations
    @Override
    public void deleteEntity(@NotNull String seriesSystemId) {
        Series series = getSeriesOrThrow(seriesSystemId);
        seriesRepository.delete(series);
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
