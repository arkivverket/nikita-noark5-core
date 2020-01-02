package nikita.common.repository.n5v5.secondary;

import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart;
import nikita.common.repository.n5v5.NoarkEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface ICorrespondencePartRepository extends
        NoarkEntityRepository<CorrespondencePart, UUID> {

    int deleteByOwnedBy(String ownedBy);
}
