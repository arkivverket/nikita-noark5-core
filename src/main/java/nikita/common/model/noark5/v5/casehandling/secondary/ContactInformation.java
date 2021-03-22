package nikita.common.model.noark5.v5.casehandling.secondary;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IContactInformationEntity;
import nikita.common.model.noark5.v5.secondary.PartPerson;
import nikita.common.model.noark5.v5.secondary.PartUnit;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.TABLE_CONTACT_INFORMATION;
import static nikita.common.config.N5ResourceMappings.SYSTEM_ID_ENG;

@Entity
@Table(name = TABLE_CONTACT_INFORMATION)
public class ContactInformation
        extends NoarkEntity
        implements ISystemId, IContactInformationEntity {

    /**
     * M001 - systemID (xs:string)
     */
    @Id
    @Type(type = "uuid-char")
    @Column(name = SYSTEM_ID_ENG, insertable = false, updatable = false,
            nullable = false)
    private UUID systemId;

    @Override
    public String getSystemId() {
        if (null != systemId)
            return systemId.toString();
        else
            return null;
    }

    @Override
    public void setSystemId(UUID systemId) {
        this.systemId = systemId;
    }

    @Override
    public UUID getId() {
        return systemId;
    }

    @Override
    public void setId(UUID systemId) {
        this.systemId = systemId;
    }

    @Override
    public String getIdentifier() {
        return getSystemId();
    }

    /**
     * M410 - epostadresse (xs:string)
     */
    @Audited
    @Column(name = "email_address")
    private String emailAddress;

    /**
     * M??? - mobiltelefon (xs:string)
     */
    @Column(name = "mobile_telephone_number")
    @Audited
    private String mobileTelephoneNumber;

    /**
     * M411 - telefonnummer (xs:string)
     */
    @Column(name = "telephone_number")
    @Audited
    private String telephoneNumber;

    @OneToOne(fetch = LAZY)
    private CorrespondencePartPerson correspondencePartPerson;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = SYSTEM_ID_ENG)
    @MapsId
    private CorrespondencePartUnit correspondencePartUnit;

    @OneToOne(fetch = LAZY)
    private PartPerson partPerson;

    @OneToOne(fetch = LAZY)
    private PartUnit partUnit;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMobileTelephoneNumber() {
        return mobileTelephoneNumber;
    }

    public void setMobileTelephoneNumber(String mobileTelephoneNumber) {
        this.mobileTelephoneNumber = mobileTelephoneNumber;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public CorrespondencePartPerson getCorrespondencePartPerson() {
        return correspondencePartPerson;
    }

    public void setCorrespondencePartPerson(
            CorrespondencePartPerson correspondencePartPerson) {
        this.correspondencePartPerson = correspondencePartPerson;
    }

    public CorrespondencePartUnit getCorrespondencePartUnit() {
        return correspondencePartUnit;
    }

    public void setCorrespondencePartUnit(
            CorrespondencePartUnit correspondencePartUnit) {
        this.correspondencePartUnit = correspondencePartUnit;
    }

    public PartPerson getPartPerson() {
        return partPerson;
    }

    public void setPartPerson(PartPerson partPerson) {
        this.partPerson = partPerson;
    }

    public PartUnit getPartUnit() {
        return partUnit;
    }

    public void setPartUnit(PartUnit partUnit) {
        this.partUnit = partUnit;
    }

    @Override
    public String toString() {
        return "ContactInformation{" + super.toString() +
                ", emailAddress='" + emailAddress + '\'' +
                ", mobileTelephoneNumber='" + mobileTelephoneNumber + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
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
        ContactInformation rhs = (ContactInformation) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(emailAddress, rhs.emailAddress)
                .append(mobileTelephoneNumber, rhs.mobileTelephoneNumber)
                .append(telephoneNumber, rhs.telephoneNumber)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(emailAddress)
                .append(mobileTelephoneNumber)
                .append(telephoneNumber)
                .toHashCode();
    }
}
