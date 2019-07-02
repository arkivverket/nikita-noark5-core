package nikita.common.repository.nikita;

import nikita.common.model.noark5.v5.admin.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository
        extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(String id);

    Optional<User> findBySystemId(UUID systemId);

    int deleteByUsername(String username);
}
