package nikita.common.model.noark5.v5.secondary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import nikita.common.model.noark5.v5.*;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.metadata.ElectronicSignatureSecurityLevel;
import nikita.common.model.noark5.v5.metadata.ElectronicSignatureVerified;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = TABLE_ELECTRONIC_SIGNATURE)
public class ElectronicSignature
        extends NoarkEntity
        implements ISystemId, Comparable<SystemIdEntity> {

    // Links to ChangeLog
    @OneToMany(mappedBy = "referenceSystemIdEntity")
    @JsonIgnore
    private final List<ChangeLog> referenceChangeLog = new ArrayList<>();
    /**
     * M001 - systemID (xs:string)
     */
    @Id
    @Column(name = SYSTEM_ID_ENG, updatable = false, nullable = false)
    @Type(type = "uuid-char")
    private UUID systemId;
    /**
     * M622 - verifisertDato (xs:date)
     */
    @Column(name = ELECTRONIC_SIGNATURE_VERIFIED_DATE_ENG)
    @JsonProperty(ELECTRONIC_SIGNATURE_VERIFIED_DATE)
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    private OffsetDateTime verifiedDate;
    /**
     * M623 - verifisertAv (xs:string)
     */
    @Column(name = ELECTRONIC_SIGNATURE_VERIFIED_BY_ENG)
    @JsonProperty(ELECTRONIC_SIGNATURE_VERIFIED_BY)
    @Audited
    private String verifiedBy;
    /**
     * M??? - elektronisksignatursikkerhetsnivaa code (xs:string)
     */
    @Column(name = ELECTRONIC_SIGNATURE_SECURITY_LEVEL_CODE_ENG)
    @JsonProperty(ELECTRONIC_SIGNATURE_SECURITY_LEVEL_CODE)
    @Audited
    private String electronicSignatureSecurityLevelCode;
    /**
     * M507 - elektronisksignatursikkerhetsnivaa code name (xs:string)
     */
    @Column(name = ELECTRONIC_SIGNATURE_SECURITY_LEVEL_CODE_NAME_ENG)
    @JsonProperty(ELECTRONIC_SIGNATURE_SECURITY_LEVEL_CODE_NAME)
    @Audited
    private String electronicSignatureSecurityLevelCodeName;
    /**
     * M??? - elektronisksignaturverifisert code (xs:string)
     */
    @Column(name = ELECTRONIC_SIGNATURE_VERIFIED_CODE_ENG)
    @JsonProperty(ELECTRONIC_SIGNATURE_VERIFIED_CODE)
    @Audited
    private String electronicSignatureVerifiedCode;

    // Link to RegistryEntry
    @OneToOne(fetch = LAZY)
    @MapsId
    @JoinColumn(name = PRIMARY_KEY_SYSTEM_ID)
    private RegistryEntry referenceRegistryEntry;

    // Link to DocumentObject
    @OneToOne(fetch = LAZY)
    @MapsId
    @JoinColumn(name = PRIMARY_KEY_SYSTEM_ID)
    private DocumentObject referenceDocumentObject;

    // Link to DocumentDescription
    @OneToOne(fetch = LAZY)
    @MapsId
    @JoinColumn(name = PRIMARY_KEY_SYSTEM_ID)
    private DocumentDescription referenceDocumentDescription;

    public ElectronicSignatureSecurityLevel
    getElectronicSignatureSecurityLevel() {
        if (null == electronicSignatureSecurityLevelCode)
            return null;
        return new ElectronicSignatureSecurityLevel
                (electronicSignatureSecurityLevelCode,
                        electronicSignatureSecurityLevelCodeName);
    }

    public void setElectronicSignatureSecurityLevel(
            ElectronicSignatureSecurityLevel electronicSignatureSecurityLevel) {
        if (null != electronicSignatureSecurityLevel) {
            this.electronicSignatureSecurityLevelCode =
                    electronicSignatureSecurityLevel.getCode();
            this.electronicSignatureSecurityLevelCodeName =
                    electronicSignatureSecurityLevel.getCodeName();
        } else {
            this.electronicSignatureSecurityLevelCode = null;
            this.electronicSignatureSecurityLevelCodeName = null;
        }
    }

    public ElectronicSignatureVerified getElectronicSignatureVerified() {
        if (null == electronicSignatureVerifiedCode)
            return null;
        return new ElectronicSignatureVerified
                (electronicSignatureVerifiedCode,
                        electronicSignatureVerifiedCodeName);
    }

    public void setElectronicSignatureVerified(
            ElectronicSignatureVerified electronicSignatureVerified) {
        if (null != electronicSignatureVerified) {
            this.electronicSignatureVerifiedCode =
                    electronicSignatureVerified.getCode();
            this.electronicSignatureVerifiedCodeName =
                    electronicSignatureVerified.getCodeName();
        } else {
            this.electronicSignatureVerifiedCode = null;
            this.electronicSignatureVerifiedCodeName = null;
        }
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

    /**
     * M508 - elektronisksignaturverifisert code name (xs:string)
     */
    @Column(name = ELECTRONIC_SIGNATURE_VERIFIED_CODE_NAME_ENG)
    @JsonProperty(ELECTRONIC_SIGNATURE_VERIFIED_CODE_NAME + "." + CODE_NAME)
    @Audited
    private String electronicSignatureVerifiedCodeName;

    @Override
    public UUID getSystemId() {
        return systemId;
    }

    @Override
    public void setSystemId(UUID systemId) {
        this.systemId = systemId;
    }

    @Override
    public String getSystemIdAsString() {
        if (null != systemId)
            return systemId.toString();
        else
            return null;
    }

    @Override
    public String getIdentifier() {
        return getSystemIdAsString();
    }

    @Override
    public String getIdentifierType() {
        return SYSTEM_ID;
    }

    // Most entities belong to arkivstruktur. These entities pick the value
    // up here
    @Override
    public String getFunctionalTypeName() {
        return NOARK_FONDS_STRUCTURE_PATH;
    }

    @Override
    public void createReference(
            @NotNull INoarkEntity entity,
            @NotNull String referenceType) {
        // I really should be overridden. Currently throwing an Exception if I
        // am not overriden as nikita is unable to process this
        throw new NikitaMalformedInputDataException("Error when trying to " +
                "create a reference between entities");
    }

    @Override
    public int compareTo(@NotNull SystemIdEntity otherEntity) {
        if (null == otherEntity) {
            return -1;
        }
        return new CompareToBuilder()
                .append(this.systemId, otherEntity.getSystemId())
                .toComparison();
    }
}
