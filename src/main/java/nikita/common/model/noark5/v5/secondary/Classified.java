package nikita.common.model.noark5.v5.secondary;

import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.*;
import nikita.common.model.noark5.v5.interfaces.entities.IClassifiedEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static nikita.common.config.Constants.TABLE_CONTACT_CLASSIFIED;
import static nikita.common.config.N5ResourceMappings.CLASSIFIED;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

/**
 * Created by tsodring on 4/10/16.
 */

@Entity
@Table(name = TABLE_CONTACT_CLASSIFIED)
public class Classified
        extends NoarkEntity
        implements IClassifiedEntity {

    private static final long serialVersionUID = 1L;

    /**
     * M506 - grad(xs:string)
     **/
    @Column(name = "classification")
    @Audited
    private String classification;

    /**
     * M624 - graderingsdato (xs:dateTime)
     **/
    @Column(name = "classification_date")
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    private ZonedDateTime classificationDate;

    /**
     * M629 - gradertAv (xs:string)
     */
    @Column(name = "classification_by")
    @Audited
    private String classificationBy;

    /**
     * M626 - nedgraderingsdato (xs:dateTime)
     **/
    @Column(name = "classification_downgraded_date")
    @Audited
    private ZonedDateTime classificationDowngradedDate;

    /**
     * M627 - nedgradertAv (xs:string)
     **/
    @Column(name = "classification_downgraded_by")
    @Audited
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
    private List<Record> referenceRecord = new ArrayList<>();

    // Links to DocumentDescription
    @OneToMany(mappedBy = "referenceClassified")
    private List<DocumentDescription> referenceDocumentDescription =
            new ArrayList<>();

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public ZonedDateTime getClassificationDate() {
        return classificationDate;
    }

    public void setClassificationDate(ZonedDateTime classificationDate) {
        this.classificationDate = classificationDate;
    }

    public String getClassificationBy() {
        return classificationBy;
    }

    public void setClassificationBy(String classificationBy) {
        this.classificationBy = classificationBy;
    }

    public ZonedDateTime getClassificationDowngradedDate() {
        return classificationDowngradedDate;
    }

    public void setClassificationDowngradedDate(
            ZonedDateTime classificationDowngradedDate) {
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

    public List<Series> getReferenceSeries() {
        return referenceSeries;
    }

    public void setReferenceSeries(List<Series> referenceSeries) {
        this.referenceSeries = referenceSeries;
    }

    public List<Class> getReferenceClass() {
        return referenceClass;
    }

    public void setReferenceClass(List<Class> referenceClass) {
        this.referenceClass = referenceClass;
    }

    public List<File> getReferenceFile() {
        return referenceFile;
    }

    public void setReferenceFile(List<File> referenceFile) {
        this.referenceFile = referenceFile;
    }

    public List<Record> getReferenceRecord() {
        return referenceRecord;
    }

    public void setReferenceRecord(List<Record> referenceRecord) {
        this.referenceRecord = referenceRecord;
    }

    public List<DocumentDescription> getReferenceDocumentDescription() {
        return referenceDocumentDescription;
    }

    public void setReferenceDocumentDescription(
            List<DocumentDescription> referenceDocumentDescription) {
        this.referenceDocumentDescription = referenceDocumentDescription;
    }

    @Override
    public String toString() {
        return "Classified{" + super.toString() +
                ", classification='" + classification + '\'' +
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
                .append(classification, rhs.classification)
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
                .append(classification)
                .append(classificationDate)
                .append(classificationBy)
                .append(classificationDowngradedDate)
                .append(classificationDowngradedBy)
                .toHashCode();
    }
}
