package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.ElectronicSignatureVerified;

/**
 * Created by tsodring on 19/02/18.
 */

public interface IElectronicSignatureVerifiedService {

    MetadataHateoas createNewElectronicSignatureVerified(
            ElectronicSignatureVerified electronicSignatureVerified,
            String outgoingAddress);

    MetadataHateoas find(String systemId, String outgoingAddress);

    MetadataHateoas findAll(String outgoingAddress);

    MetadataHateoas findByDescription(String description,
                                      String outgoingAddress);

    MetadataHateoas findByCode(String code, String outgoingAddress);

    MetadataHateoas handleUpdate(String systemId, Long version,
                                 ElectronicSignatureVerified
                                         electronicSignatureVerified,
                                 String outgoingAddress);

    ElectronicSignatureVerified generateDefaultElectronicSignatureVerified();
}
