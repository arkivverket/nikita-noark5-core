package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.DocumentType;
import nikita.common.repository.n5v5.metadata.IDocumentTypeRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IDocumentTypeService;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_TYPE;

/**
 * Created by tsodring on 07/02/18.
 */


@Service
@Transactional
public class DocumentTypeService
        extends MetadataSuperService
        implements IDocumentTypeService {

    private static final Logger logger =
            LoggerFactory.getLogger(DocumentTypeService.class);

    private IDocumentTypeRepository documentTypeRepository;

    public DocumentTypeService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IDocumentTypeRepository documentTypeRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher, metadataHateoasHandler);
        this.documentTypeRepository = documentTypeRepository;
    }
    // All CREATE operations

    /**
     * Persists a new documentType object to the database.
     *
     * @param documentType documentType object with values set
     * @return the newly persisted documentType object wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas createNewDocumentType(DocumentType documentType) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                documentTypeRepository.save(documentType));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all documentType objects
     *
     * @return list of documentType objects wrapped as a MetadataHateoas object
     */
    @Override
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<IMetadataEntity>) (List)
                        documentTypeRepository.findAll(), DOCUMENT_TYPE);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    @Override
    public DocumentType findMetadataByCode(String code) {
        return documentTypeRepository.findByCode(code);
    }

    /**
     * Generate a default DocumentType object
     *
     * @return the DocumentType object wrapped as a DocumentTypeHateoas object
     */
    @Override
    public DocumentType generateDefaultDocumentType() {
        DocumentType documentType = new DocumentType();
        documentType.setCode(TEMPLATE_DOCUMENT_TYPE_CODE);
        documentType.setCodeName(TEMPLATE_DOCUMENT_TYPE_NAME);

        return documentType;
    }

    /**
     * Update a DocumentType identified by its code
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param code    The code of the documentType object you wish to
     *                    update
     * @param incomingDocumentType The updated documentType object. Note the
     *                            values you are allowed to change are copied
     *                            from this object. This object is not
     *                            persisted.
     * @return the updated documentType
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String code,
            @NotNull final Long version,
            @NotNull final DocumentType incomingDocumentType) {

        DocumentType existingDocumentType =
	    (DocumentType) findMetadataByCodeOrThrow(code);
        updateCodeAndDescription(incomingDocumentType, existingDocumentType);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingDocumentType.setVersion(version);

        MetadataHateoas documentTypeHateoas = new MetadataHateoas(
                documentTypeRepository.save(existingDocumentType));
        metadataHateoasHandler.addLinks(documentTypeHateoas,
                new Authorisation());
        return documentTypeHateoas;
    }
}
