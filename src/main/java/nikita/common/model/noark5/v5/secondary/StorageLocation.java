package nikita.common.model.noark5.v5.secondary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.*;
import nikita.common.util.serializers.noark5v5.StorageLocationSerializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private List<Fonds> referenceFonds = new ArrayList<>();

    // Links to Series
    @ManyToMany(mappedBy = "referenceStorageLocation")
    private List<Series> referenceSeries = new ArrayList<>();

    // Links to Files
    @OneToMany(mappedBy = "referenceStorageLocation")
    private List<File> referenceFile = new ArrayList<>();

    // Links to Records
    @ManyToMany(mappedBy = "referenceStorageLocation")
    @JsonIgnore
    private List<Record> referenceRecord = new ArrayList<>();


    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public List<Fonds> getReferenceFonds() {
        return referenceFonds;
    }

    public void setReferenceFonds(List<Fonds> referenceFonds) {
        this.referenceFonds = referenceFonds;
    }

    @Override
    public String getBaseTypeName() {
        return STORAGE_LOCATION;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_STORAGE_LOCATION;
    }

    public List<Series> getReferenceSeries() {
        return referenceSeries;
    }

    public void setReferenceSeries(List<Series> referenceSeries) {
        this.referenceSeries = referenceSeries;
    }

    public List<File> getReferenceFile() {
        return referenceFile;
    }

    public void setReferenceFile(List<File> referenceFile) {
        this.referenceFile = referenceFile;
    }

    public List<Record> getReferenceRecord() {
        return referenceRecord;
    }

    public void setReferenceRecord(List<Record> referenceRecord) {
        this.referenceRecord = referenceRecord;
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
