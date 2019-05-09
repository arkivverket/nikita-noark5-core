package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v4.metadata.ScreeningDocument;
import nikita.common.repository.n5v4.metadata.IScreeningDocumentRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.metadata.IScreeningDocumentService;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        implements IScreeningDocumentService {

    private static final Logger logger =
            LoggerFactory.getLogger(ScreeningDocumentService.class);

    private IScreeningDocumentRepository screeningDocumentRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;
    private ApplicationEventPublisher applicationEventPublisher;

    public ScreeningDocumentService(
            IScreeningDocumentRepository
                    screeningDocumentRepository,
            IMetadataHateoasHandler metadataHateoasHandler,
            ApplicationEventPublisher applicationEventPublisher) {

        this.screeningDocumentRepository =
                screeningDocumentRepository;
        this.metadataHateoasHandler = metadataHateoasHandler;
        this.applicationEventPublisher = applicationEventPublisher;
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

        screeningDocument.setDeleted(false);
        screeningDocument.setOwnedBy(SecurityContextHolder.getContext().
                getAuthentication().getName());

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
                (List<INikitaEntity>) (List)
                        screeningDocumentRepository.findAll(),
                SCREENING_DOCUMENT);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // find by systemId

    /**
     * Retrieve a single ScreeningDocument object identified by systemId
     *
     * @param systemId systemId of the ScreeningDocument you wish to retrieve
     * @return single ScreeningDocument object wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas find(String systemId) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                screeningDocumentRepository
                        .findBySystemId(systemId));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Retrieve all ScreeningDocument that have a given description.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param description Description of object you wish to retrieve. The
     *                    whole text, this is an exact search.
     * @return A list of ScreeningDocument objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByDescription(String description) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        screeningDocumentRepository
                                .findByDescription(description),
                SCREENING_DOCUMENT);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all ScreeningDocument that have a particular code.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param code The code of the object you wish to retrieve
     * @return A list of ScreeningDocument objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        screeningDocumentRepository.findByCode(code),
                SCREENING_DOCUMENT);
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
        screeningDocument.setDescription(TEMPLATE_SCREENING_DOCUMENT_DESCRIPTION);

        return screeningDocument;
    }

    /**
     * Update a ScreeningDocument identified by its systemId
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param systemId          The systemId of the screeningDocument object you wish
     *                          to update
     * @param screeningDocument The updated screeningDocument object. Note the
     *                          values
     *                          you are allowed to change are copied from this
     *                          object. This object is not persisted.
     * @return the updated screeningDocument
     */
    @Override
    public MetadataHateoas handleUpdate(String systemId, Long
            version, ScreeningDocument screeningDocument) {

        ScreeningDocument existingScreeningDocument =
                getScreeningDocumentOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        if (null != screeningDocument.getCode()) {
            existingScreeningDocument.setCode(
                    screeningDocument.getCode());
        }
        if (null != screeningDocument.getDescription()) {
            existingScreeningDocument.setDescription(
                    screeningDocument.getDescription());
        }
        // Note this can potentially result in a NoarkConcurrencyException
        // exception
        existingScreeningDocument.setVersion(version);

        MetadataHateoas screeningMetadataHateoas = new MetadataHateoas(
                screeningDocumentRepository.save(existingScreeningDocument));

        metadataHateoasHandler.addLinks(screeningMetadataHateoas,
                new Authorisation());

        applicationEventPublisher.publishEvent(new
                AfterNoarkEntityUpdatedEvent(this,
                existingScreeningDocument));
        return screeningMetadataHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid ScreeningDocument object back. If
     * there is no ScreeningDocument object, a NoarkEntityNotFoundException
     * exception is thrown
     *
     * @param systemId The systemId of the ScreeningDocument object to retrieve
     * @return the ScreeningDocument object
     */
    private ScreeningDocument getScreeningDocumentOrThrow(
            @NotNull String systemId) {
        ScreeningDocument screeningDocument = screeningDocumentRepository.
                findBySystemId(systemId);
        if (screeningDocument == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " ScreeningDocument, using "
                    + "systemId " + systemId;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return screeningDocument;
    }
}
