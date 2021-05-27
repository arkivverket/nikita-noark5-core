package nikita.webapp.service.impl;

import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.hateoas.DocumentDescriptionHateoas;
import nikita.common.model.noark5.v5.hateoas.DocumentObjectHateoas;
import nikita.common.model.noark5.v5.hateoas.RecordHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.*;
import nikita.common.model.noark5.v5.metadata.AssociatedWithRecordAs;
import nikita.common.model.noark5.v5.metadata.DocumentStatus;
import nikita.common.model.noark5.v5.metadata.DocumentType;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.model.noark5.v5.secondary.*;
import nikita.common.repository.n5v5.IDocumentDescriptionRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IDocumentDescriptionHateoasHandler;
import nikita.webapp.hateoas.interfaces.secondary.IScreeningMetadataHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.interfaces.IBSMService;
import nikita.webapp.service.interfaces.IDocumentDescriptionService;
import nikita.webapp.service.interfaces.IDocumentObjectService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.odata.IODataService;
import nikita.webapp.service.interfaces.secondary.IAuthorService;
import nikita.webapp.service.interfaces.secondary.ICommentService;
import nikita.webapp.service.interfaces.secondary.IPartService;
import nikita.webapp.service.interfaces.secondary.IScreeningMetadataService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.List.copyOf;
import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.DatabaseConstants.DELETE_FROM_RECORD_DOCUMENT_DESCRIPTION;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.*;

