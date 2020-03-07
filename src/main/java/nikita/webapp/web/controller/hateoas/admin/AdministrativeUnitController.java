package nikita.webapp.web.controller.hateoas.admin;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.hateoas.admin.AdministrativeUnitHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.hateoas.interfaces.admin.IAdministrativeUnitHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.admin.IAdministrativeUnitService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping(value = HREF_BASE_ADMIN + SLASH,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class AdministrativeUnitController extends NoarkController {

    private IAdministrativeUnitService administrativeUnitService;
    private IAdministrativeUnitHateoasHandler administrativeUnitHateoasHandler;

    public AdministrativeUnitController(IAdministrativeUnitService administrativeUnitService,
                                        IAdministrativeUnitHateoasHandler administrativeUnitHateoasHandler) {
        this.administrativeUnitService = administrativeUnitService;
        this.administrativeUnitHateoasHandler = administrativeUnitHateoasHandler;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new administrativtenhet
    // POST [contextPath][api]/admin/ny-administrativtenhet
    @ApiOperation(value = "Persists a new AdministrativeUnit object", notes = "Returns the newly" +
            " created AdministrativeUnit object after it is persisted to the database",
            response = AdministrativeUnit.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "AdministrativeUnit " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = AdministrativeUnit.class),
            @ApiResponse(code = 201, message = "AdministrativeUnit " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = AdministrativeUnit.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR),
            @ApiResponse(code = 501, message = API_MESSAGE_NOT_IMPLEMENTED)})
    @Counted

    @PostMapping(value = NEW_ADMINISTRATIVE_UNIT)
    public ResponseEntity<AdministrativeUnitHateoas> createAdministrativeUnit(
            HttpServletRequest request,
            @RequestBody AdministrativeUnit administrativeUnit)
            throws NikitaException {
        administrativeUnitService.createNewAdministrativeUnitBySystem(administrativeUnit);
        AdministrativeUnitHateoas adminHateoas = new AdministrativeUnitHateoas(administrativeUnit);
        administrativeUnitHateoasHandler.addLinks(adminHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(administrativeUnit.getVersion().toString())
                .body(adminHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all administrativeUnit
    // GET [contextPath][api]/admin/administrativtenhet/
    @ApiOperation(value = "Retrieves all AdministrativeUnit ", response = AdministrativeUnit.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "AdministrativeUnit found",
                    response = AdministrativeUnit.class),
            @ApiResponse(code = 404, message = "No AdministrativeUnit found"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = ADMINISTRATIVE_UNIT)
    public ResponseEntity<AdministrativeUnitHateoas> findAll(HttpServletRequest request) {
        AdministrativeUnitHateoas adminHateoas = new AdministrativeUnitHateoas(
                (List<INoarkEntity>) (List) administrativeUnitService.findAll());
        administrativeUnitHateoasHandler.addLinks(adminHateoas, new Authorisation());

        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(adminHateoas);
    }

    // Retrieves a given administrativeUnit identified by a systemId
    // GET [contextPath][api]/admin/administrativtenhet/{systemId}/
    @ApiOperation(value = "Gets administrativeUnit identified by its systemId", notes = "Returns the requested " +
            " administrativeUnit object", response = AdministrativeUnit.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "AdministrativeUnit " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = AdministrativeUnit.class),
            @ApiResponse(code = 201, message = "AdministrativeUnit " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = AdministrativeUnit.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR),
            @ApiResponse(code = 501, message = API_MESSAGE_NOT_IMPLEMENTED)})
    @Counted
    @GetMapping(value = ADMINISTRATIVE_UNIT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<AdministrativeUnitHateoas> findBySystemId(@PathVariable(SYSTEM_ID) final String systemId,
                                                                    HttpServletRequest request) {
        AdministrativeUnit administrativeUnit =
                administrativeUnitService.findBySystemId(UUID.fromString(systemId));
        AdministrativeUnitHateoas adminHateoas = new AdministrativeUnitHateoas(administrativeUnit);
        administrativeUnitHateoasHandler.addLinks(adminHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(administrativeUnit.getVersion().toString())
                .body(adminHateoas);
    }

    // Create a suggested administrativeUnit(like a template) with default values (nothing persisted)
    // GET [contextPath][api]/admin/ny-administrativtenhet
    @ApiOperation(value = "Creates a suggested AdministrativeUnit", response = AdministrativeUnit.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "AdministrativeUnit codes found",
                    response = AdministrativeUnit.class),
            @ApiResponse(code = 404, message = "No AdministrativeUnit found"),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = NEW_ADMINISTRATIVE_UNIT)
    public ResponseEntity<AdministrativeUnitHateoas> getAdministrativeUnitTemplate(HttpServletRequest request) {
        AdministrativeUnit administrativeUnit = new AdministrativeUnit();
        administrativeUnit.setShortName("kortnavn på administrativtenhet");
        administrativeUnit.setAdministrativeUnitName("Formell navn på administrativtenhet");
        AdministrativeUnitHateoas adminHateoas = new AdministrativeUnitHateoas(administrativeUnit);
        administrativeUnitHateoasHandler.addLinksOnTemplate(adminHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(adminHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a administrativtenhet
    // PUT [contextPath][api]/admin/administrativtenhet/{systemID}
    @ApiOperation(value = "Updates a AdministrativeUnit object", notes = "Returns the newly" +
            " updated AdministrativeUnit object after it is persisted to the database",
            response = AdministrativeUnit.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "AdministrativeUnit " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = AdministrativeUnit.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404, message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 409, message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @PutMapping(value = ADMINISTRATIVE_UNIT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<AdministrativeUnitHateoas> updateAdministrativeUnit(
            HttpServletRequest request,
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of documentDescription to update.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID,
            @ApiParam(name = "administrativeUnit",
                    value = "Incoming administrativeUnit object",
                    required = true)
            @RequestBody AdministrativeUnit administrativeUnit)
            throws NikitaException {
        administrativeUnitService.update(systemID,
                parseETAG(request.getHeader(ETAG)), administrativeUnit);
        AdministrativeUnitHateoas adminHateoas = new AdministrativeUnitHateoas(administrativeUnit);
        administrativeUnitHateoasHandler.addLinks(adminHateoas, new Authorisation());
        return ResponseEntity.status(HttpStatus.OK)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(adminHateoas);
    }

    // Delete all AdministrativeUnit
    // DELETE [contextPath][api]/admin/administrativtenhet/
    @ApiOperation(value = "Deletes all AdministrativeUnit",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204,
                    message = "Deleted all AdministrativeUnit",
                    response = String.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = ADMINISTRATIVE_UNIT)
    public ResponseEntity<String> deleteAllAdministrativeUnit() {
        administrativeUnitService.deleteAllByOwnedBy();
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }

    // Delete an AdministrativeUnit identified by systemID
    // DELETE [contextPath][api]/admin/administrativtenhet/{systemId}/
    @ApiOperation(value = "Delete an AdministrativeUnit object",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204,
                    message = "Delete  AdministrativeUnit object",
                    response = String.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = ADMINISTRATIVE_UNIT + SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteAdministrativeUnit(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of AdministrativeUnit to delete.",
                    required = true)
            @PathVariable(SYSTEM_ID) String systemID) {
        administrativeUnitService.deleteEntity(systemID);
        return ResponseEntity.status(NO_CONTENT).
                body(DELETE_RESPONSE);
    }
}
