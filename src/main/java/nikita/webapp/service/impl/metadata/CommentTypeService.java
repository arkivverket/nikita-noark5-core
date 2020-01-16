package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.CommentType;
import nikita.common.repository.n5v5.metadata.ICommentTypeRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.metadata.IMetadataHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.ICommentTypeService;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.COMMENT_TYPE;

/**
 * Created by tsodring on 04/03/18.
 */

@Service
@Transactional
@SuppressWarnings("unchecked")
public class CommentTypeService
        extends MetadataSuperService
        implements ICommentTypeService {

    private static final Logger logger =
            LoggerFactory.getLogger(CommentTypeService.class);

    private ICommentTypeRepository commentTypeRepository;

    public CommentTypeService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            ICommentTypeRepository commentTypeRepository,
            IMetadataHateoasHandler metadataHateoasHandler) {
        super(entityManager, applicationEventPublisher, metadataHateoasHandler);
        this.commentTypeRepository = commentTypeRepository;
    }

    // All CREATE operations

    /**
     * Persists a new CommentType object to the database.
     *
     * @param commentType CommentType object with values set
     * @return the newly persisted CommentType object wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas createNewCommentType(
            CommentType commentType) {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                commentTypeRepository.save(commentType));
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    // All READ operations

    /**
     * Retrieve a list of all CommentType objects
     *
     * @return list of CommentType objects wrapped as a
     * MetadataHateoas object
     */
    @Override
    public MetadataHateoas findAll() {
        MetadataHateoas metadataHateoas = new MetadataHateoas(
                (List<IMetadataEntity>) (List)
                        commentTypeRepository.findAll(), COMMENT_TYPE);
        metadataHateoasHandler.addLinks(metadataHateoas, new Authorisation());
        return metadataHateoas;
    }

    /**
     * retrieve all CommentType that have a particular code.

     *
     * @param code The code of the object you wish to retrieve
     * @return A list of CommentType objects wrapped as a MetadataHateoas
     * object
     */
    @Override
    public CommentType findMetadataByCode(String code) {
        return commentTypeRepository.findByCode(code);
    }

    /**
     * Generate a default CommentType object
     *
     * @return the CommentType object wrapped as a CommentTypeHateoas object
     */
    @Override
    public CommentType generateDefaultCommentType() {

        CommentType commentType = new CommentType();
        commentType.setCode(TEMPLATE_COMMENT_TYPE_CODE);
        commentType.setCodeName(TEMPLATE_COMMENT_TYPE_NAME);

        return commentType;
    }

    /**
     * Update a CommentType identified by its code
     * <p>
     * Copy the values you are allowed to change, code and description
     *
     * @param code    The code of the commentType object you wish to
     *                    update
     * @param incomingCommentType The updated commentType object. Note the
     *                            values you are allowed to change are copied
     *                            from this object. This object is not
     *                            persisted.
     * @return the updated commentType
     */
    @Override
    public MetadataHateoas handleUpdate(
            @NotNull final String code,
            @NotNull final Long version,
            @NotNull final CommentType incomingCommentType) {

        CommentType existingCommentType =
            (CommentType) findMetadataByCodeOrThrow(code);
        updateCodeAndDescription(incomingCommentType, existingCommentType);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingCommentType.setVersion(version);

        MetadataHateoas commentTypeHateoas = new MetadataHateoas(
                commentTypeRepository.save(existingCommentType));

        metadataHateoasHandler.addLinks(commentTypeHateoas,
                new Authorisation());
        return commentTypeHateoas;
    }
}
