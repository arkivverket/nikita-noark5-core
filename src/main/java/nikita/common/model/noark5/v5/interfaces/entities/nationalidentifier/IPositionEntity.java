package nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;

public interface IPositionEntity
        extends INikitaEntity, Comparable<NoarkEntity> {
    String getCoordinateSystem();

    void setCoordinateSystem(String coordinateSystem);

    Double getX();

    void setX(Double x);

    Double getY();

    void setY(Double y);

    Double getZ();

    void setZ(Double z);
}
