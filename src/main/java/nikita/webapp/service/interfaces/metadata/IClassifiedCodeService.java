package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.ClassifiedCode;

/**
 * Created by tsodring on 20/01/19.
 */

public interface IClassifiedCodeService {

    MetadataHateoas createNewClassifiedCode(ClassifiedCode classificationType);

    MetadataHateoas find(String systemId);

    MetadataHateoas findAll();

    MetadataHateoas findByDescription(String description);

    MetadataHateoas findByCode(String code);

    MetadataHateoas handleUpdate(String systemId, Long version, ClassifiedCode
            classificationType);

    ClassifiedCode generateDefaultClassifiedCode();
}
