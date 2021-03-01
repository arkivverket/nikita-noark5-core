package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;

import static javax.persistence.InheritanceType.SINGLE_TABLE;
import static nikita.common.config.Constants.REL_METADATA_ASSOCIATED_WITH_RECORD_AS;
import static nikita.common.config.Constants.TABLE_ASSOCIATED_WITH_RECORD_AS;
import static nikita.common.config.N5ResourceMappings.ASSOCIATED_WITH_RECORD_AS;

// Noark 5v5 TilknyttetRegistreringSom
@Entity
@Inheritance(strategy = SINGLE_TABLE)
@Table(name = TABLE_ASSOCIATED_WITH_RECORD_AS)
public class AssociatedWithRecordAs
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public AssociatedWithRecordAs() {
    }

    public AssociatedWithRecordAs(String code, String codename) {
        super(code, codename);
    }

    public AssociatedWithRecordAs(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return ASSOCIATED_WITH_RECORD_AS;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_ASSOCIATED_WITH_RECORD_AS;
    }
}
