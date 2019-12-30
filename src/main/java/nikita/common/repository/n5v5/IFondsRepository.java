package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.Fonds;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IFondsRepository extends
        PagingAndSortingRepository<Fonds, UUID> {

    Fonds findBySystemId(UUID systemId);

    List<Fonds> findByOwnedBy(String ownedBy);

    long deleteByOwnedBy(String ownedBy);
}
