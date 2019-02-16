package nikita.webapp.service.impl;

import nikita.common.model.noark5.v4.BasicRecord;
import nikita.common.model.noark5.v4.File;
import nikita.common.model.noark5.v4.Record;
import nikita.common.model.noark5.v4.hateoas.FileHateoas;
import nikita.common.repository.n5v4.IFileRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IFileHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IFileService;
import nikita.webapp.service.interfaces.IRecordService;
import nikita.webapp.util.NoarkUtils;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.*;

@Service
@Transactional
public class FileService
        implements IFileService {

    private static final Logger logger =
            LoggerFactory.getLogger(FileService.class);

    private IRecordService recordService;
    private IFileRepository fileRepository;
    private IFileHateoasHandler fileHateoasHandler;
    private ApplicationEventPublisher applicationEventPublisher;

    public FileService(IRecordService recordService,
                       IFileRepository fileRepository,
                       IFileHateoasHandler fileHateoasHandler,
                       ApplicationEventPublisher applicationEventPublisher) {
        this.recordService = recordService;
        this.fileRepository = fileRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.fileHateoasHandler = fileHateoasHandler;
    }

    public FileHateoas save(File file) {
        checkDocumentMediumValid(file);
        setNoarkEntityValues(file);
        setFinaliseEntityValues(file);
        FileHateoas fileHateoas = new FileHateoas(fileRepository.save(file));
        fileHateoasHandler.addLinks(fileHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this, file));
        return fileHateoas;
    }

    @Override
    public File createFile(File file) {
        NoarkUtils.NoarkEntity.Create.checkDocumentMediumValid(file);
        NoarkUtils.NoarkEntity.Create.setNoarkEntityValues(file);
        NoarkUtils.NoarkEntity.Create.setFinaliseEntityValues(file);
        return fileRepository.save(file);
    }

    @Override
    public Record createRecordAssociatedWithFile(String fileSystemId, Record record) {
        Record persistedRecord;
        File file = fileRepository.findBySystemId(fileSystemId);
        if (file == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " File, using fileSystemId " + fileSystemId;
            logger.info(info) ;
            throw new NoarkEntityNotFoundException(info);
        }
        else {
            record.setReferenceFile(file);
            persistedRecord = recordService.create(record);
        }
        return persistedRecord;        
    }

    @Override
    public BasicRecord createBasicRecordAssociatedWithFile(String fileSystemId, BasicRecord basicRecord) {
        BasicRecord persistedBasicRecord;
        File file = fileRepository.findBySystemId(fileSystemId);
        if (file == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " File, using fileSystemId " + fileSystemId;
            logger.info(info) ;
            throw new NoarkEntityNotFoundException(info);
        }
        else {
            basicRecord.setReferenceFile(file);
            persistedBasicRecord =
                    (BasicRecord) recordService.create(basicRecord);
        }
        return persistedBasicRecord;
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
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return fileRepository.findByOwnedBy(ownedBy);
    }

    // All UPDATE operations
    public File update(File file){
        return fileRepository.save(file);
    }

    // systemId
    @Override
    public File findBySystemId(String systemId) {
        return getFileOrThrow(systemId);
    }

    // All UPDATE operations
    @Override
    public File handleUpdate(@NotNull String systemId, @NotNull Long version, @NotNull File incomingFile) {
        File existingFile = getFileOrThrow(systemId);
        // Here copy all the values you are allowed to copy ....
        if (null != existingFile.getDescription()) {
            existingFile.setDescription(incomingFile.getDescription());
        }
        if (null != existingFile.getTitle()) {
            existingFile.setTitle(incomingFile.getTitle());
        }
        if (null != existingFile.getDocumentMedium()) {
            existingFile.setDocumentMedium(existingFile.getDocumentMedium());
        }
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

    // All HELPER operations
    /**
     * Internal helper method. Rather than having a find and try catch in multiple methods, we have it here once.
     * If you call this, be aware that you will only ever get a valid File back. If there is no valid
     * File, an exception is thrown
     *
     * @param fileSystemId systemId of the file object you are looking for
     * @return the newly found file object or null if it does not exist
     */
    private File getFileOrThrow(@NotNull String fileSystemId) {
        File file = fileRepository.findBySystemId(fileSystemId);
        if (file == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " File, using systemId " + fileSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return file;
    }
}
