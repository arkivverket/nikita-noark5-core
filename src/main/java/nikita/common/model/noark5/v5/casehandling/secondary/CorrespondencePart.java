package nikita.common.model.noark5.v5.casehandling.secondary;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.bsm.BSMBase;
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

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.*;

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

    // Links to businessSpecificMetadata (virksomhetsspesifikkeMetadata)
    @OneToMany(mappedBy = "referenceCorrespondencePart",
            cascade = {PERSIST, MERGE})
    private List<BSMBase> referenceBSMBase = new ArrayList<>();

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

    public List<BSMBase> getReferenceBSMBase() {
        return referenceBSMBase;
    }

    public void setReferenceBSMBase(List<BSMBase> referenceBSMBase) {
        this.referenceBSMBase = referenceBSMBase;
        for (BSMBase bsmBase : referenceBSMBase) {
            bsmBase.setReferenceCorrespondencePart(this);
        }
    }

    public void addBSMBaseList(List<BSMBase> referenceBSMBase) {
        this.referenceBSMBase.addAll(referenceBSMBase);
        for (BSMBase bsm : referenceBSMBase) {
            bsm.setReferenceCorrespondencePart(this);
        }
    }

    public void addBSMBase(BSMBase bsmBase) {
        this.referenceBSMBase.add(bsmBase);
        bsmBase.setReferenceCorrespondencePart(this);
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
