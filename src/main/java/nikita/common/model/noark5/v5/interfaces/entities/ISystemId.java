package nikita.common.model.noark5.v5.interfaces.entities;

import java.util.UUID;

public interface ISystemId
        extends INoarkEntity {
    String getSystemId();
    void setSystemId(UUID systemId);
    UUID getId();
    void setId(UUID systemId);
}
