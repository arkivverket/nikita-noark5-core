package nikita.common.model.noark5.v5.casehandling;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.ICaseFileEntity;
import nikita.common.model.noark5.v5.metadata.CaseStatus;
import nikita.common.model.noark5.v5.secondary.Precedence;
import nikita.common.util.deserialisers.casehandling.CaseFileDeserializer;
import nikita.webapp.hateoas.casehandling.CaseFileHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = TABLE_CASE_FILE)
@Inheritance(strategy = JOINED)
@JsonDeserialize(using = CaseFileDeserializer.class)
@HateoasPacker(using = CaseFileHateoasHandler.class)
@HateoasObject(using = CaseFileHateoas.class)
@Indexed
public class CaseFile
        extends File
        implements ICaseFileEntity {

    private static final long serialVersionUID = 1L;

    /**
     * M011 - saksaar (xs:integer)
     */
    @Column(name = CASE_YEAR_ENG)
    @Audited
    @JsonProperty(CASE_YEAR)
    @GenericField
    private Integer caseYear;

    /**
     * M012 - sakssekvensnummer (xs:integer)
     */
    @Column(name = CASE_SEQUENCE_NUMBER_ENG)
    @Audited
    @JsonProperty(CASE_SEQUENCE_NUMBER)
    @GenericField
    private Integer caseSequenceNumber;

    /**
     * M100 - saksdato (xs:date)
     */
    @NotNull
    @Column(name = CASE_DATE_ENG, nullable = false)
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    @JsonProperty(CASE_DATE)
    @GenericField
    private OffsetDateTime caseDate;

    /**
     * M306 - saksansvarlig (xs:string)
     */
    @NotNull
    @Column(name = CASE_RESPONSIBLE_ENG, nullable = false)
    @Audited
    @JsonProperty(CASE_RESPONSIBLE)
    @KeywordField
    private String caseResponsible;

    /**
     * M308 - journalenhet (xs:string)
     */
    @Column(name = CASE_RECORDS_MANAGEMENT_UNIT_ENG)
    @Audited
    @JsonProperty(CASE_RECORDS_MANAGEMENT_UNIT)
    @KeywordField
    private String recordsManagementUnit;

    // Links to Precedence
    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(name = TABLE_CASE_FILE_PRECEDENCE,
            joinColumns = @JoinColumn(
                    name = FOREIGN_KEY_CASE_FILE_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(
                    name = FOREIGN_KEY_PRECEDENCE_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private final Set<Precedence> referencePrecedence = new HashSet<>();
    /**
     * M??? - saksstatus kode (xs:string)
     */
    @NotNull
    @Column(name = CASE_STATUS_CODE_ENG, nullable = false)
    @JsonProperty(CASE_STATUS_CODE)
    @Audited
    private String caseStatusCode;

    /**
     * M106 - utlaantDato (xs:date)
     */
    @Column(name = CASE_LOANED_DATE_ENG)
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    @JsonProperty(CASE_LOANED_DATE)
    @GenericField
    private OffsetDateTime loanedDate;

    /**
     * M309 - utlaantTil (xs:string)
     */
    @Column(name = CASE_LOANED_TO_ENG)
    @Audited
    @JsonProperty(CASE_LOANED_TO)
    @KeywordField
    private String loanedTo;
    /**
     * M??? - saksstatus name (xs:string)
     */
    @Column(name = CASE_STATUS_CODE_NAME_ENG)
    @JsonProperty(CASE_STATUS_CODE_NAME)
    @Audited
    private String caseStatusCodeName;

    // Link to AdministrativeUnit
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = CASE_FILE_ADMINISTRATIVE_UNIT_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    @JsonIgnore
    private AdministrativeUnit referenceAdministrativeUnit;

    public CaseFile(File file) {
        super();
        setSystemId(file.getSystemId());
        setVersion(file.getVersion(), true);
        setTitle(file.getTitle());
        setPublicTitle(file.getPublicTitle());
        setDocumentMedium(file.getDocumentMedium());
        setFileId(file.getFileId());
        // Is this a potential problem if the person that expands the file to
        // a caseFile is not the same person? However the expanded portion
        // (CaseFile) should not have a different owner than File
        setCreatedBy(file.getCreatedBy());
        setCaseDate(file.getCreatedDate());
        setLastModifiedDate(file.getLastModifiedDate());
        setLastModifiedBy(file.getLastModifiedBy());
    }

    public CaseFile() {
    }

    public Integer getCaseYear() {
        return caseYear;
    }

    public void setCaseYear(Integer caseYear) {
        this.caseYear = caseYear;
    }

    public Integer getCaseSequenceNumber() {
        return caseSequenceNumber;
    }

    public void setCaseSequenceNumber(Integer caseSequenceNumber) {
        this.caseSequenceNumber = caseSequenceNumber;
    }

    public OffsetDateTime getCaseDate() {
        return caseDate;
    }

    public void setCaseDate(OffsetDateTime caseDate) {
        this.caseDate = caseDate;
    }

    public String getCaseResponsible() {
        return caseResponsible;
    }

    public void setCaseResponsible(String caseResponsible) {
        this.caseResponsible = caseResponsible;
    }

    public String getRecordsManagementUnit() {
        return recordsManagementUnit;
    }

    public void setRecordsManagementUnit(String recordsManagementUnit) {
        this.recordsManagementUnit = recordsManagementUnit;
    }

    @Override
    public CaseStatus getCaseStatus() {
        if (null == caseStatusCode)
            return null;
        return new CaseStatus(caseStatusCode,caseStatusCodeName);
    }

    @Override
    public void setCaseStatus(CaseStatus caseStatus) {
        if (null != caseStatus) {
            this.caseStatusCode = caseStatus.getCode();
            this.caseStatusCodeName = caseStatus.getCodeName();
        } else {
            this.caseStatusCode = null;
            this.caseStatusCodeName = null;
        }
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

    @Override
    public String getBaseTypeName() {
        return CASE_FILE;
    }

    @Override
    public String getBaseRel() {
        return REL_CASE_HANDLING_CASE_FILE;
    }

    @Override
    public String getFunctionalTypeName() {
        return NOARK_CASE_HANDLING_PATH;
    }

    @Override
    public Set<Precedence> getReferencePrecedence() {
        return referencePrecedence;
    }

    @Override
    public void addPrecedence(Precedence precedence) {
        this.referencePrecedence.add(precedence);
        precedence.getReferenceCaseFile().add(this);
    }

    @Override
    public void removePrecedence(Precedence precedence) {
        this.referencePrecedence.remove(precedence);
        precedence.getReferenceCaseFile().remove(this);
    }

    public AdministrativeUnit getReferenceAdministrativeUnit() {
        return referenceAdministrativeUnit;
    }

    public void setReferenceAdministrativeUnit(
            AdministrativeUnit referenceAdministrativeUnit) {
        this.referenceAdministrativeUnit = referenceAdministrativeUnit;
    }

    @Override
    public String toString() {
        return super.toString() + " CaseFile{" +
                "loanedTo='" + loanedTo + '\'' +
                ", loanedDate=" + loanedDate +
                ", caseStatusCode='" + caseStatusCode + '\'' +
                ", caseStatusCodeName='" + caseStatusCodeName + '\'' +
                ", recordsManagementUnit='" + recordsManagementUnit + '\'' +
                ", caseResponsible='" + caseResponsible + '\'' +
                ", caseDate=" + caseDate +
                ", caseSequenceNumber=" + caseSequenceNumber +
                ", caseYear=" + caseYear +
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
        CaseFile rhs = (CaseFile) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(caseSequenceNumber, rhs.caseSequenceNumber)
                .append(caseYear, rhs.caseYear)
                .append(caseDate, rhs.caseDate)
                .append(caseResponsible, rhs.caseResponsible)
                .append(caseStatusCode, rhs.caseStatusCode)
                .append(caseStatusCodeName, rhs.caseStatusCodeName)
                .append(recordsManagementUnit, rhs.recordsManagementUnit)
                .append(loanedDate, rhs.loanedDate)
                .append(loanedTo, rhs.loanedTo)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(caseSequenceNumber)
                .append(caseYear)
                .append(caseDate)
                .append(caseResponsible)
                .append(caseStatusCode)
                .append(caseStatusCodeName)
                .append(recordsManagementUnit)
                .append(loanedDate)
                .append(loanedTo)
                .toHashCode();
    }
}
