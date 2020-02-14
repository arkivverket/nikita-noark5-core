package nikita.common.model.noark5.v5.secondary;

import nikita.common.model.noark5.v5.*;
import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.interfaces.entities.ICrossReferenceEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CROSS_REFERENCE;

/**
 * Created by tsodring
 * <p>
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
public class CrossReference
        extends SystemIdEntity
        implements ISystemId, ICrossReferenceEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "from_system_id", nullable = false)
    private String fromSystemId;

    @Column(name = "to_system_id", nullable = false)
    private String toSystemId;

    /**
     * Can be referanseTilKlasse, referanseTilMappe or
     * referanseTilRegistrering
     */
    @Column(name = "reference_type", nullable = false)
    private String referenceType;

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

    public String getFromSystemId() {
        return fromSystemId;
    }

    public void setFromSystemId(String fromSystemId) {
        this.fromSystemId = fromSystemId;
    }

    public String getToSystemId() {
        return toSystemId;
    }

    public void setToSystemId(String toSystemId) {
        this.toSystemId = toSystemId;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
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
        return "CrossReference{" +
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
