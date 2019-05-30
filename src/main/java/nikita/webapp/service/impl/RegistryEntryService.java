package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.casehandling.Precedence;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartInternal;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartInternalHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartPersonHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartUnitHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.RegistryEntryHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.repository.n5v5.IRegistryEntryRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IRegistryEntryHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IRegistryEntryService;
import nikita.webapp.service.interfaces.secondary.ICorrespondencePartService;
import nikita.webapp.service.interfaces.secondary.IPrecedenceService;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.*;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
@SuppressWarnings("unchecked")
public class RegistryEntryService
        extends NoarkService
        implements IRegistryEntryService {

    private static final Logger logger =
            LoggerFactory.getLogger(RegistryEntryService.class);
    private ICorrespondencePartService correspondencePartService;
    private IPrecedenceService precedenceService;
    private IRegistryEntryRepository registryEntryRepository;
    private IRegistryEntryHateoasHandler registryEntryHateoasHandler;

    public RegistryEntryService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            ICorrespondencePartService correspondencePartService,
            IPrecedenceService precedenceService,
            IRegistryEntryRepository registryEntryRepository,
            IRegistryEntryHateoasHandler registryEntryHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.correspondencePartService = correspondencePartService;
        this.precedenceService = precedenceService;
        this.registryEntryRepository = registryEntryRepository;
        this.registryEntryHateoasHandler = registryEntryHateoasHandler;
    }

    @Override
    public RegistryEntry save(@NotNull RegistryEntry registryEntry) {
        setNikitaEntityValues(registryEntry);
        setSystemIdEntityValues(registryEntry);
        setCreateEntityValues(registryEntry);
        checkDocumentMediumValid(registryEntry);
        registryEntry.setRecordDate(ZonedDateTime.now());
        File file = registryEntry.getReferenceFile();
        if (null != file) {
            long numberAssociated =
                    registryEntryRepository.countByReferenceFile(file) + 1;
            registryEntry.setRegistryEntryNumber((int) numberAssociated);
            registryEntry.setRecordId(file.getFileId() + "-" + numberAssociated);
        }
        registryEntryRepository.save(registryEntry);
        return registryEntry;
    }

    @Override
    public CorrespondencePartPersonHateoas
    getCorrespondencePartPersonAssociatedWithRegistryEntry(
            final String systemID) {
        return new CorrespondencePartPersonHateoas(
                (List<INikitaEntity>) (List) getRegistryEntryOrThrow(systemID).
                        getReferenceCorrespondencePartPerson());
    }

    @Override
    public CorrespondencePartInternalHateoas
    getCorrespondencePartInternalAssociatedWithRegistryEntry(
            final String systemID) {
        return new CorrespondencePartInternalHateoas(
                (List<INikitaEntity>) (List) getRegistryEntryOrThrow(systemID).
                        getReferenceCorrespondencePartInternal());
    }

    @Override
    public CorrespondencePartUnitHateoas
    getCorrespondencePartUnitAssociatedWithRegistryEntry(String systemID) {
        return new CorrespondencePartUnitHateoas(
                (List<INikitaEntity>) (List) getRegistryEntryOrThrow(systemID).
                        getReferenceCorrespondencePartUnit());
    }


    @Override
    public ResponseEntity<RegistryEntryHateoas> generateDefaultRegistryEntry(
            @NotNull final String caseFileSystemId) {
        RegistryEntry defaultRegistryEntry = new RegistryEntry();
        defaultRegistryEntry.setTitle(DEFAULT_TITLE + "RegistryEntry");
        defaultRegistryEntry.setDescription(DEFAULT_DESCRIPTION + " a CaseFile " +
                "with systemId [" + caseFileSystemId + "]");
        ZonedDateTime now = ZonedDateTime.now();
        defaultRegistryEntry.setRecordDate(now);
        defaultRegistryEntry.setDocumentDate(now);
        defaultRegistryEntry.setRecordStatus(TEST_RECORD_STATUS);
        defaultRegistryEntry.setRegistryEntryType(TEST_REGISTRY_ENTRY_TYPE);
        defaultRegistryEntry.setRecordYear(now.getYear());
        RegistryEntryHateoas registryEntryHateoas = new
                RegistryEntryHateoas(defaultRegistryEntry);
        registryEntryHateoasHandler.addLinksOnTemplate(registryEntryHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .body(registryEntryHateoas);
    }
    
    /**
     * Create a CorrespondencePartPerson object and associate it with the
     * identified registryEntry
     *
     * @param systemID           The systemId of the registryEntry object you want to
     *                           create an associated correspondencePartPerson for
     * @param correspondencePart The incoming correspondencePartPerson
     * @return The persisted CorrespondencePartPerson object wrapped as a
     * CorrespondencePartPersonHateoas object
     */
    @Override
    public CorrespondencePartPersonHateoas
    createCorrespondencePartPersonAssociatedWithRegistryEntry(
            @NotNull final String systemID,
            @NotNull final CorrespondencePartPerson correspondencePart) {
        return correspondencePartService.
                createNewCorrespondencePartPerson(correspondencePart,
                        getRegistryEntryOrThrow(systemID));
    }

    /**
     * Create a CorrespondencePartInternal object and associate it with the
     * identified registryEntry
     *
     * @param systemID           The systemId of the registryEntry object you want to
     *                           create an associated correspondencePartInternal for
     * @param correspondencePart The incoming correspondencePartInternal
     * @return The persisted CorrespondencePartInternal object wrapped as a
     * CorrespondencePartInternalHateoas object
     */
    @Override
    public CorrespondencePartInternalHateoas
    createCorrespondencePartInternalAssociatedWithRegistryEntry(
            String systemID, CorrespondencePartInternal correspondencePart) {
        return correspondencePartService.
                createNewCorrespondencePartInternal(correspondencePart,
                        getRegistryEntryOrThrow(systemID));
    }

    /**
     * Create a CorrespondencePartUnit object and associate it with the
     * identified registryEntry
     *
     * @param systemID           The systemId of the registryEntry object you want to
     *                           create an associated correspondencePartUnit for
     * @param correspondencePart The incoming correspondencePartUnit
     * @return The persisted CorrespondencePartUnit object wrapped as a
     * CorrespondencePartUnitHateoas object
     */
    @Override
    public CorrespondencePartUnitHateoas
    createCorrespondencePartUnitAssociatedWithRegistryEntry(
            String systemID, CorrespondencePartUnit correspondencePart) {
        return correspondencePartService.
                createNewCorrespondencePartUnit(correspondencePart,
                        getRegistryEntryOrThrow(systemID));
    }

    /**
     * Generate a Default CorrespondencePartUnit object that can be
     * associated with the identified RegistryEntry.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param registryEntrySystemId The systemId of the registryEntry object
     *                              you wish to create a templated object for
     * @return the CorrespondencePartUnit object wrapped as a
     * CorrespondencePartUnitHateoas object
     */
    @Override
    public CorrespondencePartInternalHateoas
    generateDefaultCorrespondencePartInternal(
            String registryEntrySystemId) {
        return correspondencePartService.
                generateDefaultCorrespondencePartInternal(registryEntrySystemId);
    }

    /**
     * Generate a Default CorrespondencePartUnit object that can be
     * associated with the identified RegistryEntry.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param registryEntrySystemId The systemId of the registryEntry object
     *                              you wish to create a templated object for
     * @return the CorrespondencePartUnit object wrapped as a
     * CorrespondencePartUnitHateoas object
     */
    @Override
    public CorrespondencePartPersonHateoas
    generateDefaultCorrespondencePartPerson(
            String registryEntrySystemId) {
        return correspondencePartService.
                generateDefaultCorrespondencePartPerson(registryEntrySystemId);
    }

    /**
     * Generate a Default CorrespondencePartUnit object that can be
     * associated with the identified RegistryEntry.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param registryEntrySystemId The systemId of the registryEntry object
     *                              you wish to create a templated object for
     * @return the CorrespondencePartUnit object wrapped as a
     * CorrespondencePartUnitHateoas object
     */
    @Override
    public CorrespondencePartUnitHateoas generateDefaultCorrespondencePartUnit(
            String registryEntrySystemId) {
        return correspondencePartService.
                generateDefaultCorrespondencePartUnit(
                        registryEntrySystemId);
    }

    // All READ operations
    public List<RegistryEntry> findRegistryEntryByOwnerPaginated(Integer top,
                                                                 Integer skip) {

        int maxPageSize = 10;
        if (top == null || top > maxPageSize) {
            top = maxPageSize;
        }
        if (skip == null) {
            skip = 0;
        }

        String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RegistryEntry> criteriaQuery = criteriaBuilder.createQuery(RegistryEntry.class);
        Root<RegistryEntry> from = criteriaQuery.from(RegistryEntry.class);
        CriteriaQuery<RegistryEntry> select = criteriaQuery.select(from);

        criteriaQuery.where(criteriaBuilder.equal(from.get("ownedBy"), loggedInUser));
        TypedQuery<RegistryEntry> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult(skip);
        typedQuery.setMaxResults(top);
        return typedQuery.getResultList();
    }

    // systemId
    public RegistryEntry findBySystemId(String systemId) {
        return getRegistryEntryOrThrow(systemId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResponseEntity<RegistryEntryHateoas> findAllRegistryEntryByCaseFile(
            CaseFile caseFile) {
        RegistryEntryHateoas registryEntryHateoas = new RegistryEntryHateoas(
                (List<INikitaEntity>)
                        (List) registryEntryRepository.
                                findByReferenceFile(caseFile));
        registryEntryHateoasHandler.addLinks(registryEntryHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .body(registryEntryHateoas);
    }

    @Override
    public Precedence createPrecedenceAssociatedWithRecord(
            String registryEntrySystemID, Precedence precedence) {

        RegistryEntry registryEntry = getRegistryEntryOrThrow(
                registryEntrySystemID);
        setNikitaEntityValues(precedence);
        setSystemIdEntityValues(precedence);
        // bidirectional relationship @ManyToMany, set both sides of relationship
        registryEntry.getReferencePrecedence().add(precedence);
        precedence.getReferenceRegistryEntry().add(registryEntry);
        return precedenceService.createNewPrecedence(precedence);

    }

    // All UPDATE operations

    /**
     * Updates a Record object in the database. First we try to locate the
     * Record object. If the Record object does not exist a
     * NoarkEntityNotFoundException exception is thrown that the caller has
     * to deal with.
     * <p>
     * After this the values you are allowed to update are copied from the
     * incomingRecord object to the existingRecord object and the existingRecord
     * object will be persisted to the database when the transaction boundary
     * is over.
     * <p>
     * Note, the version corresponds to the version number, when the object
     * was initially retrieved from the database. If this number is not the
     * same as the version number when re-retrieving the Record object from
     * the database a NoarkConcurrencyException is thrown. Note. This happens
     * <p>
     * Copies the values you are allowed to change, title, description, dueDate,
     * freedomAssessmentDate, loanedDate, loanedTo
     *
     * @param systemId              The systemId of the registryEntry object
     *                              you wish to update
     * @param incomingRegistryEntry The updated registryEntry object. Note
     *                              the values you are allowed to change are
     *                              copied from this object. This object is
     *                              not persisted.
     * @param version               The last known version number (derived
     *                              from an ETAG)
     * @return the updated registryEntry after being persisted to the database
     */
    @Override
    public RegistryEntry handleUpdate(
            @NotNull final String systemId, @NotNull final Long version,
            @NotNull final RegistryEntry incomingRegistryEntry) {
        RegistryEntry existingRegistryEntry = getRegistryEntryOrThrow(systemId);
        // Copy all the values you are allowed to copy ....
        updateTitleAndDescription(incomingRegistryEntry, existingRegistryEntry);
        if (null != incomingRegistryEntry.getDocumentMedium()) {
            existingRegistryEntry.setDocumentMedium(
                    incomingRegistryEntry.getDocumentMedium());
        }
        if (null != incomingRegistryEntry.getDocumentDate()) {
            existingRegistryEntry.setDocumentDate(
                    incomingRegistryEntry.getDocumentDate());
        }
        if (null != incomingRegistryEntry.getDueDate()) {
            existingRegistryEntry.setDueDate(
                    incomingRegistryEntry.getDueDate());
        }
        if (null != incomingRegistryEntry.getFreedomAssessmentDate()) {
            existingRegistryEntry.setFreedomAssessmentDate(
                    incomingRegistryEntry.getFreedomAssessmentDate());
        }
        if (null != incomingRegistryEntry.getLoanedDate()) {
            existingRegistryEntry.setLoanedDate(
                    incomingRegistryEntry.getLoanedDate());
        }
        if (null != incomingRegistryEntry.getLoanedTo()) {
            existingRegistryEntry.setLoanedTo(
                    incomingRegistryEntry.getLoanedTo());
        }
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingRegistryEntry.setVersion(version);
        registryEntryRepository.save(existingRegistryEntry);
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this, existingRegistryEntry));
        return existingRegistryEntry;
    }

    // All DELETE operations

    /**
     * Delete a RegistryEntry identified by the given systemId
     * <p>
     * Note. This assumes all children have also been deleted.
     *
     * @param registryEntrySystemId The systemId of the registryEntry object
     *                              you wish to delete
     */
    @Override
    public void deleteEntity(@NotNull String registryEntrySystemId) {
        registryEntryRepository.delete(
                getRegistryEntryOrThrow(registryEntrySystemId));
    }


    /**
     * Delete all objects belonging to the user identified by ownedBy
     *
     * @return the number of objects deleted
     */
    @Override
    public long deleteAllByOwnedBy() {
        return registryEntryRepository.deleteByOwnedBy(getUser());
    }
    // All helper methods

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid RegistryEntry back. If there is no
     * valid RegistryEntry, an exception is thrown
     *
     * @param registryEntrySystemId systemId of the registryEntry to find.
     * @return the registryEntry
     */
    protected RegistryEntry getRegistryEntryOrThrow(
            @NotNull String registryEntrySystemId) {
        RegistryEntry registryEntry =
                registryEntryRepository.findBySystemId(registryEntrySystemId);
        if (registryEntry == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " RegistryEntry, using systemId " + registryEntrySystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return registryEntry;
    }
}
