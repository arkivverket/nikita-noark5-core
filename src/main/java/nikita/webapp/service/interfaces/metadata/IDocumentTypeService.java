package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.DocumentType;

import javax.validation.constraints.NotNull;

public interface IDocumentTypeService {

    MetadataHateoas createNewDocumentType(DocumentType documentType);

    MetadataHateoas findAll();

    MetadataHateoas findByCode(String code);

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final DocumentType incomingDocumentType);

    DocumentType generateDefaultDocumentType();
}
