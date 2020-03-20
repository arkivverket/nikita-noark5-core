package nikita.common.model.noark5.v5.secondary;

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
import java.util.List;

import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.NOARK_FONDS_STRUCTURE_PATH;
import static nikita.common.config.Constants.TABLE_PART;
import static nikita.common.config.N5ResourceMappings.PART;

/**
 * Created by tsodring on 4/10/16.
 */

@Entity
@Table(name = TABLE_PART)
@Inheritance(strategy = JOINED)
@Audited
public class Part
        extends NoarkGeneralEntity
        implements IPartEntity {

    /**
     * M??? - partRolle code (xs:string)
     */
    @NotNull
    @Column(name = "part_role_code", nullable = false)
    @Audited
    private String partRoleCode;

    /**
     * M303 - partRolle code name (xs:string)
     */
    @Column(name = "part_role_code_name")
    @Audited
    private String partRoleCodeName;

    // Links to Files
    @ManyToMany(mappedBy = "referencePart")
    private List<File> referenceFile = new ArrayList<>();

    // Links to Record
    @ManyToMany(mappedBy = "referencePart")
    private List<Record> referenceRecord = new ArrayList<>();

    // Links to DocumentDescriptions
    @ManyToMany(mappedBy = "referencePart")
    private List<DocumentDescription> referenceDocumentDescription =
            new ArrayList<>();

    public List<File> getReferenceFile() {
        return referenceFile;
    }

    public void setReferenceFile(List<File> referenceFile) {
        this.referenceFile = referenceFile;
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

    public void addReferenceFile(File file) {
        this.referenceFile.add(file);
    }

    public List<Record> getReferenceRecord() {
        return referenceRecord;
    }

    public void setReferenceRecord(List<Record> referenceRecord) {
        this.referenceRecord = referenceRecord;
    }

    public void addReferenceRecord(Record record) {
        this.referenceRecord.add(record);
    }

    public List<DocumentDescription> getReferenceDocumentDescription() {
        return referenceDocumentDescription;
    }

    public void setReferenceDocumentDescription(
            List<DocumentDescription> referenceDocumentDescription) {
        this.referenceDocumentDescription = referenceDocumentDescription;
    }

    public void addReferenceDocumentDescription(
            DocumentDescription documentDescription) {
        this.referenceDocumentDescription.add(documentDescription);
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
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(partRoleCode)
                .append(partRoleCodeName)
                .toHashCode();
    }
}
