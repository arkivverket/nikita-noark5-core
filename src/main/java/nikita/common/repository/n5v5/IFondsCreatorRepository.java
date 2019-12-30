package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.FondsCreator;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IFondsCreatorRepository extends
        PagingAndSortingRepository<FondsCreator, UUID> {

    // -- All SAVE operations
    @Override
    FondsCreator save(FondsCreator fondsCreator);

    // -- All READ operations
    @Override
    List<FondsCreator> findAll();

    FondsCreator findBySystemId(UUID systemId);

    List<FondsCreator> findByOwnedBy(String ownedBy);

    long deleteByOwnedBy(String ownedBy);
}
