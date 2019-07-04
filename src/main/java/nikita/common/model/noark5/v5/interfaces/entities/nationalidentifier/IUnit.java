package nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;

public interface IUnit
        extends INikitaEntity, Comparable<NoarkEntity> {
    String getOrganisationNumber();

    void setOrganisationNumber(String organisationNumber);
}
