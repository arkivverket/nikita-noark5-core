package nikita.common.model.noark5.v5.nationalidentifier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.PositionHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier.IPositionEntity;
import nikita.common.model.noark5.v5.metadata.CoordinateSystem;
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
import static nikita.common.config.Constants.REL_FONDS_STRUCTURE_POSITION;
import static nikita.common.config.Constants.TABLE_POSITION;
import static nikita.common.config.N5ResourceMappings.*;

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
     * M??? - koordinatsystem code (xs:string)
     */
    @Column(name = "coordinate_system_code", nullable = false)
    @Audited
    private String coordinateSystemCode;

    /**
     * M??? - koordinatsystem code name (xs:string)
     */
    @Column(name = "coordinate_system_code_name", nullable = false)
    @Audited
    private String coordinateSystemCodeName;

    /**
     * M??? - x (xs:decimal)
     * Comment: East-West / latitude
     */
    @Column(name = X_ENG, nullable = false)
    @Audited
    @JsonProperty(X)
    private Double x;

    /**
     * M??? - y (xs:decimal)
     * Comment: north-south / longitude
     */
    @Column(name = Y_ENG, nullable = false)
    @Audited
    @JsonProperty(Y)
    private Double y;

    /**
     * M??? - z (xs:decimal)
     * height
     */
    @Column(name = Z_ENG)
    @Audited
    @JsonProperty(Z)
    private Double z;

    @Override
    public CoordinateSystem getCoordinateSystem() {
        if (null == coordinateSystemCode)
            return null;
        return new CoordinateSystem(coordinateSystemCode,
                                    coordinateSystemCodeName);
    }

    @Override
    public void setCoordinateSystem(CoordinateSystem coordinateSystem) {
        if (null != coordinateSystem) {
            this.coordinateSystemCode = coordinateSystem.getCode();
            this.coordinateSystemCodeName = coordinateSystem.getCodeName();
        } else {
            this.coordinateSystemCode = null;
            this.coordinateSystemCodeName = null;
        }
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
    public String getBaseTypeName() {
        return POSITION;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_POSITION;
    }

    @Override
    public String toString() {
        return "Position{" +
                "coordinateSystemCode='" + coordinateSystemCode + '\'' +
                "coordinateSystemCodeName='" + coordinateSystemCodeName + '\'' +
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
                .append(coordinateSystemCode, rhs.coordinateSystemCode)
                .append(coordinateSystemCodeName, rhs.coordinateSystemCodeName)
                .append(x, rhs.x)
                .append(y, rhs.y)
                .append(z, rhs.z)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(coordinateSystemCode)
                .append(coordinateSystemCodeName)
                .append(x)
                .append(y)
                .append(z)
                .toHashCode();
    }
}
