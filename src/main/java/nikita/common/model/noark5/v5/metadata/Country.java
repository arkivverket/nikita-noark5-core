package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.N5ResourceMappings;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_COUNTRY;
import static nikita.common.config.N5ResourceMappings.COUNTRY;

// Noark 5v5 Land
@Entity
@Table(name = TABLE_COUNTRY)
public class Country extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return COUNTRY;
    }
}