package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

import static nikita.common.config.N5ResourceMappings.*;

/**
 * Created : 2020/01/06 by tsodring
 * <p>
 * TODO: Deal with the following comment
 * Introducing this class so it can be a base class for e.g., Author, Keyword
 * etc.
 * <p>
 * Once this is working we need to refactor the code base. Note NoarkEntity
 * should implement INoarkEntity, not INoarkEntity and INoarkEntity should
 * inherit from INoarkEntity.
 */

@MappedSuperclass
@Table(indexes = @Index(name = "index_owned_by", columnList = "owned_by"))
public class NikitaEntity {

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
    @JsonProperty(LAST_MODIFIED_DATE)
    private OffsetDateTime lastModifiedDate;

    /**
     * M??? - oppdatertAv (xs:string)
     */
    @LastModifiedBy
    @Column(name = LAST_MODIFIED_BY_ENG)
    @JsonProperty(LAST_MODIFIED_BY)
    private String lastModifiedBy;

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

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(OffsetDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public OffsetDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(OffsetDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NikitaEntity that = (NikitaEntity) o;
        return Objects.equals(ownedBy, that.ownedBy) &&
                Objects.equals(version, that.version) &&
                Objects.equals(createdDate, that.createdDate) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
                Objects.equals(lastModifiedBy, that.lastModifiedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownedBy, version, createdDate, createdBy,
                lastModifiedDate, lastModifiedBy);
    }
}
