package nikita.common.repository.n5v5;

import nikita.common.model.noark5.v5.Part;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface IPartRepository extends
        NoarkEntityRepository<Part, UUID> {

}
