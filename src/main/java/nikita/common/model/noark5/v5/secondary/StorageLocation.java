package nikita.common.model.noark5.v5.secondary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.*;
import nikita.common.model.noark5.v5.hateoas.secondary.StorageLocationHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IStorageLocationEntity;
import nikita.common.util.deserialisers.secondary.StorageLocationDeserializer;
import nikita.common.util.serializers.noark5v5.StorageLocationSerializer;
import nikita.webapp.hateoas.secondary.StorageLocationHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

import static nikita.common.config.Constants.REL_FONDS_STRUCTURE_STORAGE_LOCATION;
import static nikita.common.config.Constants.TABLE_STORAGE_LOCATION;
import static nikita.common.config.N5ResourceMappings.*;

@Entity
@Table(name = TABLE_STORAGE_LOCATION)
@JsonSerialize(using = StorageLocationSerializer.class)
@JsonDeserialize(using = StorageLocationDeserializer.class)
@HateoasPacker(using = StorageLocationHateoasHandler.class)
@HateoasObject(using = StorageLocationHateoas.class)
@Indexed
public class StorageLocation
        extends SystemIdEntity
        implements IStorageLocationEntity {

    // Links to Fonds
    @ManyToMany(mappedBy = REFERENCE_STORAGE_LOCATION)
    private final Set<Fonds> referenceFonds = new HashSet<>();
    // Links to Series
    @ManyToMany(mappedBy = REFERENCE_STORAGE_LOCATION)
    private final Set<Series> referenceSeries = new HashSet<>();
    // Links to File
    @ManyToMany(mappedBy = REFERENCE_STORAGE_LOCATION)
    private final Set<File> referenceFile = new HashSet<>();
    // Links to Record
    @ManyToMany(mappedBy = REFERENCE_STORAGE_LOCATION)
    @JsonIgnore
    private final Set<RecordEntity> referenceRecordEntity = new HashSet<>();
    /**
     * M301 - oppbevaringssted (xs:string)
     */
    @Column(name = STORAGE_LOCATION_ENG)
    @Audited
    @JsonProperty(STORAGE_LOCATION)
    @FullTextField
    private String storageLocation;

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public Set<Fonds> getReferenceFonds() {
        return referenceFonds;
    }

    public void addReferenceFonds(Fonds fonds) {
        this.referenceFonds.add(fonds);
        fonds.getReferenceStorageLocation().add(this);
    }

    public void removeReferenceFonds(Fonds fonds) {
        this.referenceFonds.remove(fonds);
        fonds.getReferenceStorageLocation().remove(this);
    }

    public Set<Series> getReferenceSeries() {
        return referenceSeries;
    }

    public void addReferenceSeries(Series series) {
        this.referenceSeries.add(series);
        series.getReferenceStorageLocation().add(this);
    }

    public void removeReferenceSeries(Series series) {
        this.referenceSeries.remove(series);
        series.getReferenceStorageLocation().remove(this);
    }

    public Set<File> getReferenceFile() {
        return referenceFile;
    }

    public void addReferenceFile(File file) {
        this.referenceFile.add(file);
        file.getReferenceStorageLocation().add(this);
    }

    public void removeReferenceFile(File file) {
        this.referenceFile.remove(file);
        file.getReferenceStorageLocation().remove(this);
    }

    public Set<RecordEntity> getReferenceRecordEntity() {
        return referenceRecordEntity;
    }

    public void addReferenceRecord(RecordEntity record) {
        this.referenceRecordEntity.add(record);
        record.getReferenceStorageLocation().add(this);
    }

    public void removeReferenceRecord(RecordEntity record) {
        this.referenceRecordEntity.remove(record);
        record.getReferenceStorageLocation().remove(this);
    }

    @Override
    public String getBaseTypeName() {
        return STORAGE_LOCATION;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_STORAGE_LOCATION;
    }

    @Override
    public String toString() {
        return "StorageLocation{" + super.toString() +
                ", storageLocation='" + storageLocation + '\'' +
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
        StorageLocation rhs = (StorageLocation) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(storageLocation, rhs.storageLocation)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(storageLocation)
                .toHashCode();
    }
}
