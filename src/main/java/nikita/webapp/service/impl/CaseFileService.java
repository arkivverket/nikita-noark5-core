package nikita.webapp.service.impl;

import nikita.common.model.nikita.PatchMerge;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileExpansionHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.RecordNoteHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.RegistryEntryHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PrecedenceHateoas;
import nikita.common.model.noark5.v5.metadata.CaseStatus;
import nikita.common.model.noark5.v5.secondary.Precedence;
import nikita.common.repository.n5v5.ICaseFileRepository;
import nikita.common.repository.nikita.IUserRepository;
import nikita.common.util.exceptions.NoarkAdministrativeUnitMemberException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.ICaseFileHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.interfaces.ICaseFileService;
import nikita.webapp.service.interfaces.IRegistryEntryService;
import nikita.webapp.service.interfaces.ISequenceNumberGeneratorService;
import nikita.webapp.service.interfaces.admin.IAdministrativeUnitService;
import nikita.webapp.service.interfaces.casehandling.IRecordNoteService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.odata.IODataService;
import nikita.webapp.service.interfaces.secondary.IPrecedenceService;
import nikita.webapp.web.events.AfterNoarkEntityCreatedEvent;
import nikita.webapp.web.events.AfterNoarkEntityDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.time.OffsetDateTime.now;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.validateDocumentMedium;

