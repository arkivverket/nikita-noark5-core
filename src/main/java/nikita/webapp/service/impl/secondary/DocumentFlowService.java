package nikita.webapp.service.impl.secondary;

import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.hateoas.secondary.DocumentFlowHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.FlowStatus;
import nikita.common.model.noark5.v5.secondary.DocumentFlow;
import nikita.common.repository.n5v5.secondary.IDocumentFlowRepository;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.secondary.IDocumentFlowHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.IUserService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.secondary.IDocumentFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.Constants.TEMPLATE_DOCUMENT_FLOW_FLOW_STATUS_CODE;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_FLOW_FLOW_FROM;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_FLOW_FLOW_TO;
import static nikita.common.config.N5ResourceMappings.FLOW_STATUS;

@Service
@Transactional
public class DocumentFlowService
    extends NoarkService
    implements IDocumentFlowService {

    private static final Logger logger =
            LoggerFactory.getLogger(DocumentFlowService.class);
    private IUserService userService;
    private IMetadataService metadataService;
    private IDocumentFlowRepository documentFlowRepository;
    private IDocumentFlowHateoasHandler documentFlowHateoasHandler;
    public DocumentFlowService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IUserService userService,
            IMetadataService metadataService,
            IDocumentFlowRepository documentFlowRepository,
            IDocumentFlowHateoasHandler documentFlowHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.userService = userService;
        this.metadataService = metadataService;
        this.documentFlowRepository = documentFlowRepository;
        this.documentFlowHateoasHandler = documentFlowHateoasHandler;
    }

    @Override
    public DocumentFlowHateoas associateDocumentFlowWithRegistryEntry
        (DocumentFlow documentFlow, RegistryEntry registryEntry) {
        validateFlowStatus(documentFlow);
        checkFlowSentDate(documentFlow);
        createUserReferences(documentFlow);
        documentFlow.setReferenceRegistryEntry(registryEntry);
        documentFlow = documentFlowRepository.save(documentFlow);
        registryEntry.addReferenceDocumentFlow(documentFlow);
        DocumentFlowHateoas documentFlowHateoas =
            new DocumentFlowHateoas(documentFlow);
        documentFlowHateoasHandler.addLinks(documentFlowHateoas,
                                            new Authorisation());
        setOutgoingRequestHeader(documentFlowHateoas);
        return documentFlowHateoas;
    }

    @Override
    public DocumentFlowHateoas associateDocumentFlowWithRecordNote
        (DocumentFlow documentFlow, RecordNote recordNote) {
        validateFlowStatus(documentFlow);
        checkFlowSentDate(documentFlow);
        createUserReferences(documentFlow);
        documentFlow.setReferenceRecordNote(recordNote);
        documentFlow = documentFlowRepository.save(documentFlow);
        recordNote.addReferenceDocumentFlow(documentFlow);
        DocumentFlowHateoas documentFlowHateoas =
            new DocumentFlowHateoas(documentFlow);
        documentFlowHateoasHandler.addLinks(documentFlowHateoas,
                                            new Authorisation());
        setOutgoingRequestHeader(documentFlowHateoas);
        return documentFlowHateoas;
    }

    @Override
    public DocumentFlowHateoas updateDocumentFlowBySystemId
        (String systemId, Long version, DocumentFlow incoming) {
        DocumentFlow existing = getDocumentFlowOrThrow(systemId);

        existing.setFlowReceivedDate(incoming.getFlowReceivedDate());
        existing.setFlowStatus(incoming.getFlowStatus());
        existing.setFlowComment(incoming.getFlowComment());

        /* Only allow 'sent' date to be set during creation
	existing.setFlowSentDate(incoming.getFlowSentDate());
	*/

        User to = userService.validateUserReference
            (DOCUMENT_FLOW_FLOW_TO, incoming.getReferenceFlowTo(),
             incoming.getFlowTo(),
             incoming.getReferenceFlowToSystemID());
        existing.setReferenceFlowTo(to);
        if (null != to) {
            existing.setFlowTo(to.getUsername());
            existing.setReferenceFlowToSystemID(to.getId());
        } else {
            existing.setFlowTo(incoming.getFlowTo());
            existing.setReferenceFlowToSystemID(incoming.getReferenceFlowToSystemID());
        }

        /* Only allow 'from' to be set during creation
        User from = userService.validateUserReference
            (DOCUMENT_FLOW_FLOW_FROM, incoming.getReferenceFlowFrom(),
             incoming.getFlowFrom(),
             incoming.getReferenceFlowFromSystemID());
        existing.setReferenceFlowFrom(from);
        if (null != from) {
            existing.setFlowFrom(from.getUsername());
            existing.setReferenceFlowFromSystemID(from.getId());
        } else {
            existing.setFlowFrom(incoming.getFlowFrom());
            existing.setReferenceFlowFromSystemID
                (incoming.getReferenceFlowFromSystemID());
        }
        */

        existing.setVersion(version);
        DocumentFlowHateoas documentFlowHateoas =
            new DocumentFlowHateoas(documentFlowRepository.save(existing));
        documentFlowHateoasHandler.addLinks(documentFlowHateoas,
                                            new Authorisation());
        setOutgoingRequestHeader(documentFlowHateoas);
        return documentFlowHateoas;
    }

    private void checkFlowSentDate(DocumentFlow documentFlow) {
        if (null == documentFlow.getFlowSentDate()) {
            documentFlow.setFlowSentDate(OffsetDateTime.now());
        }
    }

    private void createUserReferences(DocumentFlow documentFlow) {
        // Verify during creation: If systemid is set, and point to an
        // existing user, verify the username match the existing user
        // and set the reference to this user.  If systemid is not
        // set, make sure username is set and accept username and
        // systemid verbatim.
        User to = userService.validateUserReference
            (DOCUMENT_FLOW_FLOW_TO, documentFlow.getReferenceFlowTo(),
             documentFlow.getFlowTo(),
             documentFlow.getReferenceFlowToSystemID());
        User from = userService.validateUserReference
            (DOCUMENT_FLOW_FLOW_FROM, documentFlow.getReferenceFlowFrom(),
             documentFlow.getFlowFrom(),
             documentFlow.getReferenceFlowFromSystemID());
        if (null == from && null == documentFlow.getFlowFrom()) {
            String info = "Missing " + DOCUMENT_FLOW_FLOW_FROM +
                " username value";
            throw new NikitaMalformedInputDataException(info);
        }
        if (null == to && null == documentFlow.getFlowTo()) {
            String info = "Missing " + DOCUMENT_FLOW_FLOW_TO +
                " username value";
            throw new NikitaMalformedInputDataException(info);
        }
        if (null != to && null != from && to.equals(from)) {
            String info = DOCUMENT_FLOW_FLOW_FROM + " and " +
                DOCUMENT_FLOW_FLOW_TO + " are the same";
            throw new NikitaMalformedInputDataException(info);
        }

        documentFlow.setReferenceFlowTo(to);
        documentFlow.setReferenceFlowFrom(from);

        if (null != to
            && null == documentFlow.getFlowTo()) {
            documentFlow.setFlowTo(to.getUsername());
        }
        if (null != to
            && null == documentFlow.getReferenceFlowToSystemID()) {
            documentFlow.setReferenceFlowToSystemID(to.getId());
        }

        if (null != from
            && null == documentFlow.getFlowFrom()) {
            documentFlow.setFlowFrom(from.getUsername());
        }
        if (null != from
            && null == documentFlow.getReferenceFlowFromSystemID()) {
            documentFlow.setReferenceFlowFromSystemID(from.getId());
        }
    }

    @Override
    public void deleteDocumentFlowBySystemId(String systemID) {
        deleteEntity(getDocumentFlowOrThrow(systemID));
    }

    @Override
    @SuppressWarnings("unchecked")
    public DocumentFlowHateoas findAllByOwner() {
        DocumentFlowHateoas documentFlowHateoas =
            new DocumentFlowHateoas((List<INoarkEntity>) (List)
                documentFlowRepository.findByOwnedBy(getUser()));
        documentFlowHateoasHandler.addLinks(documentFlowHateoas,
                new Authorisation());
        setOutgoingRequestHeader(documentFlowHateoas);
        return documentFlowHateoas;
    }

    @Override
    public DocumentFlowHateoas findBySystemId(String documentFlowSystemId) {
        DocumentFlowHateoas documentFlowHateoas =
            new DocumentFlowHateoas(getDocumentFlowOrThrow(documentFlowSystemId));
        documentFlowHateoasHandler.addLinks(documentFlowHateoas,
                                            new Authorisation());
        setOutgoingRequestHeader(documentFlowHateoas);
        return documentFlowHateoas;
    }

    public DocumentFlowHateoas generateDefaultDocumentFlow() {
        DocumentFlow template = new DocumentFlow();

        template.setFlowSentDate(OffsetDateTime.now());

        template.setFlowStatus(new FlowStatus(TEMPLATE_DOCUMENT_FLOW_FLOW_STATUS_CODE));
        validateFlowStatus(template);

        // Propose current user as the sender / creator of the flow record
        User u = userService.userGetByUsername(getUser());
        if (null != u) {
            template.setFlowFrom(u.getUsername());
            template.setReferenceFlowFromSystemID(u.getId());
            template.setReferenceFlowFrom(u);
        } else {
            String info = "Unable to find User object for current user when generating template!";
            logger.warn(info);
        }

        DocumentFlowHateoas documentFlowHateoas =
            new DocumentFlowHateoas(template);
        documentFlowHateoasHandler
            .addLinksOnTemplate(documentFlowHateoas, new Authorisation());
        return documentFlowHateoas;
    }
    /**
     * Internal helper method. Rather than having a find and try catch
     * in multiple methods, we have it here once. Note. If you call
     * this, you will only ever get a valid DocumentFlow back. If
     * there is no valid DocumentFlow, an exception is thrown
     *
     * @param documentFlowSystemId systemID of the DocumentFlow object to retrieve
     * @return the DocumentFlow object
     */
    protected DocumentFlow getDocumentFlowOrThrow(
            @NotNull String documentFlowSystemId) {
        DocumentFlow documentFlow = documentFlowRepository.
                findBySystemId(UUID.fromString(documentFlowSystemId));
        if (documentFlow == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " DocumentFlow, using systemId " + documentFlowSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return documentFlow;
    }

    private void validateFlowStatus(DocumentFlow incomingDocumentFlow) {
        // Assume value already set, as the deserialiser will enforce it.
        FlowStatus flowStatus =
                (FlowStatus) metadataService
                    .findValidMetadataByEntityTypeOrThrow(
                            FLOW_STATUS,
                            incomingDocumentFlow.getFlowStatus());
        incomingDocumentFlow.setFlowStatus(flowStatus);
    }
}
