package nikita.common.model.noark5.v5.metadata;


import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_FONDS_STATUS;
import static nikita.common.config.Constants.TABLE_FONDS_STATUS;
import static nikita.common.config.N5ResourceMappings.FONDS_STATUS;

// Noark 5v5 arkivstatus
@Entity
@Table(name = TABLE_FONDS_STATUS)
public class FondsStatus
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public FondsStatus() {
    }

    public FondsStatus(String code, String codename) {
        super(code, codename);
    }

    public FondsStatus(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return FONDS_STATUS;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_FONDS_STATUS;
    }
}
