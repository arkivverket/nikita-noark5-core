package nikita.webapp.service.impl.secondary;

import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.hateoas.secondary.DocumentFlowHateoas;
import nikita.common.model.noark5.v5.metadata.FlowStatus;
import nikita.common.model.noark5.v5.secondary.DocumentFlow;
import nikita.common.repository.n5v5.secondary.IDocumentFlowRepository;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.secondary.IDocumentFlowHateoasHandler;
import nikita.webapp.service.IUserService;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.odata.IODataService;
import nikita.webapp.service.interfaces.secondary.IDocumentFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.Constants.TEMPLATE_DOCUMENT_FLOW_FLOW_STATUS_CODE;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_FLOW_FLOW_FROM;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_FLOW_FLOW_TO;

@Service
public class DocumentFlowService
        extends NoarkService
        implements IDocumentFlowService {

    private static final Logger logger =
            LoggerFactory.getLogger(DocumentFlowService.class);

    private final IUserService userService;
    private final IMetadataService metadataService;
    private final IDocumentFlowRepository documentFlowRepository;
    private final IDocumentFlowHateoasHandler documentFlowHateoasHandler;

    public DocumentFlowService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IODataService odataService,
            IPatchService patchService,
            IUserService userService,
            IMetadataService metadataService,
            IDocumentFlowRepository documentFlowRepository,
            IDocumentFlowHateoasHandler documentFlowHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.userService = userService;
        this.metadataService = metadataService;
        this.documentFlowRepository = documentFlowRepository;
        this.documentFlowHateoasHandler = documentFlowHateoasHandler;
    }
    // All READ methods

    @Override
    public DocumentFlowHateoas findBySystemId(@NotNull final UUID systemId) {
        return packAsHateoas(getDocumentFlowOrThrow(systemId));
    }

    @Override
    public DocumentFlowHateoas findAll() {
        return (DocumentFlowHateoas) odataService.processODataQueryGet();
    }
    // All update methods

    @Override
    @Transactional
    public DocumentFlowHateoas associateDocumentFlowWithRegistryEntry
            (DocumentFlow documentFlow, RegistryEntry registryEntry) {
        validateFlowStatus(documentFlow);
        checkFlowSentDate(documentFlow);
        createUserReferences(documentFlow);
        documentFlow.setReferenceRegistryEntry(registryEntry);
        documentFlow = documentFlowRepository.save(documentFlow);
        registryEntry.addDocumentFlow(documentFlow);
        return packAsHateoas(documentFlow);
    }

    @Override
    @Transactional
    public DocumentFlowHateoas associateDocumentFlowWithRecordNote(
            @NotNull final DocumentFlow documentFlow,
            @NotNull final RecordNote recordNote) {
        validateFlowStatus(documentFlow);
        checkFlowSentDate(documentFlow);
        createUserReferences(documentFlow);
        documentFlow.setReferenceRecordNote(recordNote);
        return packAsHateoas(documentFlowRepository.save(documentFlow));
    }

    @Override
    @Transactional
    public DocumentFlowHateoas updateDocumentFlowBySystemId(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final DocumentFlow incoming) {
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
            existing.setReferenceFlowToSystemID(to.getSystemId());
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
            existing.setReferenceFlowFromSystemID(from.getSystemId());
        } else {
            existing.setFlowFrom(incoming.getFlowFrom());
            existing.setReferenceFlowFromSystemID
                (incoming.getReferenceFlowFromSystemID());
        }
        */
        existing.setVersion(version);
        return packAsHateoas(existing);
    }
    // All DELETE methods

    @Override
    public void deleteDocumentFlowBySystemId(@NotNull final UUID systemId) {
        documentFlowRepository.delete(getDocumentFlowOrThrow(systemId));
    }

    @Override
    public void deleteDocumentFlow(DocumentFlow documentFlow) {
        documentFlowRepository.delete(documentFlow);
    }
    // All template methods

    public DocumentFlowHateoas generateDefaultDocumentFlow() {
        DocumentFlow template = new DocumentFlow();
        template.setFlowSentDate(OffsetDateTime.now());
        template.setFlowStatus(new FlowStatus(
                TEMPLATE_DOCUMENT_FLOW_FLOW_STATUS_CODE));
        template.setVersion(-1L, true);
        validateFlowStatus(template);
        // Propose current user as the sender / creator of the flow record
        User u = userService.userGetByUsername(getUser());
        if (null != u) {
            template.setFlowFrom(u.getUsername());
            template.setReferenceFlowFromSystemID(u.getSystemId());
            template.setReferenceFlowFrom(u);
        } else {
            String info = "Unable to find User object for current user when generating template!";
            logger.warn(info);
        }
        return packAsHateoas(template);
    }
    // All helper methods

    /**
     * Internal helper method. Rather than having a find and try catch
     * in multiple methods, we have it here once. Note. If you call
     * this, you will only ever get a valid DocumentFlow back. If
     * there is no valid DocumentFlow, an exception is thrown
     *
     * @param systemId systemId of the DocumentFlow object to retrieve
     * @return the DocumentFlow object
     */
    protected DocumentFlow getDocumentFlowOrThrow(
            @NotNull final UUID systemId) {
        DocumentFlow documentFlow = documentFlowRepository.
                findBySystemId(systemId);
        if (documentFlow == null) {
            String error = INFO_CANNOT_FIND_OBJECT +
                    " DocumentFlow, using systemId " + systemId;
            logger.error(error);
            throw new NoarkEntityNotFoundException(error);
        }
        return documentFlow;
    }

    private DocumentFlowHateoas packAsHateoas(DocumentFlow documentFlow) {
        DocumentFlowHateoas documentFlowHateoas =
                new DocumentFlowHateoas(documentFlow);
        applyLinksAndHeader(documentFlowHateoas, documentFlowHateoasHandler);
        return documentFlowHateoas;
    }

    private void validateFlowStatus(DocumentFlow incomingDocumentFlow) {
        // Assume value already set, as the deserialiser will enforce it.
        FlowStatus flowStatus =
                (FlowStatus) metadataService.findValidMetadata(
                        incomingDocumentFlow.getFlowStatus());
        incomingDocumentFlow.setFlowStatus(flowStatus);
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
            documentFlow.setReferenceFlowToSystemID(to.getSystemId());
        }
        if (null != from
                && null == documentFlow.getFlowFrom()) {
            documentFlow.setFlowFrom(from.getUsername());
        }
        if (null != from
                && null == documentFlow.getReferenceFlowFromSystemID()) {
            documentFlow.setReferenceFlowFromSystemID(from.getSystemId());
        }
    }
}
