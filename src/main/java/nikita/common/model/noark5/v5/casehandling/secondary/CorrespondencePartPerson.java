package nikita.common.model.noark5.v5.casehandling.secondary;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartPersonHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ICorrespondencePartPersonEntity;
import nikita.common.util.deserialisers.casehandling.CorrespondencePartPersonDeserializer;
import nikita.webapp.hateoas.casehandling.CorrespondencePartPersonHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CORRESPONDENCE_PART_PERSON;

@Entity
@Table(name = TABLE_CORRESPONDENCE_PART_PERSON)
@JsonDeserialize(using = CorrespondencePartPersonDeserializer.class)
@HateoasPacker(using = CorrespondencePartPersonHateoasHandler.class)
@HateoasObject(using = CorrespondencePartPersonHateoas.class)
public class CorrespondencePartPerson
        extends CorrespondencePart
        implements ICorrespondencePartPersonEntity {

    /**
     * M??? - fødselsnummer (xs:string)
     */
    @Column(name = "social_security_number")
    @Audited
    private String socialSecurityNumber;

    /**
     * M??? - DNummer (xs:string)
     */
    @Column(name = "d_number")
    @Audited
    private String dNumber;

    /**
     * M400 - korrespondansepartNavn (xs:string)
     * Interface standard lists this as name. Using name until clarification
     * is provided
     */
    @Audited
    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "correspondencePartPerson", cascade = ALL)
    @JoinColumn(name = PRIMARY_KEY_SYSTEM_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private PostalAddress postalAddress;

    @OneToOne(mappedBy = "correspondencePartPerson", cascade = ALL)
    @JoinColumn(name = PRIMARY_KEY_SYSTEM_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private ResidingAddress residingAddress;

    @OneToOne(mappedBy = "correspondencePartPerson", cascade = ALL)
    @JoinColumn(name = PRIMARY_KEY_SYSTEM_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private ContactInformation contactInformation;

    @Override
    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    @Override
    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    @Override
    public String getdNumber() {
        return dNumber;
    }

    @Override
    public void setdNumber(String dNumber) {
        this.dNumber = dNumber;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public ContactInformation getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(ContactInformation contactInformation) {
        this.contactInformation = contactInformation;
    }

    public ResidingAddress getResidingAddress() {
        return residingAddress;
    }

    public void setResidingAddress(ResidingAddress residingAddress) {
        this.residingAddress = residingAddress;
    }

    public PostalAddress getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
    }

    @Override
    public String getBaseTypeName() {
        return CORRESPONDENCE_PART_PERSON;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_CORRESPONDENCE_PART_PERSON;
    }

    @Override
    public String toString() {
        return "CorrespondencePartPerson{" + super.toString() +
                "socialSecurityNumber='" + socialSecurityNumber + '\'' +
                ", dNumber='" + dNumber + '\'' +
                ", name='" + name + '\'' +
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
        CorrespondencePartPerson rhs = (CorrespondencePartPerson) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(socialSecurityNumber, rhs.socialSecurityNumber)
                .append(dNumber, rhs.dNumber)
                .append(name, rhs.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(socialSecurityNumber)
                .append(dNumber)
                .append(name)
                .toHashCode();
    }
}
