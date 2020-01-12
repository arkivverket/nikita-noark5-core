package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
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
        extends NoarkService
        implements IRegistryEntryStatusService {

    private static final Logger logger =
            LoggerFactory.getLogger(RegistryEntryStatusService.class);

    private IRegistryEntryStatusRepository registryEntryStatusRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public RegistryEntryStatusService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IRegistryEntryStatusRepository registryEntryStatusRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.registryEntryStatusRepository = registryEntryStatusRepository;
        this.metadataHateoasHandler = metadataHateoasHandler;
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
                (List<INoarkEntity>) (List)
                        registryEntryStatusRepository.findAll(),
                REGISTRY_ENTRY_STATUS);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all RegistryEntryStatus that have a particular code.

     *
     * @param code Code to retrieve
     * @return A list of RegistryEntryStatus objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas =
                new MetadataHateoas(getRegistryEntryStatusOrThrow(code));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
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
                getRegistryEntryStatusOrThrow(code);
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

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid RegistryEntryStatus object back.
     * If there is no RegistryEntryStatus object, a
     * NoarkEntityNotFoundException exception is thrown
     *
     * @param code The code of the RegistryEntryStatus object to retrieve
     * @return the RegistryEntryStatus object
     */
    private RegistryEntryStatus getRegistryEntryStatusOrThrow(
            @NotNull String code) {
        RegistryEntryStatus registryEntryStatus =
                registryEntryStatusRepository.
                        findByCode(code);
        if (registryEntryStatus  == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " RegistryEntryStatus, using code " + code;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return registryEntryStatus ;
    }
}
