package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.annotation.JsonProperty;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.common.util.exceptions.NoarkConcurrencyException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

import static nikita.common.config.Constants.NOARK_FONDS_STRUCTURE_PATH;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

/**
 * Created by tsodring on 5/8/17.
 */
@MappedSuperclass
public class NoarkEntity
        implements INoarkEntity {

    private static final long serialVersionUID = 1L;

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
    @Column(name = CREATED_DATE_ENG)
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    @JsonProperty(CREATED_DATE)
    private OffsetDateTime createdDate;

    /**
     * M601 - opprettetAv (xs:string)
     */
    @CreatedBy
    @Column(name = CREATED_BY_ENG)
    @Audited
    @JsonProperty(CREATED_BY)
    private String createdBy;

    /**
     * M??? - oppdatertDato (xs:dateTime)
     */
    @LastModifiedDate
    @Column(name = LAST_MODIFIED_DATE_ENG)
    @DateTimeFormat(iso = DATE_TIME)
    private OffsetDateTime lastModifiedDate;

    /**
     * M??? - oppdatertAv (xs:string)
     */
    @LastModifiedBy
    @Column(name = LAST_MODIFIED_BY_ENG)
    private String lastModifiedBy;

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
                    "Concurrency Exception. Old version [" + this.version +
                            "], new version [" + version + "]");
        }
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
    // You should not use, me. All subclasses must implement this themselves
    public String getBaseTypeName() {
        return null;
    }

    @Override
    public String getBaseRel() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public String getIdentifierType() {
        return null;
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
                .append(ownedBy)
                .toHashCode();
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
                .append(ownedBy, rhs.getOwnedBy())
                .isEquals();
    }

    @Override
    public String toString() {
        return "NoarkEntity{" +
                ", ownedBy='" + ownedBy + '\'' +
                ", version=" + version +
                ", lastModifiedDate=" + lastModifiedDate +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", createdDate=" + createdDate +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
