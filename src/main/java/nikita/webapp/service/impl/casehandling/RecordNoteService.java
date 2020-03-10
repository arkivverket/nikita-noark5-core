package nikita.webapp.service.impl.casehandling;

import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.hateoas.casehandling.RecordNoteHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.DocumentFlowHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.secondary.DocumentFlow;
import nikita.common.repository.n5v5.casehandling.IRecordNoteRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IRecordNoteHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IDocumentFlowHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.casehandling.IRecordNoteService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.secondary.IDocumentFlowService;
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
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_FLOW;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.validateDocumentMedium;
import static org.springframework.http.HttpStatus.*;

@Service
@Transactional
public class RecordNoteService
        extends NoarkService
        implements IRecordNoteService {

    private static final Logger logger =
            LoggerFactory.getLogger(RecordNoteService.class);
    private final IRecordNoteRepository recordNoteRepository;
    private final IDocumentFlowService documentFlowService;
    private final IMetadataService metadataService;
    private final IRecordNoteHateoasHandler recordNoteHateoasHandler;
    private final IDocumentFlowHateoasHandler documentFlowHateoasHandler;

    public RecordNoteService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IRecordNoteRepository recordNoteRepository,
            IDocumentFlowService documentFlowService,
            IMetadataService metadataService,
            IRecordNoteHateoasHandler recordNoteHateoasHandler,
            IDocumentFlowHateoasHandler documentFlowHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.recordNoteRepository = recordNoteRepository;
        this.documentFlowService = documentFlowService;
        this.metadataService = metadataService;
        this.recordNoteHateoasHandler = recordNoteHateoasHandler;
        this.documentFlowHateoasHandler = documentFlowHateoasHandler;
    }

    @Override
    public ResponseEntity<RecordNoteHateoas> save(
            @NotNull final RecordNote recordNote) {
        validateDocumentMedium(metadataService, recordNote);
        RecordNoteHateoas recordNoteHateoas = new
                RecordNoteHateoas(recordNoteRepository.save(recordNote));
        recordNoteHateoasHandler.addLinks(recordNoteHateoas,
                new Authorisation());
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(recordNoteHateoas.getEntityVersion().toString())
                .body(recordNoteHateoas);
    }

    // systemId
    @Override
    public ResponseEntity<RecordNoteHateoas> findBySystemId(String systemId) {
        RecordNoteHateoas recordNoteHateoas = new
                RecordNoteHateoas(getRecordNoteOrThrow(systemId));
        recordNoteHateoasHandler.addLinks(recordNoteHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(recordNoteHateoas.getEntityVersion().toString())
                .body(recordNoteHateoas);
    }


    @Override
    @SuppressWarnings("unchecked")
    public ResponseEntity<RecordNoteHateoas> findAllRecordNoteByCaseFile(
            CaseFile caseFile) {
        RecordNoteHateoas recordNoteHateoas = new RecordNoteHateoas(
                (List<INoarkEntity>)
                        (List) recordNoteRepository.
                                findByReferenceFile(caseFile));
        recordNoteHateoasHandler.addLinks(recordNoteHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .body(recordNoteHateoas);
    }


    @Override
    @SuppressWarnings("unchecked")
    public ResponseEntity<RecordNoteHateoas> findAllByOwner() {
        RecordNoteHateoas recordNoteHateoas = new
                RecordNoteHateoas((List<INoarkEntity>) (List)
                recordNoteRepository.findByOwnedBy(getUser()));
        recordNoteHateoasHandler.addLinks(recordNoteHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(recordNoteHateoas.getEntityVersion().toString())
                .body(recordNoteHateoas);
    }

    @Override
    public DocumentFlowHateoas findAllDocumentFlowWithRecordNoteBySystemId
        (String systemID) {
        RecordNote recordNote = getRecordNoteOrThrow(systemID);
        DocumentFlowHateoas documentFlowHateoas =
                new DocumentFlowHateoas((List<INoarkEntity>)
                        (List) recordNote.getReferenceDocumentFlow());
        documentFlowHateoasHandler.addLinks(documentFlowHateoas,
                                            new Authorisation());
        setOutgoingRequestHeader(documentFlowHateoas);
        return documentFlowHateoas;
    }

    @Override
    public DocumentFlowHateoas associateDocumentFlowWithRecordNote(
            String systemId, DocumentFlow documentFlow) {
        return documentFlowService.associateDocumentFlowWithRecordNote
            (documentFlow, getRecordNoteOrThrow(systemId));
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
     * wrapped first as RecordNoteHateoas object and then as a ResponseEntity
     */
    @Override
    public ResponseEntity<RecordNoteHateoas> handleUpdate(
            @NotNull final String systemId,
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
        recordNoteRepository.save(existingRecordNote);
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this, existingRecordNote));
        RecordNoteHateoas recordNoteHateoas = new
                RecordNoteHateoas(recordNoteRepository.save(
                existingRecordNote));
        recordNoteHateoasHandler.addLinks(recordNoteHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(recordNoteHateoas.getEntityVersion().toString())
                .body(recordNoteHateoas);
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
    public ResponseEntity<String> deleteEntity(
            @NotNull final String recordNoteSystemId) {
        RecordNote recordNote = getRecordNoteOrThrow(recordNoteSystemId);
        if (deletePossible(recordNote)) {
            deleteEntity(recordNote);
            applicationEventPublisher.publishEvent(
                    new AfterNoarkEntityDeletedEvent(this, recordNote));
        }
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }

    /**
     * Delete all RecordNote objects belonging to the user identified by ownedBy
     *
     * @return the number of objects deleted
     */
    @Override
    public ResponseEntity<String> deleteAllByOwnedBy() {
        String user = getUser();
        long count = recordNoteRepository.countByOwnedBy(user);
        recordNoteRepository.deleteByOwnedBy(user);
        logger.info("Deleted [" + count + "] RecordNote objects belonging to " +
                "[" + user + "]");
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }

    @Override
    public ResponseEntity<RecordNoteHateoas> generateDefaultRecordNote(
            @NotNull String caseFileSystemId) {
        RecordNote defaultRecordNote = new RecordNote();
        defaultRecordNote.setTitle(DEFAULT_TITLE + "RecordNote");
        defaultRecordNote.setDescription(DEFAULT_DESCRIPTION + "a CaseFile " +
                "with systemId [" + caseFileSystemId + "]");
        RecordNoteHateoas recordNoteHateoas = new
                RecordNoteHateoas(defaultRecordNote);
        recordNoteHateoasHandler.addLinksOnTemplate(recordNoteHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .body(recordNoteHateoas);
    }

    @Override
    public DocumentFlowHateoas generateDefaultDocumentFlow(String systemID) {
        return documentFlowService.generateDefaultDocumentFlow();
    }

    // All helper methods

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid RecordNote back. If there is no
     * valid RecordNote, an exception is thrown
     *
     * @param systemId systemId of the recordNote to find.
     * @return the recordNote
     */
    protected RecordNote getRecordNoteOrThrow(
            @NotNull String systemId) {
        RecordNote recordNote =
                recordNoteRepository.findBySystemId(UUID.fromString(systemId));
        if (recordNote == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " RecordNote, using systemId " + systemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return recordNote;
    }
}
