package nikita.common.model.noark5.v4.interfaces.entities;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IDisposalUndertakenEntity extends Serializable {
    String getDisposalBy();

    void setDisposalBy(String disposalBy);

    ZonedDateTime getDisposalDate();

    void setDisposalDate(ZonedDateTime disposalDate);
}
