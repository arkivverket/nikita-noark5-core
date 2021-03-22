package nikita.webapp.explore.mapsid;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.N5ResourceMappings.SYSTEM_ID_ENG;

@Entity
public class Parent
        implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {@Parameter(
                    name = "uuid_gen_strategy_class",
                    value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
    @Column(name = SYSTEM_ID_ENG, unique = true, updatable = false, nullable =
            false)
    @Type(type = "uuid-char")
    private UUID code;

    @OneToOne(mappedBy = "parent", fetch = LAZY, cascade = ALL)
    private Child child;

    public UUID getCode() {
        return code;
    }

    public void setCode(UUID code) {
        this.code = code;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
        child.setParent(this);
    }

    @Override
    public String toString() {
        return "Parent{" +
                "code='" + code + '\'' +
                '}';
    }
}
