package nikita.webapp.explore.mapsid;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.io.Serializable;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
public class Parent
        implements Serializable {

    @Id
    @Column(name = "code")
    String code;

    @OneToOne(mappedBy = "parent", fetch = LAZY, cascade = ALL)
    Child child;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
