package nikita.webapp.web.controller.hateoas;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.secondary.AuthorHateoas;
import nikita.common.model.noark5.v5.secondary.Author;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.secondary.IAuthorHateoasHandler;
import nikita.webapp.service.interfaces.secondary.IAuthorService;
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
public class AuthorHateoasController
        extends NoarkController {

    private IAuthorHateoasHandler authorHateoasHandler;
    private IAuthorService authorService;

    public AuthorHateoasController(IAuthorHateoasHandler authorHateoasHandler,
                                   IAuthorService authorService) {
        this.authorHateoasHandler = authorHateoasHandler;
        this.authorService = authorService;
    }

    // API - All GET Requests (CRUD - READ)

    // GET [contextPath][api]/arkivstruktur/forfatter/{systemId}
    @ApiOperation(value = "Retrieves a single Author entity given a systemId",
            response = AuthorHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Author returned",
                    response = Author.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = AUTHOR + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<AuthorHateoas> findAuthorBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the Author to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemId) {
        AuthorHateoas authorHateoas = authorService.findBySystemId(systemId);
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(authorHateoas);
    }

    // PUT [contextPath][api]/arkivstruktur/forfatter/{systemId}
    @ApiOperation(value = "Updates a Author identified by a given systemId",
            notes = "Returns the newly updated nationalIdentifierPerson",
            response = AuthorHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Author " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = AuthorHateoas.class),
            @ApiResponse(code = 201, message = "Author " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = AuthorHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type Author"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = AUTHOR + SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<AuthorHateoas> updateAuthorBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of Author to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID,
            @ApiParam(name = "Author",
                    value = "Incoming Author object",
                    required = true)
            @RequestBody Author author) throws NikitaException {
        AuthorHateoas authorHateoas =
            authorService.updateAuthorBySystemId
            (systemID, parseETAG(request.getHeader(ETAG)), author);
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(authorHateoas);
    }

    // DELETE [contextPath][api]/arkivstruktur/forfatter/{systemID}/
    @ApiOperation(value = "Deletes a single Author entity identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Author deleted"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = AUTHOR + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteAuthorBySystemId(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the author to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        authorService.deleteAuthorBySystemId(systemID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
