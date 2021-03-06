package nikita.webapp.web.controller.hateoas.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.hateoas.admin.AdministrativeUnitHateoas;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.admin.IAdministrativeUnitService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = HREF_BASE_ADMIN + SLASH,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class AdministrativeUnitController
        extends NoarkController {

    private final IAdministrativeUnitService administrativeUnitService;

    public AdministrativeUnitController(
            IAdministrativeUnitService administrativeUnitService) {
        this.administrativeUnitService = administrativeUnitService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new administrativtenhet
    // POST [contextPath][api]/admin/ny-administrativtenhet
    @Operation(summary = "Persists a new AdministrativeUnit object",
            description = "Returns the newly created AdministrativeUnit " +
                    "object after it is persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "AdministrativeUnit " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "AdministrativeUnit " +
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
    @PostMapping(value = NEW_ADMINISTRATIVE_UNIT)
    public ResponseEntity<AdministrativeUnitHateoas> createAdministrativeUnit(
            @RequestBody AdministrativeUnit administrativeUnit)
            throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(administrativeUnitService
                        .createNewAdministrativeUnitBySystem(
                                administrativeUnit));
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all administrativeUnit
    // GET [contextPath][api]/admin/administrativtenhet/
    @Operation(summary = "Retrieves all AdministrativeUnit ")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "AdministrativeUnit found"),
            @ApiResponse(
                    responseCode = NOT_FOUND_VAL,
                    description = "No AdministrativeUnit found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = ADMINISTRATIVE_UNIT)
    public ResponseEntity<AdministrativeUnitHateoas> findAll() {
        return ResponseEntity.status(OK)
                .body(administrativeUnitService.findAll());
    }

    // Retrieves a given administrativeUnit identified by a systemId
    // GET [contextPath][api]/admin/administrativtenhet/{systemId}/
    @Operation(summary = "Gets administrativeUnit identified by its systemId",
            description = "Returns the requested administrativeUnit object")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "AdministrativeUnit " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "AdministrativeUnit " +
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
    @GetMapping(value = ADMINISTRATIVE_UNIT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<AdministrativeUnitHateoas>
    findBySystemId(@PathVariable(SYSTEM_ID) final UUID systemId) {
        return ResponseEntity.status(OK)
                .body(administrativeUnitService.findBySystemId(systemId));
    }

    // Create a suggested administrativeUnit(like a template) with default
    // values (nothing persisted)
    // GET [contextPath][api]/admin/ny-administrativtenhet
    @Operation(summary = "Creates a suggested AdministrativeUnit")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "AdministrativeUnit codes found"),
            @ApiResponse(
                    responseCode = NOT_FOUND_VAL,
                    description = "No AdministrativeUnit found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = NEW_ADMINISTRATIVE_UNIT)
    public ResponseEntity<AdministrativeUnitHateoas>
    getAdministrativeUnitTemplate(HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .body(administrativeUnitService
                        .generateDefaultAdministrativeUnit());
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a administrativtenhet
    // PUT [contextPath][api]/admin/administrativtenhet/{systemID}
    @Operation(summary = "Updates a AdministrativeUnit object",
            description = "Returns the newly updated AdministrativeUnit " +
                    "object after it is persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "AdministrativeUnit " +
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

    @PutMapping(value = ADMINISTRATIVE_UNIT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<AdministrativeUnitHateoas> updateAdministrativeUnit(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of documentDescription to update.",
                    required = true)
            @PathVariable(SYSTEM_ID) UUID systemID,
            @Parameter(name = "administrativeUnit",
                    description = "Incoming administrativeUnit object",
                    required = true)
            @RequestBody AdministrativeUnit administrativeUnit)
            throws NikitaException {
        return ResponseEntity.status(OK)
                .body(administrativeUnitService.update(systemID,
                        parseETAG(request.getHeader(ETAG)), administrativeUnit));
    }

    // Delete all AdministrativeUnit
    // DELETE [contextPath][api]/admin/administrativtenhet/
    @Operation(summary = "Deletes all AdministrativeUnit")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Deleted all AdministrativeUnit"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = ADMINISTRATIVE_UNIT)
    public ResponseEntity<String> deleteAllAdministrativeUnit() {
        administrativeUnitService.deleteAllByOwnedBy();
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }

    // Delete an AdministrativeUnit identified by systemID
    // DELETE [contextPath][api]/admin/administrativtenhet/{systemId}/
    @Operation(summary = "Delete an AdministrativeUnit object")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Delete AdministrativeUnit object"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = ADMINISTRATIVE_UNIT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteAdministrativeUnit(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of AdministrativeUnit to delete.",
                    required = true)
            @PathVariable(SYSTEM_ID) UUID systemID) {
        administrativeUnitService.deleteEntity(systemID);
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }
}
