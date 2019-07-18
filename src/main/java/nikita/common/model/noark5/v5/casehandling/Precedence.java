package nikita.common.model.noark5.v5.casehandling;

import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.NoarkGeneralEntity;
import nikita.common.model.noark5.v5.interfaces.entities.IPrecedenceEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static nikita.common.config.Constants.REL_FONDS_STRUCTURE_PRECEDENCE;
import static nikita.common.config.Constants.TABLE_PRECEDENCE;
import static nikita.common.config.N5ResourceMappings.PRECEDENCE;

@Entity
@Table(name = TABLE_PRECEDENCE)
public class Precedence
        extends NoarkGeneralEntity
        implements IPrecedenceEntity {

    /**
     * M111 - presedensDato (xs:date)
     */
    @Column(name = "precedence_date")
    @Audited
    private OffsetDateTime precedenceDate;

    /**
     * M311 - presedensHjemmel (xs:string)
     */
    @Column(name = "precedence_authority")
    @Audited
    private String precedenceAuthority;

    /**
     * M312 - rettskildefaktor (xs:string)
     */
    @Column(name = "source_of_law")
    @Audited
    private String sourceOfLaw;

    /**
     * M628 - presedensGodkjentDato (xs:date)
     */
    @Column(name = "precedence_approved_date")
    @Audited
    private OffsetDateTime precedenceApprovedDate;

    /**
     * M629 - presedensGodkjentAv (xs:string)
     */
    @Column(name = "precedence_approved_by")
    @Audited
    private String precedenceApprovedBy;

    /**
     * M056 - presedensStatus (xs:string)
     */
    @Column(name = "precedence_status")
    @Audited
    private String precedenceStatus;

    // Link to RegistryEntry
    @ManyToMany(mappedBy = "referencePrecedence")
    private List<RegistryEntry> referenceRegistryEntry = new ArrayList<>();

    // Links to CaseFiles
    @ManyToMany(mappedBy = "referencePrecedence")
    private List<CaseFile> referenceCaseFile = new ArrayList<>();


    public OffsetDateTime getPrecedenceDate() {
        return precedenceDate;
    }

    public void setPrecedenceDate(OffsetDateTime precedenceDate) {
        this.precedenceDate = precedenceDate;
    }

    public String getPrecedenceAuthority() {
        return precedenceAuthority;
    }

    public void setPrecedenceAuthority(String precedenceAuthority) {
        this.precedenceAuthority = precedenceAuthority;
    }

    public String getSourceOfLaw() {
        return sourceOfLaw;
    }

    public void setSourceOfLaw(String sourceOfLaw) {
        this.sourceOfLaw = sourceOfLaw;
    }

    public OffsetDateTime getPrecedenceApprovedDate() {
        return precedenceApprovedDate;
    }

    public void setPrecedenceApprovedDate(OffsetDateTime precedenceApprovedDate) {
        this.precedenceApprovedDate = precedenceApprovedDate;
    }

    public String getPrecedenceApprovedBy() {
        return precedenceApprovedBy;
    }

    public void setPrecedenceApprovedBy(String precedenceApprovedBy) {
        this.precedenceApprovedBy = precedenceApprovedBy;
    }

    public String getPrecedenceStatus() {
        return precedenceStatus;
    }

    public void setPrecedenceStatus(String precedenceStatus) {
        this.precedenceStatus = precedenceStatus;
    }

    @Override
    public String getBaseTypeName() {
        return PRECEDENCE;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_PRECEDENCE;
    }

    @Override
    public String getFunctionalTypeName() {
        return Constants.NOARK_CASE_HANDLING_PATH;
    }

    public List<RegistryEntry> getReferenceRegistryEntry() {
        return referenceRegistryEntry;
    }

    public void setReferenceRegistryEntry(
            List<RegistryEntry> referenceRegistryEntry) {
        this.referenceRegistryEntry = referenceRegistryEntry;
    }

    public List<CaseFile> getReferenceCaseFile() {
        return referenceCaseFile;
    }

    public void setReferenceCaseFile(List<CaseFile> referenceCaseFile) {
        this.referenceCaseFile = referenceCaseFile;
    }

    @Override
    public String toString() {
        return "Precedence{" + super.toString() +
                "precedenceStatus='" + precedenceStatus + '\'' +
                ", precedenceApprovedBy='" + precedenceApprovedBy + '\'' +
                ", precedenceApprovedDate=" + precedenceApprovedDate +
                ", sourceOfLaw='" + sourceOfLaw + '\'' +
                ", precedenceAuthority='" + precedenceAuthority + '\'' +
                ", precedenceDate='" + precedenceDate + '\'' +
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
        Precedence rhs = (Precedence) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(precedenceStatus, rhs.precedenceStatus)
                .append(precedenceApprovedBy, rhs.precedenceApprovedBy)
                .append(precedenceApprovedDate, rhs.precedenceApprovedDate)
                .append(sourceOfLaw, rhs.sourceOfLaw)
                .append(precedenceAuthority, rhs.precedenceAuthority)
                .append(precedenceDate, rhs.precedenceDate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(precedenceStatus)
                .append(precedenceApprovedBy)
                .append(precedenceApprovedDate)
                .append(sourceOfLaw)
                .append(precedenceAuthority)
                .append(precedenceDate)
                .toHashCode();
    }
}
