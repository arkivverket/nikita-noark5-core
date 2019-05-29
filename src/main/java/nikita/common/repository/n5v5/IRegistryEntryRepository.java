package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRegistryEntryRepository extends
        PagingAndSortingRepository<RegistryEntry, Long> {

    // -- All SAVE operations
    @Override
    RegistryEntry save(RegistryEntry registryEntry);

    RegistryEntry findBySystemId(String systemId);

    Long countByReferenceFile(File file);

    long deleteByOwnedBy(String ownedBy);
}
