package nikita.webapp.web.controller.hateoas;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.secondary.AuthorHateoas;
import nikita.common.model.noark5.v5.secondary.Author;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.secondary.IAuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = HREF_BASE_FONDS_STRUCTURE + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class AuthorHateoasController
        extends NoarkController {

    private final IAuthorService authorService;

    public AuthorHateoasController(IAuthorService authorService) {
        this.authorService = authorService;
    }

    // API - All GET Requests (CRUD - READ)

    // GET [contextPath][api]/arkivstruktur/forfatter/{systemId}
    @Operation(summary = "Retrieves a single Author entity given a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Author returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = AUTHOR + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<AuthorHateoas> findAuthorBySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the Author to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemId) {
        AuthorHateoas authorHateoas = authorService.findBySystemId(systemId);
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(authorHateoas);
    }

    // PUT [contextPath][api]/arkivstruktur/forfatter/{systemId}
    @Operation(summary = "Updates a Author identified by a given systemId",
            description = "Returns the newly updated nationalIdentifierPerson")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Author " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Author " +
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
                            " of type Author"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = AUTHOR + SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<AuthorHateoas> updateAuthorBySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of Author to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @Parameter(name = "Author",
                    description = "Incoming Author object",
                    required = true)
            @RequestBody Author author) throws NikitaException {
        AuthorHateoas authorHateoas =
            authorService.updateAuthorBySystemId
            (systemID, parseETAG(request.getHeader(ETAG)), author);
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(authorHateoas);
    }

    // DELETE [contextPath][api]/arkivstruktur/forfatter/{systemID}/
    @Operation(
            summary = "Deletes a single Author entity identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Author deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = AUTHOR + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteAuthorBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the author to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        authorService.deleteAuthorBySystemId(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
