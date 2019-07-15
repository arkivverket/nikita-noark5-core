package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.FlowStatus;

import javax.validation.constraints.NotNull;

/**
 * Created by tsodring on 17/02/18.
 */

public interface IFlowStatusService {

    MetadataHateoas createNewFlowStatus(FlowStatus flowStatus);

    MetadataHateoas findAll();

    MetadataHateoas findByCode(String code);

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final FlowStatus incomingFlowStatus);

    FlowStatus generateDefaultFlowStatus();
}