@Service
public class CaseFileService
        extends NoarkService
        implements ICaseFileService {

    private static final Logger logger =
            LoggerFactory.getLogger(CaseFileService.class);

    private final IRegistryEntryService registryEntryService;
    private final IRecordNoteService recordNoteService;
    private final ICaseFileRepository caseFileRepository;
    private final ISequenceNumberGeneratorService numberGeneratorService;
    private final IAdministrativeUnitService administrativeUnitService;
    private final IPrecedenceService precedenceService;
    private final IUserRepository userRepository;
    private final IMetadataService metadataService;
    private final ICaseFileHateoasHandler caseFileHateoasHandler;

    public CaseFileService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IODataService odataService,
            IPatchService patchService,
            IRegistryEntryService registryEntryService,
            IRecordNoteService recordNoteService,
            ICaseFileRepository caseFileRepository,
            ISequenceNumberGeneratorService numberGeneratorService,
            IAdministrativeUnitService administrativeUnitService,
            IPrecedenceService precedenceService,
            IUserRepository userRepository,
            IMetadataService metadataService,
            ICaseFileHateoasHandler caseFileHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService,
                odataService);
        this.registryEntryService = registryEntryService;
        this.recordNoteService = recordNoteService;
        this.caseFileRepository = caseFileRepository;
        this.numberGeneratorService = numberGeneratorService;
        this.administrativeUnitService = administrativeUnitService;
        this.precedenceService = precedenceService;
        this.userRepository = userRepository;
        this.metadataService = metadataService;
        this.caseFileHateoasHandler = caseFileHateoasHandler;
    }

    @Override
    @Transactional
    public CaseFileHateoas save(CaseFile caseFile) {
        processCaseFileBeforeSave(caseFile);
        return packAsHateoas(caseFileRepository.save(caseFile));
    }

    /**
     * Expand a file to a casefile object and save it to the database
     * <p>
     * This method 'cheats' a little as the session has a File object with
     * the identified systemId and it is not possible to cast File to
     * CaseFile and save it. So a native query is created that inserts the
     * required values to the casefile table. Then a CaseFile object is
     * created with all the required values and links. For a client, this
     * CaseFile returned looks like a normal caseFile. It's important to note
     * this approach as it deviates with the 'normal' approach.
     *
     * @param file       the File object to expand
     * @param patchMerge the incoming values to use when expanding
     * @return a CaseFile object that holds the values of the persisted
     * CaseFile
     */
    @Override
    @Transactional
    public CaseFileHateoas expandFileAsCaseFileHateoas(
            File file, PatchMerge patchMerge) {
        CaseFile caseFile = new CaseFile(file);
        CaseStatus caseStatus = getCaseStatus(patchMerge);
        caseFile.setCaseStatus(caseStatus);
        String caseResponsible = (String) patchMerge.getValue(CASE_RESPONSIBLE);
        if (null != caseResponsible && !caseResponsible.isEmpty()) {
            caseFile.setCaseResponsible(caseResponsible);
        }
        if (null != patchMerge.getValue(CASE_DATE)) {
            caseFile.setCaseDate((OffsetDateTime)
                    patchMerge.getValue(CASE_DATE));
        }
        processCaseFileBeforeSave(caseFile);
        entityManager.createNativeQuery(
                "INSERT INTO " + TABLE_CASE_FILE +
                        "(" + SYSTEM_ID_ENG + ", " + CASE_DATE_ENG + ", " +
                        CASE_RESPONSIBLE_ENG + ", " + CASE_STATUS_CODE_ENG +
                        ", " + CASE_STATUS_CODE_NAME_ENG + "," + CASE_YEAR_ENG +
                        ", " + CASE_SEQUENCE_NUMBER_ENG + ", " +
                        CASE_RECORDS_MANAGEMENT_UNIT_ENG +
                        ") VALUES (?,?,?,?,?,?,?,?)")
                .setParameter(1, file.getSystemIdAsString())
                .setParameter(2, caseFile.getCaseDate())
                .setParameter(3, caseFile.getCaseResponsible())
                .setParameter(4, caseFile.getCaseStatus().getCode())
                .setParameter(5, caseFile.getCaseStatus().getCodeName())
                .setParameter(6, caseFile.getCaseYear())
                .setParameter(7, caseFile.getCaseSequenceNumber())
                .setParameter(8, caseFile.getRecordsManagementUnit())
                .executeUpdate();
        file.setFileId(caseFile.getFileId());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(this, caseFile));
        return packAsHateoas(caseFile);
    }

    public CaseFileHateoas findBySystemId(@NotNull final UUID systemId) {
        return packAsHateoas(getCaseFileOrThrow(systemId));
    }

    @Override
    public PrecedenceHateoas findAllPrecedenceForCaseFile(
            @NotNull final UUID systemId) {
        getCaseFileOrThrow(systemId);
        return (PrecedenceHateoas) odataService.processODataQueryGet();
    }

    @Override
    public RecordNoteHateoas findAllRecordNoteToCaseFile(
            @NotNull final UUID systemId) {
        getCaseFileOrThrow(systemId);
        return (RecordNoteHateoas) odataService.processODataQueryGet();
    }

    @Override
    public RegistryEntryHateoas findAllRegistryEntryToCaseFile(
            @NotNull final UUID systemId) {
        getCaseFileOrThrow(systemId);
        return (RegistryEntryHateoas) odataService.processODataQueryGet();
    }

    @Override
    @Transactional
    public PrecedenceHateoas createPrecedenceAssociatedWithFile(
            @NotNull final UUID systemId,
            @NotNull final Precedence precedence) {
        CaseFile caseFile = getCaseFileOrThrow(systemId);
        caseFile.addPrecedence(precedence);
        return precedenceService.createNewPrecedence(precedence);
    }

    /**
     * Create a RegistryEntry associated with a ClassFile.
     * <p>
     * First find the caseFile to associate with, set the references between
     * them before calling the RegistryEntryService save, which will add all
     * required values.
     *
     * @param systemId      systemId of the caseFile
     * @param registryEntry the incoming RegistryEntry object
     * @return he registryEntry object wrapped as a Hateoas object
     */
    @Override
    @Transactional
    public RegistryEntryHateoas createRegistryEntryAssociatedWithCaseFile(
            @NotNull final UUID systemId,
            @NotNull final RegistryEntry registryEntry) {
        CaseFile caseFile = getCaseFileOrThrow(systemId);
        registryEntry.setReferenceFile(caseFile);
        caseFile.getReferenceRecord().add(registryEntry);
        return registryEntryService.save(registryEntry);
    }

    /**
     * Create a (child) CaseFile associated with a (parent) ClassFile.
     * <p>
     * First find the parent caseFile to associate with, add all required
     * values to the child case file and set the references between them.
     *
     * @param parentSystemId systemId of the parent caseFile
     * @param caseFile       the incoming CaseFile object
     * @return the caseFile object wrapped as a Hateoas object
     */
    @Override
    @Transactional
    public CaseFileHateoas createCaseFileToCaseFile(
            @NotNull final UUID parentSystemId,
            @NotNull final CaseFile caseFile) {
        CaseFile parentCaseFile = getCaseFileOrThrow(parentSystemId);
        processCaseFileBeforeSave(caseFile);
        parentCaseFile.addFile(caseFile);
        caseFileRepository.save(caseFile);
        return packAsHateoas(caseFile);
    }

    /**
     * Create a RecordNote associated with a ClassFile.
     * <p>
     * First find the caseFile to associate with, set the references between
     * them before calling the RecordNoteService save, which will add all
     * required values.
     *
     * @param systemId   systemId of the caseFile
     * @param recordNote the incoming RecordNote object
     * @return the recordNote object wrapped as a Hateoas object
     */
    @Override
    @Transactional
    public RecordNoteHateoas createRecordNoteToCaseFile(
            @NotNull final UUID systemId,
            @NotNull final RecordNote recordNote) {
        CaseFile caseFile = getCaseFileOrThrow(systemId);
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
     * @param systemId The systemId of the CaseFile object you wish to
     *                 generate a default RecordNote for
     * @return the RecordNote object wrapped as a RecordNoteHateoas object
     */
    @Override
    public RecordNoteHateoas generateDefaultRecordNote(
            @NotNull final UUID systemId) {
        return recordNoteService.generateDefaultRecordNote(systemId);
    }

    /**
     * Generate a Default RegistryEntry object that can be associated with the
     * identified CaseFile.
     * <br>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param systemId The systemId of the CaseFile object you wish to
     *                 generate a default RegistryEntry for
     * @return the RegistryEntry object wrapped as a RegistryEntryHateoas
     * object
     */
    @Override
    public RegistryEntryHateoas generateDefaultRegistryEntry(
            @NotNull final UUID systemId) {
        return registryEntryService.generateDefaultRegistryEntry(
                systemId);
    }

    @Override
    public PrecedenceHateoas generateDefaultPrecedence(
            @NotNull final UUID systemId) {
        return precedenceService.generateDefaultPrecedence();
    }

    // All READ operations
    @Override
    public CaseFileHateoas findAll() {
        return (CaseFileHateoas) odataService.processODataQueryGet();
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
    @Transactional
    public CaseFileHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final Long version,
            @NotNull final CaseFile incomingCaseFile) {
        CaseFile existingCaseFile = getCaseFileOrThrow(systemId);
        // Copy all the values you are allowed to copy ....
        updateTitleAndDescription(incomingCaseFile, existingCaseFile);
        updateValues(incomingCaseFile, existingCaseFile);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingCaseFile.setVersion(version);
        return packAsHateoas(existingCaseFile);
    }

    // All DELETE operations
    @Override
    @Transactional
    public void deleteEntity(@NotNull final UUID systemId) {
        CaseFile caseFile = getCaseFileOrThrow(systemId);
        // Delete all precedence associated with the CaseFile. If the
        // precedence is associated with another RegistryEntry or CaseFile
        // the precedence object cannot be deleted. This event is logged.
        for (Precedence precedence : caseFile.getReferencePrecedence()) {
            precedence.removeCaseFile(caseFile);
            if (!precedenceService.deletePrecedenceIfNotEmpty(precedence)) {
                logger.info("Precedence is associated with another CaseFile " +
                        "or RegistryEntry and cannot be deleted at this time");
            }
        }
        caseFileRepository.delete(caseFile);
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityDeletedEvent(this, caseFile));
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     */
    @Override
    @Transactional
    public void deleteAllByOwnedBy() {
        caseFileRepository.deleteByOwnedBy(getUser());
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
    protected CaseFile getCaseFileOrThrow(@NotNull final UUID systemId) {
        CaseFile caseFile = caseFileRepository.
                findBySystemId(systemId);
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
    @Transactional
    protected Integer getNextSequenceNumber(AdministrativeUnit
                                                    administrativeUnit) {
        return numberGeneratorService
                .getNextCaseFileSequenceNumber(administrativeUnit);
    }

    public CaseFileHateoas generateDefaultCaseFile() {
        CaseFile defaultCaseFile = new CaseFile();
        defaultCaseFile.setCaseResponsible(getUser());
        defaultCaseFile.setCaseDate(now());
        defaultCaseFile.setVersion(-1L, true);
        CaseStatus caseStatus = (CaseStatus)
                metadataService.findValidMetadataByEntityTypeOrThrow
                        (CASE_STATUS, DEFAULT_CASE_STATUS_CODE, null);
        defaultCaseFile.setCaseStatus(caseStatus);
        return packAsHateoas(defaultCaseFile);
    }

    /**
     * Create a CaseFileExpansionHateoas that can be used when expanding a
     * File to a CaseFile. None of the File attributes should be present in
     * the returned payload. So we have a special approach that can be used to
     * achieve this.
     *
     * @return CaseFileExpansionHateoas
     */
    @Override
    public CaseFileExpansionHateoas generateDefaultExpandedCaseFile() {
        CaseFile caseFile = new CaseFile();
        CaseStatus caseStatus = new CaseStatus(TEMPLATE_CASE_STATUS_CODE,
                TEMPLATE_CASE_STATUS_NAME);
        caseFile.setCaseStatus(caseStatus);
        caseFile.setCaseResponsible(getUser());
        caseFile.setCaseDate(now());
        caseFile.setVersion(-1L, true);
        return packAsCaseFileExpansionHateoas(caseFile);
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
                    "Could not find user with systemId [" +
                            caseFile.getOwnedBy() + "]");
        }

        userOptional = userRepository.findByUsername(getUser());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            checkCaseResponsibleMemberAdministrativeUnit(user,
                    administrativeUnit);
        } else {
            throw new NoarkEntityNotFoundException(
                    "Could not find user with systemId [" +
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
                            "of the administrativeUnit with systemId [" +
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
                            "with systemId [" +
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
                    .findValidMetadata(caseFile.getCaseStatus());
            caseFile.setCaseStatus(caseStatus);
        }
    }

    private CaseFileHateoas packAsHateoas(CaseFile caseFile) {
        CaseFileHateoas caseFileHateoas = new CaseFileHateoas(caseFile);
        applyLinksAndHeader(caseFileHateoas, caseFileHateoasHandler);
        return caseFileHateoas;
    }

    private CaseFileExpansionHateoas
    packAsCaseFileExpansionHateoas(CaseFile caseFile) {
        CaseFileExpansionHateoas caseFileHateoas =
                new CaseFileExpansionHateoas(caseFile);
        applyLinksAndHeader(caseFileHateoas, caseFileHateoasHandler);
        return caseFileHateoas;
    }

    private void processCaseFileBeforeSave(CaseFile caseFile) {
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

        if (isOpen(caseFile)) {
            if (null == caseFile.getCaseDate()) {
                caseFile.setCaseDate(now());
            }
        }
        // Set case year
        Integer currentYear = now().getYear();
        caseFile.setCaseYear(currentYear);
        caseFile.setCaseSequenceNumber(getNextSequenceNumber(
                administrativeUnit));
        caseFile.setFileId(currentYear + "/" +
                caseFile.getCaseSequenceNumber());
    }

    /**
     * is this caseFile open. A caseFile is open if it does not have a
     * caseStatus value equal to 'A'
     *
     * @param caseFile the CaseFile object to check
     * @return true if open, false if closed
     */
    private boolean isOpen(@NotNull CaseFile caseFile) {
        CaseStatus caseStatus = caseFile.getCaseStatus();
        return null == caseStatus ||
                !caseStatus.getCode().equals(CASE_FILE_CLOSED_CODE_VALUE);
    }

    @SuppressWarnings("unchecked")
    private CaseStatus getCaseStatus(PatchMerge patchMerge) {
        Map<String, Object> caseStatus = (Map<String, Object>)
                patchMerge.getValue(CASE_STATUS);
        String caseStatusCode = (String) caseStatus.get(CODE);
        if (null == caseStatusCode) {
            caseStatusCode = "R";
        }
        String caseStatusCodeName = (String) caseStatus.get(CODE_NAME);
        return new CaseStatus(caseStatusCode, caseStatusCodeName);
    }
}
