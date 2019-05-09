package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.CommentType;

/**
 * Created by tsodring on 04/03/18.
 */

public interface ICommentTypeService {

    MetadataHateoas createNewCommentType(CommentType commentType,
                                         String outgoingAddress);

    MetadataHateoas find(String systemId, String outgoingAddress);

    MetadataHateoas findAll(String outgoingAddress);

    MetadataHateoas findByDescription(String description,
                                      String outgoingAddress);

    MetadataHateoas findByCode(String code, String outgoingAddress);

    MetadataHateoas handleUpdate(String systemId, Long version,
                                 CommentType commentType,
                                 String outgoingAddress);

    CommentType generateDefaultCommentType();
}
