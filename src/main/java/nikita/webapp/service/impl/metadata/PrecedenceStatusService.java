package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.metadata.PrecedenceStatus;
import nikita.common.repository.n5v5.metadata.IPrecedenceStatusRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IPrecedenceStatusService;
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
import static nikita.common.config.N5ResourceMappings.PRECEDENCE_STATUS;

/**
 * Created by tsodring on 19/02/18.
 */

@Service
@Transactional
@SuppressWarnings("unchecked")
public class PrecedenceStatusService
        extends NoarkService
        implements IPrecedenceStatusService {

    private static final Logger logger =
            LoggerFactory.getLogger(PrecedenceStatusService.class);

    private IPrecedenceStatusRepository precedenceStatusRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public PrecedenceStatusService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IPrecedenceStatusRepository precedenceStatusRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.precedenceStatusRepository = precedenceStatusRepository;
        this.metadataHateoasHandler = metadataHateoasHandler;
    }

    // All CREATE operations

    /**
     * Persists a new PrecedenceStatus object to the database.
     *
     * @param precedenceStatus PrecedenceStatus object with values set
     * @return the newly persisted PrecedenceStatus object wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas createNewPrecedenceStatus(
            PrecedenceStatus precedenceStatus) {

        precedenceStatus.setDeleted(false);
        precedenceStatus.setOwnedBy(SecurityContextHolder.getContext().
                getAuthentication().getName());

        MetadataHateoas metadataHateoas = new MetadataHateoas(
                precedenceStatusRepository.save(precedenceStatus));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all PrecedenceStatus objects
     *
     * @return list of PrecedenceStatus objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        precedenceStatusRepository.findAll(), PRECEDENCE_STATUS);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // find by systemId

    /**
     * Retrieve a single PrecedenceStatus object identified by systemId
     *
     * @param systemId systemId of the PrecedenceStatus you wish to retrieve
     * @return single PrecedenceStatus object wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas find(String systemId) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                precedenceStatusRepository
                        .findBySystemId(systemId));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Retrieve all PrecedenceStatus that have a given
     * description.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param description Description of object you wish to retrieve. The
     *                    whole text, this is an exact search.
     * @return A list of PrecedenceStatus objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByDescription(String description) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        precedenceStatusRepository
                                .findByDescription(description),
                PRECEDENCE_STATUS);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all PrecedenceStatus that have a particular code.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param code The code of the object you wish to retrieve
     * @return A list of PrecedenceStatus objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        precedenceStatusRepository.findByCode
                                (code), PRECEDENCE_STATUS);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Generate a default PrecedenceStatus object
     *
     * @return the PrecedenceStatus object
     */
    @Override
    public PrecedenceStatus generateDefaultPrecedenceStatus() {

        PrecedenceStatus precedenceStatus = new PrecedenceStatus();
        precedenceStatus.setCode(TEMPLATE_PRECEDENCE_STATUS_CODE);
        precedenceStatus.setDescription(TEMPLATE_PRECEDENCE_STATUS_DESCRIPTION);

        return precedenceStatus;
    }

    /**
     * Update a PrecedenceStatus identified by its systemId
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param systemId         The systemId of the precedenceStatus object you wish to
     *                         update
     * @param incomingPrecedenceStatus The updated precedenceStatus object.
     *                                 Note the values you are allowed to
     *                                 change are copied from this object.
     *                                 This object is not persisted.
     * @return the updated precedenceStatus
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final PrecedenceStatus incomingPrecedenceStatus) {

        PrecedenceStatus existingPrecedenceStatus =
                getPrecedenceStatusOrThrow(systemId);
        updateCodeAndDescription(incomingPrecedenceStatus,
                existingPrecedenceStatus);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingPrecedenceStatus.setVersion(version);

        MetadataHateoas precedenceStatusHateoas =
                new MetadataHateoas(
                        precedenceStatusRepository.
                                save(existingPrecedenceStatus));

        metadataHateoasHandler.addLinks(precedenceStatusHateoas, new Authorisation());

        applicationEventPublisher.publishEvent(new
                AfterNoarkEntityUpdatedEvent(this,
                existingPrecedenceStatus));
        return precedenceStatusHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid PrecedenceStatus object back. If
     * there is no PrecedenceStatus object, a NoarkEntityNotFoundException
     * exception is thrown
     *
     * @param systemId The systemId of the PrecedenceStatus object to retrieve
     * @return the PrecedenceStatus object
     */
    private PrecedenceStatus
    getPrecedenceStatusOrThrow(@NotNull String systemId) {
        PrecedenceStatus precedenceStatus = precedenceStatusRepository.findBySystemId(systemId);
        if (precedenceStatus == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " PrecedenceStatus,  " +
                    "using systemId " + systemId;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return precedenceStatus;
    }
}
