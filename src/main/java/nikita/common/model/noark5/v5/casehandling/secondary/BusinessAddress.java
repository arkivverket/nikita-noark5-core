package nikita.common.model.noark5.v5.casehandling.secondary;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.PartUnit;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ISimpleAddress;

import javax.persistence.*;

import static nikita.common.config.Constants.TABLE_BUSINESS_ADDRESS;

/**
 * Created by tsodring on 5/14/17.
 */
@Entity
@Table(name = TABLE_BUSINESS_ADDRESS)
public class BusinessAddress
        extends NoarkEntity
        implements ISimpleAddress {

    @Embedded
    private SimpleAddress simpleAddress;

    @OneToOne(fetch = FetchType.LAZY)
    private CorrespondencePartUnit correspondencePartUnit;

    @OneToOne(fetch = FetchType.LAZY)
    private PartUnit partUnit;

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

    public PartUnit getPartUnit() {
        return partUnit;
    }

    public void setPartUnit(PartUnit partUnit) {
        this.partUnit = partUnit;
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
