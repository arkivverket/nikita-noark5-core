package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_PRECEDENCE_STATUS;
import static nikita.common.config.Constants.TABLE_PRECEDENCE_STATUS;
import static nikita.common.config.N5ResourceMappings.PRECEDENCE_STATUS;

// Noark 5v5 Presedensstatus
@Entity
@Table(name = TABLE_PRECEDENCE_STATUS)
public class PrecedenceStatus
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public PrecedenceStatus() {
    }

    public PrecedenceStatus(String code, String codename) {
        super(code, codename);
    }

    public PrecedenceStatus(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return PRECEDENCE_STATUS;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_PRECEDENCE_STATUS;
    }
}
