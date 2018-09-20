package nikita.webapp.web.controller.hateoas.admin;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v4.admin.User;
import nikita.common.model.noark5.v4.hateoas.admin.UserHateoas;
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

@RestController
@RequestMapping(value = Constants.HATEOAS_API_PATH + SLASH +
        NOARK_ADMINISTRATION_PATH + SLASH,
        produces = {NOARK5_V4_CONTENT_TYPE_JSON})
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
    @RequestMapping(method = RequestMethod.POST, value = NEW_USER)
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
    @RequestMapping(method = RequestMethod.GET, value = USER)
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
    @RequestMapping(value = USER + SLASH + LEFT_PARENTHESIS +
            SYSTEM_ID + RIGHT_PARENTHESIS + SLASH,
            method = RequestMethod.GET)
    public ResponseEntity<UserHateoas>
    findBySystemId(@PathVariable("username") final String username,
                   HttpServletRequest request) {
        UserHateoas userHateoas = userService.findByUsername(username);
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

    @RequestMapping(method = RequestMethod.GET, value = NEW_USER)
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

    @RequestMapping(method = RequestMethod.PUT, value = USER +
            SLASH + LEFT_PARENTHESIS + SYSTEM_ID + RIGHT_PARENTHESIS)
    public ResponseEntity<UserHateoas>
    updateUser(HttpServletRequest request,
               @ApiParam(name = "systemID",
                       value = "systemID of documentDescription to update.",
                       required = true)
               @PathVariable("systemID") String systemID,
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
}
