package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.AssociatedWithRecordAs;
import nikita.common.repository.n5v5.metadata.IAssociatedWithRecordAsRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IAssociatedWithRecordAsService;
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
import static nikita.common.config.N5ResourceMappings.ASSOCIATED_WITH_RECORD_AS;

@Service
@Transactional
@SuppressWarnings("unchecked")
public class AssociatedWithRecordAsService
        extends MetadataSuperService
        implements IAssociatedWithRecordAsService {

    private static final Logger logger =
            LoggerFactory.getLogger(AssociatedWithRecordAsService.class);

    private IAssociatedWithRecordAsRepository associatedWithRecordAsRepository;

    public AssociatedWithRecordAsService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IAssociatedWithRecordAsRepository associatedWithRecordAsRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher, metadataHateoasHandler);
        this.associatedWithRecordAsRepository = associatedWithRecordAsRepository;
    }

    // All CREATE operations

    /**
     * Persists a new AssociatedWithRecordAs object to the database.
     *
     * @param associatedWithRecordAs AssociatedWithRecordAs object with values set
     * @return the newly persisted AssociatedWithRecordAs object wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas createNewAssociatedWithRecordAs(
            AssociatedWithRecordAs associatedWithRecordAs) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                associatedWithRecordAsRepository.save(associatedWithRecordAs));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all AssociatedWithRecordAs objects
     *
     * @return list of AssociatedWithRecordAs objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<IMetadataEntity>) (List)
                associatedWithRecordAsRepository.findAll(), ASSOCIATED_WITH_RECORD_AS);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all AssociatedWithRecordAs that have a particular code.
     *
     * @param code The code of the object you wish to retrieve
     * @return A list of AssociatedWithRecordAs objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public AssociatedWithRecordAs findMetadataByCode(String code) {
        return associatedWithRecordAsRepository.findByCode(code);
    }

    /**
     * Generate a default AssociatedWithRecordAs object
     *
     * @return the AssociatedWithRecordAs object wrapped as a AssociatedWithRecordAsHateoas object
     */
    @Override
    public AssociatedWithRecordAs generateDefaultAssociatedWithRecordAs() {

        AssociatedWithRecordAs associatedWithRecordAs = new AssociatedWithRecordAs();
        // return empty value as there are no sensible values to send
        // out.
        return associatedWithRecordAs;
    }

    /**
     * Update a AssociatedWithRecordAs identified by its code
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param code            The code of the associatedWithRecordAs object you wish to
     *                        update
     * @param incomingAssociatedWithRecordAs The updated associatedWithRecordAs object. Note the values
     *                        you are allowed to change are copied from this
     *                        object. This object is not persisted.
     * @return the updated associatedWithRecordAs
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String code,
            @NotNull final Long version,
            @NotNull final AssociatedWithRecordAs incomingAssociatedWithRecordAs) {

        AssociatedWithRecordAs existingAssociatedWithRecordAs = (AssociatedWithRecordAs) findMetadataByCodeOrThrow(code);
        updateCodeAndDescription(incomingAssociatedWithRecordAs, existingAssociatedWithRecordAs);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingAssociatedWithRecordAs.setVersion(version);

        MetadataHateoas associatedWithRecordAsHateoas = new MetadataHateoas(
                associatedWithRecordAsRepository.save(existingAssociatedWithRecordAs));

        metadataHateoasHandler.addLinks(associatedWithRecordAsHateoas,
                new Authorisation());
        return associatedWithRecordAsHateoas;
    }
}
