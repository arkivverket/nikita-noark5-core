package nikita.common.model.noark5.v5.secondary;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.*;
import nikita.common.model.noark5.v5.interfaces.entities.IScreeningEntity;
import nikita.common.model.noark5.v5.metadata.ScreeningDocument;
import nikita.common.model.noark5.v5.metadata.AccessRestriction;
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
     * M??? - tilgangsrestriksjon code n4 (JP.TGKODE) (xs:string)
     */
    @Column(name = "access_restriction_code")
    @Audited
    private String accessRestrictionCode;

    /**
     * M500 - tilgangsrestriksjon code name n4 (JP.TGKODE) (xs:string)
     */
    @Column(name = "access_restriction_code_name")
    @Audited
    private String accessRestrictionCodeName;

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
    // TODO convert to list/entity
    private String screeningMetadata;

    /**
     * M??? - skjermingDokument code (xs:string)
     */
    @Column(name = "screening_document_code")
    @Audited
    private String screeningDocumentCode;

    /**
     * M503 - skjermingDokument code name (xs:string)
     */
    @Column(name = "screening_document_code_name")
    @Audited
    private String screeningDocumentCodeName;

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

    public AccessRestriction getAccessRestriction() {
        if (null == accessRestrictionCode)
            return null;
        return new AccessRestriction(accessRestrictionCode,
                                     accessRestrictionCodeName);
    }

    public void setAccessRestriction(AccessRestriction accessRestriction) {
        if (null != accessRestriction) {
            this.accessRestrictionCode = accessRestriction.getCode();
            this.accessRestrictionCodeName = accessRestriction.getCodeName();
        } else {
            this.accessRestrictionCode = null;
            this.accessRestrictionCodeName = null;
        }
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

    public ScreeningDocument getScreeningDocument() {
        if (null == screeningDocumentCode)
            return null;
        return new ScreeningDocument(screeningDocumentCode,
                                     screeningDocumentCodeName);
    }

    public void setScreeningDocument(ScreeningDocument screeningDocument) {
        if (null != screeningDocument) {
            this.screeningDocumentCode = screeningDocument.getCode();
            this.screeningDocumentCodeName = screeningDocument.getCodeName();
        } else {
            this.screeningDocumentCode = null;
            this.screeningDocumentCodeName = null;
        }
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
                ", screeningDocumentCode='" + screeningDocumentCode + '\'' +
                ", screeningDocumentCodeName='" + screeningDocumentCodeName + '\'' +
                ", screeningMetadata='" + screeningMetadata + '\'' +
                ", screeningAuthority='" + screeningAuthority + '\'' +
                ", accessRestrictionCode='" + accessRestrictionCode + '\'' +
                ", accessRestrictionCodeName='" + accessRestrictionCodeName + '\'' +
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
                .append(screeningDocumentCode, rhs.screeningDocumentCode)
                .append(screeningDocumentCodeName, rhs.screeningDocumentCodeName)
                .append(screeningMetadata, rhs.screeningMetadata)
                .append(screeningAuthority, rhs.screeningAuthority)
                .append(accessRestrictionCode, rhs.accessRestrictionCode)
                .append(accessRestrictionCodeName, rhs.accessRestrictionCodeName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(screeningDuration)
                .append(screeningExpiresDate)
                .append(screeningDocumentCode)
                .append(screeningDocumentCodeName)
                .append(screeningMetadata)
                .append(screeningAuthority)
                .append(accessRestrictionCode)
                .append(accessRestrictionCodeName)
                .toHashCode();
    }
}
