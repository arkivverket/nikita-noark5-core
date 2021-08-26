package nikita.webapp.service.interfaces.secondary;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.RecordEntity;
import nikita.common.model.noark5.v5.hateoas.secondary.CommentHateoas;
import nikita.common.model.noark5.v5.secondary.Comment;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface ICommentService {

    CommentHateoas generateDefaultComment(@NotNull final UUID systemId);

    CommentHateoas createNewComment(
            @NotNull final Comment comment,
            @NotNull final File file);

    CommentHateoas createNewComment(
            @NotNull final Comment comment,
            @NotNull final RecordEntity record);

    CommentHateoas createNewComment(
            @NotNull final Comment comment,
            @NotNull final DocumentDescription documentDescription);

    CommentHateoas findSingleComment(@NotNull final UUID commentSystemId);

    CommentHateoas handleUpdate(@NotNull final UUID systemId,
                                @NotNull final Long version,
                                @NotNull final Comment incomingComment);

    void deleteComment(@NotNull final UUID systemId);
}
