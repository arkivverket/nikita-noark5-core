package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.hateoas.DocumentDescriptionHateoas;
import nikita.common.model.noark5.v5.hateoas.DocumentObjectHateoas;
import nikita.common.model.noark5.v5.hateoas.RecordHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.repository.n5v5.IDocumentDescriptionRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IDocumentDescriptionHateoasHandler;
import nikita.webapp.hateoas.interfaces.IDocumentObjectHateoasHandler;
import nikita.webapp.hateoas.interfaces.IRecordHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IDocumentDescriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
public class DocumentDescriptionService
        extends NoarkService
        implements IDocumentDescriptionService {

    private static final Logger logger =
            LoggerFactory.getLogger(FileService.class);

    private DocumentObjectService documentObjectService;
    private IDocumentDescriptionRepository documentDescriptionRepository;
    private IDocumentDescriptionHateoasHandler documentDescriptionHateoasHandler;
    private IDocumentObjectHateoasHandler documentObjectHateoasHandler;
    private IRecordHateoasHandler recordHateoasHandler;

    public DocumentDescriptionService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            DocumentObjectService documentObjectService,
            IDocumentDescriptionRepository documentDescriptionRepository,
            IDocumentDescriptionHateoasHandler
                    documentDescriptionHateoasHandler,
            IDocumentObjectHateoasHandler documentObjectHateoasHandler,
            IRecordHateoasHandler recordHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.documentObjectService = documentObjectService;
        this.documentDescriptionRepository = documentDescriptionRepository;
        this.documentDescriptionHateoasHandler =
                documentDescriptionHateoasHandler;
        this.documentObjectHateoasHandler = documentObjectHateoasHandler;
        this.recordHateoasHandler = recordHateoasHandler;
    }

    // All CREATE operations
    @Override
    public DocumentObject
    createDocumentObjectAssociatedWithDocumentDescription(
            String documentDescriptionSystemId, DocumentObject documentObject) {
        DocumentObject persistedDocumentObject;
        DocumentDescription documentDescription =
                getDocumentDescriptionOrThrow(documentDescriptionSystemId);
        documentObject.setReferenceDocumentDescription(documentDescription);
        List<DocumentObject> documentObjects = documentDescription
                .getReferenceDocumentObject();
        documentObjects.add(documentObject);
        return documentObjectService.save(documentObject);
    }

    /**
     * Note:
     * <p>
     * Assumes documentDescription.addReferenceRecord() has already been called.
     *
     * @param documentDescription
     * @return
     */
    @Override
    public DocumentDescription save(DocumentDescription documentDescription) {
        String username = SecurityContextHolder.getContext().
                getAuthentication().getName();
        documentDescription.setSystemId(UUID.randomUUID().toString());
        documentDescription.setCreatedDate(ZonedDateTime.now());
        documentDescription.setOwnedBy(username);
        documentDescription.setCreatedBy(username);
        documentDescription.setAssociationDate(ZonedDateTime.now());
        documentDescription.setAssociatedBy(username);

        return documentDescriptionRepository.save(documentDescription);
    }

    @Override
    public ResponseEntity<DocumentDescriptionHateoas>
    findBySystemId(String systemId) {
        DocumentDescriptionHateoas documentDescriptionHateoas = new
                DocumentDescriptionHateoas(
                getDocumentDescriptionOrThrow(systemId));
        documentDescriptionHateoasHandler.addLinks(documentDescriptionHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(documentDescriptionHateoas.getEntityVersion().toString())
                .body(documentDescriptionHateoas);
    }

    @Override
    public DocumentDescription findDocumentDescriptionBySystemId(
            String systemId) {
        return getDocumentDescriptionOrThrow(systemId);
    }

    @Override
    public ResponseEntity<DocumentDescriptionHateoas> findAll() {
        DocumentDescriptionHateoas documentDescriptionHateoas = new
                DocumentDescriptionHateoas((List<INikitaEntity>)
                (List) documentDescriptionRepository.findByOwnedBy(getUser()));
        documentDescriptionHateoasHandler.addLinks(documentDescriptionHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(documentDescriptionHateoas.getEntityVersion().toString())
                .body(documentDescriptionHateoas);
    }

    @Override
    public ResponseEntity<DocumentObjectHateoas>
    findAllDocumentObjectWithDocumentDescriptionBySystemId(
            @NotNull String systemId) {
        DocumentObjectHateoas documentObjectHateoas = new
                DocumentObjectHateoas((List<INikitaEntity>)
                (List) getDocumentDescriptionOrThrow(systemId)
                        .getReferenceDocumentObject());
        documentObjectHateoasHandler.addLinks(documentObjectHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .body(documentObjectHateoas);
    }

    @Override
    public ResponseEntity<RecordHateoas>
    findAllRecordWithDocumentDescriptionBySystemId(@NotNull String systemId) {
        RecordHateoas recordHateoas = new
                RecordHateoas((List<INikitaEntity>)
                (List) getDocumentDescriptionOrThrow(systemId)
                        .getReferenceRecord());
        recordHateoasHandler.addLinks(recordHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .body(recordHateoas);
    }

    // -- All UPDATE operations

    /**
     * Updates a DocumentDescription object in the database. First we try to locate the
     * DocumentDescription object. If the DocumentDescription object does not exist a
     * NoarkEntityNotFoundException exception is thrown that the caller has
     * to deal with.
     * <br>
     * After this the values you are allowed to update are copied from the
     * incomingDocumentDescription object to the existingDocumentDescription object and the
     * existingDocumentDescription object will be persisted to the database when the
     * transaction boundary is over.
     * <p>
     * Note, the version corresponds to the version number, when the object
     * was initially retrieved from the database. If this number is not the
     * same as the version number when re-retrieving the DocumentDescription object from
     * the database a NoarkConcurrencyException is thrown. Note. This happens
     * when the call to DocumentDescription.setVersion() occurs.
     *
     * @param systemId                    systemId of the incoming documentDescription object
     * @param version
     * @param incomingDocumentDescription the incoming documentDescription
     * @return the updated documentDescription after it is persisted
     */
    @Override
    public DocumentDescription handleUpdate(
            @NotNull String systemId, @NotNull Long version,
            @NotNull DocumentDescription incomingDocumentDescription) {
        DocumentDescription existingDocumentDescription =
                getDocumentDescriptionOrThrow(systemId);
        updateTitleAndDescription(incomingDocumentDescription,
                existingDocumentDescription);
        updateDocumentDescription(incomingDocumentDescription,
                existingDocumentDescription);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingDocumentDescription.setVersion(version);
        return documentDescriptionRepository.save(existingDocumentDescription);
    }

    // All DELETE operations
    @Override
    public int deleteEntity(@NotNull String documentDescriptionSystemId) {
        // See issue for a description of why this code was written this way
        // https://gitlab.com/OsloMet-ABI/nikita-noark5-core/issues/82
        DocumentDescription documentDescription =
                getDocumentDescriptionOrThrow(documentDescriptionSystemId);
        String query = "DELETE FROM record_document_description WHERE " +
                "F_PK_DOCUMENT_DESCRIPTION_ID  = :id ;";
        Query q = entityManager.createNativeQuery(query);
        q.setParameter("id", documentDescription.getId());
        q.executeUpdate();

        entityManager.remove(documentDescription);
        entityManager.flush();
        entityManager.clear();
        return 1;
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     *
     * @return the number of objects deleted
     */
    @Override
    public long deleteAllByOwnedBy() {
        return documentDescriptionRepository.deleteByOwnedBy(getUser());
    }

    // All HELPER operations

    private void updateDocumentDescription(
            @NotNull final DocumentDescription incomingDocumentDescription,
            @NotNull final DocumentDescription existingDocumentDescription) {
        if (null != incomingDocumentDescription.getDocumentMedium()) {
            existingDocumentDescription.setDocumentMedium(
                    incomingDocumentDescription.getDocumentMedium());
        }
        if (null != incomingDocumentDescription.getAssociatedWithRecordAs()) {
            existingDocumentDescription.setAssociatedWithRecordAs(
                    incomingDocumentDescription.getAssociatedWithRecordAs());
        }
        existingDocumentDescription.setDocumentNumber(
                incomingDocumentDescription.getDocumentNumber());
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid DocumentDescription back. If there
     * is no valid DocumentDescription, an exception is thrown
     *
     * @param documentDescriptionSystemId
     * @return The documentDescription
     */
    protected DocumentDescription getDocumentDescriptionOrThrow(
            @NotNull String documentDescriptionSystemId) {
        DocumentDescription documentDescription =
                documentDescriptionRepository.findBySystemId(
                        documentDescriptionSystemId);
        if (documentDescription == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " DocumentDescription, using systemId " +
                    documentDescriptionSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return documentDescription;
    }
}
