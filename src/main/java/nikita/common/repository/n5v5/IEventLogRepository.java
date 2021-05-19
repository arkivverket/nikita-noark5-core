package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.EventLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IEventLogRepository
        extends PagingAndSortingRepository<EventLog, UUID> {

    EventLog findBySystemId(UUID systemId);

    Page<EventLog> findByOwnedBy(String ownedBy, Pageable pageable);
}
