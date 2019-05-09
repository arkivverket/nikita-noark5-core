package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.ElectronicSignatureSecurityLevel;

/**
 * Created by tsodring on 13/02/18.
 */

public interface IElectronicSignatureSecurityLevelService {

    MetadataHateoas createNewElectronicSignatureSecurityLevel(
            ElectronicSignatureSecurityLevel electronicSignatureSecurityLevel,
            String outgoingAddress);

    MetadataHateoas find(String systemId, String outgoingAddress);

    MetadataHateoas findAll(String outgoingAddress);

    MetadataHateoas findByDescription(String description, String outgoingAddress);

    MetadataHateoas findByCode(String code, String outgoingAddress);

    MetadataHateoas handleUpdate(String systemId, Long version,
                                 ElectronicSignatureSecurityLevel
                                         electronicSignatureSecurityLevel,
                                 String outgoingAddress);

    ElectronicSignatureSecurityLevel
    generateDefaultElectronicSignatureSecurityLevel();
}
