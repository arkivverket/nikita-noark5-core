package nikita.common.model.noark5.v5.interfaces.entities.admin;

import nikita.common.model.noark5.v5.admin.Authority;
import nikita.common.model.noark5.v5.interfaces.entities.ICreate;
import nikita.common.model.noark5.v5.interfaces.entities.IFinalise;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

import java.time.OffsetDateTime;
import java.util.Set;

public interface IUserEntity
        extends INoarkEntity, ICreate, IFinalise {
    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    String getFirstname();

    void setFirstname(String firstname);

    String getLastname();

    void setLastname(String lastname);

    Boolean getEnabled();

    void setEnabled(Boolean enabled);
    boolean isAccountNonExpired();
    void setAccountNonExpired(boolean accountNonExpired);
    boolean isCredentialsNonExpired();
    void setCredentialsNonExpired(boolean credentialsNonExpired);
    boolean isAccountNonLocked();

    void setAccountNonLocked(boolean accountNonLocked);

    Set<Authority> getAuthorities();

    OffsetDateTime getLastPasswordResetDate();
    void setLastPasswordResetDate(OffsetDateTime lastPasswordResetDate);
}
