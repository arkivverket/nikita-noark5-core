package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.PrecedenceStatus;

import javax.validation.constraints.NotNull;

/**
 * Created by tsodring on 19/02/18.
 */

public interface IPrecedenceStatusService {

    MetadataHateoas createNewPrecedenceStatus(PrecedenceStatus
                                                      precedenceStatus);

    MetadataHateoas find(String systemId);

    MetadataHateoas findAll();

    MetadataHateoas findByDescription(String description);

    MetadataHateoas findByCode(String code);

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final PrecedenceStatus incomingPrecedenceStatus);

    PrecedenceStatus generateDefaultPrecedenceStatus();
}
