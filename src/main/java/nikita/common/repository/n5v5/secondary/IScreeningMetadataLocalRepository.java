package nikita.common.repository.n5v5.secondary;

import nikita.common.model.noark5.v5.secondary.ScreeningMetadataLocal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IScreeningMetadataLocalRepository
        extends PagingAndSortingRepository<ScreeningMetadataLocal, UUID> {

    Page<ScreeningMetadataLocal> findByOwnedBy(String ownedBy, Pageable pageable);

    Optional<ScreeningMetadataLocal> findBySystemId(UUID systemId);
}
