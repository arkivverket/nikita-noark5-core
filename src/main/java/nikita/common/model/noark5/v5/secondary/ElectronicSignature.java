package nikita.common.model.noark5.v5.secondary;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@Entity
@Table(name = TABLE_ELECTRONIC_SIGNATURE)
public class ElectronicSignature
        extends SystemIdEntity {

    /**
     * M??? - elektronisksignatursikkerhetsnivaa code (xs:string)
     */
    @Column(name = "electronic_signature_security_level_code")
    @Audited
    private String electronicSignatureSecurityLevelCode;

    /**
     * M507 - elektronisksignatursikkerhetsnivaa code name (xs:string)
     */
    @Column(name = ELECTRONIC_SIGNATURE_SECURITY_LEVEL_FIELD_ENG)
    @Audited
    private String electronicSignatureSecurityLevelCodeName;

    /**
     * M??? - elektronisksignaturverifisert code (xs:string)
     */
    @Column(name = "electronic_signature_verified_code")
    @Audited
    private String electronicSignatureVerifiedCode;

    /**
     * M508 - elektronisksignaturverifisert code name (xs:string)
     */
    @Column(name = ELECTRONIC_SIGNATURE_VERIFIED_FIELD_ENG)
    @Audited
    private String electronicSignatureVerifiedCodeName;

    /**
     * M622 - verifisertDato (xs:date)
     */
    @Column(name = ELECTRONIC_SIGNATURE_VERIFIED_DATE_ENG)
    @DateTimeFormat(iso = DATE)
    @Audited
    private OffsetDateTime verifiedDate;

    /**
     * M623 - verifisertAv (xs:string)
     */
    @Column(name = ELECTRONIC_SIGNATURE_VERIFIED_BY_ENG)
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

    public String getElectronicSignatureSecurityLevelCode() {
        return electronicSignatureSecurityLevelCode;
    }

    public void setElectronicSignatureSecurityLevelCode(
            String electronicSignatureSecurityLevelCode) {
        this.electronicSignatureSecurityLevelCode =
                electronicSignatureSecurityLevelCode;
    }

    public String getElectronicSignatureSecurityLevelCodeName() {
        return electronicSignatureSecurityLevelCodeName;
    }

    public void setElectronicSignatureSecurityLevelCodeName(
            String electronicSignatureSecurityLevelCodeName) {
        this.electronicSignatureSecurityLevelCodeName =
                electronicSignatureSecurityLevelCodeName;
    }

    public String getElectronicSignatureVerifiedCode() {
        return electronicSignatureVerifiedCode;
    }

    public void setElectronicSignatureVerifiedCode(
            String electronicSignatureVerifiedCode) {
        this.electronicSignatureVerifiedCode =
            electronicSignatureVerifiedCode;
    }

    public String getElectronicSignatureVerifiedCodeName() {
        return electronicSignatureVerifiedCodeName;
    }

    public void setElectronicSignatureVerifiedCodeName(
            String electronicSignatureVerifiedCodeName) {
        this.electronicSignatureVerifiedCodeName =
            electronicSignatureVerifiedCodeName;
    }

    public OffsetDateTime getVerifiedDate() {
        return verifiedDate;
    }

    public void setVerifiedDate(OffsetDateTime verifiedDate) {
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

    @Override
    public String getBaseRel() {
        return ELECTRONIC_SIGNATURE; // TODO, should it have a relation key?
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
                ", electronicSignatureSecurityLevelCode='" +
                electronicSignatureSecurityLevelCode + '\'' +
                ", electronicSignatureSecurityLevelCodeName='" +
                electronicSignatureSecurityLevelCodeName + '\'' +
                ", electronicSignatureVerifiedCode='" +
                electronicSignatureVerifiedCode + '\'' +
                ", electronicSignatureVerifiedCodeName='" +
                electronicSignatureVerifiedCodeName + '\'' +
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
                .append(electronicSignatureSecurityLevelCode,
                        rhs.electronicSignatureSecurityLevelCode)
                .append(electronicSignatureSecurityLevelCodeName,
                        rhs.electronicSignatureSecurityLevelCodeName)
                .append(electronicSignatureVerifiedCode,
                        rhs.electronicSignatureVerifiedCode)
                .append(electronicSignatureVerifiedCodeName,
                        rhs.electronicSignatureVerifiedCodeName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(electronicSignatureSecurityLevelCode)
                .append(electronicSignatureSecurityLevelCodeName)
                .append(electronicSignatureVerifiedCode)
                .append(electronicSignatureVerifiedCodeName)
                .toHashCode();
    }
}
