package nikita.webapp.web.controller.hateoas.secondary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.secondary.ScreeningMetadataHateoas;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.secondary.IScreeningMetadataService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = HREF_BASE_FONDS_STRUCTURE + SLASH + SCREENING_METADATA,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class ScreeningMetadataHateoasController
        extends NoarkController {

    private final IScreeningMetadataService screeningMetadataService;

    public ScreeningMetadataHateoasController(
            IScreeningMetadataService screeningMetadataService) {
        this.screeningMetadataService = screeningMetadataService;
    }

    // API - All GET Requests (CRUD - READ)

    // GET [contextPath][api]/arkivstruktur/skjermingmetadata/
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/skjermingmetadata/
    @Operation(summary = "Retrieves multiple ScreeningMetadata entities limited " +
            "by ownership rights")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "ScreeningMetadata found"),
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
    public ResponseEntity<ScreeningMetadataHateoas> findAllScreeningMetadata() {
        ScreeningMetadataHateoas screeningMetadataHateoas =
                screeningMetadataService.findAllByOwner();
        return ResponseEntity.status(OK)
                .body(screeningMetadataHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/skjermingmetadata/{systemId}
    @Operation(summary = "Retrieves a single ScreeningMetadata entity given a " +
            "systemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "ScreeningMetadata returned"),
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
    public ResponseEntity<ScreeningMetadataHateoas>
    findScreeningMetadataBySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the ScreeningMetadata to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        ScreeningMetadataHateoas screeningMetadataHateoas =
                screeningMetadataService.findBySystemId(systemID);
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(screeningMetadataHateoas.getEntityVersion().toString())
                .body(screeningMetadataHateoas);
    }

    // PUT [contextPath][api]/arkivstruktur/skjermingmetadata/{systemId}
    @Operation(summary = "Updates a ScreeningMetadata identified by a given " +
            "systemId",
            description = "Returns the newly updated nationalIdentifierPerson")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "ScreeningMetadata " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "ScreeningMetadata " +
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
                            " of type ScreeningMetadata"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<ScreeningMetadataHateoas>
    updateScreeningMetadataBySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemId of ScreeningMetadata to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "ScreeningMetadata",
                    description = "Incoming ScreeningMetadata object",
                    required = true)
            @RequestBody Metadata screeningMetadata)
            throws NikitaException {
        ScreeningMetadataHateoas screeningMetadataHateoas =
                screeningMetadataService.updateScreeningMetadataBySystemId(
                        systemID, parseETAG(request.getHeader(ETAG)),
                        screeningMetadata);
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(screeningMetadataHateoas.getEntityVersion().toString())
                .body(screeningMetadataHateoas);
    }

    // DELETE [contextPath][api]/arkivstruktur/skjermingmetadata/{systemID}/
    @Operation(summary = "Deletes a single ScreeningMetadata entity identified by " +
            "systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "ScreeningMetadata deleted"),
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
    public ResponseEntity<String> deleteScreeningMetadataBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the screeningMetadata to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        screeningMetadataService.deleteScreeningMetadataBySystemId(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
