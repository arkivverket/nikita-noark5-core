package nikita.common.model.noark5.v5.admin;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.AUTO;
import static nikita.common.config.Constants.TABLE_AUTHORITY;
import static nikita.common.config.Constants.TABLE_AUTHORITY_SEQ;

@Entity
@Table(name = TABLE_AUTHORITY)
@Indexed
public class Authority
        implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = AUTO,
            generator = "authority_seq")
    @SequenceGenerator(name = TABLE_AUTHORITY_SEQ,
            sequenceName = "authority_seq", allocationSize = 1)
    private Long id;

    @Column(name = "authority_name", unique = true)
    @NotNull
    @Enumerated(STRING)
    private AuthorityName authorityName;

    @ManyToMany(mappedBy = "authorities")
    private Set<User> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuthorityName getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(AuthorityName authorityName) {
        this.authorityName = authorityName;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.getAuthorities().remove(this);
    }
}
