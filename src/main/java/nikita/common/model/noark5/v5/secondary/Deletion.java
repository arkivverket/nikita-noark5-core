package nikita.common.model.noark5.v5.secondary;

import com.fasterxml.jackson.annotation.JsonProperty;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.interfaces.entities.IDeletionEntity;
import nikita.common.model.noark5.v5.metadata.DeletionType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static nikita.common.config.Constants.TABLE_DELETION;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = TABLE_DELETION)
public class Deletion
        extends SystemIdEntity
        implements IDeletionEntity {

    private static final long serialVersionUID = 1L;

    /**
     * M??? - slettingstype code (xs:string)
     */
    @Column(name = "deletion_type_code")
    @Audited
    private String deletionTypeCode;

    /**
     * M089 - slettingstype code name (xs:string)
     */
    @Column(name = "deletion_type_code_name")
    @Audited
    private String deletionTypeCodeName;

    /**
     * M614 - slettetAv (xs:string)
     */
    @Column(name = DELETION_BY_ENG)
    @Audited
    @JsonProperty(DELETION_BY)
    private String deletionBy;

    /**
     * M613 slettetDato (xs:dateTime)
     */
    @Column(name = DELETION_DATE_ENG)
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    @JsonProperty(DELETION_DATE)
    private OffsetDateTime deletionDate;

    // TODO add 'referanseSlettetAv'

    // Links to Series
    @OneToMany(mappedBy = "referenceDeletion")
    private List<Series> referenceSeries = new ArrayList<>();

    // Links to DocumentDescription
    @OneToMany(mappedBy = "referenceDeletion")
    private List<DocumentDescription>
            referenceDocumentDescription = new ArrayList<>();

    public DeletionType getDeletionType() {
        if (null == deletionTypeCode)
            return null;
        return new DeletionType(deletionTypeCode, deletionTypeCodeName);
    }

    public void setDeletionType(DeletionType deletionType) {
        if (null != deletionType) {
            this.deletionTypeCode = deletionType.getCode();
            this.deletionTypeCodeName = deletionType.getCodeName();
        } else {
            this.deletionTypeCode = null;
            this.deletionTypeCodeName = null;
        }
    }

    public String getDeletionBy() {
        return deletionBy;
    }

    public void setDeletionBy(String deletionBy) {
        this.deletionBy = deletionBy;
    }

    public OffsetDateTime getDeletionDate() {
        return deletionDate;
    }

    public void setDeletionDate(OffsetDateTime deletionDate) {
        this.deletionDate = deletionDate;
    }

    @Override
    public String getBaseTypeName() {
        return DELETION;
    }

    @Override
    public String getBaseRel() {
        return DELETION; // TODO, should it have a relation key?
    }

    public List<Series> getReferenceSeries() {
        return referenceSeries;
    }

    public void setReferenceSeries(List<Series> referenceSeries) {
        this.referenceSeries = referenceSeries;
    }

    public List<DocumentDescription> getReferenceDocumentDescription() {
        return referenceDocumentDescription;
    }

    public void setReferenceDocumentDescription(
            List<DocumentDescription> referenceDocumentDescription) {
        this.referenceDocumentDescription = referenceDocumentDescription;
    }

    @Override
    public String toString() {
        return "Deletion{" + super.toString() +
                "deletionDate=" + deletionDate +
                ", deletionBy='" + deletionBy + '\'' +
                ", deletionTypeCode='" + deletionTypeCode + '\'' +
                ", deletionTypeCodeName='" + deletionTypeCodeName + '\'' +
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
        Deletion rhs = (Deletion) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(deletionDate, rhs.deletionDate)
                .append(deletionBy, rhs.deletionBy)
                .append(deletionTypeCode, rhs.deletionTypeCode)
                .append(deletionTypeCodeName, rhs.deletionTypeCodeName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(deletionDate)
                .append(deletionBy)
                .append(deletionTypeCode)
                .append(deletionTypeCodeName)
                .toHashCode();
    }
}
