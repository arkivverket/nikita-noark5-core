package nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

public interface IBuilding
        extends INoarkEntity, Comparable<SystemIdEntity> {
    Integer getBuildingNumber();

    void setBuildingNumber(Integer buildingNumber);

    Integer getContinuousNumberingOfBuildingChange();

    void setContinuousNumberingOfBuildingChange(
            Integer continuousNumberingOfBuildingChange);
}
