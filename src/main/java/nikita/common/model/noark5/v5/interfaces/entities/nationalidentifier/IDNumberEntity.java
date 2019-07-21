package nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;

public interface IDNumberEntity
        extends INikitaEntity, Comparable<NoarkEntity> {
    String getdNumber();

    void setdNumber(String dNumber);
}
