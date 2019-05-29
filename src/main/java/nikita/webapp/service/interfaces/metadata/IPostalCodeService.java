package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.PostalCode;

import javax.validation.constraints.NotNull;

/**
 * Created by tsodring on 16/03/18.
 */

public interface IPostalCodeService {

    MetadataHateoas createNewPostalCode(PostalCode postalCode);

    MetadataHateoas find(String systemId);

    MetadataHateoas findAll();

    MetadataHateoas findByDescription(String description);

    MetadataHateoas findByCode(String code);

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final PostalCode incomingPostalCode);

    PostalCode generateDefaultPostalCode();
}
