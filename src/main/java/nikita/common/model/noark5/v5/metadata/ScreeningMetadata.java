package nikita.common.model.noark5.v5.metadata;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_SCREENING_METADATA;
import static nikita.common.config.Constants.TABLE_SCREENING_METADATA;
import static nikita.common.config.N5ResourceMappings.SCREENING_METADATA;

// Noark 5v5 skjermingmetadata
@Entity
@Table(name = TABLE_SCREENING_METADATA)
public class ScreeningMetadata
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;

    @Override
    public String getBaseTypeName() {
        return SCREENING_METADATA;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_SCREENING_METADATA;
    }
}
