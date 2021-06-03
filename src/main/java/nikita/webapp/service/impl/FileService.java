package nikita.webapp.service.impl;

import nikita.common.model.nikita.NikitaPage;
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
import nikita.common.model.noark5.v5.md_other.BSMMetadata;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.model.noark5.v5.nationalidentifier.*;
import nikita.common.model.noark5.v5.secondary.*;
import nikita.common.repository.n5v5.IFileRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IFileHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IScreeningMetadataHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.interfaces.*;
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

import static java.util.List.copyOf;
import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.*;

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
    private final ICommentService commentService;
    private final IKeywordService keywordService;
    private final ICrossReferenceService crossReferenceService;
    private final IMetadataService metadataService;
    private final INationalIdentifierService nationalIdentifierService;
    private final IPartService partService;
    private final IScreeningMetadataService screeningMetadataService;
    private final IStorageLocationService storageLocationService;
    private final IScreeningMetadataHateoasHandler screeningMetadataHateoasHandler;

    public FileService(EntityManager entityManager,
                       ApplicationEventPublisher applicationEventPublisher,
                       IODataService odataService,
                       IPatchService patchService,
                       IRecordService recordService,
                       ICaseFileService caseFileService,
                       IFileRepository fileRepository,
                       IBSMService bsmService,
                       IFileHateoasHandler fileHateoasHandler,
                       ICommentService commentService,
                       IKeywordService keywordService,
                       ICrossReferenceService crossReferenceService,
                       IMetadataService metadataService,
                       INationalIdentifierService nationalIdentifierService,
                       IPartService partService,
                       IScreeningMetadataService screeningMetadataService,
                       IStorageLocationService storageLocationService,
                       IScreeningMetadataHateoasHandler screeningMetadataHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.recordService = recordService;
        this.caseFileService = caseFileService;
        this.fileRepository = fileRepository;
        this.bsmService = bsmService;
        this.fileHateoasHandler = fileHateoasHandler;
        this.commentService = commentService;
        this.keywordService = keywordService;
        this.crossReferenceService = crossReferenceService;
        this.metadataService = metadataService;
        this.nationalIdentifierService = nationalIdentifierService;
        this.partService = partService;
        this.screeningMetadataService = screeningMetadataService;
        this.storageLocationService = storageLocationService;
        this.screeningMetadataHateoasHandler = screeningMetadataHateoasHandler;
    }

    // All CREATE operations

    @Override
    @Transactional
    public FileHateoas save(File file) {
        validateDocumentMedium(metadataService, file);
        setFinaliseEntityValues(file);
        bsmService.validateBSMList(file.getReferenceBSMBase());
        validateScreening(metadataService, file);
        return packAsHateoas(fileRepository.save(file));
    }

    @Override
    @Transactional
    public FileHateoas createFile(File file) {
        validateDocumentMedium(metadataService, file);
        validateScreening(metadataService, file);
        bsmService.validateBSMList(file.getReferenceBSMBase());
        fileRepository.save(file);
        setFinaliseEntityValues(file);
        return packAsHateoas(file);
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
            UUID systemId, File file) {
        file.setReferenceParentFile(getFileOrThrow(systemId));
        return save(file);
    }

    @Override
    @Transactional
    public CommentHateoas createCommentAssociatedWithFile
            (@NotNull final UUID systemId, Comment comment) {
        return commentService.createNewComment(comment,
                getFileOrThrow(systemId));
    }

    @Override
    @Transactional
    public RecordHateoas createRecordAssociatedWithFile(
            UUID systemId, Record record) {
        record.setReferenceFile(getFileOrThrow(systemId));
        return recordService.save(record);
    }

    @Override
    @Transactional
    public PartPersonHateoas
    createPartPersonAssociatedWithFile(
            @NotNull final UUID systemId, @NotNull PartPerson partPerson) {
        return partService.
                createNewPartPerson(partPerson, getFileOrThrow(systemId));
    }

    @Override
    @Transactional
    public PartUnitHateoas
    createPartUnitAssociatedWithFile(
            @NotNull final UUID systemId, @NotNull PartUnit partUnit) {
        return partService.
                createNewPartUnit(partUnit, getFileOrThrow(systemId));
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
            @NotNull final UUID systemId, @NotNull Building building) {
        return nationalIdentifierService.
                createNewBuilding(building, getFileOrThrow(systemId));
    }

    @Override
    @Transactional
    public CadastralUnitHateoas
    createCadastralUnitAssociatedWithFile(
            @NotNull final UUID systemId, @NotNull CadastralUnit cadastralUnit) {
        return nationalIdentifierService.
                createNewCadastralUnit(cadastralUnit, getFileOrThrow(systemId));
    }

    @Override
    @Transactional
    public DNumberHateoas
    createDNumberAssociatedWithFile(
            @NotNull final UUID systemId, @NotNull DNumber dNumber) {
        return nationalIdentifierService.
                createNewDNumber(dNumber, getFileOrThrow(systemId));
    }

    @Override
    @Transactional
    public PlanHateoas
    createPlanAssociatedWithFile(
            @NotNull final UUID systemId, @NotNull Plan plan) {
        return nationalIdentifierService.
                createNewPlan(plan, getFileOrThrow(systemId));
    }

    @Override
    @Transactional
    public PositionHateoas
    createPositionAssociatedWithFile(
            @NotNull final UUID systemId, @NotNull Position position) {
        return nationalIdentifierService.
                createNewPosition(position, getFileOrThrow(systemId));
    }

    @Override
    @Transactional
    public SocialSecurityNumberHateoas
    createSocialSecurityNumberAssociatedWithFile
            (@NotNull final UUID systemId,
             @NotNull SocialSecurityNumber socialSecurityNumber) {
        return nationalIdentifierService.
                createNewSocialSecurityNumber(socialSecurityNumber,
                        getFileOrThrow(systemId));
    }

    @Override
    @Transactional
    public UnitHateoas
    createUnitAssociatedWithFile(
            @NotNull final UUID systemId, @NotNull Unit unit) {
        return nationalIdentifierService.
                createNewUnit(unit, getFileOrThrow(systemId));
    }

    @Override
    @Transactional
    public CaseFileHateoas expandToCaseFile(
            @NotNull final UUID systemId, @NotNull PatchMerge patchMerge) {
        return caseFileService.expandFileAsCaseFileHateoas(
                getFileOrThrow(systemId), patchMerge);
    }

    @Override
    @Transactional
    public StorageLocationHateoas createStorageLocationAssociatedWithFile(
            UUID systemId, StorageLocation storageLocation) {
        File file = getFileOrThrow(systemId);
        return storageLocationService
                .createStorageLocationAssociatedWithFile(
                        storageLocation, file);
    }

    /**
     * Retrieve a list of children file belonging to the file object
     * identified by systemId
     *
     * @param systemId The systemId of the File object to retrieve its children
     * @return A FileHateoas object containing the children file's
     */
    @Override
    public FileHateoas findAllChildren(@NotNull final UUID systemId) {
        // Make sure the file exists
        getFileOrThrow(systemId);
        return (FileHateoas) odataService.processODataQueryGet();
    }

    /**
     * Retrieve a list of children file belonging to the file object
     * identified by systemId
     *
     * @param systemId The systemId of the File object to retrieve its children
     * @return A FileHateoas object containing the children file's
     */
    @Override
    public RecordHateoas findAllRecords(@NotNull final UUID systemId) {
        // Make sure the file exists
        getFileOrThrow(systemId);
        return (RecordHateoas) odataService.processODataQueryGet();
    }

    @Override
    public PartPersonHateoas generateDefaultPartPerson(@NotNull final UUID systemId) {
        return partService.generateDefaultPartPerson(systemId);
    }

    @Override
    public PartUnitHateoas generateDefaultPartUnit(@NotNull final UUID systemId) {
        return partService.generateDefaultPartUnit(systemId);
    }

    @Override
    public CaseFileExpansionHateoas generateDefaultValuesToExpandToCaseFile(
            @NotNull final UUID systemId) {
        return caseFileService.generateDefaultExpandedCaseFile();
    }

    @Override
    public FileHateoas findAll() {
        return (FileHateoas) odataService.processODataQueryGet();
    }

    @Override
    public CommentHateoas getCommentAssociatedWithFile(
            @NotNull final UUID systemId) {
        // Make sure the file exists
        getFileOrThrow(systemId);
        return (CommentHateoas) odataService.processODataQueryGet();
    }

    @Override
    public PartHateoas getPartAssociatedWithFile(
            @NotNull final UUID systemId) {
        // Make sure the file exists
        getFileOrThrow(systemId);
        return (PartHateoas) odataService.processODataQueryGet();
    }

    @Override
    public KeywordHateoas findKeywordAssociatedWithFile(
            @NotNull final UUID systemId) {
        getFileOrThrow(systemId);
        return (KeywordHateoas) odataService.processODataQueryGet();
    }

    @Override
    public CrossReferenceHateoas findCrossReferenceAssociatedWithFile(
            @NotNull final UUID systemId) {
        return (CrossReferenceHateoas) odataService.processODataQueryGet();
    }

    @Override
    public StorageLocationHateoas getStorageLocationAssociatedWithFile(
            @NotNull final UUID systemId) {
        getFileOrThrow(systemId);
        return (StorageLocationHateoas) odataService.processODataQueryGet();
    }

    @Override
    public NationalIdentifierHateoas getNationalIdentifierAssociatedWithFile(
            @NotNull final UUID systemId) {
        return (NationalIdentifierHateoas) odataService.processODataQueryGet();
    }

    @Override
    public ScreeningMetadataHateoas
    getScreeningMetadataAssociatedWithFile(@NotNull final UUID systemId) {
        Screening screening = getFileOrThrow(systemId)
                .getReferenceScreening();
        if (null == screening) {
            throw new NoarkEntityNotFoundException(
                    INFO_CANNOT_FIND_OBJECT + " Screening, using systemId " +
                            systemId);
        }
        return packAsHateoas(new NikitaPage(copyOf(
                screening.getReferenceScreeningMetadata())));
    }

    @Override
    public FileHateoas findBySystemId(@NotNull final UUID systemId) {
        return packAsHateoas(getFileOrThrow(systemId));
    }

    /**
     * Retrieve all Class associated with the file identified by
     * the files systemId.
     *
     * @param systemId systemId of the file
     * @return The parent Class packed as a ClassHateoas
     */
    @Override
    public ClassHateoas
    findClassAssociatedWithFile(@NotNull final UUID systemId) {
        return (ClassHateoas) odataService.processODataQueryGet();
    }

    /**
     * Retrieve all Series associated with the file identified by
     * the files systemId.
     *
     * @param systemId systemId of the file
     * @return The parent Series packed as a SeriesHateoas
     */
    @Override
    public SeriesHateoas
    findSeriesAssociatedWithFile(@NotNull final UUID systemId) {
        return (SeriesHateoas) odataService.processODataQueryGet();
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
    public FileHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final Long version,
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
        return packAsHateoas(existingFile);
    }

    @Transactional
    public FileHateoas update(File file) {
        bsmService.validateBSMList(file.getReferenceBSMBase());
        return packAsHateoas(file);
    }

    @Override
    @Transactional
    public FileHateoas handleUpdate(
            UUID systemId, PatchObjects patchObjects) {
        return packAsHateoas((File) handlePatch(systemId, patchObjects));
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
    public Object associateBSM(@NotNull final UUID systemId,
                               @NotNull List<BSMBase> bsm) {
        File file = getFileOrThrow(systemId);
        file.addReferenceBSMBase(bsm);
        return file;
    }

    // All DELETE operations

    @Override
    @Transactional
    public void deleteEntity(@NotNull final UUID systemId) {
        File file = getFileOrThrow(systemId);
        fileRepository.delete(file);
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityDeletedEvent(this, file));
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     */
    @Override
    @Transactional
    public void deleteAllByOwnedBy() {
        fileRepository.deleteByOwnedBy(getUser());
    }

    // All template operations

    @Override
    public StorageLocationHateoas getDefaultStorageLocation(@NotNull final UUID systemId) {
        return storageLocationService.getDefaultStorageLocation(systemId);
    }

    @Override
    public CrossReferenceHateoas getDefaultCrossReference(
            @NotNull final UUID systemId) {
        return crossReferenceService.getDefaultCrossReference(systemId);
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
    public FileHateoas generateDefaultFile(@NotNull final UUID systemId) {
        File defaultFile = new File();
        defaultFile.setVersion(-1L, true);
        return packAsHateoas(defaultFile);
    }

    @Override
    public ScreeningMetadataHateoas getDefaultScreeningMetadata(
            @NotNull final UUID systemId) {
        return screeningMetadataService.getDefaultScreeningMetadata(systemId);
    }

    @Override
    public CommentHateoas generateDefaultComment(@NotNull final UUID systemId) {
        return commentService.generateDefaultComment(systemId);
    }

    @Override
    public KeywordHateoas generateDefaultKeyword(@NotNull final UUID systemId) {
        return keywordService.generateDefaultKeyword(systemId);
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

    // All HELPER operations

    public ScreeningMetadataHateoas packAsHateoas(NikitaPage page) {
        ScreeningMetadataHateoas screeningMetadataHateoas =
                new ScreeningMetadataHateoas(page);
        applyLinksAndHeader(screeningMetadataHateoas,
                screeningMetadataHateoasHandler);
        return screeningMetadataHateoas;
    }

    public FileHateoas packAsHateoas(@NotNull final File file) {
        FileHateoas fileHateoas = new FileHateoas(file);
        applyLinksAndHeader(fileHateoas, fileHateoasHandler);
        return fileHateoas;
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

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. Note. If you call this, be aware
     * that you will only ever get a valid File back. If there is no valid
     * File, an exception is thrown
     *
     * @param systemId systemId of the file object you are looking for
     * @return the newly found file object or null if it does not exist
     */
    public File getFileOrThrow(@NotNull final UUID systemId) {
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
