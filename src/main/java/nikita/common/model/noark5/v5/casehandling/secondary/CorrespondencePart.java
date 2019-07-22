package nikita.common.model.noark5.v5.casehandling.secondary;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ICorrespondencePartEntity;
import nikita.common.model.noark5.v5.metadata.CorrespondencePartType;
import nikita.common.util.deserialisers.casehandling.CorrespondencePartUnitDeserializer;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.NOARK_FONDS_STRUCTURE_PATH;
import static nikita.common.config.Constants.TABLE_CORRESPONDENCE_PART;

@Entity
@Table(name = TABLE_CORRESPONDENCE_PART)
@Inheritance(strategy = JOINED)
@JsonDeserialize(using = CorrespondencePartUnitDeserializer.class)
@Audited
public class CorrespondencePart
        extends NoarkEntity
        implements ICorrespondencePartEntity {

    /**
     * M??? - korrespondansepartTypeKode kode (xs:string)
     */
    @NotNull
    @Column(name = "correspondence_part_type_code", nullable = false)
    @Audited
    private String correspondencePartTypeCode;

    /**
     * M??? - korrespondansepartTypeKodenavn name (xs:string)
     */
    @Column(name = "correspondence_part_type_code_name")
    @Audited
    private String correspondencePartTypeCodeName;

    private CorrespondencePartType correspondencePartType;

    @Override
    public String getCorrespondencePartTypeCode() {
        return correspondencePartTypeCode;
    }

    @Override
    public void setCorrespondencePartTypeCode(String correspondencePartTypeCode) {
        this.correspondencePartTypeCode = correspondencePartTypeCode;
    }

    @Override
    public String getCorrespondencePartTypeCodeName() {
        return correspondencePartTypeCodeName;
    }

    @Override
    public void setCorrespondencePartTypeCodeName(
            String correspondencePartTypeCodeName) {
        this.correspondencePartTypeCodeName = correspondencePartTypeCodeName;
    }

    @Override
    public CorrespondencePartType getCorrespondencePartType() {
        return correspondencePartType;
    }

    @Override
    public void setCorrespondencePartType(
            CorrespondencePartType correspondencePartType) {
        this.correspondencePartType = correspondencePartType;
    }

    @Override
    public String getFunctionalTypeName() {
        return NOARK_FONDS_STRUCTURE_PATH;
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
