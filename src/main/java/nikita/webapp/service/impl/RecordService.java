package nikita.webapp.service.impl;

import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.nikita.PatchMerge;
import nikita.common.model.nikita.PatchObjects;
import nikita.common.model.noark5.bsm.BSMBase;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartInternal;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit;
import nikita.common.model.noark5.v5.hateoas.*;
import nikita.common.model.noark5.v5.hateoas.casehandling.*;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.*;
import nikita.common.model.noark5.v5.hateoas.secondary.*;
import nikita.common.model.noark5.v5.md_other.BSMMetadata;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.model.noark5.v5.nationalidentifier.*;
import nikita.common.model.noark5.v5.secondary.*;
import nikita.common.repository.n5v5.IDocumentDescriptionRepository;
import nikita.common.repository.n5v5.IRecordRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IRecordHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.interfaces.*;
import nikita.webapp.service.interfaces.casehandling.IRecordNoteService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.odata.IODataService;
import nikita.webapp.service.interfaces.secondary.*;
import nikita.webapp.web.events.AfterNoarkEntityDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.time.OffsetDateTime.now;
import static java.util.List.copyOf;
import static nikita.common.config.Constants.*;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.validateDocumentMedium;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.validateScreening;

@Service
public class RecordService
        extends NoarkService
        implements IRecordService {

    private static final Logger logger =
            LoggerFactory.getLogger(RecordService.class);

    private final IDocumentDescriptionService documentDescriptionService;
    private final IRecordRepository recordRepository;
    private final IBSMService bsmService;
    private final IRecordHateoasHandler recordHateoasHandler;
    private final IDocumentDescriptionRepository documentDescriptionRepository;
    private final IAuthorService authorService;
    private final IKeywordService keywordService;
    private final ICrossReferenceService crossReferenceService;
    private final ICommentService commentService;
    private final ICorrespondencePartService correspondencePartService;
    private final IMetadataService metadataService;
    private final IRecordNoteService recordNoteService;
    private final IRegistryEntryService registryEntryService;
    private final INationalIdentifierService nationalIdentifierService;
    private final IPartService partService;
    private final IScreeningMetadataService screeningMetadataService;
    private final IStorageLocationService storageLocationService;

    public RecordService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IODataService odataService,
            IPatchService patchService,
            IDocumentDescriptionService documentDescriptionService,
            IRecordRepository recordRepository,
            IBSMService bsmService,
            IRecordHateoasHandler recordHateoasHandler,
            IDocumentDescriptionRepository documentDescriptionRepository,
            IAuthorService authorService,
            IKeywordService keywordService,
            ICrossReferenceService crossReferenceService,
            ICommentService commentService,
            ICorrespondencePartService correspondencePartService,
            IMetadataService metadataService,
            IRecordNoteService recordNoteService, IRegistryEntryService registryEntryService, INationalIdentifierService nationalIdentifierService,
            IPartService partService,
            IScreeningMetadataService screeningMetadataService,
            IStorageLocationService storageLocationService) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.documentDescriptionService = documentDescriptionService;
        this.recordRepository = recordRepository;
        this.bsmService = bsmService;
        this.recordHateoasHandler = recordHateoasHandler;
        this.documentDescriptionRepository = documentDescriptionRepository;
        this.authorService = authorService;
        this.keywordService = keywordService;
        this.crossReferenceService = crossReferenceService;
        this.commentService = commentService;
        this.correspondencePartService = correspondencePartService;
        this.metadataService = metadataService;
        this.recordNoteService = recordNoteService;
        this.registryEntryService = registryEntryService;
        this.nationalIdentifierService = nationalIdentifierService;
        this.partService = partService;
        this.screeningMetadataService = screeningMetadataService;
        this.storageLocationService = storageLocationService;
    }

    // All CREATE operations

    @Override
    @Transactional
    public RecordHateoas save(@NotNull final Record record) {
        validateDocumentMedium(metadataService, record);
        validateScreening(metadataService, record);
        bsmService.validateBSMList(record.getReferenceBSMBase());
        return packAsHateoas(recordRepository.save(record));
    }

    @Override
    public RecordNoteHateoas expandToRecordNote(
            @NotNull final UUID systemId,
            @NotNull final PatchMerge patchMerge) {
        return recordNoteService.expandRecordToRecordNote(
                getRecordOrThrow(systemId), patchMerge);
    }

    @Override
    public RegistryEntryHateoas expandToRegistryEntry(
            @NotNull final UUID systemId,
            @NotNull final PatchMerge patchMerge) {
        return registryEntryService.expandRecordToRegistryEntry(
                getRecordOrThrow(systemId), patchMerge);
    }

    @Override
    @Transactional
    public DocumentDescriptionHateoas
    createDocumentDescriptionAssociatedWithRecord(
            UUID systemId, DocumentDescription documentDescription) {
        Record record = getRecordOrThrow(systemId);
        validateDocumentMedium(metadataService, documentDescription);
        validateScreening(metadataService, documentDescription);
        // Adding 1 as documentNumber starts at 1, not 0
        long documentNumber =
                documentDescriptionRepository.
                        countByReferenceRecord(record) + 1;
        documentDescription.setDocumentNumber((int) documentNumber);
        record.addDocumentDescription(documentDescription);
        documentDescription.setDocumentNumber((int) documentNumber);

        return documentDescriptionService.save(documentDescription);
    }

    /**
     * Create a CorrespondencePartPerson object and associate it with the
     * identified record
     *
     * @param systemId           The systemId of the record object you want to
     *                           create an associated correspondencePartPerson for
     * @param correspondencePart The incoming correspondencePartPerson
     * @return The persisted CorrespondencePartPerson object wrapped as a
     * CorrespondencePartPersonHateoas object
     */
    @Override
    @Transactional
    public CorrespondencePartPersonHateoas
    createCorrespondencePartPersonAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final CorrespondencePartPerson correspondencePart) {
        return correspondencePartService.
                createNewCorrespondencePartPerson(correspondencePart,
                        getRecordOrThrow(systemId));
    }

    @Override
    @Transactional
    public PartPersonHateoas
    createPartPersonAssociatedWithRecord(
            @NotNull final UUID systemId, @NotNull final PartPerson partPerson) {
        return partService.
                createNewPartPerson(partPerson,
                        getRecordOrThrow(systemId));
    }

    @Override
    @Transactional
    public PartUnitHateoas
    createPartUnitAssociatedWithRecord(
            @NotNull final UUID systemId, @NotNull final PartUnit partUnit) {
        return partService.
                createNewPartUnit(partUnit, getRecordOrThrow(systemId));
    }

    /**
     * Create a CorrespondencePartInternal object and associate it with the
     * identified record
     *
     * @param systemId           The systemId of the record object you want to
     *                           create an associated correspondencePartInternal for
     * @param correspondencePart The incoming correspondencePartInternal
     * @return The persisted CorrespondencePartInternal object wrapped as a
     * CorrespondencePartInternalHateoas object
     */
    @Override
    @Transactional
    public CorrespondencePartInternalHateoas
    createCorrespondencePartInternalAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final CorrespondencePartInternal correspondencePart) {
        return correspondencePartService.
                createNewCorrespondencePartInternal(correspondencePart,
                        getRecordOrThrow(systemId));
    }

    /**
     * Create a CorrespondencePartUnit object and associate it with the
     * identified record
     *
     * @param systemId           The systemId of the record object you want to
     *                           create an associated correspondencePartUnit for
     * @param correspondencePart The incoming correspondencePartUnit
     * @return The persisted CorrespondencePartUnit object wrapped as a
     * CorrespondencePartUnitHateoas object
     */
    @Override
    @Transactional
    public CorrespondencePartUnitHateoas
    createCorrespondencePartUnitAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final CorrespondencePartUnit correspondencePart) {
        return correspondencePartService.
                createNewCorrespondencePartUnit(correspondencePart,
                        getRecordOrThrow(systemId));
    }

    @Override
    @Transactional
    public BuildingHateoas
    createBuildingAssociatedWithRecord(
            @NotNull final UUID systemId, @NotNull final Building building) {
        return nationalIdentifierService.
                createNewBuilding(building, getRecordOrThrow(systemId));
    }

    @Override
    @Transactional
    public CadastralUnitHateoas
    createCadastralUnitAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final CadastralUnit cadastralUnit) {
        return nationalIdentifierService.
                createNewCadastralUnit(cadastralUnit, getRecordOrThrow(systemId));
    }

    @Override
    @Transactional
    public DNumberHateoas
    createDNumberAssociatedWithRecord(
            @NotNull final UUID systemId, @NotNull final DNumber dNumber) {
        return nationalIdentifierService.
                createNewDNumber(dNumber, getRecordOrThrow(systemId));
    }

    @Override
    @Transactional
    public PlanHateoas
    createPlanAssociatedWithRecord(
            @NotNull final UUID systemId, @NotNull final Plan plan) {
        return nationalIdentifierService.
                createNewPlan(plan, getRecordOrThrow(systemId));
    }

    @Override
    public PositionHateoas
    createPositionAssociatedWithRecord(
            @NotNull final UUID systemId, @NotNull final Position position) {
        return nationalIdentifierService.
                createNewPosition(position, getRecordOrThrow(systemId));
    }

    @Override
    @Transactional
    public SocialSecurityNumberHateoas
    createSocialSecurityNumberAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final SocialSecurityNumber socialSecurityNumber) {
        return nationalIdentifierService
                .createNewSocialSecurityNumber(socialSecurityNumber,
                        getRecordOrThrow(systemId));
    }

    @Override
    @Transactional
    public UnitHateoas
    createUnitAssociatedWithRecord(
            @NotNull final UUID systemId, @NotNull final Unit unit) {
        return nationalIdentifierService.
                createNewUnit(unit, getRecordOrThrow(systemId));
    }

    @Override
    @Transactional
    public CommentHateoas createCommentAssociatedWithRecord(
            @NotNull final UUID systemId, @NotNull final Comment comment) {
        return commentService.createNewComment
                (comment, getRecordOrThrow(systemId));
    }

    @Override
    @Transactional
    public KeywordHateoas createKeywordAssociatedWithRecord(
            @NotNull final UUID systemId, @NotNull final Keyword keyword) {
        return keywordService
                .createKeywordAssociatedWithRecord(keyword,
                        getRecordOrThrow(systemId));
    }

    @Override
    @Transactional
    public ScreeningMetadataHateoas createScreeningMetadataAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final Metadata screeningMetadata) {
        Record record = getRecordOrThrow(systemId);
        if (null == record.getReferenceScreening()) {
            throw new NoarkEntityNotFoundException(INFO_CANNOT_FIND_OBJECT +
                    " Screening, associated with Record with systemId " +
                    systemId);
        }
        return screeningMetadataService.createScreeningMetadata(
                record.getReferenceScreening(), screeningMetadata);
    }

    @Override
    @Transactional
    public StorageLocationHateoas createStorageLocationAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final StorageLocation storageLocation) {
        Record record = getRecordOrThrow(systemId);
        return storageLocationService
                .createStorageLocationAssociatedWithRecord(
                        storageLocation, record);
    }

    @Override
    @Transactional
    public CrossReferenceHateoas createCrossReferenceAssociatedWithRecord(
            @NotNull final UUID systemId,
            @NotNull final CrossReference crossReference) {
        return crossReferenceService.createCrossReferenceAssociatedWithRecord(
                crossReference, getRecordOrThrow(systemId));
    }

    /**
     * Create a RegistryEntryExpansionHateoas that can be used when expanding a
     * File to a RegistryEntry. None of the File attributes should be present in
     * the returned payload. So we have a special approach that can be used to
     * achieve this.
     *
     * @return RegistryEntryExpansionHateoas
     */
    @Override
    public RegistryEntryExpansionHateoas
    generateDefaultValuesToExpandToRegistryEntry(@NotNull final UUID systemId) {
        return registryEntryService
                .generateDefaultExpandedRegistryEntry(systemId);
    }

    /**
     * Create a RecordNoteExpansionHateoas that can be used when expanding a
     * File to a RecordNote. None of the File attributes should be present in
     * the returned payload. So we have a special approach that can be used to
     * achieve this.
     *
     * @return RecordNoteExpansionHateoas
     */
    @Override
    public RecordNoteExpansionHateoas
    generateDefaultValuesToExpandToRecordNote(@NotNull final UUID systemId) {
        return recordNoteService.generateDefaultExpandedRecordNote(systemId);
    }

    // All READ operations

    @Override
    public AuthorHateoas findAllAuthorWithRecordBySystemId(
            @NotNull final UUID systemId) {
        getRecordOrThrow(systemId);
        return (AuthorHateoas) odataService.processODataQueryGet();
    }

    /**
     * Retrieve all File associated with the record identified by
     * the records systemId.
     *
     * @param systemId systemId of the record
     * @return The parent File packed as a FileHateoas
     */
    @Override
    public FileHateoas
    findFileAssociatedWithRecord(@NotNull final UUID systemId) {
        return (FileHateoas) odataService.processODataQueryGet();
    }

    /**
     * Retrieve all Class associated with the record identified by
     * the records systemId.
     *
     * @param systemId systemId of the record
     * @return The parent Class packed as a ClassHateoas
     */
    @Override
    public ClassHateoas
    findClassAssociatedWithRecord(@NotNull final UUID systemId) {
        return (ClassHateoas) odataService.processODataQueryGet();
    }

    /**
     * Retrieve all Series associated with the record identified by
     * the records systemId.
     *
     * @param systemId systemId of the record
     * @return The parent Series packed as a SeriesHateoas
     */
    @Override
    public SeriesHateoas
    findSeriesAssociatedWithRecord(@NotNull final UUID systemId) {
        return (SeriesHateoas) odataService.processODataQueryGet();
    }

    @Override
    public RecordHateoas findBySystemId(@NotNull final UUID systemId) {
        return packAsHateoas(getRecordOrThrow(systemId));
    }

    @Override
    public RecordHateoas findAll() {
        return (RecordHateoas) odataService.processODataQueryGet();
    }

    @Override
    public ScreeningMetadataHateoas
    getScreeningMetadataAssociatedWithRecord(@NotNull final UUID systemId) {
        Screening screening = getRecordOrThrow(systemId)
                .getReferenceScreening();
        if (null == screening) {
            throw new NoarkEntityNotFoundException(
                    INFO_CANNOT_FIND_OBJECT + " Screening, using systemId " +
                            systemId);
        }
        if (null == screening) {
            throw new NoarkEntityNotFoundException(
                    INFO_CANNOT_FIND_OBJECT + " Screening, using systemId " +
                            systemId);
        }
        NikitaPage page = new
                NikitaPage(copyOf(screening.getReferenceScreeningMetadata()));
        return new ScreeningMetadataHateoas(page);
    }

    @Override
    public CommentHateoas getCommentAssociatedWithRecord(
            @NotNull final UUID systemId) {
        // Make sure the record exists
        getRecordOrThrow(systemId);
        return (CommentHateoas) odataService.processODataQueryGet();
    }

    @Override
    public DocumentDescriptionHateoas
    getDocumentDescriptionAssociatedWithRecord(@NotNull final UUID systemId) {
        // Make sure the record exists
        getRecordOrThrow(systemId);
        return (DocumentDescriptionHateoas) odataService.processODataQueryGet();
    }

    @Override
    public CorrespondencePartHateoas
    getCorrespondencePartAssociatedWithRecord(
            @NotNull final UUID systemId) {
        // Make sure the record exists
        getRecordOrThrow(systemId);
        return (CorrespondencePartHateoas) odataService.processODataQueryGet();
    }

    @Override
    public PartHateoas
    getPartAssociatedWithRecord(@NotNull final UUID systemId) {
        // Make sure the record exists
        getRecordOrThrow(systemId);
        return (PartHateoas) odataService.processODataQueryGet();
    }

    @Override
    public NationalIdentifierHateoas getNationalIdentifierAssociatedWithRecord(
            @NotNull final UUID systemId) {
        // Make sure the record exists
        getRecordOrThrow(systemId);
        return (NationalIdentifierHateoas) odataService.processODataQueryGet();
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
    protected Optional<BSMMetadata> findBSMByName(@NotNull final String name) {
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
     * @param systemId       The systemId of the record object to retrieve
     * @param version        The last known version number (derived from an ETAG)
     * @param incomingRecord The incoming record object
     * @return The updatedRecord after it is persisted
     */
    @Override
    @Transactional
    public RecordHateoas handleUpdate(@NotNull final UUID systemId,
                                      @NotNull final Long version,
                                      @NotNull final Record incomingRecord) {
        bsmService.validateBSMList(incomingRecord.getReferenceBSMBase());
        Record existingRecord = getRecordOrThrow(systemId);
        // Here copy all the values you are allowed to copy ....
        updateTitleAndDescription(incomingRecord, existingRecord);
        if (null != incomingRecord.getDocumentMedium()) {
            existingRecord.setDocumentMedium(
                    incomingRecord.getDocumentMedium());
        }
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingRecord.setVersion(version);
        return packAsHateoas(existingRecord);
    }

    @Override
    @Transactional
    public RecordHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final PatchObjects patchObjects) {
        return packAsHateoas((Record) handlePatch(systemId, patchObjects));
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
            @NotNull final UUID systemId,
            @NotNull final Author author) {
        return authorService.associateAuthorWithRecord
                (author, getRecordOrThrow(systemId));
    }

    @Override
    @Transactional
    public Object associateBSM(@NotNull final UUID systemId,
                               @NotNull final List<BSMBase> bsm) {
        Record record = getRecordOrThrow(systemId);
        record.addReferenceBSMBase(bsm);
        return record;
    }

    // All DELETE operations

    @Override
    @Transactional
    public void deleteRecord(@NotNull final UUID systemId) {
        Record record = getRecordOrThrow(systemId);
        recordRepository.delete(record);
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityDeletedEvent(this, record));
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     */
    @Override
    @Transactional
    public void deleteAllByOwnedBy() {
        recordRepository.deleteByOwnedBy(getUser());
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
    public RecordHateoas generateDefaultRecord(@NotNull final UUID systemId) {
        Record defaultRecord = new Record();
        defaultRecord.setArchivedBy(TEST_USER_CASE_HANDLER_2);
        defaultRecord.setArchivedDate(now());
        defaultRecord.setTitle(TEST_TITLE);
        defaultRecord.setDescription(TEST_DESCRIPTION);
        defaultRecord.setVersion(-1L, true);
        return packAsHateoas(defaultRecord);
    }

    @Override
    public ScreeningMetadataHateoas getDefaultScreeningMetadata(
            @NotNull final UUID systemId) {
        return screeningMetadataService.getDefaultScreeningMetadata(systemId);
    }

    @Override
    public KeywordHateoas generateDefaultKeyword(@NotNull final UUID systemId) {
        return keywordService.generateDefaultKeyword(systemId);
    }

    @Override
    public CommentHateoas generateDefaultComment(@NotNull final UUID systemId) {
        return commentService.generateDefaultComment(systemId);
    }

    /**
     * Generate a Default CorrespondencePartUnit object that can be
     * associated with the identified Record.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param systemId The systemId of the record object
     *                 you wish to create a templated object for
     * @return the CorrespondencePartUnit object wrapped as a
     * CorrespondencePartUnitHateoas object
     */
    @Override
    public CorrespondencePartInternalHateoas
    generateDefaultCorrespondencePartInternal(@NotNull final UUID systemId) {
        return correspondencePartService.
                generateDefaultCorrespondencePartInternal(systemId);
    }

    /**
     * Generate a Default CorrespondencePartUnit object that can be
     * associated with the identified Record.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param systemId The systemId of the record object
     *                 you wish to create a templated object for
     * @return the CorrespondencePartUnit object wrapped as a
     * CorrespondencePartUnitHateoas object
     */
    @Override
    public CorrespondencePartPersonHateoas
    generateDefaultCorrespondencePartPerson(@NotNull final UUID systemId) {
        return correspondencePartService.
                generateDefaultCorrespondencePartPerson(systemId);
    }

    /**
     * Generate a Default PartUnit object that can be
     * associated with the identified Record.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param systemId The systemId of the record object
     *                 you wish to create a templated object for
     * @return the PartUnit object wrapped as a
     * PartUnitHateoas object
     */
    @Override
    public PartPersonHateoas
    generateDefaultPartPerson(@NotNull final UUID systemId) {
        return partService.generateDefaultPartPerson(systemId);
    }

    /**
     * Generate a Default PartUnit object that can be
     * associated with the identified Record.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param systemId The systemId of the record object
     *                 you wish to create a templated object for
     * @return the PartUnit object wrapped as a
     * PartUnitHateoas object
     */
    @Override
    public PartUnitHateoas
    generateDefaultPartUnit(
            UUID systemId) {
        return partService.generateDefaultPartUnit(systemId);
    }

    /**
     * Generate a Default CorrespondencePartUnit object that can be
     * associated with the identified Record.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param systemId The systemId of the record object
     *                 you wish to create a templated object for
     * @return the CorrespondencePartUnit object wrapped as a
     * CorrespondencePartUnitHateoas object
     */
    @Override
    public CorrespondencePartUnitHateoas generateDefaultCorrespondencePartUnit(
            @NotNull final UUID systemId) {
        return correspondencePartService.
                generateDefaultCorrespondencePartUnit(systemId);
    }

    @Override
    public AuthorHateoas generateDefaultAuthor(@NotNull final UUID systemId) {
        return authorService.generateDefaultAuthor(systemId);
    }

    @Override
    public BuildingHateoas generateDefaultBuilding(
            @NotNull final UUID systemId) {
        return nationalIdentifierService.generateDefaultBuilding(systemId);
    }

    @Override
    public CadastralUnitHateoas generateDefaultCadastralUnit(
            @NotNull final UUID systemId) {
        return nationalIdentifierService.generateDefaultCadastralUnit(systemId);
    }

    @Override
    public DNumberHateoas generateDefaultDNumber(@NotNull final UUID systemId) {
        return nationalIdentifierService.generateDefaultDNumber(systemId);
    }

    @Override
    public PlanHateoas generateDefaultPlan(@NotNull final UUID systemId) {
        return nationalIdentifierService.generateDefaultPlan(systemId);
    }

    @Override
    public PositionHateoas generateDefaultPosition(
            @NotNull final UUID systemId) {
        return nationalIdentifierService.generateDefaultPosition(systemId);
    }

    @Override
    public SocialSecurityNumberHateoas generateDefaultSocialSecurityNumber(
            @NotNull final UUID systemId) {
        return nationalIdentifierService
                .generateDefaultSocialSecurityNumber(systemId);
    }

    @Override
    public UnitHateoas generateDefaultUnit(@NotNull final UUID systemId) {
        return nationalIdentifierService.generateDefaultUnit(systemId);
    }

    @Override
    public StorageLocationHateoas getDefaultStorageLocation(
            @NotNull final UUID systemId) {
        return storageLocationService.getDefaultStorageLocation(systemId);
    }

    @Override
    public CrossReferenceHateoas getDefaultCrossReference(
            @NotNull final UUID systemId) {
        return crossReferenceService.getDefaultCrossReference(systemId);
    }

    // All HELPER operations

    public RecordHateoas packAsHateoas(@NotNull final Record record) {
        RecordHateoas recordHateoas = new RecordHateoas(record);
        applyLinksAndHeader(recordHateoas, recordHateoasHandler);
        return recordHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid Record back. If there is no valid
     * Record, an exception is thrown
     *
     * @param systemId the systemId of the record you want to retrieve
     * @return the record
     */
    protected Record getRecordOrThrow(@NotNull final UUID systemId) {
        Record record =
                recordRepository.findBySystemId(systemId);
        if (record == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " Record, using systemId " + systemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return record;
    }
}
