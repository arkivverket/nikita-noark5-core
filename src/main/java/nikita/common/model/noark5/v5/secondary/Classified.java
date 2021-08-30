package nikita.common.model.noark5.v5.secondary;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.*;
import nikita.common.model.noark5.v5.interfaces.entities.IClassifiedEntity;
import nikita.common.model.noark5.v5.metadata.ClassifiedCode;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static nikita.common.config.Constants.TABLE_CONTACT_CLASSIFIED;
import static nikita.common.config.N5ResourceMappings.CLASSIFICATION_ENG;
import static nikita.common.config.N5ResourceMappings.CLASSIFIED;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

/**
 * Created by tsodring on 4/10/16.
 */

@Entity
@Table(name = TABLE_CONTACT_CLASSIFIED)
@Indexed
public class Classified
        extends SystemIdEntity
        implements IClassifiedEntity {

    private static final long serialVersionUID = 1L;

    /**
     * M??? - grad/graderingskode code (xs:string)
     */
    @NotNull
    @Column(name = "classification_code", nullable = false)
    @Audited
    private String classificationCode;

    /**
     * M506 - grad/graderingskode code name (xs:string)
     */
    @Column(name = CLASSIFICATION_ENG)
    @Audited
    private String classificationCodeName;

    /**
     * M624 - graderingsdato (xs:dateTime)
     **/
    @Column(name = "classification_date")
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    @GenericField
    private OffsetDateTime classificationDate;

    /**
     * M629 - gradertAv (xs:string)
     */
    @Column(name = "classification_by")
    @Audited
    @KeywordField
    private String classificationBy;

    /**
     * M626 - nedgraderingsdato (xs:dateTime)
     **/
    @Column(name = "classification_downgraded_date")
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    @GenericField
    private OffsetDateTime classificationDowngradedDate;

    /**
     * M627 - nedgradertAv (xs:string)
     **/
    @Column(name = "classification_downgraded_by")
    @Audited
    @KeywordField
    private String classificationDowngradedBy;

    // Links to Series
    @OneToMany(mappedBy = "referenceClassified")
    private List<Series> referenceSeries = new ArrayList<>();

    // Links to Klass
    @OneToMany(mappedBy = "referenceClassified")
    private List<Class> referenceClass = new ArrayList<>();

    // Links to File
    @OneToMany(mappedBy = "referenceClassified")
    private List<File> referenceFile = new ArrayList<>();

    // Links to Record
    @OneToMany(mappedBy = "referenceClassified")
    private List<RecordEntity> referenceRecordEntity = new ArrayList<>();

    // Links to DocumentDescription
    @OneToMany(mappedBy = "referenceClassified")
    private List<DocumentDescription> referenceDocumentDescription =
            new ArrayList<>();

    public ClassifiedCode getClassification() {
        if (null == classificationCode)
            return null;
        return new ClassifiedCode(classificationCode,
                                  classificationCodeName);
    }

    public void setClassification(ClassifiedCode classification) {
        if (null != classification) {
            this.classificationCode = classification.getCode();
            this.classificationCodeName = classification.getCodeName();
        } else {
            this.classificationCode = null;
            this.classificationCodeName = null;
        }
    }

    public OffsetDateTime getClassificationDate() {
        return classificationDate;
    }

    public void setClassificationDate(OffsetDateTime classificationDate) {
        this.classificationDate = classificationDate;
    }

    public String getClassificationBy() {
        return classificationBy;
    }

    public void setClassificationBy(String classificationBy) {
        this.classificationBy = classificationBy;
    }

    public OffsetDateTime getClassificationDowngradedDate() {
        return classificationDowngradedDate;
    }

    public void setClassificationDowngradedDate(
            OffsetDateTime classificationDowngradedDate) {
        this.classificationDowngradedDate = classificationDowngradedDate;
    }

    public String getClassificationDowngradedBy() {
        return classificationDowngradedBy;
    }

    public void setClassificationDowngradedBy(
            String classificationDowngradedBy) {
        this.classificationDowngradedBy = classificationDowngradedBy;
    }

    @Override
    public String getBaseTypeName() {
        return CLASSIFIED;
    }

    @Override
    public String getBaseRel() {
        return CLASSIFIED; // TODO, should it have a relation key?
    }

    public List<Series> getReferenceSeries() {
        return referenceSeries;
    }

    public void setReferenceSeries(List<Series> referenceSeries) {
        this.referenceSeries = referenceSeries;
    }

    public void addSeries(Series klass) {
        referenceSeries.add(klass);
        klass.setReferenceClassified(this);
    }

    public void removeSeries(Series klass) {
        referenceSeries.remove(klass);
        klass.setReferenceClassified(null);
    }

    public List<Class> getReferenceClass() {
        return referenceClass;
    }

    public void setReferenceClass(List<Class> referenceClass) {
        this.referenceClass = referenceClass;
    }

    public void addClass(Class klass) {
        referenceClass.add(klass);
        klass.setReferenceClassified(this);
    }

    public void removeClass(Class klass) {
        referenceClass.remove(klass);
        klass.setReferenceClassified(null);
    }

    public List<File> getReferenceFile() {
        return referenceFile;
    }

    public void setReferenceFile(List<File> referenceFile) {
        this.referenceFile = referenceFile;
    }

    public void addFile(File klass) {
        referenceFile.add(klass);
        klass.setReferenceClassified(this);
    }

    public void removeFile(File klass) {
        referenceFile.remove(klass);
        klass.setReferenceClassified(null);
    }

    public List<RecordEntity> getReferenceRecordEntity() {
        return referenceRecordEntity;
    }

    public void setReferenceRecord(List<RecordEntity> referenceRecordEntity) {
        this.referenceRecordEntity = referenceRecordEntity;
    }

    public void addRecordEntity(RecordEntity klass) {
        referenceRecordEntity.add(klass);
        klass.setReferenceClassified(this);
    }

    public void removeRecordEntity(RecordEntity klass) {
        referenceRecordEntity.remove(klass);
        klass.setReferenceClassified(null);
    }

    public List<DocumentDescription> getReferenceDocumentDescription() {
        return referenceDocumentDescription;
    }

    public void setReferenceDocumentDescription(
            List<DocumentDescription> referenceDocumentDescription) {
        this.referenceDocumentDescription = referenceDocumentDescription;
    }

    public void addDocumentDescription(DocumentDescription klass) {
        referenceDocumentDescription.add(klass);
        klass.setReferenceClassified(this);
    }

    public void removeDocumentDescription(DocumentDescription klass) {
        referenceDocumentDescription.remove(klass);
        klass.setReferenceClassified(null);
    }

    @Override
    public String toString() {
        return "Classified{" + super.toString() +
                ", classificationCode='" + classificationCode + '\'' +
                ", classificationCodeName='" + classificationCodeName + '\'' +
                ", classificationDate=" + classificationDate +
                ", classificationBy='" + classificationBy + '\'' +
                ", classificationDowngradedDate=" +
                classificationDowngradedDate +
                ", classificationDowngradedBy='" +
                classificationDowngradedBy + '\'' +
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
        Classified rhs = (Classified) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(classificationCode, rhs.classificationCode)
                .append(classificationCodeName, rhs.classificationCodeName)
                .append(classificationDate, rhs.classificationDate)
                .append(classificationBy, rhs.classificationBy)
                .append(classificationDowngradedDate,
                        rhs.classificationDowngradedDate)
                .append(classificationDowngradedBy,
                        rhs.classificationDowngradedBy)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(classificationCode)
                .append(classificationCodeName)
                .append(classificationDate)
                .append(classificationBy)
                .append(classificationDowngradedDate)
                .append(classificationDowngradedBy)
                .toHashCode();
    }
}
