package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.ClassificationSystem;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IClassificationSystemRepository extends
        PagingAndSortingRepository<ClassificationSystem, Long> {

    // -- All SAVE operations
    @Override
    ClassificationSystem save(ClassificationSystem classificationSystem);

    // -- All READ operations
    @Override
    List<ClassificationSystem> findAll();

    // id
    Optional<ClassificationSystem> findById(Long id);

    // systemId
    ClassificationSystem findBySystemId(String systemId);

    // ownedBy
    List<ClassificationSystem> findByOwnedBy(String ownedBy);

    long deleteByOwnedBy(String ownedBy);
}
