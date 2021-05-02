package nikita.webapp.web.controller.hateoas;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.FondsCreator;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.hateoas.FondsCreatorHateoas;
import nikita.common.model.noark5.v5.hateoas.FondsHateoas;
import nikita.common.model.noark5.v5.hateoas.SeriesHateoas;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.IFondsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = HREF_BASE_FONDS_STRUCTURE + SLASH,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class FondsHateoasController
        extends NoarkController {

    private final IFondsService fondsService;

    public FondsHateoasController(IFondsService fondsService) {
        this.fondsService = fondsService;
    }

    // API - All POST Requests (CRUD - CREATE)

    // Create a Fonds
    // POST [contextPath][api]/arkivstruktur/arkiv
    @Operation(
            summary = "Persists a Fonds object",
            description = "Returns the newly created Fonds object after it is" +
                    " persisted to the database")
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
                    responseCode = NOT_FOUND_VAL,
                    description = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type Fonds"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = NEW_FONDS,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<FondsHateoas> createFonds(
            HttpServletRequest request,
            @Parameter(name = "fonds",
                    description = "Incoming fonds object",
                    required = true)
            @RequestBody Fonds fonds) throws NikitaException {
        validateForCreate(fonds);
        FondsHateoas fondsHateoas = fondsService.createNewFonds(fonds);
        return ResponseEntity.status(CREATED)
                .allow(
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(fonds.getVersion().toString())
                .body(fondsHateoas);
    }

    // Create a sub-fonds and associate it with the Fonds identified by systemId
    // POST [contextPath][api]/arkivstruktur/arkiv/{systemId}/ny-arkiv
    @Operation(
            summary = "Persists a child Fonds object",
            description = "Returns the newly created child Fonds object after" +
                    " it is persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK_VAL,
                    description = "Fonds " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(responseCode = CREATED_VAL,
                    description = "Fonds " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(responseCode = NOT_FOUND_VAL,
                    description = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type Fonds"),
            @ApiResponse(responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = FONDS + SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_FONDS,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<FondsHateoas> createFondsAssociatedWithFonds(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of parent fonds to associate the fonds " +
                            "with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @Parameter(name = "fonds",
                    description = "Incoming fonds object",
                    required = true)
            @RequestBody Fonds fonds)
            throws NikitaException {
        validateForCreate(fonds);
        FondsHateoas fondsHateoas = fondsService
                .createFondsAssociatedWithFonds(systemID, fonds);
        return ResponseEntity.status(CREATED)
                .allow(
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(fonds.getVersion().toString())
                .body(fondsHateoas);
    }

    // Create a Series and associate it with the Fonds identified by systemId
    // POST [contextPath][api]/arkivstruktur/arkiv/{systemId}/ny-arkivdel
    @Operation(
            summary = "Persists a Series object associated with the given " +
                    "Fonds systemID",
            description = "Returns the newly created Series object after it " +
                    "was associated with a Fonds object and persisted to the " +
                    "database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Series " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "Series" +
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
                            " of type Series"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping(value = FONDS + SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_SERIES,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<SeriesHateoas> createSeriesAssociatedWithFonds(
            HttpServletRequest request,
            @Parameter(
                    name = SYSTEM_ID,
                    description = "systemID of fonds to associate the series " +
                            "with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @Parameter(
                    name = "series",
                    description = "Incoming series object",
                    required = true)
            @RequestBody Series series)
            throws NikitaException {
        validateForCreate(series);
        SeriesHateoas seriesHateoas =
                fondsService.createSeriesAssociatedWithFonds(systemID, series);
        return ResponseEntity.status(CREATED)
                .allow(
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(seriesHateoas.getEntityVersion().toString())
                .body(seriesHateoas);
    }

    // Create a FondsCreator and associate it with the Fonds identified by systemId
    // API - All GET Requests (CRUD - READ)

    // POST [contextPath][api]/arkivstruktur/arkiv/{systemId}/ny-arkivskaper
    @Operation(
            summary = "Persists a FondsCreator associated with the given " +
                    "Fonds systemId",
            description = "Returns the newly created FondsCreator after it " +
                    "was associated with a Fonds and persisted to the database")
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
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)
    })
    @PostMapping(value = FONDS + SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_FONDS_CREATOR,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<FondsCreatorHateoas>
    createFondsCreatorAssociatedWithFonds(
            HttpServletRequest request,
            @Parameter(
                    name = SYSTEM_ID,
                    description = "systemID of fonds to associate the series " +
                            "with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @Parameter(
                    name = "fondsCreator",
                    description = "Incoming fondsCreator object",
                    required = true)
            @RequestBody FondsCreator fondsCreator)
            throws NikitaException {

        validateForCreate(fondsCreator);
        FondsCreatorHateoas fondsCreatorHateoas = fondsService
                .createFondsCreatorAssociatedWithFonds(systemID, fondsCreator);

        return ResponseEntity.status(CREATED)
                .allow(
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(fondsCreatorHateoas.getEntityVersion().toString())
                .body(fondsCreatorHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/arkiv/{systemId}/
    @Operation(summary = "Retrieves a single fonds entity given a systemId")
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
    @GetMapping(value = FONDS + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<FondsHateoas> findOne(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of fonds to retrieve.",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        FondsHateoas fondsHateoas = fondsService.findSingleFonds(systemID);
        return ResponseEntity.status(OK)
                .allow(
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(fondsHateoas.getEntityVersion().toString())
                .body(fondsHateoas);
    }

    // Return a Series object with default values
    //GET [contextPath][api]/arkivstruktur/arkiv/{systemId}/ny-arkivdel
    @Operation(
            summary = "Create a Series with default values")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Series returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = FONDS + SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_SERIES)
    public ResponseEntity<SeriesHateoas> createDefaultSeries(
            HttpServletRequest request,
            @Parameter(
                    name = SYSTEM_ID,
                    description = "systemID of fonds to associate Series with.",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fondsService.generateDefaultSeries(systemID));
    }

    // Get all fondsCreators associated with fonds identified by systemId
    // GET [contextPath][api]/arkivstruktur/arkiv/{systemId}/arkivskaper/
    @Operation(summary = "Retrieves the fondsCreators associated with a " +
            "Fonds identified by a systemId")
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
    @GetMapping(value = FONDS + SLASH + SYSTEM_ID_PARAMETER + SLASH +
            FONDS_CREATOR)
    public ResponseEntity<FondsCreatorHateoas>
    findFondsCreatorAssociatedWithFonds(
            @Parameter(
                    name = SYSTEM_ID,
                    description = "systemID of fonds you want retrieve " +
                            "associated FondsCreator objects for.",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return fondsService.findFondsCreatorAssociatedWithFonds(systemID);
    }

    // Get all Series associated with Fonds identified by systemId
    // GET [contextPath][api]/arkivstruktur/arkiv/{systemId}/arkivdel/
    @Operation(
            summary = "Retrieves the Series associated with a Fonds " +
                    "identified by a systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Series returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = FONDS + SLASH + SYSTEM_ID_PARAMETER + SLASH + SERIES)
    public ResponseEntity<SeriesHateoas>
    findSeriesAssociatedWithFonds(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of Fonds that has Series " +
                            "associated with it.",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {

        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fondsService.findSeriesAssociatedWithFonds(systemID));
    }

    // GET [contextPath][api]/arkivstruktur/arkiv/{systemId}/underarkiv/
    // REL https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/underarkiv/
    @Operation(summary = "Retrieves the (sub)Fonds associated with a Fonds " +
            "identified by a systemId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK_VAL,
                    description = "Fonds children found"),
            @ApiResponse(responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = FONDS + SLASH + SYSTEM_ID_PARAMETER + SLASH +
            SUB_FONDS)
    public ResponseEntity<FondsHateoas> findAllSubFondsAssociatedWithFonds(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of parent Fonds",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        FondsHateoas fondsHateoas = fondsService.findAllChildren(systemID);
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fondsHateoas);
    }

    // Get all fonds
    // GET [contextPath][api]/arkivstruktur/arkiv/
    @Operation(
            summary = "Retrieves multiple Fonds entities limited by ownership" +
                    " rights")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Fonds found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)
    })
    @GetMapping(value = FONDS)
    public ResponseEntity<FondsHateoas> findAllFonds(
            HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fondsService.findAllFonds());
    }

    // Create a suggested Fonds (like a template) object with default values
    // (nothing persisted)
    // GET [contextPath][api]/arkivstruktur/{systemId}/ny-arkiv
    @Operation(
            summary = "Suggests the contents of a new Fonds object",
            description = "Returns a pre-filled Fonds object with values " +
                    "relevant for the logged-in user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Fonds " +
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
    @GetMapping(value = {FONDS + SLASH + SYSTEM_ID_PARAMETER + SLASH +
            NEW_FONDS,
            FONDS_CREATOR + SLASH + SYSTEM_ID_PARAMETER +
                    SLASH + NEW_FONDS})
    public ResponseEntity<FondsHateoas> getSubFondsTemplate(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of fonds to associate the series " +
                            "with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID
    ) throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fondsService.generateDefaultFonds(systemID));
    }

    // Create a suggested sub-fonds (like a template) object with default values
    // (nothing persisted)
    // GET [contextPath][api]/arkivstruktur/ny-arkiv
    @Operation(
            summary = "Suggests the contents of a new Fonds object",
            description = "Returns a pre-filled Fonds object with values " +
                    "relevant for the logged-in user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "Fonds " +
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
    @GetMapping(value = NEW_FONDS)
    public ResponseEntity<FondsHateoas> getFondsTemplate(
            HttpServletRequest request
    ) throws NikitaException {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fondsService.generateDefaultFonds(null));
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a Fonds
    // PUT [contextPath][api]/arkivstruktur/arkiv
    @Operation(
            summary = "Updates a Fonds object",
            description = "Returns the newly updated Fonds object after it is" +
                    " persisted to the database")
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
                    responseCode = NOT_FOUND_VAL,
                    description = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type Fonds"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = FONDS + SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<FondsHateoas> updateFonds(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of fonds to update.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @Parameter(name = "fonds",
                    description = "Incoming fonds object",
                    required = true)
            @RequestBody Fonds fonds) throws NikitaException {

        validateForUpdate(fonds);
        FondsHateoas fondsHateoas =
                fondsService.handleUpdate(systemID,
                        parseETAG(request.getHeader(ETAG)), fonds);

        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(fondsHateoas.getEntityVersion().toString())
                .body(fondsHateoas);
    }

    // Delete a Fonds identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/arkiv/{systemId}/
    @Operation(
            summary = "Deletes a single Fonds entity identified by systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "Fonds deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = FONDS + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteFondsBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the series to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        fondsService.deleteEntity(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }

    // Delete all Fonds
    // DELETE [contextPath][api]/arkivstruktur/arkiv/
    @Operation(summary = "Deletes all Fonds")
    @ApiResponses(value = {
            @ApiResponse(responseCode = NO_CONTENT_VAL,
                    description = "Deleted all Fonds"),
            @ApiResponse(responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(FONDS)
    public ResponseEntity<String> deleteAllFonds() {
        fondsService.deleteAllByOwnedBy();
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }
}
