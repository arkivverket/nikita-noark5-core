package nikita.model.noark5.v4;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "correspondence_part")
// Enable soft delete of CorrespondencePart
@SQLDelete(sql="UPDATE correspondence_part SET deleted = true WHERE id = ?")
@Where(clause="deleted <> true")
public class CorrespondencePart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_correspondence_part_id", nullable = false, insertable = true, updatable = false)
    protected Long id;

    /** M087 - korrespondanseparttype (xs:string) */
    @Column(name = "correspondance_part_type")
    @Audited
    protected String correspondancePartType;

    /** M400 - korrespondansepartNavn (xs:string) */
    @Audited
    @Column(name = "correspondance_part_name")
    protected String correspondancePartName;

    /** M406 - postadresse (xs:string) */
    @Audited
    @Column(name = "postal_address")
    protected String postalAddress;

    /** M407 - postnummer (xs:string) */
    @Audited
    @Column(name = "post_code")
    protected String postCode;

    /** M408 - poststed (xs:string) */
    @Audited
    @Column(name = "postal_town")
    protected String postalTown;

    /** M409 - land (xs:string) */
    @Audited
    @Column(name = "country")
    protected String country;

    /** M410 - epostadresse (xs:string) */
    @Audited
    @Column(name = "email_address")
    protected String emailAddress;

    /** M411 - telefonnummer (xs:string) */
    @Column(name = "telephone_number")
    @Audited
    protected String telephoneNumber;

    /** M412 - kontaktperson (xs:string) */
    @Column(name = "contact_person")
    @Audited
    protected String contactPerson;

    /** M305 - administrativEnhet (xs:string) */
    @Column(name = "administrative_unit")
    @Audited
    protected String administrativeUnit;

    /** M307 - saksbehandler (xs:string) */
    @Column(name = "case_handler")
    @Audited
    protected String caseHandler;

    // Used for soft delete.
    @Column(name = "deleted")
    @Audited
    private Boolean deleted;

    @Column(name = "owned_by")
    @Audited
    protected String ownedBy;


    // Links to Record
    @ManyToMany(mappedBy = "referenceCorrespondencePart")
    protected Set<RegistryEntry> referenceRegistryEntry = new HashSet<RegistryEntry>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCorrespondancePartType() {
        return correspondancePartType;
    }

    public void setCorrespondancePartType(String correspondancePartType) {
        this.correspondancePartType = correspondancePartType;
    }

    public String getCorrespondancePartName() {
        return correspondancePartName;
    }

    public void setCorrespondancePartName(String correspondancePartName) {
        this.correspondancePartName = correspondancePartName;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getAdministrativeUnit() {
        return administrativeUnit;
    }

    public void setAdministrativeUnit(String administrativeUnit) {
        this.administrativeUnit = administrativeUnit;
    }

    public String getCaseHandler() {
        return caseHandler;
    }

    public void setCaseHandler(String caseHandler) {
        this.caseHandler = caseHandler;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
    }

    public Set<RegistryEntry> getReferenceRegistryEntry() {
        return referenceRegistryEntry;
    }

    public void setReferenceRegistryEntry(Set<RegistryEntry> referenceRegistryEntry) {
        this.referenceRegistryEntry = referenceRegistryEntry;
    }

    @Override
    public String toString() {
        return "CorrespondencePart{" +
                "caseHandler='" + caseHandler + '\'' +
                ", administrativeUnit='" + administrativeUnit + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", country='" + country + '\'' +
                ", postalTown='" + postalTown + '\'' +
                ", postCode='" + postCode + '\'' +
                ", postalAddress='" + postalAddress + '\'' +
                ", correspondancePartName='" + correspondancePartName + '\'' +
                ", correspondancePartType='" + correspondancePartType + '\'' +
                ", id=" + id +
                '}';
    }
}