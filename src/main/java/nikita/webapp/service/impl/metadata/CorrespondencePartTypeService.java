package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.metadata.CorrespondencePartType;
import nikita.common.repository.n5v5.metadata.ICorrespondencePartTypeRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.ICorrespondencePartTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;

import java.util.List;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;


@Service
@Transactional
public class CorrespondencePartTypeService
        extends NoarkService
        implements ICorrespondencePartTypeService {

    private static final Logger logger =
            LoggerFactory.getLogger(CorrespondencePartTypeService.class);
    private ICorrespondencePartTypeRepository correspondencePartTypeRepository;

    public CorrespondencePartTypeService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            ICorrespondencePartTypeRepository correspondencePartTypeRepository) {
        super(entityManager, applicationEventPublisher);
        this.correspondencePartTypeRepository = correspondencePartTypeRepository;
    }

    // All CREATE operations

    /**
     * Persists a new correspondencePartType object to the database.
     *
     * @param correspondencePartType correspondencePartType object with values set
     * @return the newly persisted correspondencePartType object
     */
    @Override
    public CorrespondencePartType createNewCorrespondencePartType(
            CorrespondencePartType correspondencePartType) {
        return correspondencePartTypeRepository.save(correspondencePartType);
    }

    // All READ operations

    /**
     * retrieve all correspondencePartType
     *
     * @return
     */
    @Override
    public List<IMetadataEntity> findAll() {
        return (List<IMetadataEntity>) (List)
                correspondencePartTypeRepository.findAll();
    }

    /**
     * retrieve all correspondencePartType that have a particular code. <br>
     * This will be replaced by OData search.
     *
     * @param code
     * @return
     */
    @Override
    public CorrespondencePartType findByCode(String code) {
        return getCorrespondencePartTypeOrThrow(code);
    }

    /**
     * update a particular correspondencePartType. <br>
     *
     * @param correspondencePartType
     * @return the updated correspondencePartType
     */
    @Override
    public CorrespondencePartType update(
            CorrespondencePartType correspondencePartType) {
        return correspondencePartTypeRepository.save(correspondencePartType);
    }

    // All DELETE operations

    @Override
    public void deleteEntity(@NotNull String code) {
        correspondencePartTypeRepository.delete(
                getCorrespondencePartTypeOrThrow(code));
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid CorrespondencePartType back. If
     * there is no valid CorrespondencePartType, an exception is thrown
     *
     * @param code
     * @return
     */
    protected CorrespondencePartType
    getCorrespondencePartTypeOrThrow(@NotNull String code) {
        CorrespondencePartType correspondencePartType =
                correspondencePartTypeRepository.findByCode(code);
        if (correspondencePartType == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " correspondencePartType, using kode " + code;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return correspondencePartType;
    }
}
