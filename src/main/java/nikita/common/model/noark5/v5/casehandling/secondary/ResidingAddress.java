package nikita.common.model.noark5.v5.casehandling.secondary;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.secondary.PartPerson;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.TABLE_RESIDING_ADDRESS;
import static nikita.common.config.N5ResourceMappings.SYSTEM_ID_ENG;

/**
 * Created by tsodring on 5/14/17.
 */
@Entity
@Table(name = TABLE_RESIDING_ADDRESS)
public class ResidingAddress
        extends NoarkEntity
        implements ISystemId {

    /**
     * M001 - systemID (xs:string)
     */
    @Id
    @Type(type = "uuid-char")
    @Column(name = SYSTEM_ID_ENG, insertable = false, updatable = false,
            nullable = false)
    private UUID systemId;

    @Embedded
    SimpleAddress simpleAddress;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = SYSTEM_ID_ENG)
    @MapsId
    CorrespondencePartPerson referenceCorrespondencePartPerson;

    @OneToOne(fetch = LAZY)
    private PartPerson partPerson;

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

    @Override
    public String getIdentifier() {
        return getSystemId();
    }

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
