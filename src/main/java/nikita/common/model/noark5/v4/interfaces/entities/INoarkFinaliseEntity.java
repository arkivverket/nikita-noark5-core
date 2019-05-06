package nikita.common.model.noark5.v4.interfaces.entities;

import java.time.ZonedDateTime;

public interface INoarkFinaliseEntity {

    ZonedDateTime getFinalisedDate();

    void setFinalisedDate(ZonedDateTime FinalisedDate);

    String getFinalisedBy();

    void setFinalisedBy(String FinalisedBy);
}
