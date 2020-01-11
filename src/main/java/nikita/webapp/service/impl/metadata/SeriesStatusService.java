package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.metadata.SeriesStatus;
import nikita.common.repository.n5v5.metadata.ISeriesStatusRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.ISeriesStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;

import static nikita.common.config.Constants.*;

@Service
@Transactional
public class SeriesStatusService
        extends NoarkService
        implements ISeriesStatusService {

    private static final Logger logger =
            LoggerFactory.getLogger(SeriesStatusService.class);

    private ISeriesStatusRepository seriesStatusRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public SeriesStatusService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            ISeriesStatusRepository seriesStatusRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.seriesStatusRepository = seriesStatusRepository;
        this.metadataHateoasHandler = metadataHateoasHandler;
    }

    // All CREATE operations

    /**
     * Persists a new seriesStatus object to the database.
     *
     * @param seriesStatus seriesStatus object with values set
     * @return the newly persisted seriesStatus object
     */
    @Override
    public SeriesStatus createNewSeriesStatus(SeriesStatus seriesStatus) {
        return seriesStatusRepository.save(seriesStatus);
    }

    // All READ operations

    /**
     * retrieve all seriesStatus
     *
     * @return List of all SeriesStatus objects
     */
    @Override
    public Iterable<SeriesStatus> findAll() {
        return seriesStatusRepository.findAll();
    }

    /**
     * retrieve seriesStatus that has a particular code.
     *
     * @param code The SeriesStatus to retrieve
     * @return the SeriesStatus object
     */
    @Override
    public SeriesStatus findByCode(String code) {
        return seriesStatusRepository.findByCode(code);
    }

    /**
     * retrieve a SeriesStatus identified by particular code.  Raise
     * exception if the code is unknown.
     *
     * @param code The code of the object you wish to retrieve
     * @return The SeriesStatus object wrapped
     */
    @Override
    public SeriesStatus findSeriesStatusByCode(String code) {
        return getSeriesStatusOrThrow(code);
    }

    /**
     * update a particular seriesStatus.
     *
     * @param seriesStatus incoming seriesStatus object to update
     * @return the updated seriesStatus
     */
    @Override
    public SeriesStatus update(SeriesStatus seriesStatus) {
        return seriesStatusRepository.save(seriesStatus);
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid SeriesStatus object back. If there
     * is no SeriesStatus object, a NoarkEntityNotFoundException exception
     * is thrown
     *
     * @param code The code of the SeriesStatus object to retrieve
     * @return the SeriesStatus object
     */
    private SeriesStatus getSeriesStatusOrThrow(@NotNull String code) {
        SeriesStatus seriesStatus =
           seriesStatusRepository.findByCode(code);
        if (seriesStatus == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
               "SeriesStatus, using " + "code " + code;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return seriesStatus;
    }
}
