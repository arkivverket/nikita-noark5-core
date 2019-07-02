package nikita.common.model.noark5.v5.interfaces.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * Created by tsodring on 1/16/17.
 */
public interface ICommentEntity extends Serializable {
    String getCommentText();

    void setCommentText(String commentText);

    String getCommentType();

    void setCommentType(String commentType);

    OffsetDateTime getCommentDate();

    void setCommentDate(OffsetDateTime commentDate);

    String getCommentRegisteredBy();

    void setCommentRegisteredBy(String commentRegisteredBy);
}
