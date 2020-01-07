package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.hateoas.DocumentDescriptionHateoas;
import nikita.common.model.noark5.v5.interfaces.*;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkCreateEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkTitleDescriptionEntity;
import nikita.common.model.noark5.v5.secondary.*;
import nikita.common.util.deserialisers.DocumentDescriptionDeserializer;
import nikita.webapp.hateoas.DocumentDescriptionHateoasHandler;
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
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_DESCRIPTION;
import static nikita.common.config.N5ResourceMappings.DOCUMENT_DESCRIPTION_EXTERNAL_REFERENCE_ENG;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = TABLE_DOCUMENT_DESCRIPTION)
@JsonDeserialize(using = DocumentDescriptionDeserializer.class)
@HateoasPacker(using = DocumentDescriptionHateoasHandler.class)
@HateoasObject(using = DocumentDescriptionHateoas.class)
public class DocumentDescription
        extends NoarkEntity
        implements INoarkTitleDescriptionEntity, INoarkCreateEntity,
        IDocumentMedium, ISingleStorageLocation, IDeletion, IScreening,
        IDisposal, IClassified, IDisposalUndertaken, IComment,
        IElectronicSignature, IAuthor {

    private static final long serialVersionUID = 1L;

    /**
     * M083 - dokumenttype (xs:string)
     */
    @NotNull
    @Column(name = "document_type", nullable = false)
    @Audited
    private String documentType;

    /**
     * M054 - dokumentstatus (xs:string)
     */
    @Column(name = "document_status")
    @Audited
    private String documentStatus;

    /**
     * M020 - tittel (xs:string)
     */
    @NotNull
    @Column(name = "title", nullable = false)
    @Audited
    private String title;

    /**
     * M021 - beskrivelse (xs:string)
     */
    @Column(name = "description")
    @Audited
    private String description;

    /**
     * M600 - opprettetDato (xs:dateTime)
     */
    @Column(name = "created_date")
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    private OffsetDateTime createdDate;

    /**
     * M601 - opprettetAv (xs:string)
     */
    @Column(name = "created_by")
    @Audited
    private String createdBy;

    /**
     * M300 - dokumentmedium (xs:string)
     */
    @Column(name = "document_medium")
    @Audited
    private String documentMedium;

    /**
     * M217 - tilknyttetRegistreringSom (xs:string)
     */
    @NotNull
    @Column(name = "associated_with_record_as", nullable = false)
    @Audited
    private String associatedWithRecordAs;

    /**
     * M007 - dokumentnummer (xs:integer)
     */
    @NotNull
    @Column(name = "document_number")
    @Audited
    private Integer documentNumber;

    /**
     * M620 - tilknyttetDato (xs:date)
     */
    @NotNull
    @Column(name = "association_date", nullable = false)
    @DateTimeFormat(iso = DATE)
    @Audited
    private OffsetDateTime associationDate;

    /**
     * M621 - tilknyttetAv (xs:string)
     */
    @Column(name = "associated_by")
    @Audited
    private String associatedBy;

    @Column(name = "storage_location")
    @Audited
    private String storageLocation;

    /**
     * M??? - eksternReferanse (xs:string)
     */
    @Column(name = DOCUMENT_DESCRIPTION_EXTERNAL_REFERENCE_ENG)
    @Audited
    private String externalReference;

    // Links to Records
    @ManyToMany(mappedBy = "referenceDocumentDescription")
    private List<Record> referenceRecord = new ArrayList<>();

    // Links to DocumentObjects
    @OneToMany(mappedBy = "referenceDocumentDescription")
    private List<DocumentObject> referenceDocumentObject = new ArrayList<>();

    // Links to Comments
    @ManyToMany
    @JoinTable(name = TABLE_DOCUMENT_DESCRIPTION_COMMENT,
            joinColumns = @JoinColumn(
                    name = FOREIGN_KEY_DOCUMENT_DESCRIPTION_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(
                    name = FOREIGN_KEY_COMMENT_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private List<Comment> referenceComment = new ArrayList<>();

    // Links to Authors
    @OneToMany(mappedBy = "referenceDocumentDescription",
            cascade = ALL, orphanRemoval = true)
    private List<Author> referenceAuthor = new ArrayList<>();

    // Link to Classified
    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = DOCUMENT_DESCRIPTION_CLASSIFIED_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Classified referenceClassified;

    // Link to Disposal
    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = DOCUMENT_DESCRIPTION_DISPOSAL_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Disposal referenceDisposal;

    // Link to DisposalUndertaken
    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = DOCUMENT_DESCRIPTION_DISPOSAL_UNDERTAKEN_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private DisposalUndertaken referenceDisposalUndertaken;

    // Link to Deletion
    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = DOCUMENT_DESCRIPTION_DELETION_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Deletion referenceDeletion;

    // Link to Screening
    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = DOCUMENT_DESCRIPTION_SCREENING_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Screening referenceScreening;

    // Link to ElectronicSignature
    @OneToOne
    @JoinColumn(name = PRIMARY_KEY_SYSTEM_ID)
    private ElectronicSignature referenceElectronicSignature;

    // Links to Part
    @ManyToMany
    @JoinTable(name = TABLE_DOCUMENT_DESCRIPTION_PARTY,
            joinColumns = @JoinColumn(
                    name = FOREIGN_KEY_DOCUMENT_DESCRIPTION_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(
                    name = FOREIGN_KEY_PART_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private List<Part> referencePart = new ArrayList<>();

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
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

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(OffsetDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDocumentMedium() {
        return documentMedium;
    }

    public void setDocumentMedium(String documentMedium) {
        this.documentMedium = documentMedium;
    }

    public String getAssociatedWithRecordAs() {
        return associatedWithRecordAs;
    }

    public void setAssociatedWithRecordAs(String associatedWithRecordAs) {
        this.associatedWithRecordAs = associatedWithRecordAs;
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

    public List<Record> getReferenceRecord() {
        return referenceRecord;
    }

    public void setReferenceRecord(List<Record> referenceRecord) {
        this.referenceRecord = referenceRecord;
    }

    public void addReferenceRecord(Record record) {
        this.referenceRecord.add(record);
    }

    public List<DocumentObject> getReferenceDocumentObject() {
        return referenceDocumentObject;
    }

    public void setReferenceDocumentObject(
            List<DocumentObject> referenceDocumentObject) {
        this.referenceDocumentObject = referenceDocumentObject;
    }

    public void addReferenceDocumentObject(DocumentObject documentObject) {
        referenceDocumentObject.add(documentObject);
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
    public List<Comment> getReferenceComment() {
        return referenceComment;
    }

    @Override
    public void setReferenceComment(List<Comment> referenceComment) {
        this.referenceComment = referenceComment;
    }

    @Override
    public void addReferenceComment(Comment comment) {
        this.referenceComment.add(comment);
    }

    @Override
    public List<Author> getReferenceAuthor() {
        return referenceAuthor;
    }

    @Override
    public void setReferenceAuthor(List<Author> referenceAuthor) {
        this.referenceAuthor = referenceAuthor;
    }

    public void addReferenceAuthor(Author author) {
        referenceAuthor.add(author);
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
    public ElectronicSignature getReferenceElectronicSignature() {
        return referenceElectronicSignature;
    }

    @Override
    public void setReferenceElectronicSignature(
            ElectronicSignature referenceElectronicSignature) {
        this.referenceElectronicSignature = referenceElectronicSignature;
    }

    public List<Part> getReferencePart() {
        return referencePart;
    }

    public void setReferencePart(List<Part> referencePart) {
        this.referencePart = referencePart;
    }

    public void addPart(Part part) {
        this.referencePart.add(part);
    }

    @Override
    public String toString() {
        return "DocumentDescription{" + super.toString() +
                "associatedBy='" + associatedBy + '\'' +
                ", associationDate=" + associationDate +
                ", documentNumber=" + documentNumber +
                ", associatedWithRecordAs='" + associatedWithRecordAs + '\'' +
                ", documentMedium='" + documentMedium + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdDate=" + createdDate +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                "  externalReference='" + externalReference + '\'' +
                ", documentStatus='" + documentStatus + '\'' +
                ", documentType='" + documentType + '\'' +
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
                .append(associatedWithRecordAs, rhs.associatedWithRecordAs)
                .append(documentNumber, rhs.documentNumber)
                .append(documentMedium, rhs.documentMedium)
                .append(documentStatus, rhs.documentStatus)
                .append(documentType, rhs.documentType)
                .append(description, rhs.description)
                .append(createdDate, rhs.createdDate)
                .append(createdBy, rhs.createdBy)
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
                .append(associatedWithRecordAs)
                .append(documentNumber)
                .append(documentMedium)
                .append(documentStatus)
                .append(documentType)
                .append(description)
                .append(createdDate)
                .append(createdBy)
                .append(title)
                .append(externalReference)
                .toHashCode();
    }
}
