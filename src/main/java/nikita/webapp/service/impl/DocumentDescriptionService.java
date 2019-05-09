package nikita.webapp.service.impl;

import nikita.common.model.noark5.v4.DocumentDescription;
import nikita.common.model.noark5.v4.DocumentObject;
import nikita.common.repository.n5v4.IDocumentDescriptionRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.service.interfaces.IDocumentDescriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;

@Service
@Transactional
public class DocumentDescriptionService
        implements IDocumentDescriptionService {

    private static final Logger logger =
            LoggerFactory.getLogger(FileService.class);

    private DocumentObjectService documentObjectService;
    private IDocumentDescriptionRepository documentDescriptionRepository;
    private EntityManager entityManager;

    public DocumentDescriptionService(
            DocumentObjectService documentObjectService,
            IDocumentDescriptionRepository documentDescriptionRepository,
            EntityManager entityManager) {
        this.documentObjectService = documentObjectService;
        this.documentDescriptionRepository = documentDescriptionRepository;
        this.entityManager = entityManager;
    }

    // All CREATE operations
    @Override
    public DocumentObject
    createDocumentObjectAssociatedWithDocumentDescription(
            String documentDescriptionSystemId, DocumentObject documentObject) {
        DocumentObject persistedDocumentObject;
        DocumentDescription documentDescription =
                documentDescriptionRepository.
                        findBySystemId(documentDescriptionSystemId);
        if (documentDescription == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " DocumentDescription, " +
                    "using documentDescriptionSystemId " +
                    documentDescriptionSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        } else {
            documentObject.setReferenceDocumentDescription(documentDescription);
            List<DocumentObject> documentObjects = documentDescription
                    .getReferenceDocumentObject();
            documentObjects.add(documentObject);
            persistedDocumentObject =
                    documentObjectService.save(documentObject);
        }
        return persistedDocumentObject;
    }

    /**
     * Note:
     * <p>
     * Assumes documentDescription.addReferenceRecord() has already been called.
     *
     * @param documentDescription
     * @return
     */

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

    // All READ operations
    public List<DocumentDescription> findAll() {
        return documentDescriptionRepository.findAll();
    }

    // id
    public Optional<DocumentDescription> findById(Long id) {
        return documentDescriptionRepository.findById(id);
    }

    // systemId
    public DocumentDescription findBySystemId(String systemId) {
        return getDocumentDescriptionOrThrow(systemId);
    }

    // ownedBy
    public List<DocumentDescription> findByOwnedBy(String ownedBy) {
        ownedBy = (ownedBy == null) ?
                SecurityContextHolder.getContext().getAuthentication().getName()
                : ownedBy;
        return documentDescriptionRepository.findByOwnedBy(ownedBy);
    }

    // -- All UPDATE operations
    public DocumentDescription update(DocumentDescription documentDescription) {
        return documentDescriptionRepository.save(documentDescription);
    }

    // -- All UPDATE operations
    @Override
    public DocumentDescription handleUpdate(
            @NotNull String systemId, @NotNull Long version,
            @NotNull DocumentDescription incomingDocumentDescription) {
        DocumentDescription existingDocumentDescription =
                getDocumentDescriptionOrThrow(systemId);
        // Copy all the values you are allowed to copy ....
        if (null != incomingDocumentDescription.getDescription()) {
            existingDocumentDescription.setDescription(
                    incomingDocumentDescription.getDescription());
        }
        if (null != incomingDocumentDescription.getTitle()) {
            existingDocumentDescription.setTitle(
                    incomingDocumentDescription.getTitle());
        }
        if (null != incomingDocumentDescription.getDocumentMedium()) {
            existingDocumentDescription.setDocumentMedium(
                    incomingDocumentDescription.getDocumentMedium());
        }
        if (null != incomingDocumentDescription.getAssociatedWithRecordAs()) {
            existingDocumentDescription.setAssociatedWithRecordAs(
                    incomingDocumentDescription.getAssociatedWithRecordAs());
        }
        if (null != incomingDocumentDescription.getDocumentNumber()) {
            existingDocumentDescription.setDocumentNumber(
                    incomingDocumentDescription.getDocumentNumber());
        }

        existingDocumentDescription.setVersion(version);
        documentDescriptionRepository.save(existingDocumentDescription);
        return existingDocumentDescription;
    }

    // All DELETE operations
    @Override
    public void deleteEntity(@NotNull String documentDescriptionSystemId) {
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
    }

    // All HELPER operations

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
