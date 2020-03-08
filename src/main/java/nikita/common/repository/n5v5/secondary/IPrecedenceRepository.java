package nikita.common.repository.n5v5.secondary;

import nikita.common.model.noark5.v5.secondary.Precedence;
import nikita.common.repository.n5v5.NoarkEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPrecedenceRepository
        extends NoarkEntityRepository<Precedence, String> {

    List<Precedence> findByOwnedBy(String ownedBy);
}
