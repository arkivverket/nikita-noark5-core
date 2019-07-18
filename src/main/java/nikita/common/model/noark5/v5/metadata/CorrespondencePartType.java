package nikita.common.model.noark5.v5.metadata;

import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

import static nikita.common.config.Constants.REL_METADATA_CORRESPONDENCE_PART_TYPE;
import static nikita.common.config.Constants.TABLE_CORRESPONDENCE_PART_TYPE;
import static nikita.common.config.N5ResourceMappings.CORRESPONDENCE_PART_TYPE;

// Noark 5v5 korrespondanseparttype
@Entity
@Table(name = TABLE_CORRESPONDENCE_PART_TYPE)
@Audited
public class CorrespondencePartType
        extends MetadataSuperClass {

    private static final long serialVersionUID = 1L;
    // Link to CorrespondencePart
    // TODO :Revisit this. You don't need to be able to pick up the reverse of this
    // relationship. It will be massive!
    @OneToMany(mappedBy = "referenceCorrespondencePartType")
    private List<CorrespondencePart> referenceCorrespondencePart;

    public List<CorrespondencePart> getReferenceCorrespondencePart() {
        return referenceCorrespondencePart;
    }

    public void setReferenceCorrespondencePart(
            List<CorrespondencePart> referenceCorrespondencePart) {
        this.referenceCorrespondencePart = referenceCorrespondencePart;
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
