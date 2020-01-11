package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.RegistryEntryType;
import nikita.common.repository.n5v5.metadata.IRegistryEntryTypeRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IRegistryEntryTypeService;
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
import static nikita.common.config.N5ResourceMappings.REGISTRY_ENTRY_TYPE;

/**
 * Created by tsodring on 18/02/18.
 */

@Service
@Transactional
@SuppressWarnings("unchecked")
public class RegistryEntryTypeService
        extends NoarkService
        implements IRegistryEntryTypeService {

    private static final Logger logger =
            LoggerFactory.getLogger(RegistryEntryTypeService.class);

    private IRegistryEntryTypeRepository registryEntryTypeRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public RegistryEntryTypeService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IRegistryEntryTypeRepository registryEntryTypeRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.registryEntryTypeRepository = registryEntryTypeRepository;
        this.metadataHateoasHandler = metadataHateoasHandler;
    }

    // All CREATE operations

    /**
     * Persists a new RegistryEntryType object to the database.
     *
     * @param registryEntryType RegistryEntryType object with values set
     * @return the newly persisted RegistryEntryType object wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas createNewRegistryEntryType(
            RegistryEntryType registryEntryType) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                registryEntryTypeRepository.save(registryEntryType));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all RegistryEntryType objects
     *
     * @return list of RegistryEntryType objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INoarkEntity>) (List)
                        registryEntryTypeRepository.findAll(), REGISTRY_ENTRY_TYPE);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all RegistryEntryType that have a particular code.

     *
     * @param code The code of the object you wish to retrieve
     * @return A list of RegistryEntryType objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                getRegistryEntryTypeOrThrow(code));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Generate a default RegistryEntryType object
     *
     * @return the RegistryEntryType object
     */
    @Override
    public RegistryEntryType generateDefaultRegistryEntryType() {

        RegistryEntryType format = new RegistryEntryType();
        format.setCode(TEMPLATE_REGISTRY_ENTRY_TYPE_CODE);
        format.setCodeName(TEMPLATE_REGISTRY_ENTRY_TYPE_NAME);

        return format;
    }

    /**
     * Update a RegistryEntryType identified by its code
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param code The code of the format object you wish to update
     * @param incomingRegistryEntryType The updated format object. Note the
     *                                 values you are allowed to change are
     *                                  copied from this object. This object
     *                                  is not persisted.
     * @return the updated format
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String code,
            @NotNull final Long version,
            @NotNull final RegistryEntryType incomingRegistryEntryType) {

        RegistryEntryType existingRegistryEntryType =
                getRegistryEntryTypeOrThrow(code);
        updateCodeAndDescription(incomingRegistryEntryType,
                existingRegistryEntryType);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingRegistryEntryType.setVersion(version);

        MetadataHateoas formatHateoas = new MetadataHateoas(registryEntryTypeRepository
                .save(existingRegistryEntryType));

        metadataHateoasHandler.addLinks(formatHateoas, new Authorisation());

        applicationEventPublisher.publishEvent(new
                AfterNoarkEntityUpdatedEvent(this, existingRegistryEntryType));
        return formatHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid RegistryEntryType object back. If
     * there is no RegistryEntryType object, a NoarkEntityNotFoundException
     * exception is thrown
     *
     * @param code The code of the RegistryEntryType object to retrieve
     * @return the RegistryEntryType object
     */
    private RegistryEntryType
    getRegistryEntryTypeOrThrow(@NotNull String code) {
        RegistryEntryType format =
                registryEntryTypeRepository.findByCode(code);
        if (format == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " RegistryEntryType,  " +
                    "using code " + code;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return format;
    }
}
