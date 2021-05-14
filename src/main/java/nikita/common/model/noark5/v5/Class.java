package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.hateoas.ClassHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IClassEntity;
import nikita.common.model.noark5.v5.secondary.*;
import nikita.common.util.deserialisers.ClassDeserializer;
import nikita.webapp.hateoas.ClassHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

@Entity
@Table(name = TABLE_CLASS)
@JsonDeserialize(using = ClassDeserializer.class)
@HateoasPacker(using = ClassHateoasHandler.class)
@HateoasObject(using = ClassHateoas.class)
public class Class
        extends NoarkGeneralEntity
        implements IClassEntity {

    /**
     * M002 - klasseID (xs:string)
     */
    @Column(name = CLASS_ID_ENG)
    @Audited
    @JsonProperty(CLASS_ID)
    private String classId;

    // Links to Keywords
    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(name = TABLE_CLASS_KEYWORD,
            joinColumns = @JoinColumn(
                    name = FOREIGN_KEY_CLASS_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(
                    name = FOREIGN_KEY_KEYWORD_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private Set<Keyword> referenceKeyword = new HashSet<>();

    // Link to ClassificationSystem
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "class_classification_system_id",
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private ClassificationSystem referenceClassificationSystem;

    // Link to parent Class
    @ManyToOne(fetch = LAZY)
    private Class referenceParentClass;

    // Links to child Classes
    @OneToMany(mappedBy = "referenceParentClass")
    private List<Class> referenceChildClass = new ArrayList<>();

    // Links to Files
    @OneToMany(mappedBy = "referenceClass")
    private List<File> referenceFile = new ArrayList<>();

    // Links to Records
    @OneToMany(mappedBy = "referenceClass")
    private List<Record> referenceRecord = new ArrayList<>();

    // Links to Classified
    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = CLASS_CLASSIFIED_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Classified referenceClassified;

    // Link to Disposal
    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = CLASS_DISPOSAL_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Disposal referenceDisposal;

    // Link to Screening
    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = CLASS_SCREENING_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Screening referenceScreening;

    @OneToMany(mappedBy = "referenceClass", cascade = {PERSIST, MERGE, REMOVE})
    private List<CrossReference> referenceCrossReference = new ArrayList<>();

    @Override
    public String getClassId() {
        return classId;
    }

    @Override
    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Override
    public String getBaseTypeName() {
        return CLASS;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_CLASS;
    }

    @Override
    public Set<Keyword> getReferenceKeyword() {
        return referenceKeyword;
    }

    @Override
    public void addKeyword(Keyword keyword) {
        this.referenceKeyword.add(keyword);
        keyword.getReferenceClass().add(this);
    }

    @Override
    public void removeKeyword(Keyword keyword) {
        this.referenceKeyword.remove(keyword);
        keyword.getReferenceClass().remove(this);
    }

    @Override
    public ClassificationSystem getReferenceClassificationSystem() {
        return referenceClassificationSystem;
    }

    @Override
    public void setReferenceClassificationSystem(
            ClassificationSystem referenceClassificationSystem) {
        this.referenceClassificationSystem = referenceClassificationSystem;
    }

    @Override
    public Class getReferenceParentClass() {
        return referenceParentClass;
    }

    @Override
    public void setReferenceParentClass(Class referenceParentClass) {
        this.referenceParentClass = referenceParentClass;
    }

    @Override
    public List<Class> getReferenceChildClass() {
        return referenceChildClass;
    }

    @Override
    public void setReferenceChildClass(List<Class> referenceChildClass) {
        this.referenceChildClass = referenceChildClass;
    }

    public void addClass(Class klass) {
        this.referenceChildClass.add(klass);
        klass.setReferenceParentClass(this);
    }

    public void removeClass(Class klass) {
        this.referenceChildClass.remove(klass);
        klass.setReferenceParentClass(null);
    }

    @Override
    public List<File> getReferenceFile() {
        return referenceFile;
    }

    @Override
    public void setReferenceFile(List<File> referenceFile) {
        this.referenceFile = referenceFile;
    }

    public void addFile(File file) {
        this.referenceFile.add(file);
        file.setReferenceClass(this);
    }

    public void removeFile(File file) {
        this.referenceFile.remove(file);
        file.setReferenceClass(null);
    }

    @Override
    public List<Record> getReferenceRecord() {
        return referenceRecord;
    }

    @Override
    public void setReferenceRecord(List<Record> referenceRecord) {
        this.referenceRecord = referenceRecord;
    }

    public void addRecord(Record record) {
        this.referenceRecord.add(record);
        record.setReferenceClass(this);
    }

    public void removeRecord(Record record) {
        this.referenceRecord.remove(record);
        record.setReferenceClass(null);
    }

    @Override
    public Classified getReferenceClassified() {
        return referenceClassified;
    }

    @Override
    public void setReferenceClassified(Classified referenceClassified) {
        this.referenceClassified = referenceClassified;
    }

    @Override
    public Disposal getReferenceDisposal() {
        return referenceDisposal;
    }

    @Override
    public void setReferenceDisposal(Disposal disposal) {
        this.referenceDisposal = disposal;
    }

    @Override
    public Screening getReferenceScreening() {
        return referenceScreening;
    }

    @Override
    public void setReferenceScreening(Screening screening) {
        this.referenceScreening = screening;
    }

    @Override
    public List<CrossReference> getReferenceCrossReference() {
        return referenceCrossReference;
    }

    @Override
    public void addCrossReference(CrossReference crossReference) {
        this.referenceCrossReference.add(crossReference);
        crossReference.setReferenceClass(this);
    }

    @Override
    public void removeCrossReference(CrossReference crossReference) {
        this.referenceCrossReference.remove(crossReference);
        crossReference.setReferenceClass(null);
    }

    @Override
    public String toString() {
        return "Class{" + super.toString() +
                ", classId='" + classId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != getClass()) {
            return false;
        }
        Class rhs = (Class) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(classId, rhs.classId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(classId)
                .toHashCode();
    }
}
