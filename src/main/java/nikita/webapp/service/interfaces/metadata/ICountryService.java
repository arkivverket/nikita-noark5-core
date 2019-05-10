package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.Country;

import javax.validation.constraints.NotNull;

/**
 * Created by tsodring on 14/03/18.
 */

public interface ICountryService {

    MetadataHateoas createNewCountry(Country country);

    MetadataHateoas find(String systemId);

    MetadataHateoas findAll();

    MetadataHateoas findByDescription(String description);

    MetadataHateoas findByCode(String code);

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final Country incomingCountry);

    Country generateDefaultCountry();
}
