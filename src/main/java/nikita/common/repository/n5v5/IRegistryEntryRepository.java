package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IRegistryEntryRepository extends
        PagingAndSortingRepository<RegistryEntry, UUID> {

    RegistryEntry findBySystemId(UUID systemId);

    Page<RegistryEntry> findByOwnedBy(String ownedBy, Pageable pageable);

    List<RegistryEntry> findByReferenceFile(File file);

    long deleteByOwnedBy(String ownedBy);

    Long countByReferenceFile(File file);
}
