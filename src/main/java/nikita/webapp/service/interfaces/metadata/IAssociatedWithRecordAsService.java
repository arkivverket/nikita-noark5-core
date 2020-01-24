package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.AssociatedWithRecordAs;

import javax.validation.constraints.NotNull;

public interface IAssociatedWithRecordAsService
    extends IMetadataSuperService {

    MetadataHateoas createNewAssociatedWithRecordAs(AssociatedWithRecordAs associatedWithRecordAs);

    MetadataHateoas findAll();

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final AssociatedWithRecordAs incomingAssociatedWithRecordAs);

    AssociatedWithRecordAs generateDefaultAssociatedWithRecordAs();
}
