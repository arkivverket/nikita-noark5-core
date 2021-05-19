package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IFileRepository extends
        PagingAndSortingRepository<File, UUID> {

    // -- All SAVE operations
    @Override
    File save(File file);

    // -- All READ operations
    @Override
    List<File> findAll();

    // systemId
    File findBySystemId(UUID systemId);

    // ownedBy
    Page<File> findByOwnedBy(String ownedBy, Pageable pageable);

    long deleteByOwnedBy(String ownedBy);
}
