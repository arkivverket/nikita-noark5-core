package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.RegistryEntryStatus;
import nikita.common.repository.n5v5.metadata.IRegistryEntryStatusRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IRegistryEntryStatusService;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.REGISTRY_ENTRY_STATUS;

/**
 * Created by tsodring on 12/02/18.
 */

@Service
@Transactional
public class RegistryEntryStatusService
        extends MetadataSuperService
        implements IRegistryEntryStatusService {

    private static final Logger logger =
            LoggerFactory.getLogger(RegistryEntryStatusService.class);

    private IRegistryEntryStatusRepository registryEntryStatusRepository;

    public RegistryEntryStatusService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IRegistryEntryStatusRepository registryEntryStatusRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher, metadataHateoasHandler);
        this.registryEntryStatusRepository = registryEntryStatusRepository;
    }

    // All CREATE operations

    /**
     * Persists a new RegistryEntryStatus object to the database.
     *
     * @param RegistryEntryStatus RegistryEntryStatus object with values set
     * @return the newly persisted RegistryEntryStatus object wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas createNewRegistryEntryStatus(
            RegistryEntryStatus RegistryEntryStatus) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                registryEntryStatusRepository.save(RegistryEntryStatus));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all RegistryEntryStatus objects
     *
     * @return list of RegistryEntryStatus objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    @SuppressWarnings("unchecked")
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<IMetadataEntity>) (List)
                        registryEntryStatusRepository.findAll(),
                REGISTRY_ENTRY_STATUS);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    @Override
    public RegistryEntryStatus findMetadataByCode(String code) {
        return registryEntryStatusRepository.findByCode(code);
    }

    /**
     * Generate a default RegistryEntryStatus object
     *
     * @return the RegistryEntryStatus object wrapped as a
     * RegistryEntryStatusHateoas object
     */
    @Override
    public RegistryEntryStatus generateDefaultRegistryEntryStatus() {
        RegistryEntryStatus RegistryEntryStatus = new RegistryEntryStatus();
        RegistryEntryStatus.setCode(TEMPLATE_REGISTRY_ENTRY_STATUS_CODE);
        RegistryEntryStatus.setCodeName(TEMPLATE_REGISTRY_ENTRY_STATUS_NAME);
        return RegistryEntryStatus;
    }

    /**
     * Update a RegistryEntryStatus identified by its code
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param code        The code of the registryEntryStatus object
     *                        you wish to update
     * @param incomingRegistryEntryStatus The updated registryEntryStatus
     *                                    object. Note the values you are
     *                                    allowed to change are copied from
     *                                    this object. This object is not
     *                                    persisted.
     * @return the updated registryEntryStatus
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String code,
            @NotNull final Long version,
            @NotNull final RegistryEntryStatus incomingRegistryEntryStatus) {

        RegistryEntryStatus existingRegistryEntryStatus =
            (RegistryEntryStatus) findMetadataByCodeOrThrow(code);
        updateCodeAndDescription(incomingRegistryEntryStatus,
                existingRegistryEntryStatus);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingRegistryEntryStatus.setVersion(version);

        MetadataHateoas RegistryEntryStatusHateoas = new MetadataHateoas(
                registryEntryStatusRepository.save(existingRegistryEntryStatus));
        metadataHateoasHandler.addLinks(RegistryEntryStatusHateoas,
                new Authorisation());
        return RegistryEntryStatusHateoas;
    }
}
