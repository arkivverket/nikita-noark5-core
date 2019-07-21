package nikita.common.model.noark5.v5.nationalidentifier;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.PositionHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier.IPositionEntity;
import nikita.common.util.deserialisers.nationalidentifier.PositionDeserializer;
import nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier.PositionSerializer;
import nikita.webapp.hateoas.nationalidentifier.PositionHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;

import static javax.persistence.InheritanceType.JOINED;
import static nikita.common.config.Constants.TABLE_POSITION;

@Entity
@Table(name = TABLE_POSITION)
@Inheritance(strategy = JOINED)
@JsonSerialize(using = PositionSerializer.class)
@JsonDeserialize(using = PositionDeserializer.class)
@HateoasPacker(using = PositionHateoasHandler.class)
@HateoasObject(using = PositionHateoas.class)
public class Position
        extends NationalIdentifier
        implements IPositionEntity {

    /**
     * M??? - koordinatsystem (xs:string)
     */
    @Column(name = "coordinate_system", nullable = false)
    @Audited
    private String coordinateSystem;

    /**
     * M??? - x (xs:decimal)
     * Comment: East-West / latitude
     */
    @Column(name = "x", nullable = false)
    @Audited
    private Double x;

    /**
     * M??? - y (xs:decimal)
     * Comment: north-south / longitude
     */
    @Column(name = "y", nullable = false)
    @Audited
    private Double y;

    /**
     * M??? - z (xs:decimal)
     * height
     */
    @Column(name = "z")
    @Audited
    private Double z;

    @Override
    public String getCoordinateSystem() {
        return coordinateSystem;
    }

    @Override
    public void setCoordinateSystem(String coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
    }

    @Override
    public Double getX() {
        return x;
    }

    @Override
    public void setX(Double x) {
        this.x = x;
    }

    @Override
    public Double getY() {
        return y;
    }

    @Override
    public void setY(Double y) {
        this.y = y;
    }

    @Override
    public Double getZ() {
        return z;
    }

    @Override
    public void setZ(Double z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "Position{" +
                "coordinateSystem='" + coordinateSystem + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != getClass()) {
            return false;
        }
        Position rhs = (Position) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(coordinateSystem, rhs.coordinateSystem)
                .append(x, rhs.x)
                .append(y, rhs.y)
                .append(z, rhs.z)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(coordinateSystem)
                .append(x)
                .append(y)
                .append(z)
                .toHashCode();
    }
}
