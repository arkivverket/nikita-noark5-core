package nikita.common.repository.nikita;


import nikita.common.model.noark5.v5.admin.Authority;
import nikita.common.model.noark5.v5.admin.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository
        extends JpaRepository<Authority, String> {
    Authority findByAuthorityName(AuthorityName authorityName);
}
