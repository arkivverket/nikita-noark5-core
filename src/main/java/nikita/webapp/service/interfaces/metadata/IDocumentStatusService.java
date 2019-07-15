package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.metadata.DocumentStatus;

/**
 * Created by tsodring on 31/1/18.
 */
public interface IDocumentStatusService {

    DocumentStatus createNewDocumentStatus(DocumentStatus documentStatus);

    Iterable<DocumentStatus> findAll();

    DocumentStatus update(DocumentStatus documentStatus);

    DocumentStatus findByCode(String code);
}
