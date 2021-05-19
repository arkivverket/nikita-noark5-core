package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.Series;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ISeriesRepository
        extends PagingAndSortingRepository<Series, UUID> {

    Series findBySystemId(UUID systemId);

    Page<Series> findByOwnedBy(String ownedBy, Pageable pageable);

    long deleteByOwnedBy(String ownedBy);
}
