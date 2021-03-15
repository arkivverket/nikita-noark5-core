package nikita.common.model.noark5.v5.casehandling;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.casehandling.RegistryEntryHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IRegistryEntryEntity;
import nikita.common.model.noark5.v5.metadata.RegistryEntryStatus;
import nikita.common.model.noark5.v5.metadata.RegistryEntryType;
import nikita.common.model.noark5.v5.secondary.DocumentFlow;
import nikita.common.model.noark5.v5.secondary.ElectronicSignature;
import nikita.common.model.noark5.v5.secondary.Precedence;
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
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = TABLE_REGISTRY_ENTRY)
@Inheritance(strategy = JOINED)
@JsonDeserialize(using = RegistryEntryDeserializer.class)
@HateoasPacker(using = RegistryEntryHateoasHandler.class)
@HateoasObject(using = RegistryEntryHateoas.class)
public class RegistryEntry
        extends Record
        implements IRegistryEntryEntity {

    /**
     * M013 - journalaar (xs:integer)
     */
    @Column(name = REGISTRY_ENTRY_YEAR_ENG, nullable = false)
    @Audited
    @JsonProperty(REGISTRY_ENTRY_YEAR)
    private Integer recordYear;

    /**
     * M014 - journalsekvensnummer (xs:integer)
     */
    @Column(name = REGISTRY_ENTRY_SEQUENCE_NUMBER_ENG, nullable = false)
    @Audited
    @JsonProperty(REGISTRY_ENTRY_SEQUENCE_NUMBER)
    private Integer recordSequenceNumber;

    /**
     * M015 - journalpostnummer (xs:integer)
     */
    @Column(name = REGISTRY_ENTRY_NUMBER_ENG, nullable = false)
    @Audited
    @JsonProperty(REGISTRY_ENTRY_NUMBER)
    private Integer registryEntryNumber;

    /**
     * M?? - journalposttype code (xs:string)
     */
    @NotNull
    @Column(name = REGISTRY_ENTRY_TYPE_CODE_ENG, nullable = false)
    @Audited
    @JsonProperty(REGISTRY_ENTRY_TYPE_CODE)
    private String registryEntryTypeCode;

    /**
     * M082 - journalposttype code name (xs:string)
     */
    @NotNull
    @Column(name = REGISTRY_ENTRY_TYPE_CODE_NAME_ENG, nullable = false)
    @Audited
    @JsonProperty(REGISTRY_ENTRY_TYPE_CODE_NAME)
    private String registryEntryTypeCodeName;

    /**
     * M??? - journalstatus code (xs:string)
     */
    @NotNull
    @Column(name = REGISTRY_ENTRY_STATUS_CODE_ENG)
    @Audited
    @JsonProperty(REGISTRY_ENTRY_STATUS_CODE)
    private String registryEntryStatusCode;

    /**
     * M053 - journalstatus code name (xs:string, nullable = false)
     */
    @NotNull
    @Column(name = REGISTRY_ENTRY_STATUS_CODE_NAME_ENG, nullable = false)
    @Audited
    @JsonProperty(REGISTRY_ENTRY_STATUS_CODE_NAME)
    private String registryEntryStatusCodeName;

    /**
     * M101 - journaldato (xs:date)
     */
    @NotNull
    @Column(name = REGISTRY_ENTRY_DATE_ENG, nullable = false)
    @DateTimeFormat(iso = DATE)
    @Audited
    @JsonProperty(REGISTRY_ENTRY_DATE)
    private OffsetDateTime recordDate;

    /**
     * M103 - dokumentetsDato (xs:date)
     */
    @Column(name = REGISTRY_ENTRY_DOCUMENT_DATE_ENG)
    @DateTimeFormat(iso = DATE)
    @Audited
    @JsonProperty(REGISTRY_ENTRY_DOCUMENT_DATE)
    private OffsetDateTime documentDate;

    /**
     * M104 - mottattDato (xs:dateTime)
     */
    @Column(name = REGISTRY_ENTRY_RECEIVED_DATE_ENG)
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    @JsonProperty(REGISTRY_ENTRY_RECEIVED_DATE)
    private OffsetDateTime receivedDate;

    /**
     * M105 - sendtDato (xs:dateTime)
     */
    @Column(name = REGISTRY_ENTRY_SENT_DATE_ENG)
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    @JsonProperty(REGISTRY_ENTRY_SENT_DATE)
    private OffsetDateTime sentDate;

    /**
     * M109 - forfallsdato (xs:date)
     */
    @Column(name = REGISTRY_ENTRY_DUE_DATE_ENG)
    @DateTimeFormat(iso = DATE)
    @Audited
    @JsonProperty(REGISTRY_ENTRY_DUE_DATE)
    private OffsetDateTime dueDate;

    /**
     * M110 - offentlighetsvurdertDato (xs:date)
     */
    @Column(name = REGISTRY_ENTRY_RECORD_FREEDOM_ASSESSMENT_DATE_ENG)
    @DateTimeFormat(iso = DATE)
    @Audited
    @JsonProperty(REGISTRY_ENTRY_RECORD_FREEDOM_ASSESSMENT_DATE)
    private OffsetDateTime freedomAssessmentDate;

    /**
     * M304 - antallVedlegg (xs:integer)
     */
    @Column(name = REGISTRY_ENTRY_NUMBER_OF_ATTACHMENTS_ENG)
    @Audited
    @JsonProperty(REGISTRY_ENTRY_NUMBER_OF_ATTACHMENTS)
    private Integer numberOfAttachments;

    /**
     * M106 - utlaantDato (xs:date)
     */
    @Column(name = CASE_LOANED_DATE_ENG)
    @DateTimeFormat(iso = DATE)
    @Audited
    @JsonProperty(CASE_LOANED_DATE)
    private OffsetDateTime loanedDate;

    /**
     * M309 - utlaantTil (xs:string)
     */
    @Column(name = CASE_LOANED_TO_ENG)
    @Audited
    @JsonProperty(CASE_LOANED_TO)
    private String loanedTo;

    /**
     * M308 - journalenhet (xs:string)
     */
    @Column(name = CASE_RECORDS_MANAGEMENT_UNIT_ENG)
    @Audited
    @JsonProperty(CASE_RECORDS_MANAGEMENT_UNIT)
    private String recordsManagementUnit;

    // Links to DocumentFlow
    @OneToMany(mappedBy = "referenceRegistryEntry")
    private List<DocumentFlow> referenceDocumentFlow = new ArrayList<>();

    // Links to SignOff
    @ManyToMany
    @JoinTable(name = TABLE_REGISTRY_ENTRY_SIGN_OFF,
            joinColumns = @JoinColumn(
                    name = FOREIGN_KEY_RECORD_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(
                    name = FOREIGN_KEY_SIGN_OFF_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private List<SignOff> referenceSignOff = new ArrayList<>();

    // Links to Precedence
    @ManyToMany
    @JoinTable(name = TABLE_REGISTRY_ENTRY_PRECEDENCE,
            joinColumns = @JoinColumn(
                    name = FOREIGN_KEY_RECORD_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(
                    name = FOREIGN_KEY_PRECEDENCE_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private List<Precedence> referencePrecedence = new ArrayList<>();

    // Link to ElectronicSignature
    @OneToOne
    @JoinColumn(name = PRIMARY_KEY_SYSTEM_ID)
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

    public RegistryEntryType getRegistryEntryType() {
        if (null == registryEntryTypeCode)
            return null;
        return new RegistryEntryType(registryEntryTypeCode,
                                     registryEntryTypeCodeName);
    }

    public void setRegistryEntryType(RegistryEntryType registryEntryType) {
        if (null != registryEntryType) {
            this.registryEntryTypeCode = registryEntryType.getCode();
            this.registryEntryTypeCodeName = registryEntryType.getCodeName();
        } else {
            this.registryEntryTypeCode = null;
            this.registryEntryTypeCodeName = null;
        }
    }

    public RegistryEntryStatus getRegistryEntryStatus() {
        if (null == registryEntryStatusCode)
            return null;
        return new RegistryEntryStatus(registryEntryStatusCode,
                                       registryEntryStatusCodeName);
    }

    public void setRegistryEntryStatus(RegistryEntryStatus registryEntryStatus) {
        if (null != registryEntryStatus) {
            this.registryEntryStatusCode = registryEntryStatus.getCode();
            this.registryEntryStatusCodeName = registryEntryStatus.getCodeName();
        } else {
            this.registryEntryStatusCode = null;
            this.registryEntryStatusCodeName = null;
        }
    }

    public OffsetDateTime getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(OffsetDateTime recordDate) {
        this.recordDate = recordDate;
    }

    public OffsetDateTime getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(OffsetDateTime documentDate) {
        this.documentDate = documentDate;
    }

    public OffsetDateTime getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(OffsetDateTime receivedDate) {
        this.receivedDate = receivedDate;
    }

    public OffsetDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(OffsetDateTime sentDate) {
        this.sentDate = sentDate;
    }

    public OffsetDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(OffsetDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public OffsetDateTime getFreedomAssessmentDate() {
        return freedomAssessmentDate;
    }

    public void setFreedomAssessmentDate(OffsetDateTime freedomAssessmentDate) {
        this.freedomAssessmentDate = freedomAssessmentDate;
    }

    public Integer getNumberOfAttachments() {
        return numberOfAttachments;
    }

    public void setNumberOfAttachments(Integer numberOfAttachments) {
        this.numberOfAttachments = numberOfAttachments;
    }

    public OffsetDateTime getLoanedDate() {
        return loanedDate;
    }

    public void setLoanedDate(OffsetDateTime loanedDate) {
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
        return REGISTRY_ENTRY;
    }

    @Override
    public String getBaseRel() {
        return REL_CASE_HANDLING_REGISTRY_ENTRY;
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
    public void setReferenceDocumentFlow(
            List<DocumentFlow> referenceDocumentFlow) {
        this.referenceDocumentFlow = referenceDocumentFlow;
    }

    @Override
    public void addDocumentFlow(DocumentFlow documentFlow) {
        this.referenceDocumentFlow.add(documentFlow);
        documentFlow.setReferenceRegistryEntry(this);
    }

    @Override
    public void removeDocumentFlow(DocumentFlow documentFlow) {
        referenceDocumentFlow.remove(documentFlow);
        documentFlow.setReferenceRegistryEntry(null);
    }

    public List<SignOff> getReferenceSignOff() {
        return referenceSignOff;
    }

    public void setReferenceSignOff(List<SignOff> referenceSignOff) {
        this.referenceSignOff = referenceSignOff;
    }

    public void addReferenceSignOff(SignOff signOff) {
        this.referenceSignOff.add(signOff);
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

    public void setReferenceElectronicSignature(
            ElectronicSignature referenceElectronicSignature) {
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
                ", registryEntryStatusCode='" + registryEntryStatusCode + '\'' +
                ", registryEntryStatusCodeName='" + registryEntryStatusCodeName + '\'' +
                ", registryEntryTypeCode='" + registryEntryTypeCode + '\'' +
                ", registryEntryTypeCodeName='" + registryEntryTypeCodeName + '\'' +
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
                .append(registryEntryStatusCode, rhs.registryEntryStatusCode)
                .append(registryEntryStatusCodeName, rhs.registryEntryStatusCodeName)
                .append(registryEntryTypeCode, rhs.registryEntryTypeCode)
                .append(registryEntryTypeCodeName, rhs.registryEntryTypeCodeName)
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
                .append(registryEntryStatusCode)
                .append(registryEntryStatusCodeName)
                .append(registryEntryTypeCode)
                .append(registryEntryTypeCodeName)
                .append(registryEntryNumber)
                .append(recordSequenceNumber)
                .append(recordYear)
                .toHashCode();
    }
}
