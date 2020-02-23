package nikita.common.model.noark5.v5.interfaces.entities;

import nikita.common.model.noark5.v5.metadata.DeletionType;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IDeletionEntity extends Serializable {
    DeletionType getDeletionType();

    void setDeletionType(DeletionType deletionType);

    String getDeletionBy();

    void setDeletionBy(String deletionBy);

    OffsetDateTime getDeletionDate();

    void setDeletionDate(OffsetDateTime deletionDate);
}
