package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.N5ResourceMappings;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_SCREENING_DOCUMENT;
import static nikita.common.config.N5ResourceMappings.SCREENING_DOCUMENT;

// Noark 5v5 Skjermingdokument
@Entity
@Table(name = TABLE_SCREENING_DOCUMENT)
public class ScreeningDocument
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return SCREENING_DOCUMENT;
    }
}
