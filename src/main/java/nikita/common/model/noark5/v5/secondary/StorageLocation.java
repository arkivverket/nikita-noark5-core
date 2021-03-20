package nikita.common.model.noark5.v5.secondary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.util.serializers.noark5v5.StorageLocationSerializer;

import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.TABLE_STORAGE_LOCATION;

@Entity
@Table(name = TABLE_STORAGE_LOCATION)
@JsonSerialize(using = StorageLocationSerializer.class)
public class StorageLocation
        extends SystemIdEntity {

    /**
     * m301 - oppbevaringssted (xs:string)
     */
    @column(name = "storage_location")
    @audited
    private string storagelocation;

    // links to fonds
    @manytomany(mappedby = "referencestoragelocation")
    @jsonignore
    private set<fonds> referencefonds = new hashset<>();

    // links to series
    @manytomany(mappedby = "referencestoragelocation")
    private set<series> referenceseries = new hashset<>();

    // links to file
    @manytomany(mappedby = "referencestoragelocation")
    private set<file> referencefile = new hashset<>();

    // links to record
    @manytomany(mappedby = "referencestoragelocation")
    @jsonignore
    private set<record> referencerecord = new hashset<>();

    public string getstoragelocation() {
        return storagelocation;
    }

    public void setstoragelocation(string storagelocation) {
        this.storagelocation = storagelocation;
    }

    public set<fonds> getreferencefonds() {
        return referencefonds;
    }

    public void addfonds(fonds fonds) {
        this.referencefonds.add(fonds);
        fonds.getreferencestoragelocation().add(this);
    }

    public void removefonds(fonds fonds) {
        this.referencefonds.remove(fonds);
        fonds.getreferencestoragelocation().remove(this);
    }

    public set<series> getreferenceseries() {
        return referenceseries;
    }

    public void addseries(series series) {
        this.referenceseries.add(series);
        series.getreferencestoragelocation().add(this);
    }

    public void removeseries(series series) {
        this.referenceseries.remove(series);
        series.getreferencestoragelocation().remove(this);
    }

    public set<file> getreferencefile() {
        return referencefile;
    }

    public void addfile(file file) {
        this.referencefile.add(file);
        file.getreferencestoragelocation().add(this);
    }

    public void removefile(file file) {
        this.referencefile.remove(file);
        file.getreferencestoragelocation().remove(this);
    }

    public set<record> getreferencerecord() {
        return referencerecord;
    }

    public void setreferencerecord(set<record> referencerecord) {
        this.referencerecord = referencerecord;
    }

    public void addrecord(record record) {
        this.referencerecord.add(record);
        record.getreferencestoragelocation().add(this);
    }

    public void removerecord(record record) {
        this.referencerecord.remove(record);
        record.getreferencestoragelocation().remove(this);
    }

    @override
    public string getbasetypename() {
        return storage_location;
    }

    @override
    public string getbaserel() {
        return rel_fonds_structure_storage_location;
    }

    @override
    public string tostring() {
        return "storagelocation{" + super.tostring() +
                ", storagelocation='" + storagelocation + '\'' +
                '}';
    }

    @override
    public boolean equals(object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getclass() != getclass()) {
            return false;
        }
        storagelocation rhs = (storagelocation) other;
        return new equalsbuilder()
                .appendsuper(super.equals(other))
                .append(storagelocation, rhs.storagelocation)
                .isequals();
    }

    @override
    public int hashcode() {
        return new hashcodebuilder()
                .appendsuper(super.hashcode())
                .append(storagelocation)
                .tohashcode();
    }
}
