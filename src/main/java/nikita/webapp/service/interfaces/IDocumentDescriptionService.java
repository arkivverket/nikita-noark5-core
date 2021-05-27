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
    DocumentDescriptionHateoas save(
            @NotNull final DocumentDescription documentDescription);

    DocumentObjectHateoas
    createDocumentObjectAssociatedWithDocumentDescription(
            @NotNull final UUID systemId,
            @NotNull final DocumentObject documentObject);

    CommentHateoas createCommentAssociatedWithDocumentDescription(
            @NotNull final UUID systemId,
            @NotNull final Comment comment);

    PartPersonHateoas createPartPersonAssociatedWithDocumentDescription(
            @NotNull final UUID systemId,
            @NotNull final PartPerson partPerson);

    PartUnitHateoas createPartUnitAssociatedWithDocumentDescription(
            @NotNull final UUID systemId,
            @NotNull final PartUnit partUnit);

    // -- All READ operations

    AuthorHateoas associateAuthorWithDocumentDescription(
            @NotNull final UUID systemId,
            @NotNull final Author author);

    DocumentDescriptionHateoas generateDefaultDocumentDescription(
            @NotNull final UUID systemId);

    CommentHateoas generateDefaultComment(@NotNull final UUID systemId);

    PartPersonHateoas generateDefaultPartPerson(@NotNull final UUID systemId);

    PartUnitHateoas generateDefaultPartUnit(@NotNull final UUID systemId);

    DocumentDescriptionHateoas findAll();

    DocumentDescriptionHateoas findBySystemId(@NotNull final UUID systemId);

    RecordHateoas
    findAllRecordWithDocumentDescriptionBySystemId(
            @NotNull final UUID systemId);

    DocumentObjectHateoas
    findAllDocumentObjectWithDocumentDescriptionBySystemId(
            @NotNull final UUID systemId);

    CommentHateoas getCommentAssociatedWithDocumentDescription
            (@NotNull final UUID systemId);

    PartHateoas getPartAssociatedWithDocumentDescription(
            @NotNull final UUID systemId);
    // -- All UPDATE operations

    DocumentDescriptionHateoas handleUpdate
            (@NotNull final UUID systemId, @NotNull final Long version,
             @NotNull final DocumentDescription documentDescription);

    // -- All DELETE operations
    void deleteEntity(@NotNull final UUID systemId);

    void deleteAllByOwnedBy();

    AuthorHateoas
    findAllAuthorWithDocumentDescriptionBySystemId(
            @NotNull final UUID systemId);

    AuthorHateoas generateDefaultAuthor(@NotNull final UUID systemId);

    ScreeningMetadataHateoas getDefaultScreeningMetadata(
            @NotNull final UUID systemId);

    ScreeningMetadataHateoas
    createScreeningMetadataAssociatedWithDocumentDescription(
            @NotNull final UUID systemId,
            @NotNull final Metadata screeningMetadata);

    ScreeningMetadataHateoas
    getScreeningMetadataAssociatedWithDocumentDescription(
            @NotNull final UUID systemId);
}
