package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.StorageLocation;

import java.util.List;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IStorageLocation {
    List<StorageLocation> getReferenceStorageLocation();

    void setReferenceStorageLocation(List<StorageLocation> storageLocations);

    void addReferenceStorageLocation(StorageLocation storageLocation);
}
