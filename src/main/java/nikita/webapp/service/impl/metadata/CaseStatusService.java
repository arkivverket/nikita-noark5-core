package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v4.metadata.CaseStatus;
import nikita.common.repository.n5v4.metadata.ICaseStatusRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.ICaseStatusService;
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
import java.util.Optional;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CASE_STATUS;

/**
 * Created by tsodring on 13/03/18.
 */

@Service
@Transactional
@SuppressWarnings("unchecked")
public class CaseStatusService
        extends NoarkService
        implements ICaseStatusService {

    private static final Logger logger =
            LoggerFactory.getLogger(CaseStatusService.class);

    private ICaseStatusRepository caseStatusRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public CaseStatusService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            ICaseStatusRepository
                    caseStatusRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.caseStatusRepository =
                caseStatusRepository;
        this.metadataHateoasHandler = metadataHateoasHandler;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // All CREATE operations

    /**
     * Persists a new CaseStatus object to the database.
     *
     * @param caseStatus CaseStatus object with values set
     * @return the newly persisted CaseStatus object wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas createNewCaseStatus(
            CaseStatus caseStatus) {

        caseStatus.setDeleted(false);
        caseStatus.setOwnedBy(SecurityContextHolder.getContext().
                getAuthentication().getName());

        MetadataHateoas metadataHateoas = new MetadataHateoas(
                caseStatusRepository.save(caseStatus));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all CaseStatus objects
     *
     * @return list of CaseStatus objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        caseStatusRepository.findAll(), CASE_STATUS);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // find by systemId

    /**
     * Retrieve a single CaseStatus object identified by systemId
     *
     * @param systemId systemId of the CaseStatus you wish to retrieve
     * @return single CaseStatus object wrapped as a MetadataHateoas object
     */
    @Override
    public MetadataHateoas find(String systemId) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                caseStatusRepository
                        .findBySystemId(systemId));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Retrieve all CaseStatus that have a given description.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param description Description of object you wish to retrieve. The
     *                    whole text, this is an exact search.
     * @return A list of CaseStatus objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByDescription(String description) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        caseStatusRepository
                                .findByDescription(description), CASE_STATUS);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all CaseStatus that have a particular code.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param code The code of the object you wish to retrieve
     * @return A list of CaseStatus objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        caseStatusRepository.findByCode(code), CASE_STATUS);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Generate a default CaseStatus object
     *
     * @return the CaseStatus object
     */
    @Override
    public CaseStatus generateDefaultCaseStatus() {

        CaseStatus caseStatus = new CaseStatus();
        caseStatus.setCode(TEMPLATE_CASE_STATUS_CODE);
        caseStatus.setDescription(TEMPLATE_CASE_STATUS_DESCRIPTION);

        return caseStatus;
    }

    /**
     * Update a CaseStatus identified by its systemId
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param systemId   The systemId of the caseStatus object you wish to
     *                   update
     * @param incomingCaseStatus  The incoming caseStatus object. Note the
     *                            values you are allowed to change are copied
     *                            from this object. This object is not
     *                            persisted.
     * @return the updated caseStatus
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final CaseStatus incomingCaseStatus) {

        CaseStatus existingCaseStatus = getCaseStatusOrThrow(systemId);
        // Copy all the values you are allowed to copy ....
        updateCodeAndDescription(incomingCaseStatus, existingCaseStatus);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingCaseStatus.setVersion(version);
        MetadataHateoas caseStatusHateoas = new MetadataHateoas(
                caseStatusRepository.save(existingCaseStatus));

        metadataHateoasHandler.addLinks(caseStatusHateoas,
                new Authorisation());

        applicationEventPublisher.publishEvent(new
                AfterNoarkEntityUpdatedEvent(this,
                existingCaseStatus));
        return caseStatusHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid CaseStatus object back. If there
     * is no CaseStatus object, a NoarkEntityNotFoundException exception
     * is thrown
     *
     * @param systemId The systemId of the CaseStatus object to retrieve
     * @return the CaseStatus object
     */
    private CaseStatus getCaseStatusOrThrow(@NotNull String systemId) {
        CaseStatus caseStatus = caseStatusRepository.
                findBySystemId(systemId);
        if (caseStatus == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " CaseStatus, using " +
                    "systemId " + systemId;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return caseStatus;
    }

    /**
     * Gets the default CaseStatus if a default is set. If multiple defaults
     * are set, return the first one the database finds. Note default == true
     * should not be set on multiple CaseStatus.
     *
     * @return the default CaseStatus object
     */
    @Override
    public Optional<CaseStatus> getDefaultCaseStatus() {
        return caseStatusRepository.findByDefaultCaseStatus(true);
    }

    /**
     * Gets a CaseStatus by its code.
     *
     * @return the CaseStatus object
     */
    @Override
    public Optional<CaseStatus> getCaseStatusByCode() {
        return Optional.empty();
    }

    /**
     * Gets a CaseStatus by its description.
     *
     * @return the CaseStatus object
     */
    @Override
    public Optional<CaseStatus> getCaseStatusByDescription() {
        return Optional.empty();
    }
}
