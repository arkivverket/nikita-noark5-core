package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.metadata.DocumentStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by tsodring on 31/1/18.
 */
public interface IDocumentStatusService
    extends IMetadataSuperService {

    DocumentStatus createNewDocumentStatus(DocumentStatus documentStatus);

    List<IMetadataEntity> findAll();

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final DocumentStatus incomingDocumentStatus);
}
