package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.ChangeLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IChangeLogRepository extends
        PagingAndSortingRepository<ChangeLog, UUID> {

    ChangeLog findBySystemId(UUID systemId);

    Page<ChangeLog> findByOwnedBy(String ownedBy, Pageable pageable);
}
