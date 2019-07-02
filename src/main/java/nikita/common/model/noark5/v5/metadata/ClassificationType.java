package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.N5ResourceMappings;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_CLASSIFICATION_TYPE;
import static nikita.common.config.N5ResourceMappings.CLASSIFICATION_TYPE;

// Noark 5v5 Klassifikasjonstype
@Entity
@Table(name = TABLE_CLASSIFICATION_TYPE)
public class ClassificationType
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return CLASSIFICATION_TYPE;
    }
}
