package nikita.common.model.noark5.v5.secondary;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.interfaces.entities.IDisposalUndertakenEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static nikita.common.config.Constants.TABLE_DISPOSAL_UNDERTAKEN;
import static nikita.common.config.N5ResourceMappings.DISPOSAL_UNDERTAKEN;

@Entity
@Table(name = TABLE_DISPOSAL_UNDERTAKEN)
public class DisposalUndertaken
        extends SystemIdEntity
        implements IDisposalUndertakenEntity {

    private static final long serialVersionUID = 1L;

    /**
     * M631 - kassertAv (xs:string)
     */
    @Column(name = "disposal_by")
    @Audited
    private String disposalBy;

    /**
     * M630 - kassertDato (xs:dateTime)
     */
    @Column(name = "disposal_date")
    @Audited
    private OffsetDateTime disposalDate;

    // Links to Series
    @ManyToMany(mappedBy = "referenceDisposalUndertaken")
    private List<Series> referenceSeries = new ArrayList<>();

    // Links to DocumentDescription
    @ManyToMany(mappedBy = "referenceDisposalUndertaken")
    private List<DocumentDescription>
            referenceDocumentDescription = new ArrayList<>();

    public String getDisposalBy() {
        return disposalBy;
    }

    public void setDisposalBy(String disposalBy) {
        this.disposalBy = disposalBy;
    }

    public OffsetDateTime getDisposalDate() {
        return disposalDate;
    }

    public void setDisposalDate(OffsetDateTime disposalDate) {
        this.disposalDate = disposalDate;
    }

    @Override
    public String getBaseTypeName() {
        return DISPOSAL_UNDERTAKEN;
    }

    @Override
    public String getBaseRel() {
        return DISPOSAL_UNDERTAKEN; // TODO, should it have a relation key?
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
        return "DisposalUndertaken{" + super.toString() +
                ", disposalBy='" + disposalBy + '\'' +
                ", disposalDate=" + disposalDate +
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
        DisposalUndertaken rhs = (DisposalUndertaken) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(disposalBy, rhs.disposalBy)
                .append(disposalDate, rhs.disposalDate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(disposalBy)
                .append(disposalDate)
                .toHashCode();
    }
}
