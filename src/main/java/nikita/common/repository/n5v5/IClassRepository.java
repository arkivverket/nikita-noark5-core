package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.Class;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IClassRepository
        extends PagingAndSortingRepository<Class, Long> {
    // id
    Optional<Class> findById(Long id);

    // systemId
    Optional<Class> findBySystemId(String systemId);

    // ownedBy
    List<Class> findByOwnedBy(String ownedBy);

    long deleteByOwnedBy(String ownedBy);
}
