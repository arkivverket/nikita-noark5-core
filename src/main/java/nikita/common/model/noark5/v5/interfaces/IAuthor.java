package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.Author;

import java.util.Set;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IAuthor {
    Set<Author> getReferenceAuthor();

    void setReferenceAuthor(Set<Author> authors);

    void addReferenceAuthor(Author author);
}
