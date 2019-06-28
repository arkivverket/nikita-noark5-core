package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.Author;

import java.util.List;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IAuthor {
    List<Author> getReferenceAuthor();

    void setReferenceAuthor(List<Author> authors);

    void addReferenceAuthor(Author author);
}
