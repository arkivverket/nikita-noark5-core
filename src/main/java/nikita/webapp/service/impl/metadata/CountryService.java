package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.metadata.Country;
import nikita.common.repository.n5v5.metadata.ICountryRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.ICountryService;
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
import static nikita.common.config.N5ResourceMappings.COUNTRY;

/**
 * Created by tsodring on 14/03/18.
 */

@Service
@Transactional
@SuppressWarnings("unchecked")
public class CountryService
        extends NoarkService
        implements ICountryService {

    private static final Logger logger =
            LoggerFactory.getLogger(CountryService.class);

    private ICountryRepository countryRepository;
    private IMetadataHateoasHandler metadataHateoasHandler;

    public CountryService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            ICountryRepository countryRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.countryRepository = countryRepository;
        this.metadataHateoasHandler = metadataHateoasHandler;
    }

    // All CREATE operations

    /**
     * Persists a new Country object to the database.
     *
     * @param country Country object with values set
     * @return the newly persisted Country object wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas createNewCountry(
            Country country) {

        country.setDeleted(false);
        country.setOwnedBy(SecurityContextHolder.getContext().
                getAuthentication().getName());

        MetadataHateoas metadataHateoas = new MetadataHateoas(
                countryRepository.save(country));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all Country objects
     *
     * @return list of Country objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        countryRepository.findAll(), COUNTRY);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // find by systemId

    /**
     * Retrieve a single Country object identified by systemId
     *
     * @param systemId systemId of the Country you wish to retrieve
     * @return single Country object wrapped as a MetadataHateoas object
     */
    @Override
    public MetadataHateoas find(String systemId) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                countryRepository
                        .findBySystemId(systemId));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Retrieve all Country that have a given description.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param description Description of object you wish to retrieve. The
     *                    whole text, this is an exact search.
     * @return A list of Country objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByDescription(String description) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        countryRepository
                                .findByDescription(description), COUNTRY);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all Country that have a particular code.
     * <br>
     * Note, this will be replaced by OData search.
     *
     * @param code The code of the object you wish to retrieve
     * @return A list of Country objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public MetadataHateoas findByCode(String code) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<INikitaEntity>) (List)
                        countryRepository.findByCode(code), COUNTRY);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * Generate a default Country object
     *
     * @return the Country object wrapped as a CountryHateoas object
     */
    @Override
    public Country generateDefaultCountry() {

        Country country = new Country();
        country.setCode(TEMPLATE_COUNTRY_CODE);
        country.setDescription(TEMPLATE_COUNTRY_DESCRIPTION);

        return country;
    }

    /**
     * Update a Country identified by its systemId
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param systemId The systemId of the country object you wish to
     *                 update
     * @param incomingCountry  The updated country object. Note the values
     *                         you are allowed to change are copied from this
     *                         object. This object is not persisted.
     * @return the updated country
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final Country incomingCountry) {

        Country existingCountry = getCountryOrThrow(systemId);
        updateCodeAndDescription(incomingCountry, existingCountry);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingCountry.setVersion(version);

        MetadataHateoas countryHateoas = new MetadataHateoas(
                countryRepository.save(existingCountry));

        metadataHateoasHandler.addLinks(countryHateoas,
                new Authorisation());

        applicationEventPublisher.publishEvent(new
                AfterNoarkEntityUpdatedEvent(this,
                existingCountry));
        return countryHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid Country object back. If there
     * is no Country object, a NoarkEntityNotFoundException exception
     * is thrown
     *
     * @param systemId The systemId of the Country object to retrieve
     * @return the Country object
     */
    private Country getCountryOrThrow(@NotNull String systemId) {
        Country country = countryRepository.
                findBySystemId(systemId);
        if (country == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " Country, using " +
                    "systemId " + systemId;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return country;
    }
}
