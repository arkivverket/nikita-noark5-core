package nikita.common.model.noark5.v4.casehandling.secondary;

/**
 * Created by tsodring on 5/14/17.
 */
//@Entity
//@Table(name = "residing_address")
//@AttributeOverride(name = "pk_simple_address_id",
//       column = @Column(name = "pk_residing_address_id"))
//@DiscriminatorValue(value = "ResidingAddress")
public class ResidingAddress {
    //       extends SimpleAddress {


    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
