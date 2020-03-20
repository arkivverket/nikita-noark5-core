package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_DOCUMENT_TYPE;
import static nikita.common.config.Constants.TABLE_DOCUMENT_TYPE;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_TYPE;

// Noark 5v5 dokumenttype
@Entity
@Table(name = TABLE_DOCUMENT_TYPE)
public class DocumentType
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public DocumentType() {
    }

    public DocumentType(String code, String codename) {
        super(code, codename);
    }

    public DocumentType(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return DOCUMENT_TYPE;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_DOCUMENT_TYPE;
    }
}
