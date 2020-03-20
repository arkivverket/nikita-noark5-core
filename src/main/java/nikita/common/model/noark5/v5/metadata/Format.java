package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_FORMAT;
import static nikita.common.config.Constants.TABLE_FORMAT;
import static nikita.common.config.N5ResourceMappings.FORMAT;

// Noark 5v5 format
@Entity
@Table(name = TABLE_FORMAT)
public class Format
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public Format() {
    }

    public Format(String code, String codename) {
        super(code, codename);
    }

    public Format(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return FORMAT;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_FORMAT;
    }
}
