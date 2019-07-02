package nikita.common.model.noark5.v5.interfaces.entities;

import java.time.OffsetDateTime;

public interface INoarkFinaliseEntity {

    OffsetDateTime getFinalisedDate();

    void setFinalisedDate(OffsetDateTime FinalisedDate);

    String getFinalisedBy();

    void setFinalisedBy(String FinalisedBy);
}
