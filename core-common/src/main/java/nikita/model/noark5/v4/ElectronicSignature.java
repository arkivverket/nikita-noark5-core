package nikita.model.noark5.v4;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Date;

import static nikita.config.N5ResourceMappings.ELECTRONIC_SIGNATURE;

@Entity
@Table(name = "electronic_signature")
// Enable soft delete of ElectronicSignature
@SQLDelete(sql="UPDATE electronic_signature SET deleted = true WHERE id = ?")
@Where(clause="deleted <> true")
@AttributeOverride(name = "id", column = @Column(name = "pk_electronic_signature_id"))
public class ElectronicSignature extends NoarkEntity {

    /**
     * M507 - elektronisksignatursikkerhetsnivaa (xs:string)
     */
    @Column(name = "electronic_signature_security_level")
    @Audited
    private String electronicSignatureSecurityLevel;

    /**
     * M508 - elektronisksignaturverifisert (xs:string)
     */
    @Column(name = "electronic_signature_verified")
    @Audited
    private String electronicSignatureVerified;

    /**
     * M622 - verifisertDato (xs:date)
     */
    @Column(name = "verified_date")
    @Audited
    private Date verifiedDate;

    /**
     * M623 - verifisertAv (xs:string)
     */
    @Column(name = "verified_by")
    @Audited
    private String verifiedBy;

    // Link to RegistryEntry
    @OneToOne
    @JoinColumn(name="pk_record_id")
    private RegistryEntry referenceRegistryEntry;

    // Link to DocumentObject
    @OneToOne
    @JoinColumn(name="pk_document_object_id")
    private DocumentObject referenceDocumentObject;

    // Link to DocumentDescription
    @OneToOne
    @JoinColumn(name="pk_document_description_id")
    private DocumentDescription referenceDocumentDescription;

    public String getElectronicSignatureSecurityLevel() {
        return electronicSignatureSecurityLevel;
    }

    public void setElectronicSignatureSecurityLevel(String electronicSignatureSecurityLevel) {
        this.electronicSignatureSecurityLevel = electronicSignatureSecurityLevel;
    }

    public String getElectronicSignatureVerified() {
        return electronicSignatureVerified;
    }

    public void setElectronicSignatureVerified(String electronicSignatureVerified) {
        this.electronicSignatureVerified = electronicSignatureVerified;
    }

    public Date getVerifiedDate() {
        return verifiedDate;
    }

    public void setVerifiedDate(Date verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    @Override
    public String getBaseTypeName() {
        return ELECTRONIC_SIGNATURE;
    }

    public RegistryEntry getReferenceRegistryEntry() {
        return referenceRegistryEntry;
    }

    public void setReferenceRegistryEntry(RegistryEntry referenceRegistryEntry) {
        this.referenceRegistryEntry = referenceRegistryEntry;
    }

    public DocumentObject getReferenceDocumentObject() {
        return referenceDocumentObject;
    }

    public void setReferenceDocumentObject(DocumentObject referenceDocumentObject) {
        this.referenceDocumentObject = referenceDocumentObject;
    }

    public DocumentDescription getReferenceDocumentDescription() {
        return referenceDocumentDescription;
    }

    public void setReferenceDocumentDescription(DocumentDescription referenceDocumentDescription) {
        this.referenceDocumentDescription = referenceDocumentDescription;
    }

    @Override
    public String toString() {
        return "ElectronicSignature{" + super.toString() + 
                ", electronicSignatureSecurityLevel='" + electronicSignatureSecurityLevel + '\'' +
                ", electronicSignatureVerified='" + electronicSignatureVerified + '\'' +
                '}';
    }
}
