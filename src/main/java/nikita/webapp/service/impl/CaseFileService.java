package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.metadata.CaseStatus;
import nikita.common.repository.n5v5.ICaseFileRepository;
import nikita.common.repository.n5v5.admin.IAdministrativeUnitRepository;
import nikita.common.repository.nikita.IUserRepository;
import nikita.common.util.exceptions.NoarkAdministrativeUnitMemberException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.ICaseFileHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.ICaseFileService;
import nikita.webapp.service.interfaces.IRegistryEntryService;
import nikita.webapp.service.interfaces.ISequenceNumberGeneratorService;
import nikita.webapp.service.interfaces.metadata.ICaseStatusService;
import nikita.webapp.web.events.AfterNoarkEntityCreatedEvent;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.STATUS_OPEN;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.checkDocumentMediumValid;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.setNoarkEntityValues;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
public class CaseFileService
        extends NoarkService
        implements ICaseFileService {

    private static final Logger logger =
            LoggerFactory.getLogger(CaseFileService.class);

    private IRegistryEntryService registryEntryService;
    private ICaseFileRepository caseFileRepository;
    private ISequenceNumberGeneratorService numberGeneratorService;
    private IAdministrativeUnitRepository administrativeUnitRepository;
    private IUserRepository userRepository;
    private ICaseStatusService caseStatusService;
    private ICaseFileHateoasHandler caseFileHateoasHandler;

    public CaseFileService(EntityManager entityManager,
                           ApplicationEventPublisher applicationEventPublisher,
                           IRegistryEntryService registryEntryService,
                           ICaseFileRepository caseFileRepository,
                           ISequenceNumberGeneratorService numberGeneratorService,
                           IAdministrativeUnitRepository administrativeUnitRepository,
                           IUserRepository userRepository,
                           ICaseStatusService caseStatusService,
                           ICaseFileHateoasHandler caseFileHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.registryEntryService = registryEntryService;
        this.caseFileRepository = caseFileRepository;
        this.numberGeneratorService = numberGeneratorService;
        this.administrativeUnitRepository = administrativeUnitRepository;
        this.userRepository = userRepository;
        this.caseStatusService = caseStatusService;
        this.caseFileHateoasHandler = caseFileHateoasHandler;
    }

    @Override
    public CaseFile save(CaseFile caseFile) {
        setNoarkEntityValues(caseFile);
        checkDocumentMediumValid(caseFile);

        // If caseStatus is not set, set it to "Opprettet"
        checkCaseStatusUponCreation(caseFile);

        // If the caseResponsible isn't set, set it to the owner of
        // this object
        if (null == caseFile.getCaseResponsible()) {
            caseFile.setCaseResponsible(caseFile.getOwnedBy());
        }

        // Before assigning ownership make sure the owner is part of the
        // administrative unit
        AdministrativeUnit administrativeUnit =
                getAdministrativeUnitIfMemberOrThrow(caseFile);
        caseFile.setReferenceAdministrativeUnit(administrativeUnit);

        // Set case year
        Integer currentYear = ZonedDateTime.now().getYear();
        caseFile.setCaseYear(currentYear);
        caseFile.setCaseDate(ZonedDateTime.now());
        caseFile.setCaseSequenceNumber(getNextSequenceNumber(
                administrativeUnit));
        caseFile.setFileId(currentYear.toString() + "/" +
                caseFile.getCaseSequenceNumber());
        return caseFileRepository.save(caseFile);
    }

    @Override
    public CaseFileHateoas saveHateoas(CaseFile caseFile) {
        CaseFile caseFileNew = save(caseFile);
        CaseFileHateoas caseFileHateoas = new
                CaseFileHateoas(caseFileNew);
        caseFileHateoasHandler.addLinks(caseFileHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(
                        this, caseFileNew));
        return caseFileHateoas;
    }

    // systemId
    public CaseFile findBySystemId(String systemId) {
        return getCaseFileOrThrow(systemId);
    }

    @Override
    public RegistryEntry createRegistryEntryAssociatedWithCaseFile(
            @NotNull String fileSystemId,
            @NotNull RegistryEntry registryEntry) {
        CaseFile caseFile = getCaseFileOrThrow(fileSystemId);
        registryEntry.setReferenceFile(caseFile);
        caseFile.getReferenceRecord().add(registryEntry);
        return registryEntryService.save(registryEntry);
    }

    // All READ operations
    @Override
    public List<CaseFile> findCaseFileByOwnerPaginated(
            Integer top, Integer skip) {
        if (top == null || top > 10) {
            top = 10;
        }
        if (skip == null) {
            skip = 0;
        }

        String loggedInUser = SecurityContextHolder.getContext().
                getAuthentication().getName();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CaseFile> criteriaQuery =
                criteriaBuilder.createQuery(CaseFile.class);
        Root<CaseFile> from = criteriaQuery.from(CaseFile.class);
        CriteriaQuery<CaseFile> select = criteriaQuery.select(from);

        criteriaQuery.where(criteriaBuilder.
                equal(from.get("ownedBy"), loggedInUser));
        TypedQuery<CaseFile> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult(skip);
        typedQuery.setMaxResults(top);
        return typedQuery.getResultList();
    }

    @Override
    public ResponseEntity<CaseFileHateoas> findAllCaseFileBySeries(Series series) {
        CaseFileHateoas caseFileHateoas = new CaseFileHateoas(
                (List<INikitaEntity>)
                        (List) caseFileRepository.findByReferenceSeries(series));
        caseFileHateoasHandler.addLinks(caseFileHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .body(caseFileHateoas);
    }

    // All UPDATE operations

    /**
     * Updates a CaseFile object in the database. First we try to locate the
     * CaseFile object. If the CaseFile object does not exist a
     * NoarkEntityNotFoundException exception is thrown that the caller has
     * to deal with.
     * <br>
     * After this the values you are allowed to update are copied from the
     * incomingCaseFile object to the existingCaseFile object and the
     * existingCaseFile object will be persisted to the database when the
     * transaction boundary is over.
     * <p>
     * Note, the version corresponds to the version number, when the object
     * was initially retrieved from the database. If this number is not the
     * same as the version number when re-retrieving the CaseFile object from
     * the database a NoarkConcurrencyException is thrown. Note. This happens
     * when the call to CaseFile.setVersion() occurs.
     *
     * @param systemId         systemId of the incoming caseFile object
     * @param version          ETAG value
     * @param incomingCaseFile the incoming caseFile
     * @return the updated caseFile object after it is persisted
     */
    @Override
    public CaseFile handleUpdate(@NotNull final String systemId,
                                 @NotNull final Long version,
                                 @NotNull final CaseFile incomingCaseFile) {
        CaseFile existingCaseFile = getCaseFileOrThrow(systemId);
        // Copy all the values you are allowed to copy ....
        updateTitleAndDescription(incomingCaseFile, existingCaseFile);
        updateValues(incomingCaseFile, existingCaseFile);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingCaseFile.setVersion(version);
        return caseFileRepository.save(existingCaseFile);
    }

    // All DELETE operations
    @Override
    public void deleteEntity(@NotNull String caseFileSystemId) {
        CaseFile caseFile = getCaseFileOrThrow(caseFileSystemId);
        caseFileRepository.delete(caseFile);
    }


    /**
     * Delete all objects belonging to the user identified by ownedBy
     *
     * @return the number of objects deleted
     */
    @Override
    public long deleteAllByOwnedBy() {
        return caseFileRepository.deleteByOwnedBy(getUser());
    }
    // All HELPER operations

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid CaseFile back. If there is no valid
     * CaseFile, an exception is thrown
     *
     * @param caseFileSystemId systemId of caseFile to retrieve
     * @return the caseFile
     */
    protected CaseFile getCaseFileOrThrow(@NotNull String caseFileSystemId) {
        CaseFile caseFile = caseFileRepository.findBySystemId(caseFileSystemId);
        if (caseFile == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " CaseFile, using systemId " + caseFileSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return caseFile;
    }

    /**
     * Gets the next sequence number for the given administrativeUnit and
     * increments it by one before persisting it back to the database.
     * <p>
     * If no administrativeUnit is present (==null), it uses a default
     * administrativeUnit value. Note this should probably be removed once
     * we have proper support for administrativeUnit in place
     * <p>
     * TODO: Recheck this issue.
     *
     * @param administrativeUnit The administrativeUnit
     * @return the sequence number
     */
    protected Integer getNextSequenceNumber(AdministrativeUnit
                                                    administrativeUnit) {
        return numberGeneratorService.getNextSequenceNumber(administrativeUnit);
    }

    public CaseFileHateoas generateDefaultCaseFile() {
        CaseFile defaultCaseFile = new CaseFile();
        defaultCaseFile.setReferenceCaseFileStatus(
                caseStatusService.generateDefaultCaseStatus());
        defaultCaseFile.setCaseResponsible(SecurityContextHolder.getContext().
                getAuthentication().getName());
        defaultCaseFile.setCaseDate(ZonedDateTime.now());
        defaultCaseFile.setTitle(TEST_TITLE);
        defaultCaseFile.setOfficialTitle(TEST_TITLE);
        defaultCaseFile.setDescription(TEST_DESCRIPTION);
        defaultCaseFile.setCaseStatus(STATUS_OPEN);

        CaseFileHateoas caseFileHateoas = new
                CaseFileHateoas(defaultCaseFile);
        caseFileHateoasHandler.addLinksOnNew(caseFileHateoas,
                new Authorisation());
        return caseFileHateoas;
    }


    /**
     * Internal helper method. Find the administrativeUnit identified by the
     * given systemId or throw a NoarkEntityNotFoundException. Then check that
     * the user is a member of that administrativeUnit. If they are not throw a
     * NoarkAdministrativeUnitMemberException.
     * <p>
     * Note this method will return a non-null administrativeUnit. An
     * exception is thrown otherwise.
     *
     * @param caseFile The caseFile
     * @return the administrativeUnit
     */
    private AdministrativeUnit getAdministrativeUnitIfMemberOrThrow(
            CaseFile caseFile) {

        Optional<User> userOptional =
                userRepository.findByUsername(caseFile.getOwnedBy());

        AdministrativeUnit administrativeUnit;

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            administrativeUnit = getAdministrativeUnitOrThrow(user);
            checkOwnerMemberAdministrativeUnit(user, administrativeUnit);
        } else {
            throw new NoarkEntityNotFoundException(
                    "Could not find user with systemID [" +
                            caseFile.getOwnedBy() + "]");
        }

        userOptional = userRepository.findByUsername(
                caseFile.getCaseResponsible());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            checkCaseResponsibleMemberAdministrativeUnit(user,
                    administrativeUnit);
        } else {
            throw new NoarkEntityNotFoundException(
                    "Could not find user with systemID [" +
                            caseFile.getCaseResponsible() + "]");
        }
        return administrativeUnit;
    }

    private void checkOwnerMemberAdministrativeUnit(
            User user, AdministrativeUnit administrativeUnit) {

        Set<User> users = administrativeUnit.getUsers();
        // Check that the owner is part of the administrativeUnit
        if (!users.contains(user)) {
            throw new NoarkAdministrativeUnitMemberException(
                    "User [" + user.getUsername() + "] is not a member " +
                            "of the administrativeUnit with systemID [" +
                            administrativeUnit.getSystemId() + "] when " +
                            "assigning ownership field.");
        }
    }

    private void checkCaseResponsibleMemberAdministrativeUnit(
            User user, AdministrativeUnit administrativeUnit) {
        // Check that the person responsible is part of the administrativeUnit
        Set<User> users = administrativeUnit.getUsers();
        if (!users.contains(user)) {
            throw new NoarkAdministrativeUnitMemberException(
                    "User [" + user.getSystemId() + "] is " +
                            "not a member  of the administrativeUnit " +
                            "with systemID [" +
                            administrativeUnit.getSystemId() + "] when " +
                            "assigning caseFile responsible field.");
        }
    }

    /**
     * Internal helper method. Find the administrativeUnit identified for the
     * given user or throw a NoarkEntityNotFoundException. Note this method
     * will return a non-null administrativeUnit. An exception is thrown
     * otherwise.
     *
     * @param user the user you want to retrieve an associated
     *             administrativeUnit
     * @return the administrativeUnit
     */
    private AdministrativeUnit getAdministrativeUnitOrThrow(User user) {

        Set<User> users = new HashSet<>();
        users.add(user);
        Optional<AdministrativeUnit> administrativeUnit =
                administrativeUnitRepository.
                        findByUsersInAndDefaultAdministrativeUnit(
                                users, true);
        if (administrativeUnit.isPresent()) {
            return administrativeUnit.get();
        } else {
            String info =
                    INFO_CANNOT_FIND_OBJECT +
                            " AdministrativeUnit associated with user " +
                            user.toString();
            logger.warn(info);
            throw new NoarkEntityNotFoundException(info);
        }
    }

    /**
     * Update values of an existing CaseFile object.
     * <p>
     * Note: caseStatus is not nullable
     *
     * @param incomingCaseFile the incoming caseFile
     * @param existingCaseFile the existing caseFile
     */
    private void updateValues(@NotNull final CaseFile incomingCaseFile,
                              @NotNull final CaseFile existingCaseFile) {
        existingCaseFile.setRecordsManagementUnit(
                incomingCaseFile.getRecordsManagementUnit());
        existingCaseFile.setCaseResponsible(
                incomingCaseFile.getCaseResponsible());
        existingCaseFile.setOfficialTitle(
                incomingCaseFile.getOfficialTitle());
        if (null != incomingCaseFile.getCaseStatus()) {
            existingCaseFile.setCaseStatus(
                    incomingCaseFile.getCaseStatus());
        }
    }

    private void checkCaseStatusUponCreation(CaseFile caseFile) {

        // Set to default value if no value has been set.
        // 1. See if there is a default value set in the database for caseStatus
        // 2. If no default value is set, use a system defined default value
        if (null == caseFile.getCaseStatus()) {
            CaseStatus caseStatus =
                    caseStatusService.generateDefaultCaseStatus();
            if (null == caseStatus) {
                caseFile.setCaseStatus(STATUS_OPEN);
            } else {
                caseFile.setCaseStatus(caseStatus.getCode());
            }
        } else {
            // Make sure that the caseStatus that is set is in the list of
            // default values. If it is not in the list throw a
            // NoarkIllegalValueException
//
//            Optional<CaseStatus> caseStatus =
//                    caseStatusService.getCaseStatusByDescription();
//            if()
        }
    }
}
