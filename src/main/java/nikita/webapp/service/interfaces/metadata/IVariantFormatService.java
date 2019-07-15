package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.VariantFormat;

import javax.validation.constraints.NotNull;

/**
 * Created by tsodring on 12/03/18.
 */

public interface IVariantFormatService {

    MetadataHateoas createNewVariantFormat(VariantFormat variantFormat);

    MetadataHateoas findAll();

    MetadataHateoas findByCode(String code);

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final VariantFormat incomingVariantFormat);

    VariantFormat generateDefaultVariantFormat();
}
