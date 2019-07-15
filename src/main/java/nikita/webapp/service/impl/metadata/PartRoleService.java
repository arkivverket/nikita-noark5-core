package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.metadata.PartRole;
import nikita.common.repository.n5v5.metadata.IPartRoleRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IPartRoleService;
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
public class PartRoleService
        extends NoarkService
        implements IPartRoleService {

    private static final Logger logger =
            LoggerFactory.getLogger(PartRoleService.class);

    private IPartRoleRepository partyRoleRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public PartRoleService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IPartRoleRepository partyRoleRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.partyRoleRepository =
                partyRoleRepository;
        this.metadataHateoasHandler = metadataHateoasHandler;
    }

    // All CREATE operations

    /**
     * Persists a new PartRole object to the database.
     *
     * @param partyRole partyRole object with values set
     * @return the newly persisted PartRole object wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas createNewPartRole(
            PartRole partyRole) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                partyRoleRepository.save(partyRole));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all PartRole objects
     *
     * @return list of PartRole objects wrapped as a
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

    /**
     * retrieve all PartRole that have a particular code.
     *
     * @param code The code of the object you wish to retrieve
     * @return A list of PartRole objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                partyRoleRepository.findByCode(code));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Generate a default PartRole object
     *
     * @return the PartRole object wrapped as a PartRoleHateoas object
     */
    @Override
    public PartRole generateDefaultPartRole() {
        PartRole partyRole = new PartRole();
        partyRole.setCode(TEMPLATE_PART_ROLE_CODE);
        partyRole.setName(TEMPLATE_PART_ROLE_NAME);
        return partyRole;
    }

    /**
     * Update a PartRole identified by its code
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param code      The code of the PartRole object you wish to
     *                      update
     * @param incomingPartRole The updated PartRole object. Note
     *                              the values you are allowed to change are
     *                              copied from this object. This object is
     *                              not persisted.
     * @return the updated PartRole
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String code,
            @NotNull final Long version,
            @NotNull final PartRole incomingPartRole) {

        PartRole existingPartRole = getPartRoleOrThrow(code);
        updateCodeAndDescription(incomingPartRole, existingPartRole);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingPartRole.setVersion(version);

        MetadataHateoas partyRoleHateoas = new MetadataHateoas(
                partyRoleRepository.save(existingPartRole));

        metadataHateoasHandler.addLinks(partyRoleHateoas,
                new Authorisation());

        applicationEventPublisher.publishEvent(new
                AfterNoarkEntityUpdatedEvent(this,
                existingPartRole));
        return partyRoleHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid PartRole object back. If there
     * is no PartRole object, a NoarkEntityNotFoundException exception
     * is thrown
     *
     * @param code The code of the PartRole object to retrieve
     * @return the PartRole object
     */
    private PartRole
    getPartRoleOrThrow(@NotNull String code) {
        PartRole partyRole =
                partyRoleRepository.findByCode(code);
        if (partyRole == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " PartRole, using " +
                    "code " + code;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return partyRole;
    }
}
