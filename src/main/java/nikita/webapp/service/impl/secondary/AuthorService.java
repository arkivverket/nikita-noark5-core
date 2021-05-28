package nikita.webapp.service.impl.secondary;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.secondary.AuthorHateoas;
import nikita.common.model.noark5.v5.secondary.Author;
import nikita.common.repository.n5v5.secondary.IAuthorRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.secondary.IAuthorHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.odata.IODataService;
import nikita.webapp.service.interfaces.secondary.IAuthorService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;

@Service
public class AuthorService
        extends NoarkService
        implements IAuthorService {

    private final IAuthorRepository authorRepository;
    private final IAuthorHateoasHandler authorHateoasHandler;

    public AuthorService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IODataService odataService,
            IPatchService patchService,
            IAuthorRepository authorRepository,
            IAuthorHateoasHandler authorHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.authorRepository = authorRepository;
        this.authorHateoasHandler = authorHateoasHandler;
    }

    // All CREATE methods

    @Override
    @Transactional
    public AuthorHateoas associateAuthorWithDocumentDescription(
            @NotNull final Author author,
            @NotNull final DocumentDescription documentDescription) {
        author.setReferenceDocumentDescription(documentDescription);
        return packAsHateoas(authorRepository.save(author));
    }

    @Override
    @Transactional
    public AuthorHateoas associateAuthorWithRecord(
            @NotNull final Author author,
            @NotNull final Record record) {
        author.setReferenceRecord(record);
        return packAsHateoas(authorRepository.save(author));
    }

    // All READ methods

    @Override
    public AuthorHateoas findBySystemId(@NotNull final UUID systemId) {
        return packAsHateoas(getAuthorOrThrow(systemId));
    }

    // All UPDATE methods

    @Override
    @Transactional
    public AuthorHateoas updateAuthorBySystemId(
            @NotNull final UUID systemId,
            @NotNull final Long version,
            @NotNull final Author incomingAuthor) {
        Author existingAuthor = getAuthorOrThrow(systemId);
        existingAuthor.setAuthor(incomingAuthor.getAuthor());
        existingAuthor.setVersion(version);
        return packAsHateoas(existingAuthor);
    }

    // All DELETE methods

    @Override
    @Transactional
    public void deleteAuthorBySystemId(@NotNull final UUID systemId) {
        deleteEntity(getAuthorOrThrow(systemId));
    }

    // All template methods

    public AuthorHateoas generateDefaultAuthor(@NotNull final UUID systemId) {
        Author author = new Author();
        author.setVersion(-1L, true);
        return packAsHateoas(author);
    }

    // All helper methods

    public AuthorHateoas packAsHateoas(@NotNull final Author author) {
        AuthorHateoas authorHateoas = new AuthorHateoas(author);
        applyLinksAndHeader(authorHateoas, authorHateoasHandler);
        return authorHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. Note. If you call this, you
     * will only ever get a valid Author back. If there is no valid
     * Author, an exception is thrown
     *
     * @param systemId systemId of the Author object to retrieve
     * @return the Author object
     */
    protected Author getAuthorOrThrow(@NotNull final UUID systemId) {
        Author author = authorRepository.findBySystemId(systemId);
        if (author == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " Author, using systemId " + systemId;
            throw new NoarkEntityNotFoundException(info);
        }
        return author;
    }
}
