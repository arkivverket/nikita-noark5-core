package nikita.common.model.noark5.v5.secondary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.*;
import nikita.common.model.noark5.v5.interfaces.entities.IScreeningEntity;
import nikita.common.model.noark5.v5.metadata.AccessRestriction;
import nikita.common.model.noark5.v5.metadata.ScreeningDocument;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;
import static nikita.common.config.Constants.TABLE_SCREENING;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = TABLE_SCREENING)
public class Screening
        extends SystemIdEntity
        implements IScreeningEntity {

    /**
     * M??? - tilgangsrestriksjon code n4 (JP.TGKODE) (xs:string)
     */
    @Column(name = ACCESS_RESTRICTION_CODE_ENG)
    @Audited
    @JsonProperty(ACCESS_RESTRICTION_CODE)
    private String accessRestrictionCode;

    /**
     * M500 - tilgangsrestriksjon code name n4 (JP.TGKODE) (xs:string)
     */
    @Column(name = ACCESS_RESTRICTION_CODE_NAME_ENG)
    @Audited
    @JsonProperty(ACCESS_RESTRICTION_CODE_NAME)
    private String accessRestrictionCodeName;

    /**
     * M501 - skjermingshjemmel n4 (JP.UOFF)
     */
    @Column(name = SCREENING_AUTHORITY_ENG)
    @Audited
    @JsonProperty(SCREENING_AUTHORITY)
    private String screeningAuthority;

    /**
     * M??? - skjermingDokument code (xs:string)
     */
    @Column(name = SCREENING_DOCUMENT_CODE_ENG)
    @Audited
    @JsonProperty(SCREENING_DOCUMENT_CODE)
    private String screeningDocumentCode;

    /**
     * M503 - skjermingdokument code name (xs:string)
     */
    @Column(name = SCREENING_DOCUMENT_CODE_NAME_ENG)
    @Audited
    @JsonProperty(SCREENING_DOCUMENT_CODE_NAME)
    private String screeningDocumentCodeName;

    /**
     * M505 - skjermingOpphoererDato n4(JP.AGDATO)
     */
    @Column(name = SCREENING_EXPIRES_DATE_ENG)
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    @JsonProperty(SCREENING_EXPIRES_DATE)
    private OffsetDateTime screeningExpiresDate;

    /**
     * M504 - skjermingsvarighet (xs:integer)
     */
    @Column(name = SCREENING_DURATION_ENG)
    @Audited
    @JsonProperty(SCREENING_DURATION)
    private Integer screeningDuration;

    /**
     * Links to ScreeningMetadata
     * M502 - skjermingMetadata. Note this is a list
     */
    @OneToMany(mappedBy = REFERENCE_SCREENING, cascade = PERSIST)
    @JsonIgnore
    private final Set<ScreeningMetadataLocal> referenceScreeningMetadata
            = new HashSet<>();
    // Links to Series
    @OneToMany(mappedBy = REFERENCE_SCREENING)
    private List<Series> referenceSeries = new ArrayList<>();
    // Links to Class
    @OneToMany(mappedBy = REFERENCE_SCREENING)
    private List<Class> referenceClass = new ArrayList<>();
    // Links to File
    @OneToMany(mappedBy = REFERENCE_SCREENING)
    private List<File> referenceFile = new ArrayList<>();
    // Links to Record
    @OneToMany(mappedBy = REFERENCE_SCREENING)
    private List<Record> referenceRecord = new ArrayList<>();

    // Links to DocumentDescription
    @OneToMany(mappedBy = REFERENCE_SCREENING)
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

    public void addSeries(Series series) {
        this.referenceSeries.add(series);
        series.setReferenceScreening(this);
    }

    public void removeSeries(Series series) {
        this.referenceSeries.remove(series);
        series.setReferenceScreening(null);
    }

    public List<Class> getReferenceClass() {
        return referenceClass;
    }

    public void setReferenceClass(List<Class> referenceClass) {
        this.referenceClass = referenceClass;
    }

    public void addClass(Class klass) {
        this.referenceClass.add(klass);
        klass.setReferenceScreening(this);
    }

    public void removeClass(Class klass) {
        this.referenceClass.remove(klass);
        klass.setReferenceScreening(null);
    }

    public List<File> getReferenceFile() {
        return referenceFile;
    }

    public void setReferenceFile(List<File> referenceFile) {
        this.referenceFile = referenceFile;
    }

    public void addFile(File file) {
        this.referenceFile.add(file);
        file.setReferenceScreening(this);
    }

    public void removeFile(File file) {
        this.referenceFile.remove(file);
        file.setReferenceScreening(null);
    }

    public List<Record> getReferenceRecord() {
        return referenceRecord;
    }

    public void setReferenceRecord(List<Record> referenceRecord) {
        this.referenceRecord = referenceRecord;
    }

    public void addRecord(Record record) {
        this.referenceRecord.add(record);
        record.setReferenceScreening(this);
    }

    public void removeRecord(Record record) {
        this.referenceRecord.remove(record);
        record.setReferenceScreening(null);
    }

    public List<DocumentDescription> getReferenceDocumentDescription() {
        return referenceDocumentDescription;
    }

    public void setReferenceDocumentDescription(
            List<DocumentDescription> referenceDocumentDescription) {
        this.referenceDocumentDescription = referenceDocumentDescription;
    }

    public void addDocumentDescription(
            DocumentDescription documentDescription) {
        this.referenceDocumentDescription.add(documentDescription);
        documentDescription.setReferenceScreening(this);
    }

    public void removeDocumentDescription(
            DocumentDescription documentDescription) {
        this.referenceDocumentDescription.remove(documentDescription);
        documentDescription.setReferenceScreening(null);
    }

    public Set<ScreeningMetadataLocal> getReferenceScreeningMetadata() {
        return referenceScreeningMetadata;
    }

    public void addReferenceScreeningMetadata(
            ScreeningMetadataLocal screeningMetadata) {
        this.referenceScreeningMetadata.add(screeningMetadata);
        screeningMetadata.setReferenceScreening(this);
    }

    public void removeReferenceScreeningMetadata(
            ScreeningMetadataLocal screeningMetadata) {
        this.referenceScreeningMetadata.remove(screeningMetadata);
        screeningMetadata.setReferenceScreening(null);
    }

    @Override
    public String toString() {
        return "Screening {" + super.toString() +
                "screeningDuration='" + screeningDuration + '\'' +
                ", screeningExpiresDate=" + screeningExpiresDate +
                ", screeningDocumentCode='" + screeningDocumentCode + '\'' +
                ", screeningDocumentCodeName='" + screeningDocumentCodeName + '\'' +
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
                .append(screeningAuthority)
                .append(accessRestrictionCode)
                .append(accessRestrictionCodeName)
                .toHashCode();
    }
}
