package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_CLASSIFICATION_TYPE;
import static nikita.common.config.Constants.TABLE_CLASSIFICATION_TYPE;
import static nikita.common.config.N5ResourceMappings.CLASSIFICATION_TYPE;

// Noark 5v5 Klassifikasjonstype
@Entity
@Table(name = TABLE_CLASSIFICATION_TYPE)
public class ClassificationType
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public ClassificationType() {
    }

    public ClassificationType(String code, String codename) {
        super(code, codename);
    }

    public ClassificationType(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return CLASSIFICATION_TYPE;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_CLASSIFICATION_TYPE;
    }
}
