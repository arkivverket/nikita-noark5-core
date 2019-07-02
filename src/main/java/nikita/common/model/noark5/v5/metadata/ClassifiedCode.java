package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.N5ResourceMappings;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_CLASSIFIED_CODE;
import static nikita.common.config.N5ResourceMappings.CLASSIFIED_CODE;

// Noark 5v5 graderingskode
@Entity
@Table(name = TABLE_CLASSIFIED_CODE)
public class ClassifiedCode
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return CLASSIFIED_CODE;
    }
}