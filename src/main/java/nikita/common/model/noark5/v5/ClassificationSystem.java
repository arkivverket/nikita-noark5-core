package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.hateoas.ClassificationSystemHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IClassificationSystemEntity;
import nikita.common.model.noark5.v5.metadata.ClassificationType;
import nikita.common.util.deserialisers.ClassificationSystemDeserializer;
import nikita.webapp.hateoas.ClassificationSystemHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static nikita.common.config.Constants.REL_FONDS_STRUCTURE_CLASSIFICATION_SYSTEM;
import static nikita.common.config.Constants.TABLE_CLASSIFICATION_SYSTEM;
import static nikita.common.config.N5ResourceMappings.CLASSIFICATION_SYSTEM;
import static nikita.common.config.N5ResourceMappings.REFERENCE_CLASSIFICATION_SYSTEM_DB;

@Entity
@Table(name = TABLE_CLASSIFICATION_SYSTEM)
@JsonDeserialize(using = ClassificationSystemDeserializer.class)
@HateoasPacker(using = ClassificationSystemHateoasHandler.class)
@HateoasObject(using = ClassificationSystemHateoas.class)
@Indexed
public class ClassificationSystem
        extends NoarkGeneralEntity
        implements IClassificationSystemEntity {

    private static final long serialVersionUID = 1L;

    /**
     * M??? - klassifikasjonstype code (xs:string)
     */
    @Column(name = "classification_type_code")
    @Audited
    private String classificationTypeCode;

    /**
     * M086 - klassifikasjonstype code name (xs:string)
     */
    @Column(name = "classification_type_code_name")
    @Audited
    private String classificationTypeCodeName;

    // Links to Series
    @ManyToMany(mappedBy = REFERENCE_CLASSIFICATION_SYSTEM_DB)
    private final Set<Series> referenceSeries = new HashSet<>();

    // Links to child Classes
    @OneToMany(mappedBy = "referenceClassificationSystem")
    private final Set<Class> referenceClass = new HashSet<>();

    @Override
    public ClassificationType getClassificationType() {
        if (null == classificationTypeCode)
            return null;
        return new ClassificationType(classificationTypeCode,
                                      classificationTypeCodeName);
    }

    @Override
    public void setClassificationType(ClassificationType classificationType) {
        if (null != classificationType) {
            this.classificationTypeCode = classificationType.getCode();
            this.classificationTypeCodeName = classificationType.getCodeName();
        } else {
            this.classificationTypeCode = null;
            this.classificationTypeCodeName = null;
        }
    }

    @Override
    public String getBaseTypeName() {
        return CLASSIFICATION_SYSTEM;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_CLASSIFICATION_SYSTEM;
    }

    @Override
    public Set<Series> getReferenceSeries() {
        return referenceSeries;
    }

    @Override
    public void addSeries(Series series) {
        this.referenceSeries.add(series);
        series.getReferenceClassificationSystem().add(this);
    }

    @Override
    public void removeSeries(Series series) {
        this.referenceSeries.remove(series);
        series.getReferenceClassificationSystem().remove(this);
    }

    @Override
    public Set<Class> getReferenceClass() {
        return referenceClass;
    }

    @Override
    public void addClass(Class klass) {
        this.referenceClass.add(klass);
        klass.setReferenceClassificationSystem(this);
    }

    @Override
    public void removeClass(Class klass) {
        this.referenceClass.remove(klass);
        klass.setReferenceClassificationSystem(this);
    }

    @Override
    public String toString() {
        return "ClassificationSystem{" + super.toString() +
                "classificationTypeCode='" + classificationTypeCode + '\'' +
                "classificationTypeCodeName='" + classificationTypeCodeName + '\'' +
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
                .append(classificationTypeCode, rhs.classificationTypeCode)
                .append(classificationTypeCodeName, rhs.classificationTypeCodeName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(classificationTypeCode)
                .append(classificationTypeCodeName)
                .toHashCode();
    }
}
