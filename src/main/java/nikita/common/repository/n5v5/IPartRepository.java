package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.Part;
import org.springframework.stereotype.Repository;


@Repository
public interface IPartRepository extends
        NoarkEntityRepository<Part, Long> {

}
