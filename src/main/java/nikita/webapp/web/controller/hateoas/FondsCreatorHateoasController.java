package nikita.webapp.web.controller.hateoas;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.FondsCreator;
import nikita.common.model.noark5.v5.hateoas.FondsCreatorHateoas;
import nikita.common.model.noark5.v5.hateoas.FondsHateoas;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.IFondsCreatorService;
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
@RequestMapping(value = HREF_BASE_FONDS_STRUCTURE + SLASH,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class FondsCreatorHateoasController
        extends NoarkController {

    private final IFondsCreatorService fondsCreatorService;

    public FondsCreatorHateoasController(
            IFondsCreatorService fondsCreatorService) {
        this.fondsCreatorService = fondsCreatorService;
    }

    // API - All POST Requests (CRUD - CREATE)

    // Create a new FondsCreator
    // POST [contextPath][api]/arkivstruktur/ny-arkivskaper
    @Operation(summary = "Persists a FondsCreator object",
            description = "Returns the newly created FondsCreator object " +
                    "after it is persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "FondsCreator " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "FondsCreator " +
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
                            " of type FondsCreator"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = NEW_FONDS_CREATOR,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<FondsCreatorHateoas> createFondsCreator(
            @Parameter(name = "FondsCreator",
                    description = "Incoming FondsCreator object",
                    required = true)
            @RequestBody FondsCreator fondsCreator)
            throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(fondsCreatorService.createNewFondsCreator(fondsCreator));
    }

    // Create a new fonds
    // POST [contextPath][api]/arkivstruktur/arkivskaper/{systemID}/ny-arkiv
    @Operation(summary = "Persists a new Fonds associated with a FondsCreator",
            description = "Returns the newly created Fonds after it is " +
                    "associated with the Fonds and persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Fonds " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Fonds " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = FONDS_CREATOR + SLASH + SYSTEM_ID_PARAMETER +
            SLASH + NEW_FONDS,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<FondsHateoas> createFondsAssociatedWithFondsCreator(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of FondsCreator to associate the " +
                            "Fonds with.",
                    required = true)
            @PathVariable(SYSTEM_ID) UUID systemID,
            @Parameter(name = "fonds",
                    description = "Incoming fonds object",
                    required = true)
            @RequestBody Fonds fonds) throws NikitaException {
        return ResponseEntity.status(CREATED)
                .body(fondsCreatorService
                        .createFondsAssociatedWithFondsCreator(systemID, fonds));
    }

    // API - All GET Requests (CRUD - READ)

    // Get a FondsCreator identified by a systemId
    // GET [contextPath][api]/arkivstruktur/arkivskaper/{systemId}
    @Operation(summary = "Retrieves a single FondsCreator entity given a " +
            "systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "FondsCreator returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = FONDS_CREATOR + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<FondsCreatorHateoas>
    findOne(@Parameter(name = SYSTEM_ID,
            description = "systemId of FondsCreator to retrieve.",
            required = true)
            @PathVariable(SYSTEM_ID) final UUID systemId) {
        return ResponseEntity.status(OK)
                .body(fondsCreatorService.findBySystemId(systemId));
    }

    // Get all FondsCreator
    // GET [contextPath][api]/arkivstruktur/arkivskaper/
    @Operation(summary = "Retrieves multiple FondsCreator entities limited by" +
            " ownership rights")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "FondsCreator found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = FONDS_CREATOR)
    public ResponseEntity<FondsCreatorHateoas> findAllFondsCreator() {
        return ResponseEntity.status(OK)
                .body(fondsCreatorService.findAll());
    }

    // API - All PUT Requests (CRUD - UPDATE)

    // Updates a FondsCreator identified by a systemId
    // PUT [contextPath][api]/arkivstruktur/arkivskaper/{systemId}
    @Operation(summary = "Updates a FondsCreator identified by a systemId " +
            "with new values")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "FondsCreator updated"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = FONDS_CREATOR + SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<FondsCreatorHateoas>
    updateFondsCreator(HttpServletRequest request,
                       @Parameter(name = "fondsCreator",
                               description = "Incoming fondsCreator object",
                               required = true)
                       @RequestBody FondsCreator fondsCreator,
                       @Parameter(name = SYSTEM_ID,
                               description = "systemId of FondsCreator to " +
                                       "retrieve.",
                               required = true)
                       @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(fondsCreatorService
                        .handleUpdate(systemID,
                                parseETAG(request.getHeader(ETAG)),
                                fondsCreator));
    }

    // Create a suggested FondsCreator (like a template) object with default values (nothing persisted)
    // GET [contextPath][api]/arkivstruktur/arkiv/{systemID}/ny-arkivskaper
    // GET [contextPath][api]/arkivstruktur/ny-arkivskaper
    @Operation(summary = "Suggests the contents of a new FondsCreator",
            description = "Returns a pre-filled FondsCreator with values " +
                    "relevant for the logged-in user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "FondsCreator " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = {NEW_FONDS_CREATOR, FONDS + SLASH +
            SYSTEM_ID_PARAMETER + SLASH + NEW_FONDS_CREATOR})
    public ResponseEntity<FondsCreatorHateoas> getFondsCreatorTemplate()
            throws NikitaException {
        return ResponseEntity.status(OK)
                .body(fondsCreatorService.generateDefaultFondsCreator());
    }

    // GET [contextPath][api]/arkivstruktur/arkivskaper/{systemId}/arkiv
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkiv/
    @Operation(summary = "Retrieve the Fonds associated with the " +
            "Fonds Creator identified by the given systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Fonds returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = FONDS_CREATOR + SLASH + SYSTEM_ID_PARAMETER + SLASH + FONDS)
    public ResponseEntity<FondsHateoas> findParentFondsAssociatedWithSeries(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the Series ",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        return ResponseEntity.status(OK)
                .body(fondsCreatorService
                        .findFondsAssociatedWithFondsCreator(systemID));
    }

    // Delete a FondsCreator identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/arkivskaper/{systemId}/
    @Operation(summary = "Deletes a single FondsCreator entity identified by" +
            " systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "FondsCreator deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = FONDS_CREATOR + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteSeriesBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the fondsCreator to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemId) {
        fondsCreatorService.deleteEntity(systemId);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete all FondsCreator
    // DELETE [contextPath][api]/arkivstruktur/arkivskaper/
    @Operation(summary = "Deletes all FondsCreator")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Deleted all FondsCreator"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = FONDS_CREATOR)
    public ResponseEntity<String> deleteAllFondsCreator() {
        fondsCreatorService.deleteAllByOwnedBy();
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }
}
