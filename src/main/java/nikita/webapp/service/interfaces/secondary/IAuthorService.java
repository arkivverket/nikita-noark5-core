package nikita.webapp.service.interfaces.secondary;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.secondary.AuthorHateoas;
import nikita.common.model.noark5.v5.secondary.Author;

public interface IAuthorService {

    AuthorHateoas associateAuthorWithDocumentDescription
        (Author author, DocumentDescription documentDescription);

    AuthorHateoas associateAuthorWithRecord(Author author, Record record);

    AuthorHateoas updateAuthorBySystemId(String systemId, Long version,
                        Author incomingAuthor);

    void deleteAuthorBySystemId(String systemID);

    AuthorHateoas findBySystemId(String precedenceSystemId);

    AuthorHateoas generateDefaultAuthor();
}
