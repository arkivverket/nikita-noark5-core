package nikita.common.model.noark5.v5.interfaces.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IDisposalUndertakenEntity extends Serializable {
    String getDisposalBy();

    void setDisposalBy(String disposalBy);

    OffsetDateTime getDisposalDate();

    void setDisposalDate(OffsetDateTime disposalDate);
}
