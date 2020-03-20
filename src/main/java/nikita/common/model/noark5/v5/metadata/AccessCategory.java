package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_ACCESS_CATEGORY;
import static nikita.common.config.Constants.TABLE_ACCESS_CATEGORY;
import static nikita.common.config.N5ResourceMappings.ACCESS_CATEGORY;

// Noark 5v5 Tilgangskategori
@Entity
@Table(name = TABLE_ACCESS_CATEGORY)
public class AccessCategory
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public AccessCategory() {
    }

    public AccessCategory(String code, String codename) {
        super(code, codename);
    }

    public AccessCategory(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return ACCESS_CATEGORY;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_ACCESS_CATEGORY;
    }
}
