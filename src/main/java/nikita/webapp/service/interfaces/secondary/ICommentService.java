package nikita.webapp.service.interfaces.secondary;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.secondary.CommentHateoas;
import nikita.common.model.noark5.v5.secondary.Comment;

import javax.validation.constraints.NotNull;

public interface ICommentService {

    CommentHateoas generateDefaultComment();

    CommentHateoas createNewComment(Comment comment, File file);

    CommentHateoas createNewComment(Comment comment, Record record);

    CommentHateoas createNewComment
        (Comment comment, DocumentDescription documentDescription);

    CommentHateoas findSingleComment(String commentSystemId);

    CommentHateoas handleUpdate(@NotNull String systemId,
                                @NotNull Long version,
                                @NotNull Comment incomingComment);

    void deleteEntity(@NotNull String systemId);
}
