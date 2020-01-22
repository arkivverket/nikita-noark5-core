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

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

/**
 * Created by tsodring on 5/8/17.
 */
@MappedSuperclass
@HateoasPacker
public class NoarkGeneralEntity
        extends SystemIdEntity
        implements INoarkGeneralEntity {

    /**
     * M020 - tittel (xs:string)
     */
    @NotNull
    @Column(name = TITLE_ENG, nullable = false, length = TITLE_LENGTH)
    @Audited
    @JsonProperty(TITLE)
    private String title;

    /**
     * M021 - beskrivelse (xs:string)
     */
    @Column(name = DESCRIPTION_ENG, length = DESCRIPTION_LENGTH)
    @Audited
    @JsonProperty(DESCRIPTION)
    private String description;

    /**
     * M602 - avsluttetDato (xs:dateTime)
     */
    @Column(name = FINALISED_DATE_ENG)
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    @JsonProperty(FINALISED_DATE)
    private OffsetDateTime finalisedDate;

    /**
     * M603 - avsluttetAv (xs:string)
     */
    @Column(name = FINALISED_BY_ENG)
    @Audited
    @JsonProperty(FINALISED_BY)
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
    public int compareTo(SystemIdEntity otherEntity) {
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
