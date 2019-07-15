package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.RegistryEntryStatus;

import javax.validation.constraints.NotNull;

/**
 * Created by tsodring on 12/02/18.
 */

public interface IRegistryEntryStatusService {

    MetadataHateoas createNewRegistryEntryStatus(
            RegistryEntryStatus RegistryEntryStatus);

    MetadataHateoas findByCode(String code);

    MetadataHateoas findAll();

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final RegistryEntryStatus incomingRegistryEntryStatus);

    RegistryEntryStatus generateDefaultRegistryEntryStatus();
}
