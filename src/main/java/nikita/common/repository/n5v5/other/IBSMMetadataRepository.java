package nikita.common.repository.n5v5.other;


import nikita.common.model.noark5.v5.md_other.BSMMetadata;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface IBSMMetadataRepository
        extends PagingAndSortingRepository<BSMMetadata, UUID> {
    Optional<BSMMetadata> findByName(String name);
}
