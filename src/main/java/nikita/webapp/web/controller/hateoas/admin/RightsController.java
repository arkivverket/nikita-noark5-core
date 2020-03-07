package nikita.webapp.web.controller.hateoas.admin;

import nikita.common.config.Constants;
import nikita.webapp.web.controller.hateoas.NoarkController;
import org.springframework.web.bind.annotation.*;

import static nikita.common.config.Constants.*;

@RestController
@RequestMapping(value = HREF_BASE_ADMIN + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class RightsController extends NoarkController {
/*
    private IUserService administrativeUnitService;
    private IUserHateoasHandler administrativeUnitHateoasHandler;

    public RightsController(IUserService administrativeUnitService,
                            IUserHateoasHandler administrativeUnitHateoasHandler) {
        this.administrativeUnitService = administrativeUnitService;
        this.administrativeUnitHateoasHandler = administrativeUnitHateoasHandler;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new administrativtenhet
    // POST [contextPath][api]/admin/ny-administrativtenhet
    @ApiOperation(value = "Persists a new User object", notes = "Returns the newly" +
            " created User object after it is persisted to the database",
            response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = User.class),
            @ApiResponse(code = 201, message = "User " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = User.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR),
            @ApiResponse(code = 501, message = API_MESSAGE_NOT_IMPLEMENTED)})
    @Counted

    @PostMapping(value = NEW_ADMINISTRATIVE_UNIT)
    public ResponseEntity<UserHateoas> createUser(
            HttpServletRequest request,
            @RequestBody User administrativeUnit)
            throws NikitaException {
        administrativeUnitService.createNewUser(administrativeUnit);
        UserHateoas adminHateoas = new UserHateoas(administrativeUnit);
        administrativeUnitHateoasHandler.addLinks(adminHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(administrativeUnit.getVersion().toString())
                .body(adminHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all administrativeUnit
    // GET [contextPath][api]/admin/administrativtenhet/
    @ApiOperation(value = "Retrieves all User ", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User found",
                    response = User.class),
            @ApiResponse(code = 404, message = "No User found"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = ADMINISTRATIVE_UNIT)
    public ResponseEntity<UserHateoas> findAll(HttpServletRequest request) {
        UserHateoas adminHateoas = new UserHateoas(
                (List<INoarkEntity>) (List) administrativeUnitService.findAll());
        administrativeUnitHateoasHandler.addLinks(adminHateoas, new Authorisation());

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(adminHateoas);
    }

    // Retrieves a given administrativeUnit identified by a systemId
    // GET [contextPath][api]/admin/administrativtenhet/{systemId}/
    @ApiOperation(value = "Gets administrativeUnit identified by its systemId", notes = "Returns the requested " +
            " administrativeUnit object", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = User.class),
            @ApiResponse(code = 201, message = "User " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = User.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR),
            @ApiResponse(code = 501, message = API_MESSAGE_NOT_IMPLEMENTED)})
    @Counted

    @GetMapping(value = ADMINISTRATIVE_UNIT + SLASH + SYSTEM_ID_PARAMETER + SLASH)
    public ResponseEntity<UserHateoas> findBySystemId(@PathVariable(SYSTEM_ID) final String systemId,
                                                                                   HttpServletRequest request) {
        User administrativeUnit = administrativeUnitService.findBySystemId(UUID.fromString(systemId));
        UserHateoas adminHateoas = new UserHateoas(administrativeUnit);
        administrativeUnitHateoasHandler.addLinks(adminHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(administrativeUnit.getVersion().toString())
                .body(adminHateoas);
    }

    // Create a suggested administrativeUnit(like a template) with default values (nothing persisted)
    // GET [contextPath][api]/admin/ny-administrativtenhet
    @ApiOperation(value = "Creates a suggested User", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User codes found",
                    response = User.class),
            @ApiResponse(code = 404, message = "No User found"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = NEW_ADMINISTRATIVE_UNIT)
    public ResponseEntity<UserHateoas> getUserTemplate(HttpServletRequest request) {
        User administrativeUnit = new User();
        administrativeUnit.setShortName("kortnavn på administrativtenhet");
        administrativeUnit.setUserName("Formell navn på administrativtenhet");
        UserHateoas adminHateoas = new UserHateoas(administrativeUnit);
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(adminHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a administrativtenhet
    // PUT [contextPath][api]/metadata/administrativtenhet/{systemID}
    @ApiOperation(value = "Updates a User object", notes = "Returns the newly" +
            " updated User object after it is persisted to the database",
            response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = User.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PutMapping(value = ADMINISTRATIVE_UNIT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<UserHateoas> updateUser(HttpServletRequest request,
                                                                              @ApiParam(name = SYSTEM_ID,
                                                                                      value = "systemID of documentDescription to update.",
                                                                                      required = true)
                                                                              @PathVariable(SYSTEM_ID) String systemID,
                                                                              @ApiParam(name = "administrativeUnit",
                                                                                      value = "Incoming administrativeUnit object",
                                                                                      required = true)
                                                                              @RequestBody User administrativeUnit)
            throws NikitaException {
        User newUser = administrativeUnitService.update(systemID,
                parseETAG(request.getHeader(ETAG)), administrativeUnit);
        UserHateoas adminHateoas = new UserHateoas(administrativeUnit);
        administrativeUnitHateoasHandler.addLinks(adminHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(adminHateoas);
    }*/
}
