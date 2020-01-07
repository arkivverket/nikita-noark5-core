package nikita.common.model.noark5.v5.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.webapp.hateoas.metadata.MetadataHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import static nikita.common.config.Constants.NOARK_METADATA_PATH;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

/**
 * Created by tsodring on 3/23/17.
 */
@MappedSuperclass
@HateoasPacker(using = MetadataHateoasHandler.class)
@HateoasObject(using = MetadataHateoas.class)
public class MetadataSuperClass
        implements IMetadataEntity {

    /**
     * M -  (xs:string)
     */
    @Id
    @Column(name = "code")
    @Audited
    protected String code;

    /**
     * M -  (xs:boolean)
     */
    @Column(name = "inactive")
    @Audited
    protected Boolean inactive = false;

    /**
     * M -  (xs:string)
     */
    @Column(name = "code_name")
    @Audited
    protected String codeName;

    @Column(name = "system_id", updatable = false, nullable = false)
    @Type(type = "uuid-char")
    protected UUID systemId;

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

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getCodeName() {
        return codeName;
    }

    @Override
    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    @Override
    public Boolean getInactive() {
        return inactive;
    }

    @Override
    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    @Override
    public String getSystemId() {
        return systemId.toString();
    }

    @Override
    public void setSystemId(UUID systemId) {
        this.systemId = systemId;
    }

    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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

    @Override
    public OffsetDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    @Override
    public void setLastModifiedDate(OffsetDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    @Override
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public String toString() {
        return "MetadataSuperClass{" +
                "code='" + code + '\'' +
                ", inactive='" + inactive + '\'' +
                ", codeName='" + codeName + '\'' +
                ", ownedBy='" + ownedBy + '\'' +
                ", version=" + version +
                ", createdDate=" + createdDate +
                ", createdBy='" + createdBy + '\'' +
                ", lastModifiedDate=" + lastModifiedDate +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
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
        MetadataSuperClass rhs = (MetadataSuperClass) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(code, rhs.code)
                .append(codeName, rhs.codeName)
                .append(inactive, rhs.inactive)
                .append(version, rhs.getVersion())
                .append(createdBy, rhs.getCreatedBy())
                .append(createdDate, rhs.getCreatedDate())
                .append(ownedBy, rhs.getOwnedBy())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, codeName, inactive, createdDate,
                createdBy, lastModifiedDate, lastModifiedBy, ownedBy, version);
    }

    @Override
    public String getBaseTypeName() {
        return "MetadataSuperClass";
    }

    @Override
    public String getBaseRel() {
        return null;
    }

    @Override
    // All Metadata entities belong to "metadata".
    // All children pick up the value from here
    public String getFunctionalTypeName() {
        return NOARK_METADATA_PATH;
    }

    public void createReference(@NotNull INikitaEntity entity,
                                @NotNull String referenceType) {
        // I really should be overridden. Currently throwing an Exception if I
        // am not overridden as nikita is unable to process this
        throw new NikitaMalformedInputDataException("Error when trying to " +
                "create a reference between entities");
    }
}
