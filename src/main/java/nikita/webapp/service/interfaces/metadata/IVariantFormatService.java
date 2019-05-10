package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.VariantFormat;

import javax.validation.constraints.NotNull;

/**
 * Created by tsodring on 12/03/18.
 */

public interface IVariantFormatService {

    MetadataHateoas createNewVariantFormat(VariantFormat variantFormat);

    MetadataHateoas find(String systemId);

    MetadataHateoas findAll();

    MetadataHateoas findByDescription(String description);

    MetadataHateoas findByCode(String code);

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final VariantFormat incomingVariantFormat);

    VariantFormat generateDefaultVariantFormat();
}
