package nikita.common.model.noark5.v5.interfaces.entities;

import java.time.OffsetDateTime;

public interface INoarkLastModifiedEntity {
    OffsetDateTime getLastModifiedDate();

    void setLastModifiedDate(OffsetDateTime lastModifiedDate);

    String getLastModifiedBy();

    void setLastModifiedBy(String lastModifiedBy);
}
