package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v4.metadata.CasePartyRole;
import nikita.common.repository.n5v4.metadata.ICasePartyRoleRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.ICasePartyRoleService;
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
import static nikita.common.config.N5ResourceMappings.CASE_PARTY_ROLE;

/**
 * Created by tsodring on 21/02/18.
 */

@Service
@Transactional
@SuppressWarnings("unchecked")
public class CasePartyRoleService
        extends NoarkService
        implements ICasePartyRoleService {

    private static final Logger logger =
            LoggerFactory.getLogger(CasePartyRoleService.class);

    private ICasePartyRoleRepository casePartyRoleRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public CasePartyRoleService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            ICasePartyRoleRepository casePartyRoleRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.casePartyRoleRepository =
                casePartyRoleRepository;
        this.metadataHateoasHandler = metadataHateoasHandler;
    }

    // All CREATE operations

    /**
     * Persists a new CasePartyRole object to the database.
     *
     * @param casePartyRole CasePartyRole object with values set
     * @return the newly persisted CasePartyRole object wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas createNewCasePartyRole(
            CasePartyRole casePartyRole) {

        casePartyRole.setDeleted(false);
        casePartyRole.setOwnedBy(SecurityContextHolder.getContext().
                getAuthentication().getName());

        MetadataHateoas metadataHateoas = new MetadataHateoas(
                casePartyRoleRepository.save(casePartyRole));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all CasePartyRole objects
     *
     * @return list of CasePartyRole objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        casePartyRoleRepository.findAll(), CASE_PARTY_ROLE);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // find by systemId

    /**
     * Retrieve a single CasePartyRole object identified by systemId
     *
     * @param systemId systemId of the CasePartyRole you wish to retrieve
     * @return single CasePartyRole object wrapped as a MetadataHateoas object
     */
    @Override
    public MetadataHateoas find(String systemId) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                casePartyRoleRepository
                        .findBySystemId(systemId));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Retrieve all CasePartyRole that have a given
     * description.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param description Description of object you wish to retrieve. The
     *                    whole text, this is an exact search.
     * @return A list of CasePartyRole objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByDescription(String description) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        casePartyRoleRepository
                                .findByDescription(description),
                CASE_PARTY_ROLE);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all CasePartyRole that have a particular code.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param code The code of the object you wish to retrieve
     * @return A list of CasePartyRole objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        casePartyRoleRepository.findByCode
                                (code), CASE_PARTY_ROLE);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Generate a default CasePartyRole object
     *
     * @return the CasePartyRole object wrapped as a CasePartyRoleHateoas object
     */
    @Override
    public CasePartyRole generateDefaultCasePartyRole() {

        CasePartyRole casePartyRole = new CasePartyRole();
        casePartyRole.setCode(TEMPLATE_CASE_PARTY_ROLE_CODE);
        casePartyRole.setDescription(TEMPLATE_CASE_PARTY_ROLE_DESCRIPTION);

        return casePartyRole;
    }

    /**
     * Update a CasePartyRole identified by its systemId
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param systemId      The systemId of the casePartyRole object you wish to
     *                      update
     * @param incomingCasePartyRole The updated casePartyRole object. Note
     *                              the values you are allowed to change are
     *                              copied from this object. This object is
     *                              not persisted.
     * @return the updated casePartyRole
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final CasePartyRole incomingCasePartyRole) {

        CasePartyRole existingCasePartyRole = getCasePartyRoleOrThrow(systemId);
        updateCodeAndDescription(incomingCasePartyRole, existingCasePartyRole);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingCasePartyRole.setVersion(version);

        MetadataHateoas casePartyRoleHateoas = new MetadataHateoas(
                casePartyRoleRepository.save(existingCasePartyRole));

        metadataHateoasHandler.addLinks(casePartyRoleHateoas,
                new Authorisation());

        applicationEventPublisher.publishEvent(new
                AfterNoarkEntityUpdatedEvent(this,
                existingCasePartyRole));
        return casePartyRoleHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid CasePartyRole object back. If there
     * is no CasePartyRole object, a NoarkEntityNotFoundException exception
     * is thrown
     *
     * @param systemId The systemId of the CasePartyRole object to retrieve
     * @return the CasePartyRole object
     */
    private CasePartyRole
    getCasePartyRoleOrThrow(@NotNull String systemId) {
        CasePartyRole casePartyRole =
                casePartyRoleRepository.findBySystemId(systemId);
        if (casePartyRole == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " CasePartyRole, using " +
                    "systemId " + systemId;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return casePartyRole;
    }
}
