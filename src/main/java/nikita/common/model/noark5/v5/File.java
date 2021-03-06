package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.bsm.BSMBase;
import nikita.common.model.noark5.v5.hateoas.FileHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IFileEntity;
import nikita.common.model.noark5.v5.metadata.DocumentMedium;
import nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier;
import nikita.common.model.noark5.v5.secondary.*;
import nikita.common.util.deserialisers.BSMDeserialiser;
import nikita.common.util.deserialisers.FileDeserializer;
import nikita.webapp.hateoas.FileHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.*;
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
@Indexed
public class File
        extends NoarkGeneralEntity
        implements IFileEntity {
    /**
     * M003 - mappeID (xs:string)
     */
    @Column(name = FILE_ID_ENG)
    @Audited
    @JsonProperty(FILE_ID)
    @KeywordField
    private String fileId;

    /**
     * M025 - offentligTittel (xs:string)
     */
    @Column(name = FILE_PUBLIC_TITLE_ENG, length = TITLE_LENGTH)
    @Audited
    @JsonProperty(FILE_PUBLIC_TITLE)
    @FullTextField
    private String publicTitle;

    /**
     * M??? - dokumentmedium code (xs:string)
     */
    @Column(name = DOCUMENT_MEDIUM_CODE_ENG)
    @Audited
    private String documentMediumCode;

    /**
     * M300 - dokumentmedium code name (xs:string)
     */
    @Column(name = DOCUMENT_MEDIUM_CODE_NAME_ENG)
    @Audited
    private String documentMediumCodeName;

    // Link to StorageLocation
    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(name = TABLE_FILE_STORAGE_LOCATION,
            joinColumns = @JoinColumn(name = FOREIGN_KEY_FILE_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns =
            @JoinColumn(name = FOREIGN_KEY_STORAGE_LOCATION_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private final Set<StorageLocation> referenceStorageLocation =
            new HashSet<>();

    // Links to Keywords
    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(name = TABLE_FILE_KEYWORD, joinColumns =
    @JoinColumn(name = FOREIGN_KEY_FILE_PK,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(name = FOREIGN_KEY_KEYWORD_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private final Set<Keyword> referenceKeyword = new HashSet<>();

    // Link to parent File
    @ManyToOne(fetch = LAZY)
    private File referenceParentFile;

    // Links to child Files
    @OneToMany(mappedBy = "referenceParentFile")
    private final List<File> referenceChildFile = new ArrayList<>();

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
    private final List<RecordEntity> referenceRecordEntity = new ArrayList<>();

    // Links to Comments
    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(name = TABLE_FILE_COMMENT,
            joinColumns = @JoinColumn(name = FOREIGN_KEY_FILE_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(name = FOREIGN_KEY_COMMENT_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private final Set<Comment> referenceComment = new HashSet<>();

    // Links to Classified
    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = FILE_CLASSIFIED_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    @JsonIgnore
    private Classified referenceClassified;

    // Link to Disposal
    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = FILE_DISPOSAL_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Disposal referenceDisposal;

    // Link to Screening
    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = FILE_SCREENING_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Screening referenceScreening;

    @OneToMany(mappedBy = "referenceFile", cascade = {PERSIST, MERGE, REMOVE})
    private final List<CrossReference> referenceCrossReference =
            new ArrayList<>();

    // Links to Part
    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(name = TABLE_FILE_PARTY,
            joinColumns = @JoinColumn(name = FOREIGN_KEY_FILE_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(name = FOREIGN_KEY_PART_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private final Set<Part> referencePart = new HashSet<>();

    // Links to NationalIdentifiers
    @OneToMany(mappedBy = "referenceFile")
    private final List<NationalIdentifier> referenceNationalIdentifier =
            new ArrayList<>();

    // Links to businessSpecificMetadata (virksomhetsspesifikkeMetadata)
    @OneToMany(mappedBy = "referenceFile", cascade = {PERSIST, MERGE, REMOVE})
    @JsonDeserialize(using = BSMDeserialiser.class)
    @JsonProperty(BSM_DEF)
    private final List<BSMBase> referenceBSMBase = new ArrayList<>();

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getPublicTitle() {
        return publicTitle;
    }

    public void setPublicTitle(String publicTitle) {
        this.publicTitle = publicTitle;
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
        return FILE;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_FILE;
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
    public Set<StorageLocation> getReferenceStorageLocation() {
        return referenceStorageLocation;
    }

    @Override
    public void addReferenceStorageLocation(
            StorageLocation storageLocation) {
        this.referenceStorageLocation.add(storageLocation);
        storageLocation.getReferenceFile().add(this);
    }

    @Override
    public void removeReferenceStorageLocation(
            StorageLocation storageLocation) {
        this.referenceStorageLocation.remove(storageLocation);
        storageLocation.getReferenceFile().remove(this);
    }

    @Override
    public Set<Keyword> getReferenceKeyword() {
        return referenceKeyword;
    }

    @Override
    public void addKeyword(Keyword keyword) {
        this.referenceKeyword.add(keyword);
        keyword.getReferenceFile().add(this);
    }

    @Override
    public void removeKeyword(Keyword keyword) {
        this.referenceKeyword.remove(keyword);
        keyword.getReferenceFile().remove(this);
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

    public void addFile(File file) {
        this.referenceChildFile.add(file);
        file.setReferenceParentFile(file);
    }

    public void removeFile(File file) {
        this.referenceChildFile.remove(file);
        file.setReferenceParentFile(null);
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

    public List<RecordEntity> getReferenceRecordEntity() {
        return referenceRecordEntity;
    }

    public void addRecordEntity(RecordEntity record) {
        referenceRecordEntity.add(record);
        record.setReferenceFile(this);
    }

    public void removeRecordEntity(RecordEntity record) {
        referenceRecordEntity.remove(record);
        record.setReferenceFile(null);
    }

    public Set<Comment> getReferenceComment() {
        return referenceComment;
    }

    @Override
    public void addComment(Comment comment) {
        referenceComment.add(comment);
        comment.getReferenceFile().add(this);
    }

    @Override
    public List<CrossReference> getReferenceCrossReference() {
        return referenceCrossReference;
    }

    @Override
    public Set<Part> getReferencePart() {
        return referencePart;
    }

    @Override
    public void addPart(Part part) {
        referencePart.add(part);
        part.getReferenceFile().add(this);
    }

    public void removePart(Part part) {
        referencePart.remove(part);
        part.getReferenceFile().remove(this);
    }

    @Override
    public void addCrossReference(CrossReference crossReference) {
        referenceCrossReference.add(crossReference);
        crossReference.setReferenceFile(this);
    }

    @Override
    public void removeCrossReference(CrossReference crossReference) {
        referenceCrossReference.remove(crossReference);
        crossReference.setReferenceFile(null);
    }

    public List<NationalIdentifier> getReferenceNationalIdentifier() {
        return referenceNationalIdentifier;
    }

    public void addNationalIdentifier(
            NationalIdentifier nationalIdentifier) {
        referenceNationalIdentifier.add(nationalIdentifier);
        nationalIdentifier.setReferenceFile(this);
    }

    public void removeNationalIdentifier(
            NationalIdentifier nationalIdentifier) {
        referenceNationalIdentifier.remove(nationalIdentifier);
        nationalIdentifier.setReferenceFile(null);
    }

    public List<BSMBase> getReferenceBSMBase() {
        return referenceBSMBase;
    }

    @Override
    public void addReferenceBSMBase(List<BSMBase> referenceBSMBase) {
        this.referenceBSMBase.addAll(referenceBSMBase);
        for (BSMBase bsm : referenceBSMBase) {
            bsm.setReferenceFile(this);
        }
    }

    @Override
    public void addBSMBase(BSMBase bsmBase) {
        this.referenceBSMBase.add(bsmBase);
        bsmBase.setReferenceFile(this);
    }

    @Override
    public void removeBSMBase(BSMBase bsmBase) {
        this.referenceBSMBase.remove(bsmBase);
        bsmBase.setReferenceFile(null);
    }

    @Override
    public String toString() {
        return "File{" + super.toString() +
                ", documentMediumCode='" + documentMediumCode + '\'' +
                ", documentMediumCodeName='" + documentMediumCodeName + '\'' +
                ", publicTitle='" + publicTitle + '\'' +
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
                .append(documentMediumCode, rhs.documentMediumCode)
                .append(documentMediumCodeName, rhs.documentMediumCodeName)
                .append(publicTitle, rhs.publicTitle)
                .append(fileId, rhs.fileId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(documentMediumCode)
                .append(documentMediumCodeName)
                .append(publicTitle)
                .append(fileId)
                .toHashCode();
    }
}
