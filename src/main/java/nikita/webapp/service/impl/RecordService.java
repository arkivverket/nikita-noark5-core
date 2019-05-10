package nikita.webapp.service.impl;

import nikita.common.model.noark5.v4.DocumentDescription;
import nikita.common.model.noark5.v4.Record;
import nikita.common.model.noark5.v4.hateoas.DocumentDescriptionHateoas;
import nikita.common.model.noark5.v4.hateoas.RecordHateoas;
import nikita.common.repository.n5v4.IDocumentDescriptionRepository;
import nikita.common.repository.n5v4.IRecordRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IDocumentDescriptionHateoasHandler;
import nikita.webapp.hateoas.interfaces.IRecordHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IRecordService;
import nikita.webapp.web.events.AfterNoarkEntityCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;

@Service
@Transactional
public class RecordService
        extends NoarkService
        implements IRecordService {

    private static final Logger logger =
            LoggerFactory.getLogger(RecordService.class);

    private DocumentDescriptionService documentDescriptionService;
    private IRecordRepository recordRepository;
    private IRecordHateoasHandler recordHateoasHandler;
    private IDocumentDescriptionHateoasHandler
            documentDescriptionHateoasHandler;
    private IDocumentDescriptionRepository documentDescriptionRepository;

    public RecordService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            DocumentDescriptionService documentDescriptionService,
            IRecordRepository recordRepository,
            IRecordHateoasHandler recordHateoasHandler,
            IDocumentDescriptionHateoasHandler
                    documentDescriptionHateoasHandler,
            IDocumentDescriptionRepository documentDescriptionRepository) {
        super(entityManager, applicationEventPublisher);
        this.documentDescriptionService = documentDescriptionService;
        this.recordRepository = recordRepository;
        this.entityManager = entityManager;
        this.recordHateoasHandler = recordHateoasHandler;
        this.documentDescriptionHateoasHandler =
                documentDescriptionHateoasHandler;
        this.documentDescriptionRepository = documentDescriptionRepository;
    }

    // All CREATE operations
    @Override
    public RecordHateoas save(Record record) {
        String username = SecurityContextHolder.getContext().
                getAuthentication().getName();

        record.setSystemId(UUID.randomUUID().toString());
        record.setCreatedDate(ZonedDateTime.now());
        record.setOwnedBy(username);
        record.setCreatedBy(username);
        record.setDeleted(false);

        RecordHateoas recordHateoas =
                new RecordHateoas(recordRepository.save(record));
        recordHateoasHandler.addLinks(recordHateoas, new Authorisation());
        return recordHateoas;
    }


    public Record create(Record record) {
        String username = SecurityContextHolder.getContext().
                getAuthentication().getName();

        record.setSystemId(UUID.randomUUID().toString());
        record.setCreatedDate(ZonedDateTime.now());
        record.setOwnedBy(username);
        record.setCreatedBy(username);
        record.setDeleted(false);

        return recordRepository.save(record);
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
    public Record update(Record record){
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
        // TODO: FIND ALL VALUES
        // This might be a class that can only have values set via parameter
        // values rather than request bodies

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
