package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.ClassificationType;

import javax.validation.constraints.NotNull;

/**
 * Created by tsodring on 11/03/18.
 */

public interface IClassificationTypeService
    extends IMetadataSuperService {

    MetadataHateoas createNewClassificationType(
            ClassificationType classificationType);

    MetadataHateoas findAll();

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final ClassificationType incomingClassificationType);

    ClassificationType generateDefaultClassificationType();
}
