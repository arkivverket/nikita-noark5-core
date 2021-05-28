package nikita.webapp.web.controller.hateoas.secondary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.secondary.CrossReferenceHateoas;
import nikita.common.model.noark5.v5.secondary.CrossReference;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.secondary.ICrossReferenceService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = HREF_BASE_FONDS_STRUCTURE + SLASH + CROSS_REFERENCE,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class CrossReferenceHateoasController
        extends NoarkController {

    private final ICrossReferenceService crossReferenceService;

    public CrossReferenceHateoasController(
            ICrossReferenceService crossReferenceService) {
        this.crossReferenceService = crossReferenceService;
    }

    // API - All GET Requests (CRUD - READ)
    // GET [contextPath][api]/arkivstruktur/kryssreferanse/
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/kryssreferanse/
    @Operation(summary = "Retrieves multiple CrossReference entities limited " +
            "by ownership rights")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CrossReference found"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping()
    public ResponseEntity<CrossReferenceHateoas> findAllCrossReference() {
        CrossReferenceHateoas crossReferenceHateoas =
                crossReferenceService.findAll();
        return ResponseEntity.status(OK)
                .body(crossReferenceHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/kryssreferanse/{systemId}
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/kryssreferanse/
    @Operation(summary = "Retrieves a single CrossReference entity given a " +
            "systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CrossReference returned"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<CrossReferenceHateoas> findCrossReferenceBySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the CrossReference to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        CrossReferenceHateoas crossReferenceHateoas =
                crossReferenceService.findBySystemId(systemID);
        return ResponseEntity.status(OK)
                .body(crossReferenceHateoas);
    }

    // PUT [contextPath][api]/arkivstruktur/kryssreferanse/{systemId}
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/kryssreferanse/
    @Operation(summary = "Updates a CrossReference identified by a given systemId",
            description = "Returns the newly updated nationalIdentifierPerson")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CrossReference " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "CrossReference " +
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
                            " of type CrossReference"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<CrossReferenceHateoas> updateCrossReferenceBySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of CrossReference to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "CrossReference",
                    description = "Incoming CrossReference object",
                    required = true)
            @RequestBody CrossReference crossReference) throws NikitaException {
        CrossReferenceHateoas crossReferenceHateoas =
                crossReferenceService.updateCrossReferenceBySystemId(systemID,
                        parseETAG(request.getHeader(ETAG)), crossReference);
        return ResponseEntity.status(OK)
                .body(crossReferenceHateoas);
    }

    // DELETE [contextPath][api]/arkivstruktur/kryssreferanse/{systemID}/
    @Operation(summary = "Deletes a single CrossReference entity identified by " +
            "systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "CrossReference deleted"),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @DeleteMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteCrossReferenceBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the crossReference to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        crossReferenceService.deleteCrossReferenceBySystemId(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
