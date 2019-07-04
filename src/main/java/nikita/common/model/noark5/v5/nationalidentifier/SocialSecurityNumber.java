package nikita.common.model.noark5.v5.nationalidentifier;

import nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier.ISocialSecurityNumber;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;

import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.TABLE_SOCIAL_SECURITY_NUMBER;

@Entity
@Table(name = TABLE_SOCIAL_SECURITY_NUMBER)
@Inheritance(strategy = JOINED)
//@JsonDeserialize(using = SocialSecurityNumberDeserializer.class)
//@HateoasPacker(using = SocialSecurityNumberHateoasHandler.class)
//@HateoasObject(using = SocialSecurityNumberHateoas.class)
public class SocialSecurityNumber
        extends PersonIdentifier
        implements ISocialSecurityNumber {
    /**
     * M??? - fødselsnummer (xs:string)
     */
    @Column(name = "social_security_number")
    @Audited
    private String socialSecurityNumber;

    @Override
    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    @Override
    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    @Override
    public String toString() {
        return "SocialSecurityNumber{" +
                "socialSecurityNumber='" + socialSecurityNumber + '\'' +
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
        SocialSecurityNumber rhs = (SocialSecurityNumber) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(socialSecurityNumber, rhs.socialSecurityNumber)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(socialSecurityNumber)
                .toHashCode();
    }
}
