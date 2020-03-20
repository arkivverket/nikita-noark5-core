package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_REGISTRY_ENTRY_TYPE;
import static nikita.common.config.Constants.TABLE_REGISTRY_ENTRY_TYPE;
import static nikita.common.config.N5ResourceMappings.REGISTRY_ENTRY_TYPE;

// Noark 5v5 journaltype
@Entity
@Table(name = TABLE_REGISTRY_ENTRY_TYPE)
public class RegistryEntryType
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public RegistryEntryType() {
    }

    public RegistryEntryType(String code, String codename) {
        super(code, codename);
    }

    public RegistryEntryType(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return REGISTRY_ENTRY_TYPE;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_REGISTRY_ENTRY_TYPE;
    }
}
