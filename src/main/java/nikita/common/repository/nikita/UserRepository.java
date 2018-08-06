package nikita.common.repository.nikita;

import nikita.common.model.noark5.v4.admin.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository
        extends JpaRepository<User, String> {
    User findByUsername(String username);

    Optional<User> findById(String id);
}
