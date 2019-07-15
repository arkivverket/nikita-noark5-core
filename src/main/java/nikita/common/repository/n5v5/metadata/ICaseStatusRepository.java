package nikita.common.repository.n5v5.metadata;
import nikita.common.model.noark5.v5.metadata.CaseStatus;

import java.util.Optional;

public interface ICaseStatusRepository
        extends MetadataRepository<CaseStatus, String> {
    Optional<CaseStatus> findByCaseStatus(Boolean caseStatus);
}
