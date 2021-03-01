package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;

import static javax.persistence.InheritanceType.SINGLE_TABLE;
import static nikita.common.config.Constants.REL_METADATA_VARIANT_FORMAT;
import static nikita.common.config.Constants.TABLE_VARIANT_FORMAT;
import static nikita.common.config.N5ResourceMappings.VARIANT_FORMAT;

// Noark 5v5 variantformat
@Entity
@Inheritance(strategy = SINGLE_TABLE)
@Table(name = TABLE_VARIANT_FORMAT)
public class VariantFormat
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public VariantFormat() {
    }

    public VariantFormat(String code, String codename) {
        super(code, codename);
    }

    public VariantFormat(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return VARIANT_FORMAT;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_VARIANT_FORMAT;
    }
}
