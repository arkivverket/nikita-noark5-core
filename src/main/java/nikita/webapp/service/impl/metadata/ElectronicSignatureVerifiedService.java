package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.metadata.ElectronicSignatureVerified;
import nikita.common.repository.n5v5.metadata.IElectronicSignatureVerifiedRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IElectronicSignatureVerifiedService;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.ELECTRONIC_SIGNATURE_VERIFIED;

/**
 * Created by tsodring on 19/02/18.
 */

@Service
@Transactional
@SuppressWarnings("unchecked")
public class ElectronicSignatureVerifiedService
        extends NoarkService
        implements IElectronicSignatureVerifiedService {

    private static final Logger logger =
            LoggerFactory.
                    getLogger(ElectronicSignatureVerifiedService.class);

    private IElectronicSignatureVerifiedRepository
            electronicSignatureVerifiedRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public ElectronicSignatureVerifiedService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IElectronicSignatureVerifiedRepository
                    electronicSignatureVerifiedRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.electronicSignatureVerifiedRepository =
                electronicSignatureVerifiedRepository;
        this.metadataHateoasHandler = metadataHateoasHandler;
    }

    // All CREATE operations

    /**
     * Persists a new ElectronicSignatureVerified object to the database.
     *
     * @param electronicSignatureVerified ElectronicSignatureVerified
     *                                    object with values set
     * @return the newly persisted ElectronicSignatureVerified object
     * wrapped as a MetadataHateoas object
     */
    @Override
    public MetadataHateoas createNewElectronicSignatureVerified(
            ElectronicSignatureVerified electronicSignatureVerified) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                electronicSignatureVerifiedRepository.save
                        (electronicSignatureVerified));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all ElectronicSignatureVerified objects
     *
     * @return list of ElectronicSignatureVerified objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        electronicSignatureVerifiedRepository.findAll()
                , ELECTRONIC_SIGNATURE_VERIFIED);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // find by systemId

    /**
     * Retrieve a single ElectronicSignatureVerified object identified by
     * systemId
     *
     * @param systemId systemId of the ElectronicSignatureVerified you wish
     *                 to retrieve
     * @return single ElectronicSignatureVerified object wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas find(String systemId) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                electronicSignatureVerifiedRepository.save(
                        electronicSignatureVerifiedRepository
                                .findBySystemId(UUID.fromString(systemId))));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Retrieve all ElectronicSignatureVerified that have a given
     * description.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param description Description of object you wish to retrieve. The
     *                    whole text, this is an exact search.
     * @return A list of ElectronicSignatureVerified objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas findByDescription(String description) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        electronicSignatureVerifiedRepository
                                .findByDescription(description),
                ELECTRONIC_SIGNATURE_VERIFIED);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all ElectronicSignatureVerified that have a particular code.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param code The code of the object you wish to retrieve
     * @return A list of ElectronicSignatureVerified objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        electronicSignatureVerifiedRepository.findByCode
                                (code),
                ELECTRONIC_SIGNATURE_VERIFIED);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Generate a default ElectronicSignatureVerified object
     *
     * @return the ElectronicSignatureVerified object wrapped as a
     * ElectronicSignatureVerifiedHateoas object
     */
    @Override
    public ElectronicSignatureVerified
    generateDefaultElectronicSignatureVerified() {

        ElectronicSignatureVerified electronicSignatureVerified =
                new ElectronicSignatureVerified();
        electronicSignatureVerified.setCode
                (TEMPLATE_ELECTRONIC_SIGNATURE_VERIFIED_CODE);
        electronicSignatureVerified.
                setDescription(
                        TEMPLATE_ELECTRONIC_SIGNATURE_VERIFIED_DESCRIPTION);

        return electronicSignatureVerified;
    }

    /**
     * Update a ElectronicSignatureVerified identified by its systemId
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param systemId                    The systemId of the
     *                                    electronicSignatureVerified
     *                                    object you wish to update
     * @param incomingElectronicSignatureVerified The updated
     *                                            electronicSignatureVerified
     *                                            object. Note the values you
     *                                            are allowed to change are
     *                                            copied from this object.
     *                                            This object is not persisted.
     * @return the updated electronicSignatureVerified
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final ElectronicSignatureVerified
                    incomingElectronicSignatureVerified) {

        ElectronicSignatureVerified
                existingElectronicSignatureVerified =
                getElectronicSignatureVerifiedOrThrow(systemId);

        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingElectronicSignatureVerified.setVersion(version);

        MetadataHateoas electronicSignatureVerifiedHateoas =
                new MetadataHateoas(
                        electronicSignatureVerifiedRepository.save
                                (existingElectronicSignatureVerified));

        metadataHateoasHandler.addLinks(electronicSignatureVerifiedHateoas,
                new Authorisation());

        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this,
                        existingElectronicSignatureVerified));
        return electronicSignatureVerifiedHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid ElectronicSignatureVerified
     * object back. If there is no ElectronicSignatureVerified object,
     * a NoarkEntityNotFoundException exception is thrown
     *
     * @param systemId The systemId of the ElectronicSignatureVerified
     *                 object to retrieve
     * @return the ElectronicSignatureVerified object
     */
    private ElectronicSignatureVerified
    getElectronicSignatureVerifiedOrThrow(@NotNull String systemId) {
        ElectronicSignatureVerified electronicSignatureVerified =
                electronicSignatureVerifiedRepository.
                        findBySystemId(UUID.fromString(systemId));
        if (electronicSignatureVerified == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " ElectronicSignatureVerified, using systemId " +
                    systemId;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return electronicSignatureVerified;
    }
}
