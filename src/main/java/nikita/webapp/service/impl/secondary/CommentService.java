package nikita.webapp.service.impl.secondary;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.secondary.CommentHateoas;
import nikita.common.model.noark5.v5.metadata.CommentType;
import nikita.common.model.noark5.v5.secondary.Comment;
import nikita.common.repository.n5v5.secondary.ICommentRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.secondary.ICommentHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.secondary.ICommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.N5ResourceMappings.COMMENT_TYPE;

@Service
@Transactional
public class CommentService
        extends NoarkService
        implements ICommentService {

    private static final Logger logger =
            LoggerFactory.getLogger(CommentService.class);

    private IMetadataService metadataService;
    private ICommentRepository commentRepository;
    private ICommentHateoasHandler commentHateoasHandler;

    public CommentService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IMetadataService metadataService,
            ICommentRepository commentRepository,
            ICommentHateoasHandler commentHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.metadataService = metadataService;
        this.commentRepository = commentRepository;
        this.commentHateoasHandler = commentHateoasHandler;
    }

    @Override
    public CommentHateoas generateDefaultComment() {
        Comment defaultComment = new Comment();

        defaultComment.setCommentDate(OffsetDateTime.now());
        defaultComment.setCommentRegisteredBy(getUser());
        CommentHateoas commentHateoas = new CommentHateoas(defaultComment);
        commentHateoasHandler.addLinksOnTemplate(commentHateoas, new Authorisation());
        return commentHateoas;
    }

    @Override
    public CommentHateoas createNewComment(Comment comment, File file) {
        checkCommentType(comment);
        if (null == comment.getCommentDate())
            comment.setCommentDate(OffsetDateTime.now());
        comment.setReferenceFile(file);
        comment = commentRepository.save(comment);
        file.addReferenceComment(comment);
        CommentHateoas commentHateoas = new CommentHateoas(comment);
        commentHateoasHandler.addLinks(commentHateoas, new Authorisation());
        return commentHateoas;
    }

    @Override
    public CommentHateoas createNewComment(Comment comment, Record record) {
        checkCommentType(comment);
        if (null == comment.getCommentDate())
            comment.setCommentDate(OffsetDateTime.now());
        comment.setReferenceRecord(record);
        comment = commentRepository.save(comment);
        record.addReferenceComment(comment);
        CommentHateoas commentHateoas = new CommentHateoas(comment);
        commentHateoasHandler.addLinks(commentHateoas, new Authorisation());
        return commentHateoas;
    }

    @Override
    public CommentHateoas createNewComment
        (Comment comment, DocumentDescription documentDescription) {
        checkCommentType(comment);
        if (null == comment.getCommentDate())
            comment.setCommentDate(OffsetDateTime.now());
        comment.setReferenceDocumentDescription(documentDescription);
        comment = commentRepository.save(comment);
        documentDescription.addReferenceComment(comment);
        CommentHateoas commentHateoas = new CommentHateoas(comment);
        commentHateoasHandler.addLinks(commentHateoas, new Authorisation());
        return commentHateoas;
    }

    @Override
    public CommentHateoas findSingleComment(String commentSystemId) {
        Comment existingComment = getCommentOrThrow(commentSystemId);

        CommentHateoas commentHateoas =
            new CommentHateoas(commentRepository.save(existingComment));
        commentHateoasHandler.addLinks(commentHateoas, new Authorisation());
        return commentHateoas;
    }

    @Override
    public CommentHateoas handleUpdate(@NotNull String commentSystemId,
                                       @NotNull Long version,
                                       @NotNull Comment incomingComment) {
        Comment existingComment = getCommentOrThrow(commentSystemId);

	/* Only check if it changed, in case it has a historical value */
	if (existingComment.getCommentType() != incomingComment.getCommentType())
	    checkCommentType(incomingComment);

        // Copy all the values you are allowed to copy ....
        existingComment.setCommentText(incomingComment.getCommentText());
        existingComment.setCommentType(incomingComment.getCommentType());
        existingComment.setCommentDate(incomingComment.getCommentDate());
        existingComment.setCommentRegisteredBy
            (incomingComment.getCommentRegisteredBy());

        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingComment.setVersion(version);

        CommentHateoas commentHateoas =
            new CommentHateoas(commentRepository.save(existingComment));
        commentHateoasHandler.addLinks(commentHateoas, new Authorisation());
        return commentHateoas;
    }

    public void deleteEntity(String systemID) {
        deleteEntity(getCommentOrThrow(systemID));
    }

    protected Comment getCommentOrThrow(@NotNull String commentSystemId) {
        Comment comment = commentRepository.
                findBySystemId(UUID.fromString(commentSystemId));
        if (comment == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " Comment, using systemId " + commentSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return comment;
    }

    private void checkCommentType(Comment comment) {
        if (comment.getCommentType() != null) {
            CommentType commentType = (CommentType) metadataService
                    .findValidMetadataByEntityTypeOrThrow(COMMENT_TYPE,
                            comment.getCommentType());
            comment.setCommentType(commentType);
        }
    }
}
