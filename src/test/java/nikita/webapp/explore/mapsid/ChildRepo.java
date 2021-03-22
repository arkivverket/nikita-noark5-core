package nikita.webapp.explore.mapsid;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildRepo
        extends CrudRepository<Parent, String> {
}
