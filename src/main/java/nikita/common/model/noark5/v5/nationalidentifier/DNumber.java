package nikita.common.model.noark5.v5.nationalidentifier;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier.DNumberSerializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;

import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.TABLE_D_NUMBER;

@Entity
@Table(name = TABLE_D_NUMBER)
@Inheritance(strategy = JOINED)
@JsonSerialize(using = DNumberSerializer.class)
//@JsonDeserialize(using = DNumberDeserializer.class)
//@HateoasPacker(using = DNumberHateoasHandler.class)
//@HateoasObject(using = DNumberHateoas.class)
public class DNumber
        extends PersonIdentifier
        implements IDNumber {

    /**
     * M??? - DNummer (xs:string)
     */
    @Column(name = "d_number", nullable = false)
    @Audited
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
