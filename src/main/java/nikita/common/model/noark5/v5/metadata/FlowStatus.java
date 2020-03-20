package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_FLOW_STATUS;
import static nikita.common.config.Constants.TABLE_FLOW_STATUS;
import static nikita.common.config.N5ResourceMappings.FLOW_STATUS;

// Noark 5v5 Flytstatus
@Entity
@Table(name = TABLE_FLOW_STATUS)
public class FlowStatus
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public FlowStatus() {
    }

    public FlowStatus(String code, String codename) {
        super(code, codename);
    }

    public FlowStatus(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return FLOW_STATUS;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_FLOW_STATUS;
    }
}
