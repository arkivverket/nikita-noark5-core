package nikita.webapp.web.controller.hateoas;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.FondsCreator;
import nikita.common.model.noark5.v5.hateoas.FondsCreatorHateoas;
import nikita.common.model.noark5.v5.hateoas.FondsHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IFondsCreatorHateoasHandler;
import nikita.webapp.hateoas.interfaces.IFondsHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IFondsCreatorService;
import nikita.webapp.web.events.AfterNoarkEntityCreatedEvent;
import nikita.webapp.web.events.AfterNoarkEntityDeletedEvent;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping(value = HREF_BASE_FONDS_STRUCTURE + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class FondsCreatorHateoasController
        extends NoarkController {

    private IFondsCreatorService fondsCreatorService;
    private IFondsCreatorHateoasHandler fondsCreatorHateoasHandler;
    private IFondsHateoasHandler fondsHateoasHandler;
    private ApplicationEventPublisher applicationEventPublisher;

    public FondsCreatorHateoasController(IFondsCreatorService fondsCreatorService,
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
    @ApiOperation(value = "Persists a FondsCreator object", notes = "Returns the newly" +
            " created FondsCreator object after it is persisted to the database", response = FondsCreatorHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FondsCreator " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FondsCreatorHateoas.class),
            @ApiResponse(code = 201, message = "FondsCreator " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = FondsCreatorHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_PARENT_DOES_NOT_EXIST + " of type FondsCreator"),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PostMapping(value = NEW_FONDS_CREATOR,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<FondsCreatorHateoas> createFondsCreator(
            HttpServletRequest request,
            @ApiParam(name = "FondsCreator",
                    value = "Incoming FondsCreator object",
                    required = true)
            @RequestBody FondsCreator fondsCreator) throws NikitaException {
        fondsCreatorService.createNewFondsCreator(fondsCreator);
        FondsCreatorHateoas fondsCreatorHateoas = new FondsCreatorHateoas(fondsCreator);
        fondsCreatorHateoasHandler.addLinks(fondsCreatorHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(new AfterNoarkEntityCreatedEvent(this, fondsCreator));

        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(fondsCreator.getVersion().toString())
                .body(fondsCreatorHateoas);
    }

    // Create a new fonds
    // POST [contextPath][api]/arkivstruktur/arkivskaper/{systemID}/ny-arkiv
    @ApiOperation(value = "Persists a new Fonds associated with a FondsCreator", notes = "Returns the newly" +
            " created Fonds after it is associated with the Fonds and persisted to the database",
            response = FondsCreatorHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Fonds " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FondsHateoas.class),
            @ApiResponse(code = 201, message = "Fonds " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = FondsHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PostMapping(value = FONDS_CREATOR + SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_FONDS,
                 consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<FondsHateoas> createFondsAssociatedWithFondsCreator(
            HttpServletRequest request,
            @ApiParam(name = "systemId",
                    value = "systemId of FondsCreator to associate the Fonds with.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @ApiParam(name = "fonds",
                    value = "Incoming fonds object",
                    required = true)
            @RequestBody Fonds fonds) throws NikitaException {
        fondsCreatorService.createFondsAssociatedWithFondsCreator(systemID, fonds);
        FondsHateoas fondsHateoas = new FondsHateoas(fonds);
        fondsHateoasHandler.addLinks(fondsHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(new AfterNoarkEntityUpdatedEvent(this, fonds));
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(fonds.getVersion().toString())
                .body(fondsHateoas);
    }

    // API - All GET Requests (CRUD - READ)

    // Get a FondsCreator identified by a systemId
    // GET [contextPath][api]/arkivstruktur/arkivskaper/{systemId}
    @ApiOperation(value = "Retrieves a single FondsCreator entity given a systemId", response = FondsCreator.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FondsCreator returned", response = FondsCreator.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = FONDS_CREATOR + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<FondsCreatorHateoas> findOne(HttpServletRequest request,
                                                       @ApiParam(name = "systemId",
                                                               value = "systemId of FondsCreator to retrieve.",
                                                               required = true)
                                                       @PathVariable(SYSTEM_ID) final String fondsCreatorSystemId) {
        FondsCreator fondsCreator = fondsCreatorService.findBySystemId(fondsCreatorSystemId);
        if (fondsCreator == null) {
            throw new NoarkEntityNotFoundException("Could not find FondsCreator object with systemID " +
                    fondsCreatorSystemId);
        }
        FondsCreatorHateoas fondsCreatorHateoas = new FondsCreatorHateoas(fondsCreator);
        fondsCreatorHateoasHandler.addLinks(fondsCreatorHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(fondsCreator.getVersion().toString())
                .body(fondsCreatorHateoas);
    }

    // Get all FondsCreator
    // GET [contextPath][api]/arkivstruktur/arkivskaper/
    @ApiOperation(value = "Retrieves multiple FondsCreator entities limited by ownership rights", notes = "The field skip" +
            "tells how many FondsCreator rows of the result set to ignore (starting at 0), while  top tells how many rows" +
            " after skip to return. Note if the value of top is greater than system value " +
            " nikita-noark5-core.pagination.maxPageSize, then nikita-noark5-core.pagination.maxPageSize is used. ",
            response = FondsCreatorHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FondsCreator found",
                    response = FondsCreatorHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = FONDS_CREATOR)
    public ResponseEntity<FondsCreatorHateoas> findAllFondsCreator(
            HttpServletRequest request,
            @RequestParam(name = "top", required = false) Integer top,
            @RequestParam(name = "skip", required = false) Integer skip) {
        String ownedBy = SecurityContextHolder.getContext().getAuthentication()
                .getName();
        FondsCreatorHateoas fondsCreatorHateoas = new
                FondsCreatorHateoas((List<INoarkEntity>) (List)
                fondsCreatorService.findByOwnedBy(ownedBy));
        fondsCreatorHateoasHandler.addLinks(fondsCreatorHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fondsCreatorHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)

    // Updates a FondsCreator identified by a systemId
    // PUT [contextPath][api]/arkivstruktur/arkivskaper/{systemId}
    @ApiOperation(value = "Updates a FondsCreator identified by a systemId with new values",
            response = FondsCreator.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FondsCreator updated", response = FondsCreator.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PutMapping(value = FONDS_CREATOR + SLASH + SYSTEM_ID_PARAMETER,
                consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<FondsCreatorHateoas> updateFondsCreator(HttpServletRequest request,
                                                                  @ApiParam(name = "fondsCreator",
                                                                          value = "Incoming fondsCreator object",
                                                                          required = true)
                                                                  @RequestBody FondsCreator fondsCreator,
                                                                  @ApiParam(name = "systemId",
                                                                          value = "systemId of FondsCreator to retrieve.",
                                                                          required = true)
                                                                      @PathVariable(SYSTEM_ID) final String systemID) {
        FondsCreator createdFonds = fondsCreatorService.handleUpdate(systemID, parseETAG(request.getHeader(ETAG)),
                fondsCreator);
        applicationEventPublisher.publishEvent(new AfterNoarkEntityUpdatedEvent(this, createdFonds));
        FondsCreatorHateoas fondsCreatorHateoas = new FondsCreatorHateoas(createdFonds);
        fondsCreatorHateoasHandler.addLinks(fondsCreatorHateoas, new Authorisation());

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(createdFonds.getVersion().toString())
                .body(fondsCreatorHateoas);
    }

    // Create a suggested FondsCreator (like a template) object with default values (nothing persisted)
    // GET [contextPath][api]/arkivstruktur/arkiv/{systemID}/ny-arkivskaper
    // GET [contextPath][api]/arkivstruktur/ny-arkivskaper
    @ApiOperation(value = "Suggests the contents of a new FondsCreator", notes = "Returns a pre-filled FondsCreator" +
            " with values relevant for the logged-in user", response = FondsCreatorHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FondsCreator " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = FondsCreatorHateoas.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @GetMapping(value = {NEW_FONDS_CREATOR, FONDS + SLASH + SYSTEM_ID_PARAMETER + SLASH + NEW_FONDS_CREATOR})
    public ResponseEntity<FondsCreatorHateoas> getFondsCreatorTemplate(
            HttpServletRequest request
    ) throws NikitaException {
        FondsCreator suggestedFondsCreator = new FondsCreator();
        // TODO Defaults should be replaced with configurable data
        // based on whoever is logged in
        FondsCreatorHateoas fondsCreatorHateoas = new FondsCreatorHateoas(suggestedFondsCreator);
        fondsHateoasHandler.addLinksOnTemplate(fondsCreatorHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(fondsCreatorHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/arkivskaper/{systemId}/arkiv
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkiv/
    @ApiOperation(value = "Retrieve the Fonds associated with the " +
                  "Fonds Creator identified by the given systemId",
                  response = FondsHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Fonds returned",
                    response = FondsHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = FONDS_CREATOR + SLASH + SYSTEM_ID_PARAMETER + SLASH + FONDS)
    public ResponseEntity<FondsHateoas> findParentFondsAssociatedWithSeries(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the Series ",
                    required = true)
            @PathVariable(SYSTEM_ID) final String systemID) {
        return fondsCreatorService.findFondsAssociatedWithFondsCreator(systemID);
    }

    // Delete a FondsCreator identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/arkivskaper/{systemId}/
    @ApiOperation(value = "Deletes a single FondsCreator entity identified by" +
            " systemID", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204,
                    message = "FondsCreator deleted",
                    response = String.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = FONDS_CREATOR + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteSeriesBySystemId(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of the fondsCreator to delete",
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
    @ApiOperation(value = "Deletes all FondsCreator", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Deleted all FondsCreator",
                    response = String.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = FONDS_CREATOR)
    public ResponseEntity<String> deleteAllFondsCreator() {
        fondsCreatorService.deleteAllByOwnedBy();
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }
}
