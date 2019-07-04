package nikita.common.model.noark5.v5.nationalidentifier;

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
//@JsonDeserialize(using = BuildingDeserializer.class)
//@HateoasPacker(using = BuildingHateoasHandler.class)
//@HateoasObject(using = BuildingHateoas.class)
public class Building
        extends NationalIdentifier {

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

    public Integer getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(Integer buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public Integer getContinuousNumberingOfBuildingChange() {
        return continuousNumberingOfBuildingChange;
    }

    public void setContinuousNumberingOfBuildingChange(Integer continuousNumberingOfBuildingChange) {
        this.continuousNumberingOfBuildingChange = continuousNumberingOfBuildingChange;
    }

    @Override
    public String toString() {
        return "Building{" +
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
