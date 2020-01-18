package nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

public interface IPositionEntity
        extends INoarkEntity, ISystemId, Comparable<SystemIdEntity> {
    String getCoordinateSystemCode();

    void setCoordinateSystemCode(String coordinateSystemCode);

    String getCoordinateSystemCodeName();

    void setCoordinateSystemCodeName(String coordinateSystemCodeName);

    Double getX();

    void setX(Double x);

    Double getY();

    void setY(Double y);

    Double getZ();

    void setZ(Double z);
}
