package nikita.webapp.service.impl;

import nikita.common.model.nikita.PatchObjects;
import nikita.common.model.noark5.bsm.BSMBase;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartInternal;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit;
import nikita.common.model.noark5.v5.hateoas.*;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartInternalHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartPersonHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartUnitHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.*;
import nikita.common.model.noark5.v5.hateoas.secondary.*;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.md_other.BSMMetadata;
import nikita.common.model.noark5.v5.nationalidentifier.*;
import nikita.common.model.noark5.v5.secondary.Author;
import nikita.common.model.noark5.v5.secondary.Comment;
import nikita.common.model.noark5.v5.secondary.PartPerson;
import nikita.common.model.noark5.v5.secondary.PartUnit;
import nikita.common.repository.n5v5.IDocumentDescriptionRepository;
import nikita.common.repository.n5v5.IRecordRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.*;
import nikita.webapp.hateoas.interfaces.nationalidentifier.INationalIdentifierHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IAuthorHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.ICommentHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.ICorrespondencePartHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IPartHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.interfaces.IBSMService;
import nikita.webapp.service.interfaces.INationalIdentifierService;
import nikita.webapp.service.interfaces.IRecordService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.secondary.IAuthorService;
import nikita.webapp.service.interfaces.secondary.ICommentService;
import nikita.webapp.service.interfaces.secondary.ICorrespondencePartService;
import nikita.webapp.service.interfaces.secondary.IPartService;
import nikita.webapp.web.events.AfterNoarkEntityCreatedEvent;
import nikita.webapp.web.events.AfterNoarkEntityDeletedEvent;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.time.OffsetDateTime.now;
import static nikita.common.config.Constants.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.validateDocumentMedium;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service
public class RecordService
        extends NoarkService
        implements IRecordService {

    private static final Logger logger =
            LoggerFactory.getLogger(RecordService.class);

    private final DocumentDescriptionService documentDescriptionService;
    private final IRecordRepository recordRepository;
    private final IBSMService bsmService;
    private final IRecordHateoasHandler recordHateoasHandler;
    private final IFileHateoasHandler fileHateoasHandler;
    private final ISeriesHateoasHandler seriesHateoasHandler;
    private final IClassHateoasHandler classHateoasHandler;
    private final IDocumentDescriptionHateoasHandler
            documentDescriptionHateoasHandler;
    private final IDocumentDescriptionRepository documentDescriptionRepository;
    private final IAuthorService authorService;
    private final ICommentService commentService;
    private final ICorrespondencePartService correspondencePartService;
    private final IMetadataService metadataService;
    private final INationalIdentifierService nationalIdentifierService;
    private final IPartService partService;
    private final ICorrespondencePartHateoasHandler correspondencePartHateoasHandler;
    private final INationalIdentifierHateoasHandler nationalIdentifierHateoasHandler;
    private final IPartHateoasHandler partHateoasHandler;
    private final IAuthorHateoasHandler authorHateoasHandler;
    private final ICommentHateoasHandler commentHateoasHandler;

    public RecordService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IPatchService patchService,
            DocumentDescriptionService documentDescriptionService,
            IRecordRepository recordRepository,
            IBSMService bsmService,
            IRecordHateoasHandler recordHateoasHandler,
            IFileHateoasHandler fileHateoasHandler,
            ISeriesHateoasHandler seriesHateoasHandler,
            IClassHateoasHandler classHateoasHandler,
            IDocumentDescriptionHateoasHandler
                    documentDescriptionHateoasHandler,
            IDocumentDescriptionRepository documentDescriptionRepository,
            IAuthorService authorService,
            ICommentService commentService,
            ICorrespondencePartService correspondencePartService,
            IMetadataService metadataService,
            INationalIdentifierService nationalIdentifierService,
            IPartService partService,
            ICorrespondencePartHateoasHandler correspondencePartHateoasHandler,
            INationalIdentifierHateoasHandler nationalIdentifierHateoasHandler,
            IPartHateoasHandler partHateoasHandler,
            IAuthorHateoasHandler authorHateoasHandler,
            ICommentHateoasHandler commentHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService);
        this.documentDescriptionService = documentDescriptionService;
        this.recordRepository = recordRepository;
        this.bsmService = bsmService;
        this.entityManager = entityManager;
        this.recordHateoasHandler = recordHateoasHandler;
        this.fileHateoasHandler = fileHateoasHandler;
        this.seriesHateoasHandler = seriesHateoasHandler;
        this.classHateoasHandler = classHateoasHandler;
        this.documentDescriptionHateoasHandler =
                documentDescriptionHateoasHandler;
        this.documentDescriptionRepository = documentDescriptionRepository;
        this.authorService = authorService;
        this.commentService = commentService;
        this.correspondencePartService = correspondencePartService;
        this.metadataService = metadataService;
        this.nationalIdentifierService = nationalIdentifierService;
        this.partService = partService;
        this.correspondencePartHateoasHandler = correspondencePartHateoasHandler;
        this.nationalIdentifierHateoasHandler = nationalIdentifierHateoasHandler;
        this.partHateoasHandler = partHateoasHandler;
        this.authorHateoasHandler = authorHateoasHandler;
        this.commentHateoasHandler = commentHateoasHandler;
    }

    // All CREATE operations

    @Override
    @Transactional
    public ResponseEntity<RecordHateoas> save(Record record) {
        validateDocumentMedium(metadataService, record);
        bsmService.validateBSMList(record.getReferenceBSMBase());
        RecordHateoas recordHateoas =
                new RecordHateoas(recordRepository.save(record));
        recordHateoasHandler.addLinks(recordHateoas, new Authorisation());
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(recordHateoas.getEntityVersion().toString())
                .body(recordHateoas);
    }

    @Override
    @Transactional
    public DocumentDescriptionHateoas
    createDocumentDescriptionAssociatedWithRecord(
            String systemID, DocumentDescription documentDescription) {
        Record record = getRecordOrThrow(systemID);
        validateDocumentMedium(metadataService, documentDescription);
        // Adding 1 as documentNumber starts at 1, not 0
        long documentNumber =
                documentDescriptionRepository.
                        countByReferenceRecord(record) + 1;
        documentDescription.setDocumentNumber((int) documentNumber);
        record.addDocumentDescription(documentDescription);
        documentDescription.setDocumentNumber((int) documentNumber);

        DocumentDescriptionHateoas documentDescriptionHateoas =
                new DocumentDescriptionHateoas(
                        documentDescriptionService.save(documentDescription));
        documentDescriptionHateoasHandler.addLinks(
                documentDescriptionHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(new
                AfterNoarkEntityCreatedEvent(this, documentDescription));

        return documentDescriptionHateoas;
    }

    /**
     * Create a CorrespondencePartPerson object and associate it with the
     * identified record
     *
     * @param systemID           The systemId of the record object you want to
     *                           create an associated correspondencePartPerson for
     * @param correspondencePart The incoming correspondencePartPerson
     * @return The persisted CorrespondencePartPerson object wrapped as a
     * CorrespondencePartPersonHateoas object
     */
    @Override
    @Transactional
    public CorrespondencePartPersonHateoas
    createCorrespondencePartPersonAssociatedWithRecord(
            @NotNull final String systemID,
            @NotNull final CorrespondencePartPerson correspondencePart) {
        return correspondencePartService.
                createNewCorrespondencePartPerson(correspondencePart,
                        getRecordOrThrow(systemID));
    }

    @Override
    @Transactional
    public PartPersonHateoas
    createPartPersonAssociatedWithRecord(
            @NotNull String systemID, @NotNull PartPerson partPerson) {
        return partService.
                createNewPartPerson(partPerson,
                        getRecordOrThrow(systemID));
    }

    @Override
    @Transactional
    public PartUnitHateoas
    createPartUnitAssociatedWithRecord(
            @NotNull String systemID, @NotNull PartUnit partUnit) {
        return partService.
                createNewPartUnit(partUnit, getRecordOrThrow(systemID));
    }

    /**
     * Create a CorrespondencePartInternal object and associate it with the
     * identified record
     *
     * @param systemID           The systemId of the record object you want to
     *                           create an associated correspondencePartInternal for
     * @param correspondencePart The incoming correspondencePartInternal
     * @return The persisted CorrespondencePartInternal object wrapped as a
     * CorrespondencePartInternalHateoas object
     */
    @Override
    @Transactional
    public CorrespondencePartInternalHateoas
    createCorrespondencePartInternalAssociatedWithRecord(
            String systemID, CorrespondencePartInternal correspondencePart) {
        return correspondencePartService.
                createNewCorrespondencePartInternal(correspondencePart,
                        getRecordOrThrow(systemID));
    }

    /**
     * Create a CorrespondencePartUnit object and associate it with the
     * identified record
     *
     * @param systemID           The systemId of the record object you want to
     *                           create an associated correspondencePartUnit for
     * @param correspondencePart The incoming correspondencePartUnit
     * @return The persisted CorrespondencePartUnit object wrapped as a
     * CorrespondencePartUnitHateoas object
     */
    @Override
    @Transactional
    public CorrespondencePartUnitHateoas
    createCorrespondencePartUnitAssociatedWithRecord(
            String systemID, CorrespondencePartUnit correspondencePart) {
        return correspondencePartService.
                createNewCorrespondencePartUnit(correspondencePart,
                        getRecordOrThrow(systemID));
    }

    @Override
    @Transactional
    public BuildingHateoas
    createBuildingAssociatedWithRecord(
            @NotNull String systemID, @NotNull Building building) {
        return nationalIdentifierService.
                createNewBuilding(building, getRecordOrThrow(systemID));
    }

    @Override
    @Transactional
    public CadastralUnitHateoas
    createCadastralUnitAssociatedWithRecord(
            @NotNull String systemID, @NotNull CadastralUnit cadastralUnit) {
        return nationalIdentifierService.
                createNewCadastralUnit(cadastralUnit, getRecordOrThrow(systemID));
    }

    @Override
    @Transactional
    public DNumberHateoas
    createDNumberAssociatedWithRecord(
            @NotNull String systemID, @NotNull DNumber dNumber) {
        return nationalIdentifierService.
                createNewDNumber(dNumber, getRecordOrThrow(systemID));
    }

    @Override
    @Transactional
    public PlanHateoas
    createPlanAssociatedWithRecord(
            @NotNull String systemID, @NotNull Plan plan) {
        return nationalIdentifierService.
                createNewPlan(plan, getRecordOrThrow(systemID));
    }

    @Override
    public PositionHateoas
    createPositionAssociatedWithRecord(
            @NotNull String systemID, @NotNull Position position) {
        return nationalIdentifierService.
                createNewPosition(position, getRecordOrThrow(systemID));
    }

    @Override
    @Transactional
    public SocialSecurityNumberHateoas
    createSocialSecurityNumberAssociatedWithRecord(
            @NotNull String systemID,
            @NotNull SocialSecurityNumber socialSecurityNumber) {
        return nationalIdentifierService
                .createNewSocialSecurityNumber(socialSecurityNumber,
                        getRecordOrThrow(systemID));
    }

    @Override
    @Transactional
    public UnitHateoas
    createUnitAssociatedWithRecord(
            @NotNull String systemID, @NotNull Unit unit) {
        return nationalIdentifierService.
                createNewUnit(unit, getRecordOrThrow(systemID));
    }

    @Override
    @Transactional
    public CommentHateoas createCommentAssociatedWithRecord
            (String systemID, Comment comment) {
        return commentService.createNewComment
                (comment, getRecordOrThrow(systemID));
    }

    // All READ operations

    public List<Record> findAll() {
        return recordRepository.findAll();
    }

    /**
     * Retrieve all record associated with the documentDescription identified by
     * the documentDescriptions systemId.
     *
     * @param systemId systemId of the associated documentDescription
     * @return The list of record packed as a ResponseEntity
     */
    @Override
    @SuppressWarnings("unchecked")
    public ResponseEntity<RecordHateoas>
    findByReferenceDocumentDescription(@NotNull final String systemId) {
        RecordHateoas recordHateoas = new RecordHateoas(
                (List<INoarkEntity>) (List)
                        recordRepository.
                                findAllByReferenceDocumentDescription(
                                        documentDescriptionService.
                                                findDocumentDescriptionBySystemId(
                                                        systemId)));
        recordHateoasHandler.addLinks(recordHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .body(recordHateoas);
    }

    @Override
    @SuppressWarnings("unchecked")
    public AuthorHateoas findAllAuthorWithRecordBySystemId(String systemId) {
        Record record = getRecordOrThrow(systemId);
        AuthorHateoas authorHateoas =
                new AuthorHateoas((List<INoarkEntity>)
                        (List) record.getReferenceAuthor());
        authorHateoasHandler.addLinks(authorHateoas, new Authorisation());
        setOutgoingRequestHeader(authorHateoas);
        return authorHateoas;
    }

    /**
     * Retrieve all File associated with the record identified by
     * the records systemId.
     *
     * @param systemId systemId of the record
     * @return The parent File packed as a ResponseEntity
     */
    @Override
    public ResponseEntity<FileHateoas>
    findFileAssociatedWithRecord(@NotNull final String systemId) {
        FileHateoas fileHateoas = new FileHateoas(
                recordRepository.
                        findBySystemId(
                                UUID.fromString(systemId)).getReferenceFile());
        fileHateoasHandler.addLinks(fileHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(fileHateoas.getEntityVersion().toString())
                .body(fileHateoas);
    }

    /**
     * Retrieve all Class associated with the record identified by
     * the records systemId.
     *
     * @param systemId systemId of the record
     * @return The parent Class packed as a ResponseEntity
     */
    @Override
    public ResponseEntity<ClassHateoas>
    findClassAssociatedWithRecord(@NotNull final String systemId) {
        ClassHateoas classHateoas = new ClassHateoas(
                recordRepository.findBySystemId(UUID.fromString(systemId)).
                        getReferenceClass());

        classHateoasHandler.addLinks(classHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(classHateoas.getEntityVersion().toString())
                .body(classHateoas);
    }

    /**
     * Retrieve all Series associated with the record identified by
     * the records systemId.
     *
     * @param systemId systemId of the record
     * @return The parent Series packed as a ResponseEntity
     */
    @Override
    public ResponseEntity<SeriesHateoas>
    findSeriesAssociatedWithRecord(@NotNull final String systemId) {
        SeriesHateoas seriesHateoas = new SeriesHateoas(
                recordRepository.findBySystemId(UUID.fromString(systemId)).
                        getReferenceSeries());

        seriesHateoasHandler.addLinks(seriesHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(seriesHateoas.getEntityVersion().toString())
                .body(seriesHateoas);
    }

    public Record findBySystemId(String systemId) {
        return getRecordOrThrow(systemId);
    }

    @Override
    public List<Record> findByOwnedBy() {
        return recordRepository.findByOwnedBy(getUser());
    }

    @Override
    public CommentHateoas getCommentAssociatedWithRecord(
            @NotNull final String systemID) {
        CommentHateoas commentHateoas =
                new CommentHateoas(List.copyOf(
                        getRecordOrThrow(systemID).getReferenceComment()));
        commentHateoasHandler.addLinks(commentHateoas, new Authorisation());
        return commentHateoas;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CorrespondencePartHateoas
    getCorrespondencePartAssociatedWithRecord(
            final String systemID) {
        CorrespondencePartHateoas correspondencePartHateoas =
            new CorrespondencePartHateoas(
                (List<INoarkEntity>) (List) getRecordOrThrow(systemID).
                        getReferenceCorrespondencePart());
        correspondencePartHateoasHandler.addLinks(
                correspondencePartHateoas, new Authorisation());
        return correspondencePartHateoas;
    }

    @Override
    public PartHateoas
    getPartAssociatedWithRecord(final String systemID) {
        PartHateoas partHateoas = new PartHateoas(List.copyOf(
                getRecordOrThrow(systemID).getReferencePart()));
        partHateoasHandler.addLinks(partHateoas, new Authorisation());
        return partHateoas;
    }

    @Override
    @SuppressWarnings("unchecked")
    public NationalIdentifierHateoas getNationalIdentifierAssociatedWithRecord(
            @NotNull final String systemID) {
        NationalIdentifierHateoas niHateoas = new NationalIdentifierHateoas(
                (List<INoarkEntity>) (List) getRecordOrThrow(systemID).
                        getReferenceNationalIdentifier());
        nationalIdentifierHateoasHandler
            .addLinks(niHateoas, new Authorisation());
        return niHateoas;
    }

    /**
     * Used to retrieve a BSMBase object so parent can can check that the
     * BSMMetadata object exists and is not outdated.
     * Done to simplify coding.
     *
     * @param name Name of the BSM parameter to check
     * @return BSMMetadata object corresponding to the name
     */
    @Override
    protected Optional<BSMMetadata> findBSMByName(String name) {
        return bsmService.findBSMByName(name);
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
     * when the call to Record.setVersion() occurs.
     * <p>
     * It's a little unclear if it's possible to update this as it has no
     * fields that are updatable. It's also unclear how to set the archivedBy
     * value.
     *
     * @param recordSystemId The systemId of the record object to retrieve
     * @param version        The last known version number (derived from an ETAG)
     * @param incomingRecord The incoming record object
     * @return The updatedRecord after it is persisted
     */
    @Override
    @Transactional
    public Record handleUpdate(@NotNull final String recordSystemId,
                               @NotNull final Long version,
                               @NotNull final Record incomingRecord) {
        bsmService.validateBSMList(incomingRecord.getReferenceBSMBase());
        Record existingRecord = getRecordOrThrow(recordSystemId);
        // Here copy all the values you are allowed to copy ....
        updateTitleAndDescription(incomingRecord, existingRecord);
        if (null != incomingRecord.getDocumentMedium()) {
            existingRecord.setDocumentMedium(
                    incomingRecord.getDocumentMedium());
        }
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingRecord.setVersion(version);
        recordRepository.save(existingRecord);
        return existingRecord;
    }

    @Override
    @Transactional
    public ResponseEntity<RecordHateoas> handleUpdate(
            UUID systemID, PatchObjects patchObjects) {
        Record record = (Record) handlePatch(systemID, patchObjects);
        RecordHateoas recordHateoas = new RecordHateoas(record);
        recordHateoasHandler.addLinks(recordHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this, record));
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(recordHateoas.getEntityVersion().toString())
                .body(recordHateoas);
    }

    /**
     * Persist and associate the incoming author object with the record
     * identified by systemId
     *
     * @param systemId The sytsemId of the record to associate with
     * @param author   The incoming author object
     * @return author object wrapped as a AuthorHateaos
     */
    @Override
    @Transactional
    public AuthorHateoas associateAuthorWithRecord(
            String systemId, Author author) {
        return authorService.associateAuthorWithRecord
                (author, getRecordOrThrow(systemId));
    }

    @Override
    @Transactional
    public Object associateBSM(@NotNull UUID systemId,
                               @NotNull List<BSMBase> bsm) {
        Record record = getRecordOrThrow(systemId);
        record.addReferenceBSMBase(bsm);
        return record;
    }

    // All DELETE operations

    @Override
    @Transactional
    public void deleteRecord(@NotNull UUID systemID) {
        Record record = getRecordOrThrow(systemID);
        recordRepository.delete(record);
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityDeletedEvent(this, record));
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     *
     * @return the number of objects deleted
     */
    @Override
    @Transactional
    public long deleteAllByOwnedBy() {
        return recordRepository.deleteByOwnedBy(getUser());
    }

    // All template methods

    /**
     * Generate a Default CorrespondencePartUnit object that can be
     * associated with the identified Record.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @return the CorrespondencePartUnit object wrapped as a
     * CorrespondencePartUnitHateoas object
     */
    @Override
    public RecordHateoas
    generateDefaultRecord() {
        Record defaultRecord = new Record();
        defaultRecord.setArchivedBy(TEST_USER_CASE_HANDLER_2);
        defaultRecord.setArchivedDate(now());
        defaultRecord.setTitle(TEST_TITLE);
        defaultRecord.setDescription(TEST_DESCRIPTION);
        RecordHateoas recordHateoas = new RecordHateoas(defaultRecord);
        recordHateoasHandler.addLinksOnTemplate(recordHateoas, new Authorisation());
        return recordHateoas;
    }

    @Override
    public CommentHateoas generateDefaultComment() {
        return commentService.generateDefaultComment();
    }

    /**
     * Generate a Default CorrespondencePartUnit object that can be
     * associated with the identified Record.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param recordSystemId The systemId of the record object
     *                       you wish to create a templated object for
     * @return the CorrespondencePartUnit object wrapped as a
     * CorrespondencePartUnitHateoas object
     */
    @Override
    public CorrespondencePartInternalHateoas
    generateDefaultCorrespondencePartInternal(
            String recordSystemId) {
        return correspondencePartService.
                generateDefaultCorrespondencePartInternal(recordSystemId);
    }

    /**
     * Generate a Default CorrespondencePartUnit object that can be
     * associated with the identified Record.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param recordSystemId The systemId of the record object
     *                       you wish to create a templated object for
     * @return the CorrespondencePartUnit object wrapped as a
     * CorrespondencePartUnitHateoas object
     */
    @Override
    public CorrespondencePartPersonHateoas
    generateDefaultCorrespondencePartPerson(
            String recordSystemId) {
        return correspondencePartService.
                generateDefaultCorrespondencePartPerson(recordSystemId);
    }

    /**
     * Generate a Default PartUnit object that can be
     * associated with the identified Record.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param recordSystemId The systemId of the record object
     *                       you wish to create a templated object for
     * @return the PartUnit object wrapped as a
     * PartUnitHateoas object
     */
    @Override
    public PartPersonHateoas
    generateDefaultPartPerson(
            String recordSystemId) {
        return partService.generateDefaultPartPerson(recordSystemId);
    }

    /**
     * Generate a Default PartUnit object that can be
     * associated with the identified Record.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param recordSystemId The systemId of the record object
     *                       you wish to create a templated object for
     * @return the PartUnit object wrapped as a
     * PartUnitHateoas object
     */
    @Override
    public PartUnitHateoas
    generateDefaultPartUnit(
            String recordSystemId) {
        return partService.generateDefaultPartUnit(recordSystemId);
    }

    /**
     * Generate a Default CorrespondencePartUnit object that can be
     * associated with the identified Record.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param recordSystemId The systemId of the record object
     *                       you wish to create a templated object for
     * @return the CorrespondencePartUnit object wrapped as a
     * CorrespondencePartUnitHateoas object
     */
    @Override
    public CorrespondencePartUnitHateoas generateDefaultCorrespondencePartUnit(
            String recordSystemId) {
        return correspondencePartService.
                generateDefaultCorrespondencePartUnit(
                        recordSystemId);
    }

    @Override
    public AuthorHateoas generateDefaultAuthor(String systemID) {
        return authorService.generateDefaultAuthor();
    }

    @Override
    public BuildingHateoas generateDefaultBuilding() {
        return nationalIdentifierService.generateDefaultBuilding();
    }

    @Override
    public CadastralUnitHateoas generateDefaultCadastralUnit() {
        return nationalIdentifierService.generateDefaultCadastralUnit();
    }

    @Override
    public DNumberHateoas generateDefaultDNumber() {
        return nationalIdentifierService.generateDefaultDNumber();
    }

    @Override
    public PlanHateoas generateDefaultPlan() {
        return nationalIdentifierService.generateDefaultPlan();
    }

    @Override
    public PositionHateoas generateDefaultPosition() {
        return nationalIdentifierService.generateDefaultPosition();
    }

    @Override
    public SocialSecurityNumberHateoas generateDefaultSocialSecurityNumber() {
        return nationalIdentifierService.generateDefaultSocialSecurityNumber();
    }

    @Override
    public UnitHateoas generateDefaultUnit() {
        return nationalIdentifierService.generateDefaultUnit();
    }

    // All HELPER operations

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid Record back. If there is no valid
     * Record, an exception is thrown
     *
     * @param systemID the systemId of the record you want to retrieve
     * @return the record
     */
    protected Record getRecordOrThrow(@NotNull String systemID) {
        return getRecordOrThrow(UUID.fromString(systemID));
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid Record back. If there is no valid
     * Record, an exception is thrown
     *
     * @param systemID the systemId of the record you want to retrieve
     * @return the record
     */
    protected Record getRecordOrThrow(@NotNull UUID systemID) {
        Record record =
                recordRepository.findBySystemId(systemID);
        if (record == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " Record, using systemId " + systemID;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return record;
    }
}
