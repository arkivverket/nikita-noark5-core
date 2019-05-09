package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.VariantFormat;

/**
 * Created by tsodring on 12/03/18.
 */

public interface IVariantFormatService {

    MetadataHateoas createNewVariantFormat(VariantFormat variantFormat, String outgoingAddress);

    MetadataHateoas find(String systemId, String outgoingAddress);

    MetadataHateoas findAll(String outgoingAddress);

    MetadataHateoas findByDescription(String description, String outgoingAddress);

    MetadataHateoas findByCode(String code, String outgoingAddress);

    MetadataHateoas handleUpdate(String systemId, Long version, VariantFormat
            variantFormat, String outgoingAddress);

    VariantFormat generateDefaultVariantFormat();
}
