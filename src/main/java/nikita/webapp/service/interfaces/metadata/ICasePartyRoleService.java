package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.CasePartyRole;

import javax.validation.constraints.NotNull;

/**
 * Created by tsodring on 21/02/18.
 */

public interface ICasePartyRoleService {

    MetadataHateoas createNewCasePartyRole(CasePartyRole casePartyRole);

    MetadataHateoas find(String systemId);

    MetadataHateoas findAll();

    MetadataHateoas findByDescription(String description);

    MetadataHateoas findByCode(String code);

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final CasePartyRole incomingCasePartyRole);

    CasePartyRole generateDefaultCasePartyRole();
}
