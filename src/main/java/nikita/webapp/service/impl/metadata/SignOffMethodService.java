package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.metadata.SignOffMethod;
import nikita.common.repository.n5v5.metadata.ISignOffMethodRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.ISignOffMethodService;
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
import static nikita.common.config.N5ResourceMappings.SIGN_OFF_METHOD;

/**
 * Created by tsodring on 13/02/18.
 */


@Service
@Transactional
public class SignOffMethodService
        extends NoarkService
        implements ISignOffMethodService {

    private static final Logger logger =
            LoggerFactory.getLogger(SignOffMethodService.class);

    private ISignOffMethodRepository signOffMethodRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public SignOffMethodService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            ISignOffMethodRepository signOffMethodRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.signOffMethodRepository = signOffMethodRepository;
        this.metadataHateoasHandler = metadataHateoasHandler;
    }

    // All CREATE operations

    /**
     * Persists a new SignOffMethod object to the database.
     *
     * @param SignOffMethod SignOffMethod object with values set
     * @return the newly persisted SignOffMethod object wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas createNewSignOffMethod(
            SignOffMethod SignOffMethod) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                signOffMethodRepository.save(SignOffMethod));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all SignOffMethod objects
     *
     * @return list of SignOffMethod objects wrapped as a MetadataHateoas object
     */
    @Override
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        signOffMethodRepository.findAll(), SIGN_OFF_METHOD);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // find by systemId

    /**
     * Retrieve a single SignOffMethod object identified by systemId
     *
     * @param systemId
     * @return single SignOffMethod object wrapped as a MetadataHateoas object
     */
    @Override
    public MetadataHateoas find(String systemId) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                signOffMethodRepository.save(
                        signOffMethodRepository.
                                findBySystemId(UUID.fromString(systemId))));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Retrieve all SignOffMethod that have a given description.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param description
     * @return A list of SignOffMethod objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByDescription(String description) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        signOffMethodRepository.findByDescription(description),
                SIGN_OFF_METHOD);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all SignOffMethod that have a particular code.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param code
     * @return A list of SignOffMethod objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        signOffMethodRepository.findByCode(code),
                SIGN_OFF_METHOD);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Generate a default SignOffMethod object
     *
     * @return the SignOffMethod object wrapped as a SignOffMethodHateoas object
     */
    @Override
    public SignOffMethod generateDefaultSignOffMethod() {

        SignOffMethod SignOffMethod = new SignOffMethod();
        SignOffMethod.setCode(TEMPLATE_SIGN_OFF_METHOD_CODE);
        SignOffMethod.setDescription(TEMPLATE_SIGN_OFF_METHOD_DESCRIPTION);

        return SignOffMethod;
    }

    /**
     * Update a SignOffMethod identified by its systemId
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param systemId          The systemId of the signOffMethod object you wish to
     *                          update
     * @param incomingSignOffMethod The updated signOffMethod object.
     *                                  Note the values you are allowed to
     *                                  change are copied from this object.
     *                                  This object is not persisted.
     * @return the updated signOffMethod
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final SignOffMethod incomingSignOffMethod) {

        SignOffMethod existingSignOffMethod = getSignOffMethodOrThrow(systemId);
        updateCodeAndDescription(incomingSignOffMethod, existingSignOffMethod);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingSignOffMethod.setVersion(version);

        MetadataHateoas SignOffMethodHateoas = new MetadataHateoas(
                signOffMethodRepository.save(existingSignOffMethod));
        metadataHateoasHandler.addLinks(SignOffMethodHateoas,
                new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this,
                        existingSignOffMethod));
        return SignOffMethodHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid SignOffMethod object back. If there
     * is no SignOffMethod object, a NoarkEntityNotFoundException exception
     * is thrown
     *
     * @param systemId The systemId of the SignOffMethod object to retrieve
     * @return the SignOffMethod object
     */
    private SignOffMethod getSignOffMethodOrThrow(@NotNull String systemId) {
        SignOffMethod SignOffMethod =
                signOffMethodRepository.
                        findBySystemId(UUID.fromString(systemId));
        if (SignOffMethod == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " SignOffMethod, using " +
                    "systemId " + systemId;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return SignOffMethod;
    }

}
