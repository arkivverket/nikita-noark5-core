package nikita.common.model.noark5.v5.metadata;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_PART_ROLE;
import static nikita.common.config.N5ResourceMappings.PART_ROLE;

// Noark 5v5 Partrolle
@Entity
@Table(name = TABLE_PART_ROLE)
public class PartRole
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return PART_ROLE;
    }
}
