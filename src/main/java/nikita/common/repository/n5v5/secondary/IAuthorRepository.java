package nikita.common.repository.n5v5.secondary;

import nikita.common.model.noark5.v5.secondary.Author;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IAuthorRepository
        extends PagingAndSortingRepository<Author, UUID> {

    Author findBySystemId(UUID systemId);
}
