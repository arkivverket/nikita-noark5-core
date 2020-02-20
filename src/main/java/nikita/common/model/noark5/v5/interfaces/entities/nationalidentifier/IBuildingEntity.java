package nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier;

import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

public interface IBuildingEntity
        extends ISystemId {
    Integer getBuildingNumber();

    void setBuildingNumber(Integer buildingNumber);

    Integer getRunningChangeNumber();

    void setRunningChangeNumber(Integer runningChangeNumber);
}
