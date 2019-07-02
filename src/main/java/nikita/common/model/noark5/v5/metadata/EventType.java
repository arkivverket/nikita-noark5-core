package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.N5ResourceMappings;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_EVENT_TYPE;
import static nikita.common.config.N5ResourceMappings.EVENT_TYPE;

// Noark 5v5 hendelsetype
@Entity
@Table(name = TABLE_EVENT_TYPE)
public class EventType
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return EVENT_TYPE;
    }
}
