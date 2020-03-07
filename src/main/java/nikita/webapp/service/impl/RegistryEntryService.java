package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.secondary.Precedence;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart;
import nikita.common.model.noark5.v5.hateoas.casehandling.RegistryEntryHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.DocumentFlowHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PrecedenceHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.SignOffHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.RegistryEntryStatus;
import nikita.common.model.noark5.v5.metadata.RegistryEntryType;
import nikita.common.model.noark5.v5.metadata.SignOffMethod;
import nikita.common.model.noark5.v5.secondary.DocumentFlow;
import nikita.common.model.noark5.v5.secondary.SignOff;
import nikita.common.repository.n5v5.IRegistryEntryRepository;
import nikita.common.repository.n5v5.secondary.ISignOffRepository;
import nikita.common.repository.nikita.IUserRepository;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.common.util.exceptions.NoarkAdministrativeUnitMemberException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IRegistryEntryHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IDocumentFlowHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IPrecedenceHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.ISignOffHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IRegistryEntryService;
import nikita.webapp.service.interfaces.ISequenceNumberGeneratorService;
import nikita.webapp.service.interfaces.admin.IAdministrativeUnitService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.secondary.ICorrespondencePartService;
import nikita.webapp.service.interfaces.secondary.IDocumentFlowService;
import nikita.webapp.service.interfaces.secondary.IPrecedenceService;
import nikita.webapp.web.events.AfterNoarkEntityDeletedEvent;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
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
import java.time.Year;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.validateDocumentMedium;
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
    private IDocumentFlowService documentFlowService;
    private IPrecedenceService precedenceService;
    private IMetadataService metadataService;
    private IRegistryEntryRepository registryEntryRepository;
    private IRegistryEntryHateoasHandler registryEntryHateoasHandler;
    private IDocumentFlowHateoasHandler documentFlowHateoasHandler;
    private IPrecedenceHateoasHandler precedenceHateoasHandler;
    private ISignOffHateoasHandler signOffHateoasHandler;
    private ISequenceNumberGeneratorService numberGeneratorService;
    private ISignOffRepository signOffRepository;
    private IUserRepository userRepository;
    private IAdministrativeUnitService administrativeUnitService;

    public RegistryEntryService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            ICorrespondencePartService correspondencePartService,
            IDocumentFlowService documentFlowService,
            IPrecedenceService precedenceService,
            IMetadataService metadataService,
            IRegistryEntryRepository registryEntryRepository,
            IRegistryEntryHateoasHandler registryEntryHateoasHandler,
            IDocumentFlowHateoasHandler documentFlowHateoasHandler,
            IPrecedenceHateoasHandler precedenceHateoasHandler,
            ISignOffHateoasHandler signOffHateoasHandler,
            ISequenceNumberGeneratorService numberGeneratorService,
            ISignOffRepository signOffRepository,
            IUserRepository userRepository,
            IAdministrativeUnitService administrativeUnitService) {
        super(entityManager, applicationEventPublisher);
        this.correspondencePartService = correspondencePartService;
        this.documentFlowService = documentFlowService;
        this.precedenceService = precedenceService;
        this.metadataService = metadataService;
        this.registryEntryRepository = registryEntryRepository;
        this.registryEntryHateoasHandler = registryEntryHateoasHandler;
        this.documentFlowHateoasHandler = documentFlowHateoasHandler;
        this.precedenceHateoasHandler =  precedenceHateoasHandler;
        this.signOffHateoasHandler = signOffHateoasHandler;
        this.numberGeneratorService = numberGeneratorService;
        this.userRepository = userRepository;
        this.signOffRepository = signOffRepository;
        this.administrativeUnitService = administrativeUnitService;
    }

    @Override
    public RegistryEntry save(@NotNull RegistryEntry registryEntry) {
        validateDocumentMedium(metadataService, registryEntry);
        validateRegistryEntryStatus(registryEntry);
        validateRegistryEntryType(registryEntry);
        registryEntry.setRecordDate(OffsetDateTime.now());
        File file = registryEntry.getReferenceFile();
        if (null != file) {
            long numberAssociated =
                    registryEntryRepository.countByReferenceFile(file) + 1;
            registryEntry.setRegistryEntryNumber((int) numberAssociated);
            registryEntry.setRecordId(file.getFileId() + "-" + numberAssociated);
            AdministrativeUnit administrativeUnit =
                    getAdministrativeUnitIfMemberOrThrow(registryEntry);

            // TODO. Pick up ZoneId value from spring configuration file
            int year = Year.now(ZoneId.of("Europe/Oslo")).getValue();
            registryEntry.setRecordYear(year);
            registryEntry.setRecordSequenceNumber(
                    numberGeneratorService.getNextRecordSequenceNumber(administrativeUnit));
        }
        registryEntryRepository.save(registryEntry);
        return registryEntry;
    }


    @Override
    public PrecedenceHateoas generateDefaultPrecedence(String systemID) {
        return precedenceService.generateDefaultPrecedence();
    }

    @Override
    public DocumentFlowHateoas generateDefaultDocumentFlow(String systemID) {
        return documentFlowService.generateDefaultDocumentFlow();
    }

    @Override
    public ResponseEntity<RegistryEntryHateoas> generateDefaultRegistryEntry(
            @NotNull final String caseFileSystemId) {
        RegistryEntry defaultRegistryEntry = new RegistryEntry();
        OffsetDateTime now = OffsetDateTime.now();
        defaultRegistryEntry.setRecordDate(now);
        defaultRegistryEntry.setDocumentDate(now);
        RegistryEntryStatus registryEntryStatus = (RegistryEntryStatus)
            metadataService.findValidMetadataByEntityTypeOrThrow
                (REGISTRY_ENTRY_STATUS, TEST_REGISTRY_ENTRY_STATUS_CODE, null);
        defaultRegistryEntry.setRegistryEntryStatus(registryEntryStatus);
        RegistryEntryType registryEntryType = (RegistryEntryType)
            metadataService.findValidMetadataByEntityTypeOrThrow
                (REGISTRY_ENTRY_TYPE, TEST_REGISTRY_ENTRY_TYPE_CODE, null);
        defaultRegistryEntry.setRegistryEntryType(registryEntryType);
        defaultRegistryEntry.setRecordYear(now.getYear());
        RegistryEntryHateoas registryEntryHateoas = new
                RegistryEntryHateoas(defaultRegistryEntry);
        registryEntryHateoasHandler.addLinksOnTemplate(registryEntryHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .body(registryEntryHateoas);
    }


    @Override
    public SignOffHateoas generateDefaultSignOff
       (@NotNull final String registryEntrySystemId) {
        SignOff defaultSignOff = new SignOff();
        OffsetDateTime now = OffsetDateTime.now();
        defaultSignOff.setSignOffDate(now);
        defaultSignOff.setSignOffBy(getUser());
        SignOffHateoas signOffHateoas = new SignOffHateoas(defaultSignOff);
        signOffHateoasHandler
            .addLinksOnTemplate(signOffHateoas, new Authorisation());
        return signOffHateoas;
    }

    @Override
    public SignOffHateoas
    createSignOffAssociatedWithRegistryEntry(String systemId,
                                             SignOff signOff) {
        RegistryEntry registryEntry = getRegistryEntryOrThrow(systemId);
        validateSignOffMethod(signOff);
        updateSignOffReferences(signOff);

        if (null == signOff.getReferenceSignedOffRecord()) {
            String info = "Rejecting invalid reference in field "
                + SIGN_OFF_REFERENCE_RECORD + ".";
            logger.info(info);
            throw new NikitaMalformedInputDataException(info);
        }
        //  This one is optional, but must be valid if set
        if (null != signOff.getReferenceSignedOffCorrespondencePartSystemID()
            && null == signOff.getReferenceSignedOffCorrespondencePart()) {
            String info = "Rejecting invalid reference in field "
                + SIGN_OFF_REFERENCE_CORRESPONDENCE_PART + ".";
            logger.info(info);
            throw new NikitaMalformedInputDataException(info);
        }

        signOff.setReferenceRecord(registryEntry);
        signOff = signOffRepository.save(signOff);
        registryEntry.addReferenceSignOff(signOff);
        SignOffHateoas signOffHateoas = new SignOffHateoas(signOff);
        signOffHateoasHandler.addLinks(signOffHateoas, new Authorisation());
        return signOffHateoas;
    }

    private void updateSignOffReferences(SignOff signOff) {
        RegistryEntry referenceRegistryEntry = null;
        CorrespondencePart referenceCorrespondencePart = null;

        UUID registryEntryID = signOff.getReferenceSignedOffRecordSystemID();
        UUID partID = signOff.getReferenceSignedOffCorrespondencePartSystemID();

        if (null == registryEntryID && null != partID) {
            String info = INFO_CANNOT_FIND_OBJECT +
                " CorrespondencePart " + partID.toString() +
                " without providing RegistryEntry.";
            logger.info(info);
            throw new NikitaMalformedInputDataException(info);
        }

        // look up IDs before changing anything, to avoid changing
        // instance if one of the IDs are unknown.
        if (null != registryEntryID) {
            // Will throw if registry entry is unknown
            // TODO avoid UUID->String->UUID conversion
            referenceRegistryEntry = findBySystemId(registryEntryID.toString());
        }
        if (null != partID) {
            // Will throw if correspondence part is unknown
            // TODO avoid UUID->String->UUID conversion
            referenceCorrespondencePart =
                correspondencePartService.findBySystemId(partID.toString());
        }

        if (null != referenceCorrespondencePart) {
            if (!referenceCorrespondencePart.getReferenceRecord()
                .equals(referenceRegistryEntry)) {
                String info = INFO_CANNOT_FIND_OBJECT +
                    " CorrespondencePart " + partID.toString() +
                    " below RegistryEntry " +
                    referenceRegistryEntry.getSystemId() + ".";
                logger.info(info);
                throw new NoarkEntityNotFoundException(info);
            }
            signOff.setReferenceSignedOffCorrespondencePart
                (referenceCorrespondencePart);
        } else {
            signOff.setReferenceSignedOffCorrespondencePart(null);
        }

        if (null != registryEntryID && null != referenceRegistryEntry) {
            signOff.setReferenceSignedOffRecord(referenceRegistryEntry);
        } else {
            signOff.setReferenceSignedOffRecord(null);
        }
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

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RegistryEntry> criteriaQuery = criteriaBuilder.createQuery(RegistryEntry.class);
        Root<RegistryEntry> from = criteriaQuery.from(RegistryEntry.class);
        CriteriaQuery<RegistryEntry> select = criteriaQuery.select(from);

        criteriaQuery.where(criteriaBuilder.equal(from.get("ownedBy"), getUser()));
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
                (List<INoarkEntity>)
                        (List) registryEntryRepository.
                                findByReferenceFile(caseFile));
        registryEntryHateoasHandler.addLinks(registryEntryHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .body(registryEntryHateoas);
    }

    @Override
    public DocumentFlowHateoas findAllDocumentFlowWithRegistryEntryBySystemId
        (String systemID) {
        RegistryEntry registryEntry = getRegistryEntryOrThrow(systemID);
        DocumentFlowHateoas documentFlowHateoas =
                new DocumentFlowHateoas((List<INoarkEntity>)
                        (List) registryEntry.getReferenceDocumentFlow());
        documentFlowHateoasHandler.addLinks(documentFlowHateoas,
                                            new Authorisation());
        setOutgoingRequestHeader(documentFlowHateoas);
        return documentFlowHateoas;
    }

    @Override
    public PrecedenceHateoas findAllPrecedenceForRegistryEntry(
            @NotNull final String systemID) {
        RegistryEntry registryEntry = getRegistryEntryOrThrow(systemID);
        PrecedenceHateoas precedenceHateoas =
                new PrecedenceHateoas((List<INoarkEntity>)
                        (List) registryEntry.getReferencePrecedence());
        precedenceHateoasHandler.addLinks(precedenceHateoas,
                                          new Authorisation());
        setOutgoingRequestHeader(precedenceHateoas);
        return precedenceHateoas;
    }

    @Override
    public SignOffHateoas
    findAllSignOffAssociatedWithRegistryEntry(String systemId) {
        SignOffHateoas signOffHateoas =
            new SignOffHateoas((List<INoarkEntity>) (List)
            getRegistryEntryOrThrow(systemId).getReferenceSignOff());
        signOffHateoasHandler.addLinks(signOffHateoas,
                                       new Authorisation());
        return signOffHateoas;
    }

    @Override
    public SignOffHateoas
    findSignOffAssociatedWithRegistryEntry(String systemId,
                                           String subSystemId) {
        RegistryEntry registryEntry = getRegistryEntryOrThrow(systemId);
        SignOff signOff = getSignOffOrThrow(subSystemId);
        if (null == signOff.getReferenceRecord()
            || !signOff.getReferenceRecord().equals(registryEntry)) {
            String info = INFO_CANNOT_FIND_OBJECT +
                " SignOff " + subSystemId +
                " below RegistryEntry " + systemId + ".";
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        SignOffHateoas signOffHateoas = new SignOffHateoas(signOff);
        signOffHateoasHandler.addLinks(signOffHateoas, new Authorisation());
        setOutgoingRequestHeader(signOffHateoas);
        return signOffHateoas;
    }

    @Override
    public DocumentFlowHateoas associateDocumentFlowWithRegistryEntry(
            String systemId, DocumentFlow documentFlow) {
        return documentFlowService.associateDocumentFlowWithRegistryEntry
            (documentFlow, getRegistryEntryOrThrow(systemId));
    }

    @Override
    public PrecedenceHateoas createPrecedenceAssociatedWithRecord(
            String registryEntrySystemID, Precedence precedence) {

        RegistryEntry registryEntry = getRegistryEntryOrThrow(
                registryEntrySystemID);
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

    @Override
    public SignOffHateoas
    handleUpdateSignOff(@NotNull final String systemID,
                        @NotNull final String signOffSystemID,
                        @NotNull final Long version,
                        @NotNull final SignOff incomingSignOff) {
        RegistryEntry registryEntry = getRegistryEntryOrThrow(systemID);
        SignOff existingSignOff = getSignOffOrThrow(signOffSystemID);
        if (null == existingSignOff.getReferenceRecord()
            || ! existingSignOff.getReferenceRecord().equals(registryEntry)) {
            String info = INFO_CANNOT_FIND_OBJECT +
                " SignOff " + signOffSystemID +
                " below RegistryEntry " + systemID + ".";
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }

        updateSignOffReferences(incomingSignOff);

        existingSignOff.setSignOffMethod(incomingSignOff.getSignOffMethod());
        existingSignOff.setReferenceSignedOffRecordSystemID
            (incomingSignOff.getReferenceSignedOffRecordSystemID());
        existingSignOff.setReferenceSignedOffCorrespondencePartSystemID
            (incomingSignOff.getReferenceSignedOffCorrespondencePartSystemID());
        existingSignOff.setReferenceSignedOffRecord
            (incomingSignOff.getReferenceSignedOffRecord());
        existingSignOff.setReferenceSignedOffCorrespondencePart
            (incomingSignOff.getReferenceSignedOffCorrespondencePart());

        SignOffHateoas signOffHateoas =
            new SignOffHateoas(signOffRepository
                                  .save(existingSignOff));
        signOffHateoasHandler.addLinks(signOffHateoas,
                new Authorisation());
        setOutgoingRequestHeader(signOffHateoas);
        return signOffHateoas;
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
        RegistryEntry registryEntry = getRegistryEntryOrThrow(
                registryEntrySystemId);
        deleteEntity(registryEntry);
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityDeletedEvent(this, registryEntry));
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

    @Override
    public void deleteSignOff(@NotNull String systemID,
                              @NotNull String signOffSystemID) {
        RegistryEntry registryEntry = getRegistryEntryOrThrow(systemID);
        SignOff signOff = getSignOffOrThrow(signOffSystemID);
        if (null == signOff.getReferenceRecord()
            || ! signOff.getReferenceRecord().equals(registryEntry)) {
            String info = INFO_CANNOT_FIND_OBJECT +
                " Conversion " + signOffSystemID +
                " below DocumentObject " + systemID + ".";
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        deleteEntity(signOff);
    }

    // All helper methods

    /**
     * Internal helper method. Find the administrativeUnit identified by the
     * given systemId or throw a NoarkEntityNotFoundException. Then check that
     * the user is a member of that administrativeUnit. If they are not throw a
     * NoarkAdministrativeUnitMemberException.
     * <p>
     * Note this method will return a non-null administrativeUnit. An
     * exception is thrown otherwise.
     *
     * @param RegistryEntry The RegistryEntry
     * @return the administrativeUnit
     */
    private AdministrativeUnit getAdministrativeUnitIfMemberOrThrow(
            RegistryEntry RegistryEntry) {

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
                            RegistryEntry.getOwnedBy() + "]");
        }

        userOptional = userRepository.findByUsername(getUser());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            checkCaseResponsibleMemberAdministrativeUnit(user,
                    administrativeUnit);
        } else {
            throw new NoarkEntityNotFoundException(
                    "Could not find user with systemID [" +
                            RegistryEntry.getSystemId() + "]");
        }
        return administrativeUnit;
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
                registryEntryRepository.
                        findBySystemId(UUID.fromString(registryEntrySystemId));
        if (registryEntry == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " RegistryEntry, using systemId " + registryEntrySystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return registryEntry;
    }

    protected SignOff getSignOffOrThrow(@NotNull String signOffSystemId) {
        SignOff signOff = signOffRepository
            .findBySystemId(UUID.fromString(signOffSystemId));
        if (signOff == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " SignOff, using systemId " +
                    signOffSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return signOff;
    }

    private void validateRegistryEntryStatus(RegistryEntry registryEntry) {
        // Assume value already set, as the deserialiser will enforce it.
        RegistryEntryStatus registryEntryStatus = (RegistryEntryStatus)
                metadataService.findValidMetadataByEntityTypeOrThrow(
                                REGISTRY_ENTRY_STATUS,
                                registryEntry.getRegistryEntryStatus());
        registryEntry.setRegistryEntryStatus(registryEntryStatus);
    }

    private void validateRegistryEntryType(RegistryEntry registryEntry) {
        // Assume value already set, as the deserialiser will enforce it.
        RegistryEntryType registryEntryType = (RegistryEntryType)
                metadataService.findValidMetadataByEntityTypeOrThrow(
                                REGISTRY_ENTRY_TYPE,
                                registryEntry.getRegistryEntryType());
        registryEntry.setRegistryEntryType(registryEntryType);
    }

    private void validateSignOffMethod(SignOff incomingSignOff) {
        // Assume value already set, as the deserialiser will enforce it.
        SignOffMethod signOffMethod =
                (SignOffMethod) metadataService
                    .findValidMetadataByEntityTypeOrThrow(
                            SIGN_OFF_METHOD,
                            incomingSignOff.getSignOffMethodCode(),
                            incomingSignOff.getSignOffMethodCodeName());
        incomingSignOff.setSignOffMethodCodeName(signOffMethod.getCodeName());
    }
}
