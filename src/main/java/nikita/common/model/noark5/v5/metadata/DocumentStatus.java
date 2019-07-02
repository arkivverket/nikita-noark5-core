package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.N5ResourceMappings;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_DOCUMENT_STATUS;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_STATUS;

// Noark 5v5 dokumentstatus
@Entity
@Table(name = TABLE_DOCUMENT_STATUS)
public class DocumentStatus
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return DOCUMENT_STATUS;
    }
}
