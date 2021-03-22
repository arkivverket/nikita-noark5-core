package nikita.common.model.noark5.v5.casehandling.secondary;

import nikita.common.model.noark5.v5.MappedSystemIdEntity;
import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ISimpleAddress;
import nikita.common.model.noark5.v5.secondary.PartUnit;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static java.util.UUID.fromString;
import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.NOARK_FONDS_STRUCTURE_PATH;
import static nikita.common.config.Constants.TABLE_BUSINESS_ADDRESS;
import static nikita.common.config.N5ResourceMappings.SYSTEM_ID;
import static nikita.common.config.N5ResourceMappings.SYSTEM_ID_ENG;

@Entity
@Table(name = TABLE_BUSINESS_ADDRESS)
public class BusinessAddress
//        extends MappedSystemIdEntity
//        implements ISimpleAddress {
        extends NoarkEntity
        implements ISystemId, Comparable<MappedSystemIdEntity>,
        ISimpleAddress {

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

    @Override
    public String getIdentifierType() {
        return SYSTEM_ID;
    }

    // Most entities belong to arkivstruktur. These entities pick the value
    // up here
    @Override
    public String getFunctionalTypeName() {
        return NOARK_FONDS_STRUCTURE_PATH;
    }

    @Override
    public void createReference(
            @NotNull INoarkEntity entity,
            @NotNull String referenceType) {
        // I really should be overridden. Currently throwing an Exception if I
        // am not overriden as nikita is unable to process this
        throw new NikitaMalformedInputDataException("Error when trying to " +
                "create a reference between entities");
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(systemId)
                .toHashCode();
    }

    @Override
    public int compareTo(MappedSystemIdEntity otherEntity) {
        if (null == otherEntity) {
            return -1;
        }
        return new CompareToBuilder()
                .append(this.systemId,
                        fromString(otherEntity.getSystemId()))
                .toComparison();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != getClass()) {
            return false;
        }
        MappedSystemIdEntity rhs = (MappedSystemIdEntity) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(systemId, fromString(rhs.getSystemId()))
                .isEquals();
    }
}
