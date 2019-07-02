package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.N5ResourceMappings;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_REGISTRY_ENTRY_STATUS;
import static nikita.common.config.N5ResourceMappings.REGISTRY_ENTRY_STATUS;

// Noark 5v5 journalposttype
@Entity
@Table(name = TABLE_REGISTRY_ENTRY_STATUS)
public class RegistryEntryStatus
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return REGISTRY_ENTRY_STATUS;
    }
}
