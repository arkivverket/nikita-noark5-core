package nikita.common.repository.n5v5.secondary;

import nikita.common.model.noark5.v5.secondary.Conversion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IConversionRepository extends
        PagingAndSortingRepository<Conversion, UUID> {
    Conversion findBySystemId(UUID systemId);

    int deleteByOwnedBy(String ownedBy);
}
