package nikita.webapp.web.controller.hateoas.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.hateoas.admin.UserHateoas;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.IUserService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static com.google.common.net.HttpHeaders.ETAG;
import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = HREF_BASE_ADMIN + SLASH,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class UserController
        extends NoarkController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new bruker
    // POST [contextPath][api]/admin/ny-bruker
    @Operation(summary = "Persists a new User object",
            description = "Returns the newly created User object after it is " +
                    "persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "User " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "User " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = NOT_FOUND_VAL,
                    description = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = NEW_USER)
    public ResponseEntity<UserHateoas> createUser(
            @RequestBody User user)
            throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(userService.createNewUser(user));
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all user
    // GET [contextPath][api]/admin/bruker/
    @Operation(summary = "Retrieves all Users")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "User found"),
            @ApiResponse(
                    responseCode = NOT_FOUND_VAL,
                    description = "No User found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = USER)
    public ResponseEntity<UserHateoas> findAll() {
        UserHateoas userHateoas = userService.findAll();
        return ResponseEntity.status(OK)
                .body(userHateoas);
    }

    // Retrieves a given user identified by a systemId
    // GET [contextPath][api]/admin/bruker/{systemID}/
    @Operation(summary = "Gets user identified by its systemID",
            description = "Returns the requested user object")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL, description = "User found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = NOT_FOUND_VAL,
                    description = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)
    })
    @GetMapping(value = USER + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<UserHateoas>
    findBySystemId(@Parameter(name = SYSTEM_ID,
            description = "systemID of the user to retrieve",
            required = true)
                   @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(userService.findBySystemID(systemID));
    }

    // Create a suggested user(like a template) with default values
    // (nothing persisted)
    // GET [contextPath][api]/admin/ny-bruker
    @Operation(summary = "Creates a suggested User")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "User codes found"),
            @ApiResponse(
                    responseCode = NOT_FOUND_VAL,
                    description = "No User found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = NEW_USER)
    public ResponseEntity<UserHateoas> getUserTemplate() {
        return ResponseEntity.status(OK)
                .body(userService.getDefaultUser());
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a bruker
    // PUT [contextPath][api]/admin/bruker/{systemID}
    @Operation(summary = "Updates a User object",
            description = "Returns the newly updated User object after it is " +
                    "persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL, description = "User " +
                    API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = NOT_FOUND_VAL,
                    description = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = USER + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<UserHateoas>
    updateUser(HttpServletRequest request,
               @Parameter(name = SYSTEM_ID,
                       description = "systemID of documentDescription to " +
                               "update.",
                       required = true)
               @PathVariable(SYSTEM_ID) UUID systemID,
               @Parameter(name = "user",
                       description = "Incoming user object",
                       required = true)
               @RequestBody User user)
            throws NikitaException {
        UserHateoas userHateoas = userService.handleUpdate(systemID,
                parseETAG(request.getHeader(ETAG)), user);
        return ResponseEntity.status(OK)
                .body(userHateoas);
    }

    // Delete all User
    // DELETE [contextPath][api]/admin/bruker/
    @Operation(summary = "Deletes all User"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Deleted all User"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = USER)
    public ResponseEntity<String> deleteAllUser() {
        userService.deleteAll();
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }

    // DELETE [contextPath][api]/admin/bruker/{systemId}/
    @Operation(summary = "Deletes a User object")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Deleted User object"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = USER + SLASH + "{username}")
    public ResponseEntity<String> deleteSingleUser(
            @PathVariable("username") final String username) {
        userService.deleteByUsername(username);
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }
}
