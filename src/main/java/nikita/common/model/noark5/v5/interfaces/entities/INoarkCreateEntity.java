package nikita.common.model.noark5.v5.interfaces.entities;

import java.time.OffsetDateTime;

public interface INoarkCreateEntity {

    OffsetDateTime getCreatedDate();

    void setCreatedDate(OffsetDateTime createdDate);

    String getCreatedBy();

    void setCreatedBy(String createdBy);
}
