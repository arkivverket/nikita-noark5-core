package nikita.common.model.noark5.v5.casehandling.secondary;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ISimpleAddress;
import nikita.common.model.noark5.v5.secondary.PartPerson;
import nikita.common.model.noark5.v5.secondary.PartUnit;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.TABLE_POSTAL_ADDRESS;
import static nikita.common.config.N5ResourceMappings.SYSTEM_ID_ENG;

@Entity
@Table(name = TABLE_POSTAL_ADDRESS)
public class PostalAddress
        extends NoarkEntity
        implements ISystemId, ISimpleAddress {

    /**
     * M001 - systemID (xs:string)
     */
    @Id
    @Type(type = "uuid-char")
    @Column(name = SYSTEM_ID_ENG, insertable = false, updatable = false,
            nullable = false)
    private UUID systemId;

    @Embedded
    private SimpleAddress simpleAddress;

    @OneToOne(fetch = LAZY)
    private CorrespondencePartPerson correspondencePartPerson;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = SYSTEM_ID_ENG)
    @MapsId
    private CorrespondencePartUnit correspondencePartUnit;

    @OneToOne(fetch = LAZY)
    private PartPerson partPerson;

    @OneToOne(fetch = LAZY)
    private PartUnit partUnit;

    @Override
    public String getSystemId() {
        if (null != systemId)
            return systemId.toString();
        else
            return null;
    }

    @Override
    public void setSystemId(UUID systemId) {
        this.systemId = systemId;
    }

    @Override
    public UUID getId() {
        return systemId;
    }

    @Override
    public void setId(UUID systemId) {
        this.systemId = systemId;
    }

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

    public PartPerson getPartPerson() {
        return partPerson;
    }

    public void setPartPerson(PartPerson partPerson) {
        this.partPerson = partPerson;
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
