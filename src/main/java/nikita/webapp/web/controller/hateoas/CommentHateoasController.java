package nikita.webapp.web.controller.hateoas;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.secondary.CommentHateoas;
import nikita.common.model.noark5.v5.secondary.Comment;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.secondary.ICommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = HREF_BASE_FONDS_STRUCTURE + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class CommentHateoasController
        extends NoarkController {

    private final ICommentService commentService;

    public CommentHateoasController(ICommentService commentService) {
        this.commentService = commentService;
    }

    // GET [contextPath][api]/arkivstruktur/merknad/{systemId}/
    @Operation(summary = "Retrieves a single comment entity given a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Comment returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = COMMENT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<CommentHateoas> findOne(
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of comment to retrieve.",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        CommentHateoas commentHateoas =
                commentService.findSingleComment(systemID);
        return ResponseEntity.status(OK)
                .body(commentHateoas);
    }

    // PUT [contextPath][api]/arkivstruktur/merknad/{systemId}/
    @Operation(
            summary = "Updates a Comment object",
            description = "Returns the newly updated Comment object after it " +
                    "is persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description =
                            "Comment " + API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Comment " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = NOT_FOUND_VAL,
                    description = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type Comment"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = COMMENT + SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CommentHateoas> updateComment(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of comment to update.",
                    required = true)
            @PathVariable(SYSTEM_ID) UUID systemID,
            @Parameter(name = "comment",
                    description = "Incoming comment object",
                    required = true)
            @RequestBody Comment comment) throws NikitaException {
        CommentHateoas commentHateoas =
                commentService.handleUpdate(systemID,
                        parseETAG(request.getHeader(ETAG)), comment);
        return ResponseEntity.status(OK)
                .body(commentHateoas);
    }

    // DELETE [contextPath][api]/arkivstruktur/merknad/{systemId}/
    @Operation(
            summary = "Deletes a single Comment entity identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "ok message"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = COMMENT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteCommentBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the comment to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        commentService.deleteComment(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
