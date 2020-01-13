package nikita.common.model.noark5.v5;

import nikita.common.model.noark5.v5.interfaces.entities.secondary.IPartEntity;
import nikita.common.model.noark5.v5.metadata.PartRole;
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
     * M??? - partTypeKode kode (xs:string)
     */
    @NotNull
    @Column(name = "part_type_code", nullable = false)
    @Audited
    private String partTypeCode;

    /**
     * M??? - partTypeKodenavn code name (xs:string)
     */
    @Column(name = "correspondence_part_type_code_name")
    @Audited
    private String partTypeCodeName;

    private PartRole referencePartRole;

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

    public String getPartTypeCode() {
        return partTypeCode;
    }

    public void setPartTypeCode(String partTypeCode) {
        this.partTypeCode = partTypeCode;
    }

    public String getPartTypeCodeName() {
        return partTypeCodeName;
    }

    public void setPartTypeCodeName(String partTypeCodeName) {
        this.partTypeCodeName = partTypeCodeName;
    }

    @Override
    public PartRole getPartRole() {
        return referencePartRole;
    }

    @Override
    public void setPartRole(PartRole partRole) {
        this.referencePartRole = partRole;
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

    public PartRole getReferencePartRole() {
        return referencePartRole;
    }

    public void setReferencePartRole(PartRole referencePartRole) {
        this.referencePartRole = referencePartRole;
    }

    @Override
    public String getBaseTypeName() {
        return PART;
    }

    @Override
    public String getFunctionalTypeName() {
        return NOARK_FONDS_STRUCTURE_PATH;
    }

}
