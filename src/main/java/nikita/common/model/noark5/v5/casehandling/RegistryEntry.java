package nikita.common.model.noark5.v5.casehandling;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.config.Constants;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.BasicRecord;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartInternal;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit;
import nikita.common.model.noark5.v5.hateoas.casehandling.RegistryEntryHateoas;
import nikita.common.model.noark5.v5.interfaces.IDocumentFlow;
import nikita.common.model.noark5.v5.interfaces.IElectronicSignature;
import nikita.common.model.noark5.v5.interfaces.IPrecedence;
import nikita.common.model.noark5.v5.interfaces.ISignOff;
import nikita.common.model.noark5.v5.secondary.ElectronicSignature;
import nikita.common.model.noark5.v5.secondary.SignOff;
import nikita.common.util.deserialisers.casehandling.RegistryEntryDeserializer;
import nikita.webapp.hateoas.casehandling.RegistryEntryHateoasHandler;
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

import static nikita.common.config.Constants.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = "registry_entry")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonDeserialize(using = RegistryEntryDeserializer.class)
@HateoasPacker(using = RegistryEntryHateoasHandler.class)
@HateoasObject(using = RegistryEntryHateoas.class)
public class RegistryEntry
        extends BasicRecord implements
        IElectronicSignature,
        IPrecedence, /*ICorrespondencePart,*/ ISignOff, IDocumentFlow {

    /**
     * M013 - journalaar (xs:integer)
     */
    @Column(name = "record_year")
    @Audited
    private Integer recordYear;

    /**
     * M014 - journalsekvensnummer (xs:integer)
     */
    @Column(name = "record_sequence_number")
    @Audited
    private Integer recordSequenceNumber;

    /**
     * M015 - journalpostnummer (xs:integer)
     */
    @Column(name = "registry_entry_number")
    @Audited
    private Integer registryEntryNumber;

    /**
     * M082 - journalposttype (xs:string)
     */
    @NotNull
    @Column(name = "registry_entry_type", nullable = false)
    @Audited
    private String registryEntryType;

    /**
     * M053 - journalstatus (xs:string, nullable = false)
     */
    @NotNull
    @Column(name = "record_status")
    @Audited
    private String recordStatus;

    /**
     * M101 - journaldato (xs:date)
     */
    @NotNull
    @Column(name = "record_date", nullable = false)
    @DateTimeFormat(iso = DATE)
    @Audited
    private ZonedDateTime recordDate;

    /**
     * M103 - dokumentetsDato (xs:date)
     */
    @Column(name = "document_date")
    @DateTimeFormat(iso = DATE)
    @Audited
    private ZonedDateTime documentDate;

    /**
     * M104 - mottattDato (xs:dateTime)
     */
    @Column(name = "received_date")
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    private ZonedDateTime receivedDate;

    /**
     * M105 - sendtDato (xs:dateTime)
     */
    @Column(name = "sent_date")
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    private ZonedDateTime sentDate;

    /**
     * M109 - forfallsdato (xs:date)
     */
    @Column(name = "due_date")
    @DateTimeFormat(iso = DATE)
    @Audited
    private ZonedDateTime dueDate;

    /**
     * M110 - offentlighetsvurdertDato (xs:date)
     */
    @Column(name = "freedom_assessment_date")
    @DateTimeFormat(iso = DATE)
    @Audited
    private ZonedDateTime freedomAssessmentDate;

    /**
     * M304 - antallVedlegg (xs:integer)
     */
    @Column(name = "number_of_attachments")
    @Audited

    private Integer numberOfAttachments;

    /**
     * M106 - utlaantDato (xs:date)
     */
    @Column(name = "loaned_date")
    @DateTimeFormat(iso = DATE)
    @Audited
    private ZonedDateTime loanedDate;

    /**
     * M309 - utlaantTil (xs:string)
     */
    @Column(name = "loaned_to")
    @Audited
    private String loanedTo;

    /**
     * M308 - journalenhet (xs:string)
     */
    @Column(name = "records_management_unit")
    @Audited

    private String recordsManagementUnit;

    // Links to CorrespondencePartPerson
    @ManyToMany
    @JoinTable(name = "registry_entry_correspondence_part_person",
            joinColumns = @JoinColumn(name = "f_pk_record_id",
                    referencedColumnName = "pk_record_id"),
            inverseJoinColumns = @JoinColumn(name =
                    FOREIGN_KEY_CORRESPONDENCE_PART_PERSON_PK,
                    referencedColumnName = PRIMARY_KEY_CORRESPONDENCE_PART))
    private List<CorrespondencePartPerson>
            referenceCorrespondencePartPerson = new ArrayList<>();

    // Links to CorrespondencePartUnit
    @ManyToMany
    @JoinTable(name = "registry_entry_correspondence_part_unit",
            joinColumns = @JoinColumn(name = "f_pk_record_id",
                    referencedColumnName = "pk_record_id"),
            inverseJoinColumns = @JoinColumn(name = "f_pk_correspondence_part_unit_id",
                    referencedColumnName = "pk_correspondence_part_id"))
    private List<CorrespondencePartUnit>
            referenceCorrespondencePartUnit = new ArrayList<>();

    // Links to CorrespondencePartInternal
    @ManyToMany
    @JoinTable(name = TABLE_REGISTRY_ENTRY_CORRESPONDENCE_PART_INTERNAL,
            joinColumns = @JoinColumn(name = FOREIGN_KEY_RECORD_PK,
                    referencedColumnName = PRIMARY_KEY_RECORD),
            inverseJoinColumns =
            @JoinColumn(name = FOREIGN_KEY_CORRESPONDENCE_PART_INTERNAL_ID,
                    referencedColumnName = PRIMARY_KEY_CORRESPONDENCE_PART))
    private List<CorrespondencePartInternal>
            referenceCorrespondencePartInternal = new ArrayList<>();

    // Links to DocumentFlow
    @OneToMany(mappedBy = "referenceRegistryEntry")
    private List<DocumentFlow> referenceDocumentFlow = new ArrayList<>();
    // Links to SignOff
    @ManyToMany
    @JoinTable(name = "registry_entry_sign_off",
            joinColumns = @JoinColumn(name = "f_pk_record_id",
                    referencedColumnName = "pk_record_id"),
            inverseJoinColumns = @JoinColumn(name = "f_pk_sign_off_id",
                    referencedColumnName = "pk_sign_off_id"))

    private List<SignOff> referenceSignOff = new ArrayList<>();

    // Links to Precedence
    @ManyToMany
    @JoinTable(name = "registry_entry_precedence",
            joinColumns = @JoinColumn(name = "f_pk_record_id",
                    referencedColumnName = "pk_record_id"),
            inverseJoinColumns = @JoinColumn(name = "f_pk_precedence_id",
                    referencedColumnName = "pk_precedence_id"))
    private List<Precedence> referencePrecedence = new ArrayList<>();

    // Link to ElectronicSignature
    @OneToOne
    @JoinColumn(name = "pk_electronic_signature_id")
    private ElectronicSignature referenceElectronicSignature;

    public Integer getRecordYear() {
        return recordYear;
    }

    public void setRecordYear(Integer recordYear) {
        this.recordYear = recordYear;
    }

    public Integer getRecordSequenceNumber() {
        return recordSequenceNumber;
    }

    public void setRecordSequenceNumber(Integer recordSequenceNumber) {
        this.recordSequenceNumber = recordSequenceNumber;
    }

    public Integer getRegistryEntryNumber() {
        return registryEntryNumber;
    }

    public void setRegistryEntryNumber(Integer registryEntryNumber) {
        this.registryEntryNumber = registryEntryNumber;
    }

    public String getRegistryEntryType() {
        return registryEntryType;
    }

    public void setRegistryEntryType(String registryEntryType) {
        this.registryEntryType = registryEntryType;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public ZonedDateTime getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(ZonedDateTime recordDate) {
        this.recordDate = recordDate;
    }

    public ZonedDateTime getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(ZonedDateTime documentDate) {
        this.documentDate = documentDate;
    }

    public ZonedDateTime getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(ZonedDateTime receivedDate) {
        this.receivedDate = receivedDate;
    }

    public ZonedDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(ZonedDateTime sentDate) {
        this.sentDate = sentDate;
    }

    public ZonedDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(ZonedDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public ZonedDateTime getFreedomAssessmentDate() {
        return freedomAssessmentDate;
    }

    public void setFreedomAssessmentDate(ZonedDateTime freedomAssessmentDate) {
        this.freedomAssessmentDate = freedomAssessmentDate;
    }

    public Integer getNumberOfAttachments() {
        return numberOfAttachments;
    }

    public void setNumberOfAttachments(Integer numberOfAttachments) {
        this.numberOfAttachments = numberOfAttachments;
    }

    public ZonedDateTime getLoanedDate() {
        return loanedDate;
    }

    public void setLoanedDate(ZonedDateTime loanedDate) {
        this.loanedDate = loanedDate;
    }

    public String getLoanedTo() {
        return loanedTo;
    }

    public void setLoanedTo(String loanedTo) {
        this.loanedTo = loanedTo;
    }

    public String getRecordsManagementUnit() {
        return recordsManagementUnit;
    }

    public void setRecordsManagementUnit(String recordsManagementUnit) {
        this.recordsManagementUnit = recordsManagementUnit;
    }

    @Override
    public String getBaseTypeName() {
        return N5ResourceMappings.REGISTRY_ENTRY;
    }

    @Override
    public String getFunctionalTypeName() {
        return Constants.NOARK_CASE_HANDLING_PATH;
    }

    @Override
    public List<DocumentFlow> getReferenceDocumentFlow() {
        return referenceDocumentFlow;
    }

    @Override
    public void setReferenceDocumentFlow(List<DocumentFlow> referenceDocumentFlow) {
        this.referenceDocumentFlow = referenceDocumentFlow;
    }

    public List<CorrespondencePartPerson> getReferenceCorrespondencePartPerson() {
        return referenceCorrespondencePartPerson;
    }

    public void setReferenceCorrespondencePartPerson(
            List<CorrespondencePartPerson> referenceCorrespondencePartPerson) {
        this.referenceCorrespondencePartPerson = referenceCorrespondencePartPerson;
    }

    public void addCorrespondencePartPerson(
            CorrespondencePartPerson correspondencePartPerson) {
        this.referenceCorrespondencePartPerson.add(
                correspondencePartPerson);
    }

    public List<CorrespondencePartUnit> getReferenceCorrespondencePartUnit() {
        return referenceCorrespondencePartUnit;
    }

    public void setReferenceCorrespondencePartUnit(
            List<CorrespondencePartUnit> referenceCorrespondencePartUnit) {
        this.referenceCorrespondencePartUnit = referenceCorrespondencePartUnit;
    }

    public void addCorrespondencePartUnit(
            CorrespondencePartUnit correspondencePartUnit) {
        this.referenceCorrespondencePartUnit.add(
                correspondencePartUnit);
    }

    public List<CorrespondencePartInternal>
    getReferenceCorrespondencePartInternal() {
        return referenceCorrespondencePartInternal;
    }

    public void setReferenceCorrespondencePartInternal(
            List<CorrespondencePartInternal>
                    referenceCorrespondencePartInternal) {
        this.referenceCorrespondencePartInternal =
                referenceCorrespondencePartInternal;
    }

    public List<SignOff> getReferenceSignOff() {
        return referenceSignOff;
    }

    public void setReferenceSignOff(List<SignOff> referenceSignOff) {
        this.referenceSignOff = referenceSignOff;
    }

    public List<Precedence> getReferencePrecedence() {
        return referencePrecedence;
    }

    public void setReferencePrecedence(List<Precedence> referencePrecedence) {
        this.referencePrecedence = referencePrecedence;
    }

    public ElectronicSignature getReferenceElectronicSignature() {
        return referenceElectronicSignature;
    }

    public void setReferenceElectronicSignature(ElectronicSignature referenceElectronicSignature) {
        this.referenceElectronicSignature = referenceElectronicSignature;
    }

    @Override
    public String toString() {
        return super.toString() + " RegistryEntry{" + super.toString() +
                "recordsManagementUnit='" + recordsManagementUnit + '\'' +
                ", loanedTo=" + loanedTo +
                ", loanedDate=" + loanedDate +
                ", numberOfAttachments=" + numberOfAttachments +
                ", freedomAssessmentDate=" + freedomAssessmentDate +
                ", dueDate=" + dueDate +
                ", sentDate=" + sentDate +
                ", receivedDate=" + receivedDate +
                ", documentDate=" + documentDate +
                ", recordDate=" + recordDate +
                ", recordStatus='" + recordStatus + '\'' +
                ", registryEntryType='" + registryEntryType + '\'' +
                ", registryEntryNumber=" + registryEntryNumber +
                ", recordSequenceNumber=" + recordSequenceNumber +
                ", recordYear=" + recordYear +
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
        RegistryEntry rhs = (RegistryEntry) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(recordsManagementUnit, rhs.recordsManagementUnit)
                .append(loanedTo, rhs.loanedTo)
                .append(loanedDate, rhs.loanedDate)
                .append(numberOfAttachments, rhs.numberOfAttachments)
                .append(freedomAssessmentDate, rhs.freedomAssessmentDate)
                .append(dueDate, rhs.dueDate)
                .append(sentDate, rhs.sentDate)
                .append(receivedDate, rhs.receivedDate)
                .append(documentDate, rhs.documentDate)
                .append(recordDate, rhs.recordDate)
                .append(registryEntryType, rhs.registryEntryType)
                .append(registryEntryNumber, rhs.registryEntryNumber)
                .append(recordSequenceNumber, rhs.recordSequenceNumber)
                .append(recordYear, rhs.recordYear)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(recordsManagementUnit)
                .append(loanedTo)
                .append(loanedDate)
                .append(numberOfAttachments)
                .append(freedomAssessmentDate)
                .append(dueDate)
                .append(sentDate)
                .append(receivedDate)
                .append(documentDate)
                .append(recordDate)
                .append(registryEntryType)
                .append(registryEntryNumber)
                .append(recordSequenceNumber)
                .append(recordYear)
                .toHashCode();
    }
}
