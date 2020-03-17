package nikita.webapp.web.controller.hateoas;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.secondary.CommentHateoas;
import nikita.common.model.noark5.v5.secondary.Comment;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.secondary.ICommentHateoasHandler;
import nikita.webapp.service.interfaces.secondary.ICommentService;
import nikita.webapp.web.controller.hateoas.NoarkController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;

@RestController
@RequestMapping(value = HREF_BASE_FONDS_STRUCTURE + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class CommentHateoasController
        extends NoarkController {

    private ICommentHateoasHandler commentHateoasHandler;
    private ICommentService commentService;

    public CommentHateoasController
        (ICommentHateoasHandler commentHateoasHandler,
         ICommentService commentService) {
        this.commentHateoasHandler = commentHateoasHandler;
        this.commentService = commentService;
    }

    // GET [contextPath][api]/arkivstruktur/merknad/{systemId}/
    @ApiOperation(value = "Retrieves a single comment entity given a systemId",
            response = Comment.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Comment returned",
                    response = Comment.class),
            @ApiResponse(
                    code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = COMMENT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<CommentHateoas> findOne(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of comment to retrieve.",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        CommentHateoas commentHateoas =
	    commentService.findSingleComment(systemID);
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(commentHateoas.getEntityVersion().toString())
                .body(commentHateoas);
    }

    // PUT [contextPath][api]/arkivstruktur/merknad/{systemId}/
    @ApiOperation(
            value = "Updates a Comment object",
            notes = "Returns the newly updated Comment object after it is " +
                    "persisted to the database",
            response = CommentHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Comment " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = CommentHateoas.class),
            @ApiResponse(
                    code = 201,
                    message = "Comment " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = CommentHateoas.class),
            @ApiResponse(
                    code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 404,
                    message = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type Comment"),
            @ApiResponse(
                    code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = COMMENT + SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CommentHateoas> updateComment(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of comment to update.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @ApiParam(name = "comment",
                    value = "Incoming comment object",
                    required = true)
            @RequestBody Comment comment) throws NikitaException {
        CommentHateoas commentHateoas =
                commentService.handleUpdate(systemID,
                        parseETAG(request.getHeader(ETAG)), comment);

        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(commentHateoas.getEntityVersion().toString())
                .body(commentHateoas);
    }

    // DELETE [contextPath][api]/arkivstruktur/merknad/{systemId}/
    @ApiOperation(
            value = "Deletes a single Comment entity identified by systemID",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 204,
                    message = "ok message",
                    response = String.class),
            @ApiResponse(
                    code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = COMMENT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteCommentBySystemId(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the comment to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        commentService.deleteEntity(systemID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
