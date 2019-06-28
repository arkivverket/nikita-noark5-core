package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.*;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.repository.n5v5.IDocumentDescriptionRepository;
import nikita.common.repository.n5v5.IRecordRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.*;
import nikita.webapp.security.Authorisation;
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

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
@SuppressWarnings("unchecked")
public class RecordService
        extends NoarkService
        implements IRecordService {

    private static final Logger logger =
            LoggerFactory.getLogger(RecordService.class);

    private DocumentDescriptionService documentDescriptionService;
    private IRecordRepository recordRepository;
    private IRecordHateoasHandler recordHateoasHandler;
    private IFileHateoasHandler fileHateoasHandler;
    private ISeriesHateoasHandler seriesHateoasHandler;
    private IClassHateoasHandler classHateoasHandler;
    private IDocumentDescriptionHateoasHandler
            documentDescriptionHateoasHandler;
    private IDocumentDescriptionRepository documentDescriptionRepository;

    public RecordService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            DocumentDescriptionService documentDescriptionService,
            IRecordRepository recordRepository,
            IRecordHateoasHandler recordHateoasHandler,
            IFileHateoasHandler fileHateoasHandler,
            ISeriesHateoasHandler seriesHateoasHandler,
            IClassHateoasHandler classHateoasHandler,
            IDocumentDescriptionHateoasHandler
                    documentDescriptionHateoasHandler,
            IDocumentDescriptionRepository documentDescriptionRepository) {
        super(entityManager, applicationEventPublisher);
        this.documentDescriptionService = documentDescriptionService;
        this.recordRepository = recordRepository;
        this.entityManager = entityManager;
        this.recordHateoasHandler = recordHateoasHandler;
        this.fileHateoasHandler = fileHateoasHandler;
        this.seriesHateoasHandler = seriesHateoasHandler;
        this.classHateoasHandler = classHateoasHandler;
        this.documentDescriptionHateoasHandler =
                documentDescriptionHateoasHandler;
        this.documentDescriptionRepository = documentDescriptionRepository;
    }

    // All CREATE operations
    @Override
    public ResponseEntity<RecordHateoas> save(Record record) {
        RecordHateoas recordHateoas =
                new RecordHateoas(recordRepository.save(record));
        classHateoasHandler.addLinks(recordHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(recordHateoas.getEntityVersion().toString())
                .body(recordHateoas);
    }

    public DocumentDescriptionHateoas
    createDocumentDescriptionAssociatedWithRecord(
            String systemID, DocumentDescription documentDescription) {

        Record record = getRecordOrThrow(systemID);

        // Adding 1 as documentNumber starts at 1, not 0
        long documentNumber =
                documentDescriptionRepository.
                        countByReferenceRecord(record) + 1;
        documentDescription.setDocumentNumber((int) documentNumber);
        record.addReferenceDocumentDescription(documentDescription);
        documentDescription.addReferenceRecord(record);
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

    // All READ operations
    public List<Record> findAll() {
        return recordRepository.findAll();
    }

    // id
    public Optional<Record> findById(Long id) {
        return recordRepository.findById(id);
    }

    /**
     * Retrieve all record associated with the documentDescription identified by
     * the documentDescriptions systemId.
     *
     * @param systemId systemId of the associated documentDescription
     * @return The list of record packed as a ResponseEntity
     */
    @Override
    public ResponseEntity<RecordHateoas>
    findByReferenceDocumentDescription(@NotNull final String systemId) {
        RecordHateoas recordHateoas = new RecordHateoas(
                (List<INikitaEntity>) (List)
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
                recordRepository.findBySystemId(systemId).
                        getReferenceFile());

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
                recordRepository.findBySystemId(systemId).
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
                recordRepository.findBySystemId(systemId).
                        getReferenceSeries());

        seriesHateoasHandler.addLinks(seriesHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(seriesHateoas.getEntityVersion().toString())
                .body(seriesHateoas);
    }

    // systemId
    public Record findBySystemId(String systemId) {
        return getRecordOrThrow(systemId);
    }

    // ownedBy
    public List<Record> findByOwnedBy(String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().
                getAuthentication().getName() : ownedBy;
        return recordRepository.findByOwnedBy(ownedBy);
    }

    // All UPDATE operations
    public Record update(Record record) {
        return recordRepository.save(record);
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
    public Record handleUpdate(@NotNull final String recordSystemId,
                               @NotNull final Long version,
                               @NotNull final Record incomingRecord) {
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

    // All DELETE operations
    @Override
    public void deleteEntity(@NotNull String systemID) {
        Record record = getRecordOrThrow(systemID);

        // See issue for a description of why this code was written this way
        // https://gitlab.com/OsloMet-ABI/nikita-noark5-core/issues/82
        //Query q = entityManager.createNativeQuery("DELETE FROM
        // fonds_fonds_creator WHERE pk_fonds_creator_id  = :id ;");
        //q.setParameter("id", record.getId());
        //q.executeUpdate();
        entityManager.remove(record);
        entityManager.flush();
        entityManager.clear();
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     *
     * @return the number of objects deleted
     */
    @Override
    public long deleteAllByOwnedBy() {
        return recordRepository.deleteByOwnedBy(getUser());
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
        Record record = recordRepository.findBySystemId(systemID);
        if (record == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " Record, using systemId " + systemID;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return record;
    }
}
