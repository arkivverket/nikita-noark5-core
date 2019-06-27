package nikita.common.model.noark5.v5.interfaces.entities;

import java.time.ZonedDateTime;

public interface INoarkCreateEntity {

    ZonedDateTime getCreatedDate();

    void setCreatedDate(ZonedDateTime createdDate);

    String getCreatedBy();

    void setCreatedBy(String createdBy);
}
