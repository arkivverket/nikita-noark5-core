package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.Series;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ISeriesRepository
        extends PagingAndSortingRepository<Series, UUID> {

    // -- All SAVE operations
    @Override
    Series save(Series series);

    // -- All READ operations
    @Override
    List<Series> findAll();

    // systemId
    Series findBySystemId(UUID systemId);

    // ownedBy
    List<Series> findByOwnedBy(String ownedBy);

    long deleteByOwnedBy(String ownedBy);
}
