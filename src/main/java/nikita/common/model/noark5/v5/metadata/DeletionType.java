package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_DELETION_TYPE;
import static nikita.common.config.Constants.TABLE_DELETION_TYPE;
import static nikita.common.config.N5ResourceMappings.DELETION_TYPE;

// Noark 5v5 Slettingstype
@Entity
@Table(name = TABLE_DELETION_TYPE)
public class DeletionType
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public DeletionType() {
    }

    public DeletionType(String code, String codename) {
        super(code, codename);
    }

    public DeletionType(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return DELETION_TYPE;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_DELETION_TYPE;
    }

}
