package nikita.webapp.service.impl;

import nikita.common.model.nikita.PatchMerge;
import nikita.common.model.nikita.PatchObjects;
import nikita.common.model.noark5.bsm.BSMBase;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.ClassHateoas;
import nikita.common.model.noark5.v5.hateoas.FileHateoas;
import nikita.common.model.noark5.v5.hateoas.RecordHateoas;
import nikita.common.model.noark5.v5.hateoas.SeriesHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileExpansionHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.*;
import nikita.common.model.noark5.v5.hateoas.secondary.*;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.md_other.BSMMetadata;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.model.noark5.v5.nationalidentifier.*;
import nikita.common.model.noark5.v5.secondary.*;
import nikita.common.repository.n5v5.IFileRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IClassHateoasHandler;
import nikita.webapp.hateoas.interfaces.IFileHateoasHandler;
import nikita.webapp.hateoas.interfaces.ISeriesHateoasHandler;
import nikita.webapp.hateoas.interfaces.nationalidentifier.INationalIdentifierHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.ICommentHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IPartHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IScreeningMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.interfaces.*;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.secondary.*;
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
import java.util.Set;
import java.util.UUID;

import static java.util.List.copyOf;
import static java.util.UUID.fromString;
import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.*;
import static org.springframework.http.HttpStatus.OK;

