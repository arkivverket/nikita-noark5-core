package nikita.common.repository.n5v5.secondary;

import nikita.common.model.noark5.v5.secondary.CrossReference;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ICrossReferenceRepository
        extends PagingAndSortingRepository<CrossReference, UUID> {
    CrossReference findBySystemId(UUID systemId);

    Optional<CrossReference> findByFromSystemIdAndToSystemId(
            UUID fromSystemId, UUID toSystemId);

    Set<CrossReference> findByOwnedBy(String user);
}
