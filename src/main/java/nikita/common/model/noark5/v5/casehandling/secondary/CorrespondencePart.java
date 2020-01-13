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

    // Links to Record
    @ManyToMany(mappedBy = "referenceCorrespondencePart")
    private List<Record> referenceRecord = new ArrayList<>();

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

    public List<Record> getReferenceRecord() {
        return referenceRecord;
    }

    public void setReferenceRecord(List<Record> referenceRecord) {
        this.referenceRecord = referenceRecord;
    }

    public void addRecord(Record referenceRecord) {
        this.referenceRecord.add(referenceRecord);
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
