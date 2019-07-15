package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.RegistryEntryType;

import javax.validation.constraints.NotNull;

/**
 * Created by tsodring on 18/02/18.
 */

public interface IRegistryEntryTypeService {

    MetadataHateoas createNewRegistryEntryType(RegistryEntryType
                                                       registryEntryType);

    MetadataHateoas findAll();

    MetadataHateoas findByCode(String code);

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final RegistryEntryType incomingRegistryEntryType);

    RegistryEntryType generateDefaultRegistryEntryType();
}
