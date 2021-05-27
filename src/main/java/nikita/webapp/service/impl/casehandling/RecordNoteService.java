package nikita.webapp.service.impl.casehandling;

import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.hateoas.casehandling.RecordNoteHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.DocumentFlowHateoas;
import nikita.common.model.noark5.v5.secondary.DocumentFlow;
import nikita.common.repository.n5v5.casehandling.IRecordNoteRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IRecordNoteHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.casehandling.IRecordNoteService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.odata.IODataService;
import nikita.webapp.service.interfaces.secondary.IDocumentFlowService;
import nikita.webapp.web.events.AfterNoarkEntityDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.validateDocumentMedium;

@Service
public class RecordNoteService
        extends NoarkService
        implements IRecordNoteService {

    private static final Logger logger =
            LoggerFactory.getLogger(RecordNoteService.class);

    private final IRecordNoteRepository recordNoteRepository;
    private final IDocumentFlowService documentFlowService;
    private final IMetadataService metadataService;
    private final IRecordNoteHateoasHandler recordNoteHateoasHandler;

    public RecordNoteService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IODataService odataService,
            IPatchService patchService,
            IRecordNoteRepository recordNoteRepository,
            IDocumentFlowService documentFlowService,
            IMetadataService metadataService,
            IRecordNoteHateoasHandler recordNoteHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.recordNoteRepository = recordNoteRepository;
        this.documentFlowService = documentFlowService;
        this.metadataService = metadataService;
        this.recordNoteHateoasHandler = recordNoteHateoasHandler;
    }

    // All CREATE methods

    @Override
    @Transactional
    public RecordNoteHateoas save(@NotNull final RecordNote recordNote) {
        validateDocumentMedium(metadataService, recordNote);
        return packAsHateoas(recordNoteRepository.save(recordNote));
    }

    // All READ methods

    @Override
    public RecordNoteHateoas findBySystemId(@NotNull final UUID systemId) {
        return packAsHateoas(getRecordNoteOrThrow(systemId));
    }

    @Override
    public RecordNoteHateoas findAll() {
        return (RecordNoteHateoas) odataService.processODataQueryGet();
    }

    @Override
    public DocumentFlowHateoas findAllDocumentFlowWithRecordNoteBySystemId(
            @NotNull final UUID systemId) {
        getRecordNoteOrThrow(systemId);
        return (DocumentFlowHateoas) odataService.processODataQueryGet();
    }

    // All UPDATE operations

    @Override
    @Transactional
    public DocumentFlowHateoas associateDocumentFlowWithRecordNote(
            UUID systemId, DocumentFlow documentFlow) {
        return documentFlowService.associateDocumentFlowWithRecordNote(
                documentFlow, getRecordNoteOrThrow(systemId));
    }

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
     * <p>
     * Note. The last known version number (derived from an ETAG in the request)
     * is retrieved and checked to make sure the user is updating the latest
     * copy.
     *
     * @param systemId           The systemId of the recordNote object
     *                           you wish to update
     * @param incomingRecordNote The updated recordNote object. Note
     *                           the values you are allowed to change are
     *                           copied from this object. This object is
     *                           not persisted.
     * @return the updated recordNote after being persisted to the database
     * wrapped as RecordNoteHateoas object
     */
    @Override
    @Transactional
    public RecordNoteHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final RecordNote incomingRecordNote) {

        Long version = getETag();
        RecordNote existingRecordNote = getRecordNoteOrThrow(systemId);
        // Copy all the values you are allowed to copy ....
        updateTitleAndDescription(incomingRecordNote, existingRecordNote);
        if (null != incomingRecordNote.getDocumentMedium()) {
            existingRecordNote.setDocumentMedium(
                    incomingRecordNote.getDocumentMedium());
        }
        if (null != incomingRecordNote.getDocumentDate()) {
            existingRecordNote.setDocumentDate(
                    incomingRecordNote.getDocumentDate());
        }
        if (null != incomingRecordNote.getDueDate()) {
            existingRecordNote.setDueDate(
                    incomingRecordNote.getDueDate());
        }
        if (null != incomingRecordNote.getFreedomAssessmentDate()) {
            existingRecordNote.setFreedomAssessmentDate(
                    incomingRecordNote.getFreedomAssessmentDate());
        }
        if (null != incomingRecordNote.getLoanedDate()) {
            existingRecordNote.setLoanedDate(
                    incomingRecordNote.getLoanedDate());
        }
        if (null != incomingRecordNote.getLoanedTo()) {
            existingRecordNote.setLoanedTo(
                    incomingRecordNote.getLoanedTo());
        }
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingRecordNote.setVersion(version);
        return packAsHateoas(existingRecordNote);
    }

    // All DELETE operations

    /**
     * Delete a RecordNote identified by the given systemId
     * <p>
     * Note. This assumes all children have also been deleted.
     *
     * @param recordNoteSystemId The systemId of the recordNote object
     *                           you wish to delete
     */
    @Override
    @Transactional
    public String deleteEntity(
            @NotNull final UUID recordNoteSystemId) {
        RecordNote recordNote = getRecordNoteOrThrow(recordNoteSystemId);
        for (DocumentFlow documentFlow : recordNote
                .getReferenceDocumentFlow()) {
            documentFlowService.deleteDocumentFlow(documentFlow);
        }
        recordNoteRepository.delete(recordNote);
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityDeletedEvent(this, recordNote));
        // If the delete failed an exception should have been thrown. Getting
        // this far means it went OK
        return DELETE_RESPONSE;
    }

    /**
     * Delete all RecordNote objects belonging to the user identified by ownedBy
     */
    @Override
    @Transactional
    public String deleteAllByOwnedBy() {
        String user = getUser();
        long count = recordNoteRepository.countByOwnedBy(user);
        recordNoteRepository.deleteByOwnedBy(user);
        logger.info("Deleted [" + count + "] RecordNote objects belonging to " +
                "[" + user + "]");
        return DELETE_RESPONSE;
    }

    // All template methods

    @Override
    public RecordNoteHateoas generateDefaultRecordNote(
            @NotNull final UUID caseFileSystemId) {
        RecordNote defaultRecordNote = new RecordNote();
        defaultRecordNote.setTitle(DEFAULT_TITLE + "RecordNote");
        defaultRecordNote.setDescription(DEFAULT_DESCRIPTION + "a CaseFile " +
                "with systemId [" + caseFileSystemId + "]");
        defaultRecordNote.setVersion(-1L, true);
        return packAsHateoas(defaultRecordNote);
    }

    @Override
    public DocumentFlowHateoas generateDefaultDocumentFlow(@NotNull final UUID systemId) {
        return documentFlowService.generateDefaultDocumentFlow();
    }

    // All helper methods

    public RecordNoteHateoas packAsHateoas(@NotNull final RecordNote recordNote) {
        RecordNoteHateoas recordNoteHateoas = new RecordNoteHateoas(recordNote);
        applyLinksAndHeader(recordNoteHateoas, recordNoteHateoasHandler);
        return recordNoteHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid RecordNote back. If there is no
     * valid RecordNote, an exception is thrown
     *
     * @param systemId systemId of the recordNote to find.
     * @return the recordNote
     */
    protected RecordNote getRecordNoteOrThrow(@NotNull final UUID systemId) {
        RecordNote recordNote =
                recordNoteRepository.findBySystemId(systemId);
        if (recordNote == null) {
            String error = INFO_CANNOT_FIND_OBJECT +
                    " RecordNote, using systemId " + systemId;
            logger.error(error);
            throw new NoarkEntityNotFoundException(error);
        }
        return recordNote;
    }
}
