package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.ScreeningDocument;

import javax.validation.constraints.NotNull;

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

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final ScreeningDocument incomingScreeningDocument);

    ScreeningDocument generateDefaultScreeningDocument();
}
