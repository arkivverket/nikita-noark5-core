package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_DOCUMENT_MEDIUM;
import static nikita.common.config.Constants.TABLE_DOCUMENT_MEDIUM;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_MEDIUM;

// Noark 5v5 dokumentmedium
@Entity
@Table(name = TABLE_DOCUMENT_MEDIUM)
public class DocumentMedium
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public DocumentMedium() {
    }

    public DocumentMedium(String code, String codename) {
        super(code, codename);
    }

    public DocumentMedium(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return DOCUMENT_MEDIUM;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_DOCUMENT_MEDIUM;
    }
}
