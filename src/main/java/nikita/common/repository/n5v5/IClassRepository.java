package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.Class;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IClassRepository
        extends PagingAndSortingRepository<Class, UUID> {

    Optional<Class> findBySystemId(UUID systemId);

    Page<Class> findByOwnedBy(String ownedBy, Pageable pageable);

    long deleteByOwnedBy(String ownedBy);
}
