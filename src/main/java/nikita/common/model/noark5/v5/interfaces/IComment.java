package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.Comment;

import java.util.List;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IComment {
    List<Comment> getReferenceComment();

    void setReferenceComment(List<Comment> comments);

    void addReferenceComment(Comment comment);
}
