package nikita.common.model.noark5.v5.nationalidentifier;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Column;

public class CadastralUnit
        extends NationalIdentifier {

    /**
     * M??? - kommunenummer (xs:string)
     */
    @Column(name = "holding_number", nullable = false)
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
