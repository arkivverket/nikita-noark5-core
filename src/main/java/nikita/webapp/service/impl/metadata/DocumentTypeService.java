package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
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
        extends NoarkService
        implements IDocumentTypeService {

    private static final Logger logger =
            LoggerFactory.getLogger(DocumentTypeService.class);

    private IDocumentTypeRepository documentTypeRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public DocumentTypeService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IDocumentTypeRepository documentTypeRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.documentTypeRepository = documentTypeRepository;
        this.metadataHateoasHandler = metadataHateoasHandler;
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
                (List<INikitaEntity>) (List)
                        documentTypeRepository.findAll(), DOCUMENT_TYPE);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all documentType that have a particular code.

     *
     * @param code
     * @return A list of documentType objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                documentTypeRepository.findByCode(code));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
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

        DocumentType existingDocumentType = getDocumentTypeOrThrow(code);
        updateCodeAndDescription(incomingDocumentType, existingDocumentType);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingDocumentType.setVersion(version);

        MetadataHateoas documentTypeHateoas = new MetadataHateoas(
                documentTypeRepository.save(existingDocumentType));
        metadataHateoasHandler.addLinks(documentTypeHateoas,
                new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this,
                        existingDocumentType));
        return documentTypeHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid DocumentType object back. If there
     * is no DocumentType object, a NoarkEntityNotFoundException exception
     * is thrown
     *
     * @param code The code of the DocumentType object to retrieve
     * @return the DocumentType object
     */
    private DocumentType getDocumentTypeOrThrow(@NotNull String code) {
        DocumentType documentType =
                documentTypeRepository.
                        findByCode(code);
        if (documentType == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " DocumentType, using " +
                    "code " + code;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return documentType;
    }

}