@Service
public class FileService
        extends NoarkService
        implements IFileService {

    private static final Logger logger =
            LoggerFactory.getLogger(FileService.class);

    private final IRecordService recordService;
    private final ICaseFileService caseFileService;
    private final IFileRepository fileRepository;
    private final IBSMService bsmService;
    private final IFileHateoasHandler fileHateoasHandler;
    private final ISeriesHateoasHandler seriesHateoasHandler;
    private final IClassHateoasHandler classHateoasHandler;
    private final ICommentService commentService;
    private final IKeywordService keywordService;
    private final ICrossReferenceService crossReferenceService;
    private final IMetadataService metadataService;
    private final INationalIdentifierService nationalIdentifierService;
    private final IPartService partService;
    private final ICommentHateoasHandler commentHateoasHandler;
    private final INationalIdentifierHateoasHandler nationalIdentifierHateoasHandler;
    private final IPartHateoasHandler partHateoasHandler;
    private final IScreeningMetadataService screeningMetadataService;
    private final IScreeningMetadataHateoasHandler screeningMetadataHateoasHandler;
    private final IStorageLocationService storageLocationService;

    public FileService(EntityManager entityManager,
                       ApplicationEventPublisher applicationEventPublisher,
                       IPatchService patchService,
                       IRecordService recordService,
                       ICaseFileService caseFileService,
                       IFileRepository fileRepository,
                       IBSMService bsmService,
                       IFileHateoasHandler fileHateoasHandler,
                       ISeriesHateoasHandler seriesHateoasHandler,
                       IClassHateoasHandler classHateoasHandler,
                       ICommentService commentService,
                       IKeywordService keywordService,
                       ICrossReferenceService crossReferenceService,
                       IMetadataService metadataService,
                       INationalIdentifierService nationalIdentifierService,
                       IPartService partService,
                       ICommentHateoasHandler commentHateoasHandler,
                       INationalIdentifierHateoasHandler nationalIdentifierHateoasHandler,
                       IPartHateoasHandler partHateoasHandler,
                       IScreeningMetadataService screeningMetadataService,
                       IScreeningMetadataHateoasHandler screeningMetadataHateoasHandler,
                       IStorageLocationService storageLocationService) {
        super(entityManager, applicationEventPublisher, patchService);
        this.recordService = recordService;
        this.caseFileService = caseFileService;
        this.fileRepository = fileRepository;
        this.bsmService = bsmService;
        this.fileHateoasHandler = fileHateoasHandler;
        this.seriesHateoasHandler = seriesHateoasHandler;
        this.classHateoasHandler = classHateoasHandler;
        this.commentService = commentService;
        this.keywordService = keywordService;
        this.crossReferenceService = crossReferenceService;
        this.metadataService = metadataService;
        this.nationalIdentifierService = nationalIdentifierService;
        this.partService = partService;
        this.commentHateoasHandler = commentHateoasHandler;
        this.nationalIdentifierHateoasHandler = nationalIdentifierHateoasHandler;
        this.partHateoasHandler = partHateoasHandler;
        this.screeningMetadataService = screeningMetadataService;
        this.screeningMetadataHateoasHandler = screeningMetadataHateoasHandler;
        this.storageLocationService = storageLocationService;
    }

    // All CREATE operations

    @Override
    @Transactional
    public FileHateoas save(File file) {
        validateDocumentMedium(metadataService, file);
        setFinaliseEntityValues(file);
        bsmService.validateBSMList(file.getReferenceBSMBase());
        validateScreening(metadataService, file);
        FileHateoas fileHateoas = new FileHateoas(fileRepository.save(file));
        fileHateoasHandler.addLinks(fileHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(this, file));
        return fileHateoas;
    }

    @Override
    @Transactional
    public File createFile(File file) {
        validateDocumentMedium(metadataService, file);
        validateScreening(metadataService, file);
        bsmService.validateBSMList(file.getReferenceBSMBase());
        fileRepository.save(file);
        setFinaliseEntityValues(file);
        return file;
    }

    /**
     * Persists a new file object to the database as a sub-file to an
     * existing file object. Some values are set in the incoming
     * payload (e.g. title) while some are set by the core.  owner,
     * createdBy, createdDate are automatically set by the core.
     *
     * @param systemId systemId of the parent object to connect this
     *                 file as a child to
     * @param file     The file object object with some values set
     * @return the newly persisted file object wrapped as a fileHateaos object
     */
    @Override
    @Transactional
    public FileHateoas createFileAssociatedWithFile(
            String systemId, File file) {
        file.setReferenceParentFile(getFileOrThrow(systemId));
        return save(file);
    }

    @Override
    @Transactional
    public CommentHateoas createCommentAssociatedWithFile
            (String systemID, Comment comment) {
        return commentService.createNewComment(comment,
                getFileOrThrow(systemID));
    }

    @Override
    @Transactional
    public ResponseEntity<RecordHateoas> createRecordAssociatedWithFile(
            String fileSystemId, Record record) {
        record.setReferenceFile(getFileOrThrow(fileSystemId));
        return recordService.save(record);
    }

    @Override
    @Transactional
    public PartPersonHateoas
    createPartPersonAssociatedWithFile(
            @NotNull String systemID, @NotNull PartPerson partPerson) {
        return partService.
                createNewPartPerson(partPerson, getFileOrThrow(systemID));
    }

    @Override
    @Transactional
    public PartUnitHateoas
    createPartUnitAssociatedWithFile(
            @NotNull String systemID, @NotNull PartUnit partUnit) {
        return partService.
                createNewPartUnit(partUnit, getFileOrThrow(systemID));
    }

    @Override
    public KeywordHateoas createKeywordAssociatedWithFile(
            UUID systemId, Keyword keyword) {
        return keywordService.createKeywordAssociatedWithFile(keyword,
                getFileOrThrow(systemId));
    }

    @Override
    @Transactional
    public BuildingHateoas
    createBuildingAssociatedWithFile(
            @NotNull String systemID, @NotNull Building building) {
        return nationalIdentifierService.
                createNewBuilding(building, getFileOrThrow(systemID));
    }

    @Override
    @Transactional
    public CadastralUnitHateoas
    createCadastralUnitAssociatedWithFile(
            @NotNull String systemID, @NotNull CadastralUnit cadastralUnit) {
        return nationalIdentifierService.
                createNewCadastralUnit(cadastralUnit, getFileOrThrow(systemID));
    }

    @Override
    @Transactional
    public DNumberHateoas
    createDNumberAssociatedWithFile(
            @NotNull String systemID, @NotNull DNumber dNumber) {
        return nationalIdentifierService.
                createNewDNumber(dNumber, getFileOrThrow(systemID));
    }

    @Override
    @Transactional
    public PlanHateoas
    createPlanAssociatedWithFile(
            @NotNull String systemID, @NotNull Plan plan) {
        return nationalIdentifierService.
                createNewPlan(plan, getFileOrThrow(systemID));
    }

    @Override
    @Transactional
    public PositionHateoas
    createPositionAssociatedWithFile(
            @NotNull String systemID, @NotNull Position position) {
        return nationalIdentifierService.
                createNewPosition(position, getFileOrThrow(systemID));
    }

    @Override
    @Transactional
    public SocialSecurityNumberHateoas
    createSocialSecurityNumberAssociatedWithFile
            (@NotNull String systemID,
             @NotNull SocialSecurityNumber socialSecurityNumber) {
        return nationalIdentifierService.
                createNewSocialSecurityNumber(socialSecurityNumber,
                        getFileOrThrow(systemID));
    }

    @Override
    @Transactional
    public UnitHateoas
    createUnitAssociatedWithFile(
            @NotNull String systemID, @NotNull Unit unit) {
        return nationalIdentifierService.
                createNewUnit(unit, getFileOrThrow(systemID));
    }

    @Override
    @Transactional
    public CaseFileHateoas expandToCaseFile(
            @NotNull UUID systemId, @NotNull PatchMerge patchMerge) {
        return caseFileService.expandFileAsCaseFileHateoas(
                getFileOrThrow(systemId), patchMerge);
    }

    @Override
    @Transactional
    public StorageLocationHateoas createStorageLocationAssociatedWithFile(
            UUID systemId, StorageLocation storageLocation) {
        File file = getFileOrThrow(systemId.toString());
        return storageLocationService
                .createStorageLocationAssociatedWithFile(
                        storageLocation, file);
    }

    // All READ operations.

    public List<File> findAll() {
        return fileRepository.findAll();
    }

    /**
     * Retrieve a list of children file belonging to the file object
     * identified by systemId
     *
     * @param systemId The systemId of the File object to retrieve its children
     * @return A FileHateoas object containing the children file's
     */
    @Override
    public FileHateoas findAllChildren(@NotNull String systemId) {
        FileHateoas fileHateoas = new
                FileHateoas((List<INoarkEntity>)
                (List) getFileOrThrow(systemId).getReferenceChildFile());
        fileHateoasHandler.addLinks(fileHateoas, new Authorisation());
        return fileHateoas;
    }

    @Override
    public PartPersonHateoas generateDefaultPartPerson(String systemID) {
        return partService.generateDefaultPartPerson(systemID);
    }

    @Override
    public PartUnitHateoas generateDefaultPartUnit(String systemID) {
        return partService.generateDefaultPartUnit(systemID);
    }

    @Override
    public CaseFileExpansionHateoas generateDefaultValuesToExpandToCaseFile(
            @NotNull final UUID systemId) {
        return caseFileService.generateDefaultExpandedCaseFile();
    }

    public List<File> findByOwnedBy(String ownedBy) {
        ownedBy = (ownedBy == null) ? getUser() : ownedBy;
        return fileRepository.findByOwnedBy(ownedBy);
    }

    @Override
    public CommentHateoas getCommentAssociatedWithFile(
            @NotNull final String systemID) {
        CommentHateoas commentHateoas = new CommentHateoas(
                copyOf(getFileOrThrow(systemID).getReferenceComment()));
        commentHateoasHandler.addLinks(commentHateoas, new Authorisation());
        return commentHateoas;
    }

    @Override
    public PartHateoas getPartAssociatedWithFile(
            @NotNull final String systemID) {
        PartHateoas partHateoas = new PartHateoas(
                copyOf(getFileOrThrow(systemID).getReferencePart()));
        partHateoasHandler.addLinks(partHateoas, new Authorisation());
        return partHateoas;
    }

    @Override
    public NationalIdentifierHateoas getNationalIdentifierAssociatedWithFile(
            @NotNull final String systemID) {
        NationalIdentifierHateoas niHateoas = new NationalIdentifierHateoas(
                (List<INoarkEntity>) (List) getFileOrThrow(systemID).
                        getReferenceNationalIdentifier());
        nationalIdentifierHateoasHandler
                .addLinks(niHateoas, new Authorisation());
        return niHateoas;
    }

    @Override
    public ScreeningMetadataHateoas
    getScreeningMetadataAssociatedWithFile(UUID fileSystemId) {
        Screening screening = getFileOrThrow(fileSystemId)
                .getReferenceScreening();
        if (null == screening) {
            throw new NoarkEntityNotFoundException(
                    INFO_CANNOT_FIND_OBJECT + " Screening, using systemId " +
                            fileSystemId);
        }
        Set<ScreeningMetadataLocal> screeningMetadata =
                screening.getReferenceScreeningMetadata();
        ScreeningMetadataHateoas screeningMetadataHateoas =
                new ScreeningMetadataHateoas(copyOf(screeningMetadata));
        screeningMetadataHateoasHandler.addLinks(screeningMetadataHateoas,
                new Authorisation());
        return screeningMetadataHateoas;
    }

    @Override
    public File findBySystemId(String systemId) {
        return getFileOrThrow(systemId);
    }

    /**
     * Retrieve all Class associated with the file identified by
     * the files systemId.
     *
     * @param systemId systemId of the file
     * @return The parent Class packed as a ResponseEntity
     */
    @Override
    public ResponseEntity<ClassHateoas>
    findClassAssociatedWithFile(@NotNull final String systemId) {
        ClassHateoas classHateoas = new ClassHateoas(
                fileRepository.findBySystemId(fromString(systemId)).
                        getReferenceClass());

        classHateoasHandler.addLinks(classHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(classHateoas.getEntityVersion().toString())
                .body(classHateoas);
    }

    /**
     * Retrieve all Series associated with the file identified by
     * the files systemId.
     *
     * @param systemId systemId of the file
     * @return The parent Series packed as a ResponseEntity
     */
    @Override
    public ResponseEntity<SeriesHateoas>
    findSeriesAssociatedWithFile(@NotNull final String systemId) {
        SeriesHateoas seriesHateoas = new SeriesHateoas(
                fileRepository.findBySystemId(fromString(systemId)).
                        getReferenceSeries());

        seriesHateoasHandler.addLinks(seriesHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(seriesHateoas.getEntityVersion().toString())
                .body(seriesHateoas);
    }

    // All UPDATE operations

    /**
     * Updates a DocumentDescription object in the database. First we try to
     * locate the DocumentDescription object. If the DocumentDescription object
     * does not exist a NoarkEntityNotFoundException exception is thrown that
     * the caller has to deal with.
     * <br>
     * After this the values you are allowed to update are copied from the
     * incomingDocumentDescription object to the existingDocumentDescription
     * object and the existingDocumentDescription object will be persisted to
     * the database when the transaction boundary is over.
     * <p>
     * Note, the version corresponds to the version number, when the object
     * was initially retrieved from the database. If this number is not the
     * same as the version number when re-retrieving the DocumentDescription
     * object from the database a NoarkConcurrencyException is thrown. Note.
     * This happens when the call to DocumentDescription.setVersion() occurs.
     * <p>
     * Note: title is not nullable
     *
     * @param systemId     systemId of File to update
     * @param version      ETAG value
     * @param incomingFile the incoming file
     * @return the updated File object after it is persisted
     */
    @Override
    @Transactional
    public File handleUpdate(@NotNull String systemId, @NotNull Long version,
                             @NotNull File incomingFile) {
        File existingFile = getFileOrThrow(systemId);
        // Here copy all the values you are allowed to copy ....
        updateTitleAndDescription(incomingFile, existingFile);
        if (null != incomingFile.getDocumentMedium()) {
            existingFile.setDocumentMedium(
                    incomingFile.getDocumentMedium());
        }
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingFile.setVersion(version);
        fileRepository.save(existingFile);
        return existingFile;
    }

    @Transactional
    public FileHateoas update(File file) {
        FileHateoas fileHateoas = new FileHateoas(fileRepository.save(file));
        bsmService.validateBSMList(file.getReferenceBSMBase());
        fileHateoasHandler.addLinks(fileHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this, file));
        return fileHateoas;
    }

    @Override
    @Transactional
    public ResponseEntity<FileHateoas> handleUpdate(
            UUID systemID, PatchObjects patchObjects) {
        File file = (File) handlePatch(systemID, patchObjects);
        FileHateoas fileHateoas = new FileHateoas(file);
        fileHateoasHandler.addLinks(fileHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this, file));
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(fileHateoas.getEntityVersion().toString())
                .body(fileHateoas);
    }

    @Override
    @Transactional
    public ScreeningMetadataHateoas createScreeningMetadataAssociatedWithFile(
            UUID systemId, Metadata screeningMetadata) {
        File file = getFileOrThrow(systemId);
        if (null == file.getReferenceScreening()) {
            throw new NoarkEntityNotFoundException(INFO_CANNOT_FIND_OBJECT +
                    " Screening, associated with File with systemId " +
                    systemId);
        }
        return screeningMetadataService.createScreeningMetadata(
                file.getReferenceScreening(), screeningMetadata);
    }

    @Override
    @Transactional
    public CrossReferenceHateoas createCrossReferenceAssociatedWithFile(
            @NotNull final UUID systemId,
            @NotNull final CrossReference crossReference) {
        return crossReferenceService.createCrossReferenceAssociatedWithFile(
                crossReference, getFileOrThrow(systemId));
    }

    @Override
    @Transactional
    public Object associateBSM(@NotNull UUID systemId,
                               @NotNull List<BSMBase> bsm) {
        File file = getFileOrThrow(systemId);
        file.addReferenceBSMBase(bsm);
        return file;
    }

    // All DELETE operations

    @Override
    @Transactional
    public void deleteEntity(@NotNull String fileSystemId) {
        File file = getFileOrThrow(fileSystemId);
        fileRepository.delete(file);
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityDeletedEvent(this, file));
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     *
     * @return the number of objects deleted
     */
    @Override
    @Transactional
    public long deleteAllByOwnedBy() {
        return fileRepository.deleteByOwnedBy(getUser());
    }

    // All template operations

    @Override
    public StorageLocationHateoas getDefaultStorageLocation(UUID systemId) {
        return storageLocationService.getDefaultStorageLocation(systemId);
    }

    @Override
    public CrossReferenceHateoas getDefaultCrossReference() {
        return crossReferenceService.getDefaultCrossReference();
    }

    /**
     * Generate a Default File object
     * <br>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @return the File object wrapped as a FileHateoas object
     */
    @Override
    public FileHateoas generateDefaultFile() {
        File defaultFile = new File();
        //defaultFile.setReferenceScreening(generateDefaultScreening());
        FileHateoas fileHateoas = new FileHateoas(defaultFile);
        fileHateoasHandler.addLinksOnTemplate(fileHateoas, new Authorisation());
        return fileHateoas;
    }

    @Override
    public ScreeningMetadataHateoas getDefaultScreeningMetadata(UUID systemId) {
        return screeningMetadataService.getDefaultScreeningMetadata(systemId);
    }

    @Override
    public CommentHateoas generateDefaultComment() {
        return commentService.generateDefaultComment();
    }

    @Override
    public KeywordHateoas generateDefaultKeyword() {
        return keywordService.generateDefaultKeyword();
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

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. Note. If you call this, be aware
     * that you will only ever get a valid File back. If there is no valid
     * File, an exception is thrown
     *
     * @param systemId systemId of the file object you are looking for
     * @return the newly found file object or null if it does not exist
     */
    public File getFileOrThrow(@NotNull String systemId) {
        return getFileOrThrow(fromString(systemId));
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. Note. If you call this, be aware
     * that you will only ever get a valid File back. If there is no valid
     * File, an exception is thrown
     *
     * @param systemId systemId of the file object you are looking for
     * @return the newly found file object or null if it does not exist
     */
    public File getFileOrThrow(@NotNull UUID systemId) {
        File file = fileRepository.findBySystemId(systemId);
        if (file == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " File, using systemId " +
                    systemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return file;
    }
}
