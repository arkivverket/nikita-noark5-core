package nikita.common.model.noark5.v5.casehandling.secondary;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.casehandling.ISimpleAddress;

import javax.persistence.*;

/**
 * Created by tsodring on 5/14/17.
 */
@Entity
@Table(name = "business_address")
public class BusinessAddress
        extends NoarkEntity
        implements ISimpleAddress {

    @Embedded
    private SimpleAddress simpleAddress;

    @OneToOne(fetch = FetchType.LAZY)
    private CorrespondencePartUnit correspondencePartUnit;

    public SimpleAddress getSimpleAddress() {
        return simpleAddress;
    }

    public void setSimpleAddress(SimpleAddress simpleAddress) {
        this.simpleAddress = simpleAddress;
    }

    public CorrespondencePartUnit getCorrespondencePartUnit() {
        return correspondencePartUnit;
    }

    public void setCorrespondencePartUnit(CorrespondencePartUnit
                                                  correspondencePartUnit) {
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
