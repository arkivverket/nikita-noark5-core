package nikita.common.model.noark5.v5.casehandling.secondary;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartUnitHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ICorrespondencePartUnitEntity;
import nikita.common.util.deserialisers.casehandling.CorrespondencePartUnitDeserializer;
import nikita.webapp.hateoas.casehandling.CorrespondencePartUnitHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.REL_FONDS_STRUCTURE_CORRESPONDENCE_PART_UNIT;
import static nikita.common.config.Constants.TABLE_CORRESPONDENCE_PART_UNIT;
import static nikita.common.config.N5ResourceMappings.CORRESPONDENCE_PART_UNIT;

@Entity
@Table(name = TABLE_CORRESPONDENCE_PART_UNIT)
@JsonDeserialize(using = CorrespondencePartUnitDeserializer.class)
@HateoasPacker(using = CorrespondencePartUnitHateoasHandler.class)
@HateoasObject(using = CorrespondencePartUnitHateoas.class)
public class CorrespondencePartUnit
        extends CorrespondencePart
        implements ICorrespondencePartUnitEntity {

    /**
     * M??? - enhetsidentifikator (xs:string)
     */
    @Column(name = "unit_identifier")
    @Audited
    private String unitIdentifier;

    /**
     * M??? - navn (xs:string)
     */
    @Column(name = "name")
    @Audited
    private String name;

    /**
     * M412 - kontaktperson  (xs:string)
     */
    @Column(name = "contact_person")
    @Audited
    private String contactPerson;

    @OneToOne(mappedBy = "correspondencePartUnit", fetch = LAZY, cascade = ALL)
    private PostalAddress postalAddress;

    @OneToOne(mappedBy = "correspondencePartUnit", fetch = LAZY, cascade = ALL)
    private BusinessAddress businessAddress;

    @OneToOne(mappedBy = "correspondencePartUnit", fetch = LAZY, cascade = ALL)
    private ContactInformation contactInformation;

    public String getUnitIdentifier() {
        return unitIdentifier;
    }

    public void setUnitIdentifier(String unitIdentifier) {
        this.unitIdentifier = unitIdentifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ContactInformation getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(ContactInformation contactInformation) {
        this.contactInformation = contactInformation;
        contactInformation.setCorrespondencePartUnit(this);
    }

    public BusinessAddress getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(BusinessAddress businessAddress) {
        this.businessAddress = businessAddress;
        this.businessAddress.setCorrespondencePartUnit(this);
    }

    public PostalAddress getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
        this.postalAddress.setCorrespondencePartUnit(this);
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    @Override
    public String getBaseTypeName() {
        return CORRESPONDENCE_PART_UNIT;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_CORRESPONDENCE_PART_UNIT;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", unitIdentifier='" + unitIdentifier + '\'' +
                ", name='" + name + '\'' +
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
        CorrespondencePartUnit rhs = (CorrespondencePartUnit) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(unitIdentifier, rhs.unitIdentifier)
                .append(name, rhs.name)
                .append(contactPerson, rhs.contactPerson)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(unitIdentifier)
                .append(name)
                .append(contactPerson)
                .toHashCode();
    }
}
