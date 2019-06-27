package nikita.common.model.noark5.v5.metadata;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.N5ResourceMappings.PART_ROLE;

// Noark 5v5 Partrolle
@Entity
@Table(name = "part_role")
@AttributeOverride(name = "id",
        column = @Column(name = "pk_part_role_id"))
public class PartyRole
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return PART_ROLE;
    }
}
