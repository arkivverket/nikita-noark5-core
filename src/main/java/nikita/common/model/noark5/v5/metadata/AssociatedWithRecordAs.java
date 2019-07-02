package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.N5ResourceMappings;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_ASSOCIATED_WITH_RECORD_AS;
import static nikita.common.config.N5ResourceMappings.ASSOCIATED_WITH_RECORD_AS;

// Noark 5v5 Tilknyttet registrering som
@Entity
@Table(name = TABLE_ASSOCIATED_WITH_RECORD_AS)
public class AssociatedWithRecordAs
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return ASSOCIATED_WITH_RECORD_AS;
    }
}
