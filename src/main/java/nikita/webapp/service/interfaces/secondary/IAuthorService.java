package nikita.webapp.service.interfaces.secondary;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.secondary.AuthorHateoas;
import nikita.common.model.noark5.v5.secondary.Author;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IAuthorService {

    AuthorHateoas associateAuthorWithDocumentDescription(
            @NotNull final Author author,
            @NotNull final DocumentDescription documentDescription);

    AuthorHateoas associateAuthorWithRecord(
            @NotNull final Author author,
            @NotNull final Record record);

    AuthorHateoas updateAuthorBySystemId(
            @NotNull final UUID systemId,
            @NotNull final Long version,
            @NotNull final Author incomingAuthor);

    void deleteAuthorBySystemId(@NotNull final UUID systemId);

    AuthorHateoas findBySystemId(@NotNull final UUID systemId);

    AuthorHateoas generateDefaultAuthor(@NotNull final UUID systemId);
}
