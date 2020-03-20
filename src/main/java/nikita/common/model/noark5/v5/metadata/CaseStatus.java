package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_CASE_STATUS;
import static nikita.common.config.Constants.TABLE_CASE_STATUS;
import static nikita.common.config.N5ResourceMappings.CASE_STATUS;

// Noark 5v5 saksstatus
@Entity
@Table(name = TABLE_CASE_STATUS)
public class CaseStatus
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public CaseStatus() {
    }

    public CaseStatus(String code, String codename) {
        super(code, codename);
    }

    public CaseStatus(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return CASE_STATUS;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_CASE_STATUS;
    }
}
