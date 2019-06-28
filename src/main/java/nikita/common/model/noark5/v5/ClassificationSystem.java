package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.hateoas.ClassificationSystemHateoas;
import nikita.common.util.deserialisers.ClassifiactionSystemDeserializer;
import nikita.webapp.hateoas.ClassificationSystemHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import static nikita.common.config.Constants.TABLE_CLASSIFICATION_SYSTEM;

@Entity
@Table(name = TABLE_CLASSIFICATION_SYSTEM)
@JsonDeserialize(using = ClassifiactionSystemDeserializer.class)
@HateoasPacker(using = ClassificationSystemHateoasHandler.class)
@HateoasObject(using = ClassificationSystemHateoas.class)
public class ClassificationSystem
        extends NoarkGeneralEntity {

    private static final long serialVersionUID = 1L;

    /**
     * M086 - klassifikasjonstype (xs:string)
     */
    @Column(name = "classification_type")
    @Audited
    private String classificationType;

    // Links to Series
    @OneToMany(mappedBy = "referenceClassificationSystem")
    private List<Series> referenceSeries = new ArrayList<>();

    // Links to child Classes
    @OneToMany(mappedBy = "referenceClassificationSystem")
    private List<Class> referenceClass = new ArrayList<>();

    public String getClassificationType() {
        return classificationType;
    }

    public void setClassificationType(String classificationType) {
        this.classificationType = classificationType;
    }

    @Override
    public String getBaseTypeName() {
        return N5ResourceMappings.CLASSIFICATION_SYSTEM;
    }

    public List<Series> getReferenceSeries() {
        return referenceSeries;
    }

    public void setReferenceSeries(List<Series> referenceSeries) {
        this.referenceSeries = referenceSeries;
    }

    public List<Class> getReferenceClass() {
        return referenceClass;
    }

    public void setReferenceClass(List<Class> referenceClass) {
        this.referenceClass = referenceClass;
    }

    @Override
    public String toString() {
        return "ClassificationSystem{" + super.toString() +
                "classificationType='" + classificationType + '\'' +
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
        ClassificationSystem rhs = (ClassificationSystem) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(classificationType, rhs.classificationType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(classificationType)
                .toHashCode();
    }
}
