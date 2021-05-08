package nikita.webapp.service.impl.secondary;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.secondary.KeywordHateoas;
import nikita.common.model.noark5.v5.secondary.Keyword;
import nikita.common.repository.n5v5.secondary.IKeywordRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.secondary.IKeywordHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.secondary.IKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;

@Service
public class KeywordService
        extends NoarkService
        implements IKeywordService {

    private static final Logger logger =
            LoggerFactory.getLogger(KeywordService.class);

    private final IKeywordRepository keywordRepository;
    private final IKeywordHateoasHandler keywordHateoasHandler;

    public KeywordService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IPatchService patchService,
            IKeywordRepository keywordRepository,
            IKeywordHateoasHandler keywordHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService);
        this.keywordRepository = keywordRepository;
        this.keywordHateoasHandler = keywordHateoasHandler;
    }

    // All CREATE methods

    @Override
    @Transactional
    public KeywordHateoas createKeywordAssociatedWithFile
            (Keyword keyword, File file) {
        keyword.addReferenceFile(file);
        keyword = keywordRepository.save(keyword);
        KeywordHateoas keywordHateoas = new KeywordHateoas(keyword);
        keywordHateoasHandler.addLinks(keywordHateoas, new Authorisation());
        setOutgoingRequestHeader(keywordHateoas);
        return keywordHateoas;
    }

    @Override
    @Transactional
    public KeywordHateoas createKeywordAssociatedWithClass
            (Keyword keyword, Class klass) {
        keyword.addReferenceClass(klass);
        keyword = keywordRepository.save(keyword);
        KeywordHateoas keywordHateoas = new KeywordHateoas(keyword);
        keywordHateoasHandler.addLinks(keywordHateoas, new Authorisation());
        setOutgoingRequestHeader(keywordHateoas);
        return keywordHateoas;
    }

    @Override
    @Transactional
    public KeywordHateoas createKeywordAssociatedWithRecord
            (Keyword keyword, Record record) {
        keyword.addReferenceRecord(record);
        keyword = keywordRepository.save(keyword);
        KeywordHateoas keywordHateoas = new KeywordHateoas(keyword);
        keywordHateoasHandler.addLinks(keywordHateoas, new Authorisation());
        setOutgoingRequestHeader(keywordHateoas);
        return keywordHateoas;
    }

    // All READ methods

    @Override
    public KeywordHateoas findBySystemId(UUID systemId) {
        KeywordHateoas keywordHateoas =
                new KeywordHateoas(getKeywordOrThrow(systemId));
        keywordHateoasHandler.addLinks(keywordHateoas, new Authorisation());
        setOutgoingRequestHeader(keywordHateoas);
        return keywordHateoas;
    }

    @Override
    public KeywordHateoas findAllByOwner() {
        KeywordHateoas keywordHateoas =
                new KeywordHateoas(List.copyOf(
                        keywordRepository.findByOwnedBy(getUser())));
        keywordHateoasHandler.addLinks(keywordHateoas,
                new Authorisation());
        setOutgoingRequestHeader(keywordHateoas);
        return keywordHateoas;
    }

    // All UPDATE methods

    @Override
    @Transactional
    public KeywordHateoas updateKeywordBySystemId(UUID systemId, Long version,
                                                  Keyword incomingKeyword) {
        Keyword existingKeyword = getKeywordOrThrow(systemId);
        existingKeyword.setKeyword(incomingKeyword.getKeyword());
        existingKeyword.setVersion(version);
        KeywordHateoas keywordHateoas =
                new KeywordHateoas(keywordRepository.save(existingKeyword));
        keywordHateoasHandler.addLinks(keywordHateoas, new Authorisation());
        setOutgoingRequestHeader(keywordHateoas);
        return keywordHateoas;
    }

    // All DELETE methods

    @Override
    @Transactional
    public void deleteKeywordBySystemId(UUID systemId) {
        Keyword keyword = getKeywordOrThrow(systemId);
        // Remove any associations between a Class and the given Keyword
        for (Class klass : keyword.getReferenceClass()) {
            klass.removeKeyword(keyword);
        }
        // Remove any associations between a Record and the given Keyword
        for (Record record : keyword.getReferenceRecord()) {
            record.removeKeyword(keyword);
        }
        // Remove any associations between a File and the given Keyword
        for (File file : keyword.getReferenceFile()) {
            file.removeKeyword(keyword);
        }
        keywordRepository.delete(keyword);
    }

    public boolean deleteKeywordIfNotEmpty(Keyword keyword) {
        if (keyword.getReferenceClass().size() > 0) {
            return false;
        }
        if (keyword.getReferenceFile().size() > 0) {
            return false;
        }
        if (keyword.getReferenceRecord().size() > 0) {
            return false;
        }
        keywordRepository.delete(keyword);
        return true;
    }

    // All template methods

    public KeywordHateoas generateDefaultKeyword() {
        Keyword suggestedKeyword = new Keyword();
        KeywordHateoas partHateoas = new KeywordHateoas(suggestedKeyword);
        keywordHateoasHandler.addLinksOnTemplate(partHateoas, new Authorisation());
        return partHateoas;
    }

    // All helper methods

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. Note. If you call this, you
     * will only ever get a valid Keyword back. If there is no valid
     * Keyword, an exception is thrown
     *
     * @param keywordSystemId systemID of the Keyword object to retrieve
     * @return the Keyword object
     */
    protected Keyword getKeywordOrThrow(
            @NotNull UUID keywordSystemId) {
        Keyword keyword = keywordRepository.
                findBySystemId(keywordSystemId);
        if (keyword == null) {
            String error = INFO_CANNOT_FIND_OBJECT +
                    " Keyword, using systemId " + keywordSystemId;
            logger.error(error);
            throw new NoarkEntityNotFoundException(error);
        }
        return keyword;
    }
}
