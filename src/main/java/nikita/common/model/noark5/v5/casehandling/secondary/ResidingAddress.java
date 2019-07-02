package nikita.common.model.noark5.v5.casehandling.secondary;

import nikita.common.model.noark5.v5.NoarkEntity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.TABLE_RESIDING_ADDRESS;

/**
 * Created by tsodring on 5/14/17.
 */
@Entity
@Table(name = TABLE_RESIDING_ADDRESS)
public class ResidingAddress
        extends NoarkEntity {

    @Embedded
    SimpleAddress simpleAddress;

    @OneToOne(fetch = LAZY)
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

    public void setCorrespondencePartPerson(
            CorrespondencePartPerson correspondencePartPerson) {
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
