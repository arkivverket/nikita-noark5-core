package nikita.common.model.noark5.v5.casehandling.secondary;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.casehandling.ICorrespondencePartEntity;
import nikita.common.model.noark5.v5.metadata.CorrespondencePartType;
import nikita.common.util.deserialisers.casehandling.CorrespondencePartUnitDeserializer;
import org.hibernate.envers.Audited;

import javax.persistence.*;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.*;

@Entity
@Table(name = TABLE_CORRESPONDENCE_PART)
@Inheritance(strategy = JOINED)
@JsonDeserialize(using = CorrespondencePartUnitDeserializer.class)
@Audited
public class CorrespondencePart
        extends NoarkEntity
        implements ICorrespondencePartEntity {

    /**
     * M087 - korrespondanseparttype (xs:string)
     */
    // Link to CorrespondencePartType
    @ManyToOne(fetch = EAGER, cascade = PERSIST)
    @JoinColumn(name = CORRESPONDENCE_PART_CORRESPONDENCE_PART_TYPE_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private CorrespondencePartType referenceCorrespondencePartType;

    @Override
    public CorrespondencePartType getCorrespondencePartType() {
        return referenceCorrespondencePartType;
    }

    @Override
    public void setCorrespondencePartType(
            CorrespondencePartType referenceCorrespondencePartType) {
        this.referenceCorrespondencePartType = referenceCorrespondencePartType;
    }

    @Override
    public String getFunctionalTypeName() {
        return Constants.NOARK_CASE_HANDLING_PATH;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
