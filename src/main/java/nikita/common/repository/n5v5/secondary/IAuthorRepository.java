package nikita.common.repository.n5v5.secondary;

import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.secondary.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface IAuthorRepository
        extends CrudRepository<Author, INoarkEntity> {
    // systemId
    Set<Author> findByAuthor(String author);
}
