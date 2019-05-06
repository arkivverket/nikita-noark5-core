package nikita.common.model.noark5.v4.interfaces.entities;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Created by tsodring on 1/16/17.
 */
public interface ICommentEntity extends Serializable {
    String getCommentText();

    void setCommentText(String commentText);

    String getCommentType();

    void setCommentType(String commentType);

    ZonedDateTime getCommentDate();

    void setCommentDate(ZonedDateTime commentDate);

    String getCommentRegisteredBy();

    void setCommentRegisteredBy(String commentRegisteredBy);
}
