package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.CaseStatus;

import java.util.Optional;

/**
 * Created by tsodring on 13/03/18.
 */

public interface ICaseStatusService {

    MetadataHateoas createNewCaseStatus(CaseStatus caseStatus,
                                        String outgoingAddress);

    MetadataHateoas find(String systemId, String outgoingAddress);

    MetadataHateoas findAll(String outgoingAddress);

    MetadataHateoas findByDescription(String description,
                                      String outgoingAddress);

    MetadataHateoas findByCode(String code, String outgoingAddress);

    MetadataHateoas handleUpdate(String systemId, Long version,
                                 CaseStatus caseStatus, String outgoingAddress);

    CaseStatus generateDefaultCaseStatus();

    Optional<CaseStatus> getDefaultCaseStatus();

    Optional<CaseStatus> getCaseStatusByCode();

    Optional<CaseStatus> getCaseStatusByDescription();
}
