package nikita.webapp.service.impl.secondary;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.secondary.AuthorHateoas;
import nikita.common.model.noark5.v5.secondary.Author;
import nikita.common.repository.n5v5.secondary.IAuthorRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.secondary.IAuthorHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.secondary.IAuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;

import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;

@Service
@Transactional
public class AuthorService
    extends NoarkService
    implements IAuthorService {

    private static final Logger logger =
            LoggerFactory.getLogger(AuthorService.class);
    private IAuthorRepository authorRepository;
    private IAuthorHateoasHandler authorHateoasHandler;
    public AuthorService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IAuthorRepository authorRepository,
            IAuthorHateoasHandler authorHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.authorRepository = authorRepository;
        this.authorHateoasHandler = authorHateoasHandler;
    }

    @Override
    public AuthorHateoas associateAuthorWithDocumentDescription
        (Author author, DocumentDescription documentDescription) {
        author.setReferenceDocumentDescription(documentDescription);
        author = authorRepository.save(author);
        documentDescription.addReferenceAuthor(author);
        AuthorHateoas authorHateoas = new AuthorHateoas(author);
        authorHateoasHandler.addLinks(authorHateoas, new Authorisation());
        setOutgoingRequestHeader(authorHateoas);
        return authorHateoas;
    }

    @Override
    public AuthorHateoas associateAuthorWithRecord
        (Author author, Record record) {
        author.setReferenceRecord(record);
        author = authorRepository.save(author);
        record.addReferenceAuthor(author);
        AuthorHateoas authorHateoas = new AuthorHateoas(author);
        authorHateoasHandler.addLinks(authorHateoas, new Authorisation());
        setOutgoingRequestHeader(authorHateoas);
        return authorHateoas;
    }
    @Override
    public AuthorHateoas updateAuthorBySystemId(String systemId, Long version,
                                                Author incomingAuthor) {
        Author existingAuthor = getAuthorOrThrow(systemId);
        existingAuthor.setAuthor(incomingAuthor.getAuthor());
        existingAuthor.setVersion(version);
        AuthorHateoas authorHateoas =
            new AuthorHateoas(authorRepository.save(existingAuthor));
        authorHateoasHandler.addLinks(authorHateoas, new Authorisation());
        setOutgoingRequestHeader(authorHateoas);
        return authorHateoas;
    }

    @Override
    public void deleteAuthorBySystemId(String systemID) {
        deleteEntity(getAuthorOrThrow(systemID));
    }

    @Override
    public AuthorHateoas findBySystemId(String authorSystemId) {
        AuthorHateoas authorHateoas =
            new AuthorHateoas(getAuthorOrThrow(authorSystemId));
        authorHateoasHandler.addLinks(authorHateoas, new Authorisation());
        setOutgoingRequestHeader(authorHateoas);
        return authorHateoas;
    }

    public AuthorHateoas generateDefaultAuthor() {
        Author suggestedPart = new Author();
        AuthorHateoas partHateoas = new AuthorHateoas(suggestedPart);
        authorHateoasHandler.addLinksOnTemplate(partHateoas, new Authorisation());
        return partHateoas;
    }
    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. Note. If you call this, you
     * will only ever get a valid Author back. If there is no valid
     * Author, an exception is thrown
     *
     * @param authorSystemId systemID of the Author object to retrieve
     * @return the Author object
     */
    protected Author getAuthorOrThrow(
            @NotNull String authorSystemId) {
        Author author = authorRepository.
                findBySystemId(UUID.fromString(authorSystemId));
        if (author == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " Author, using systemId " + authorSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return author;
    }
}
