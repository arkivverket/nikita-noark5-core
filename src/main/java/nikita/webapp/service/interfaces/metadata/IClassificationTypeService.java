package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.ClassificationType;

/**
 * Created by tsodring on 11/03/18.
 */

public interface IClassificationTypeService {

    MetadataHateoas createNewClassificationType(
            ClassificationType classificationType, String outgoingAddress);

    MetadataHateoas find(String systemId, String outgoingAddress);

    MetadataHateoas findAll(String outgoingAddress);

    MetadataHateoas findByDescription(String description,
                                      String outgoingAddress);

    MetadataHateoas findByCode(String code, String outgoingAddress);

    MetadataHateoas handleUpdate(String systemId, Long version,
                                 ClassificationType classificationType,
                                 String outgoingAddress);

    ClassificationType generateDefaultClassificationType();
}
