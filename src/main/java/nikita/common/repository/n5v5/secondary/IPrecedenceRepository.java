package nikita.common.repository.n5v5.secondary;

import nikita.common.model.noark5.v5.casehandling.Precedence;
import nikita.common.repository.n5v5.NoarkEntityRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IPrecedenceRepository
        extends NoarkEntityRepository<Precedence, String> {
}
