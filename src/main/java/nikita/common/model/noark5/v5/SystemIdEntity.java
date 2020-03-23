package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javax.persistence.InheritanceType.TABLE_PER_CLASS;
import static nikita.common.config.Constants.NOARK_FONDS_STRUCTURE_PATH;
import static nikita.common.config.N5ResourceMappings.*;
import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

/**
 * Created by tsodring on 5/8/17.
 */
@Entity
@Inheritance(strategy = TABLE_PER_CLASS)
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = @Index(name = "index_owned_by",
        columnList = "owned_by"))
@Audited(targetAuditMode = NOT_AUDITED)
public class SystemIdEntity
        extends NoarkEntity
        implements ISystemId, Comparable<SystemIdEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * M001 - systemID (xs:string)
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {@Parameter(
                    name = "uuid_gen_strategy_class",
                    value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
    @Column(name = "system_id", updatable = false, nullable = false)
    @Type(type = "uuid-char")
    private UUID systemId;

    // Links to ChangeLog
    @OneToMany(mappedBy = "referenceSystemIdEntity")
    @JsonIgnore
    private List<ChangeLog> referenceChangeLog = new ArrayList<>();

    @Override
    public String getSystemId() {
        if (null != systemId)
            return systemId.toString();
        else
            return null;
    }

    @Override
    public void setSystemId(UUID systemId) {
        this.systemId = systemId;
    }

    @Override
    public UUID getId() {
        return systemId;
    }

    @Override
    public void setId(UUID systemId) {
        this.systemId = systemId;
    }

    @Override
    public String getIdentifier() {
        return getSystemId();
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
