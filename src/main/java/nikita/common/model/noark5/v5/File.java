package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.hateoas.FileHateoas;
import nikita.common.model.noark5.v5.interfaces.*;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.secondary.*;
import nikita.common.util.deserialisers.FileDeserializer;
import nikita.webapp.hateoas.FileHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

@Entity
@Table(name = TABLE_FILE)
@Inheritance(strategy = JOINED)
@JsonDeserialize(using = FileDeserializer.class)
@HateoasPacker(using = FileHateoasHandler.class)
@HateoasObject(using = FileHateoas.class)
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
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns =
            @JoinColumn(name = FOREIGN_KEY_STORAGE_LOCATION_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private List<StorageLocation> referenceStorageLocation = new ArrayList<>();

    // Links to Keywords
    @ManyToMany
    @JoinTable(name = TABLE_FILE_KEYWORD, joinColumns =
    @JoinColumn(name = FOREIGN_KEY_FILE_PK,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(name = FOREIGN_KEY_KEYWORD_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private List<Keyword> referenceKeyword = new ArrayList<>();

    // Link to parent File
    @ManyToOne(fetch = LAZY)
    private File referenceParentFile;

    // Links to child Files
    @OneToMany(mappedBy = "referenceParentFile")
    private List<File> referenceChildFile = new ArrayList<>();

    // Link to Series
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = FILE_SERIES_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Series referenceSeries;

    // Link to Class
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = FILE_CLASS_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Class referenceClass;

    // Links to Records
    @OneToMany(mappedBy = "referenceFile")
    private List<Record> referenceRecord = new ArrayList<>();

    // Links to Comments
    @ManyToMany(cascade = PERSIST)
    @JoinTable(name = TABLE_FILE_COMMENT,
            joinColumns = @JoinColumn(name = FOREIGN_KEY_FILE_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(name = FOREIGN_KEY_COMMENT_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private List<Comment> referenceComment = new ArrayList<>();

    // Links to Classified
    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = FILE_CLASSIFIED_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    @JsonIgnore
    private Classified referenceClassified;

    // Link to Disposal
    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = FILE_DISPOSAL_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Disposal referenceDisposal;

    // Link to Screening
    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = FILE_SCREENING_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Screening referenceScreening;

    @OneToMany(mappedBy = "referenceFile", cascade = ALL)
    private List<CrossReference> referenceCrossReference = new ArrayList<>();

    // Links to Part
    @ManyToMany
    @JoinTable(name = TABLE_FILE_PARTY,
            joinColumns = @JoinColumn(name = FOREIGN_KEY_FILE_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(name = FOREIGN_KEY_PART_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private List<Part> referencePart = new ArrayList<>();

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
        return FILE;
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
    public void addReferenceStorageLocation(
            StorageLocation storageLocation) {
        this.referenceStorageLocation.add(storageLocation);
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

    @Override
    public void addReferenceComment(Comment comment) {
        this.referenceComment.add(comment);
    }

    public List<Part> getReferencePart() {
        return referencePart;
    }

    public void setReferencePart(List<Part> referencePart) {
        this.referencePart = referencePart;
    }

    public void addReferencePart(Part part) {
        this.referencePart.add(part);
    }

    @Override
    public void addReferenceCrossReference(CrossReference crossReference) {
        this.referenceCrossReference.add(crossReference);
    }

    @Override
    public void createReference(@NotNull INikitaEntity entity,
                                @NotNull String referenceType) {

        if (referenceType.equalsIgnoreCase(NEW_CROSS_REFERENCE)) {
            CrossReference crossReference = new CrossReference();
            crossReference.setFromSystemId(getSystemId());
            crossReference.setToSystemId(entity.getSystemId());

            if (entity.getBaseTypeName().equals(CLASS)) {
                crossReference.setReferenceClass((Class) entity);
                crossReference.setReferenceType(REFERENCE_TO_CLASS);
            } else if (entity.getBaseTypeName().equals(REGISTRATION)) {
                crossReference.setReferenceRecord((Record) entity);
                crossReference.setReferenceType(REFERENCE_TO_REGISTRATION);
            }
            crossReference.setReferenceFile(this);
            referenceCrossReference.add(crossReference);
            ((ICrossReference) entity).
                    addReferenceCrossReference(crossReference);
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
