package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.annotation.JsonProperty;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkGeneralEntity;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

/**
 * Created by tsodring on 5/8/17.
 */
@MappedSuperclass
@HateoasPacker
public class NoarkGeneralEntity
        extends NoarkEntity
        implements INoarkGeneralEntity {

    /**
     * M020 - tittel (xs:string)
     */
    @NotNull
    @Column(name = "title", nullable = false)
    @Audited
    @JsonProperty("tittel")
    private String title;

    /**
     * M021 - beskrivelse (xs:string)
     */
    @Column(name = "description")
    @Audited
    private String description;

    /**
     * M602 - avsluttetDato (xs:dateTime)
     */
    @Column(name = "finalised_date")
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    private OffsetDateTime finalisedDate;

    /**
     * M603 - avsluttetAv (xs:string)
     */
    @Column(name = "finalised_by")
    @Audited
    private String finalisedBy;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public OffsetDateTime getFinalisedDate() {
        return finalisedDate;
    }

    @Override
    public void setFinalisedDate(OffsetDateTime finalisedDate) {
        this.finalisedDate = finalisedDate;
    }

    @Override
    public String getFinalisedBy() {
        return finalisedBy;
    }

    @Override
    public void setFinalisedBy(String finalisedBy) {
        this.finalisedBy = finalisedBy;
    }

    @Override
    public int compareTo(NoarkEntity otherEntity) {
        if (null == otherEntity) {
            return -1;
        }
        return new CompareToBuilder()
                .appendSuper(super.compareTo(otherEntity))
                .append(this.title,
                        ((NoarkGeneralEntity) otherEntity).title)
                .append(this.description,
                        ((NoarkGeneralEntity) otherEntity).description)
                .append(this.finalisedBy,
                        ((NoarkGeneralEntity) otherEntity).finalisedBy)
                .append(this.finalisedDate,
                        ((NoarkGeneralEntity) otherEntity).finalisedDate)
                .toComparison();
    }

    @Override
    public String toString() {

        return super.toString() +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", finalisedDate=" + finalisedDate +
                ", finalisedBy='" + finalisedBy + '\'';
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
        NoarkGeneralEntity rhs = (NoarkGeneralEntity) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(title, rhs.title)
                .append(description, rhs.description)
                .append(finalisedDate, rhs.finalisedDate)
                .append(finalisedBy, rhs.finalisedBy)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(title)
                .append(description)
                .append(finalisedDate)
                .append(finalisedBy)
                .toHashCode();
    }
}