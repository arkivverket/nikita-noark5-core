package nikita.common.model.noark5.v4;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v4.hateoas.FileHateoas;
import nikita.common.model.noark5.v4.interfaces.*;
import nikita.common.model.noark5.v4.secondary.*;
import nikita.common.util.deserialisers.FileDeserializer;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.FileHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static nikita.common.config.Constants.*;

@Entity
@Table(name = "file")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonDeserialize(using = FileDeserializer.class)
@HateoasPacker(using = FileHateoasHandler.class)
@HateoasObject(using = FileHateoas.class)
@AttributeOverride(name = "id", column = @Column(name = PRIMARY_KEY_FILE))
public class File
        extends NoarkGeneralEntity
        implements IDocumentMedium, IStorageLocation, IKeyword, IClassified,
        IDisposal, IScreening, IComment, ICrossReference {
    /**
     * M003 - mappeID (xs:string)
     */
    @Column(name = "file_id")
    @Audited
    private String fileId;

    /**
     * M025 - offentligTittel (xs:string)
     */
    @Column(name = "official_title")
    @Audited
    private String officialTitle;

    /**
     * M300 - dokumentmedium (xs:string)
     */
    @Column(name = "document_medium")
    @Audited
    private String documentMedium;

    // Link to StorageLocation
    @ManyToMany
    @JoinTable(name = TABLE_FILE_STORAGE_LOCATION,
            joinColumns = @JoinColumn(name = FOREIGN_KEY_FILE_PK,
                    referencedColumnName = PRIMARY_KEY_FILE),
            inverseJoinColumns =
            @JoinColumn(name = FOREIGN_KEY_STORAGE_LOCATION_PK,
                    referencedColumnName = PRIMARY_KEY_STORAGE_LOCATION))
    private List<StorageLocation> referenceStorageLocation = new ArrayList<>();

    // Links to Keywords
    @ManyToMany
    @JoinTable(name = TABLE_FILE_KEYWORD, joinColumns =
    @JoinColumn(name = FOREIGN_KEY_FILE_PK,
            referencedColumnName = PRIMARY_KEY_FILE),
            inverseJoinColumns = @JoinColumn(name = FOREIGN_KEY_KEYWORD_PK,
                    referencedColumnName = PRIMARY_KEY_KEYWORD))
    private List<Keyword> referenceKeyword = new ArrayList<>();

    // Link to parent File
    @ManyToOne(fetch = FetchType.LAZY)
    private File referenceParentFile;

    // Links to child Files
    @OneToMany(mappedBy = "referenceParentFile")
    private List<File> referenceChildFile = new ArrayList<>();

    // Link to Series
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = FILE_SERIES_ID,
            referencedColumnName = PRIMARY_KEY_SERIES)
    private Series referenceSeries;

    // Link to Class
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = FILE_CLASS_ID,
            referencedColumnName = PRIMARY_KEY_CLASS)
    private Class referenceClass;

    // Links to Records
    @OneToMany(mappedBy = "referenceFile")
    private List<Record> referenceRecord = new ArrayList<>();

    // Links to Comments
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = TABLE_FILE_COMMENT,
            joinColumns = @JoinColumn(name = FOREIGN_KEY_FILE_PK,
                    referencedColumnName = PRIMARY_KEY_FILE),
            inverseJoinColumns = @JoinColumn(name = FOREIGN_KEY_COMMENT_PK,
                    referencedColumnName = PRIMARY_KEY_COMMENT))
    private List<Comment> referenceComment = new ArrayList<>();

    // Links to Classified
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = FILE_CLASSIFIED_ID,
            referencedColumnName = PRIMARY_KEY_CLASSIFIED)
    @JsonIgnore
    private Classified referenceClassified;

    // Link to Disposal
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = FILE_DISPOSAL_ID,
            referencedColumnName = PRIMARY_KEY_DISPOSAL)
    private Disposal referenceDisposal;

    // Link to Screening
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = FILE_SCREENING_ID,
            referencedColumnName = PRIMARY_KEY_SCREENING)
    private Screening referenceScreening;

    @OneToMany(mappedBy = "referenceFile")
    private List<CrossReference> referenceCrossReference;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getOfficialTitle() {
        return officialTitle;
    }

    public void setOfficialTitle(String officialTitle) {
        this.officialTitle = officialTitle;
    }

    public String getDocumentMedium() {
        return documentMedium;
    }

    public void setDocumentMedium(String documentMedium) {
        this.documentMedium = documentMedium;
    }

    @Override
    public String getBaseTypeName() {
        return N5ResourceMappings.FILE;
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

    public List<Keyword> getReferenceKeyword() {
        return referenceKeyword;
    }

    public void setReferenceKeyword(List<Keyword> referenceKeyword) {
        this.referenceKeyword = referenceKeyword;
    }

    public File getReferenceParentFile() {
        return referenceParentFile;
    }

    public void setReferenceParentFile(File referenceParentFile) {
        this.referenceParentFile = referenceParentFile;
    }

    public List<File> getReferenceChildFile() {
        return referenceChildFile;
    }

    public void setReferenceChildFile(List<File> referenceChildFile) {
        this.referenceChildFile = referenceChildFile;
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

    public List<Record> getReferenceRecord() {
        return referenceRecord;
    }

    public void setReferenceRecord(List<Record> referenceRecord) {
        this.referenceRecord = referenceRecord;
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

    /**
     * Identify who is the parent of this object.
     */
    public NoarkEntity chooseParent() {
        if (null != referenceParentFile) {
            return referenceParentFile;
        } else if (null != referenceClass) {
            return referenceClass;
        } else if (null != referenceSeries) {
            return referenceSeries;
        } else { // This should be impossible,
            // a File cannot exist without a parent
            throw new NoarkEntityNotFoundException(
                    "Could not find parent object for " + this.toString());
        }
    }

    @Override
    public String toString() {
        return "File{" + super.toString() +
                ", documentMedium='" + documentMedium + '\'' +
                ", officialTitle='" + officialTitle + '\'' +
                ", fileId='" + fileId + '\'' +
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
        File rhs = (File) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(documentMedium, rhs.documentMedium)
                .append(officialTitle, rhs.officialTitle)
                .append(fileId, rhs.fileId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(documentMedium)
                .append(officialTitle)
                .append(fileId)
                .toHashCode();
    }
}
