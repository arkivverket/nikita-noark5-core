package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.ClassHateoas;
import nikita.common.model.noark5.v5.hateoas.FileHateoas;
import nikita.common.model.noark5.v5.hateoas.RecordHateoas;
import nikita.common.model.noark5.v5.hateoas.SeriesHateoas;
import nikita.common.repository.n5v5.IFileRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IClassHateoasHandler;
import nikita.webapp.hateoas.interfaces.IFileHateoasHandler;
import nikita.webapp.hateoas.interfaces.ISeriesHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IFileService;
import nikita.webapp.service.interfaces.IRecordService;
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
import java.util.Optional;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.checkDocumentMediumValid;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.setFinaliseEntityValues;
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

    public FileService(EntityManager entityManager,
                       ApplicationEventPublisher applicationEventPublisher,
                       IRecordService recordService,
                       IFileRepository fileRepository,
                       IFileHateoasHandler fileHateoasHandler,
                       ISeriesHateoasHandler seriesHateoasHandler,
                       IClassHateoasHandler classHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.recordService = recordService;
        this.fileRepository = fileRepository;
        this.fileHateoasHandler = fileHateoasHandler;
        this.seriesHateoasHandler = seriesHateoasHandler;
        this.classHateoasHandler = classHateoasHandler;
    }

    public FileHateoas save(File file) {
        checkDocumentMediumValid(file);
        setFinaliseEntityValues(file);
        FileHateoas fileHateoas = new FileHateoas(fileRepository.save(file));
        fileHateoasHandler.addLinks(fileHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(this, file));
        return fileHateoas;
    }

    @Override
    public File createFile(File file) {
        checkDocumentMediumValid(file);
        setFinaliseEntityValues(file);
        return fileRepository.save(file);
    }

    @Override
    public ResponseEntity<RecordHateoas> createRecordAssociatedWithFile(
            String fileSystemId, Record record) {
        record.setReferenceFile(getFileOrThrow(fileSystemId));
        return recordService.save(record);
    }


    // All READ operations
    public List<File> findAll() {
        return fileRepository.findAll();
    }

    // id
    public Optional<File> findById(Long id) {
        return fileRepository.findById(id);
    }

    // ownedBy
    public List<File> findByOwnedBy(String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().
                getAuthentication().getName() : ownedBy;
        return fileRepository.findByOwnedBy(ownedBy);
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
        existingFile.setDocumentMedium(incomingFile.getDocumentMedium());
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingFile.setVersion(version);
        fileRepository.save(existingFile);
        return existingFile;
    }

    // All DELETE operations
    @Override
    public void deleteEntity(@NotNull String fileSystemId) {
        File file = getFileOrThrow(fileSystemId);
        fileRepository.delete(file);
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
        defaultFile.setTitle(TEST_TITLE);
        defaultFile.setDescription(TEST_DESCRIPTION);
        FileHateoas fondsHateoas = new FileHateoas(defaultFile);
        fileHateoasHandler.addLinksOnNew(fondsHateoas, new Authorisation());
        return fondsHateoas;
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
