package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.ClassificationSystem;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IClassificationSystemRepository extends
        PagingAndSortingRepository<ClassificationSystem, UUID> {

    ClassificationSystem findBySystemId(UUID systemId);

    long deleteByOwnedBy(String ownedBy);
}
