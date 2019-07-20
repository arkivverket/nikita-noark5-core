package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.CaseStatus;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Created by tsodring on 13/03/18.
 */

public interface ICaseStatusService {

    MetadataHateoas createNewCaseStatus(CaseStatus caseStatus);

    MetadataHateoas findAll();

    MetadataHateoas findByCode(String code);

    CaseStatus findCaseStatusByCode(String code);

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final CaseStatus incomingCaseStatus);

    CaseStatus generateDefaultCaseStatus();

    Optional<CaseStatus> getDefaultCaseStatus();

    Optional<CaseStatus> getCaseStatusByCode();

    Optional<CaseStatus> getCaseStatusByDescription();
}
