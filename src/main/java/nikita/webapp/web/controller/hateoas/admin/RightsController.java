package nikita.webapp.web.controller.hateoas.admin;

import nikita.webapp.web.controller.hateoas.NoarkController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Operation(summary = "Persists a new User object", description = "Returns the newly" +
            " created User object after it is persisted to the database",
            response = User.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK_VAL, description = "User " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = User.class),
            @ApiResponse(responseCode = CREATED_VAL, description = "User " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = User.class),
            @ApiResponse(responseCode = UNAUTHORIZED_VAL, description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(responseCode = FORBIDDEN_VAL, description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(responseCode = NOT_FOUND_VAL, description = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(responseCode = CONFLICT_VAL, description = API_MESSAGE_CONFLICT),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_VAL, description = API_MESSAGE_INTERNAL_SERVER_ERROR),
            @ApiResponse(responseCode = 501, description = API_MESSAGE_NOT_IMPLEMENTED)})
    
    @PostMapping(value = NEW_ADMINISTRATIVE_UNIT)
    public ResponseEntity<UserHateoas> createUser(
            HttpServletRequest request,
            @RequestBody User administrativeUnit)
            throws NikitaException {
        administrativeUnitService.createNewUser(administrativeUnit);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all administrativeUnit
    // GET [contextPath][api]/admin/administrativtenhet/
    @Operation(summary = "Retrieves all User ", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK_VAL, description = "User found",
                    response = User.class),
            @ApiResponse(responseCode = NOT_FOUND_VAL, description = "No User found"),
            @ApiResponse(responseCode = UNAUTHORIZED_VAL, description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(responseCode = FORBIDDEN_VAL, description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_VAL, description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    
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
    @Operation(summary = "Gets administrativeUnit identified by its systemId", description = "Returns the requested " +
            " administrativeUnit object", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK_VAL, description = "User " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = User.class),
            @ApiResponse(responseCode = CREATED_VAL, description = "User " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = User.class),
            @ApiResponse(responseCode = UNAUTHORIZED_VAL, description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(responseCode = FORBIDDEN_VAL, description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(responseCode = NOT_FOUND_VAL, description = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(responseCode = CONFLICT_VAL, description = API_MESSAGE_CONFLICT),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_VAL, description = API_MESSAGE_INTERNAL_SERVER_ERROR),
            @ApiResponse(responseCode = 501, description = API_MESSAGE_NOT_IMPLEMENTED)})
    
    @GetMapping(value = ADMINISTRATIVE_UNIT + SLASH + SYSTEM_ID_PARAMETER + SLASH)
    public ResponseEntity<UserHateoas> findBySystemId(@PathVariable(SYSTEM_ID) final UUID systemId,
                                                                                   HttpServletRequest request) {
        User administrativeUnit = administrativeUnitService.findBySystemId(UUID.fromString(systemId));
        UserHateoas adminHateoas = new UserHateoas(administrativeUnit);
        administrativeUnitHateoasHandler.addLinks(adminHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminHateoas);
    }

    // Create a suggested administrativeUnit(like a template) with default values (nothing persisted)
    // GET [contextPath][api]/admin/ny-administrativtenhet
    @Operation(summary = "Creates a suggested User", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK_VAL, description = "User codes found",
                    response = User.class),
            @ApiResponse(responseCode = NOT_FOUND_VAL, description = "No User found"),
            @ApiResponse(responseCode = UNAUTHORIZED_VAL, description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(responseCode = FORBIDDEN_VAL, description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_VAL, description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    
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
    @Operation(summary = "Updates a User object", description = "Returns the newly" +
            " updated User object after it is persisted to the database",
            response = User.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK_VAL, description = "User " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = User.class),
            @ApiResponse(responseCode = UNAUTHORIZED_VAL, description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(responseCode = FORBIDDEN_VAL, description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(responseCode = NOT_FOUND_VAL, description = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(responseCode = CONFLICT_VAL, description = API_MESSAGE_CONFLICT),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_VAL, description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    
    @PutMapping(value = ADMINISTRATIVE_UNIT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<UserHateoas> updateUser(HttpServletRequest request,
                                                                              @Parameter(name = SYSTEM_ID,
                                                                                      description = "systemID of documentDescription to update.",
                                                                                      required = true)
                                                                              @PathVariable(SYSTEM_ID) UUID systemID,
                                                                              @Parameter(name = "administrativeUnit",
                                                                                      description = "Incoming administrativeUnit object",
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
