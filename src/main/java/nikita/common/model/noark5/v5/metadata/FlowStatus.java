package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.N5ResourceMappings;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_FLOW_STATUS;
import static nikita.common.config.N5ResourceMappings.FLOW_STATUS;

// Noark 5v5 Flytstatus
@Entity
@Table(name = TABLE_FLOW_STATUS)
public class FlowStatus
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return FLOW_STATUS;
    }
}
