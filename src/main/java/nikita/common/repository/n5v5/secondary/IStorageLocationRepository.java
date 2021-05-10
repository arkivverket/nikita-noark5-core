package nikita.common.repository.n5v5.secondary;

import nikita.common.model.noark5.v5.secondary.StorageLocation;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface IStorageLocationRepository
        extends PagingAndSortingRepository<StorageLocation, UUID> {
    StorageLocation findBySystemId(UUID systemId);

    Set<StorageLocation> findByOwnedBy(String user);
}
