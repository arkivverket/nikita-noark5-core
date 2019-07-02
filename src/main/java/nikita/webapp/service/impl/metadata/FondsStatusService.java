package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.metadata.FondsStatus;
import nikita.common.repository.n5v5.metadata.IFondsStatusRepository;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IFondsStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class FondsStatusService
        extends NoarkService
        implements IFondsStatusService {

    private static final Logger logger =
            LoggerFactory.getLogger(FondsStatusService.class);

    private IFondsStatusRepository fondsStatusRepository;

    public FondsStatusService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IFondsStatusRepository fondsStatusRepository) {
        super(entityManager, applicationEventPublisher);
        this.fondsStatusRepository = fondsStatusRepository;
    }

    // All CREATE operations

    /**
     * Persists a new fondsStatus object to the database.
     *
     * @param fondsStatus fondsStatus object with values set
     * @return the newly persisted fondsStatus object
     */
    @Override
    public FondsStatus createNewFondsStatus(FondsStatus fondsStatus) {
        return fondsStatusRepository.save(fondsStatus);
    }

    // All READ operations

    /**
     * retrieve all fondsStatus
     *
     * @return
     */
    @Override
    public ArrayList<INikitaEntity> findAll() {
        return (ArrayList<INikitaEntity>)
                (ArrayList) fondsStatusRepository
                        .findAll();
    }

    // find by systemId

    /**
     * retrieve a single fondsStatus identified by systemId
     *
     * @param systemId
     * @return
     */
    @Override
    public FondsStatus findBySystemId(String systemId) {
        return fondsStatusRepository.findBySystemId(UUID.fromString(systemId));
    }

    /**
     * retrieve all fondsStatus that have a particular description. <br>
     * This will be replaced by OData search.
     *
     * @param description
     * @return
     */
    @Override
    public List<FondsStatus> findByDescription(String description) {
        return fondsStatusRepository.findByDescription(description);
    }

    /**
     * retrieve all fondsStatus that have a particular code. <br>
     * This will be replaced by OData search.
     *
     * @param code
     * @return
     */
    @Override
    public List<FondsStatus> findByCode(String code) {
        return fondsStatusRepository.findByCode(code);
    }

    /**
     * update a particular fondsStatus. <br>
     *
     * @param fondsStatus
     * @return the updated fondsStatus
     */
    @Override
    public FondsStatus update(FondsStatus fondsStatus) {
        return fondsStatusRepository.save(fondsStatus);
    }
}
