package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.metadata.PartyRole;
import nikita.common.repository.n5v5.metadata.IPartyRoleRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IPartyRoleService;
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
import static nikita.common.config.N5ResourceMappings.PART_ROLE;

/**
 * Created by tsodring on 21/02/18.
 */

@Service
@Transactional
public class PartyRoleService
        extends NoarkService
        implements IPartyRoleService {

    private static final Logger logger =
            LoggerFactory.getLogger(PartyRoleService.class);

    private IPartyRoleRepository partyRoleRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public PartyRoleService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IPartyRoleRepository partyRoleRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.partyRoleRepository =
                partyRoleRepository;
        this.metadataHateoasHandler = metadataHateoasHandler;
    }

    // All CREATE operations

    /**
     * Persists a new PartyRole object to the database.
     *
     * @param partyRole partyRole object with values set
     * @return the newly persisted PartyRole object wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas createNewPartyRole(
            PartyRole partyRole) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                partyRoleRepository.save(partyRole));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all PartyRole objects
     *
     * @return list of PartyRole objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    @SuppressWarnings("unchecked")
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        partyRoleRepository.findAll(), PART_ROLE);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // find by systemId

    /**
     * Retrieve a single PartyRole object identified by systemId
     *
     * @param systemId systemId of the PartyRole you wish to retrieve
     * @return single PartyRole object wrapped as a MetadataHateoas object
     */
    @Override
    public MetadataHateoas find(String systemId) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                partyRoleRepository
                        .findBySystemId(systemId));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Retrieve all PartyRole that have a given
     * description.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param description Description of object you wish to retrieve. The
     *                    whole text, this is an exact search.
     * @return A list of PartyRole objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    @SuppressWarnings("unchecked")
    public MetadataHateoas findByDescription(String description) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        partyRoleRepository
                                .findByDescription(description), PART_ROLE);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all PartyRole that have a particular code.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param code The code of the object you wish to retrieve
     * @return A list of PartyRole objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    @SuppressWarnings("unchecked")
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        partyRoleRepository.findByCode
                                (code), PART_ROLE);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Generate a default PartyRole object
     *
     * @return the PartyRole object wrapped as a PartyRoleHateoas object
     */
    @Override
    public PartyRole generateDefaultPartyRole() {

        PartyRole partyRole = new PartyRole();
        partyRole.setCode(TEMPLATE_PART_ROLE_CODE);
        partyRole.setDescription(TEMPLATE_PART_ROLE_DESCRIPTION);

        return partyRole;
    }

    /**
     * Update a PartyRole identified by its systemId
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param systemId      The systemId of the PartyRole object you wish to
     *                      update
     * @param incomingPartyRole The updated PartyRole object. Note
     *                              the values you are allowed to change are
     *                              copied from this object. This object is
     *                              not persisted.
     * @return the updated PartyRole
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final PartyRole incomingPartyRole) {

        PartyRole existingPartyRole = getPartyRoleOrThrow(systemId);
        updateCodeAndDescription(incomingPartyRole, existingPartyRole);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingPartyRole.setVersion(version);

        MetadataHateoas partyRoleHateoas = new MetadataHateoas(
                partyRoleRepository.save(existingPartyRole));

        metadataHateoasHandler.addLinks(partyRoleHateoas,
                new Authorisation());

        applicationEventPublisher.publishEvent(new
                AfterNoarkEntityUpdatedEvent(this,
                existingPartyRole));
        return partyRoleHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid PartyRole object back. If there
     * is no PartyRole object, a NoarkEntityNotFoundException exception
     * is thrown
     *
     * @param systemId The systemId of the PartyRole object to retrieve
     * @return the PartyRole object
     */
    private PartyRole
    getPartyRoleOrThrow(@NotNull String systemId) {
        PartyRole partyRole =
                partyRoleRepository.findBySystemId(systemId);
        if (partyRole == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " PartyRole, using " +
                    "systemId " + systemId;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return partyRole;
    }
}
