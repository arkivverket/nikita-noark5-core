package nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

public interface IBuildingEntity
    extends INoarkEntity, ISystemId, Comparable<SystemIdEntity> {
    Integer getBuildingNumber();

    void setBuildingNumber(Integer buildingNumber);

    Integer getContinuousNumberingOfBuildingChange();

    void setContinuousNumberingOfBuildingChange(
            Integer continuousNumberingOfBuildingChange);
}
