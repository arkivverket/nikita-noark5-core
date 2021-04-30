package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.hateoas.DocumentDescriptionHateoas;
import nikita.common.model.noark5.v5.hateoas.DocumentObjectHateoas;
import nikita.common.model.noark5.v5.hateoas.RecordHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.*;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.model.noark5.v5.secondary.Author;
import nikita.common.model.noark5.v5.secondary.Comment;
import nikita.common.model.noark5.v5.secondary.PartPerson;
import nikita.common.model.noark5.v5.secondary.PartUnit;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IDocumentDescriptionService {

    // -- All CREATE operations
    DocumentDescription save(DocumentDescription documentDescription);

    DocumentObjectHateoas
    createDocumentObjectAssociatedWithDocumentDescription(
            String documentDescriptionSystemId,
            DocumentObject documentObject);

    CommentHateoas createCommentAssociatedWithDocumentDescription
	(String systemID, Comment comment);

    PartPersonHateoas createPartPersonAssociatedWithDocumentDescription(
            String systemID, PartPerson partPerson);

    PartUnitHateoas createPartUnitAssociatedWithDocumentDescription(
            String systemID, PartUnit partUnit);

    // -- All READ operations

    AuthorHateoas associateAuthorWithDocumentDescription(
            String systemId, Author author);

    DocumentDescriptionHateoas generateDefaultDocumentDescription();

    CommentHateoas generateDefaultComment();

    PartPersonHateoas generateDefaultPartPerson(String systemID);

    PartUnitHateoas generateDefaultPartUnit(String systemID);

    DocumentDescriptionHateoas findAll();

    DocumentDescriptionHateoas findBySystemId(@NotNull String systemId);

    RecordHateoas
    findAllRecordWithDocumentDescriptionBySystemId(@NotNull String systemId);

    DocumentObjectHateoas
    findAllDocumentObjectWithDocumentDescriptionBySystemId(
            @NotNull String systemId);

    DocumentDescription findDocumentDescriptionBySystemId(
            @NotNull String systemId);

    CommentHateoas getCommentAssociatedWithDocumentDescription
	(@NotNull final String systemID);

    PartHateoas getPartAssociatedWithDocumentDescription(
            @NotNull final String systemID);
    // -- All UPDATE operations

    DocumentDescriptionHateoas handleUpdate
        (@NotNull final String systemId, @NotNull final Long version,
         @NotNull final DocumentDescription documentDescription);

    // -- All DELETE operations
    void deleteEntity(@NotNull String systemId);

    long deleteAllByOwnedBy();

    AuthorHateoas
    findAllAuthorWithDocumentDescriptionBySystemId(String systemID);

    AuthorHateoas generateDefaultAuthor(String systemID);

    ScreeningMetadataHateoas getDefaultScreeningMetadata(
            @NotNull final UUID systemID);

    ScreeningMetadataHateoas
    createScreeningMetadataAssociatedWithDocumentDescription(
            @NotNull final UUID systemId,
            @NotNull final Metadata screeningMetadata);

    ScreeningMetadataHateoas
    getScreeningMetadataAssociatedWithDocumentDescription(
            @NotNull final UUID systemId);
}
