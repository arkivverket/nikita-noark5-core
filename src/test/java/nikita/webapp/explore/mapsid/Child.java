package nikita.webapp.explore.mapsid;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Child
        implements Serializable {

    @Id
    @Column(name = "code", insertable = false, updatable = false)
    String code;

    @OneToOne
    @MapsId
    @JoinColumn(name = "code")
    Parent parent;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
