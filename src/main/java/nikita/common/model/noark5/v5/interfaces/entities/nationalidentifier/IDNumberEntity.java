package nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier;

import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

public interface IDNumberEntity
        extends ISystemId {
    String getdNumber();

    void setdNumber(String dNumber);
}
