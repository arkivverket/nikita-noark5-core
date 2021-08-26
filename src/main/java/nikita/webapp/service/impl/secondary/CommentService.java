package nikita.webapp.service.impl.secondary;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.RecordEntity;
import nikita.common.model.noark5.v5.hateoas.secondary.CommentHateoas;
import nikita.common.model.noark5.v5.metadata.CommentType;
import nikita.common.model.noark5.v5.secondary.Comment;
import nikita.common.repository.n5v5.secondary.ICommentRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.secondary.ICommentHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.odata.IODataService;
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

@Service
public class CommentService
        extends NoarkService
        implements ICommentService {

    private static final Logger logger =
            LoggerFactory.getLogger(CommentService.class);

    private final IMetadataService metadataService;
    private final ICommentRepository commentRepository;
    private final ICommentHateoasHandler commentHateoasHandler;

    public CommentService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IODataService odataService,
            IPatchService patchService,
            IMetadataService metadataService,
            ICommentRepository commentRepository,
            ICommentHateoasHandler commentHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.metadataService = metadataService;
        this.commentRepository = commentRepository;
        this.commentHateoasHandler = commentHateoasHandler;
    }

    // All CREATE methods

    @Override
    @Transactional
    public CommentHateoas createNewComment(@NotNull final Comment comment,
                                           @NotNull final File file) {
        checkCommentType(comment);
        if (null == comment.getCommentDate())
            comment.setCommentDate(OffsetDateTime.now());
        file.addComment(comment);
        return packAsHateoas(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentHateoas createNewComment(@NotNull final Comment comment,
                                           @NotNull final RecordEntity record) {
        checkCommentType(comment);
        if (null == comment.getCommentDate())
            comment.setCommentDate(OffsetDateTime.now());
        comment.addRecord(record);
        return packAsHateoas(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentHateoas createNewComment(
            @NotNull final Comment comment,
            @NotNull final DocumentDescription documentDescription) {
        checkCommentType(comment);
        if (null == comment.getCommentDate())
            comment.setCommentDate(OffsetDateTime.now());
        comment.addDocumentDescription(documentDescription);
        return packAsHateoas(commentRepository.save(comment));
    }

    // All READ methods

    @Override
    public CommentHateoas findSingleComment(
            @NotNull final UUID commentSystemId) {
        return packAsHateoas(getCommentOrThrow(commentSystemId));
    }

    // All UPDATE methods

    @Override
    @Transactional
    public CommentHateoas handleUpdate(@NotNull final UUID commentSystemId,
                                       @NotNull final Long version,
                                       @NotNull final Comment incomingComment) {
        Comment existingComment = getCommentOrThrow(commentSystemId);

        /* Only check if it changed, in case it has a historical value */
        if (existingComment.getCommentType() !=
                incomingComment.getCommentType())
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
        return packAsHateoas(existingComment);
    }

    // All DELETE methods

    @Override
    @Transactional
    public void deleteComment(@NotNull final UUID systemId) {
        Comment comment = getCommentOrThrow(systemId);
        for (DocumentDescription documentDescription :
                comment.getReferenceDocumentDescription()) {
            comment.removeDocumentDescription(documentDescription);
        }
        for (RecordEntity record : comment.getReferenceRecord()) {
            comment.removeRecord(record);
        }
        for (File file : comment.getReferenceFile()) {
            comment.removeFile(file);
        }
        commentRepository.delete(comment);
    }

    // All template methods

    @Override
    public CommentHateoas generateDefaultComment(@NotNull final UUID systemId) {
        Comment defaultComment = new Comment();
        defaultComment.setCommentDate(OffsetDateTime.now());
        defaultComment.setCommentRegisteredBy(getUser());
        defaultComment.setVersion(-1L, true);
        return packAsHateoas(defaultComment);
    }

    // All helper methods

    private CommentHateoas packAsHateoas(Comment comment) {
        CommentHateoas commentHateoas = new CommentHateoas(comment);
        applyLinksAndHeader(commentHateoas, commentHateoasHandler);
        return commentHateoas;
    }

    protected Comment getCommentOrThrow(@NotNull final UUID commentSystemId) {
        Comment comment = commentRepository.findBySystemId(commentSystemId);
        if (comment == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " Comment, using systemId " + commentSystemId;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return comment;
    }

    private void checkCommentType(Comment comment) {
        if (comment.getCommentType() != null) {
            CommentType commentType = (CommentType)
                    metadataService.findValidMetadata(comment.getCommentType());
            comment.setCommentType(commentType);
        }
    }
}
