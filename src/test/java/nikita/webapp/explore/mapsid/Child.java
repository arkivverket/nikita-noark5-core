package nikita.webapp.explore.mapsid;

import javax.persistence.*;

@Entity
public class Child {

    @Id
    @Column(name = "code")
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
}
