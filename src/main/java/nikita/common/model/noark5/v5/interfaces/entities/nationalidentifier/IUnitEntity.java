package nikita.common.model.noark5.v5.interfaces.entities.nationalidentifier;

import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

public interface IUnitEntity
        extends ISystemId {
    String getUnitIdentifier();

    void setUnitIdentifier(String organisationNumber);
}
