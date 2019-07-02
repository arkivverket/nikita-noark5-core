package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.N5ResourceMappings;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_DOCUMENT_TYPE;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_TYPE;

// Noark 5v5 dokumenttype
@Entity
@Table(name = TABLE_DOCUMENT_TYPE)
public class DocumentType
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return DOCUMENT_TYPE;
    }
}
