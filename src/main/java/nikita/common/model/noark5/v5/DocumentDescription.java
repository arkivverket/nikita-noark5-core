package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.bsm.BSMBase;
import nikita.common.model.noark5.v5.hateoas.DocumentDescriptionHateoas;
import nikita.common.model.noark5.v5.interfaces.*;
import nikita.common.model.noark5.v5.interfaces.entities.ICreate;
import nikita.common.model.noark5.v5.interfaces.entities.ITitleDescription;
import nikita.common.model.noark5.v5.metadata.AssociatedWithRecordAs;
import nikita.common.model.noark5.v5.metadata.DocumentMedium;
import nikita.common.model.noark5.v5.metadata.DocumentStatus;
import nikita.common.model.noark5.v5.metadata.DocumentType;
import nikita.common.model.noark5.v5.secondary.*;
import nikita.common.util.deserialisers.DocumentDescriptionDeserializer;
import nikita.webapp.hateoas.DocumentDescriptionHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = TABLE_DOCUMENT_DESCRIPTION)
@JsonDeserialize(using = DocumentDescriptionDeserializer.class)
@HateoasPacker(using = DocumentDescriptionHateoasHandler.class)
@HateoasObject(using = DocumentDescriptionHateoas.class)
@Indexed
public class DocumentDescription
        extends SystemIdEntity
        implements ICreate, ITitleDescription,
        IDocumentMedium, ISingleStorageLocation, IDeletion, IScreening,
        IDisposal, IClassified, IDisposalUndertaken, IComment,
        IElectronicSignature, IAuthor, IBSM {

    private static final long serialVersionUID = 1L;

    /**
     * M??? - dokumenttype code (xs:string)
     */
    @NotNull
    @Column(name = DOCUMENT_TYPE_CODE_ENG, nullable = false)
    @JsonProperty(DOCUMENT_TYPE_CODE)
    @Audited
    private String documentTypeCode;

    /**
     * M083 - dokumenttype code name (xs:string)
     */
    @NotNull
    @Column(name = DOCUMENT_TYPE_CODE_NAME_ENG, nullable = false)
    @JsonProperty(DOCUMENT_TYPE_CODE_NAME)
    @Audited
    private String documentTypeCodeName;

    /**
     * M??? - dokumentstatus code (xs:string)
     */
    @Column(name = DOCUMENT_STATUS_CODE_ENG)
    @JsonProperty(DOCUMENT_STATUS_CODE)
    @Audited
    private String documentStatusCode;

    /**
     * M054 - dokumentstatus code name (xs:string)
     */
    @Column(name = DOCUMENT_STATUS_CODE_NAME_ENG)
    @JsonProperty(DOCUMENT_STATUS_CODE_NAME)

    @Audited
    private String documentStatusCodeName;

    /**
     * M020 - tittel (xs:string)
     */
    @NotNull
    @Column(name = TITLE_ENG, nullable = false, length = TITLE_LENGTH)
    @Audited
    @JsonProperty(TITLE)
    @FullTextField
    private String title;

    /**
     * M021 - beskrivelse (xs:string)
     */
    @Column(name = DESCRIPTION_ENG, length = DESCRIPTION_LENGTH)
    @Audited
    @JsonProperty(DESCRIPTION)
    @FullTextField
    private String description;

    /**
     * M??? - dokumentmedium code (xs:string)
     */
    @Column(name = DOCUMENT_MEDIUM_CODE_ENG)
    @JsonProperty(DOCUMENT_MEDIUM_CODE)
    @Audited
    private String documentMediumCode;

    /**
     * M300 - dokumentmedium code name (xs:string)
     */
    @Column(name = DOCUMENT_MEDIUM_CODE_NAME_ENG)
    @JsonProperty(DOCUMENT_MEDIUM_CODE_NAME)
    @Audited
    private String documentMediumCodeName;

    /**
     * M??? - tilknyttetRegistreringSom code (xs:string)
     */
    @NotNull
    @Column(name = ASSOCIATED_WITH_RECORD_AS_CODE_ENG, nullable = false)
    @JsonProperty(ASSOCIATED_WITH_RECORD_AS_CODE)
    @Audited
    private String associatedWithRecordAsCode;

    /**
     * M217 - tilknyttetRegistreringSom code name (xs:string)
     */
    @NotNull
    @Column(name = ASSOCIATED_WITH_RECORD_AS_CODE_NAME_ENG, nullable = false)
    @JsonProperty(ASSOCIATED_WITH_RECORD_AS_CODE_NAME)
    @Audited
    private String associatedWithRecordAsCodeName;

    /**
     * M007 - dokumentnummer (xs:integer)
     */
    @NotNull
    @Column(name = DOCUMENT_DESCRIPTION_DOCUMENT_NUMBER_ENG)
    @Audited
    @JsonProperty(DOCUMENT_DESCRIPTION_DOCUMENT_NUMBER)
    private Integer documentNumber;

    /**
     * M620 - tilknyttetDato (xs:date)
     */
    @NotNull
    @Column(name = DOCUMENT_DESCRIPTION_ASSOCIATED_DATE_ENG)
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    @JsonProperty(DOCUMENT_DESCRIPTION_ASSOCIATED_DATE)
    @GenericField
    private OffsetDateTime associationDate;

    /**
     * M621 - tilknyttetAv (xs:string)
     */
    @Column(name = DOCUMENT_DESCRIPTION_ASSOCIATED_BY_ENG)
    @Audited
    @JsonProperty(DOCUMENT_DESCRIPTION_ASSOCIATED_BY)
    @KeywordField
    private String associatedBy;

    /**
     * M301 - oppbevaringssted (xs:string)
     */
    @Column(name = STORAGE_LOCATION_ENG)
    @Audited
    @JsonProperty(STORAGE_LOCATION)
    @FullTextField
    private String storageLocation;

    /**
     * M??? - eksternReferanse (xs:string)
     */
    @Column(name = DOCUMENT_DESCRIPTION_EXTERNAL_REFERENCE_ENG)
    @Audited
    @JsonProperty(DOCUMENT_DESCRIPTION_EXTERNAL_REFERENCE)
    @FullTextField
    private String externalReference;

    // Links to Records
    @ManyToMany(mappedBy = "referenceDocumentDescription")
    private final Set<Record> referenceRecord = new HashSet<>();

    // Links to DocumentObjects
    @OneToMany(mappedBy = "referenceDocumentDescription")
    private final List<DocumentObject> referenceDocumentObject =
            new ArrayList<>();

    // Links to Comments
    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(name = TABLE_DOCUMENT_DESCRIPTION_COMMENT,
            joinColumns = @JoinColumn(
                    name = FOREIGN_KEY_DOCUMENT_DESCRIPTION_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(
                    name = FOREIGN_KEY_COMMENT_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private final Set<Comment> referenceComment = new HashSet<>();

    // Links to Authors
    @OneToMany(mappedBy = "referenceDocumentDescription")
    private final List<Author> referenceAuthor = new ArrayList<>();

    // Link to Classified
    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = DOCUMENT_DESCRIPTION_CLASSIFIED_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Classified referenceClassified;

    // Link to Disposal
    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = DOCUMENT_DESCRIPTION_DISPOSAL_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Disposal referenceDisposal;

    // Link to DisposalUndertaken
    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = DOCUMENT_DESCRIPTION_DISPOSAL_UNDERTAKEN_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private DisposalUndertaken referenceDisposalUndertaken;

    // Link to Deletion
    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = DOCUMENT_DESCRIPTION_DELETION_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Deletion referenceDeletion;

    // Link to Screening
    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = DOCUMENT_DESCRIPTION_SCREENING_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Screening referenceScreening;

    // Link to ElectronicSignature
    @OneToOne(mappedBy = REFERENCE_DOCUMENT_DESCRIPTION_DB, fetch = LAZY,
            cascade = ALL)
    private ElectronicSignature referenceElectronicSignature;

    // Links to Part
    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(name = TABLE_DOCUMENT_DESCRIPTION_PARTY,
            joinColumns = @JoinColumn(
                    name = FOREIGN_KEY_DOCUMENT_DESCRIPTION_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(
                    name = FOREIGN_KEY_PART_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private final Set<Part> referencePart = new HashSet<>();

    // Links to businessSpecificMetadata (virksomhetsspesifikkeMetadata)
    @OneToMany(mappedBy = REFERENCE_DOCUMENT_DESCRIPTION_DB,
            cascade = {PERSIST, MERGE, REMOVE})
    private final List<BSMBase> referenceBSMBase = new ArrayList<>();

    public DocumentType getDocumentType() {
        if (null == documentTypeCode)
            return null;
        return new DocumentType(documentTypeCode, documentTypeCodeName);
    }

    public void setDocumentType(DocumentType documentType) {
        if (null != documentType) {
            this.documentTypeCode = documentType.getCode();
            this.documentTypeCodeName = documentType.getCodeName();
        } else {
            this.documentTypeCode = null;
            this.documentTypeCodeName = null;
        }
    }

    public DocumentStatus getDocumentStatus() {
        if (null == documentStatusCode)
            return null;
        return new DocumentStatus(documentStatusCode, documentStatusCodeName);
    }

    public void setDocumentStatus(DocumentStatus documentStatus) {
        if (null != documentStatus) {
            this.documentStatusCode = documentStatus.getCode();
            this.documentStatusCodeName = documentStatus.getCodeName();
        } else {
            this.documentStatusCode = null;
            this.documentStatusCodeName = null;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public AssociatedWithRecordAs getAssociatedWithRecordAs() {
        if (null == associatedWithRecordAsCode)
            return null;
        return new AssociatedWithRecordAs(associatedWithRecordAsCode,
                                          associatedWithRecordAsCodeName);
    }

    public void setAssociatedWithRecordAs(AssociatedWithRecordAs associatedWithRecordAs) {
        if (null != associatedWithRecordAs) {
            this.associatedWithRecordAsCode =
                associatedWithRecordAs.getCode();
            this.associatedWithRecordAsCodeName =
                associatedWithRecordAs.getCodeName();
        } else {
            this.associatedWithRecordAsCode = null;
            this.associatedWithRecordAsCodeName = null;
        }
    }

    @Override
    public String getBaseTypeName() {
        return DOCUMENT_DESCRIPTION;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_DOCUMENT_DESCRIPTION;
    }

    public Integer getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(Integer documentNumber) {
        this.documentNumber = documentNumber;
    }

    public OffsetDateTime getAssociationDate() {
        return associationDate;
    }

    public void setAssociationDate(OffsetDateTime associationDate) {
        this.associationDate = associationDate;
    }

    public String getAssociatedBy() {
        return associatedBy;
    }

    public void setAssociatedBy(String associatedBy) {
        this.associatedBy = associatedBy;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public Set<Record> getReferenceRecord() {
        return referenceRecord;
    }

    public void addRecord(Record record) {
        this.referenceRecord.add(record);
        record.addDocumentDescription(this);
    }

    public void removeRecord(Record record) {
        this.referenceRecord.remove(record);
        record.addDocumentDescription(this);
    }

    public List<DocumentObject> getReferenceDocumentObject() {
        return referenceDocumentObject;
    }

    public void addDocumentObject(DocumentObject documentObject) {
        referenceDocumentObject.add(documentObject);
        documentObject.setReferenceDocumentDescription(this);
    }

    public void removeDocumentObject(DocumentObject documentObject) {
        referenceDocumentObject.remove(documentObject);
        documentObject.setReferenceDocumentDescription(null);
    }

    @Override
    public String getStorageLocation() {
        return storageLocation;
    }

    @Override
    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    @Override
    public Set<Comment> getReferenceComment() {
        return referenceComment;
    }

    @Override
    public void addComment(Comment comment) {
        this.referenceComment.add(comment);
        comment.getReferenceDocumentDescription().add(this);
    }

    @Override
    public List<Author> getReferenceAuthor() {
        return referenceAuthor;
    }

    @Override
    public void addAuthor(Author author) {
        referenceAuthor.add(author);
        author.setReferenceDocumentDescription(this);
    }

    @Override
    public void removeAuthor(Author author) {
        referenceAuthor.remove(author);
        author.setReferenceDocumentDescription(null);
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
    public void setDisposalUndertaken(DisposalUndertaken disposalUndertaken) {
        this.referenceDisposalUndertaken = disposalUndertaken;
    }

    public void removeDisposalUndertaken() {
        this.referenceDisposalUndertaken = null;
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
    public ElectronicSignature getReferenceElectronicSignature() {
        return referenceElectronicSignature;
    }

    @Override
    public void setReferenceElectronicSignature(
            ElectronicSignature referenceElectronicSignature) {
        this.referenceElectronicSignature = referenceElectronicSignature;
    }

    public Set<Part> getReferencePart() {
        return referencePart;
    }

    public void addPart(Part part) {
        this.referencePart.add(part);
        part.getReferenceDocumentDescription().add(this);
    }

    public void removePart(Part part) {
        this.referencePart.remove(part);
        part.getReferenceDocumentDescription().remove(this);
    }

    @Override
    public void addBSMBase(BSMBase bsmBase) {
        this.referenceBSMBase.add(bsmBase);
        bsmBase.setReferenceDocumentDescription(this);
    }

    @Override
    public void addReferenceBSMBase(List<BSMBase> referenceBSMBase) {
        this.referenceBSMBase.addAll(referenceBSMBase);
        for (BSMBase bsm : referenceBSMBase) {
            bsm.setReferenceDocumentDescription(this);
        }
    }

    @Override
    public void removeBSMBase(BSMBase bSMBase) {

    }

    public List<BSMBase> getReferenceBSMBase() {
        return referenceBSMBase;
    }

    @Override
    public String toString() {
        return "DocumentDescription{" + super.toString() +
                "associatedBy='" + associatedBy + '\'' +
                ", associationDate=" + associationDate +
                ", documentNumber=" + documentNumber +
                ", associatedWithRecordAsCode='" + associatedWithRecordAsCode + '\'' +
                ", associatedWithRecordAsCodeName='" + associatedWithRecordAsCodeName + '\'' +
                ", documentMediumCode='" + documentMediumCode + '\'' +
                ", documentMediumCodeName='" + documentMediumCodeName + '\'' +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                "  externalReference='" + externalReference + '\'' +
                ", documentStatusCode='" + documentStatusCode + '\'' +
                ", documentStatusCodeName='" + documentStatusCodeName + '\'' +
                ", documentTypeCode='" + documentTypeCode + '\'' +
                ", documentTypeCodeName='" + documentTypeCodeName + '\'' +
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
        DocumentDescription rhs = (DocumentDescription) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(associatedBy, rhs.associatedBy)
                .append(associationDate, rhs.associationDate)
                .append(associatedWithRecordAsCode, rhs.associatedWithRecordAsCode)
                .append(associatedWithRecordAsCodeName, rhs.associatedWithRecordAsCodeName)
                .append(documentNumber, rhs.documentNumber)
                .append(documentMediumCode, rhs.documentMediumCode)
                .append(documentMediumCodeName, rhs.documentMediumCodeName)
                .append(documentStatusCode, rhs.documentStatusCode)
                .append(documentStatusCodeName, rhs.documentStatusCodeName)
                .append(documentTypeCode, rhs.documentTypeCode)
                .append(documentTypeCodeName, rhs.documentTypeCodeName)
                .append(description, rhs.description)
                .append(title, rhs.title)
                .append(externalReference, rhs.externalReference)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(associatedBy)
                .append(associationDate)
                .append(associatedWithRecordAsCode)
                .append(associatedWithRecordAsCodeName)
                .append(documentNumber)
                .append(documentMediumCode)
                .append(documentMediumCodeName)
                .append(documentStatusCode)
                .append(documentStatusCodeName)
                .append(documentTypeCode)
                .append(documentTypeCodeName)
                .append(description)
                .append(title)
                .append(externalReference)
                .toHashCode();
    }
}
