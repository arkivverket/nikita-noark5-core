package nikita.common.model.noark5.v5.casehandling.secondary;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ICorrespondencePartEntity;
import nikita.common.model.noark5.v5.metadata.CorrespondencePartType;
import nikita.common.util.deserialisers.casehandling.CorrespondencePartUnitDeserializer;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.FOREIGN_KEY_RECORD_PK;
import static nikita.common.config.Constants.NOARK_FONDS_STRUCTURE_PATH;
import static nikita.common.config.Constants.TABLE_CORRESPONDENCE_PART;

@Entity
@Table(name = TABLE_CORRESPONDENCE_PART)
@Inheritance(strategy = JOINED)
@JsonDeserialize(using = CorrespondencePartUnitDeserializer.class)
@Audited
public class CorrespondencePart
        extends SystemIdEntity
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

    // Link to Record
    @ManyToOne
    @JoinColumn(name = FOREIGN_KEY_RECORD_PK)
    private Record referenceRecord;

    @Override
    public CorrespondencePartType getCorrespondencePartType() {
        if (null == correspondencePartTypeCode)
            return null;
        return new CorrespondencePartType(correspondencePartTypeCode,
                                          correspondencePartTypeCodeName);
    }

    @Override
    public void setCorrespondencePartType(
            CorrespondencePartType correspondencePartType) {
        if (null != correspondencePartType) {
            this.correspondencePartTypeCode = correspondencePartType.getCode();
            this.correspondencePartTypeCodeName = correspondencePartType.getCodeName();
        } else {
            this.correspondencePartTypeCode = null;
            this.correspondencePartTypeCodeName = null;
        }
    }

    public Record getReferenceRecord() {
        return referenceRecord;
    }

    public void setReferenceRecord(Record referenceRecord) {
        this.referenceRecord = referenceRecord;
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
