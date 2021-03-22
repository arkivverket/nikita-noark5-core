package nikita.webapp.explore.mapsid;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Child
        implements Serializable {

    @Id
    @Type(type = "uuid-char")
    @Column(name = "code", insertable = false, updatable = false)
    UUID code;

    @OneToOne(fetch = LAZY)
    @MapsId
    @JoinColumn(name = "code")
    Parent parent;

    public UUID getCode() {
        return code;
    }

    public void setCode(UUID code) {
        this.code = code;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Child{" +
                "code='" + code + '\'' +
                '}';
    }
}
