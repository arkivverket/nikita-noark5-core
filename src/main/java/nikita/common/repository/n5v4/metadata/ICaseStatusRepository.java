package nikita.common.repository.n5v4.metadata;

import nikita.common.model.noark5.v4.metadata.CaseStatus;

import java.util.Optional;

public interface ICaseStatusRepository
        extends MetadataRepository<CaseStatus, Long> {

    Optional<CaseStatus> findByDefaultCaseStatus(Boolean defaultCaseStatus);
}
