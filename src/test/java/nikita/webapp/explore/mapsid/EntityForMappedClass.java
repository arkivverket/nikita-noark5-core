package nikita.webapp.explore.mapsid;

import org.springframework.data.annotation.CreatedBy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class EntityForMappedClass {

    @CreatedBy
    @Column(name = "owned_by")
    private String ownedBy;

    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
    }
}
