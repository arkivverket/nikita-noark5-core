package nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

public interface ISocialSecurityNumberEntity
        extends INoarkEntity, Comparable<NoarkEntity> {
    String getSocialSecurityNumber();

    void setSocialSecurityNumber(String socialSecurityNumber);
}
