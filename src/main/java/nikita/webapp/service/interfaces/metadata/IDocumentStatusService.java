package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.metadata.DocumentStatus;

import java.util.List;

/**
 * Created by tsodring on 31/1/18.
 */
public interface IDocumentStatusService
    extends IMetadataSuperService {

    DocumentStatus createNewDocumentStatus(DocumentStatus documentStatus);

    List<IMetadataEntity> findAll();

    DocumentStatus update(DocumentStatus documentStatus);
}
