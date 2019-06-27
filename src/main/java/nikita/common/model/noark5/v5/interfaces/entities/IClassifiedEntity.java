package nikita.common.model.noark5.v5.interfaces.entities;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IClassifiedEntity extends Serializable {
    String getClassification();

    void setClassification(String classification);

    ZonedDateTime getClassificationDate();

    void setClassificationDate(ZonedDateTime classificationDate);

    String getClassificationBy();

    void setClassificationBy(String classificationBy);

    ZonedDateTime getClassificationDowngradedDate();

    void setClassificationDowngradedDate(ZonedDateTime classificationDowngradedDate);

    String getClassificationDowngradedBy();

    void setClassificationDowngradedBy(String classificationDowngradedBy);

}
