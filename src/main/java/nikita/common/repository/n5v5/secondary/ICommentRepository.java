package nikita.common.repository.n5v5.secondary;

import nikita.common.model.noark5.v5.secondary.Comment;
import nikita.common.repository.n5v5.NoarkEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ICommentRepository extends
        NoarkEntityRepository<Comment, UUID> {
}
