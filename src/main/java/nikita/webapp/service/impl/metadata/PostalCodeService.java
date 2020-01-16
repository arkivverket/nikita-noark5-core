package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.PostalCode;
import nikita.common.repository.n5v5.metadata.IPostalCodeRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IPostalCodeService;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.POST_CODE;

/**
 * Created by tsodring on 14/03/18.
 */

@Service
@Transactional
public class PostalCodeService
        extends MetadataSuperService
        implements IPostalCodeService {

    private static final Logger logger =
            LoggerFactory.getLogger(PostalCodeService.class);

    private IPostalCodeRepository postalCodeRepository;

    public PostalCodeService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IPostalCodeRepository postalCodeRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher, metadataHateoasHandler);
        this.postalCodeRepository = postalCodeRepository;
    }
    // All CREATE operations

    /**
     * Persists a new PostalCode object to the database.
     *
     * @param postalCode PostalCode object with values set
     * @return the newly persisted PostalCode object wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas createNewPostalCode(
            PostalCode postalCode) {
        postalCode.setOwnedBy(SecurityContextHolder.getContext().
                getAuthentication().getName());

        MetadataHateoas metadataHateoas = new MetadataHateoas(
                postalCodeRepository.save(postalCode));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all PostalCode objects
     *
     * @return list of PostalCode objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    @SuppressWarnings("unchecked")
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<IMetadataEntity>) (List)
                        postalCodeRepository.findAll(), POST_CODE);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    @Override
    public PostalCode findMetadataByCode(String code) {
        return postalCodeRepository.findByCode(code);
    }

    /**
     * Generate a default PostalCode object
     *
     * @return the PostalCode object wrapped as a PostalCodeHateoas object
     */
    @Override
    public PostalCode generateDefaultPostalCode() {

        PostalCode postalCode = new PostalCode();
        postalCode.setCode(TEMPLATE_POST_CODE_CODE);
        postalCode.setCodeName(TEMPLATE_POST_CODE_NAME);

        return postalCode;
    }

    /**
     * Update a PostalCode identified by its code
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param code   The code of the postalCode object you wish to
     *                   update
     * @param incomingPostalCode The updated postalCode object. Note the
     *                           values you are allowed to change are copied
     *                           from this object. This object is not persisted.
     * @return the updated postalCode
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String code,
            @NotNull final Long version,
            @NotNull final PostalCode incomingPostalCode) {

        PostalCode existingPostalCode =
            (PostalCode) findMetadataByCodeOrThrow(code);
        updateCodeAndDescription(incomingPostalCode, existingPostalCode);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingPostalCode.setVersion(version);

        MetadataHateoas postalCodeHateoas = new MetadataHateoas(
                postalCodeRepository.save(existingPostalCode));

        metadataHateoasHandler.addLinks(postalCodeHateoas,
                new Authorisation());
        return postalCodeHateoas;
    }
}
