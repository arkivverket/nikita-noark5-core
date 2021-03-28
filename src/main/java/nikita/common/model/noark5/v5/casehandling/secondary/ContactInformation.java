package nikita.common.model.noark5.v5.casehandling.secondary;

import com.fasterxml.jackson.annotation.JsonProperty;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IContactInformationEntity;
import nikita.common.model.noark5.v5.secondary.PartPerson;
import nikita.common.model.noark5.v5.secondary.PartUnit;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.TABLE_CONTACT_INFORMATION;
import static nikita.common.config.N5ResourceMappings.*;

@Entity
@Table(name = TABLE_CONTACT_INFORMATION)
public class ContactInformation
        extends SystemIdEntity
        implements ISystemId, IContactInformationEntity {

    /**
     * M410 - epostadresse (xs:string)
     */
    @Column(name = EMAIL_ADDRESS_ENG)
    @JsonProperty(EMAIL_ADDRESS)
    @Audited
    private String emailAddress;

    /**
     * M??? - mobiltelefon (xs:string)
     */
    @Column(name = MOBILE_TELEPHONE_NUMBER_ENG)
    @JsonProperty(MOBILE_TELEPHONE_NUMBER)
    @Audited
    private String mobileTelephoneNumber;

    /**
     * M411 - telefonnummer (xs:string)
     */
    @Column(name = TELEPHONE_NUMBER_ENG)
    @JsonProperty(TELEPHONE_NUMBER)
    @Audited
    private String telephoneNumber;

    @OneToOne(fetch = LAZY)
    private CorrespondencePartPerson referenceCorrespondencePartPerson;

    @OneToOne(fetch = LAZY)
    private CorrespondencePartUnit referenceCorrespondencePartUnit;

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
        return referenceCorrespondencePartPerson;
    }

    public void setCorrespondencePartPerson(
            CorrespondencePartPerson referenceCorrespondencePartPerson) {
        this.referenceCorrespondencePartPerson = referenceCorrespondencePartPerson;
    }

    public CorrespondencePartUnit getReferenceCorrespondencePartUnit() {
        return referenceCorrespondencePartUnit;
    }

    public void setCorrespondencePartUnit(
            CorrespondencePartUnit referenceCorrespondencePartUnit) {
        this.referenceCorrespondencePartUnit = referenceCorrespondencePartUnit;
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
