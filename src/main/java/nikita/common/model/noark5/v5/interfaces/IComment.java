package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.Comment;

import java.util.List;

public interface IComment {
    List<Comment> getReferenceComment();
    void setReferenceComment(List<Comment> comments);
    void addComment(Comment comment);
    void removeComment(Comment comment);
}
