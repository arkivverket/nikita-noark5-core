package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.ElectronicSignatureVerified;

import javax.validation.constraints.NotNull;

/**
 * Created by tsodring on 19/02/18.
 */

public interface IElectronicSignatureVerifiedService {

    MetadataHateoas createNewElectronicSignatureVerified(
            ElectronicSignatureVerified electronicSignatureVerified);

    MetadataHateoas find(String systemId);

    MetadataHateoas findAll();

    MetadataHateoas findByDescription(String description);

    MetadataHateoas findByCode(String code);

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final ElectronicSignatureVerified
                    incomingElectronicSignatureVerified);

    ElectronicSignatureVerified generateDefaultElectronicSignatureVerified();
}
