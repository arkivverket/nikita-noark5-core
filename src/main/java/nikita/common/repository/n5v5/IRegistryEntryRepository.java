package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IRegistryEntryRepository extends
        PagingAndSortingRepository<RegistryEntry, UUID> {

    // -- All SAVE operations
    @Override
    RegistryEntry save(RegistryEntry registryEntry);

    RegistryEntry findBySystemId(UUID systemId);

    List<RegistryEntry> findByReferenceFile(File file);

    long deleteByOwnedBy(String ownedBy);

    Long countByReferenceFile(File file);
}
