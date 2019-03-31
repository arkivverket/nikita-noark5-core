package nikita.common.model.noark5.v4.casehandling.secondary;

import nikita.common.model.noark5.v4.NoarkEntity;
import nikita.common.model.noark5.v4.interfaces.entities.casehandling.ISimpleAddress;

import javax.persistence.*;

/**
 * Created by tsodring on 5/14/17.
 */
@Entity
@Table(name = "postal_address")
public class PostalAddress
        extends NoarkEntity
        implements ISimpleAddress {

    @Embedded
    private SimpleAddress simpleAddress;

    @OneToOne(fetch = FetchType.LAZY)
    private CorrespondencePartPerson correspondencePartPerson;

    @OneToOne(fetch = FetchType.LAZY)
    private CorrespondencePartUnit correspondencePartUnit;

    public SimpleAddress getSimpleAddress() {
        return simpleAddress;
    }

    public void setSimpleAddress(SimpleAddress simpleAddress) {
        this.simpleAddress = simpleAddress;
    }

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
