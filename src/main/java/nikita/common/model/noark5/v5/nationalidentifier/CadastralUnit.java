package nikita.common.model.noark5.v5.nationalidentifier;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;

import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.TABLE_CADASTRAL;

@Entity
@Table(name = TABLE_CADASTRAL)
@Inheritance(strategy = JOINED)
public class CadastralUnit
        extends NationalIdentifier {

    /**
     * M??? - kommunenummer (xs:string)
     */
    @Column(name = "municipality_number", nullable = false)
    @Audited
    String municipalityNumber;

    /**
     * M??? gaardsnummer - (xs:integer)
     */
    @Column(name = "holding_number", nullable = false)
    @Audited
    Integer holdingNumber;

    /**
     * M??? bruksnummer - (xs:integer)
     */
    @Column(name = "sub_holding_number", nullable = false)
    @Audited
    Integer subHoldingNumber;

    /**
     * M??? festenummer - (xs:integer)
     */
    @Column(name = "lease_number")
    @Audited
    Integer leaseNumber;

    /**
     * M??? seksjonsnummer - (xs:integer)
     */
    @Column(name = "section_number")
    @Audited
    Integer sectionNumber;

    public String getMunicipalityNumber() {
        return municipalityNumber;
    }

    public void setMunicipalityNumber(String municipalityNumber) {
        this.municipalityNumber = municipalityNumber;
    }

    public Integer getHoldingNumber() {
        return holdingNumber;
    }

    public void setHoldingNumber(Integer holdingNumber) {
        this.holdingNumber = holdingNumber;
    }

    public Integer getSubHoldingNumber() {
        return subHoldingNumber;
    }

    public void setSubHoldingNumber(Integer subHoldingNumber) {
        this.subHoldingNumber = subHoldingNumber;
    }

    public Integer getLeaseNumber() {
        return leaseNumber;
    }

    public void setLeaseNumber(Integer leaseNumber) {
        this.leaseNumber = leaseNumber;
    }

    public Integer getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(Integer sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    @Override
    public String toString() {
        return "CadastralUnit{" +
                "municipalityNumber='" + municipalityNumber + '\'' +
                ", holdingNumber=" + holdingNumber +
                ", subHoldingNumber=" + subHoldingNumber +
                ", leaseNumber=" + leaseNumber +
                ", sectionNumber=" + sectionNumber +
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
        CadastralUnit rhs = (CadastralUnit) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(municipalityNumber, rhs.municipalityNumber)
                .append(holdingNumber, rhs.holdingNumber)
                .append(subHoldingNumber, rhs.subHoldingNumber)
                .append(leaseNumber, rhs.leaseNumber)
                .append(sectionNumber, rhs.sectionNumber)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(municipalityNumber)
                .append(holdingNumber)
                .append(subHoldingNumber)
                .append(leaseNumber)
                .append(sectionNumber)
                .toHashCode();
    }
}
