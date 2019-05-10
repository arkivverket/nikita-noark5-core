package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v4.metadata.Format;
import nikita.common.repository.n5v4.metadata.IFormatRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IFormatService;
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
import static nikita.common.config.N5ResourceMappings.FORMAT;

/**
 * Created by tsodring on 15/02/18.
 */

@Service
@Transactional
@SuppressWarnings("unchecked")
public class FormatService
        extends NoarkService
        implements IFormatService {

    private static final Logger logger =
            LoggerFactory.getLogger(FormatService.class);

    private IFormatRepository formatRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public FormatService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IFormatRepository formatRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.formatRepository = formatRepository;
        this.metadataHateoasHandler = metadataHateoasHandler;
    }

    // All CREATE operations

    /**
     * Persists a new Format object to the database.
     *
     * @param format Format object with values set
     * @return the newly persisted Format object wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas createNewFormat(
            Format format) {

        format.setDeleted(false);
        format.setOwnedBy(SecurityContextHolder.getContext().
                getAuthentication().getName());

        MetadataHateoas metadataHateoas = new MetadataHateoas(
                formatRepository.save(format));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all Format objects
     *
     * @return list of Format objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        formatRepository.findAll(), FORMAT);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // find by systemId

    /**
     * Retrieve a single Format object identified by systemId
     *
     * @param systemId systemId of the Format you wish to retrieve
     * @return single Format object wrapped as a MetadataHateoas object
     */
    @Override
    public MetadataHateoas find(String systemId) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                formatRepository.save(
                        formatRepository
                                .findBySystemId(systemId)));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Retrieve all Format that have a given
     * description.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param description Description of object you wish to retrieve. The
     *                    whole text, this is an exact search.
     * @return A list of Format objects wrapped as a MetadataHateoas object
     */
    @Override
    public MetadataHateoas findByDescription(String description) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        formatRepository
                                .findByDescription(description),
                FORMAT);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all Format that have a particular code.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param code The code of the object you wish to retrieve
     * @return A list of Format objects wrapped as a MetadataHateoas object
     */
    @Override
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        formatRepository.findByCode
                                (code),
                FORMAT);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Generate a default Format object
     *
     * @return the Format object wrapped as a FormatHateoas object
     */
    @Override
    public Format
    generateDefaultFormat() {

        Format format = new Format();
        format.setCode(TEMPLATE_FORMAT_CODE);
        format.setDescription(TEMPLATE_FORMAT_DESCRIPTION);

        return format;
    }

    /**
     * Update a Format identified by its systemId
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param systemId The systemId of the format object you wish to update
     * @param incomingFormat The updated format object. Note the values you
     *                       are allowed to change are copied from this
     *                       object. This object is not persisted.
     * @return the updated format
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final Format incomingFormat) {

        Format existingFormat = getFormatOrThrow(systemId);
        updateCodeAndDescription(incomingFormat, existingFormat);

        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingFormat.setVersion(version);

        MetadataHateoas formatHateoas = new MetadataHateoas(formatRepository
                .save(existingFormat));

        metadataHateoasHandler.addLinks(formatHateoas, new Authorisation());

        applicationEventPublisher.publishEvent(new
                AfterNoarkEntityUpdatedEvent(this, existingFormat));
        return formatHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid Format object back. If there is no
     * Format object, a NoarkEntityNotFoundException exception is thrown
     *
     * @param systemId The systemId of the Format object to retrieve
     * @return the Format object
     */
    private Format
    getFormatOrThrow(@NotNull String systemId) {
        Format format =
                formatRepository.findBySystemId
                        (systemId);
        if (format == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " Format, using systemId " +
                    systemId;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return format;
    }
}
