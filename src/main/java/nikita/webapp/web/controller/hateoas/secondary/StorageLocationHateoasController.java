package nikita.webapp.web.controller.hateoas.secondary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.secondary.StorageLocationHateoas;
import nikita.common.model.noark5.v5.secondary.StorageLocation;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.secondary.IStorageLocationService;
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
@RequestMapping(value = HREF_BASE_FONDS_STRUCTURE + SLASH + STORAGE_LOCATION,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class StorageLocationHateoasController
        extends NoarkController {

    private final IStorageLocationService storageLocationService;

    public StorageLocationHateoasController(
            IStorageLocationService storageLocationService) {
        this.storageLocationService = storageLocationService;
    }

    // API - All GET Requests (CRUD - READ)
    // GET [contextPath][api]/arkivstruktur/oppbevaringssted/
    // https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/oppbevaringssted/
    @Operation(summary = "Retrieves multiple StorageLocation entities limited " +
            "by ownership rights")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "StorageLocation found"),
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
    public ResponseEntity<StorageLocationHateoas> findAllStorageLocation() {
        StorageLocationHateoas storageLocationHateoas =
                storageLocationService.findAll();
        return ResponseEntity.status(OK)
                .body(storageLocationHateoas);
    }

    // GET [contextPath][api]/arkivstruktur/oppbevaringssted/{systemID}
    @Operation(summary = "Retrieves a single StorageLocation entity given a systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "StorageLocation returned"),
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
    public ResponseEntity<StorageLocationHateoas> findStorageLocationBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the StorageLocation to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        StorageLocationHateoas storageLocationHateoas =
                storageLocationService.findBySystemId(systemID);
        return ResponseEntity.status(OK)
                .body(storageLocationHateoas);
    }

    // PUT [contextPath][api]/arkivstruktur/oppbevaringssted/{systemID}
    @Operation(summary = "Updates a StorageLocation identified by a given systemID",
            description = "Returns the newly updated nationalIdentifierPerson")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "StorageLocation " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED),
            @ApiResponse(
                    responseCode = CREATED_VAL,
                    description = "StorageLocation " +
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
                            " of type StorageLocation"),
            @ApiResponse(
                    responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PutMapping(value = SLASH + SYSTEM_ID_PARAMETER,
            consumes = NOARK5_V5_CONTENT_TYPE_JSON)
    public ResponseEntity<StorageLocationHateoas> updateStorageLocationBySystemId(
            HttpServletRequest request,
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of StorageLocation to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "StorageLocation",
                    description = "Incoming StorageLocation object",
                    required = true)
            @RequestBody StorageLocation storageLocation) throws NikitaException {
        StorageLocationHateoas storageLocationHateoas =
                storageLocationService.updateStorageLocationBySystemId(systemID,
                        parseETAG(request.getHeader(ETAG)), storageLocation);
        return ResponseEntity.status(OK)
                .body(storageLocationHateoas);
    }

    // DELETE [contextPath][api]/arkivstruktur/oppbevaringssted/{systemID}/
    @Operation(summary = "Deletes a single StorageLocation entity identified by " +
            "systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_VAL,
                    description = "StorageLocation deleted"),
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
    public ResponseEntity<String> deleteStorageLocationBySystemId(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of the storageLocation to delete",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        storageLocationService.deleteStorageLocationBySystemId(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
