package nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier;

import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

public interface ISocialSecurityNumberEntity
        extends ISystemId {
    String getSocialSecurityNumber();

    void setSocialSecurityNumber(String socialSecurityNumber);
}
