package nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier;

import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

public interface ICadastralUnitEntity
        extends ISystemId {

    String getMunicipalityNumber();

    void setMunicipalityNumber(String municipalityNumber);

    Integer getHoldingNumber();

    void setHoldingNumber(Integer holdingNumber);

    Integer getSubHoldingNumber();

    void setSubHoldingNumber(Integer subHoldingNumber);

    Integer getLeaseNumber();

    void setLeaseNumber(Integer leaseNumber);

    Integer getSectionNumber();

    void setSectionNumber(Integer sectionNumber);
}
