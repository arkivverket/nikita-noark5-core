package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

public interface IStorageLocationEntity
        extends ISystemId {
    String getStorageLocation();

    void setStorageLocation(String keyword);
}
