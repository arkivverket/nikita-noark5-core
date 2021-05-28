package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.File;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IFileRepository extends
        PagingAndSortingRepository<File, UUID> {

    File findBySystemId(UUID systemId);

    long deleteByOwnedBy(String ownedBy);
}
