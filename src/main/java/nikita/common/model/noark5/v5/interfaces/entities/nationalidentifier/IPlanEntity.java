package nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.metadata.Country;

public interface IPlanEntity
        extends INoarkEntity, ISystemId, Comparable<SystemIdEntity> {
    String getMunicipalityNumber();

    void setMunicipalityNumber(String municipalityNumber);

    String getCountyNumber();

    void setCountyNumber(String countyNumber);

    Country getCountry();

    void setCountry(Country country);

    String getPlanIdentification();

    void setPlanIdentification(String planIdentification);
}
