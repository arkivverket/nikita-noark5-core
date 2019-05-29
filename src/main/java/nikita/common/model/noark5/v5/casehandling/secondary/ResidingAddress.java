package nikita.common.model.noark5.v5.casehandling.secondary;

import nikita.common.model.noark5.v5.NoarkEntity;

import javax.persistence.*;

/**
 * Created by tsodring on 5/14/17.
 */
@Entity
@Table(name = "residing_address")
public class ResidingAddress
        extends NoarkEntity {

    @Embedded
    SimpleAddress simpleAddress;

    @OneToOne(fetch = FetchType.LAZY)
    CorrespondencePartPerson correspondencePartPerson;

    public SimpleAddress getSimpleAddress() {
        return simpleAddress;
    }

    public void setSimpleAddress(SimpleAddress simpleAddress) {
        this.simpleAddress = simpleAddress;
    }

    public CorrespondencePartPerson getCorrespondencePartPerson() {
        return correspondencePartPerson;
    }

    public void setCorrespondencePartPerson(CorrespondencePartPerson correspondencePartPerson) {
        this.correspondencePartPerson = correspondencePartPerson;
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
