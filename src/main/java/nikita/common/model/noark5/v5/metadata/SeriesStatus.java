package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_SERIES_STATUS;
import static nikita.common.config.Constants.TABLE_SERIES_STATUS;
import static nikita.common.config.N5ResourceMappings.SERIES_STATUS;

// Noark 5v5 arkvdelstatus
@Entity
@Table(name = TABLE_SERIES_STATUS)
public class SeriesStatus
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public SeriesStatus() {
    }

    public SeriesStatus(String code, String codename) {
        super(code, codename);
    }

    public SeriesStatus(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return SERIES_STATUS;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_SERIES_STATUS;
    }
}
