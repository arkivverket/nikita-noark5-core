package nikita.webapp.explore.mapsid;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class MappedClassForChild
        extends EntityForMappedClass {

    @Id
    @Type(type = "uuid-char")
    @Column(name = "code", insertable = false, updatable = false,
            nullable = false)
    private UUID code;

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
