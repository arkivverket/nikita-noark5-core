package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.metadata.DocumentMedium;
import nikita.common.repository.n5v5.metadata.IDocumentMediumRepository;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IDocumentMediumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


@Service
@Transactional
public class DocumentMediumService
        extends NoarkService
        implements IDocumentMediumService {

    private static final Logger logger =
            LoggerFactory.getLogger(DocumentMediumService.class);

    private IDocumentMediumRepository documentMediumRepository;

    public DocumentMediumService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IDocumentMediumRepository documentMediumRepository) {
        super(entityManager, applicationEventPublisher);
        this.documentMediumRepository = documentMediumRepository;
    }

    // All CREATE operations
    /**
     * Persists a new documentMedium object to the database.
     *
     * @param documentMedium documentMedium object with values set
     * @return the newly persisted documentMedium object
     */
    @Override
    public DocumentMedium createNewDocumentMedium(DocumentMedium documentMedium) {
        return documentMediumRepository.save(documentMedium);
    }

    // All READ operations

    /**
     * retrieve all documentMedium
     *
     * @return
     */
    @Override
    public List<IMetadataEntity> findAll() {
        return (List<IMetadataEntity>) (List)
                documentMediumRepository.findAll();
    }

    /**
     * retrieve all documentMedium that have a particular code. <br>
     * This will be replaced by OData search.

     * @param code
     * @return
     */
    @Override
    public DocumentMedium findByCode(String code) {
        return documentMediumRepository.findByCode(code);
    }

    /**
     * update a given documentMedium. <br>
     *
     * @param documentMedium
     * @return the updated documentMedium
     */
    @Override
    public DocumentMedium update(DocumentMedium documentMedium) {
        return documentMediumRepository.save(documentMedium);
    }
}
