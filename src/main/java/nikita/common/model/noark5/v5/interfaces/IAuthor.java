package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.Author;

import java.util.List;

public interface IAuthor {
    List<Author> getReferenceAuthor();
    void addAuthor(Author author);
    void removeAuthor(Author author);
}
