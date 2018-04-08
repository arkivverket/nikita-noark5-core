package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.ScreeningDocument;

/**
 * Created by tsodring on 16/03/18.
 */

public interface IScreeningDocumentService {

    MetadataHateoas createNewScreeningDocument(
            ScreeningDocument screeningDocument);

    MetadataHateoas find(String systemId);

    MetadataHateoas findAll();

    MetadataHateoas findByDescription(String description);

    MetadataHateoas findByCode(String code);

    MetadataHateoas handleUpdate(String systemId, Long version,
                                 ScreeningDocument screeningDocument);

    ScreeningDocument generateDefaultScreeningDocument();
}
