package nikita.webapp.explore.mapsid;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import java.io.Serializable;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Child
        extends MappedClassForChild
        implements Serializable {

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
}
