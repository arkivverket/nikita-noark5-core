package nikita.common.repository.n5v5.secondary;

import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart;
import nikita.common.repository.n5v5.NoarkEntityRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ICorrespondencePartRepository extends
        NoarkEntityRepository<CorrespondencePart, String> {

    int deleteByOwnedBy(String ownedBy);
}
