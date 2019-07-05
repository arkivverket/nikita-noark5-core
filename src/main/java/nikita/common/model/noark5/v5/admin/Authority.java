package nikita.common.model.noark5.v5.admin;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.AUTO;
import static nikita.common.config.Constants.TABLE_AUTHORITY;
import static nikita.common.config.Constants.TABLE_AUTHORITY_SEQ;

@Entity
@Table(name = TABLE_AUTHORITY)
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
    private List<User> users;

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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
