package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.ScreeningMetadata;
import nikita.common.repository.n5v5.metadata.IScreeningMetadataRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IScreeningMetadataService;
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
import static nikita.common.config.N5ResourceMappings.SCREENING_METADATA;

/**
 * Created by tsodring on 07/04/18.
 */

@Service
@Transactional
@SuppressWarnings("unchecked")
public class ScreeningMetadataService
        extends NoarkService
        implements IScreeningMetadataService {

    private static final Logger logger =
            LoggerFactory.getLogger(ScreeningMetadataService.class);

    private IScreeningMetadataRepository screeningMetadataRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public ScreeningMetadataService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IScreeningMetadataRepository screeningMetadataRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.screeningMetadataRepository = screeningMetadataRepository;
        this.metadataHateoasHandler = metadataHateoasHandler;
    }

    // All CREATE operations

    /**
     * Persists a new ScreeningMetadata object to the database.
     *
     * @param screeningMetadata ScreeningMetadata object with values set
     * @return the newly persisted ScreeningMetadata object wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas createNewScreeningMetadata(
            ScreeningMetadata screeningMetadata) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                screeningMetadataRepository.save(screeningMetadata));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all ScreeningMetadata objects
     *
     * @return list of ScreeningMetadata objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<IMetadataEntity>) (List)
                        screeningMetadataRepository.findAll(),
                SCREENING_METADATA);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all ScreeningMetadata that have a particular code.

     *
     * @param code The code of the object you wish to retrieve
     * @return A list of ScreeningMetadata objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                getScreeningMetadataOrThrow(code));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Generate a default ScreeningMetadata object
     *
     * @return the ScreeningMetadata object wrapped as a ScreeningMetadataHateoas object
     */
    @Override
    public ScreeningMetadata generateDefaultScreeningMetadata() {
        ScreeningMetadata screeningMetadata = new ScreeningMetadata();
        screeningMetadata.setCode(TEMPLATE_SCREENING_METADATA_CODE);
        screeningMetadata.setCodeName(TEMPLATE_SCREENING_METADATA_NAME);
        return screeningMetadata;
    }

    /**
     * Update a ScreeningMetadata identified by its code
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param code          The code of the screeningMetadata object you wish to
     *                          update
     * @param incomingScreeningMetadata The updated screeningMetadata object.
     *                                  Note the values you are allowed to
     *                                  change are copied from this object.
     *                                  This object is not persisted.
     * @return the updated screeningMetadata
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String code,
            @NotNull final Long version,
            @NotNull final ScreeningMetadata incomingScreeningMetadata) {

        ScreeningMetadata existingScreeningMetadata =
                getScreeningMetadataOrThrow(code);
        updateCodeAndDescription(incomingScreeningMetadata,
                existingScreeningMetadata);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingScreeningMetadata.setVersion(version);

        MetadataHateoas screeningMetadataHateoas = new MetadataHateoas(
                screeningMetadataRepository.save(existingScreeningMetadata));

        metadataHateoasHandler.addLinks(screeningMetadataHateoas,
                new Authorisation());
        return screeningMetadataHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid ScreeningMetadata object back. If there
     * is no ScreeningMetadata object, a NoarkEntityNotFoundException exception
     * is thrown
     *
     * @param code The code of the ScreeningMetadata object to retrieve
     * @return the ScreeningMetadata object
     */
    private ScreeningMetadata getScreeningMetadataOrThrow(@NotNull String code) {
        ScreeningMetadata screeningMetadata = screeningMetadataRepository.
                findByCode(code);
        if (screeningMetadata == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " ScreeningMetadata, using " +
                    "code " + code;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return screeningMetadata;
    }
}
