package nikita.common.model.noark5.v5.casehandling;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.interfaces.IPart;
import nikita.common.model.noark5.v5.interfaces.IPrecedence;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.metadata.CaseStatus;
import nikita.common.util.deserialisers.casehandling.CaseFileDeserializer;
import nikita.webapp.hateoas.casehandling.CaseFileHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CASE_FILE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@Entity
@Table(name = TABLE_CASE_FILE)
@Inheritance(strategy = JOINED)
@JsonDeserialize(using = CaseFileDeserializer.class)
@HateoasPacker(using = CaseFileHateoasHandler.class)
@HateoasObject(using = CaseFileHateoas.class)
public class CaseFile
        extends File
        implements Serializable, INikitaEntity, IPrecedence, IPart {

    private static final long serialVersionUID = 1L;

    /**
     * M011 - saksaar (xs:integer)
     */
    @Column(name = "case_year")
    @Audited
    private Integer caseYear;

    /**
     * M012 - sakssekvensnummer (xs:integer)
     */
    @Column(name = "case_sequence_number")
    @Audited
    private Integer caseSequenceNumber;

    /**
     * M100 - saksdato (xs:date)
     */
    @NotNull
    @Column(name = "case_date", nullable = false)
    @DateTimeFormat(iso = DATE)
    @Audited
    private ZonedDateTime caseDate;

    /**
     * M306 - saksansvarlig (xs:string)
     */
    @NotNull
    @Column(name = "case_responsible", nullable = false)
    @Audited

    private String caseResponsible;

    /**
     * M308 - journalenhet (xs:string)
     */
    @Column(name = "records_management_unit")
    @Audited
    private String recordsManagementUnit;

    /**
     * M052 - saksstatus (xs:string)
     */
    @NotNull
    @Column(name = "case_status", nullable = false)
    @Audited
    private String caseStatus;

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

    // Links to Precedence
    @ManyToMany
    @JoinTable(name = TABLE_CASE_FILE_PRECEDENCE,
            joinColumns = @JoinColumn(
                    name = FOREIGN_KEY_CASE_FILE_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(
                    name = FOREIGN_KEY_PRECEDENCE_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private List<Precedence> referencePrecedence = new ArrayList<>();

    // Link to AdministrativeUnit
    @ManyToOne
    @JoinColumn(name = CASE_FILE_ADMINISTRATIVE_UNIT_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    @JsonIgnore
    private AdministrativeUnit referenceAdministrativeUnit;

    // Link to CaseFileStatus
    @ManyToOne
    @JoinColumn(name = JOIN_CASE_FILE_STATUS,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    @JsonIgnore
    private CaseStatus referenceCaseFileStatus;

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

    public ZonedDateTime getCaseDate() {
        return caseDate;
    }

    public void setCaseDate(ZonedDateTime caseDate) {
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

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
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

    @Override
    public String getBaseTypeName() {
        return CASE_FILE;
    }

    @Override
    public String getFunctionalTypeName() {
        return NOARK_CASE_HANDLING_PATH;
    }

    public List<Precedence> getReferencePrecedence() {
        return referencePrecedence;
    }

    public void setReferencePrecedence(List<Precedence> referencePrecedence) {
        this.referencePrecedence = referencePrecedence;
    }

    public AdministrativeUnit getReferenceAdministrativeUnit() {
        return referenceAdministrativeUnit;
    }

    public void setReferenceAdministrativeUnit(
            AdministrativeUnit referenceAdministrativeUnit) {
        this.referenceAdministrativeUnit = referenceAdministrativeUnit;
    }

    public CaseStatus getReferenceCaseFileStatus() {
        return referenceCaseFileStatus;
    }

    public void setReferenceCaseFileStatus(CaseStatus referenceCaseFileStatus) {
        this.referenceCaseFileStatus = referenceCaseFileStatus;
    }

    @Override
    public String toString() {
        return super.toString() + " CaseFile{" +
                "loanedTo='" + loanedTo + '\'' +
                ", loanedDate=" + loanedDate +
                ", caseStatus='" + caseStatus + '\'' +
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
                .append(caseStatus, rhs.caseStatus)
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
                .append(caseStatus)
                .append(recordsManagementUnit)
                .append(loanedDate)
                .append(loanedTo)
                .toHashCode();
    }
}
