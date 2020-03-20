package nikita.common.model.noark5.v5.metadata;

import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_CORRESPONDENCE_PART_TYPE;
import static nikita.common.config.Constants.TABLE_CORRESPONDENCE_PART_TYPE;
import static nikita.common.config.N5ResourceMappings.CORRESPONDENCE_PART_TYPE;

// Noark 5v5 korrespondanseparttype
@Entity
@Table(name = TABLE_CORRESPONDENCE_PART_TYPE)
@Audited
public class CorrespondencePartType
        extends Metadata {

    private static final long serialVersionUID = 1L;

    public CorrespondencePartType() {
    }

    public CorrespondencePartType(String code, String codename) {
        super(code, codename);
    }

    public CorrespondencePartType(String code) {
        super(code, (String)null);
    }

    @Override
    public String getBaseTypeName() {
        return CORRESPONDENCE_PART_TYPE;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_CORRESPONDENCE_PART_TYPE;
    }
}
