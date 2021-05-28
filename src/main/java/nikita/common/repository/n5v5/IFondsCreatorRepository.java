package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.FondsCreator;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IFondsCreatorRepository extends
        PagingAndSortingRepository<FondsCreator, UUID> {

    FondsCreator findBySystemId(UUID systemId);

    long deleteByOwnedBy(String ownedBy);
}
