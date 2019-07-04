package nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.metadata.Country;

public interface IPlan
        extends INikitaEntity, Comparable<NoarkEntity> {
    String getMunicipalityNumber();

    void setMunicipalityNumber(String municipalityNumber);

    String getCountyNumber();

    void setCountyNumber(String countyNumber);

    Country getCountry();

    void setCountry(Country country);

    String getPlanIdentification();

    void setPlanIdentification(String planIdentification);
}
