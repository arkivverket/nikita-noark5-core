package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.CommentType;

import javax.validation.constraints.NotNull;

/**
 * Created by tsodring on 04/03/18.
 */

public interface ICommentTypeService {

    MetadataHateoas createNewCommentType(CommentType commentType);

    MetadataHateoas find(String systemId);

    MetadataHateoas findAll();

    MetadataHateoas findByDescription(String description);

    MetadataHateoas findByCode(String code);

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final CommentType incomingCommentType);

    CommentType generateDefaultCommentType();
}
