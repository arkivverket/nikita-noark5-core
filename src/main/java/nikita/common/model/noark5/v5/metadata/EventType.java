package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_EVENT_TYPE;
import static nikita.common.config.Constants.TABLE_EVENT_TYPE;
import static nikita.common.config.N5ResourceMappings.EVENT_TYPE;

// Noark 5v5 hendelsetype
@Entity
@Table(name = TABLE_EVENT_TYPE)
public class EventType
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public EventType() {
    }

    public EventType(String code, String codename) {
        super(code, codename);
    }

    public EventType(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return EVENT_TYPE;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_EVENT_TYPE;
    }
}
