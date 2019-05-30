package nikita.common.model.noark5.v5.casehandling;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.config.Constants;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.hateoas.casehandling.CaseFileHateoas;
import nikita.common.model.noark5.v5.interfaces.IParty;
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

import static nikita.common.config.Constants.JOIN_CASE_FILE_STATUS;
import static nikita.common.config.Constants.PRIMARY_KEY_CASE_FILE_STATUS;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;


// TODO: You are missing M209 referanseSekundaerKlassifikasjon


@Entity
@Table(name = "case_file")
// Enable soft delete of CaseFile
// @SQLDelete(sql="UPDATE case_file SET deleted = true WHERE pk_file_id = ? and version = ?")
// @Where(clause="deleted <> true")
//@Indexed(index = "case_file")
@JsonDeserialize(using = CaseFileDeserializer.class)
@HateoasPacker(using = CaseFileHateoasHandler.class)
@HateoasObject(using = CaseFileHateoas.class)
public class CaseFile extends File implements Serializable, INikitaEntity,
        IPrecedence, IParty {

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

    @Column(name = "owned_by")
    @Audited
    private String ownedBy;

    // Links to Party
    @ManyToMany
    @JoinTable(name = "case_file_case_file_party",
            joinColumns = @JoinColumn(name = "f_pk_case_file_id",
                    referencedColumnName = "pk_file_id"),
            inverseJoinColumns = @JoinColumn(name = "f_pk_part_id",
                    referencedColumnName = "pk_part_id"))

    private List<Party> referenceParty = new ArrayList<>();

    // Links to Precedence
    @ManyToMany
    @JoinTable(name = "case_file_precedence",
            joinColumns = @JoinColumn(name = "f_pk_case_file_id",
                    referencedColumnName = "pk_file_id"),
            inverseJoinColumns = @JoinColumn(name = "f_pk_precedence_id",
                    referencedColumnName = "pk_precedence_id"))

    private List<Precedence> referencePrecedence = new ArrayList<>();

    // Used for soft delete.
    @Column(name = "deleted")
    @Audited
    private Boolean deleted;

    // Link to AdministrativeUnit
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_file_administrative_unit_id",
            referencedColumnName = Constants.PRIMARY_KEY_ADMINISTRATIVE_UNIT)
    @JsonIgnore
    private AdministrativeUnit referenceAdministrativeUnit;

    // Link to CaseFileStatus
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = JOIN_CASE_FILE_STATUS,
            referencedColumnName = PRIMARY_KEY_CASE_FILE_STATUS)
    @JsonIgnore
    private CaseStatus referenceCaseFileStatus;

    public static String getForeignKeyIdentifier(String parent) {

        if (parent.equalsIgnoreCase("series")) {
            return "referenceSeries";
        } else
            return null;
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
    }

    @Override
    public String getBaseTypeName() {
        return N5ResourceMappings.CASE_FILE;
    }

    @Override
    public String getFunctionalTypeName() {
        return Constants.NOARK_CASE_HANDLING_PATH;
    }

    public List<Party> getReferenceParty() {
        return referenceParty;
    }

    public void setReferenceParty(List<Party> referenceParty) {
        this.referenceParty = referenceParty;
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
