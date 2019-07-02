package nikita.common.model.noark5.v5.secondary;

import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.ZonedDateTime;

import static nikita.common.config.Constants.PRIMARY_KEY_SYSTEM_ID;
import static nikita.common.config.Constants.TABLE_ELECTRONIC_SIGNATURE;
import static nikita.common.config.N5ResourceMappings.ELECTRONIC_SIGNATURE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@Entity
@Table(name = TABLE_ELECTRONIC_SIGNATURE)
public class ElectronicSignature
        extends NoarkEntity {

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
    @DateTimeFormat(iso = DATE)
    @Audited
    private ZonedDateTime verifiedDate;

    /**
     * M623 - verifisertAv (xs:string)
     */
    @Column(name = "verified_by")
    @Audited
    private String verifiedBy;

    // Link to RegistryEntry
    @OneToOne
    @JoinColumn(name = PRIMARY_KEY_SYSTEM_ID)
    private RegistryEntry referenceRegistryEntry;

    // Link to DocumentObject
    @OneToOne
    @JoinColumn(name = PRIMARY_KEY_SYSTEM_ID)
    private DocumentObject referenceDocumentObject;

    // Link to DocumentDescription
    @OneToOne
    @JoinColumn(name = PRIMARY_KEY_SYSTEM_ID)
    private DocumentDescription referenceDocumentDescription;

    public String getElectronicSignatureSecurityLevel() {
        return electronicSignatureSecurityLevel;
    }

    public void setElectronicSignatureSecurityLevel(
            String electronicSignatureSecurityLevel) {
        this.electronicSignatureSecurityLevel =
                electronicSignatureSecurityLevel;
    }

    public String getElectronicSignatureVerified() {
        return electronicSignatureVerified;
    }

    public void setElectronicSignatureVerified(
            String electronicSignatureVerified) {
        this.electronicSignatureVerified = electronicSignatureVerified;
    }

    public ZonedDateTime getVerifiedDate() {
        return verifiedDate;
    }

    public void setVerifiedDate(ZonedDateTime verifiedDate) {
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

    public void setReferenceRegistryEntry(
            RegistryEntry referenceRegistryEntry) {
        this.referenceRegistryEntry = referenceRegistryEntry;
    }

    public DocumentObject getReferenceDocumentObject() {
        return referenceDocumentObject;
    }

    public void setReferenceDocumentObject(
            DocumentObject referenceDocumentObject) {
        this.referenceDocumentObject = referenceDocumentObject;
    }

    public DocumentDescription getReferenceDocumentDescription() {
        return referenceDocumentDescription;
    }

    public void setReferenceDocumentDescription(
            DocumentDescription referenceDocumentDescription) {
        this.referenceDocumentDescription = referenceDocumentDescription;
    }

    @Override
    public String toString() {
        return "ElectronicSignature{" + super.toString() +
                ", electronicSignatureSecurityLevel='" +
                electronicSignatureSecurityLevel + '\'' +
                ", electronicSignatureVerified='" +
                electronicSignatureVerified + '\'' +
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
        ElectronicSignature rhs = (ElectronicSignature) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(electronicSignatureSecurityLevel,
                        rhs.electronicSignatureSecurityLevel)
                .append(electronicSignatureVerified,
                        rhs.electronicSignatureVerified)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(electronicSignatureSecurityLevel)
                .append(electronicSignatureVerified)
                .toHashCode();
    }
}
