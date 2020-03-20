package nikita.common.model.noark5.v5.metadata;

import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_PART_ROLE;
import static nikita.common.config.Constants.TABLE_PART_ROLE;
import static nikita.common.config.N5ResourceMappings.PART_ROLE;

// Noark 5v5 Partrolle
@Entity
@Table(name = TABLE_PART_ROLE)
@Audited
public class PartRole
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public PartRole() {
    }

    public PartRole(String code, String codename) {
        super(code, codename);
    }

    public PartRole(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return PART_ROLE;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_PART_ROLE;
    }

}
