package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.CaseStatus;
import nikita.common.repository.n5v5.metadata.ICaseStatusRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.ICaseStatusService;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
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
                (List<INoarkEntity>) (List)
                        caseStatusRepository.findAll(), CASE_STATUS);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve a CaseStatus identified particular code.
     *
     * @param code The code of the object you wish to retrieve
     * @return The CaseStatus object wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                getCaseStatusOrThrow(code));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve a CaseStatus identified by particular code.  Raise
     * exception if the code is unknown.
     *
     * @param code The code of the object you wish to retrieve
     * @return The CaseStatus object wrapped
     */
    @Override
    public CaseStatus findCaseStatusByCode(String code) {
        return getCaseStatusOrThrow(code);
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
        caseStatus.setCodeName(TEMPLATE_CASE_STATUS_NAME);

        return caseStatus;
    }

    /**
     * Update a CaseStatus identified by its code
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param code               The code of the caseStatus object you wish to
     *                           update
     * @param incomingCaseStatus The incoming caseStatus object. Note the
     *                           values you are allowed to change are copied
     *                           from this object. This object is not
     *                           persisted.
     * @return the updated caseStatus
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String code,
            @NotNull final Long version,
            @NotNull final CaseStatus incomingCaseStatus) {

        CaseStatus existingCaseStatus = getCaseStatusOrThrow(code);
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
     * @param code The code of the CaseStatus object to retrieve
     * @return the CaseStatus object
     */
    private CaseStatus getCaseStatusOrThrow(@NotNull String code) {
        CaseStatus caseStatus = caseStatusRepository.
                findByCode(code);
        if (caseStatus == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " CaseStatus, using " +
                    "code " + code;
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
        return caseStatusRepository.findByCaseStatus(true);
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
