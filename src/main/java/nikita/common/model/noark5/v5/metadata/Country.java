package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_COUNTRY;
import static nikita.common.config.Constants.TABLE_COUNTRY;
import static nikita.common.config.N5ResourceMappings.COUNTRY;

// Noark 5v5 Land
@Entity
@Table(name = TABLE_COUNTRY)
public class Country extends Metadata {

    private static final long serialVersionUID = 1L;

    public Country() {
    }

    public Country(String code, String codename) {
        super(code, codename);
    }

    public Country(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return COUNTRY;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_COUNTRY;
    }
}
