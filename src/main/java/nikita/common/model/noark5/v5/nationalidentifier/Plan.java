package nikita.common.model.noark5.v5.nationalidentifier;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.PlanHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier.IPlanEntity;
import nikita.common.model.noark5.v5.metadata.Country;
import nikita.common.util.deserialisers.nationalidentifier.PlanDeserializer;
import nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier.PlanSerializer;
import nikita.webapp.hateoas.nationalidentifier.PlanHateoasHandler;
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
import static nikita.common.config.Constants.REL_FONDS_STRUCTURE_PLAN;
import static nikita.common.config.Constants.TABLE_PLAN;
import static nikita.common.config.N5ResourceMappings.PLAN;

/**
 * Note this should be implemented shuch that only one of
 * kommunenummer, fylkenummer and land can have a value
 */
@Entity
@Table(name = TABLE_PLAN)
@Inheritance(strategy = JOINED)
@JsonSerialize(using = PlanSerializer.class)
@JsonDeserialize(using = PlanDeserializer.class)
@HateoasPacker(using = PlanHateoasHandler.class)
@HateoasObject(using = PlanHateoas.class)
public class Plan
        extends NationalIdentifier
        implements IPlanEntity {

    /**
     * M??? - kommunenummer (xs:string)
     */
    @Column(name = "municipality_number")
    @Audited
    String municipalityNumber;

    /**
     * M??? - fylkenummer (xs:string)
     */
    @Column(name = "county_number")
    @Audited
    String countyNumber;

    /**
     * M??? - landkode code (xs:string)
     */
    @Column(name = "country_code")
    @Audited
    String countryCode;

    /**
     * M??? - landkode code name (xs:string)
     */
    @Column(name = "country_code_name")
    @Audited
    String countryCodeName;

    /**
     * M??? - planidentifikasjon (xs:string)
     */
    @Column(name = "plan_identification", nullable = false)
    @Audited
    String planIdentification;

    @Override
    public String getMunicipalityNumber() {
        return municipalityNumber;
    }

    @Override
    public void setMunicipalityNumber(String municipalityNumber) {
        this.municipalityNumber = municipalityNumber;
    }

    @Override
    public String getCountyNumber() {
        return countyNumber;
    }

    @Override
    public void setCountyNumber(String countyNumber) {
        this.countyNumber = countyNumber;
    }

    @Override
    public Country getCountry() {
        Country country = new Country();
        country.setCode(countryCode);
        country.setCodeName(countryCodeName);
        return country;
    }

    @Override
    public void setCountry(Country country) {
        countryCode = country.getCode();
        countryCodeName = country.getCodeName();
    }

    @Override
    public String getPlanIdentification() {
        return planIdentification;
    }

    @Override
    public void setPlanIdentification(String planIdentification) {
        this.planIdentification = planIdentification;
    }

    @Override
    public String getBaseTypeName() {
        return PLAN;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_PLAN;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "municipalityNumber='" + municipalityNumber + '\'' +
                ", countyNumber='" + countyNumber + '\'' +
                ", countryCode=" + countryCode +
                ", countryCodeName=" + countryCodeName +
                ", planIdentification='" + planIdentification + '\'' +
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
        Plan rhs = (Plan) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(municipalityNumber, rhs.municipalityNumber)
                .append(countyNumber, rhs.countyNumber)
                .append(countryCode, rhs.countryCode)
                .append(countryCodeName, rhs.countryCodeName)
                .append(planIdentification, rhs.planIdentification)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(municipalityNumber)
                .append(countyNumber)
                .append(countryCode)
                .append(countryCodeName)
                .append(planIdentification)
                .toHashCode();
    }
}

