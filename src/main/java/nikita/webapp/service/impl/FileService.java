package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.nationalidentifier.*;
import nikita.common.model.noark5.v5.hateoas.*;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.*;
import nikita.common.model.noark5.v5.hateoas.secondary.CommentHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PartHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PartPersonHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PartUnitHateoas;
import nikita.common.model.noark5.v5.secondary.Comment;
import nikita.common.model.noark5.v5.secondary.PartPerson;
import nikita.common.model.noark5.v5.secondary.PartUnit;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.repository.n5v5.IFileRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IClassHateoasHandler;
import nikita.webapp.hateoas.interfaces.IFileHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IPartHateoasHandler;
import nikita.webapp.hateoas.interfaces.ISeriesHateoasHandler;
import nikita.webapp.hateoas.interfaces.nationalidentifier.INationalIdentifierHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.ICommentHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IFileService;
import nikita.webapp.service.interfaces.INationalIdentifierService;
import nikita.webapp.service.interfaces.IRecordService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.secondary.ICommentService;
import nikita.webapp.service.interfaces.secondary.IPartService;
import nikita.webapp.web.events.AfterNoarkEntityCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.setFinaliseEntityValues;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.validateDocumentMedium;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
public class FileService
        extends NoarkService
        implements IFileService {

    private static final Logger logger =
            LoggerFactory.getLogger(FileService.class);

    private IRecordService recordService;
    private IFileRepository fileRepository;
    private IFileHateoasHandler fileHateoasHandler;
    private ISeriesHateoasHandler seriesHateoasHandler;
    private IClassHateoasHandler classHateoasHandler;
    private ICommentService commentService;
    private IMetadataService metadataService;
    private INationalIdentifierService nationalIdentifierService;
    private IPartService partService;
    private ICommentHateoasHandler commentHateoasHandler;
    private INationalIdentifierHateoasHandler nationalIdentifierHateoasHandler;
    private IPartHateoasHandler partHateoasHandler;

    public FileService(EntityManager entityManager,
                       ApplicationEventPublisher applicationEventPublisher,
                       IRecordService recordService,
                       IFileRepository fileRepository,
                       IFileHateoasHandler fileHateoasHandler,
                       ISeriesHateoasHandler seriesHateoasHandler,
                       IClassHateoasHandler classHateoasHandler,
                       ICommentService commentService,
                       IMetadataService metadataService,
                       INationalIdentifierService nationalIdentifierService,
                       IPartService partService,
                       ICommentHateoasHandler commentHateoasHandler,
                       INationalIdentifierHateoasHandler nationalIdentifierHateoasHandler,
                       IPartHateoasHandler partHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.recordService = recordService;
        this.fileRepository = fileRepository;
        this.fileHateoasHandler = fileHateoasHandler;
        this.seriesHateoasHandler = seriesHateoasHandler;
        this.classHateoasHandler = classHateoasHandler;
        this.commentService = commentService;
        this.metadataService = metadataService;
        this.nationalIdentifierService = nationalIdentifierService;
        this.partService = partService;
        this.commentHateoasHandler = commentHateoasHandler;
        this.nationalIdentifierHateoasHandler = nationalIdentifierHateoasHandler;
        this.partHateoasHandler = partHateoasHandler;
    }

    public FileHateoas save(File file) {
        validateDocumentMedium(metadataService, file);
        setFinaliseEntityValues(file);
        FileHateoas fileHateoas = new FileHateoas(fileRepository.save(file));
        fileHateoasHandler.addLinks(fileHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(this, file));
        return fileHateoas;
    }

    @Override
    public File createFile(File file) {
        validateDocumentMedium(metadataService, file);
        setFinaliseEntityValues(file);
        return fileRepository.save(file);
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
    public FileHateoas createFileAssociatedWithFile(
            String systemId, File file) {
        file.setReferenceParentFile(getFileOrThrow(systemId));
        return save(file);
    }

    @Override
    public CommentHateoas createCommentAssociatedWithFile
        (String systemID, Comment comment) {
        return commentService.createNewComment(comment,
                                               getFileOrThrow(systemID));
    }

    @Override
    public ResponseEntity<RecordHateoas> createRecordAssociatedWithFile(
            String fileSystemId, Record record) {
        record.setReferenceFile(getFileOrThrow(fileSystemId));
        return recordService.save(record);
    }

    @Override
    public PartPersonHateoas
    createPartPersonAssociatedWithFile(
            @NotNull String systemID, @NotNull PartPerson partPerson) {
        return partService.
                createNewPartPerson(partPerson, getFileOrThrow(systemID));
    }

    @Override
    public PartUnitHateoas
    createPartUnitAssociatedWithFile(
            @NotNull String systemID, @NotNull PartUnit partUnit) {
        return partService.
                createNewPartUnit(partUnit, getFileOrThrow(systemID));
    }

    @Override
    public BuildingHateoas
    createBuildingAssociatedWithFile(
            @NotNull String systemID, @NotNull Building building) {
        return nationalIdentifierService.
                createNewBuilding(building, getFileOrThrow(systemID));
    }

    @Override
    public CadastralUnitHateoas
    createCadastralUnitAssociatedWithFile(
            @NotNull String systemID, @NotNull CadastralUnit cadastralUnit) {
        return nationalIdentifierService.
                createNewCadastralUnit(cadastralUnit, getFileOrThrow(systemID));
    }

    @Override
    public DNumberHateoas
    createDNumberAssociatedWithFile(
            @NotNull String systemID, @NotNull DNumber dNumber) {
        return nationalIdentifierService.
                createNewDNumber(dNumber, getFileOrThrow(systemID));
    }

    @Override
    public PlanHateoas
    createPlanAssociatedWithFile(
            @NotNull String systemID, @NotNull Plan plan) {
        return nationalIdentifierService.
                createNewPlan(plan, getFileOrThrow(systemID));
    }

    @Override
    public PositionHateoas
    createPositionAssociatedWithFile(
            @NotNull String systemID, @NotNull Position position) {
        return nationalIdentifierService.
                createNewPosition(position, getFileOrThrow(systemID));
    }

    @Override
    public SocialSecurityNumberHateoas
    createSocialSecurityNumberAssociatedWithFile
        (@NotNull String systemID,
         @NotNull SocialSecurityNumber socialSecurityNumber) {
        return nationalIdentifierService.
                createNewSocialSecurityNumber(socialSecurityNumber,
                                              getFileOrThrow(systemID));
    }

    @Override
    public UnitHateoas
    createUnitAssociatedWithFile(
            @NotNull String systemID, @NotNull Unit unit) {
        return nationalIdentifierService.
                createNewUnit(unit, getFileOrThrow(systemID));
    }

    // All READ operations
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
    @SuppressWarnings("unchecked")
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

    // ownedBy
    public List<File> findByOwnedBy(String ownedBy) {
        ownedBy = (ownedBy == null) ? getUser() : ownedBy;
        return fileRepository.findByOwnedBy(ownedBy);
    }

    @Override
    public CommentHateoas getCommentAssociatedWithFile(
            @NotNull final String systemID) {
        CommentHateoas commentHateoas = new CommentHateoas(
                (List<INoarkEntity>) (List) getFileOrThrow(systemID).
                        getReferenceComment());
        commentHateoasHandler.addLinks(commentHateoas, new Authorisation());
        return commentHateoas;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PartHateoas getPartAssociatedWithFile(
            @NotNull final String systemID) {
        PartHateoas partHateoas = new PartHateoas(
                (List<INoarkEntity>) (List) getFileOrThrow(systemID).
                        getReferencePart());
        partHateoasHandler.addLinks(partHateoas, new Authorisation());
        return partHateoas;
    }

    @Override
    @SuppressWarnings("unchecked")
    public NationalIdentifierHateoas getNationalIdentifierAssociatedWithFile(
            @NotNull final String systemID) {
        NationalIdentifierHateoas niHateoas = new NationalIdentifierHateoas(
                (List<INoarkEntity>) (List) getFileOrThrow(systemID).
                        getReferenceNationalIdentifier());
        nationalIdentifierHateoasHandler
	    .addLinks(niHateoas, new Authorisation());
        return niHateoas;
    }

    // All UPDATE operations
    public File update(File file) {
        return fileRepository.save(file);
    }

    // systemId
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
                fileRepository.findBySystemId(UUID.fromString(systemId)).
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
                fileRepository.findBySystemId(UUID.fromString(systemId)).
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

    // All DELETE operations
    @Override
    public void deleteEntity(@NotNull String fileSystemId) {
        deleteEntity(getFileOrThrow(fileSystemId));
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     *
     * @return the number of objects deleted
     */
    @Override
    public long deleteAllByOwnedBy() {
        return fileRepository.deleteByOwnedBy(getUser());
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
        FileHateoas fileHateoas = new FileHateoas(defaultFile);
        fileHateoasHandler.addLinksOnTemplate(fileHateoas, new Authorisation());
        return fileHateoas;
    }

    @Override
    public CommentHateoas generateDefaultComment() {
        return commentService.generateDefaultComment();
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
     * multiple methods, we have it here once. Note. If you call this, be aware
     * that you will only ever get a valid File back. If there is no valid
     * File, an exception is thrown
     *
     * @param systemId systemId of the file object you are looking for
     * @return the newly found file object or null if it does not exist
     */
    private File getFileOrThrow(@NotNull String systemId) {
        File file =
                fileRepository.findBySystemId(UUID.fromString(systemId));
        if (file == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " File, using systemId " +
                    systemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return file;
    }
}
