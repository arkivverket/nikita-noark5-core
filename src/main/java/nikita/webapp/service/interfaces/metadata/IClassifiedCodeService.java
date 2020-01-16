package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.ClassifiedCode;

import javax.validation.constraints.NotNull;

/**
 * Created by tsodring on 20/01/19.
 */

public interface IClassifiedCodeService
    extends IMetadataSuperService {

    MetadataHateoas createNewClassifiedCode(ClassifiedCode classificationType);

    MetadataHateoas findAll();

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final ClassifiedCode incomingClassificationType);

    ClassifiedCode generateDefaultClassifiedCode();
}
