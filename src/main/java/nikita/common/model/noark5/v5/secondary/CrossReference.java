package nikita.common.model.noark5.v5.secondary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.hateoas.secondary.CrossReferenceHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.ICrossReferenceEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.util.deserialisers.secondary.CrossReferenceDeserializer;
import nikita.webapp.hateoas.secondary.CrossReferenceHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import javax.persistence.*;
import java.util.UUID;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Note a cross-reference is a a one way relationship from an entity to
 * another entity. It is a NoarkGeneral entity so it has a systemID for
 * identification purposes. This breaks with the current understanding of the
 * domain model in the API-standard.
 * <p>
 * A cross reference can occur between File, Class and Record. The fields
 * fromSystemId and toSystemId show the one way relationship.
 */
@Entity
@Table(name = TABLE_CROSS_REFERENCE)
@JsonDeserialize(using = CrossReferenceDeserializer.class)
@HateoasPacker(using = CrossReferenceHateoasHandler.class)
@HateoasObject(using = CrossReferenceHateoas.class)
@Indexed
public class CrossReference
        extends SystemIdEntity
        implements ISystemId, ICrossReferenceEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = FROM_SYSTEM_ID_ENG, nullable = false)
    @JsonProperty(FROM_SYSTEM_ID)
    @KeywordField
    private UUID fromSystemId;

    @Column(name = TO_SYSTEM_ID_ENG, nullable = false)
    @JsonProperty(TO_SYSTEM_ID)
    @KeywordField
    private UUID toSystemId;

    /**
     * Can be referanseTilKlasse, referanseTilMappe or
     * referanseTilRegistrering
     */
    @Column(name = REFERENCE_TYPE_ENG, nullable = false)
    @JsonProperty(REFERENCE_TYPE)
    private String referenceType;

    /**
     * Can be class(klasse), mappe or registrering. This field is only used
     * internally and should never be serialised as JSON
     */
    @Column(name = REFERENCE_FROM_TYPE, nullable = false)
    private String fromReferenceType;

    /**
     * Link to Class
     * Can be used to determine:
     * M219 - referanseTilKlasse (xs:string)
     * points to systemId of the referenced Class
     **/
    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = CROSS_REFERENCE_CLASS_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Class referenceClass;

    /**
     * Link to File
     * Can be used to determine:
     * M210 - referanseTilMappe (xs:string)
     * points to systemId of the referenced FiLink to File
     **/
    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = CROSS_REFERENCE_FILE_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private File referenceFile;

    /**
     * Link to Record
     * Can be used to determine:
     * M212 - referanseTilRegistrering (xs:string)
     * points to systemId of the referenced Record
     **/
    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = CROSS_REFERENCE_RECORD_ID,
            referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    private Record referenceRecord;

    public UUID getFromSystemId() {
        return fromSystemId;
    }

    public void setFromSystemId(UUID fromSystemId) {
        this.fromSystemId = fromSystemId;
    }

    public UUID getToSystemId() {
        return toSystemId;
    }

    public void setToSystemId(UUID toSystemId) {
        this.toSystemId = toSystemId;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public String getFromReferenceType() {
        return fromReferenceType;
    }

    public void setFromReferenceType(String fromReferenceType) {
        this.fromReferenceType = fromReferenceType;
    }

    public Class getReferenceClass() {
        return referenceClass;
    }

    public void setReferenceClass(Class referenceClass) {
        this.referenceClass = referenceClass;
    }

    public File getReferenceFile() {
        return referenceFile;
    }

    public void setReferenceFile(File referenceFile) {
        this.referenceFile = referenceFile;
    }

    public Record getReferenceRecord() {
        return referenceRecord;
    }

    public void setReferenceRecord(Record referenceRecord) {
        this.referenceRecord = referenceRecord;
    }

    @Override
    public String getBaseTypeName() {
        return CROSS_REFERENCE;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_CROSS_REFERENCE;
    }

    @Override
    public String toString() {
        return "CrossReference{" + super.toString() + '\'' +
                "fromSystemId='" + fromSystemId + '\'' +
                ", toSystemId='" + toSystemId + '\'' +
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
        CrossReference rhs = (CrossReference) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(fromSystemId, rhs.fromSystemId)
                .append(toSystemId, rhs.toSystemId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(fromSystemId)
                .append(toSystemId)
                .toHashCode();
    }
}
