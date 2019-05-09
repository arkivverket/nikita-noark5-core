package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.RegistryEntryStatus;

/**
 * Created by tsodring on 12/02/18.
 */

public interface IRegistryEntryStatusService {

    MetadataHateoas createNewRegistryEntryStatus(
            RegistryEntryStatus RegistryEntryStatus, String outgoingAddress);

    MetadataHateoas find(String systemId, String outgoingAddress);

    MetadataHateoas findAll(String outgoingAddress);

    MetadataHateoas findByDescription(String description, String outgoingAddress);

    MetadataHateoas findByCode(String code, String outgoingAddress);

    MetadataHateoas handleUpdate(String systemId, Long version,
                                 RegistryEntryStatus RegistryEntryStatus,
                                 String outgoingAddress);

    RegistryEntryStatus generateDefaultRegistryEntryStatus();
}
