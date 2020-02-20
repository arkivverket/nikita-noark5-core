package nikita.common.model.noark5.v5.nationalidentifier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.SocialSecurityNumberHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier.ISocialSecurityNumberEntity;
import nikita.common.util.deserialisers.nationalidentifier.SocialSecurityNumberDeserializer;
import nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier.SocialSecurityNumberSerializer;
import nikita.webapp.hateoas.nationalidentifier.SocialSecurityNumberHateoasHandler;
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
import static nikita.common.config.Constants.REL_FONDS_STRUCTURE_SOCIAL_SECURITY_NUMBER;
import static nikita.common.config.Constants.TABLE_SOCIAL_SECURITY_NUMBER;
import static nikita.common.config.N5ResourceMappings.*;

@Entity
@Table(name = TABLE_SOCIAL_SECURITY_NUMBER)
@Inheritance(strategy = JOINED)
@JsonSerialize(using = SocialSecurityNumberSerializer.class)
@JsonDeserialize(using = SocialSecurityNumberDeserializer.class)
@HateoasPacker(using = SocialSecurityNumberHateoasHandler.class)
@HateoasObject(using = SocialSecurityNumberHateoas.class)
public class SocialSecurityNumber
        extends PersonIdentifier
        implements ISocialSecurityNumberEntity {
    /**
     * M??? - foedselsnummer (xs:string)
     */
    @Column(name = SOCIAL_SECURITY_NUMBER_ENG)
    @Audited
    @JsonProperty(SOCIAL_SECURITY_NUMBER)
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
    public String getBaseTypeName() {
        return SOCIAL_SECURITY_NUMBER;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_SOCIAL_SECURITY_NUMBER;
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
