package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.Class;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IClassRepository
        extends PagingAndSortingRepository<Class, UUID> {

    Optional<Class> findBySystemId(UUID systemId);

    long deleteByOwnedBy(String ownedBy);
}
