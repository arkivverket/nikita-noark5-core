package nikita.common.repository.nikita;

import nikita.common.model.noark5.v5.admin.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository
        extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(String id);

    Optional<User> findBySystemId(String systemId);

    int deleteByUsername(String username);
}
