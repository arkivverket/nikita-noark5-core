package nikita.common.model.noark5.v5;

import nikita.common.config.Constants;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.interfaces.entities.IPartyEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static nikita.common.config.Constants.PRIMARY_KEY_PART;

/**
 * Created by tsodring on 4/10/16.
 */

@Entity
@Table(name = "party")
@AttributeOverride(name = "id",
        column = @Column(name = PRIMARY_KEY_PART))
public class Party
        extends NoarkGeneralEntity
        implements IPartyEntity {

    /**
     * M010 - partID (xs:string)
     */
    @Column(name = "part_id")
    @Audited
    private String partyId;

    /**
     * M302 - partNavn (xs:string)
     */
    @Column(name = "part_name")
    @Audited
    private String partyName;

    /**
     * M303 - partRolle (xs:string)
     */
    @Column(name = "part_role")
    @Audited
    private String partyRole;

    /**
     * M406 - postadresse (xs:string)
     */
    @Column(name = "postal_address")
    @Audited
    private String postalAddress;

    /**
     * M407 - postnummer (xs:string)
     */
    @Column(name = "postal_code")
    @Audited
    private String postCode;

    /**
     * M408 - poststed (xs:string)
     */
    @Column(name = "postal_town")
    @Audited
    private String postalTown;

    /**
     * M409 - utenlandsadresse (xs:string)
     */
    @Column(name = "foreign_address")
    @Audited
    private String foreignAddress;

    /**
     * M410 - epostadresse (xs:string)
     */
    @Column(name = "email_address")
    @Audited
    private String emailAddress;

    /**
     * M411 - telefonnummer (xs:string)
     * TODO: This is a multi-value attributte, that needs to implemented properly
     */
    @Column(name = "telephone_number")
    @Audited
    private String telephoneNumber;

    /**
     * M412 - kontaktperson (xs:string)
     */
    @Column(name = "contact_person")
    @Audited
    private String contactPerson;

    // Links to Files
    @ManyToMany(mappedBy = "referenceParty")
    private List<File> referenceFile = new ArrayList<>();

    // Links to Record
    @ManyToMany(mappedBy = "referenceParty")
    private List<Record> referenceRecord = new ArrayList<>();

    // Links to DocumentDescriptions
    @ManyToMany(mappedBy = "referenceParty")
    private List<DocumentDescription> referenceDocumentDescription =
            new ArrayList<>();

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPartyRole() {
        return partyRole;
    }

    public void setPartyRole(String partyRole) {
        this.partyRole = partyRole;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPostalTown() {
        return postalTown;
    }

    public void setPostalTown(String postalTown) {
        this.postalTown = postalTown;
    }

    public String getForeignAddress() {
        return foreignAddress;
    }

    public void setForeignAddress(String foreignAddress) {
        this.foreignAddress = foreignAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    @Override
    public String getBaseTypeName() {
        return N5ResourceMappings.PART;
    }

    @Override
    public String getFunctionalTypeName() {
        return Constants.NOARK_CASE_HANDLING_PATH;
    }

    public List<File> getReferenceFile() {
        return referenceFile;
    }

    public void setReferenceFile(List<File> referenceFile) {
        this.referenceFile = referenceFile;
    }

    public void addReferenceFile(File file) {
        this.referenceFile.add(file);
    }

    public List<Record> getReferenceRecord() {
        return referenceRecord;
    }

    public void setReferenceRecord(List<Record> referenceRecord) {
        this.referenceRecord = referenceRecord;
    }

    public void addReferenceRecord(Record record) {
        this.referenceRecord.add(record);
    }

    public List<DocumentDescription> getReferenceDocumentDescription() {
        return referenceDocumentDescription;
    }

    public void setReferenceDocumentDescription(
            List<DocumentDescription> referenceDocumentDescription) {
        this.referenceDocumentDescription = referenceDocumentDescription;
    }

    public void addReferenceDocumentDescription(
            DocumentDescription documentDescription) {
        this.referenceDocumentDescription.add(documentDescription);
    }

    @Override
    public String toString() {
        return "Party{" + super.toString() +
                ", partyId='" + partyId + '\'' +
                ", partyName='" + partyName + '\'' +
                ", partyRole='" + partyRole + '\'' +
                ", postalAddress='" + postalAddress + '\'' +
                ", postCode='" + postCode + '\'' +
                ", postalTown='" + postalTown + '\'' +
                ", foreignAddress='" + foreignAddress + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
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
        Party rhs = (Party) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(partyId, rhs.partyId)
                .append(partyName, rhs.partyName)
                .append(partyRole, rhs.partyRole)
                .append(postalAddress, rhs.postalAddress)
                .append(postCode, rhs.postCode)
                .append(postalTown, rhs.postalTown)
                .append(foreignAddress, rhs.foreignAddress)
                .append(emailAddress, rhs.emailAddress)
                .append(telephoneNumber, rhs.telephoneNumber)
                .append(contactPerson, rhs.contactPerson)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(partyId)
                .append(partyName)
                .append(partyRole)
                .append(postalAddress)
                .append(postCode)
                .append(postalTown)
                .append(foreignAddress)
                .append(emailAddress)
                .append(telephoneNumber)
                .append(contactPerson)
                .toHashCode();
    }
}
