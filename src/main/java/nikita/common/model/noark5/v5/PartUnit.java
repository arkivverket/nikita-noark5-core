package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.casehandling.secondary.BusinessAddress;
import nikita.common.model.noark5.v5.casehandling.secondary.ContactInformation;
import nikita.common.model.noark5.v5.casehandling.secondary.PostalAddress;
import nikita.common.model.noark5.v5.hateoas.PartUnitHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IPartUnitEntity;
import nikita.common.util.deserialisers.PartUnitDeserializer;
import nikita.webapp.hateoas.PartUnitHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.REL_FONDS_STRUCTURE_PART;
import static nikita.common.config.Constants.TABLE_PART_UNIT;
import static nikita.common.config.N5ResourceMappings.PART_UNIT;

@Entity
@Table(name = TABLE_PART_UNIT)
@JsonDeserialize(using = PartUnitDeserializer.class)
@HateoasPacker(using = PartUnitHateoasHandler.class)
@HateoasObject(using = PartUnitHateoas.class)
public class PartUnit
        extends Part
        implements IPartUnitEntity {

    /**
     * M??? - organisasjonsnummer (xs:string)
     */
    @Column(name = "organisation_number")
    @Audited
    private String organisationNumber;

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

    @OneToOne(mappedBy = "partUnit",
            fetch = LAZY, cascade = ALL)
    private PostalAddress postalAddress;

    @OneToOne(mappedBy = "partUnit",
            fetch = LAZY, cascade = ALL)
    private BusinessAddress businessAddress;

    @OneToOne(mappedBy = "partUnit",
            fetch = LAZY, cascade = ALL)
    private ContactInformation contactInformation;

    // Links to Record
    @ManyToMany(mappedBy = "referencePartUnit")
    private List<Record> referenceRecord = new ArrayList<>();

    public String getOrganisationNumber() {
        return organisationNumber;
    }

    public void setOrganisationNumber(String organisationNumber) {
        this.organisationNumber = organisationNumber;
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
    public List<Record> getReferenceRecord() {
        return referenceRecord;
    }

    @Override
    public void setReferenceRecord(
            List<Record> referenceRecord) {
        this.referenceRecord = referenceRecord;
    }

    @Override
    public void addRecord(Record referenceRecord) {
        this.referenceRecord.add(referenceRecord);
    }

    @Override
    public String toString() {
        return super.toString() +
                ", organisationNumber='" + organisationNumber + '\'' +
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
        PartUnit rhs = (PartUnit) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(organisationNumber, rhs.organisationNumber)
                .append(name, rhs.name)
                .append(contactPerson, rhs.contactPerson)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(organisationNumber)
                .append(name)
                .append(contactPerson)
                .toHashCode();
    }
}
