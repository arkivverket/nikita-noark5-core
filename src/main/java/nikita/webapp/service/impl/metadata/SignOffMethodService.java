package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.metadata.MetadataSuperClass;
import nikita.common.model.noark5.v5.metadata.SignOffMethod;
import nikita.common.repository.n5v5.metadata.ISignOffMethodRepository;
import nikita.common.util.exceptions.NoarkInvalidStructureException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.metadata.ISignOffMethodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;

import static nikita.common.config.Constants.TEMPLATE_SIGN_OFF_METHOD_CODE;
import static nikita.common.config.Constants.TEMPLATE_SIGN_OFF_METHOD_NAME;
import static nikita.common.config.N5ResourceMappings.SIGN_OFF_METHOD;

/**
 * Created by tsodring on 13/02/18.
 */


@Service
@Transactional
public class SignOffMethodService
        extends MetadataSuperService
        implements ISignOffMethodService {

    private static final Logger logger =
            LoggerFactory.getLogger(SignOffMethodService.class);

    private ISignOffMethodRepository signOffMethodRepository;

    public SignOffMethodService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            ISignOffMethodRepository signOffMethodRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher, metadataHateoasHandler);
        this.signOffMethodRepository = signOffMethodRepository;
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
                (List<IMetadataEntity>) (List)
                        signOffMethodRepository.findAll(), SIGN_OFF_METHOD);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    @Override
    public SignOffMethod findMetadataByCode(String code) {
        return signOffMethodRepository.findByCode(code);
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
        SignOffMethod.setCodeName(TEMPLATE_SIGN_OFF_METHOD_NAME);
        return SignOffMethod;
    }

    /**
     * Update a SignOffMethod identified by its code
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param code          The code of the signOffMethod object you wish to
     *                          update
     * @param incomingSignOffMethod The updated signOffMethod object.
     *                                  Note the values you are allowed to
     *                                  change are copied from this object.
     *                                  This object is not persisted.
     * @return the updated signOffMethod
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String code,
            @NotNull final Long version,
            @NotNull final SignOffMethod incomingSignOffMethod) {

        SignOffMethod existingSignOffMethod =
            (SignOffMethod) findMetadataByCodeOrThrow(code);
        updateCodeAndDescription(incomingSignOffMethod, existingSignOffMethod);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingSignOffMethod.setVersion(version);

        MetadataHateoas SignOffMethodHateoas = new MetadataHateoas(
                signOffMethodRepository.save(existingSignOffMethod));
        metadataHateoasHandler.addLinks(SignOffMethodHateoas,
                new Authorisation());
        return SignOffMethodHateoas;
    }

    public MetadataSuperClass findValidMetadataOrThrow(
            String parent, String code, String codename) {
        if (null == code) {
            String entityname = "TODO unknown entity name";
            String info = entityname + " malformed, missing code.";
            logger.info(info);
            throw new NoarkInvalidStructureException(
                    info, parent, entityname);
        }
        MetadataSuperClass entity =
                (MetadataSuperClass) findMetadataByCodeOrThrow(code);
        if (null != codename &&
                !entity.getCodeName().equals(codename)) {
            String entityname = entity.getBaseTypeName();
            String info = entityname + " code " + code + " and code name " +
                    codename + " did not match metadata catalog.";
            logger.info(info);
            throw new NoarkInvalidStructureException(
                    info, parent, entityname);
        }
        return entity;
    }

}
