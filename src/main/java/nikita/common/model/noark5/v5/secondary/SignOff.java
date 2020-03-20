package nikita.common.model.noark5.v5.secondary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart;
import nikita.common.model.noark5.v5.hateoas.secondary.SignOffHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ISignOffEntity;
import nikita.common.model.noark5.v5.metadata.SignOffMethod;
import nikita.common.util.deserialisers.secondary.SignOffDeserializer;
import nikita.webapp.hateoas.secondary.SignOffHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;


@Entity
@Table(name = TABLE_SIGN_OFF)
@JsonDeserialize(using = SignOffDeserializer.class)
@HateoasPacker(using = SignOffHateoasHandler.class)
@HateoasObject(using = SignOffHateoas.class)
public class SignOff
        extends SystemIdEntity
        implements ISignOffEntity {

    /**
     * M617 - avskrivningsdato
     */
    @Column(name = SIGN_OFF_DATE_ENG)
    @DateTimeFormat(iso = DATE)
    @Audited
    @JsonProperty(SIGN_OFF_DATE)
    private OffsetDateTime signOffDate;

    /**
     * M618 - avskrevetAv
     */
    @Column(name = SIGN_OFF_BY_ENG)
    @Audited
    @JsonProperty(SIGN_OFF_BY)
    private String signOffBy;

    /**
     * M??? - avskrivningsmaate code
     */
    @Column(name = "sign_off_method_code")
    @Audited
    private String signOffMethodCode;

    /**
     * M619 - avskrivningsmaate code name
     */
    @Column(name = "sign_off_method_code_name")
    @Audited
    private String signOffMethodCodeName;

    /**
     * M215 referanseAvskrivesAvJournalpost
     */
    @Column(name = "reference_record_id")
    @Audited
    @JsonProperty(SIGN_OFF_REFERENCE_RECORD)
    private UUID referenceSignedOffRecordSystemID;

    /**
     * M??? - referanseAvskrivesAvKorrespondansepart
     * Note this is missing in Noark v5. No Metadata number.
     * See https://github.com/arkivverket/schemas/issues/21
     */
    @Column(name = "reference_correspondence_part_id")
    @Audited
    @JsonProperty(SIGN_OFF_REFERENCE_CORRESPONDENCE_PART)
    private UUID referenceSignedOffCorrespondencePartSystemID;

    // Link to reference registry entry if present
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_record_id")
    private RegistryEntry referenceSignedOffRecord;

    // Link to reference correspondence part if present
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pk_correspondence_part_id")
    private CorrespondencePart referenceSignedOffCorrespondencePart;

    // Links to RegistryEntry
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sign_off_registry_entry_id",
                referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private RegistryEntry referenceRecord;

    @Override
    public OffsetDateTime getSignOffDate() {
        return signOffDate;
    }

    @Override
    public void setSignOffDate(OffsetDateTime signOffDate) {
        this.signOffDate = signOffDate;
    }

    @Override
    public String getSignOffBy() {
        return signOffBy;
    }

    @Override
    public void setSignOffBy(String signOffBy) {
        this.signOffBy = signOffBy;
    }

    // TODO get rid of ..Code() and ..CodeName().
    @Override
    public String getSignOffMethodCode() {
        return signOffMethodCode;
    }

    @Override
    public void setSignOffMethodCode(String signOffMethodCode) {
        this.signOffMethodCode = signOffMethodCode;
    }

    @Override
    public String getSignOffMethodCodeName() {
        return signOffMethodCodeName;
    }

    @Override
    public void setSignOffMethodCodeName(String signOffMethodCodeName) {
        this.signOffMethodCodeName = signOffMethodCodeName;
    }

    @Override
    public SignOffMethod getSignOffMethod() {
        if (null == signOffMethodCode)
            return null;
        return new SignOffMethod(signOffMethodCode, signOffMethodCodeName);
    }

    @Override
    public void setSignOffMethod(SignOffMethod signOffMethod) {
        if (null != signOffMethod) {
            this.signOffMethodCode = signOffMethod.getCode();
            this.signOffMethodCodeName = signOffMethod.getCodeName();
        } else {
            this.signOffMethodCode = null;
            this.signOffMethodCodeName = null;
        }
    }

    @Override
    public String getBaseTypeName() {
        return SIGN_OFF;
    }

    @Override
    public String getBaseRel() {
        return REL_CASE_HANDLING_SIGN_OFF;
    }

    @Override
    public UUID getReferenceSignedOffRecordSystemID() {
        return referenceSignedOffRecordSystemID;
    }

    @Override
    public void setReferenceSignedOffRecordSystemID(
            UUID referenceSignedOffRecordSystemID) {
        this.referenceSignedOffRecordSystemID =
            referenceSignedOffRecordSystemID;
    }

    @Override
    public UUID getReferenceSignedOffCorrespondencePartSystemID() {
        return referenceSignedOffCorrespondencePartSystemID;
    }

    @Override
    public void setReferenceSignedOffCorrespondencePartSystemID(
            UUID referenceSignedOffCorrespondencePartSystemID) {
        this.referenceSignedOffCorrespondencePartSystemID =
                referenceSignedOffCorrespondencePartSystemID;
    }

    @Override
    public RegistryEntry getReferenceSignedOffRecord() {
        return referenceSignedOffRecord;
    }

    @Override
    public void setReferenceSignedOffRecord(
            RegistryEntry referenceSignedOffRecord) {
        this.referenceSignedOffRecord = referenceSignedOffRecord;
    }

    @Override
    public CorrespondencePart getReferenceSignedOffCorrespondencePart() {
        return referenceSignedOffCorrespondencePart;
    }

    @Override
    public void setReferenceSignedOffCorrespondencePart(
            CorrespondencePart referenceSignedOffCorrespondencePart) {
        this.referenceSignedOffCorrespondencePart =
                referenceSignedOffCorrespondencePart;
    }

    @Override
    public RegistryEntry getReferenceRecord() {
        return referenceRecord;
    }

    @Override
    public void setReferenceRecord(RegistryEntry referenceRecord) {
        this.referenceRecord = referenceRecord;
    }

    @Override
    public String toString() {
        return "SignOff{" + super.toString() +
                "signOffMethodCode='" + signOffMethodCode + '\'' +
                "signOffMethodCodeName='" + signOffMethodCodeName + '\'' +
                ", signOffBy='" + signOffBy + '\'' +
                ", signOffDate=" + signOffDate +
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
        SignOff rhs = (SignOff) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(signOffMethodCode, rhs.signOffMethodCode)
                .append(signOffMethodCodeName, rhs.signOffMethodCodeName)
                .append(signOffBy, rhs.signOffBy)
                .append(signOffDate, rhs.signOffDate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(signOffMethodCode)
                .append(signOffMethodCodeName)
                .append(signOffBy)
                .append(signOffDate)
                .toHashCode();
    }
}
