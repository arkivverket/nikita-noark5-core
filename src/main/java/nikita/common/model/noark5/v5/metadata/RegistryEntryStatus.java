package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_REGISTRY_ENTRY_STATUS;
import static nikita.common.config.Constants.TABLE_REGISTRY_ENTRY_STATUS;
import static nikita.common.config.N5ResourceMappings.REGISTRY_ENTRY_STATUS;

// Noark 5v5 journalposttype
@Entity
@Table(name = TABLE_REGISTRY_ENTRY_STATUS)
public class RegistryEntryStatus
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public RegistryEntryStatus() {
    }

    public RegistryEntryStatus(String code, String codename) {
        super(code, codename);
    }

    public RegistryEntryStatus(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return REGISTRY_ENTRY_STATUS;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_REGISTRY_ENTRY_STATUS;
    }
}
