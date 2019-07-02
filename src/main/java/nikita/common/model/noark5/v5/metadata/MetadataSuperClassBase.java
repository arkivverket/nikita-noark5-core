package nikita.common.model.noark5.v5.metadata;

import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.util.exceptions.NikitaException;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

/**
 * Created by tsodring on 3/23/17.
 */
@MappedSuperclass
public class MetadataSuperClassBase implements INikitaEntity,
        Comparable<MetadataSuperClassBase> {

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
    protected UUID systemId;

    /**
     * M021 - beskrivelse (xs:string)
     */
    @Column(name = "description")
    @Audited
    protected String description;

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
    private OffsetDateTime createdDate;

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
    private OffsetDateTime lastModifiedDate;

    /**
     * M??? - oppdatertAv (xs:string)
     */
    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;


    public String getSystemId() {
        return systemId.toString();
    }

    @Override
    public void setSystemId(UUID systemId) {
        this.systemId = systemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(OffsetDateTime createdDate) {
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

    public void setLastModifiedDate(OffsetDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
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
        this.version = version;
    }

    @Override
    public OffsetDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    @Override
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    @Override
    // This method should never be called. This exception can only occur if you add a new
    // metadata entity and forget to override this method
    public String getBaseTypeName() {
        throw new NikitaException("MetadataSuperClass exception. Internal exception. " +
                "This exception can only occur if a new metadata entity is introduced and you forgot to override " +
                "getBaseTypeName. Check logfile to see what caused this");
    }

    @Override
    // All Metadata entities belong to "metadata". These entities pick the value up here
    public String getFunctionalTypeName() {
        return Constants.NOARK_METADATA_PATH;
    }

    public void createReference(@NotNull INikitaEntity entity,
                                @NotNull String referenceType) {
        // I really should be overridden. Currently throwing an Exception if I
        // am not overridden as nikita is unable to process this
        throw new NikitaMalformedInputDataException("Error when trying to " +
                "create a reference between entities");
    }

    @Override
    public int compareTo(MetadataSuperClassBase otherEntity) {
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
        UniqueCodeMetadataSuperClass rhs = (UniqueCodeMetadataSuperClass) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(systemId, rhs.systemId)
                .append(description, rhs.getDescription())
                .append(version, rhs.getVersion())
                .append(createdBy, rhs.getCreatedBy())
                .append(createdDate, rhs.getCreatedDate())
                .append(ownedBy, rhs.getOwnedBy())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return Objects.hash(systemId, description, createdDate, createdBy,
                lastModifiedDate, lastModifiedBy, ownedBy, version);
    }

    @Override
    public String toString() {
        return "MetadataSuperClassBase{" +
                "systemId=" + systemId +
                ", description='" + description + '\'' +
                ", createdDate=" + createdDate +
                ", createdBy='" + createdBy + '\'' +
                ", lastModifiedDate=" + lastModifiedDate +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", ownedBy='" + ownedBy + '\'' +
                ", version=" + version +
                '}';
    }
}
