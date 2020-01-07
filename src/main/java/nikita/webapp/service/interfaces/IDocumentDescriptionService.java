package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.PartPerson;
import nikita.common.model.noark5.v5.PartUnit;
import nikita.common.model.noark5.v5.hateoas.*;
import nikita.common.model.noark5.v5.hateoas.secondary.AuthorHateoas;
import nikita.common.model.noark5.v5.secondary.Author;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;

public interface IDocumentDescriptionService {

    // -- All CREATE operations
    DocumentDescription save(DocumentDescription documentDescription);

    DocumentObject
    createDocumentObjectAssociatedWithDocumentDescription(
            String documentDescriptionSystemId,
            DocumentObject documentObject);

    PartPersonHateoas createPartPersonAssociatedWithDocumentDescription(
            String systemID, PartPerson partPerson);

    PartUnitHateoas createPartUnitAssociatedWithDocumentDescription(
            String systemID, PartUnit partUnit);

    // -- All READ operations

    AuthorHateoas associateAuthorWithDocumentDescription(
            String systemId, Author author);

    DocumentDescriptionHateoas generateDefaultDocumentDescription();

    PartPersonHateoas generateDefaultPartPerson(String systemID);

    PartUnitHateoas generateDefaultPartUnit(String systemID);

    ResponseEntity<DocumentDescriptionHateoas> findAll();

    ResponseEntity<DocumentDescriptionHateoas> findBySystemId(
            @NotNull String systemId);

    ResponseEntity<RecordHateoas>
    findAllRecordWithDocumentDescriptionBySystemId(@NotNull String systemId);

    ResponseEntity<DocumentObjectHateoas>
    findAllDocumentObjectWithDocumentDescriptionBySystemId(
            @NotNull String systemId);

    DocumentDescription findDocumentDescriptionBySystemId(
            @NotNull String systemId);

    PartHateoas getPartAssociatedWithDocumentDescription(
            @NotNull final String systemID);
    // -- All UPDATE operations

    DocumentDescription handleUpdate(@NotNull final String systemId,
                                     @NotNull final Long version,
                                     @NotNull final DocumentDescription
                                             documentDescription);

    // -- All DELETE operations
    void deleteEntity(@NotNull String systemId);

    long deleteAllByOwnedBy();

    AuthorHateoas
    findAllAuthorWithDocumentDescriptionBySystemId(String systemID);

    AuthorHateoas generateDefaultAuthor(String systemID);
}
