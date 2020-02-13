package nikita.common.model.noark5.v5.nationalidentifier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.DNumberHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier.IDNumberEntity;
import nikita.common.util.deserialisers.nationalidentifier.DNumberDeserializer;
import nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier.DNumberSerializer;
import nikita.webapp.hateoas.nationalidentifier.DNumberHateoasHandler;
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
import static nikita.common.config.Constants.REL_FONDS_STRUCTURE_D_NUMBER;
import static nikita.common.config.Constants.TABLE_D_NUMBER;
import static nikita.common.config.N5ResourceMappings.*;

@Entity
@Table(name = TABLE_D_NUMBER)
@Inheritance(strategy = JOINED)
@JsonSerialize(using = DNumberSerializer.class)
@JsonDeserialize(using = DNumberDeserializer.class)
@HateoasPacker(using = DNumberHateoasHandler.class)
@HateoasObject(using = DNumberHateoas.class)
public class DNumber
        extends PersonIdentifier
        implements IDNumberEntity {

    /**
     * M??? - DNummer (xs:string)
     */
    @Column(name = D_NUMBER_FIELD_ENG, nullable = false)
    @Audited
    @JsonProperty(D_NUMBER_FIELD)
    private String dNumber;

    @Override
    public String getdNumber() {
        return dNumber;
    }

    @Override
    public void setdNumber(String dNumber) {
        this.dNumber = dNumber;
    }

    @Override
    public String getBaseTypeName() {
        return D_NUMBER;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_D_NUMBER;
    }

    @Override
    public String toString() {
        return "DNumber{" +
                "dNumber='" + dNumber + '\'' +
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
        DNumber rhs = (DNumber) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(dNumber, rhs.dNumber)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(dNumber)
                .toHashCode();
    }
}
