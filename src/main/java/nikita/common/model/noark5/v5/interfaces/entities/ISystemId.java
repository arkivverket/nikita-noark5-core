package nikita.common.model.noark5.v5.interfaces.entities;

import java.util.UUID;

public interface ISystemId
        extends INoarkEntity {
    UUID getSystemId();

    String getSystemIdAsString();

    void setSystemId(UUID systemId);
}
