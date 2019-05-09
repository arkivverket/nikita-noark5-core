package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.ClassifiedCode;

/**
 * Created by tsodring on 20/01/19.
 */

public interface IClassifiedCodeService {

    MetadataHateoas createNewClassifiedCode(ClassifiedCode classificationType,
                                            String outgoingAddress);

    MetadataHateoas find(String systemId, String outgoingAddress);

    MetadataHateoas findAll(String outgoingAddress);

    MetadataHateoas findByDescription(String description, String outgoingAddress);

    MetadataHateoas findByCode(String code, String outgoingAddress);

    MetadataHateoas handleUpdate(String systemId, Long version,
                                 ClassifiedCode classificationType,
                                 String outgoingAddress);

    ClassifiedCode generateDefaultClassifiedCode();
}
