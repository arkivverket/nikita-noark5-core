package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.N5ResourceMappings;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_VARIANT_FORMAT;
import static nikita.common.config.N5ResourceMappings.VARIANT_FORMAT;

// Noark 5v5 variantformat
@Entity
@Table(name = TABLE_VARIANT_FORMAT)
public class VariantFormat
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return VARIANT_FORMAT;
    }
}
