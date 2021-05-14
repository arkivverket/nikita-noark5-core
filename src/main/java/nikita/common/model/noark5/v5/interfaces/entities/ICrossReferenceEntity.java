package nikita.common.model.noark5.v5.interfaces.entities;

import java.io.Serializable;
import java.util.UUID;

public interface ICrossReferenceEntity
        extends Serializable, ISystemId {

    UUID getFromSystemId();

    void setFromSystemId(UUID fromSystemId);

    UUID getToSystemId();

    void setToSystemId(UUID toSystemId);

    String getReferenceType();

    void setReferenceType(String toSystemId);
}