@Service
public class DocumentDescriptionService
        extends NoarkService
        implements IDocumentDescriptionService {

    private final IDocumentObjectService documentObjectService;
    private final IDocumentDescriptionRepository documentDescriptionRepository;
    private final IDocumentDescriptionHateoasHandler documentDescriptionHateoasHandler;
    private final IAuthorService authorService;
    private final ICommentService commentService;
    private final IMetadataService metadataService;
    private final IPartService partService;
    private final IBSMService bsmService;
    private final IScreeningMetadataService screeningMetadataService;
    private final IScreeningMetadataHateoasHandler screeningMetadataHateoasHandler;

    public DocumentDescriptionService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IODataService odataService,
            IPatchService patchService,
            IDocumentObjectService documentObjectService,
            IDocumentDescriptionRepository documentDescriptionRepository,
            IDocumentDescriptionHateoasHandler
                    documentDescriptionHateoasHandler,
            IAuthorService authorService,
            ICommentService commentService,
            IMetadataService metadataService,
            IPartService partService,
            IBSMService bsmService,
            IScreeningMetadataService screeningMetadataService,
            IScreeningMetadataHateoasHandler screeningMetadataHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.documentObjectService = documentObjectService;
        this.documentDescriptionRepository = documentDescriptionRepository;
        this.documentDescriptionHateoasHandler =
                documentDescriptionHateoasHandler;
        this.authorService = authorService;
        this.commentService = commentService;
        this.metadataService = metadataService;
        this.partService = partService;
        this.bsmService = bsmService;
        this.screeningMetadataService = screeningMetadataService;
        this.screeningMetadataHateoasHandler = screeningMetadataHateoasHandler;
    }

    // All CREATE operations
    @Override
    @Transactional
    public DocumentObjectHateoas
    createDocumentObjectAssociatedWithDocumentDescription(
            @NotNull final UUID systemId,
            @NotNull final DocumentObject documentObject) {
        DocumentDescription documentDescription =
                getDocumentDescriptionOrThrow(systemId);
        documentObject.setReferenceDocumentDescription(documentDescription);
        List<DocumentObject> documentObjects = documentDescription
                .getReferenceDocumentObject();
        documentObjects.add(documentObject);
        bsmService.validateBSMList(documentDescription.getReferenceBSMBase());
        return documentObjectService.save(documentObject);
    }

    @Override
    @Transactional
    public ScreeningMetadataHateoas
    createScreeningMetadataAssociatedWithDocumentDescription(
            UUID systemId, Metadata screeningMetadata) {
        DocumentDescription documentDescription =
                getDocumentDescriptionOrThrow(systemId);
        if (null == documentDescription.getReferenceScreening()) {
            throw new NoarkEntityNotFoundException(INFO_CANNOT_FIND_OBJECT +
                    " Screening, associated with DocumentDescription with systemId " +
                    systemId);
        }
        return screeningMetadataService.createScreeningMetadata(
                documentDescription.getReferenceScreening(), screeningMetadata);
    }

    @Override
    @Transactional
    public CommentHateoas createCommentAssociatedWithDocumentDescription
            (@NotNull final UUID systemId, Comment comment) {
        return commentService.createNewComment
                (comment, getDocumentDescriptionOrThrow(systemId));
    }

    @Override
    @Transactional
    public PartPersonHateoas
    createPartPersonAssociatedWithDocumentDescription(
            UUID systemId, PartPerson partPerson) {
        return partService.
                createNewPartPerson(partPerson,
                        getDocumentDescriptionOrThrow(systemId));
    }

    @Override
    @Transactional
    public PartUnitHateoas
    createPartUnitAssociatedWithDocumentDescription(
            UUID systemId, PartUnit partUnit) {
        return partService.
                createNewPartUnit(partUnit,
                        getDocumentDescriptionOrThrow(systemId));
    }

    @Override
    @Transactional
    public DocumentDescriptionHateoas save(DocumentDescription documentDescription) {
        validateDocumentMedium(metadataService, documentDescription);
        validateDocumentStatus(documentDescription);
        validateDocumentType(documentDescription);
        validateDeletion(documentDescription.getReferenceDeletion());
        bsmService.validateBSMList(documentDescription.getReferenceBSMBase());
        documentDescription.setAssociationDate(OffsetDateTime.now());
        documentDescription.setAssociatedBy(getUser());
        return packAsHateoas(documentDescriptionRepository
                .save(documentDescription));
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
    @Transactional
    public AuthorHateoas associateAuthorWithDocumentDescription(
            UUID systemId, Author author) {
        return authorService.associateAuthorWithDocumentDescription
                (author, getDocumentDescriptionOrThrow(systemId));
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
    public DocumentDescriptionHateoas generateDefaultDocumentDescription(
            @NotNull final UUID systemId) {
        DocumentDescription defaultDocumentDescription =
                new DocumentDescription();

        AssociatedWithRecordAs associatedWithRecordAs = (AssociatedWithRecordAs)
                metadataService.findValidMetadataByEntityTypeOrThrow
                        (ASSOCIATED_WITH_RECORD_AS, MAIN_DOCUMENT_CODE, null);
        defaultDocumentDescription
                .setAssociatedWithRecordAs(associatedWithRecordAs);
        DocumentType documentType = (DocumentType)
                metadataService.findValidMetadataByEntityTypeOrThrow
                        (DOCUMENT_TYPE, LETTER_CODE, null);
        defaultDocumentDescription.setDocumentType(documentType);
        DocumentStatus documentStatus = (DocumentStatus)
                metadataService.findValidMetadataByEntityTypeOrThrow
                        (DOCUMENT_STATUS, DOCUMENT_STATUS_FINALISED_CODE, null);
        defaultDocumentDescription.setDocumentStatus(documentStatus);
        defaultDocumentDescription.setVersion(-1L, true);
        return packAsHateoas(defaultDocumentDescription);
    }

    @Override
    public CommentHateoas generateDefaultComment(@NotNull final UUID systemId) {
        return commentService.generateDefaultComment(systemId);
    }

    @Override
    public PartPersonHateoas generateDefaultPartPerson(
            @NotNull final UUID systemId) {
        return partService.generateDefaultPartPerson(systemId);
    }

    @Override
    public PartUnitHateoas generateDefaultPartUnit(@NotNull final UUID systemId) {
        return partService.generateDefaultPartUnit(systemId);
    }

    @Override
    public AuthorHateoas generateDefaultAuthor(@NotNull final UUID systemId) {
        return authorService.generateDefaultAuthor(systemId);
    }

    @Override
    public ScreeningMetadataHateoas getDefaultScreeningMetadata(
            @NotNull final UUID systemId) {
        return screeningMetadataService.getDefaultScreeningMetadata(systemId);
    }

    @Override
    public DocumentDescriptionHateoas findBySystemId(
            @NotNull final UUID systemId) {
        return packAsHateoas(getDocumentDescriptionOrThrow(systemId));
    }

    @Override
    public AuthorHateoas findAllAuthorWithDocumentDescriptionBySystemId(
            UUID systemId) {
        getDocumentDescriptionOrThrow(systemId);
        return (AuthorHateoas) odataService.processODataQueryGet();
    }

    @Override
    public ScreeningMetadataHateoas
    getScreeningMetadataAssociatedWithDocumentDescription(
            @NotNull final UUID systemId) {
        Screening screening = getDocumentDescriptionOrThrow(systemId)
                .getReferenceScreening();
        if (null == screening) {
            throw new NoarkEntityNotFoundException(
                    INFO_CANNOT_FIND_OBJECT + " Screening, using systemId " +
                            systemId);
        }
        return packAsHateoas(new NikitaPage(copyOf(
                screening.getReferenceScreeningMetadata())));
    }

    @Override
    public DocumentDescriptionHateoas findAll() {
        return (DocumentDescriptionHateoas) odataService.processODataQueryGet();
    }

    @Override
    public DocumentObjectHateoas
    findAllDocumentObjectWithDocumentDescriptionBySystemId(
            @NotNull final UUID systemId) {
        getDocumentDescriptionOrThrow(systemId);
        return (DocumentObjectHateoas) odataService.processODataQueryGet();
    }

    @Override
    public RecordHateoas
    findAllRecordWithDocumentDescriptionBySystemId(
            @NotNull final UUID systemId) {
        getDocumentDescriptionOrThrow(systemId);
        return (RecordHateoas) odataService.processODataQueryGet();
    }

    @Override
    public CommentHateoas getCommentAssociatedWithDocumentDescription(
            @NotNull final UUID systemId) {
        return (CommentHateoas) odataService.processODataQueryGet();
    }

    @Override
    public PartHateoas getPartAssociatedWithDocumentDescription(
            @NotNull final UUID systemId) {
        return (PartHateoas) odataService.processODataQueryGet();
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
     * @param version                     version of object, field is given by nikita
     * @param incomingDocumentDescription the incoming documentDescription
     * @return the updated documentDescription after it is persisted
     */
    @Override
    @Transactional
    public DocumentDescriptionHateoas handleUpdate(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final DocumentDescription incomingDocumentDescription) {
        DocumentDescription existingDocumentDescription =
                getDocumentDescriptionOrThrow(systemId);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingDocumentDescription.setVersion(version);
        updateDeletion(incomingDocumentDescription,
                existingDocumentDescription);
        updateTitleAndDescription(incomingDocumentDescription,
                existingDocumentDescription);
        updateDocumentDescription(incomingDocumentDescription,
                existingDocumentDescription);

        validateDocumentType(incomingDocumentDescription);
        existingDocumentDescription.setDocumentType(
                incomingDocumentDescription.getDocumentType());
        validateDocumentStatus(incomingDocumentDescription);
        existingDocumentDescription.setDocumentStatus(
                incomingDocumentDescription.getDocumentStatus());

        bsmService.validateBSMList(incomingDocumentDescription
                .getReferenceBSMBase());

        existingDocumentDescription.setStorageLocation(
                incomingDocumentDescription.getStorageLocation());
        return packAsHateoas(existingDocumentDescription);
    }

    // All DELETE operations

    @Override
    @Transactional
    public void deleteEntity(@NotNull final UUID systemId) {
        DocumentDescription documentDescription =
                getDocumentDescriptionOrThrow(systemId);
        // Disassociate any links between DocumentDescription and Record
        disassociateForeignKeys(documentDescription,
                DELETE_FROM_RECORD_DOCUMENT_DESCRIPTION);
        deleteEntity(documentDescription);
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     */
    @Override
    @Transactional
    public void deleteAllByOwnedBy() {
        documentDescriptionRepository.deleteByOwnedBy(getUser());
    }

    // All HELPER operations

    public ScreeningMetadataHateoas packAsHateoas(NikitaPage page) {
        ScreeningMetadataHateoas screeningMetadataHateoas =
                new ScreeningMetadataHateoas(page);
        applyLinksAndHeader(screeningMetadataHateoas,
                screeningMetadataHateoasHandler);
        return screeningMetadataHateoas;
    }

    public DocumentDescriptionHateoas packAsHateoas(
            @NotNull final DocumentDescription documentDescription) {
        DocumentDescriptionHateoas documentDescriptionHateoas =
                new DocumentDescriptionHateoas(documentDescription);
        applyLinksAndHeader(documentDescriptionHateoas,
                documentDescriptionHateoasHandler);
        return documentDescriptionHateoas;
    }

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
        if (null != incomingDocumentDescription.getDocumentType()) {
            existingDocumentDescription.setDocumentType(
                    incomingDocumentDescription.getDocumentType());
        }
        if (null != incomingDocumentDescription.getDocumentStatus()) {
            existingDocumentDescription.setDocumentStatus(
                    incomingDocumentDescription.getDocumentStatus());
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
     * @param systemId systemId of the documentDescription
     * @return The documentDescription to be returned
     */
    protected DocumentDescription getDocumentDescriptionOrThrow(
            @NotNull final UUID systemId) {
        DocumentDescription documentDescription =
                documentDescriptionRepository.findBySystemId(
                        systemId);
        if (documentDescription == null) {
            String error = INFO_CANNOT_FIND_OBJECT +
                    " DocumentDescription, using systemId " +
                    systemId;
            throw new NoarkEntityNotFoundException(error);
        }
        return documentDescription;
    }

    private void validateDocumentStatus(DocumentDescription documentDescription) {
        // Assume value already set, as the deserialiser will enforce it.
        DocumentStatus documentStatus = (DocumentStatus)
                metadataService.findValidMetadata(
                        documentDescription.getDocumentStatus());
        documentDescription.setDocumentStatus(documentStatus);
    }

    private void validateDocumentType(DocumentDescription documentDescription) {
        // Assume value already set, as the deserialiser will enforce it.
        DocumentType documentType = (DocumentType)
                metadataService.findValidMetadata(
                        documentDescription.getDocumentType());
        documentDescription.setDocumentType(documentType);
    }
}
