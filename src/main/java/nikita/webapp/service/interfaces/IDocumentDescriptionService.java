package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v4.DocumentDescription;
import nikita.common.model.noark5.v4.DocumentObject;
import nikita.common.model.noark5.v4.hateoas.DocumentDescriptionHateoas;
import nikita.common.model.noark5.v4.hateoas.DocumentObjectHateoas;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;

public interface IDocumentDescriptionService {

    // -- All CREATE operations
    DocumentDescription save(DocumentDescription documentDescription);

    DocumentObject
    createDocumentObjectAssociatedWithDocumentDescription(
            String documentDescriptionSystemId,
            DocumentObject documentObject);

    // -- All READ operations
    ResponseEntity<DocumentDescriptionHateoas> findAll();

    ResponseEntity<DocumentDescriptionHateoas> findBySystemId(
            @NotNull String systemId);

    ResponseEntity<DocumentObjectHateoas>
    findAllDocumentObjectWithDocumentDescriptionBySystemId(
            @NotNull String systemId);

    // -- All UPDATE operations
    DocumentDescription handleUpdate(@NotNull final String systemId,
                                     @NotNull final Long version,
                                     @NotNull final DocumentDescription
                                             documentDescription);
    // -- All DELETE operations
    int deleteEntity(@NotNull String systemId);

    long deleteAllByOwnedBy();
}
