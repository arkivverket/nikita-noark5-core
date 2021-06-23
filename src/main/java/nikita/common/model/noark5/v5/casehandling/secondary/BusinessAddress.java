package nikita.common.model.noark5.v5.casehandling.secondary;

import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ISimpleAddress;
import nikita.common.model.noark5.v5.secondary.PartUnit;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.TABLE_BUSINESS_ADDRESS;

@Entity
@Table(name = TABLE_BUSINESS_ADDRESS)
@Indexed
public class BusinessAddress
        extends SystemIdEntity
        implements ISystemId, ISimpleAddress {

    @Embedded
    @IndexedEmbedded
    private SimpleAddress simpleAddress;

    @OneToOne(fetch = LAZY)
    private CorrespondencePartUnit referenceCorrespondencePartUnit;

    @OneToOne(fetch = LAZY)
    private PartUnit partUnit;

    public SimpleAddress getSimpleAddress() {
        return simpleAddress;
    }

    public void setSimpleAddress(SimpleAddress simpleAddress) {
        this.simpleAddress = simpleAddress;
    }

    public CorrespondencePartUnit getReferenceCorrespondencePartUnit() {
        return referenceCorrespondencePartUnit;
    }

    public void setReferenceCorrespondencePartUnit(
            CorrespondencePartUnit referenceCorrespondencePartUnit) {
        this.referenceCorrespondencePartUnit = referenceCorrespondencePartUnit;
    }

    public PartUnit getPartUnit() {
        return partUnit;
    }

    public void setPartUnit(PartUnit partUnit) {
        this.partUnit = partUnit;
    }
}
