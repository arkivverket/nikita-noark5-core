package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.CasePartyRole;

/**
 * Created by tsodring on 21/02/18.
 */

public interface ICasePartyRoleService {

    MetadataHateoas createNewCasePartyRole(CasePartyRole casePartyRole, String outgoingAddress);

    MetadataHateoas find(String systemId, String outgoingAddress);

    MetadataHateoas findAll(String outgoingAddress);

    MetadataHateoas findByDescription(String description, String outgoingAddress);

    MetadataHateoas findByCode(String code, String outgoingAddress);

    MetadataHateoas handleUpdate(
            String systemId, Long version,
            CasePartyRole casePartyRole, String outgoingAddress);

    CasePartyRole generateDefaultCasePartyRole();
}
