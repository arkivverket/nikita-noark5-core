package nikita.common.repository.n5v5.secondary;

import nikita.common.model.noark5.v5.secondary.Keyword;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface IKeywordRepository
        extends PagingAndSortingRepository<Keyword, UUID> {
    Keyword findBySystemId(UUID systemId);

    Set<Keyword> findByOwnedBy(String user);
}
