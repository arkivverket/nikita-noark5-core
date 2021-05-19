package nikita.common.repository.n5v5.secondary;

import nikita.common.model.noark5.v5.secondary.Precedence;
import nikita.common.repository.n5v5.NoarkEntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IPrecedenceRepository
        extends NoarkEntityRepository<Precedence, String> {

    Page<Precedence> findByOwnedBy(String ownedBy, Pageable pageable);
}
