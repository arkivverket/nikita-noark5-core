package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_ACCESS_RESTRICTION;
import static nikita.common.config.Constants.TABLE_ACCESS_RESTRICTION;
import static nikita.common.config.N5ResourceMappings.ACCESS_RESTRICTION;

// Noark 5v5 Tilgangsrestriksjon
@Entity
@Table(name = TABLE_ACCESS_RESTRICTION)
public class AccessRestriction
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public AccessRestriction() {
    }

    public AccessRestriction(String code, String codename) {
        super(code, codename);
    }

    public AccessRestriction(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return ACCESS_RESTRICTION;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_ACCESS_RESTRICTION;
    }
}
