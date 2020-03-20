package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart;
import nikita.common.model.noark5.v5.hateoas.RecordHateoas;
import nikita.common.model.noark5.v5.interfaces.IPart;
import nikita.common.model.noark5.v5.interfaces.entities.IRecordEntity;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ICorrespondencePart;
import nikita.common.model.noark5.v5.metadata.DocumentMedium;
import nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier;
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
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = TABLE_RECORD)
@Inheritance(strategy = JOINED)
@JsonDeserialize(using = RecordDeserializer.class)
@HateoasPacker(using = RecordHateoasHandler.class)
@HateoasObject(using = RecordHateoas.class)
public class Record
        extends SystemIdEntity
        implements IRecordEntity, IPart, ICorrespondencePart {

    /**
     * M604 - arkivertDato (xs:dateTime)
     */
    @Column(name = RECORD_ARCHIVED_DATE_ENG)
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    @JsonProperty(RECORD_ARCHIVED_DATE)
    private OffsetDateTime archivedDate;

    /**
     * M605 - arkivertAv (xs:string)
     */
    @Column(name = RECORD_ARCHIVED_BY_ENG)
    @Audited
    @JsonProperty(RECORD_ARCHIVED_BY)
    private String archivedBy;

    /**
     * M004 - registreringsID (xs:string)
     */
    @Column(name = RECORD_ID_ENG)
    @Audited
    @JsonProperty(RECORD_ID)
    private String recordId;

    /**
     * M020 - tittel (xs:string)
     */
    @NotNull
    @Column(name = TITLE_ENG, nullable = false, length = TITLE_LENGTH)
    @Audited
    @JsonProperty(TITLE)
    private String title;

    /**
     * M025 - offentligTittel (xs:string)
     */
    @Column(name = FILE_PUBLIC_TITLE_ENG, length = TITLE_LENGTH)
    @Audited
    @JsonProperty(FILE_PUBLIC_TITLE)
    private String publicTitle;

    /**
     * M021 - beskrivelse (xs:string)
     */
    @Column(name = DESCRIPTION_ENG, length = DESCRIPTION_LENGTH)
    @Audited
    @JsonProperty(DESCRIPTION)
    private String description;

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

    // Link to File
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = RECORD_FILE_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private File referenceFile;

    // Links to Part
    @ManyToMany
    @JoinTable(name = TABLE_RECORD_PART,
            joinColumns = @JoinColumn(
                    name = FOREIGN_KEY_RECORD_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(
                    name = FOREIGN_KEY_PART_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private List<Part> referencePart = new ArrayList<>();

    // Links to CorrespondencePart
    @OneToMany(mappedBy = "referenceRecord",
            cascade = ALL, orphanRemoval = true)
    private List<CorrespondencePart>
            referenceCorrespondencePart = new ArrayList<>();

    // Link to StorageLocation
    @ManyToMany(cascade = PERSIST)
    @JoinTable(name = TABLE_RECORD_STORAGE_LOCATION,
            joinColumns = @JoinColumn(name = FOREIGN_KEY_RECORD_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(
                    name = FOREIGN_KEY_STORAGE_LOCATION_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private List<StorageLocation> referenceStorageLocation =
            new ArrayList<>();

    // Link to Series
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = RECORD_SERIES_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Series referenceSeries;

    // Link to Class
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = RECORD_CLASS_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Class referenceClass;

    // Links to Keywords
    @ManyToMany
    @JoinTable(name = TABLE_RECORD_KEYWORD,
            joinColumns = @JoinColumn(name = FOREIGN_KEY_RECORD_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(name = FOREIGN_KEY_KEYWORD_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private List<Keyword> referenceKeyword = new ArrayList<>();

    // Links to Authors
    @OneToMany(mappedBy = "referenceRecord",
            cascade = ALL, orphanRemoval = true)
    private List<Author> referenceAuthor = new ArrayList<>();

    // Links to Comments
    @ManyToMany
    @JoinTable(name = TABLE_RECORD_COMMENT,
            joinColumns = @JoinColumn(
                    name = FOREIGN_KEY_RECORD_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns =
            @JoinColumn(
                    name = FOREIGN_KEY_COMMENT_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private List<Comment> referenceComment = new ArrayList<>();

    // Links to DocumentDescriptions
    @ManyToMany
    @JoinTable(name = TABLE_RECORD_DOCUMENT_DESCRIPTION,
            joinColumns = @JoinColumn(
                    name = FOREIGN_KEY_RECORD_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(
                    name = FOREIGN_KEY_DOCUMENT_DESCRIPTION_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private List<DocumentDescription> referenceDocumentDescription =
            new ArrayList<>();

    // Links to CrossReference
    @OneToMany(mappedBy = "referenceRecord")
    private List<CrossReference> referenceCrossReference = new ArrayList<>();

    // Links to Classified
    @ManyToOne(cascade = ALL)
    @JoinColumn(name = RECORD_CLASSIFIED_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Classified referenceClassified;

    // Link to Disposal
    @ManyToOne(cascade = ALL)
    @JoinColumn(name = RECORD_DISPOSAL_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Disposal referenceDisposal;

    // Link to Screening
    @ManyToOne(cascade = ALL)
    @JoinColumn(name = "record_screening_id",
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Screening referenceScreening;

    // Links to NationalIdentifiers
    @OneToMany(mappedBy = "referenceRecord")
    private List<NationalIdentifier> referenceNationalIdentifier =
            new ArrayList<>();

    public OffsetDateTime getArchivedDate() {
        return archivedDate;
    }

    public void setArchivedDate(OffsetDateTime archivedDate) {
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

    public String getPublicTitle() {
        return publicTitle;
    }

    public void setPublicTitle(String publicTitle) {
        this.publicTitle = publicTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public String getBaseTypeName() {
        return RECORD;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_RECORD;
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

    @Override
    public void setReferenceStorageLocation(
            List<StorageLocation> referenceStorageLocation) {
        this.referenceStorageLocation = referenceStorageLocation;
    }

    @Override
    public void addReferenceStorageLocation(StorageLocation storageLocation) {
        this.referenceStorageLocation.add(storageLocation);
    }

    @Override
    public List<Keyword> getReferenceKeyword() {
        return referenceKeyword;
    }

    @Override
    public void setReferenceKeyword(List<Keyword> referenceKeyword) {
        this.referenceKeyword = referenceKeyword;
    }

    @Override
    public void addReferenceKeyword(Keyword keyword) {
        this.referenceKeyword.add(keyword);
    }

    @Override
    public List<Author> getReferenceAuthor() {
        return referenceAuthor;
    }

    @Override
    public void setReferenceAuthor(List<Author> referenceAuthor) {
        this.referenceAuthor = referenceAuthor;
    }

    @Override
    public void addReferenceAuthor(Author author) {
        this.referenceAuthor.add(author);
    }

    public List<Comment> getReferenceComment() {
        return referenceComment;
    }

    public void setReferenceComment(List<Comment> referenceComment) {
        this.referenceComment = referenceComment;
    }

    public void addReferenceComment(Comment comment) {
        this.referenceComment.add(comment);
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

    @Override
    public List<Part> getReferencePart() {
        return referencePart;
    }

    @Override
    public void setReferencePart(List<Part> referencePart) {
        this.referencePart = referencePart;
    }

    @Override
    public void addPart(Part part) {
        this.referencePart.add(part);
    }


    @Override
    public List<CorrespondencePart> getReferenceCorrespondencePart() {
        return referenceCorrespondencePart;
    }

    @Override
    public void setReferenceCorrespondencePart(
            List<CorrespondencePart> referenceCorrespondencePart) {
        this.referenceCorrespondencePart = referenceCorrespondencePart;
    }

    @Override
    public void addCorrespondencePart(CorrespondencePart part) {
        this.referenceCorrespondencePart.add(part);
    }

    public List<NationalIdentifier> getReferenceNationalIdentifier() {
        return referenceNationalIdentifier;
    }

    public void setReferenceNationalIdentifier(
            List<NationalIdentifier> referenceNationalIdentifier) {
        this.referenceNationalIdentifier = referenceNationalIdentifier;
    }

    public void addNationalIdentifier(
            NationalIdentifier referenceNationalIdentifier) {
        this.referenceNationalIdentifier.add(referenceNationalIdentifier);
    }

    @Override
    public String toString() {
        return "Record{" + super.toString() +
                "archivedBy='" + archivedBy + '\'' +
                ", archivedDate=" + archivedDate +
                ", description='" + description + '\'' +
                ", publicTitle='" + publicTitle + '\'' +
                ", title='" + title + '\'' +
                ", recordId='" + recordId + '\'' +
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
                .append(recordId, rhs.recordId)
                .append(title, rhs.title)
                .append(publicTitle, rhs.publicTitle)
                .append(description, rhs.description)
                .append(documentMediumCode, rhs.documentMediumCode)
                .append(documentMediumCodeName, rhs.documentMediumCodeName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(archivedBy)
                .append(archivedDate)
                .append(recordId)
                .append(title)
                .append(publicTitle)
                .append(description)
                .append(documentMediumCode)
                .append(documentMediumCodeName)
                .toHashCode();
    }
}
