package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.PartPerson;
import nikita.common.model.noark5.v5.PartUnit;
import nikita.common.model.noark5.v5.hateoas.*;
import nikita.common.model.noark5.v5.hateoas.secondary.AuthorHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.DocumentType;
import nikita.common.model.noark5.v5.secondary.Author;
import nikita.common.repository.n5v5.IDocumentDescriptionRepository;
import nikita.common.repository.n5v5.secondary.IAuthorRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IDocumentDescriptionHateoasHandler;
import nikita.webapp.hateoas.interfaces.IDocumentObjectHateoasHandler;
import nikita.webapp.hateoas.interfaces.IPartHateoasHandler;
import nikita.webapp.hateoas.interfaces.IRecordHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IAuthorHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IDocumentDescriptionService;
import nikita.webapp.service.interfaces.metadata.IDocumentTypeService;
import nikita.webapp.service.interfaces.secondary.IPartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.DatabaseConstants.DELETE_FROM_RECORD_DOCUMENT_DESCRIPTION;
import static nikita.common.config.N5ResourceMappings.*;
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
    private IDocumentTypeService documentTypeService;
    private IPartService partService;
    private IPartHateoasHandler partHateoasHandler;
    private IAuthorRepository authorRepository;
    private IAuthorHateoasHandler authorHateoasHandler;

    public DocumentDescriptionService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            DocumentObjectService documentObjectService,
            IDocumentDescriptionRepository documentDescriptionRepository,
            IDocumentDescriptionHateoasHandler
                    documentDescriptionHateoasHandler,
            IDocumentObjectHateoasHandler documentObjectHateoasHandler,
            IRecordHateoasHandler recordHateoasHandler,
            IDocumentTypeService documentTypeService,
            IPartService partService,
            IPartHateoasHandler partHateoasHandler,
            IAuthorRepository authorRepository,
            IAuthorHateoasHandler authorHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.documentObjectService = documentObjectService;
        this.documentDescriptionRepository = documentDescriptionRepository;
        this.documentDescriptionHateoasHandler =
                documentDescriptionHateoasHandler;
        this.documentObjectHateoasHandler = documentObjectHateoasHandler;
        this.recordHateoasHandler = recordHateoasHandler;
        this.documentTypeService = documentTypeService;
        this.partService = partService;
        this.partHateoasHandler = partHateoasHandler;
        this.authorRepository = authorRepository;
        this.authorHateoasHandler = authorHateoasHandler;
    }

    // All CREATE operations
    @Override
    public DocumentObject
    createDocumentObjectAssociatedWithDocumentDescription(
            String documentDescriptionSystemId, DocumentObject documentObject) {
        DocumentDescription documentDescription =
                getDocumentDescriptionOrThrow(documentDescriptionSystemId);
        documentObject.setReferenceDocumentDescription(documentDescription);
        List<DocumentObject> documentObjects = documentDescription
                .getReferenceDocumentObject();
        documentObjects.add(documentObject);
        return documentObjectService.save(documentObject);
    }


    @Override
    public PartPersonHateoas
    createPartPersonAssociatedWithDocumentDescription(
            String systemID, PartPerson partPerson) {
        return partService.
                createNewPartPerson(partPerson,
                        getDocumentDescriptionOrThrow(systemID));
    }

    @Override
    public PartUnitHateoas
    createPartUnitAssociatedWithDocumentDescription(
            String systemID, PartUnit partUnit) {
        return partService.
                createNewPartUnit(partUnit,
                        getDocumentDescriptionOrThrow(systemID));
    }


    /**
     * Persist and associate the incoming author object with the
     * documentDescription identified by systemId
     *
     * @param systemId The systemId of the documentDescription to associate
     *                 with
     * @param author   The incoming author object
     * @return author object wrapped as a AuthorHateaos
     */
    @Override
    public AuthorHateoas associateAuthorWithDocumentDescription(
            String systemId, Author author) {
        DocumentDescription documentDescription =
                getDocumentDescriptionOrThrow(systemId);
        author.setReferenceDocumentDescription(documentDescription);
        authorRepository.save(author);
        AuthorHateoas authorHateoas = new AuthorHateoas(author);
        authorHateoasHandler.addLinks(authorHateoas, new Authorisation());
        setOutgoingRequestHeader(authorHateoas);
        return authorHateoas;
    }

    /**
     * Generate a Default DocumentDescription object.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @return the DocumentDescription object wrapped as a
     * DocumentDescriptionHateoas object
     */
    @Override
    public DocumentDescriptionHateoas
    generateDefaultDocumentDescription() {
        DocumentDescription defaultDocumentDescription =
            new DocumentDescription();

        defaultDocumentDescription.setAssociatedWithRecordAsCode(MAIN_DOCUMENT_CODE);
        defaultDocumentDescription.setAssociatedWithRecordAsCodeName(MAIN_DOCUMENT);
        defaultDocumentDescription.setDocumentTypeCode(LETTER_CODE);
        defaultDocumentDescription.setDocumentTypeCodeName(LETTER);
        defaultDocumentDescription.setDocumentStatusCode(DOCUMENT_STATUS_FINALISED_CODE);
        defaultDocumentDescription.setDocumentStatusCodeName(DOCUMENT_STATUS_FINALISED);

        DocumentDescriptionHateoas documentDescriptionHateoas = new
                DocumentDescriptionHateoas(defaultDocumentDescription);
        documentDescriptionHateoasHandler.addLinksOnTemplate(
                documentDescriptionHateoas, new Authorisation());
        return documentDescriptionHateoas;
    }

    @Override
    public PartPersonHateoas generateDefaultPartPerson(String systemID) {
        return partService.generateDefaultPartPerson(systemID);
    }

    @Override
    public PartUnitHateoas generateDefaultPartUnit(String systemID) {
        return partService.generateDefaultPartUnit(systemID);
    }

    @Override
    public AuthorHateoas generateDefaultAuthor(String systemID) {
        Author suggestedPart = new Author();
        suggestedPart.setAuthor("Ole Olsen");
        AuthorHateoas partHateoas = new AuthorHateoas(suggestedPart);
        authorHateoasHandler.addLinksOnTemplate(partHateoas, new Authorisation());
        return partHateoas;
    }

    /*
     * Note:
     * <p>
     * Assumes documentDescription.addReferenceRecord() has already been called.
     *
     */
    @Override
    public DocumentDescription save(DocumentDescription documentDescription) {
        validateDocumentType(documentDescription);
        String username = SecurityContextHolder.getContext().
                getAuthentication().getName();
        documentDescription.setAssociationDate(OffsetDateTime.now());
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
    public AuthorHateoas findAllAuthorWithDocumentDescriptionBySystemId(
            String systemId) {
        DocumentDescription documentDescription =
                getDocumentDescriptionOrThrow(systemId);
        AuthorHateoas authorHateoas =
                new AuthorHateoas((List<INoarkEntity>)
                        (List) documentDescription.getReferenceAuthor(),
                        AUTHOR);
        authorHateoasHandler.addLinks(authorHateoas, new Authorisation());
        setOutgoingRequestHeader(authorHateoas);
        return authorHateoas;
    }

    @Override
    public DocumentDescription findDocumentDescriptionBySystemId(
            String systemId) {
        return getDocumentDescriptionOrThrow(systemId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResponseEntity<DocumentDescriptionHateoas> findAll() {
        DocumentDescriptionHateoas documentDescriptionHateoas = new
                DocumentDescriptionHateoas((List<INoarkEntity>)
                (List) documentDescriptionRepository.findByOwnedBy(getUser()));
        documentDescriptionHateoasHandler.addLinks(documentDescriptionHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .eTag(documentDescriptionHateoas.getEntityVersion().toString())
                .body(documentDescriptionHateoas);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResponseEntity<DocumentObjectHateoas>
    findAllDocumentObjectWithDocumentDescriptionBySystemId(
            @NotNull String systemId) {
        DocumentObjectHateoas documentObjectHateoas = new
                DocumentObjectHateoas((List<INoarkEntity>)
                (List) getDocumentDescriptionOrThrow(systemId)
                        .getReferenceDocumentObject());
        documentObjectHateoasHandler.addLinks(documentObjectHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .body(documentObjectHateoas);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResponseEntity<RecordHateoas>
    findAllRecordWithDocumentDescriptionBySystemId(@NotNull String systemId) {
        RecordHateoas recordHateoas = new
                RecordHateoas((List<INoarkEntity>)
                (List) getDocumentDescriptionOrThrow(systemId)
                        .getReferenceRecord());
        recordHateoasHandler.addLinks(recordHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(getServletPath()))
                .body(recordHateoas);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PartHateoas getPartAssociatedWithDocumentDescription(
            @NotNull final String systemID) {
        PartHateoas partHateoas = new PartHateoas(
                (List<INoarkEntity>) (List)
                        getDocumentDescriptionOrThrow(systemID).
                                getReferencePart());
        partHateoasHandler.addLinks(partHateoas, new Authorisation());
        return partHateoas;
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
     * @param version version of object, field is given by nikita
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
    public void deleteEntity(@NotNull String documentDescriptionSystemId) {
        DocumentDescription documentDescription =
                getDocumentDescriptionOrThrow(documentDescriptionSystemId);
        // Disassociate any links between DocumentDescription and Record
        disassociateForeignKeys(documentDescription,
                DELETE_FROM_RECORD_DOCUMENT_DESCRIPTION);
        deleteEntity(documentDescription);
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
        if (null != incomingDocumentDescription.getDocumentMediumCode()) {
            existingDocumentDescription.setDocumentMediumCode(
                    incomingDocumentDescription.getDocumentMediumCode());
        }
        if (null != incomingDocumentDescription.getDocumentMediumCodeName()) {
            existingDocumentDescription.setDocumentMediumCodeName(
                    incomingDocumentDescription.getDocumentMediumCodeName());
        }
        if (null != incomingDocumentDescription.getAssociatedWithRecordAsCode()) {
            existingDocumentDescription.setAssociatedWithRecordAsCode(
                    incomingDocumentDescription.getAssociatedWithRecordAsCode());
        }
        if (null != incomingDocumentDescription.getAssociatedWithRecordAsCodeName()) {
            existingDocumentDescription.setAssociatedWithRecordAsCodeName(
                    incomingDocumentDescription.getAssociatedWithRecordAsCodeName());
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
     * @param documentDescriptionSystemId systemId of the documentDescription
     * @return The documentDescription to be returned
     */
    protected DocumentDescription getDocumentDescriptionOrThrow(
            @NotNull String documentDescriptionSystemId) {
        DocumentDescription documentDescription =
                documentDescriptionRepository.findBySystemId(
                        UUID.fromString(documentDescriptionSystemId));
        if (documentDescription == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " DocumentDescription, using systemId " +
                    documentDescriptionSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return documentDescription;
    }
    private void validateDocumentType(DocumentDescription documentDescription) {
        // Assume value already set, as the deserialiser will enforce it.
        DocumentType documentType =
            (DocumentType) documentTypeService
            .findValidMetadataOrThrow(documentDescription.getBaseTypeName(),
                                      documentDescription.getDocumentTypeCode(),
                                      documentDescription.getDocumentTypeCodeName());
        documentDescription.setDocumentTypeCodeName(documentType.getCodeName());
    }
}
