package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.hateoas.ChangeLogHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IChangeLogEntity;
import nikita.common.util.deserialisers.ChangeLogDeserializer;
import nikita.webapp.hateoas.ChangeLogHateoasHandler;
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
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = TABLE_CHANGE_LOG)
@JsonDeserialize(using = ChangeLogDeserializer.class)
@HateoasPacker(using = ChangeLogHateoasHandler.class)
@HateoasObject(using = ChangeLogHateoas.class)
public class ChangeLog
    extends SystemIdEntity
    implements IChangeLogEntity
{

    private static final long serialVersionUID = 1L;

    /**
     * M680 - referanseArkivenhet (xs:string/SystemID/UUID)
     */
    @Column(name = REFERENCE_ARCHIVE_UNIT_ENG)
    @Audited
    @JsonProperty(REFERENCE_ARCHIVE_UNIT)
    private UUID referenceArchiveUnitSystemId;

    /**
     * M681 - referanseMetadata (xs:string)
     */
    @Column(name = REFERENCE_METADATA_ENG)
    @Audited
    @JsonProperty(REFERENCE_METADATA)
    private String referenceMetadata;

    /**
     * M??? - referanseEndretAv (xs:string/SystemID/UUID)
     */
    @Column(name = REFERENCE_CHANGED_BY_ENG)
    @Audited
    @JsonProperty(REFERENCE_CHANGED_BY)
    private String referenceChangedBy;

    /**
     * M684 - tidligereVerdi (xs:string)
     */
    @Column(name = OLD_VALUE_ENG)
    @Audited
    @JsonProperty(OLD_VALUE)
    private String oldValue;

    /**
     * M685 - nyVerdi (xs:string)
     */
    @Column(name = NEW_VALUE_ENG)
    @Audited
    @JsonProperty(NEW_VALUE)
    private String newValue;

    // Link to Archive Unit (aka SystemIdEntity)
    @ManyToOne
    @JoinColumn(name = SYSTEM_ID_ENTITY_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private SystemIdEntity referenceSystemIdEntity;

    @Override
    public UUID getReferenceArchiveUnitSystemId() {
        return referenceArchiveUnitSystemId;
    }

    @Override
    public void setReferenceArchiveUnitSystemId(UUID referenceArchiveUnitSystemId) {
        this.referenceArchiveUnitSystemId = referenceArchiveUnitSystemId;
    }

    @Override
    public String getReferenceMetadata() {
        return referenceMetadata;
    }

    @Override
    public void setReferenceMetadata(String referenceMetadata) {
        this.referenceMetadata = referenceMetadata;
    }

    @Override
    public OffsetDateTime getChangedDate() {
        return getLastModifiedDate();
    }

    @Override
    public void setChangedDate(OffsetDateTime changedDate) {
        setLastModifiedDate(changedDate);
    }

    @Override
    public String getChangedBy() {
        return getLastModifiedBy();
    }

    @Override
    public void setChangedBy(String changedBy) {
        setLastModifiedBy(changedBy);
    }

    @Override
    public String getReferenceChangedBy() {
        return referenceChangedBy;
    }

    @Override
    public void setReferenceChangedBy(String referenceChangedBy) {
        this.referenceChangedBy = referenceChangedBy;
    }

    @Override
    public String getOldValue() {
        return oldValue;
    }

    @Override
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    @Override
    public String getNewValue() {
        return newValue;
    }

    @Override
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    @Override
    public SystemIdEntity getReferenceArchiveUnit() {
        return referenceSystemIdEntity;
    }

    @Override
    public void setReferenceArchiveUnit
        (SystemIdEntity referenceSystemIdEntity) {
        this.referenceSystemIdEntity = referenceSystemIdEntity;
    }

    @Override
    public String getBaseTypeName() {
        return CHANGE_LOG;
    }

    @Override
    public String getBaseRel() {
        return REL_LOGGING_CHANGE_LOG;
    }

    @Override
    public String toString() {
        return "ChangeLog{" + super.toString() +
                ", referenceArchiveUnitSystemId='" + referenceArchiveUnitSystemId + '\'' +
                ", referenceMetadata='" + referenceMetadata + '\'' +
                ", referenceChangedBy='" + referenceChangedBy + '\'' +
                ", oldValue='" + oldValue + '\'' +
                ", newValue='" + newValue + '\'' +
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
        ChangeLog rhs = (ChangeLog) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(referenceArchiveUnitSystemId, rhs.referenceArchiveUnitSystemId)
                .append(referenceMetadata, rhs.referenceMetadata)
                .append(referenceChangedBy, rhs.referenceChangedBy)
                .append(oldValue, rhs.oldValue)
                .append(newValue, rhs.newValue)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(referenceArchiveUnitSystemId)
                .append(referenceMetadata)
                .append(referenceChangedBy)
                .append(oldValue)
                .append(newValue)
                .toHashCode();
    }
}
