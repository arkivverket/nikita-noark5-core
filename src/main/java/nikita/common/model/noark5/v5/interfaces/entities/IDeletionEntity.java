package nikita.common.model.noark5.v5.interfaces.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IDeletionEntity extends Serializable {
    String getDeletionType();

    void setDeletionType(String deletionType);

    String getDeletionBy();

    void setDeletionBy(String deletionBy);

    OffsetDateTime getDeletionDate();

    void setDeletionDate(OffsetDateTime deletionDate);
}
