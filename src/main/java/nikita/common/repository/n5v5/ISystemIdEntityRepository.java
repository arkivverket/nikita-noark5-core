package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.SystemIdEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ISystemIdEntityRepository extends
        PagingAndSortingRepository<SystemIdEntity, UUID> {
}
