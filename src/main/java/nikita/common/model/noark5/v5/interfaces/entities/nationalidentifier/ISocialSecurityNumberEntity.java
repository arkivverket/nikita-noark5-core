package nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

public interface ISocialSecurityNumberEntity
        extends INoarkEntity, ISystemId, Comparable<SystemIdEntity> {
    String getSocialSecurityNumber();

    void setSocialSecurityNumber(String socialSecurityNumber);
}
