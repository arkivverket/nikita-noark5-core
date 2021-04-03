package nikita.common.model.noark5.v5.secondary;

import com.fasterxml.jackson.annotation.JsonProperty;
import nikita.common.model.noark5.bsm.BSMBase;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.NoarkGeneralEntity;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IPartEntity;
import nikita.common.model.noark5.v5.metadata.PartRole;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static nikita.common.config.Constants.NOARK_FONDS_STRUCTURE_PATH;
import static nikita.common.config.Constants.TABLE_PART;
import static nikita.common.config.N5ResourceMappings.*;

@Entity
@Table(name = TABLE_PART)
@Audited
public class Part
        extends NoarkGeneralEntity
        implements IPartEntity {

    /**
     * M??? - partRolle code (xs:string)
     */
    @NotNull
    @Column(name = PART_ROLE_CODE_ENG, nullable = false)
    @JsonProperty(PART_ROLE_CODE)
    @Audited
    private String partRoleCode;

    /**
     * M303 - partRolle code name (xs:string)
     */
    @Column(name = PART_ROLE_CODE_NAME_ENG)
    @JsonProperty(PART_ROLE_CODE_NAME)
    @Audited
    private String partRoleCodeName;

    /**
     * M302 - partNavn (xs:string)
     */
    @Column(name = CORRESPONDENCE_PART_NAME_ENG)
    @Audited
    @JsonProperty(CORRESPONDENCE_PART_NAME)
    private String name;

    // Links to Files
    @ManyToMany(mappedBy = "referencePart")
    private Set<File> referenceFile = new HashSet<>();

    // Links to Record
    @ManyToMany(mappedBy = "referencePart")
    private Set<Record> referenceRecord = new HashSet<>();

    // Links to DocumentDescriptions
    @ManyToMany(mappedBy = "referencePart")
    private Set<DocumentDescription> referenceDocumentDescription =
            new HashSet<>();

    // Links to businessSpecificMetadata (virksomhetsspesifikkeMetadata)
    @OneToMany(mappedBy = "referencePart", cascade = {PERSIST, MERGE})
    private List<BSMBase> referenceBSMBase = new ArrayList<>();

    public Set<File> getReferenceFile() {
        return referenceFile;
    }

    public void addFile(File file) {
        this.referenceFile.add(file);
        file.getReferencePart().add(this);
    }

    public void removeFile(File file) {
        referenceFile.remove(file);
        file.getReferencePart().remove(this);
    }

    public PartRole getPartRole() {
        if (null == partRoleCode)
            return null;
        return new PartRole(partRoleCode, partRoleCodeName);
    }

    public void setPartRole(PartRole partRole) {
        if (null != partRole) {
            this.partRoleCode = partRole.getCode();
            this.partRoleCodeName = partRole.getCodeName();
        } else {
            this.partRoleCode = null;
            this.partRoleCodeName = null;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Set<Record> getReferenceRecord() {
        return referenceRecord;
    }

    @Override
    public void addRecord(Record record) {
        this.referenceRecord.add(record);
        record.getReferencePart().add(this);
    }

    public void removeRecord(Record record) {
        referenceRecord.remove(record);
        record.getReferencePart().remove(this);
    }

    public Set<DocumentDescription> getReferenceDocumentDescription() {
        return referenceDocumentDescription;
    }

    public void addDocumentDescription(
            DocumentDescription documentDescription) {
        referenceDocumentDescription.add(documentDescription);
        documentDescription.getReferencePart().add(this);
    }

    public void removeDocumentDescription(
            DocumentDescription documentDescription) {
        referenceDocumentDescription.remove(documentDescription);
        documentDescription.getReferencePart().remove(this);
    }

    public List<BSMBase> getReferenceBSMBase() {
        return referenceBSMBase;
    }

    public void setReferenceBSMBase(List<BSMBase> referenceBSMBase) {
        this.referenceBSMBase = referenceBSMBase;
        for (BSMBase base : referenceBSMBase) {
            base.setReferencePart(this);
        }
    }

    public void addBSMBase(BSMBase bsmBase) {
        referenceBSMBase.add(bsmBase);
        bsmBase.setReferencePart(this);
    }

    public void removeBSMBase(BSMBase bsmBase) {
        referenceBSMBase.remove(bsmBase);
        bsmBase.setReferencePart(null);
    }

    @Override
    public String getBaseTypeName() {
        return PART;
    }

    @Override
    public String getFunctionalTypeName() {
        return NOARK_FONDS_STRUCTURE_PATH;
    }

    @Override
    public String toString() {
        return "Part{" + super.toString() +
                ", partRoleCode='" + partRoleCode + '\'' +
                ", partRoleCodeName='" + partRoleCodeName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Part)) return false;
        Part rhs = (Part) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(partRoleCode, rhs.partRoleCode)
                .append(partRoleCodeName, rhs.partRoleCodeName)
                .append(name, rhs.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(partRoleCode)
                .append(partRoleCodeName)
                .append(name)
                .toHashCode();
    }
}
