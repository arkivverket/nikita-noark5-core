package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_COORDINATE_SYSTEM;
import static nikita.common.config.Constants.TABLE_COORDINATE_SYSTEM;
import static nikita.common.config.N5ResourceMappings.COORDINATE_SYSTEM;

// Noark 5v5 Koordinatsystem
@Entity
@Table(name = TABLE_COORDINATE_SYSTEM)
public class CoordinateSystem extends Metadata {

    private static final long serialVersionUID = 1L;

    public CoordinateSystem() {
    }

    public CoordinateSystem(String code, String codename) {
        super(code, codename);
    }

    public CoordinateSystem(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return COORDINATE_SYSTEM;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_COORDINATE_SYSTEM;
    }
}
