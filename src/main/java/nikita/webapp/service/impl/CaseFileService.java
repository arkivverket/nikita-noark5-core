package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.RecordNoteHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.RegistryEntryHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PrecedenceHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.CaseStatus;
import nikita.common.model.noark5.v5.secondary.Precedence;
import nikita.common.repository.n5v5.ICaseFileRepository;
import nikita.common.repository.nikita.IUserRepository;
import nikita.common.util.exceptions.NoarkAdministrativeUnitMemberException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.secondary.IPrecedenceHateoasHandler;
import nikita.webapp.hateoas.interfaces.ICaseFileHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.ICaseFileService;
import nikita.webapp.service.interfaces.IRegistryEntryService;
import nikita.webapp.service.interfaces.ISequenceNumberGeneratorService;
import nikita.webapp.service.interfaces.admin.IAdministrativeUnitService;
import nikita.webapp.service.interfaces.casehandling.IRecordNoteService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.secondary.IPrecedenceService;
import nikita.webapp.web.events.AfterNoarkEntityCreatedEvent;
import nikita.webapp.web.events.AfterNoarkEntityDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CASE_STATUS;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.validateDocumentMedium;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
public class CaseFileService
        extends NoarkService
        implements ICaseFileService {

    private static final Logger logger =
            LoggerFactory.getLogger(CaseFileService.class);

    private IRegistryEntryService registryEntryService;
    private IRecordNoteService recordNoteService;
    private ICaseFileRepository caseFileRepository;
    private ISequenceNumberGeneratorService numberGeneratorService;
    private IAdministrativeUnitService administrativeUnitService;
    private IPrecedenceService precedenceService;
    private IUserRepository userRepository;
    private IMetadataService metadataService;
    private IPrecedenceHateoasHandler precedenceHateoasHandler;
    private ICaseFileHateoasHandler caseFileHateoasHandler;

    public CaseFileService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IRegistryEntryService registryEntryService,
            IRecordNoteService recordNoteService,
            ICaseFileRepository caseFileRepository,
            ISequenceNumberGeneratorService numberGeneratorService,
            IAdministrativeUnitService administrativeUnitService,
            IPrecedenceService precedenceService,
            IUserRepository userRepository,
            IMetadataService metadataService,
            IPrecedenceHateoasHandler precedenceHateoasHandler,
            ICaseFileHateoasHandler caseFileHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.registryEntryService = registryEntryService;
        this.recordNoteService = recordNoteService;
        this.caseFileRepository = caseFileRepository;
        this.numberGeneratorService = numberGeneratorService;
        this.administrativeUnitService = administrativeUnitService;
        this.precedenceService = precedenceService;
        this.userRepository = userRepository;
        this.metadataService = metadataService;
        this.metadataService = metadataService;
        this.precedenceHateoasHandler =  precedenceHateoasHandler;
        this.caseFileHateoasHandler = caseFileHateoasHandler;
    }

    @Override
    public CaseFile save(CaseFile caseFile) {
        validateDocumentMedium(metadataService, caseFile);

        validateCaseStatus(caseFile);

        // If the caseResponsible isn't set, set it to the owner of
        // this object
        if (null == caseFile.getCaseResponsible()) {
            caseFile.setCaseResponsible(getUser());
        }

        // Before assigning ownership make sure the owner is part of the
        // administrative unit
        AdministrativeUnit administrativeUnit =
                getAdministrativeUnitIfMemberOrThrow(caseFile);
        caseFile.setReferenceAdministrativeUnit(administrativeUnit);

        // Set case year
        Integer currentYear = OffsetDateTime.now().getYear();
        caseFile.setCaseYear(currentYear);
        caseFile.setCaseDate(OffsetDateTime.now());
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
    public PrecedenceHateoas findAllPrecedenceForCaseFile(
            @NotNull final String systemID) {
        CaseFile caseFile = getCaseFileOrThrow(systemID);
        PrecedenceHateoas precedenceHateoas =
                new PrecedenceHateoas((List<INoarkEntity>)
                        (List) caseFile.getReferencePrecedence());
        precedenceHateoasHandler
            .addLinks(precedenceHateoas, new Authorisation());
        setOutgoingRequestHeader(precedenceHateoas);
        return precedenceHateoas;
    }

    @Override
    public ResponseEntity<RecordNoteHateoas> findAllRecordNoteToCaseFile(
            @NotNull final String caseFileSystemId) {
        return recordNoteService.findAllRecordNoteByCaseFile(
                getCaseFileOrThrow(caseFileSystemId));
    }

    @Override
    public ResponseEntity<RegistryEntryHateoas> findAllRegistryEntryToCaseFile(
            @NotNull final String caseFileSystemId) {
        return registryEntryService.findAllRegistryEntryByCaseFile(
                getCaseFileOrThrow(caseFileSystemId));
    }

    @Override
    public PrecedenceHateoas createPrecedenceAssociatedWithFile(
            String caseFileSystemID, Precedence precedence) {

        CaseFile caseFile = getCaseFileOrThrow(caseFileSystemID);
        // bidirectional relationship @ManyToMany, set both sides of relationship
        caseFile.getReferencePrecedence().add(precedence);
        precedence.getReferenceCaseFile().add(caseFile);
        return precedenceService.createNewPrecedence(precedence);
    }

    /**
     * Create a RegistryEntry associated with a ClassFile.
     * <p>
     * First find the caseFile to associate with, set the references between
     * them before calling the RegistryEntryService save, which will add all
     * required values.
     *
     * @param fileSystemId  systemID of the caseFile
     * @param registryEntry the incoming RegistryEntry object
     * @return he registryEntry object wrapped as a Hateoas object, further
     * wrapped as a ResponseEntity
     */
    @Override
    public RegistryEntry createRegistryEntryAssociatedWithCaseFile(
            @NotNull String fileSystemId,
            @NotNull RegistryEntry registryEntry) {
        CaseFile caseFile = getCaseFileOrThrow(fileSystemId);
        registryEntry.setReferenceFile(caseFile);
        caseFile.getReferenceRecord().add(registryEntry);
        return registryEntryService.save(registryEntry);
    }

    /**
     * Create a RecordNote associated with a ClassFile.
     * <p>
     * First find the caseFile to associate with, set the references between
     * them before calling the RecordNoteService save, which will add all
     * required values.
     *
     * @param fileSystemId systemID of the caseFile
     * @param recordNote the incoming RecordNote object
     *
     * @return the recordNote object wrapped as a Hateoas object, further
     * wrapped as a ResponseEntity
     */
    @Override
    public ResponseEntity<RecordNoteHateoas> createRecordNoteToCaseFile(
            @NotNull String fileSystemId,
            @NotNull RecordNote recordNote) {
        CaseFile caseFile = getCaseFileOrThrow(fileSystemId);
        recordNote.setReferenceFile(caseFile);
        caseFile.getReferenceRecord().add(recordNote);
        return recordNoteService.save(recordNote);
    }

    /**
     * Generate a Default RecordNote object that can be associated with the
     * identified CaseFile.
     * <br>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param caseFileSystemId The systemId of the CaseFile object you wish to
     *                         generate a default RecordNote for
     * @return the RecordNote object wrapped as a RecordNoteHateoas object
     */
    @Override
    public ResponseEntity<RecordNoteHateoas> generateDefaultRecordNote(
            @NotNull String caseFileSystemId) {
        return recordNoteService.generateDefaultRecordNote(caseFileSystemId);
    }

    /**
     * Generate a Default RegistryEntry object that can be associated with the
     * identified CaseFile.
     * <br>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param caseFileSystemId The systemId of the CaseFile object you wish to
     *                         generate a default RegistryEntry for
     * @return the RegistryEntry object wrapped as a RegistryEntryHateoas
     * object, further wrapped as a ResponseEntity object
     */
    @Override
    public ResponseEntity<RegistryEntryHateoas> generateDefaultRegistryEntry(
            @NotNull String caseFileSystemId) {
        return registryEntryService.generateDefaultRegistryEntry(
                caseFileSystemId);
    }

    @Override
    public PrecedenceHateoas generateDefaultPrecedence(String systemID) {
        return precedenceService.generateDefaultPrecedence();
    }

    // All READ operations
    @Override
    public List<CaseFile> findCaseFileByOwnerPaginated(
            Integer top, Integer skip) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CaseFile> criteriaQuery =
                criteriaBuilder.createQuery(CaseFile.class);
        Root<CaseFile> from = criteriaQuery.from(CaseFile.class);
        CriteriaQuery<CaseFile> select = criteriaQuery.select(from);

        criteriaQuery.where(criteriaBuilder.equal(from.get("ownedBy"), getUser()));
        TypedQuery<CaseFile> typedQuery = entityManager.createQuery(select);
        return typedQuery.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResponseEntity<CaseFileHateoas> findAllCaseFileBySeries(Series series) {
        CaseFileHateoas caseFileHateoas = new CaseFileHateoas(
                (List<INoarkEntity>)
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
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityDeletedEvent(this, caseFile));
        deleteEntity(caseFile);
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
     * @param systemId systemId of caseFile to retrieve
     * @return the caseFile
     */
    protected CaseFile getCaseFileOrThrow(@NotNull String systemId) {
        CaseFile caseFile = caseFileRepository.
                findBySystemId(UUID.fromString(systemId));
        if (caseFile == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " CaseFile, using systemId " + systemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return caseFile;
    }

    /**
     * Gets the next sequence number for the given administrativeUnit and
     * increments it by one before persisting it back to the database.
     *
     * @param administrativeUnit The administrativeUnit
     * @return the sequence number
     */
    protected Integer getNextSequenceNumber(AdministrativeUnit
                                                    administrativeUnit) {
        return numberGeneratorService.getNextCaseFileSequenceNumber(administrativeUnit);
    }

    public CaseFileHateoas generateDefaultCaseFile() {
        CaseFile defaultCaseFile = new CaseFile();
        defaultCaseFile.setCaseResponsible(getUser());
        defaultCaseFile.setCaseDate(OffsetDateTime.now());
        CaseStatus caseStatus = (CaseStatus)
            metadataService.findValidMetadataByEntityTypeOrThrow
                (CASE_STATUS, DEFAULT_CASE_STATUS_CODE, null);
        defaultCaseFile.setCaseStatus(caseStatus);

        CaseFileHateoas caseFileHateoas = new
                CaseFileHateoas(defaultCaseFile);
        caseFileHateoasHandler.addLinksOnTemplate(caseFileHateoas,
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

        //assumes ownedby is set!!!! need to get user!!!
        Optional<User> userOptional =
                userRepository.findByUsername(getUser());

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

        userOptional = userRepository.findByUsername(getUser());

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
        return administrativeUnitService.getAdministrativeUnitOrThrow(user);
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
        existingCaseFile.setPublicTitle(
                incomingCaseFile.getPublicTitle());
        if (null != incomingCaseFile.getCaseStatus()) {
            existingCaseFile.setCaseStatus(
                    incomingCaseFile.getCaseStatus());
        }
    }

    private void validateCaseStatus(CaseFile caseFile) {
        if (null != caseFile.getCaseStatus()) {
            CaseStatus caseStatus = (CaseStatus) metadataService
                    .findValidMetadataByEntityTypeOrThrow(
                            CASE_STATUS,
                            caseFile.getCaseStatus());
            caseFile.setCaseStatus(caseStatus);
        }
    }
}
