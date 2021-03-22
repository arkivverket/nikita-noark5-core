package nikita.webapp.explore.mapsid;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Child
        extends EntityForMappedClass
        implements Serializable {

    @Id
    @Type(type = "uuid-char")
    @Column(name = "code", insertable = false, updatable = false,
            nullable = false)
    protected UUID code;

    @OneToOne(fetch = LAZY)
    @MapsId
    @JoinColumn(name = "code")
    private Parent parent;

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public UUID getCode() {
        return code;
    }

    public void setCode(UUID code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Child{" +
                "code='" + code + '\'' +
                '}';
    }
}
