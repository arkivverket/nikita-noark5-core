package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.CoordinateSystem;
import nikita.common.repository.n5v5.metadata.ICoordinateSystemRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.ICoordinateSystemService;
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
import static nikita.common.config.N5ResourceMappings.COORDINATE_SYSTEM;

@Service
@Transactional
@SuppressWarnings("unchecked")
public class CoordinateSystemService
        extends MetadataSuperService
        implements ICoordinateSystemService {

    private static final Logger logger =
            LoggerFactory.getLogger(CoordinateSystemService.class);

    private ICoordinateSystemRepository coordinateSystemRepository;

    public CoordinateSystemService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            ICoordinateSystemRepository coordinateSystemRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher, metadataHateoasHandler);
        this.coordinateSystemRepository = coordinateSystemRepository;
    }

    // All CREATE operations

    /**
     * Persists a new CoordinateSystem object to the database.
     *
     * @param coordinateSystem CoordinateSystem object with values set
     * @return the newly persisted CoordinateSystem object wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas createNewCoordinateSystem(
            CoordinateSystem coordinateSystem) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                coordinateSystemRepository.save(coordinateSystem));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all CoordinateSystem objects
     *
     * @return list of CoordinateSystem objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<IMetadataEntity>) (List)
                coordinateSystemRepository.findAll(), COORDINATE_SYSTEM);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all CoordinateSystem that have a particular code.
     *
     * @param code The code of the object you wish to retrieve
     * @return A list of CoordinateSystem objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public CoordinateSystem findMetadataByCode(String code) {
        return coordinateSystemRepository.findByCode(code);
    }

    /**
     * Generate a default CoordinateSystem object
     *
     * @return the CoordinateSystem object wrapped as a CoordinateSystemHateoas object
     */
    @Override
    public CoordinateSystem generateDefaultCoordinateSystem() {

        CoordinateSystem coordinateSystem = new CoordinateSystem();
        // return empty value as there are no sensible values to send
        // out.
        return coordinateSystem;
    }

    /**
     * Update a CoordinateSystem identified by its code
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param code            The code of the coordinateSystem object you wish to
     *                        update
     * @param incomingCoordinateSystem The updated coordinateSystem object. Note the values
     *                        you are allowed to change are copied from this
     *                        object. This object is not persisted.
     * @return the updated coordinateSystem
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String code,
            @NotNull final Long version,
            @NotNull final CoordinateSystem incomingCoordinateSystem) {

        CoordinateSystem existingCoordinateSystem = (CoordinateSystem) findMetadataByCodeOrThrow(code);
        updateCodeAndDescription(incomingCoordinateSystem, existingCoordinateSystem);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingCoordinateSystem.setVersion(version);

        MetadataHateoas coordinateSystemHateoas = new MetadataHateoas(
                coordinateSystemRepository.save(existingCoordinateSystem));

        metadataHateoasHandler.addLinks(coordinateSystemHateoas,
                new Authorisation());
        return coordinateSystemHateoas;
    }
}
