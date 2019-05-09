package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.FlowStatus;

/**
 * Created by tsodring on 17/02/18.
 */

public interface IFlowStatusService {

    MetadataHateoas createNewFlowStatus(FlowStatus FlowStatus, String outgoingAddress);

    MetadataHateoas find(String systemId, String outgoingAddress);

    MetadataHateoas findAll(String outgoingAddress);

    MetadataHateoas findByDescription(String description, String outgoingAddress);

    MetadataHateoas findByCode(String code, String outgoingAddress);

    MetadataHateoas handleUpdate(String systemId, Long version, FlowStatus FlowStatus, String outgoingAddress);

    FlowStatus generateDefaultFlowStatus();
}
