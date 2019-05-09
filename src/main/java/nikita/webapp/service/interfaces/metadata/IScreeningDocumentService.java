package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.ScreeningDocument;

/**
 * Created by tsodring on 16/03/18.
 */

public interface IScreeningDocumentService {

    MetadataHateoas createNewScreeningDocument(
            ScreeningDocument screeningDocument, String outgoingAddress);

    MetadataHateoas find(String systemId, String outgoingAddress);

    MetadataHateoas findAll(String outgoingAddress);

    MetadataHateoas findByDescription(String description, String outgoingAddress);

    MetadataHateoas findByCode(String code, String outgoingAddress);

    MetadataHateoas handleUpdate(String systemId, Long version,
                                 ScreeningDocument screeningDocument,
                                 String outgoingAddress);

    ScreeningDocument generateDefaultScreeningDocument();
}
