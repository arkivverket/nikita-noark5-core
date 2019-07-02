package nikita.common.model.noark5.v5;

import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.common.util.exceptions.NoarkConcurrencyException;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

import static nikita.common.config.Constants.NOARK_FONDS_STRUCTURE_PATH;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

/**
 * Created by tsodring on 5/8/17.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class NoarkEntity
        implements INikitaEntity, Comparable<NoarkEntity> {

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
    @Type(type="uuid-char")
    private UUID systemId;

    @CreatedBy
    @Column(name = "owned_by")
    @Audited
    private String ownedBy;

    @Version
    @Column(name = "version")
    private Long version;

    /**
     * M600 - opprettetDato (xs:dateTime)
     */
    @CreatedDate
    @Column(name = "created_date")
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    private ZonedDateTime createdDate;

    /**
     * M601 - opprettetAv (xs:string)
     */
    @CreatedBy
    @Column(name = "created_by")
    @Audited
    private String createdBy;

    /**
     * M??? - oppdatertDato (xs:dateTime)
     */
    @LastModifiedDate
    @Column(name = "last_modified_date")
    @DateTimeFormat(iso = DATE_TIME)
    private ZonedDateTime lastModifiedDate;

    /**
     * M??? - oppdatertAv (xs:string)
     */
    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

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

    public String getId() {
        return systemId.toString();
    }

    public void setId(UUID systemId) {
        this.systemId = systemId;
    }

    @Override
    public String getOwnedBy() {
        return ownedBy;
    }

    @Override
    public void setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
    }

    @Override
    public Long getVersion() {
        return version;
    }

    @Override
    public void setVersion(Long version) {
        if (!this.version.equals(version)) {
            throw new NoarkConcurrencyException(
                    "Concurrency Exception. Old version [" + this
                            .version + "], new version [" + version + "]");
        }
        this.version = version;
    }

    @Override
    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    @Override
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    @Override
    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    // You should not use, me. All subclasses must implement this themselves
    public String getBaseTypeName() {
        return null;
    }

    @Override

    // Most entities belong to arkivstruktur. These entities pick the value
    // up here
    public String getFunctionalTypeName() {
        return NOARK_FONDS_STRUCTURE_PATH;
    }

    @Override
    public void createReference(
            @NotNull INikitaEntity entity,
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
                .append(ownedBy)
                .append(version)
                .toHashCode();
    }

    @Override
    public int compareTo(NoarkEntity otherEntity) {
        if (null == otherEntity) {
            return -1;
        }
        return new CompareToBuilder()
                .append(this.systemId, otherEntity.systemId)
                .append(ownedBy, otherEntity.getOwnedBy())
                .append(version, otherEntity.getVersion())
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
        NoarkEntity rhs = (NoarkEntity) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(systemId, rhs.getSystemId())
                .append(ownedBy, rhs.getOwnedBy())
                .append(version, rhs.getVersion())
                .isEquals();
    }

    @Override
    public String toString() {
        return "NoarkEntity{" +
                "systemId=" + systemId +
                ", ownedBy='" + ownedBy + '\'' +
                ", version=" + version +
                ", lastModifiedDate=" + lastModifiedDate +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", createdDate=" + createdDate +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
