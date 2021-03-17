package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.Comment;

import java.util.Set;

public interface IComment {
    Set<Comment> getReferenceComment();

    void setReferenceComment(Set<Comment> comments);

    void addComment(Comment comment);

    void removeComment(Comment comment);
}
