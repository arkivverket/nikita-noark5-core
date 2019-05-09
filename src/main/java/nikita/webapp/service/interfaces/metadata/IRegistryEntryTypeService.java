package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.RegistryEntryType;

/**
 * Created by tsodring on 18/02/18.
 */

public interface IRegistryEntryTypeService {

    MetadataHateoas createNewRegistryEntryType(RegistryEntryType
                                                       registryEntryType,
                                               String outgoingAddress);

    MetadataHateoas find(String systemId, String outgoingAddress);

    MetadataHateoas findAll(String outgoingAddress);

    MetadataHateoas findByDescription(String description, String outgoingAddress);

    MetadataHateoas findByCode(String code, String outgoingAddress);

    MetadataHateoas handleUpdate(String systemId, Long version,
                                 RegistryEntryType registryEntryType,
                                 String outgoingAddress);

    RegistryEntryType generateDefaultRegistryEntryType();
}
