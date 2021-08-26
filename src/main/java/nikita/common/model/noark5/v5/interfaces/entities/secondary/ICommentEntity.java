package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.RecordEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.metadata.CommentType;

import java.time.OffsetDateTime;
import java.util.Set;

// TODO check if this inheritance is ok.
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

    Set<File> getReferenceFile();

    void addFile(File referenceFile);

    Set<RecordEntity> getReferenceRecord();

    void addRecord(RecordEntity referenceRecord);

    Set<DocumentDescription> getReferenceDocumentDescription();

    void addDocumentDescription(
            DocumentDescription referenceDocumentDescription);
}
