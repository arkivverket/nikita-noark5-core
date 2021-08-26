package nikita.webapp.service.impl.secondary;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.RecordEntity;
import nikita.common.model.noark5.v5.hateoas.secondary.KeywordHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.KeywordTemplateHateoas;
import nikita.common.model.noark5.v5.secondary.Keyword;
import nikita.common.repository.n5v5.secondary.IKeywordRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.secondary.IKeywordHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.odata.IODataService;
import nikita.webapp.service.interfaces.secondary.IKeywordService;
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
            IODataService odataService,
            IPatchService patchService,
            IKeywordRepository keywordRepository,
            IKeywordHateoasHandler keywordHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.keywordRepository = keywordRepository;
        this.keywordHateoasHandler = keywordHateoasHandler;
    }

    // All CREATE methods

    @Override
    @Transactional
    public KeywordHateoas createKeywordAssociatedWithFile
            (Keyword keyword, File file) {
        keyword.addReferenceFile(file);
        return packAsHateoas(keywordRepository.save(keyword));
    }

    @Override
    @Transactional
    public KeywordHateoas createKeywordAssociatedWithClass
            (Keyword keyword, Class klass) {
        keyword.addReferenceClass(klass);
        return packAsHateoas(keywordRepository.save(keyword));
    }

    @Override
    @Transactional
    public KeywordHateoas createKeywordAssociatedWithRecord
            (Keyword keyword, RecordEntity record) {
        keyword.addReferenceRecord(record);
        return packAsHateoas(keywordRepository.save(keyword));
    }

    // All READ methods

    @Override
    public KeywordHateoas findBySystemId(@NotNull final UUID systemId) {
        return packAsHateoas(getKeywordOrThrow(systemId));
    }

    @Override
    public KeywordHateoas findAll() {
        return (KeywordHateoas) odataService.processODataQueryGet();
    }

    // All UPDATE methods

    @Override
    @Transactional
    public KeywordHateoas updateKeywordBySystemId(@NotNull final UUID systemId, Long version,
                                                  Keyword incomingKeyword) {
        Keyword existingKeyword = getKeywordOrThrow(systemId);
        existingKeyword.setKeyword(incomingKeyword.getKeyword());
        existingKeyword.setVersion(version);
        return packAsHateoas(existingKeyword);
    }

    // All DELETE methods

    @Override
    @Transactional
    public void deleteKeywordBySystemId(@NotNull final UUID systemId) {
        Keyword keyword = getKeywordOrThrow(systemId);
        // Remove any associations between a Class and the given Keyword
        for (Class klass : keyword.getReferenceClass()) {
            klass.removeKeyword(keyword);
        }
        // Remove any associations between a Record and the given Keyword
        for (RecordEntity record : keyword.getReferenceRecord()) {
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

    @Override
    public KeywordTemplateHateoas generateDefaultKeyword(@NotNull final UUID systemId) {
        Keyword suggestedKeyword = new Keyword();
        suggestedKeyword.setVersion(-1L, true);
        return packAsKeywordTemplateHateoas(suggestedKeyword);
    }

    // All helper methods

    public KeywordHateoas packAsHateoas(@NotNull final Keyword keyword) {
        KeywordHateoas keywordHateoas = new KeywordHateoas(keyword);
        applyLinksAndHeader(keywordHateoas, keywordHateoasHandler);
        return keywordHateoas;
    }

    public KeywordTemplateHateoas packAsKeywordTemplateHateoas(@NotNull final Keyword keyword) {
        KeywordTemplateHateoas keywordHateoas =
                new KeywordTemplateHateoas(keyword);
        applyLinksAndHeader(keywordHateoas, keywordHateoasHandler);
        return keywordHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. Note. If you call this, you
     * will only ever get a valid Keyword back. If there is no valid
     * Keyword, an exception is thrown
     *
     * @param keywordSystemId systemId of the Keyword object to retrieve
     * @return the Keyword object
     */
    protected Keyword getKeywordOrThrow(
            @NotNull final UUID keywordSystemId) {
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
