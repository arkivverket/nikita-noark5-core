package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.casehandling.secondary.ContactInformation;
import nikita.common.model.noark5.v5.casehandling.secondary.PostalAddress;
import nikita.common.model.noark5.v5.casehandling.secondary.ResidingAddress;
import nikita.common.model.noark5.v5.hateoas.PartPersonHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IPartPersonEntity;
import nikita.common.util.deserialisers.PartPersonDeserializer;
import nikita.webapp.hateoas.PartPersonHateoasHandler;
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
import static nikita.common.config.Constants.*;
import static nikita.common.config.Constants.PRIMARY_KEY_SYSTEM_ID;
import static nikita.common.config.N5ResourceMappings.PART_PERSON;

@Entity
@Table(name = TABLE_PART_PERSON)
@JsonDeserialize(using = PartPersonDeserializer.class)
@HateoasPacker(using = PartPersonHateoasHandler.class)
@HateoasObject(using = PartPersonHateoas.class)
public class PartPerson
        extends Part
        implements IPartPersonEntity {

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

    @OneToOne(mappedBy = "partPerson", cascade = ALL)
    @JoinColumn(name = PRIMARY_KEY_SYSTEM_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private PostalAddress postalAddress;

    @OneToOne(mappedBy = "partPerson", cascade = ALL)
    @JoinColumn(name = PRIMARY_KEY_SYSTEM_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private ResidingAddress residingAddress;

    @OneToOne(mappedBy = "partPerson", cascade = ALL)
    @JoinColumn(name = PRIMARY_KEY_SYSTEM_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private ContactInformation contactInformation;

    // Links to Record
    @ManyToMany(mappedBy = "referencePartPerson")
    private List<Record> referenceRecord = new ArrayList<>();

    @Override
    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getdNumber() {
        return dNumber;
    }

    public void setdNumber(String dNumber) {
        this.dNumber = dNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PostalAddress getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
    }

    public ResidingAddress getResidingAddress() {
        return residingAddress;
    }

    public void setResidingAddress(ResidingAddress residingAddress) {
        this.residingAddress = residingAddress;
    }

    public ContactInformation getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(ContactInformation contactInformation) {
        this.contactInformation = contactInformation;
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
    public void addRecord(Record record) {
        this.referenceRecord.add(record);
    }

    @Override
    public String getBaseTypeName() {
        return PART_PERSON;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_PART_PERSON;
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
        PartPerson rhs = (PartPerson) other;
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
