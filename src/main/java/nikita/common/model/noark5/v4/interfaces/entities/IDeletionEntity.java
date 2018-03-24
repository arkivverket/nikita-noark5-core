package nikita.common.model.noark5.v4.interfaces.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IDeletionEntity extends Serializable {
    String getDeletionType();

    void setDeletionType(String deletionType);

    String getDeletionBy();

    void setDeletionBy(String deletionBy);

    Date getDeletionDate();

    void setDeletionDate(Date deletionDate);
}
