package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.hateoas.ClassificationSystemHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IClassificationSystemEntity;
import nikita.common.util.deserialisers.ClassificationSystemDeserializer;
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

import static nikita.common.config.Constants.REL_FONDS_STRUCTURE_CLASSIFICATION_SYSTEM;
import static nikita.common.config.Constants.TABLE_CLASSIFICATION_SYSTEM;
import static nikita.common.config.N5ResourceMappings.CLASSIFICATION_SYSTEM;

@Entity
@Table(name = TABLE_CLASSIFICATION_SYSTEM)
@JsonDeserialize(using = ClassificationSystemDeserializer.class)
@HateoasPacker(using = ClassificationSystemHateoasHandler.class)
@HateoasObject(using = ClassificationSystemHateoas.class)
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
     * M086 - klassifikasjonstype name (xs:string)
     */
    @Column(name = "classification_type_name")
    @Audited
    private String classificationTypeName;

    // Links to Series
    @OneToMany(mappedBy = "referenceClassificationSystem")
    private List<Series> referenceSeries = new ArrayList<>();

    // Links to child Classes
    @OneToMany(mappedBy = "referenceClassificationSystem")
    private List<Class> referenceClass = new ArrayList<>();

    @Override
    public String getClassificationTypeCode() {
        return classificationTypeCode;
    }

    @Override
    public void setClassificationTypeCode(String classificationTypeCode) {
        this.classificationTypeCode = classificationTypeCode;
    }

    @Override
    public String getClassificationTypeName() {
        return classificationTypeName;
    }

    @Override
    public void setClassificationTypeName(String classificationTypeName) {
        this.classificationTypeName = classificationTypeName;
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
    public List<Series> getReferenceSeries() {
        return referenceSeries;
    }

    @Override
    public void setReferenceSeries(List<Series> referenceSeries) {
        this.referenceSeries = referenceSeries;
    }

    @Override
    public List<Class> getReferenceClass() {
        return referenceClass;
    }

    @Override
    public void setReferenceClass(List<Class> referenceClass) {
        this.referenceClass = referenceClass;
    }

    @Override
    public String toString() {
        return "ClassificationSystem{" + super.toString() +
                "classificationTypeCode='" + classificationTypeCode + '\'' +
                "classificationTypeName='" + classificationTypeName + '\'' +
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
                .append(classificationTypeName, rhs.classificationTypeName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(classificationTypeCode)
                .append(classificationTypeName)
                .toHashCode();
    }
}
