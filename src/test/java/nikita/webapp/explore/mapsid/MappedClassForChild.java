package nikita.webapp.explore.mapsid;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class MappedClassForChild {

    @Id
    @Type(type = "uuid-char")
    @Column(name = "code", insertable = false, updatable = false,
            nullable = false)
    protected UUID code;

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
