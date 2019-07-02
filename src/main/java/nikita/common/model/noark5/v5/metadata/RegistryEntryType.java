package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.N5ResourceMappings;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_REGISTRY_ENTRY_TYPE;
import static nikita.common.config.N5ResourceMappings.REGISTRY_ENTRY_TYPE;

// Noark 5v5 journaltype
@Entity
@Table(name = TABLE_REGISTRY_ENTRY_TYPE)
public class RegistryEntryType
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return REGISTRY_ENTRY_TYPE;
    }
}