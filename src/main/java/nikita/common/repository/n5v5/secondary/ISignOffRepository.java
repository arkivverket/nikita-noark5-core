package nikita.common.repository.n5v5.secondary;

import nikita.common.model.noark5.v5.secondary.SignOff;
import nikita.common.repository.n5v5.NoarkEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ISignOffRepository extends
        NoarkEntityRepository<SignOff, UUID> {
}
