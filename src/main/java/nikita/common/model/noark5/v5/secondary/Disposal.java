package nikita.common.model.noark5.v5.secondary;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.*;
import nikita.common.model.noark5.v5.interfaces.entities.IDisposalEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static nikita.common.config.Constants.TABLE_DISPOSAL;
import static nikita.common.config.N5ResourceMappings.DISPOSAL;

/**
 * Created by tsodring on 4/10/16.
 */
@Entity
@Table(name = TABLE_DISPOSAL)
public class Disposal
        extends SystemIdEntity
        implements IDisposalEntity {

    private static final long serialVersionUID = 1L;

    /**
     * M450 - kassasjonsvedtak (xs:string)
     */
    @Column(name = "disposal_decision")
    @Audited
    private String disposalDecision;

    /**
     * M453 - kassasjonshjemmel (xs:string)
     */
    @Column(name = "disposal_authority")
    @Audited
    private String disposalAuthority;

    /**
     * M451 - bevaringstid (xs:integer)
     */
    @Column(name = "preservation_time")
    @Audited
    private Integer preservationTime;

    /**
     * M452 - kassasjonsdato (xs:date)
     */
    @Column(name = "disposal_date")
    @Audited
    private OffsetDateTime disposalDate;

    // Links to Series
    @OneToMany(mappedBy = "referenceDisposal")
    private List<Series> referenceSeries = new ArrayList<>();

    // Links to Class
    @OneToMany(mappedBy = "referenceDisposal")
    private List<Class> referenceClass = new ArrayList<>();

    // Links to File
    @OneToMany(mappedBy = "referenceDisposal")
    private List<File> referenceFile = new ArrayList<>();

    // Links to Record
    @OneToMany(mappedBy = "referenceDisposal")
    private List<Record> referenceRecord = new ArrayList<>();

    // Links to DocumentDescription
    @OneToMany(mappedBy = "referenceDisposal")
    private List<DocumentDescription>
            referenceDocumentDescription = new ArrayList<>();

    public String getDisposalDecision() {
        return disposalDecision;
    }

    public void setDisposalDecision(String disposalDecision) {
        this.disposalDecision = disposalDecision;
    }

    public String getDisposalAuthority() {
        return disposalAuthority;
    }

    public void setDisposalAuthority(String disposalAuthority) {
        this.disposalAuthority = disposalAuthority;
    }

    public Integer getPreservationTime() {
        return preservationTime;
    }

    public void setPreservationTime(Integer preservationTime) {
        this.preservationTime = preservationTime;
    }

    public OffsetDateTime getDisposalDate() {
        return disposalDate;
    }

    public void setDisposalDate(OffsetDateTime disposalDate) {
        this.disposalDate = disposalDate;
    }

    @Override
    public String getBaseTypeName() {
        return DISPOSAL;
    }

    @Override
    public String getBaseRel() {
        return DISPOSAL; // TODO, should it have a relation key?
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
        return "Disposal{" + super.toString() +
                "disposalDate=" + disposalDate +
                ", preservationTime=" + preservationTime +
                ", disposalAuthority='" + disposalAuthority + '\'' +
                ", disposalDecision='" + disposalDecision + '\'' +
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
        Disposal rhs = (Disposal) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(disposalDate, rhs.disposalDate)
                .append(preservationTime, rhs.preservationTime)
                .append(disposalAuthority, rhs.disposalAuthority)
                .append(disposalDecision, rhs.disposalDecision)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(disposalDate)
                .append(preservationTime)
                .append(disposalAuthority)
                .append(disposalDecision)
                .toHashCode();
    }
}
