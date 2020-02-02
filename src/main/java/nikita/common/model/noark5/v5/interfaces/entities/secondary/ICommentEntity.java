package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.metadata.CommentType;

import java.time.OffsetDateTime;

/**
 * Created by tsodring on 1/16/17.
 */
// TODO check if this inheritence is ok.
public interface ICommentEntity
        extends ISystemId {

    String getCommentText();

    void setCommentText(String commentText);

    CommentType getCommentType();

    void setCommentType(CommentType commentType);

    OffsetDateTime getCommentDate();

    void setCommentDate(OffsetDateTime commentDate);

    String getCommentRegisteredBy();

    void setCommentRegisteredBy(String commentRegisteredBy);

    File getReferenceFile();

    void setReferenceFile(File referenceFile);

    Record getReferenceRecord();

    void setReferenceRecord(Record referenceRecord);

    DocumentDescription getReferenceDocumentDescription();

    void setReferenceDocumentDescription
        (DocumentDescription referenceDocumentDescription);
}
