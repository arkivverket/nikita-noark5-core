package nikita.common.model.noark5.v5.secondary;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.*;
import nikita.common.model.noark5.v5.interfaces.entities.IScreeningEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static nikita.common.config.Constants.TABLE_SCREENING;
import static nikita.common.config.N5ResourceMappings.SCREENING;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@Entity
@Table(name = TABLE_SCREENING)
public class Screening
        extends SystemIdEntity
        implements IScreeningEntity {

    /**
     * M500 - tilgangsrestriksjon n4 (JP.TGKODE)
     */
    @Column(name = "access_restriction")
    @Audited
    private String accessRestriction;

    /**
     * M501 - skjermingshjemmel n4 (JP.UOFF)
     */
    @Column(name = "screening_authority")
    @Audited
    private String screeningAuthority;

    /**
     * M502 - skjermingMetadata should be 1-M
     */
    @Column(name = "screening_metadata")
    @Audited
    private String screeningMetadata;

    /**
     * M503 - skjermingDokument
     */
    @Column(name = "screening_document")
    @Audited
    private String screeningDocument;

    /**
     * M505 - skjermingOpphoererDato n4(JP.AGDATO)
     */
    @Column(name = "screening_expires")
    @DateTimeFormat(iso = DATE)
    @Audited
    private OffsetDateTime screeningExpiresDate;

    /**
     * M504 - skjermingsvarighet (xs:integer)
     */
    @Column(name = "screening_duration")
    @Audited
    private Integer screeningDuration;

    // Links to Series
    @ManyToMany(mappedBy = "referenceScreening")
    private List<Series> referenceSeries = new ArrayList<>();

    // Links to Class
    @ManyToMany(mappedBy = "referenceScreening")
    private List<Class> referenceClass = new ArrayList<>();

    // Links to File
    @ManyToMany(mappedBy = "referenceScreening")
    private List<File> referenceFile = new ArrayList<>();

    // Links to Record
    @ManyToMany(mappedBy = "referenceScreening")
    private List<Record> referenceRecord = new ArrayList<>();

    // Links to DocumentDescription
    @ManyToMany(mappedBy = "referenceScreening")
    private List<DocumentDescription> referenceDocumentDescription =
            new ArrayList<>();

    public String getAccessRestriction() {
        return accessRestriction;
    }

    public void setAccessRestriction(String accessRestriction) {
        this.accessRestriction = accessRestriction;
    }

    public String getScreeningAuthority() {
        return screeningAuthority;
    }

    public void setScreeningAuthority(String screeningAuthority) {
        this.screeningAuthority = screeningAuthority;
    }

    public String getScreeningMetadata() {
        return screeningMetadata;
    }

    public void setScreeningMetadata(String screeningMetadata) {
        this.screeningMetadata = screeningMetadata;
    }

    public String getScreeningDocument() {
        return screeningDocument;
    }

    public void setScreeningDocument(String screeningDocument) {
        this.screeningDocument = screeningDocument;
    }

    public OffsetDateTime getScreeningExpiresDate() {
        return screeningExpiresDate;
    }

    public void setScreeningExpiresDate(OffsetDateTime screeningExpiresDate) {
        this.screeningExpiresDate = screeningExpiresDate;
    }

    public Integer getScreeningDuration() {
        return screeningDuration;
    }

    public void setScreeningDuration(Integer screeningDuration) {
        this.screeningDuration = screeningDuration;
    }

    @Override
    public String getBaseTypeName() {
        return SCREENING;
    }

    @Override
    public String getBaseRel() {
        return SCREENING; // TODO, should it have a relation key?
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
        return "Screening {" + super.toString() +
                "screeningDuration='" + screeningDuration + '\'' +
                ", screeningExpiresDate=" + screeningExpiresDate +
                ", screeningDocument='" + screeningDocument + '\'' +
                ", screeningMetadata='" + screeningMetadata + '\'' +
                ", screeningAuthority='" + screeningAuthority + '\'' +
                ", accessRestriction='" + accessRestriction + '\'' +
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
        Screening rhs = (Screening) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(screeningDuration, rhs.screeningDuration)
                .append(screeningExpiresDate, rhs.screeningExpiresDate)
                .append(screeningDocument, rhs.screeningDocument)
                .append(screeningMetadata, rhs.screeningMetadata)
                .append(screeningAuthority, rhs.screeningAuthority)
                .append(accessRestriction, rhs.accessRestriction)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(screeningDuration)
                .append(screeningExpiresDate)
                .append(screeningDocument)
                .append(screeningMetadata)
                .append(screeningAuthority)
                .append(accessRestriction)
                .toHashCode();
    }
}
