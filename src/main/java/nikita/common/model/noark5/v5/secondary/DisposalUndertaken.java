package nikita.common.model.noark5.v5.secondary;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.interfaces.entities.IDisposalUndertakenEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static nikita.common.config.Constants.TABLE_DISPOSAL_UNDERTAKEN;
import static nikita.common.config.N5ResourceMappings.DISPOSAL_UNDERTAKEN;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = TABLE_DISPOSAL_UNDERTAKEN)
@Indexed
public class DisposalUndertaken
        extends SystemIdEntity
        implements IDisposalUndertakenEntity {

    private static final long serialVersionUID = 1L;

    /**
     * M631 - kassertAv (xs:string)
     */
    @Column(name = "disposal_by")
    @Audited
    @KeywordField
    private String disposalBy;

    /**
     * M630 - kassertDato (xs:dateTime)
     */
    @Column(name = "disposal_date")
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    @GenericField
    private OffsetDateTime disposalDate;

    // Links to Series
    @OneToMany(mappedBy = "referenceDisposalUndertaken")
    private List<Series> referenceSeries = new ArrayList<>();

    // Links to DocumentDescription
    @OneToMany(mappedBy = "referenceDisposalUndertaken")
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

    public void addSeries(Series series) {
        this.referenceSeries.add(series);
        series.setDisposalUndertaken(this);
    }

    public void removeSeries(Series series) {
        this.referenceSeries.remove(series);
        series.setDisposalUndertaken(null);
    }

    public List<DocumentDescription> getReferenceDocumentDescription() {
        return referenceDocumentDescription;
    }

    public void setReferenceDocumentDescription(
            List<DocumentDescription> referenceDocumentDescription) {
        this.referenceDocumentDescription = referenceDocumentDescription;
    }

    public void addDocumentDescription(
            DocumentDescription documentDescription) {
        referenceDocumentDescription.add(documentDescription);
        documentDescription.setDisposalUndertaken(this);
    }

    public void removeDocumentDescription(
            DocumentDescription documentDescription) {
        referenceDocumentDescription.remove(documentDescription);
        documentDescription.removeDisposalUndertaken();
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
