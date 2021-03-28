package nikita.webapp.explore.mapsid;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChildRepo
        extends CrudRepository<Child, UUID> {
}
