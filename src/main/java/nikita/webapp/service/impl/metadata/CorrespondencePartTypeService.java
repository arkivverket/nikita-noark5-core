package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.metadata.CorrespondencePartType;
import nikita.common.repository.n5v5.metadata.ICorrespondencePartTypeRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
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
        extends MetadataSuperService
        implements ICorrespondencePartTypeService {

    private static final Logger logger =
            LoggerFactory.getLogger(CorrespondencePartTypeService.class);
    private ICorrespondencePartTypeRepository correspondencePartTypeRepository;

    public CorrespondencePartTypeService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            ICorrespondencePartTypeRepository correspondencePartTypeRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {

        super(entityManager, applicationEventPublisher, metadataHateoasHandler);
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
    public CorrespondencePartType findMetadataByCode(String code) {
        return correspondencePartTypeRepository.findByCode(code);
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
            (CorrespondencePartType) findMetadataByCodeOrThrow(code));
    }
}
