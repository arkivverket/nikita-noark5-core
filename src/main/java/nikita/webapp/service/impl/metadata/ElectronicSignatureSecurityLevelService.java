package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.ElectronicSignatureSecurityLevel;
import nikita.common.repository.n5v5.metadata.IElectronicSignatureSecurityLevelRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IElectronicSignatureSecurityLevelService;
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
import static nikita.common.config.N5ResourceMappings.ELECTRONIC_SIGNATURE_SECURITY_LEVEL;

/**
 * Created by tsodring on 13/02/18.
 */

@Service
@Transactional
@SuppressWarnings("unchecked")
public class ElectronicSignatureSecurityLevelService
        extends NoarkService
        implements IElectronicSignatureSecurityLevelService {

    private static final Logger logger =
            LoggerFactory.
                    getLogger(ElectronicSignatureSecurityLevelService.class);

    private IElectronicSignatureSecurityLevelRepository
            electronicSignatureSecurityLevelRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public ElectronicSignatureSecurityLevelService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IElectronicSignatureSecurityLevelRepository
                    electronicSignatureSecurityLevelRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.electronicSignatureSecurityLevelRepository =
                electronicSignatureSecurityLevelRepository;
        this.metadataHateoasHandler = metadataHateoasHandler;
    }

    // All CREATE operations

    /**
     * Persists a new ElectronicSignatureSecurityLevel object to the database.
     *
     * @param electronicSignatureSecurityLevel ElectronicSignatureSecurityLevel
     *                                         object with values set
     * @return the newly persisted ElectronicSignatureSecurityLevel object
     * wrapped as a MetadataHateoas object
     */
    @Override
    public MetadataHateoas createNewElectronicSignatureSecurityLevel(
            ElectronicSignatureSecurityLevel electronicSignatureSecurityLevel) {

        MetadataHateoas metadataHateoas = new MetadataHateoas(
                electronicSignatureSecurityLevelRepository.save
                        (electronicSignatureSecurityLevel));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all ElectronicSignatureSecurityLevel objects
     *
     * @return list of ElectronicSignatureSecurityLevel objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INoarkEntity>) (List)
                        electronicSignatureSecurityLevelRepository.findAll()
                , ELECTRONIC_SIGNATURE_SECURITY_LEVEL);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all ElectronicSignatureSecurityLevel that have a particular code.

     *
     * @param code The code of the object you wish to retrieve
     * @return A list of ElectronicSignatureSecurityLevel objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                electronicSignatureSecurityLevelRepository.
                        findByCode(code));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Generate a default ElectronicSignatureSecurityLevel object
     *
     * @return the ElectronicSignatureSecurityLevel object wrapped as a
     * ElectronicSignatureSecurityLevelHateoas object
     */
    @Override
    public ElectronicSignatureSecurityLevel
    generateDefaultElectronicSignatureSecurityLevel() {

        ElectronicSignatureSecurityLevel electronicSignatureSecurityLevel =
                new ElectronicSignatureSecurityLevel();
        electronicSignatureSecurityLevel.setCode
                (TEMPLATE_ELECTRONIC_SIGNATURE_SECURITY_LEVEL_CODE);
        electronicSignatureSecurityLevel.setCodeName(
                TEMPLATE_ELECTRONIC_SIGNATURE_SECURITY_LEVEL_NAME);

        return electronicSignatureSecurityLevel;
    }

    /**
     * Update a ElectronicSignatureSecurityLevel identified by its code
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param code                         The code of the
     *                                         electronicSignatureSecurityLevel
     *                                         object you wish to update
     * @param electronicSignatureSecurityLevel The updated
     *                                         electronicSignatureSecurityLevel
     *                                         object. Note the values you
     *                                         are allowed to change are
     *                                         copied from this object. This
     *                                         object is not persisted.
     * @return the updated electronicSignatureSecurityLevel
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String code,
            @NotNull final Long version,
            @NotNull final ElectronicSignatureSecurityLevel
                    electronicSignatureSecurityLevel) {

        ElectronicSignatureSecurityLevel
                existingElectronicSignatureSecurityLevel =
                getElectronicSignatureSecurityLevelOrThrow(code);
        updateCodeAndDescription(electronicSignatureSecurityLevel,
                existingElectronicSignatureSecurityLevel);
        // Note setVersion can potentially result in a
        // NoarkConcurrencyException exception as it checks the ETAG value
        existingElectronicSignatureSecurityLevel.setVersion(version);

        MetadataHateoas electronicSignatureSecurityLevelHateoas =
                new MetadataHateoas(
                        electronicSignatureSecurityLevelRepository.save
                                (existingElectronicSignatureSecurityLevel));

        metadataHateoasHandler.addLinks(electronicSignatureSecurityLevelHateoas,
                new Authorisation());

        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this,
                        existingElectronicSignatureSecurityLevel));
        return electronicSignatureSecurityLevelHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid ElectronicSignatureSecurityLevel
     * object back. If there is no ElectronicSignatureSecurityLevel object,
     * a NoarkEntityNotFoundException exception is thrown
     *
     * @param code The code of the ElectronicSignatureSecurityLevel
     *                 object to retrieve
     * @return the ElectronicSignatureSecurityLevel object
     */
    private ElectronicSignatureSecurityLevel
    getElectronicSignatureSecurityLevelOrThrow(@NotNull String code) {
        ElectronicSignatureSecurityLevel electronicSignatureSecurityLevel =
                electronicSignatureSecurityLevelRepository.
                        findByCode(code);
        if (electronicSignatureSecurityLevel == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " ElectronicSignatureSecurityLevel, using code " +
                    code;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return electronicSignatureSecurityLevel;
    }
}
