package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import nikita.common.model.noark5.v5.hateoas.ChangeLogHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IChangeLogEntity;
//import nikita.common.util.deserialisers.ChangeLogDeserializer;
//import nikita.webapp.hateoas.ChangeLogHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = TABLE_CHANGE_LOG)
//@JsonDeserialize(using = ChangeLogDeserializer.class)
//@HateoasPacker(using = ChangeLogHateoasHandler.class)
//@HateoasObject(using = ChangeLogHateoas.class)
@Audited(withModifiedFlag = true)
public class ChangeLog
    extends SystemIdEntity
    implements IChangeLogEntity
{

    private static final long serialVersionUID = 1L;

    /**
     * M680 - referanseArkivenhet (xs:string/SystemID/UUID)
     */
    @Column(name = "changelog_reference_archive_unit")
    @JsonProperty(REFERENCE_ARCHIVE_UNIT)
    private String referenceArchiveUnit;

    /**
     * M681 - referanseMetadata (xs:string)
     */
    @Column(name = "changelog_reference_metadata")
    @JsonProperty(REFERENCE_METADATA)
    private String referenceMetadata;

    /**
     * M682 - endretDato (xs:datetime)
     */
    @Column(name = "changelog_changed_date")
    @DateTimeFormat(iso = DATE_TIME)
    @JsonProperty(CHANGED_DATE)
    private OffsetDateTime changedDate;

    /**
     * M683 - endretAv (xs:string)
     */
    @Column(name = "changelog_changed_by")
    @JsonProperty(CHANGED_BY)
    private String changedBy;

    /**
     * M??? - referanseEndretAv (xs:string/SystemID/UUID)
     */
    @Column(name = "changelog_reference_changed_by")
    @JsonProperty(REFERENCE_CHANGED_BY)
    private String referenceChangedBy;

    /**
     * M684 - tidligereVerdi (xs:string)
     */
    @Column(name = "changelog_old_value")
    @JsonProperty(OLD_VALUE)
    private String oldValue;

    /**
     * M685 - nyVerdi (xs:string)
     */
    @Column(name = "changelog_new_value")
    @JsonProperty(NEW_VALUE)
    private String newValue;

    @Override
    public String getReferenceArchiveUnit() {
        return referenceArchiveUnit;
    }

    @Override
    public void setReferenceArchiveUnit(String referenceArchiveUnit) {
        this.referenceArchiveUnit = referenceArchiveUnit;
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
        return changedDate;
    }

    @Override
    public void setChangedDate(OffsetDateTime changedDate) {
        this.changedDate = changedDate;
    }

    @Override
    public String getChangedBy() {
        return changedBy;
    }

    @Override
    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
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
                ", referenceArchiveUnit='" + referenceArchiveUnit + '\'' +
                ", referenceMetadata='" + referenceMetadata + '\'' +
                ", changedDate='" + changedDate + '\'' +
                ", changedBy='" + changedBy + '\'' +
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
                .append(referenceArchiveUnit, rhs.referenceArchiveUnit)
                .append(referenceMetadata, rhs.referenceMetadata)
                .append(changedDate, rhs.changedDate)
                .append(changedBy, rhs.changedBy)
                .append(referenceChangedBy, rhs.referenceChangedBy)
                .append(oldValue, rhs.oldValue)
                .append(newValue, rhs.newValue)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(referenceArchiveUnit)
                .append(referenceMetadata)
                .append(changedDate)
                .append(changedBy)
                .append(referenceChangedBy)
                .append(oldValue)
                .append(newValue)
                .toHashCode();
    }
}
