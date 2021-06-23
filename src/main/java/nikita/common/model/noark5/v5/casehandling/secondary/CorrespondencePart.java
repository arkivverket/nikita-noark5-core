package nikita.common.model.noark5.v5.casehandling.secondary;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.bsm.BSMBase;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ICorrespondencePartEntity;
import nikita.common.model.noark5.v5.metadata.CorrespondencePartType;
import nikita.common.util.deserialisers.casehandling.CorrespondencePartUnitDeserializer;
import nikita.webapp.hateoas.casehandling.CorrespondencePartHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.hibernate.envers.Audited;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

@Entity
@Table(name = TABLE_CORRESPONDENCE_PART)
@JsonDeserialize(using = CorrespondencePartUnitDeserializer.class)
@HateoasPacker(using = CorrespondencePartHateoasHandler.class)
@HateoasObject(using = CorrespondencePartHateoas.class)
@Audited
@Indexed
public class CorrespondencePart
        extends SystemIdEntity
        implements ICorrespondencePartEntity {

    /**
     * M??? - korrespondansepartTypeKode kode (xs:string)
     */
    @NotNull
    @Column(name = CORRESPONDENCE_PART_TYPE_CODE, nullable = false)
    @Audited
    private String correspondencePartTypeCode;

    /**
     * M??? - korrespondansepartTypeKodenavn name (xs:string)
     */
    @Column(name = CORRESPONDENCE_PART_TYPE_CODE_NAME)
    @Audited
    private String correspondencePartTypeCodeName;

    // Link to Record
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = FOREIGN_KEY_RECORD_PK)
    private Record referenceRecord;

    // Links to businessSpecificMetadata (virksomhetsspesifikkeMetadata)
    @OneToMany(mappedBy = REFERENCE_CORRESPONDENCE_PART,
            cascade = {PERSIST, MERGE, REMOVE})
    private final List<BSMBase> referenceBSMBase = new ArrayList<>();

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

    @Override
    public List<BSMBase> getReferenceBSMBase() {
        return referenceBSMBase;
    }

    @Override
    public void addReferenceBSMBase(List<BSMBase> bSMBase) {
        this.referenceBSMBase.addAll(bSMBase);
        for (BSMBase bsm : referenceBSMBase) {
            bsm.setReferenceCorrespondencePart(this);
        }
    }

    @Override
    public void addBSMBase(BSMBase bsmBase) {
        this.referenceBSMBase.add(bsmBase);
        bsmBase.setReferenceCorrespondencePart(this);
    }

    @Override
    public void removeBSMBase(BSMBase bsmBase) {
        this.referenceBSMBase.remove(bsmBase);
        bsmBase.setReferenceCorrespondencePart(null);
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
