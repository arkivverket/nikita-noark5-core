package nikita.common.model.noark5.v5.secondary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.casehandling.secondary.BusinessAddress;
import nikita.common.model.noark5.v5.casehandling.secondary.ContactInformation;
import nikita.common.model.noark5.v5.casehandling.secondary.PostalAddress;
import nikita.common.model.noark5.v5.hateoas.secondary.PartUnitHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IPartUnitEntity;
import nikita.common.util.deserialisers.secondary.PartUnitDeserializer;
import nikita.webapp.hateoas.secondary.PartUnitHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

@Entity
@Table(name = TABLE_PART_UNIT)
@JsonDeserialize(using = PartUnitDeserializer.class)
@HateoasPacker(using = PartUnitHateoasHandler.class)
@HateoasObject(using = PartUnitHateoas.class)
@Indexed
public class PartUnit
        extends Part
        implements IPartUnitEntity {

    /**
     * M??? - organisasjonsnummer (xs:string)
     */
    @Column(name = ORGANISATION_NUMBER_ENG)
    @Audited
    @JsonProperty(ORGANISATION_NUMBER)
    @KeywordField
    private String organisationNumber;

    /**
     * M412 - kontaktperson  (xs:string)
     */
    @Column(name = CONTACT_PERSON_ENG)
    @Audited
    @JsonProperty(CONTACT_PERSON)
    @FullTextField
    private String contactPerson;

    @OneToOne(mappedBy = "partUnit", fetch = LAZY, cascade = ALL)
    @JoinColumn(name = PRIMARY_KEY_SYSTEM_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private PostalAddress postalAddress;

    @OneToOne(mappedBy = "partUnit", fetch = LAZY, cascade = ALL)
    @JoinColumn(name = PRIMARY_KEY_SYSTEM_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private BusinessAddress businessAddress;

    @OneToOne(mappedBy = "partUnit", fetch = LAZY, cascade = ALL)
    @JoinColumn(name = PRIMARY_KEY_SYSTEM_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private ContactInformation contactInformation;

    public String getUnitIdentifier() {
        return organisationNumber;
    }

    public void setUnitIdentifier(String organisationNumber) {
        this.organisationNumber = organisationNumber;
    }

    public ContactInformation getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(ContactInformation contactInformation) {
        this.contactInformation = contactInformation;
    }

    public BusinessAddress getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(BusinessAddress businessAddress) {
        this.businessAddress = businessAddress;
    }

    public PostalAddress getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    @Override
    public String getBaseTypeName() {
        return PART_UNIT;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_PART;
    }

    @Override
    public String toString() {
        return "PartUnit{" + super.toString() +
                ", organisationNumber='" + organisationNumber + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
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
        PartUnit rhs = (PartUnit) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(organisationNumber, rhs.organisationNumber)
                .append(contactPerson, rhs.contactPerson)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(organisationNumber)
                .append(contactPerson)
                .toHashCode();
    }
}
