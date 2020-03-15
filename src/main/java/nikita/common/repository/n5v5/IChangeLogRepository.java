package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.ChangeLog;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IChangeLogRepository extends
        PagingAndSortingRepository<ChangeLog, UUID> {

    ChangeLog findBySystemId(UUID systemId);

    List<ChangeLog> findByOwnedBy(String ownedBy);
}
