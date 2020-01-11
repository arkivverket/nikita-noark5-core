package nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

public interface IDNumberEntity
        extends INoarkEntity, Comparable<NoarkEntity> {
    String getdNumber();

    void setdNumber(String dNumber);
}
