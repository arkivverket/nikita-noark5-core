package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.metadata.DocumentMedium;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by tsodring on 3/9/17.
 */
public interface IDocumentMediumService
    extends IMetadataSuperService {

    DocumentMedium createNewDocumentMedium(DocumentMedium documentMedium);

    List<IMetadataEntity> findAll();

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final DocumentMedium incomingDocumentMedium);

}
