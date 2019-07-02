package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.Series;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ISeriesRepository
        extends PagingAndSortingRepository<Series, Long> {

    // -- All SAVE operations
    @Override
    Series save(Series series);

    // -- All READ operations
    @Override
    List<Series> findAll();

    // id
    Optional<Series> findById(Long id);

    // systemId
    Series findBySystemId(UUID systemId);

    // ownedBy
    List<Series> findByOwnedBy(String ownedBy);

    long deleteByOwnedBy(String ownedBy);
}
