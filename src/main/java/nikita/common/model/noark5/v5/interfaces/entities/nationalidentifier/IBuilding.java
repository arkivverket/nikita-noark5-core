package nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;

public interface IBuilding
        extends INikitaEntity, Comparable<NoarkEntity> {
    Integer getBuildingNumber();

    void setBuildingNumber(Integer buildingNumber);

    Integer getContinuousNumberingOfBuildingChange();

    void setContinuousNumberingOfBuildingChange(
            Integer continuousNumberingOfBuildingChange);
}
