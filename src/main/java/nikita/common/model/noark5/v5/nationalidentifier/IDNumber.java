package nikita.common.model.noark5.v5.nationalidentifier;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;

public interface IDNumber
        extends INikitaEntity, Comparable<NoarkEntity> {
    String getdNumber();

    void setdNumber(String dNumber);
}
