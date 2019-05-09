package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.PostalCode;

/**
 * Created by tsodring on 16/03/18.
 */

public interface IPostalCodeService {

    MetadataHateoas createNewPostalCode(PostalCode PostalCode,
                                        String outgoingAddress);

    MetadataHateoas find(String systemId, String outgoingAddress);

    MetadataHateoas findAll(String outgoingAddress);

    MetadataHateoas findByDescription(String description,
                                      String outgoingAddress);

    MetadataHateoas findByCode(String code, String outgoingAddress);

    MetadataHateoas handleUpdate(String systemId, Long version,
                                 PostalCode postalCode, String outgoingAddress);

    PostalCode generateDefaultPostalCode();
}
