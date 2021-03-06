package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.NOARK_FONDS_STRUCTURE_PATH;
import static nikita.common.config.N5ResourceMappings.SYSTEM_ID;
import static nikita.common.config.N5ResourceMappings.SYSTEM_ID_ENG;
import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Inheritance(strategy = JOINED)
@EntityListeners(AuditingEntityListener.class)
@Audited(targetAuditMode = NOT_AUDITED)
public class SystemIdEntity
        extends NoarkEntity
        implements ISystemId, Comparable<SystemIdEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * M001 - systemID (xs:string)
     */
    @Id
    @GenericGenerator(name = "uuid-gen",
            strategy = "nikita.common.util.NikitaUUIDGenerator")
    @GeneratedValue(generator = "uuid-gen")
    @Column(name = SYSTEM_ID_ENG, updatable = false, nullable = false)
    @Type(type = "uuid-char")
    @KeywordField
    private UUID systemId;

    // Links to ChangeLog
    @OneToMany(mappedBy = "referenceSystemIdEntity")
    @JsonIgnore
    private List<ChangeLog> referenceChangeLog = new ArrayList<>();

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

    public List<ChangeLog> getChangeLog() {
        return referenceChangeLog;
    }

    public void setChangeLog(List<ChangeLog> referenceChangeLog) {
        this.referenceChangeLog = referenceChangeLog;
    }

    public void addChangeLog(ChangeLog eventLog) {
        this.referenceChangeLog.add(eventLog);
        eventLog.setReferenceArchiveUnit(this);
    }

    public void removeChangeLog(ChangeLog eventLog) {
        referenceChangeLog.remove(eventLog);
        eventLog.setReferenceArchiveUnit(null);
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
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(systemId)
                .toHashCode();
    }

    @Override
    public int compareTo(SystemIdEntity otherEntity) {
        if (null == otherEntity) {
            return -1;
        }
        return new CompareToBuilder()
                .append(this.systemId, otherEntity.systemId)
                .toComparison();
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
        SystemIdEntity rhs = (SystemIdEntity) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(systemId, rhs.getSystemId())
                .isEquals();
    }

    @Override
    public String toString() {
        return "NoarkEntity{" +
                "systemId=" + systemId +
                '}';
    }
}
