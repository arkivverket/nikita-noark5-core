package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.hateoas.SeriesHateoas;
import nikita.common.model.noark5.v5.interfaces.*;
import nikita.common.model.noark5.v5.metadata.DocumentMedium;
import nikita.common.model.noark5.v5.metadata.SeriesStatus;
import nikita.common.model.noark5.v5.secondary.*;
import nikita.common.util.deserialisers.SeriesDeserializer;
import nikita.webapp.hateoas.SeriesHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@Entity
@Table(name = TABLE_SERIES)
@JsonDeserialize(using = SeriesDeserializer.class)
@HateoasPacker(using = SeriesHateoasHandler.class)
@HateoasObject(using = SeriesHateoas.class)
public class Series
        extends NoarkGeneralEntity
        implements IStorageLocation, IDocumentMedium, IClassified, IScreening,
        IDisposal, IDisposalUndertaken, IDeletion {

    private static final long serialVersionUID = 1L;

    /**
     * M??? - arkivdelstatus code (xs:string)
     */
    @NotNull
    @Column(name = "series_status_code", nullable = false)
    @Audited
    private String seriesStatusCode;

    /**
     * M051 - arkivdelstatus code name (xs:string)
     */
    @NotNull
    @Column(name = "series_status_code_name", nullable = false)
    @Audited
    private String seriesStatusCodeName;

    /**
     * M107 - arkivperiodeStartDato (xs:date)
     */
    @Column(name = SERIES_START_DATE_ENG)
    @DateTimeFormat(iso = DATE)
    @Audited
    @JsonProperty(SERIES_START_DATE)
    private OffsetDateTime seriesStartDate;

    /**
     * M108 - arkivperiodeSluttDato (xs:date)
     */
    @Column(name = SERIES_END_DATE_ENG)
    @DateTimeFormat(iso = DATE)
    @Audited
    @JsonProperty(SERIES_END_DATE)
    private OffsetDateTime seriesEndDate;

    /**
     * M??? - dokumentmedium code (xs:string)
     */
    @Column(name = "document_medium_code")
    @Audited
    private String documentMediumCode;

    /**
     * M300 - dokumentmedium code name (xs:string)
     */
    @Column(name = "document_medium_code_name")
    @Audited
    private String documentMediumCodeName;

    // Links to StorageLocations
    @ManyToMany(cascade = ALL)
    @JoinTable(name = TABLE_SERIES_STORAGE_LOCATION,
            joinColumns = @JoinColumn(
                    name = FOREIGN_KEY_SERIES_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(
                    name = FOREIGN_KEY_STORAGE_LOCATION_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private List<StorageLocation> referenceStorageLocation = new ArrayList<>();

    // Link to Fonds
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = SERIES_FONDS_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    @JsonIgnore
    private Fonds referenceFonds;

    /**
     * M202 - referanseForloeper (xs:ID)
     */
    @Column(name = "reference_precursor_id")
    @Type(type = "uuid-char")
    @JsonProperty(SERIES_ASSOCIATE_AS_PRECURSOR)
    private UUID referencePrecursorSystemID;

    /**
     * M203 - referanseArvtaker (xs:ID)
     */
    @Column(name = "reference_successor_id")
    @Type(type = "uuid-char")
    @JsonProperty(SERIES_ASSOCIATE_AS_SUCCESSOR)
    private UUID referenceSuccessorSystemID;

    // Link to precursor Series if present
    @OneToOne(fetch = LAZY)
    private Series referencePrecursor;

    // Link to successor Series if present
    @OneToOne(fetch = LAZY, mappedBy = "referencePrecursor")
    private Series referenceSuccessor;

    // Link to ClassificationSystem
    @ManyToMany
    @JoinTable(name = TABLE_SERIES_CLASSIFICATION_SYSTEM,
            joinColumns = @JoinColumn(
                    name = FOREIGN_KEY_SERIES_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(
                    name = FOREIGN_KEY_CLASSIFICATION_SYSTEM_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private List<ClassificationSystem> referenceClassificationSystem =
            new ArrayList<>();

    // Links to Files
    @JsonIgnore
    @OneToMany(mappedBy = "referenceSeries")
    private List<File> referenceFile = new ArrayList<>();

    // Links to Records
    @OneToMany(mappedBy = "referenceSeries")
    @JsonIgnore
    private List<Record> referenceRecord = new ArrayList<>();

    // Links to Classified
    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = SERIES_CLASSIFIED_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    @JsonIgnore
    private Classified referenceClassified;

    // Link to Disposal
    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = SERIES_DISPOSAL_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    @JsonIgnore
    private Disposal referenceDisposal;

    // Link to Screening
    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = SERIES_SCREENING_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    @JsonIgnore
    private Screening referenceScreening;

    // Link to DisposalUndertaken
    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = SERIES_DISPOSAL_UNDERTAKEN_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    @JsonIgnore
    private DisposalUndertaken referenceDisposalUndertaken;

    // Link to Deletion
    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = SERIES_DELETION_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    @JsonIgnore
    private Deletion referenceDeletion;

    public SeriesStatus getSeriesStatus() {
        if (null == seriesStatusCode)
            return null;
        return new SeriesStatus(seriesStatusCode, seriesStatusCodeName);
    }

    public void setSeriesStatus(SeriesStatus seriesStatus) {
        if (null != seriesStatus) {
            this.seriesStatusCode = seriesStatus.getCode();
            this.seriesStatusCodeName = seriesStatus.getCodeName();
        } else {
            this.seriesStatusCode = null;
            this.seriesStatusCodeName = null;
        }
    }

    public DocumentMedium getDocumentMedium() {
        if (null == documentMediumCode)
            return null;
        return new DocumentMedium(documentMediumCode, documentMediumCodeName);
    }

    public void setDocumentMedium(DocumentMedium documentMedium) {
        if (null != documentMedium) {
            this.documentMediumCode = documentMedium.getCode();
            this.documentMediumCodeName = documentMedium.getCodeName();
        } else {
            this.documentMediumCode = null;
            this.documentMediumCodeName = null;
        }
    }

    public OffsetDateTime getSeriesStartDate() {
        return seriesStartDate;
    }

    public void setSeriesStartDate(OffsetDateTime seriesStartDate) {
        this.seriesStartDate = seriesStartDate;
    }

    public OffsetDateTime getSeriesEndDate() {
        return seriesEndDate;
    }

    public void setSeriesEndDate(OffsetDateTime seriesEndDate) {
        this.seriesEndDate = seriesEndDate;
    }

    @Override
    public String getBaseTypeName() {
        return SERIES;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_SERIES;
    }

    @Override
    public List<StorageLocation> getReferenceStorageLocation() {
        return referenceStorageLocation;
    }

    @Override
    public void setReferenceStorageLocation(
            List<StorageLocation> referenceStorageLocation) {
        this.referenceStorageLocation = referenceStorageLocation;
    }

    @Override
    public void addReferenceStorageLocation(StorageLocation storageLocation) {
        this.referenceStorageLocation.add(storageLocation);
    }

    public Fonds getReferenceFonds() {
        return referenceFonds;
    }

    public void setReferenceFonds(Fonds referenceFonds) {
        this.referenceFonds = referenceFonds;
    }

    public UUID getReferencePrecursorSystemID() {
        return referencePrecursorSystemID;
    }

    public void setReferencePrecursorSystemID(UUID referencePrecursorSystemID) {
        this.referencePrecursorSystemID = referencePrecursorSystemID;
    }

    public UUID getReferenceSuccessorSystemID() {
        return referenceSuccessorSystemID;
    }

    public void setReferenceSuccessorSystemID(UUID referenceSuccessorSystemID) {
        this.referenceSuccessorSystemID = referenceSuccessorSystemID;
    }

    public Series getReferencePrecursor() {
        return referencePrecursor;
    }

    public void setReferencePrecursor(Series referencePrecursor) {
        this.referencePrecursor = referencePrecursor;
    }

    public Series getReferenceSuccessor() {
        return referenceSuccessor;
    }

    public void setReferenceSuccessor(Series referenceSuccessor) {
        this.referenceSuccessor = referenceSuccessor;
    }

    public List<ClassificationSystem> getReferenceClassificationSystem() {
        return referenceClassificationSystem;
    }

    public void setReferenceClassificationSystem(
            List <ClassificationSystem> referenceClassificationSystem) {
        this.referenceClassificationSystem = referenceClassificationSystem;
    }

    public void addClassificationSystem(
            ClassificationSystem classificationSystem) {
        this.referenceClassificationSystem.add(classificationSystem);
    }

    public List<File> getReferenceFile() {
        return referenceFile;
    }

    public void setReferenceFile(List<File> referenceFile) {
        this.referenceFile = referenceFile;
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
    public void setReferenceDisposal(Disposal referenceDisposal) {
        this.referenceDisposal = referenceDisposal;
    }

    @Override
    public DisposalUndertaken getReferenceDisposalUndertaken() {
        return referenceDisposalUndertaken;
    }

    @Override
    public void setReferenceDisposalUndertaken(
            DisposalUndertaken referenceDisposalUndertaken) {
        this.referenceDisposalUndertaken = referenceDisposalUndertaken;
    }

    @Override
    public Screening getReferenceScreening() {
        return referenceScreening;
    }

    @Override
    public void setReferenceScreening(Screening referenceScreening) {
        this.referenceScreening = referenceScreening;
    }

    @Override
    public Deletion getReferenceDeletion() {
        return referenceDeletion;
    }

    @Override
    public void setReferenceDeletion(Deletion referenceDeletion) {
        this.referenceDeletion = referenceDeletion;
    }

    @Override
    public String toString() {
        return "Series{" + super.toString() +
                ", seriesStatusCode='" + seriesStatusCode + '\'' +
                ", seriesStatusCodeName='" + seriesStatusCodeName + '\'' +
                ", seriesStartDate=" + seriesStartDate +
                ", seriesEndDate=" + seriesEndDate +
                ", documentMediumCode='" + documentMediumCode + '\'' +
                ", documentMediumCodeName='" + documentMediumCodeName + '\'' +
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
        Series rhs = (Series) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(seriesEndDate, rhs.seriesEndDate)
                .append(seriesStartDate, rhs.seriesStartDate)
                .append(documentMediumCode, rhs.documentMediumCode)
                .append(documentMediumCodeName, rhs.documentMediumCodeName)
                .append(seriesStatusCode, rhs.seriesStatusCode)
                .append(seriesStatusCodeName, rhs.seriesStatusCodeName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(seriesEndDate)
                .append(seriesStartDate)
                .append(documentMediumCode)
                .append(documentMediumCodeName)
                .append(seriesStatusCode)
                .append(seriesStatusCodeName)
                .toHashCode();
    }
}
