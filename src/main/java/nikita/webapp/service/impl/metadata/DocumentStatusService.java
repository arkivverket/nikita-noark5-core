package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.metadata.DocumentStatus;
import nikita.common.repository.n5v5.metadata.IDocumentStatusRepository;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IDocumentStatusService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by tsodring on 31/1/18.
 */


@Service
@Transactional
public class DocumentStatusService
        extends MetadataSuperService
        implements IDocumentStatusService {

    private IDocumentStatusRepository documentStatusRepository;

    public DocumentStatusService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IDocumentStatusRepository documentStatusRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher, metadataHateoasHandler);
        this.documentStatusRepository = documentStatusRepository;
    }

    // All CREATE operations

    /**
     * Persists a new documentStatus object to the database.
     *
     * @param documentStatus documentStatus object with values set
     * @return the newly persisted documentStatus object
     */
    @Override
    public DocumentStatus createNewDocumentStatus(
            DocumentStatus documentStatus) {
        return documentStatusRepository.save(documentStatus);
    }

    // All READ operations

    /**
     * retrieve all documentStatus
     *
     * @return
     */
    @Override
    public List<IMetadataEntity> findAll() {
        return (List<IMetadataEntity>) (List)
                documentStatusRepository.findAll();
    }

    @Override
    public DocumentStatus findMetadataByCode(String code) {
        return documentStatusRepository.findByCode(code);
    }

    /**
     * update a particular documentStatus. <br>
     *
     * @param documentStatus
     * @return the updated documentStatus
     */
    @Override
    public DocumentStatus update(DocumentStatus documentStatus) {
        return documentStatusRepository.save(documentStatus);
    }
}
