package nikita.common.model.noark5.v5.secondary;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.interfaces.entities.IDeletionEntity;
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
import static nikita.common.config.N5ResourceMappings.DELETION;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = TABLE_DELETION)
public class Deletion
        extends SystemIdEntity
        implements IDeletionEntity {

    private static final long serialVersionUID = 1L;

    /**
     * M089 - slettingstype (xs:string)
     */
    @Column(name = "deletion_type")
    @Audited
    private String deletionType;

    /**
     * M614 - slettetAv (xs:string)
     */
    @Column(name = "deletion_by")
    @Audited
    private String deletionBy;

    /**
     * M613 slettetDato (xs:dateTime)
     */
    @Column(name = "deletion_date")
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    private OffsetDateTime deletionDate;

    // Links to Series
    @OneToMany(mappedBy = "referenceDeletion")
    private List<Series> referenceSeries = new ArrayList<>();

    // Links to DocumentDescription
    @OneToMany(mappedBy = "referenceDeletion")
    private List<DocumentDescription>
            referenceDocumentDescription = new ArrayList<>();

    public String getDeletionType() {
        return deletionType;
    }

    public void setDeletionType(String deletionType) {
        this.deletionType = deletionType;
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
        return DELETION; // FIXME, should it have a relation key?
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
                ", deletionType='" + deletionType + '\'' +
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
                .append(deletionType, rhs.deletionType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(deletionDate)
                .append(deletionBy)
                .append(deletionType)
                .toHashCode();
    }
}
