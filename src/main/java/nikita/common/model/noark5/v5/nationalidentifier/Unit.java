package nikita.common.model.noark5.v5.nationalidentifier;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.UnitHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier.IUnitEntity;
import nikita.common.util.deserialisers.nationalidentifier.UnitDeserializer;
import nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier.UnitSerializer;
import nikita.webapp.hateoas.nationalidentifier.UnitHateoasHandler;
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
import static nikita.common.config.Constants.TABLE_UNIT;

@Entity
@Table(name = TABLE_UNIT)
@Inheritance(strategy = JOINED)
@JsonSerialize(using = UnitSerializer.class)
@JsonDeserialize(using = UnitDeserializer.class)
@HateoasPacker(using = UnitHateoasHandler.class)
@HateoasObject(using = UnitHateoas.class)
public class Unit
        extends NationalIdentifier
        implements IUnitEntity {

    /**
     * M??? - organisasjonsnummer (xs:string)
     */
    @Column(name = "organisation_number", nullable = false)
    @Audited
    private String organisationNumber;

    @Override
    public String getOrganisationNumber() {
        return organisationNumber;
    }

    @Override
    public void setOrganisationNumber(String organisationNumber) {
        this.organisationNumber = organisationNumber;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "organisationNumber='" + organisationNumber + '\'' +
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
        Unit rhs = (Unit) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(organisationNumber, rhs.organisationNumber)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(organisationNumber)
                .toHashCode();
    }
}
