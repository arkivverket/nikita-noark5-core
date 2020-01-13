package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.ScreeningDocument;
import nikita.common.repository.n5v5.metadata.IScreeningDocumentRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IScreeningDocumentService;
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
import static nikita.common.config.N5ResourceMappings.SCREENING_DOCUMENT;

/**
 * Created by tsodring on 07/04/18.
 */

@Service
@Transactional
@SuppressWarnings("unchecked")
public class ScreeningDocumentService
        extends NoarkService
        implements IScreeningDocumentService {

    private static final Logger logger =
            LoggerFactory.getLogger(ScreeningDocumentService.class);

    private IScreeningDocumentRepository screeningDocumentRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public ScreeningDocumentService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IScreeningDocumentRepository screeningDocumentRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.screeningDocumentRepository = screeningDocumentRepository;
        this.metadataHateoasHandler = metadataHateoasHandler;
    }

    // All CREATE operations

    /**
     * Persists a new ScreeningDocument object to the database.
     *
     * @param screeningDocument ScreeningDocument object with values set
     * @return the newly persisted ScreeningDocument object wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas createNewScreeningDocument(
            ScreeningDocument screeningDocument) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                screeningDocumentRepository.save(screeningDocument));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all ScreeningDocument objects
     *
     * @return list of ScreeningDocument objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<IMetadataEntity>) (List)
                screeningDocumentRepository.findAll(), SCREENING_DOCUMENT);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all ScreeningDocument that have a particular code.
     *
     * @param code The code of the object you wish to retrieve
     * @return A list of ScreeningDocument objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas =
                new MetadataHateoas(
                        screeningDocumentRepository.findByCode(code));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Generate a default ScreeningDocument object
     *
     * @return the ScreeningDocument object wrapped as a
     * ScreeningMetadataHateoas object
     */
    @Override
    public ScreeningDocument generateDefaultScreeningDocument() {
        ScreeningDocument screeningDocument = new ScreeningDocument();
        screeningDocument.setCode(TEMPLATE_SCREENING_DOCUMENT_CODE);
        screeningDocument.setCodeName(TEMPLATE_SCREENING_DOCUMENT_NAME);
        return screeningDocument;
    }

    /**
     * Update a ScreeningDocument identified by its code
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param code                      The code of the screeningDocument object you wish
     *                                  to update
     * @param incomingScreeningDocument The updated screeningDocument object.
     *                                  Note the values you are allowed to
     *                                  change are copied from this object.
     *                                  This object is not persisted.
     * @return the updated screeningDocument
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String code,
            @NotNull final Long version,
            @NotNull final ScreeningDocument incomingScreeningDocument) {

        ScreeningDocument existingScreeningDocument =
                getScreeningDocumentOrThrow(code);
        updateCodeAndDescription(incomingScreeningDocument,
                existingScreeningDocument);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingScreeningDocument.setVersion(version);

        MetadataHateoas screeningMetadataHateoas = new MetadataHateoas(
                screeningDocumentRepository.save(existingScreeningDocument));

        metadataHateoasHandler.addLinks(screeningMetadataHateoas,
                new Authorisation());
        return screeningMetadataHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid ScreeningDocument object back. If
     * there is no ScreeningDocument object, a NoarkEntityNotFoundException
     * exception is thrown
     *
     * @param code The code of the ScreeningDocument object to retrieve
     * @return the ScreeningDocument object
     */
    private ScreeningDocument getScreeningDocumentOrThrow(
            @NotNull String code) {
        ScreeningDocument screeningDocument =
                screeningDocumentRepository.findByCode(code);
        if (screeningDocument == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " ScreeningDocument, using "
                    + "code " + code;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return screeningDocument;
    }
}
