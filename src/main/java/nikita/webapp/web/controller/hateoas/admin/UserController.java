package nikita.webapp.web.controller.hateoas.admin;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.hateoas.admin.UserHateoas;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.IUserService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.google.common.net.HttpHeaders.ETAG;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping(value = HREF_BASE_ADMIN + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class UserController
        extends NoarkController {

    private IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new bruker
    // POST [contextPath][api]/admin/ny-bruker
    @ApiOperation(value = "Persists a new User object",
            notes = "Returns the newly created User object after it is " +
                    "persisted to the database",
            response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "User " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = User.class),
            @ApiResponse(code = 201,
                    message = "User " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = User.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR),
            @ApiResponse(code = 501,
                    message = API_MESSAGE_NOT_IMPLEMENTED)})
    @Counted
    @PostMapping(value = NEW_USER)
    public ResponseEntity<UserHateoas> createUser(
            HttpServletRequest request,
            @RequestBody User user)
            throws NikitaException {
        UserHateoas userHateoas = userService.createNewUser(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(user.getVersion().toString())
                .body(userHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all user
    // GET [contextPath][api]/admin/bruker/
    @ApiOperation(value = "Retrieves all Users ", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "User found",
                    response = User.class),
            @ApiResponse(code = 404,
                    message = "No User found"),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = USER)
    public ResponseEntity<UserHateoas> findAll(HttpServletRequest request) {
        UserHateoas userHateoas = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(userHateoas);
    }

    // Retrieves a given user identified by a systemId
    // GET [contextPath][api]/admin/bruker/{systemID}/
    @ApiOperation(value = "Gets user identified by its systemID",
            notes = "Returns the requested user object", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User found",
                    response = User.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)
    })
    @Counted
    @GetMapping(value = USER + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<UserHateoas>
    findBySystemId(HttpServletRequest request,
		   @ApiParam(name = SYSTEM_ID,
			     value = "systemID of the user to retrieve",
			     required = true)
		   @PathVariable(SYSTEM_ID) final String systemID) {
        UserHateoas userHateoas = userService.findBySystemID(systemID);
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(userHateoas.getEntityVersion().toString())
                .body(userHateoas);
    }

    // Create a suggested user(like a template) with default values
    // (nothing persisted)
    // GET [contextPath][api]/admin/ny-bruker
    @ApiOperation(value = "Creates a suggested User", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "User codes found",
                    response = User.class),
            @ApiResponse(code = 404,
                    message = "No User found"),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = NEW_USER)
    public ResponseEntity<UserHateoas>
    getUserTemplate(HttpServletRequest request) {
        User user = new User();
        user.setUsername("example@example.com");
        user.setFirstname("Hans");
        user.setLastname("Hansen");
        UserHateoas userHateoas = new UserHateoas(user);
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .body(userHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a bruker
    // PUT [contextPath][api]/admin/bruker/{systemID}
    @ApiOperation(value = "Updates a User object", notes = "Returns the newly" +
            " updated User object after it is persisted to the database",
            response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User " +
                    API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = User.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = USER + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<UserHateoas>
    updateUser(HttpServletRequest request,
               @ApiParam(name = SYSTEM_ID,
                       value = "systemID of documentDescription to update.",
                       required = true)
               @PathVariable(SYSTEM_ID) String systemID,
               @ApiParam(name = "user",
                       value = "Incoming user object",
                       required = true)
               @RequestBody User user)
            throws NikitaException {
        UserHateoas userHateoas = userService.handleUpdate(systemID,
                parseETAG(request.getHeader(ETAG)), user);
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(userHateoas.getEntityVersion().toString())
                .body(userHateoas);
    }

    // Delete all User
    // DELETE [contextPath][api]/admin/bruker/
    @ApiOperation(value = "Deletes all User", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Deleted all User",
                    response = String.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = USER)
    public ResponseEntity<String> deleteAllUser() {
        userService.deleteAll();
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }

    // DELETE [contextPath][api]/admin/bruker/{systemId}/
    @ApiOperation(value = "Deletes a User object", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Deleted User object",
                    response = String.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = USER + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteSingleUser(
            @PathVariable("username") final String username) {
        userService.deleteByUsername(username);
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }
}
