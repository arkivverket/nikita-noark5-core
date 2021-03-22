package nikita.webapp.explore.mapsid;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepo
        extends CrudRepository<Parent, String> {
}
