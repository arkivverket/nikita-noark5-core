package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.StorageLocation;

import java.util.Set;

public interface IStorageLocation {
    Set<StorageLocation> getReferenceStorageLocation();

    void addStorageLocation(StorageLocation storageLocation);
}
