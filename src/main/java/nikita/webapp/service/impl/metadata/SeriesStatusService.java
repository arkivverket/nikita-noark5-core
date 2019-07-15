package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.metadata.SeriesStatus;
import nikita.common.repository.n5v5.metadata.ISeriesStatusRepository;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.ISeriesStatusService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@Transactional
public class SeriesStatusService
        extends NoarkService
        implements ISeriesStatusService {

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
