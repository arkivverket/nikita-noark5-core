package nikita.common.model.noark5.v5.secondary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.*;
import nikita.common.util.serializers.noark5v5.StorageLocationSerializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

import static nikita.common.config.Constants.REL_FONDS_STRUCTURE_STORAGE_LOCATION;
import static nikita.common.config.Constants.TABLE_STORAGE_LOCATION;
import static nikita.common.config.N5ResourceMappings.STORAGE_LOCATION;

@Entity
@Table(name = TABLE_STORAGE_LOCATION)
@JsonSerialize(using = StorageLocationSerializer.class)
public class StorageLocation
        extends SystemIdEntity {

    /**
     * M301 - oppbevaringssted (xs:string)
     */
    @Column(name = "storage_location")
    @Audited
    private String storageLocation;

    // Links to Fonds
    @ManyToMany(mappedBy = "referenceStorageLocation")
    @JsonIgnore
    private Set<Fonds> referenceFonds = new HashSet<>();

    // Links to Series
    @ManyToMany(mappedBy = "referenceStorageLocation")
    private Set<Series> referenceSeries = new HashSet<>();

    // Links to File
    @ManyToMany(mappedBy = "referenceStorageLocation")
    private Set<File> referenceFile = new HashSet<>();

    // Links to Record
    @ManyToMany(mappedBy = "referenceStorageLocation")
    @JsonIgnore
    private Set<Record> referenceRecord = new HashSet<>();

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public Set<Fonds> getReferenceFonds() {
        return referenceFonds;
    }

    public void addFonds(Fonds fonds) {
        this.referenceFonds.add(fonds);
        fonds.getReferenceStorageLocation().add(this);
    }

    public void removeFonds(Fonds fonds) {
        this.referenceFonds.remove(fonds);
        fonds.getReferenceStorageLocation().remove(this);
    }

    public Set<Series> getReferenceSeries() {
        return referenceSeries;
    }

    public void addSeries(Series series) {
        this.referenceSeries.add(series);
        series.getReferenceStorageLocation().add(this);
    }

    public void removeSeries(Series series) {
        this.referenceSeries.remove(series);
        series.getReferenceStorageLocation().remove(this);
    }

    public Set<File> getReferenceFile() {
        return referenceFile;
    }

    public void addFile(File file) {
        this.referenceFile.add(file);
        file.getReferenceStorageLocation().add(this);
    }

    public void removeFile(File file) {
        this.referenceFile.remove(file);
        file.getReferenceStorageLocation().remove(this);
    }

    public Set<Record> getReferenceRecord() {
        return referenceRecord;
    }

    public void setReferenceRecord(Set<Record> referenceRecord) {
        this.referenceRecord = referenceRecord;
    }

    public void addRecord(Record record) {
        this.referenceRecord.add(record);
        record.getReferenceStorageLocation().add(this);
    }

    public void removeRecord(Record record) {
        this.referenceRecord.remove(record);
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
