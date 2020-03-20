package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_DOCUMENT_STATUS;
import static nikita.common.config.Constants.TABLE_DOCUMENT_STATUS;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_STATUS;

// Noark 5v5 dokumentstatus
@Entity
@Table(name = TABLE_DOCUMENT_STATUS)
public class DocumentStatus
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public DocumentStatus() {
    }

    public DocumentStatus(String code, String codename) {
        super(code, codename);
    }

    public DocumentStatus(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return DOCUMENT_STATUS;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_DOCUMENT_STATUS;
    }
}
