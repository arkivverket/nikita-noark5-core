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

import java.util.List;

import static nikita.common.config.Constants.*;

@Service
@Transactional
public class SeriesStatusService
        extends MetadataSuperService
        implements ISeriesStatusService {

    private static final Logger logger =
            LoggerFactory.getLogger(SeriesStatusService.class);

    private ISeriesStatusRepository seriesStatusRepository;

    public SeriesStatusService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            ISeriesStatusRepository seriesStatusRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher, metadataHateoasHandler);
        this.seriesStatusRepository = seriesStatusRepository;
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
    public List<SeriesStatus> findAll() {
        return seriesStatusRepository.findAll();
    }

    /**
     * retrieve seriesStatus that has a particular code.
     *
     * @param code The SeriesStatus to retrieve
     * @return the SeriesStatus object
     */
    @Override
    public SeriesStatus findMetadataByCode(String code) {
        return seriesStatusRepository.findByCode(code);
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
}
