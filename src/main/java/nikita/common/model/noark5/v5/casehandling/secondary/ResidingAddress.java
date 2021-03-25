package nikita.common.model.noark5.v5.casehandling.secondary;

import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.secondary.PartPerson;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.TABLE_RESIDING_ADDRESS;

@Entity
@Table(name = TABLE_RESIDING_ADDRESS)
public class ResidingAddress
        extends SystemIdEntity
        implements ISystemId {

    @Embedded
    SimpleAddress simpleAddress;

    @OneToOne(fetch = LAZY)
    CorrespondencePartPerson referenceCorrespondencePartPerson;

    @OneToOne(fetch = LAZY)
    private PartPerson partPerson;

    public SimpleAddress getSimpleAddress() {
        return simpleAddress;
    }

    public void setSimpleAddress(SimpleAddress simpleAddress) {
        this.simpleAddress = simpleAddress;
    }

    public CorrespondencePartPerson getCorrespondencePartPerson() {
        return referenceCorrespondencePartPerson;
    }

    public void setCorrespondencePartPerson(
            CorrespondencePartPerson referenceCorrespondencePartPerson) {
        this.referenceCorrespondencePartPerson = referenceCorrespondencePartPerson;
    }

    public PartPerson getPartPerson() {
        return partPerson;
    }

    public void setPartPerson(PartPerson partPerson) {
        this.partPerson = partPerson;
    }
}
