package nikita.common.model.noark5.v5.metadata;

import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_DOCUMENT_MEDIUM;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_MEDIUM;

// Noark 5v5 dokumentmedium
@Entity
@Table(name = TABLE_DOCUMENT_MEDIUM)
public class DocumentMedium
        extends MetadataSuperClass
        implements INikitaEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return DOCUMENT_MEDIUM;
    }
}
