package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.N5ResourceMappings;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_SERIES_STATUS;
import static nikita.common.config.N5ResourceMappings.SERIES_STATUS;

// Noark 5v5 arkvdelstatus
@Entity
@Table(name = TABLE_SERIES_STATUS)
public class SeriesStatus
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return SERIES_STATUS;
    }
}
