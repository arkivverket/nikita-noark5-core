package nikita.common.model.noark5.v5.nationalidentifier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.CadastralUnitHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier.ICadastralUnitEntity;
import nikita.common.util.deserialisers.nationalidentifier.CadastralUnitDeserializer;
import nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier.CadastralUnitSerializer;
import nikita.webapp.hateoas.nationalidentifier.CadastralUnitHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;

import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.REL_FONDS_STRUCTURE_CADASTRAL_UNIT;
import static nikita.common.config.Constants.TABLE_CADASTRAL;
import static nikita.common.config.N5ResourceMappings.*;

@Entity
@Table(name = TABLE_CADASTRAL)
@Inheritance(strategy = JOINED)
@JsonSerialize(using = CadastralUnitSerializer.class)
@JsonDeserialize(using = CadastralUnitDeserializer.class)
@HateoasPacker(using = CadastralUnitHateoasHandler.class)
@HateoasObject(using = CadastralUnitHateoas.class)
public class CadastralUnit
        extends NationalIdentifier
        implements ICadastralUnitEntity {

    /**
     * M??? - kommunenummer (xs:string)
     */
    @Column(name = MUNICIPALITY_NUMBER_ENG, nullable = false)
    @Audited
    @JsonProperty(MUNICIPALITY_NUMBER)
    String municipalityNumber;

    /**
     * M??? gaardsnummer - (xs:integer)
     */
    @Column(name = HOLDING_NUMBER_ENG, nullable = false)
    @Audited
    @JsonProperty(HOLDING_NUMBER)
    Integer holdingNumber;

    /**
     * M??? bruksnummer - (xs:integer)
     */
    @Column(name = SUB_HOLDING_NUMBER_ENG, nullable = false)
    @Audited
    @JsonProperty(SUB_HOLDING_NUMBER)
    Integer subHoldingNumber;

    /**
     * M??? festenummer - (xs:integer)
     */
    @Column(name = LEASE_NUMBER_ENG)
    @Audited
    @JsonProperty(LEASE_NUMBER)
    Integer leaseNumber;

    /**
     * M??? seksjonsnummer - (xs:integer)
     */
    @Column(name = SECTION_NUMBER_ENG)
    @Audited
    @JsonProperty(SECTION_NUMBER)
    Integer sectionNumber;

    @Override
    public String getMunicipalityNumber() {
        return municipalityNumber;
    }

    @Override
    public void setMunicipalityNumber(String municipalityNumber) {
        this.municipalityNumber = municipalityNumber;
    }

    @Override
    public Integer getHoldingNumber() {
        return holdingNumber;
    }

    @Override
    public void setHoldingNumber(Integer holdingNumber) {
        this.holdingNumber = holdingNumber;
    }

    @Override
    public Integer getSubHoldingNumber() {
        return subHoldingNumber;
    }

    @Override
    public void setSubHoldingNumber(Integer subHoldingNumber) {
        this.subHoldingNumber = subHoldingNumber;
    }

    @Override
    public Integer getLeaseNumber() {
        return leaseNumber;
    }

    @Override
    public void setLeaseNumber(Integer leaseNumber) {
        this.leaseNumber = leaseNumber;
    }

    @Override
    public Integer getSectionNumber() {
        return sectionNumber;
    }

    @Override
    public void setSectionNumber(Integer sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    @Override
    public String getBaseTypeName() {
        return CADASTRAL_UNIT;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_CADASTRAL_UNIT;
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
