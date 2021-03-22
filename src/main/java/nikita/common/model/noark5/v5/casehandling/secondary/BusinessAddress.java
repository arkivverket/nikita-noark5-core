package nikita.common.model.noark5.v5.casehandling.secondary;

import nikita.common.model.noark5.v5.MappedSystemIdEntity;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ISimpleAddress;
import nikita.common.model.noark5.v5.secondary.PartUnit;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.TABLE_BUSINESS_ADDRESS;
import static nikita.common.config.N5ResourceMappings.SYSTEM_ID_ENG;

@Entity
@Table(name = TABLE_BUSINESS_ADDRESS)
public class BusinessAddress
        extends MappedSystemIdEntity
        implements ISimpleAddress {

    @Embedded
    private SimpleAddress simpleAddress;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = SYSTEM_ID_ENG)
    @MapsId
    private CorrespondencePartUnit correspondencePartUnit;

    @OneToOne(fetch = LAZY)
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
        correspondencePartUnit.setBusinessAddress(this);
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
