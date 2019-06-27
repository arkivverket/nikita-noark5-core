package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.hateoas.RecordHateoas;
import nikita.common.model.noark5.v5.interfaces.*;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkCreateEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkTitleDescriptionEntity;
import nikita.common.model.noark5.v5.secondary.*;
import nikita.common.util.deserialisers.RecordDeserializer;
import nikita.webapp.hateoas.RecordHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = "record")
@Inheritance(strategy = JOINED)
@JsonDeserialize(using = RecordDeserializer.class)
@HateoasPacker(using = RecordHateoasHandler.class)
@HateoasObject(using = RecordHateoas.class)
@AttributeOverride(name = "id", column = @Column(name = PRIMARY_KEY_RECORD))
public class Record
        extends NoarkEntity
        implements INoarkCreateEntity, IClassified, IScreening, IDisposal,
        IDocumentMedium, INoarkTitleDescriptionEntity, IStorageLocation,
        IKeyword, IComment, ICrossReference, IAuthor {

    /**
     * M600 - opprettetDato (xs:dateTime)
     */
    @Column(name = "created_date")
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    private ZonedDateTime createdDate;

    /**
     * M601 - opprettetAv (xs:string)
     */
    @Column(name = "created_by")
    @Audited
    private String createdBy;

    /**
     * M604 - arkivertDato (xs:dateTime)
     */
    @Column(name = "archived_date")
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    private ZonedDateTime archivedDate;

    /**
     * M605 - arkivertAv (xs:string)
     */
    @Column(name = "archived_by")
    @Audited
    private String archivedBy;


    /**
     * M004 - registreringsID (xs:string)
     */
    @Column(name = "record_id")
    @Audited
    private String recordId;

    /**
     * M020 - tittel (xs:string)
     */
    @NotNull
    @Column(name = "title", nullable = false)
    @Audited
    private String title;

    /**
     * M025 - offentligTittel (xs:string)
     */
    @Column(name = "official_title")
    @Audited
    private String officialTitle;

    /**
     * M021 - beskrivelse (xs:string)
     */
    @Column(name = "description")
    @Audited
    private String description;

    /**
     * M300 - dokumentmedium (xs:string)
     */
    @Column(name = "document_medium")
    @Audited
    private String documentMedium;

    @Column(name = "owned_by")
    @Audited
    private String ownedBy;

    // Link to File
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "record_file_id", referencedColumnName = "pk_file_id")
    private File referenceFile;

    // Link to StorageLocation
    @ManyToMany(cascade = PERSIST)
    @JoinTable(name = TABLE_RECORD_STORAGE_LOCATION,
            joinColumns = @JoinColumn(name = FOREIGN_KEY_RECORD_PK,
                    referencedColumnName = PRIMARY_KEY_RECORD),
            inverseJoinColumns = @JoinColumn(
                    name = FOREIGN_KEY_STORAGE_LOCATION_PK,
                    referencedColumnName = PRIMARY_KEY_STORAGE_LOCATION))
    private List<StorageLocation> referenceStorageLocation =
            new ArrayList<>();
    // Link to Series
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "record_series_id", referencedColumnName = "pk_series_id")
    private Series referenceSeries;
    // Link to Class
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "record_class_id", referencedColumnName = "pk_class_id")
    private Class referenceClass;

    // Used for soft delete.
    @Column(name = "deleted")
    @Audited
    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }
    
    // Links to Keywords
    @ManyToMany
    @JoinTable(name = TABLE_RECORD_KEYWORD,
            joinColumns = @JoinColumn(name = FOREIGN_KEY_RECORD_PK,
                    referencedColumnName = PRIMARY_KEY_RECORD),
            inverseJoinColumns = @JoinColumn(name = FOREIGN_KEY_KEYWORD_PK,
                    referencedColumnName = PRIMARY_KEY_KEYWORD))
    private List<Keyword> referenceKeyword = new ArrayList<>();

    // Links to Authors
    @ManyToMany
    @JoinTable(name = "registration_author",
            joinColumns = @JoinColumn(name = FOREIGN_KEY_RECORD_PK,
                    referencedColumnName = PRIMARY_KEY_RECORD),
            inverseJoinColumns = @JoinColumn(name = FOREIGN_KEY_AUTHOR_PK,
                    referencedColumnName = PRIMARY_KEY_AUTHOR))
    private List<Author> referenceAuthor = new ArrayList<>();

    // Links to Comments
    @ManyToMany
    @JoinTable(name = TABLE_RECORD_COMMENT,
            joinColumns = @JoinColumn(name = FOREIGN_KEY_RECORD_PK,
                    referencedColumnName = PRIMARY_KEY_RECORD),
            inverseJoinColumns =
            @JoinColumn(name = FOREIGN_KEY_COMMENT_PK,
                    referencedColumnName = PRIMARY_KEY_COMMENT))
    private List<Comment> referenceComment = new ArrayList<>();

    // Links to DocumentDescriptions
    @ManyToMany
    @JoinTable(name = "record_document_description",
            joinColumns = @JoinColumn(name = FOREIGN_KEY_RECORD_PK,
                    referencedColumnName = PRIMARY_KEY_RECORD),
            inverseJoinColumns = @JoinColumn(
                    name = "f_pk_document_description_id",
                    referencedColumnName = PRIMARY_KEY_DOCUMENT_DESCRIPTION))
    private List<DocumentDescription> referenceDocumentDescription =
            new ArrayList<>();

    // Links to CrossReference
    @OneToMany(mappedBy = "referenceRecord")
    private List<CrossReference> referenceCrossReference;

    // Links to Classified
    @ManyToOne(cascade = ALL)
    @JoinColumn(name = "record_classified_id",
            referencedColumnName = "pk_classified_id")
    private Classified referenceClassified;

    // Link to Disposal
    @ManyToOne(cascade = ALL)
    @JoinColumn(name = "record_disposal_id",
            referencedColumnName = "pk_disposal_id")
    private Disposal referenceDisposal;

    // Link to Screening
    @ManyToOne(cascade = ALL)
    @JoinColumn(name = "record_screening_id",
            referencedColumnName = "pk_screening_id")
    private Screening referenceScreening;

    // Links to Party
    @ManyToMany
    @JoinTable(name = TABLE_RECORD_PARTY,
            joinColumns = @JoinColumn(name = FOREIGN_KEY_RECORD_PK,
                    referencedColumnName = PRIMARY_KEY_RECORD),
            inverseJoinColumns = @JoinColumn(name = FOREIGN_KEY_PART_PK,
                    referencedColumnName = PRIMARY_KEY_PART))
    private List<Party> referenceParty = new ArrayList<>();

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getArchivedDate() {
        return archivedDate;
    }

    public void setArchivedDate(ZonedDateTime archivedDate) {
        this.archivedDate = archivedDate;
    }

    public String getArchivedBy() {
        return archivedBy;
    }

    public void setArchivedBy(String archivedBy) {
        this.archivedBy = archivedBy;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOfficialTitle() {
        return officialTitle;
    }

    public void setOfficialTitle(String officialTitle) {
        this.officialTitle = officialTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocumentMedium() {
        return documentMedium;
    }

    public void setDocumentMedium(String documentMedium) {
        this.documentMedium = documentMedium;
    }

    @Override
    public String getOwnedBy() {
        return ownedBy;
    }

    @Override
    public void setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
    }

    @Override
    public String getBaseTypeName() {
        return N5ResourceMappings.RECORD;
    }

    public File getReferenceFile() {
        return referenceFile;
    }

    public void setReferenceFile(File referenceFile) {
        this.referenceFile = referenceFile;
    }

    public Series getReferenceSeries() {
        return referenceSeries;
    }

    public void setReferenceSeries(Series referenceSeries) {
        this.referenceSeries = referenceSeries;
    }

    public Class getReferenceClass() {
        return referenceClass;
    }

    public void setReferenceClass(Class referenceClass) {
        this.referenceClass = referenceClass;
    }

    public List<DocumentDescription> getReferenceDocumentDescription() {
        return referenceDocumentDescription;
    }

    public void setReferenceDocumentDescription(
            List<DocumentDescription> referenceDocumentDescription) {
        this.referenceDocumentDescription = referenceDocumentDescription;
    }

    public void addReferenceDocumentDescription(
            DocumentDescription referenceDocumentDescription) {
        this.referenceDocumentDescription.add(referenceDocumentDescription);
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
    public Screening getReferenceScreening() {
        return referenceScreening;
    }

    @Override
    public void setReferenceScreening(Screening referenceScreening) {
        this.referenceScreening = referenceScreening;
    }

    @Override
    public List<StorageLocation> getReferenceStorageLocation() {
        return referenceStorageLocation;
    }

    public void setReferenceStorageLocation(List<StorageLocation> referenceStorageLocation) {
        this.referenceStorageLocation = referenceStorageLocation;
    }

    public List<Keyword> getReferenceKeyword() {
        return referenceKeyword;
    }

    public void setReferenceKeyword(List<Keyword> referenceKeyword) {
        this.referenceKeyword = referenceKeyword;
    }

    public List<Author> getReferenceAuthor() {
        return referenceAuthor;
    }

    public void setReferenceAuthor(List<Author> referenceAuthor) {
        this.referenceAuthor = referenceAuthor;
    }

    public List<Comment> getReferenceComment() {
        return referenceComment;
    }

    public void setReferenceComment(List<Comment> referenceComment) {
        this.referenceComment = referenceComment;
    }

    @Override
    public List<CrossReference> getReferenceCrossReference() {
        return referenceCrossReference;
    }

    @Override
    public void setReferenceCrossReference(
            List<CrossReference> referenceCrossReference) {
        this.referenceCrossReference = referenceCrossReference;
    }

    @Override
    public void addReferenceCrossReference(CrossReference crossReference) {
        this.referenceCrossReference.add(crossReference);
    }

    public List<Party> getReferenceParty() {
        return referenceParty;
    }

    public void setReferenceParty(List<Party> referenceParty) {
        this.referenceParty = referenceParty;
    }

    public void addReferenceParty(Party party) {
        this.referenceParty.add(party);
    }

    @Override
    public String toString() {
        return "Record{" + super.toString() +
                "archivedBy='" + archivedBy + '\'' +
                ", archivedDate=" + archivedDate +
                ", createdBy='" + createdBy + '\'' +
                ", createdDate=" + createdDate +
                ", description='" + description + '\'' +
                ", officialTitle='" + officialTitle + '\'' +
                ", title='" + title + '\'' +
                ", recordId='" + recordId + '\'' +
                ", ownedBy='" + ownedBy + '\'' +
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
        Record rhs = (Record) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(archivedBy, rhs.archivedBy)
                .append(archivedDate, rhs.archivedDate)
                .append(createdBy, rhs.createdBy)
                .append(createdDate, rhs.createdDate)
                .append(recordId, rhs.recordId)
                .append(title, rhs.title)
                .append(officialTitle, rhs.officialTitle)
                .append(description, rhs.description)
                .append(documentMedium, rhs.documentMedium)
                .append(ownedBy, rhs.ownedBy)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(archivedBy)
                .append(archivedDate)
                .append(createdBy)
                .append(createdDate)
                .append(recordId)
                .append(title)
                .append(officialTitle)
                .append(description)
                .append(documentMedium)
                .append(ownedBy)
                .toHashCode();
    }
}
