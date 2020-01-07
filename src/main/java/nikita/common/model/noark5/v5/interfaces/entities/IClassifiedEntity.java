package nikita.common.model.noark5.v5.interfaces.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IClassifiedEntity extends Serializable {
    String getClassificationCode();

    void setClassificationCode(String classificationCode);

    String getClassificationName();

    void setClassificationName(String classificationName);

    OffsetDateTime getClassificationDate();

    void setClassificationDate(OffsetDateTime classificationDate);

    String getClassificationBy();

    void setClassificationBy(String classificationBy);

    OffsetDateTime getClassificationDowngradedDate();

    void setClassificationDowngradedDate(OffsetDateTime classificationDowngradedDate);

    String getClassificationDowngradedBy();

    void setClassificationDowngradedBy(String classificationDowngradedBy);

}
