package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_ASSOCIATED_WITH_RECORD_AS;
import static nikita.common.config.Constants.TABLE_ASSOCIATED_WITH_RECORD_AS;
import static nikita.common.config.N5ResourceMappings.ASSOCIATED_WITH_RECORD_AS;

// Noark 5v5 TilknyttetRegistreringSom
@Entity
@Table(name = TABLE_ASSOCIATED_WITH_RECORD_AS)
public class AssociatedWithRecordAs
        extends Metadata {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return ASSOCIATED_WITH_RECORD_AS;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_ASSOCIATED_WITH_RECORD_AS;
    }
}
