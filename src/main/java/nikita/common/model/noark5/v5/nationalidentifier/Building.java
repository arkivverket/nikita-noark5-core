package nikita.common.model.noark5.v5.nationalidentifier;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.nationalidentifier.BuildingHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier.IBuilding;
import nikita.common.util.deserialisers.nationalidentifier.BuildingDeserializer;
import nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier.BuildingSerializer;
import nikita.webapp.hateoas.nationalidentifier.BuildingHateoasHandler;
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
import static nikita.common.config.Constants.TABLE_BUILDING;

@Entity
@Table(name = TABLE_BUILDING)
@Inheritance(strategy = JOINED)
@JsonSerialize(using = BuildingSerializer.class)
@JsonDeserialize(using = BuildingDeserializer.class)
@HateoasPacker(using = BuildingHateoasHandler.class)
@HateoasObject(using = BuildingHateoas.class)
public class Building
        extends NationalIdentifier
        implements IBuilding {

    /**
     * M??? bygningsnummer - (xs:integer)
     */
    @Column(name = "building_number", nullable = false)
    @Audited
    Integer buildingNumber;

    /**
     * M??? endringsloepenummer - (xs:integer)
     */
    @Column(name = "building_change_number")
    @Audited
    Integer continuousNumberingOfBuildingChange;

    @Override
    public Integer getBuildingNumber() {
        return buildingNumber;
    }

    @Override
    public void setBuildingNumber(Integer buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    @Override
    public Integer getContinuousNumberingOfBuildingChange() {
        return continuousNumberingOfBuildingChange;
    }

    @Override
    public void setContinuousNumberingOfBuildingChange(Integer continuousNumberingOfBuildingChange) {
        this.continuousNumberingOfBuildingChange = continuousNumberingOfBuildingChange;
    }

    @Override
    public String toString() {
        return "IBuilding{" +
                "buildingNumber=" + buildingNumber +
                ", continuousNumberingOfBuildingChange=" +
                continuousNumberingOfBuildingChange +
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
        Building rhs = (Building) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(buildingNumber, rhs.buildingNumber)
                .append(continuousNumberingOfBuildingChange,
                        rhs.continuousNumberingOfBuildingChange)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(buildingNumber)
                .append(continuousNumberingOfBuildingChange)
                .toHashCode();
    }
}