package nikita.common.repository.n5v5.metadata;

import nikita.common.model.noark5.v5.metadata.CaseStatus;

import java.util.Optional;

public interface ICaseStatusRepository
        extends MetadataRepository<CaseStatus, Long> {

    Optional<CaseStatus> findByCaseStatus(Boolean aseStatus);
}
