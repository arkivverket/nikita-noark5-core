package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v4.metadata.RegistryEntryStatus;
import nikita.common.repository.n5v4.metadata.IRegistryEntryStatusRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IRegistryEntryStatusService;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private IRegistryEntryStatusRepository RegistryEntryStatusRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public RegistryEntryStatusService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IRegistryEntryStatusRepository registryEntryStatusRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        RegistryEntryStatusRepository = registryEntryStatusRepository;
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

        RegistryEntryStatus.setDeleted(false);
        RegistryEntryStatus.setOwnedBy(
                SecurityContextHolder.getContext().
                        getAuthentication().getName());

        MetadataHateoas metadataHateoas = new MetadataHateoas(
                RegistryEntryStatusRepository.save(RegistryEntryStatus));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all RegistryEntryStatus objects
     *
     * @return list of RegistryEntryStatus objects wrapped as a MetadataHateoas object
     */
    @Override
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        RegistryEntryStatusRepository.findAll(), REGISTRY_ENTRY_STATUS);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // find by systemId

    /**
     * Retrieve a single RegistryEntryStatus object identified by systemId
     *
     * @param systemId
     * @return single RegistryEntryStatus object wrapped as a MetadataHateoas object
     */
    @Override
    public MetadataHateoas find(String systemId) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                RegistryEntryStatusRepository.save(
                        RegistryEntryStatusRepository.findBySystemId(systemId)));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Retrieve all RegistryEntryStatus that have a given description.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param description
     * @return A list of RegistryEntryStatus objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByDescription(String description) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        RegistryEntryStatusRepository.findByDescription(description),
                REGISTRY_ENTRY_STATUS);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all RegistryEntryStatus that have a particular code.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param code
     * @return A list of RegistryEntryStatus objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        RegistryEntryStatusRepository.findByCode(code),
                REGISTRY_ENTRY_STATUS);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Generate a default RegistryEntryStatus object
     *
     * @return the RegistryEntryStatus object wrapped as a RegistryEntryStatusHateoas object
     */
    @Override
    public RegistryEntryStatus generateDefaultRegistryEntryStatus() {

        RegistryEntryStatus RegistryEntryStatus = new RegistryEntryStatus();
        RegistryEntryStatus.setCode(TEMPLATE_REGISTRY_ENTRY_STATUS_CODE);
        RegistryEntryStatus.setDescription(TEMPLATE_REGISTRY_ENTRY_STATUS_DESCRIPTION);

        return RegistryEntryStatus;
    }

    /**
     * Update a RegistryEntryStatus identified by its systemId
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param systemId        The systemId of the registryEntryStatus object
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
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final RegistryEntryStatus incomingRegistryEntryStatus) {

        RegistryEntryStatus existingRegistryEntryStatus =
                getRegistryEntryStatusOrThrow(systemId);
        updateCodeAndDescription(incomingRegistryEntryStatus,
                existingRegistryEntryStatus);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingRegistryEntryStatus.setVersion(version);

        MetadataHateoas RegistryEntryStatusHateoas = new MetadataHateoas(
                RegistryEntryStatusRepository.save(existingRegistryEntryStatus));
        metadataHateoasHandler.addLinks(RegistryEntryStatusHateoas,
                new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this,
                        existingRegistryEntryStatus));
        return RegistryEntryStatusHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid RegistryEntryStatus object back. If there
     * is no RegistryEntryStatus object, a NoarkEntityNotFoundException exception
     * is thrown
     *
     * @param systemId The systemId of the RegistryEntryStatus object to retrieve
     * @return the RegistryEntryStatus object
     */
    private RegistryEntryStatus getRegistryEntryStatusOrThrow(@NotNull String systemId) {
        RegistryEntryStatus RegistryEntryStatus = RegistryEntryStatusRepository.findBySystemId
                (systemId);
        if (RegistryEntryStatus == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " RegistryEntryStatus, using " +
                    "systemId " + systemId;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return RegistryEntryStatus;
    }

}
