package nikita.common.repository.n5v5.secondary;

import nikita.common.model.noark5.v5.secondary.ScreeningMetadataLocal;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IScreeningMetadataLocalRepository
        extends PagingAndSortingRepository<ScreeningMetadataLocal, UUID> {

    List<ScreeningMetadataLocal> findByOwnedBy(String ownedBy);

    Optional<ScreeningMetadataLocal> findBySystemId(UUID systemId);
}
