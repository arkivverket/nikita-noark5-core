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
import nikita.webapp.hateoas.interfaces.IFondsCreatorHateoasHandler;
import nikita.webapp.hateoas.interfaces.IFondsHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IFondsCreatorService;
import nikita.webapp.web.events.AfterNoarkEntityCreatedEvent;
import nikita.webapp.web.events.AfterNoarkEntityDeletedEvent;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = HREF_BASE_FONDS_STRUCTURE + SLASH,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class FondsCreatorHateoasController
        extends NoarkController {

    private final IFondsCreatorService fondsCreatorService;
    private final IFondsCreatorHateoasHandler fondsCreatorHateoasHandler;
    private final IFondsHateoasHandler fondsHateoasHandler;
    private final ApplicationEventPublisher applicationEventPublisher;

    public FondsCreatorHateoasController(
            IFondsCreatorService fondsCreatorService,
            IFondsCreatorHateoasHandler fondsCreatorHateoasHandler,
            IFondsHateoasHandler fondsHateoasHandler,
            ApplicationEventPublisher applicationEventPublisher) {
        this.fondsCreatorService = fondsCreatorService;
        this.fondsCreatorHateoasHandler = fondsCreatorHateoasHandler;
        this.fondsHateoasHandler = fondsHateoasHandler;
        this.applicationEventPublisher = applicationEventPublisher;
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
            HttpServletRequest request,
            @Parameter(name = "FondsCreator",
                    description = "Incoming FondsCreator object",
                    required = true)
            @RequestBody FondsCreator fondsCreator)
            throws NikitaException {
        fondsCreatorService.createNewFondsCreator(fondsCreator);
        FondsCreatorHateoas fondsCreatorHateoas =
                new FondsCreatorHateoas(fondsCreator);
        fondsCreatorHateoasHandler.addLinks(
                fondsCreatorHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(this, fondsCreator));
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(fondsCreator.getVersion().toString())
                .body(fondsCreatorHateoas);
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
            HttpServletRequest request,
            @Parameter(name = "systemId",
                    description = "systemID of FondsCreator to associate the " +
                            "Fonds with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @Parameter(name = "fonds",
                    description = "Incoming fonds object",
                    required = true)
            @RequestBody Fonds fonds) throws NikitaException {
        fondsCreatorService
                .createFondsAssociatedWithFondsCreator(systemID, fonds);
        FondsHateoas fondsHateoas = new FondsHateoas(fonds);
        fondsHateoasHandler.addLinks(fondsHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this, fonds));
        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(fonds.getVersion().toString())
                .body(fondsHateoas);
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
    findOne(HttpServletRequest request,
            @Parameter(name = "systemId",
                    description = "systemId of FondsCreator to retrieve.",
                    required = true)
            @PathVariable(SYSTEM_ID) final String fondsCreatorSystemId) {
        FondsCreator fondsCreator =
                fondsCreatorService.findBySystemId(fondsCreatorSystemId);
        FondsCreatorHateoas fondsCreatorHateoas =
                new FondsCreatorHateoas(fondsCreator);
        fondsCreatorHateoasHandler.addLinks(
                fondsCreatorHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(fondsCreator.getVersion().toString())
                .body(fondsCreatorHateoas);
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
    public ResponseEntity<FondsCreatorHateoas> findAllFondsCreator(
            HttpServletRequest request) {
        String ownedBy = SecurityContextHolder.getContext().getAuthentication()
                .getName();
        FondsCreatorHateoas fondsCreatorHateoas = new
                FondsCreatorHateoas(List.copyOf(
                fondsCreatorService.findByOwnedBy(ownedBy)));
        fondsCreatorHateoasHandler.addLinks(fondsCreatorHateoas,
                new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fondsCreatorHateoas);
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
                       @Parameter(name = "systemId",
                               description = "systemId of FondsCreator to " +
                                       "retrieve.",
                               required = true)
                       @PathVariable(SYSTEM_ID) final String systemID) {
        FondsCreator createdFonds = fondsCreatorService
                .handleUpdate(systemID, parseETAG(request.getHeader(ETAG)),
                        fondsCreator);
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityUpdatedEvent(this, createdFonds));
        FondsCreatorHateoas fondsCreatorHateoas =
                new FondsCreatorHateoas(createdFonds);
        fondsCreatorHateoasHandler.addLinks(
                fondsCreatorHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(createdFonds.getVersion().toString())
                .body(fondsCreatorHateoas);
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
    public ResponseEntity<FondsCreatorHateoas> getFondsCreatorTemplate(
            HttpServletRequest request)
            throws NikitaException {
        FondsCreator suggestedFondsCreator = new FondsCreator();
        FondsCreatorHateoas fondsCreatorHateoas =
                new FondsCreatorHateoas(suggestedFondsCreator);
        fondsHateoasHandler.addLinksOnTemplate(
                fondsCreatorHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fondsCreatorHateoas);
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
            @PathVariable(SYSTEM_ID) final String systemID) {
        return fondsCreatorService.findFondsAssociatedWithFondsCreator(systemID);
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
            @PathVariable(SYSTEM_ID) final String seriesSystemId) {

        FondsCreator fondsCreator = fondsCreatorService
                .findBySystemId(seriesSystemId);
        fondsCreatorService.deleteEntity(seriesSystemId);
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityDeletedEvent(this, fondsCreator));
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
