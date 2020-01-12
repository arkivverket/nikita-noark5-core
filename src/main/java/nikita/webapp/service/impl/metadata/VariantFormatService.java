package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.VariantFormat;
import nikita.common.repository.n5v5.metadata.IVariantFormatRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IVariantFormatService;
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
import static nikita.common.config.N5ResourceMappings.VARIANT_FORMAT;

/**
 * Created by tsodring on 12/03/18.
 */

@Service
@Transactional
@SuppressWarnings("unchecked")
public class VariantFormatService
        extends NoarkService
        implements IVariantFormatService {

    private static final Logger logger =
            LoggerFactory.getLogger(VariantFormatService.class);

    private IVariantFormatRepository variantFormatRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public VariantFormatService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IVariantFormatRepository variantFormatRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.variantFormatRepository = variantFormatRepository;
        this.metadataHateoasHandler = metadataHateoasHandler;
    }

    // All CREATE operations

    /**
     * Persists a new VariantFormat object to the database.
     *
     * @param variantFormat VariantFormat object with values set
     * @return the newly persisted VariantFormat object wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas createNewVariantFormat(
            VariantFormat variantFormat) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                variantFormatRepository.save(variantFormat));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all VariantFormat objects
     *
     * @return list of VariantFormat objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INoarkEntity>) (List)
                        variantFormatRepository.findAll(), VARIANT_FORMAT);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all VariantFormat that have a particular code.

     *
     * @param code The code of the object you wish to retrieve
     * @return A list of VariantFormat objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                variantFormatRepository.findByCode(code));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Generate a default VariantFormat object
     *
     * @return the VariantFormat object wrapped as a VariantFormatHateoas object
     */
    @Override
    public VariantFormat generateDefaultVariantFormat() {
        VariantFormat variantFormat = new VariantFormat();
        variantFormat.setCode(TEMPLATE_VARIANT_FORMAT_CODE);
        variantFormat.setCodeName(TEMPLATE_VARIANT_FORMAT_NAME);
        return variantFormat;
    }

    /**
     * Update a VariantFormat identified by its code
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param code      The code of the variantFormat object you wish to
     *                      update
     * @param incomingVariantFormat The updated variantFormat object. Note
     *                              the values you are allowed to change are
     *                              copied from this object. This object is
     *                              not persisted.
     * @return the updated variantFormat
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String code,
            @NotNull final Long version,
            @NotNull final VariantFormat incomingVariantFormat) {
        VariantFormat existingVariantFormat = getVariantFormatOrThrow(code);
        updateCodeAndDescription(incomingVariantFormat, existingVariantFormat);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingVariantFormat.setVersion(version);

        MetadataHateoas variantFormatHateoas = new MetadataHateoas(
                variantFormatRepository.save(existingVariantFormat));

        metadataHateoasHandler.addLinks(variantFormatHateoas,
                new Authorisation());
        return variantFormatHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid VariantFormat object back. If there
     * is no VariantFormat object, a NoarkEntityNotFoundException exception
     * is thrown
     *
     * @param code The code of the VariantFormat object to retrieve
     * @return the VariantFormat object
     */
    private VariantFormat getVariantFormatOrThrow(@NotNull String code) {
        VariantFormat variantFormat = variantFormatRepository.findByCode(code);
        if (variantFormat == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " VariantFormat, using " +
                    "code " + code;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return variantFormat;
    }
}
