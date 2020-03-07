package nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier;

import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.metadata.CoordinateSystem;

public interface IPositionEntity
        extends ISystemId {
    CoordinateSystem getCoordinateSystem();

    void setCoordinateSystem(CoordinateSystem coordinateSystem);

    Double getX();

    void setX(Double x);

    Double getY();

    void setY(Double y);

    Double getZ();

    void setZ(Double z);
}
