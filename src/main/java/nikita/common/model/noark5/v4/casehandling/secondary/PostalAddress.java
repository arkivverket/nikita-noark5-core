package nikita.common.model.noark5.v4.casehandling.secondary;

/**
 * Created by tsodring on 5/14/17.
 */
//@Entity
//@Table(name = "postal_address")
//@AttributeOverride(name = "pk_simple_address_id",
//        column = @Column(name = "pk_postal_address_id"))
public class PostalAddress
        extends SimpleAddress {

    //@OneToOne(mappedBy = "postalAddress", cascade = CascadeType.ALL,
    //        fetch = FetchType.LAZY, optional = false)
    //CorrespondencePartPerson correspondencePartPerson;

    //    @OneToOne(fetch = FetchType.LAZY)
    //   @MapsId
    //   CorrespondencePartUnit correspondencePartUnit;
/*
    public CorrespondencePartPerson getCorrespondencePartPerson() {
        return correspondencePartPerson;
    }

    public void setCorrespondencePartPerson(
            CorrespondencePartPerson correspondencePartPerson) {
        this.correspondencePartPerson = correspondencePartPerson;
    }

    public CorrespondencePartUnit getCorrespondencePartUnit() {
        return correspondencePartUnit;
    }

    public void setCorrespondencePartUnit(
            CorrespondencePartUnit correspondencePartUnit) {
        this.correspondencePartUnit = correspondencePartUnit;
    }
*/
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
