package nikita.webapp.web.controller.hateoas.metadata;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nikita.common.model.nikita.PatchMerge;
import nikita.common.model.noark5.v5.hateoas.metadata.BSMMetadataHateoas;
import nikita.common.model.noark5.v5.md_other.BSMMetadata;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.IBSMMetadataService;
import nikita.webapp.web.controller.hateoas.NoarkController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static nikita.common.config.Constants.*;
import static nikita.common.config.HATEOASConstants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = HREF_BASE_METADATA + SLASH + BSM_DEF,
        produces = NOARK5_V5_CONTENT_TYPE_JSON)
public class BSMMetadataController
        extends NoarkController {

    private final IBSMMetadataService bsmMetadataService;

    public BSMMetadataController(IBSMMetadataService bsmMetadataService) {
        this.bsmMetadataService = bsmMetadataService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new BSMMetadata
    // POST [contextPath][api]/metadata/ny-virksomhetsspesifikkeMetadata
    @Operation(summary = "Persists a new BSMMetadata object",
            description = "Returns the newly created BSMMetadata object after "
                    + "it is persisted to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED_VAL,
                    description = "BSMMetadata " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED),
            @ApiResponse(responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(responseCode = NOT_FOUND_VAL,
                    description = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PostMapping
    public ResponseEntity<BSMMetadataHateoas> createBSMMetadata(
            @RequestBody BSMMetadata bsmMetadata) {
        return bsmMetadataService.save(bsmMetadata);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all BSMMetadata of a given type (entityName)
    // GET [contextPath][api]/metadata/virksomhetsspesifikkeMetadata
    @Operation(summary = "Retrieves all BSMMetadataEntity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK_VAL,
                    description = "BSMMetadataEntity codes found"),
            @ApiResponse(responseCode = NOT_FOUND_VAL,
                    description = "No BSMMetadataEntity found"),
            @ApiResponse(responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping
    public ResponseEntity<BSMMetadataHateoas> findAll() {
        return bsmMetadataService.findAll();
    }

    // Create a suggested fondsStatus(like a template) with default values
    // (nothing persisted)
    // GET [contextPath][api]/metadata/ny-BSMMetadata
    @Operation(summary = "Creates a suggested BSMMetadata")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK_VAL,
                    description = "BSMMetadata codes found"),
            @ApiResponse(responseCode = NOT_FOUND_VAL,
                    description = "No BSMMetadataEntity found"),
            @ApiResponse(responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = NEW + "-" + BSM_DEF)
    public ResponseEntity<String>
    getBSMMetadataTemplate(HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body("{}");
    }

    // Update a BSMMetadata with given values
    // PATCH [contextPath][api]/metadata/virksomhetsspesifikkeMetadata/{systemId}
    @Operation(summary = "Updates a BSMMetadata identified by a given systemId",
            description = "Returns the newly updated bsmMetadata")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK_VAL,
                    description = "BSMMetadata OK"),
            @ApiResponse(responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(responseCode = NOT_FOUND_VAL,
                    description = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type BSMMetadata"),
            @ApiResponse(responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<BSMMetadataHateoas> getBSMMetadata(
            @Parameter(name = SYSTEM_ID,
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID)
            throws NikitaException {
        return bsmMetadataService.find(systemID);
    }

    // Update a BSMMetadata identified by a UUID
    // PATCH [contextPath][api]/metadata/virksomhetsspesifikkeMetadata/{systemId}
    @Operation(summary = "Updates a BSMMetadata identified by a given systemId",
            description = "Returns the newly updated bsmMetadata")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK_VAL,
                    description = "BSMMetadata OK"),
            @ApiResponse(responseCode = UNAUTHORIZED_VAL,
                    description = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(responseCode = FORBIDDEN_VAL,
                    description = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(responseCode = NOT_FOUND_VAL,
                    description = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type BSMMetadata"),
            @ApiResponse(responseCode = CONFLICT_VAL,
                    description = API_MESSAGE_CONFLICT),
            @ApiResponse(responseCode = INTERNAL_SERVER_ERROR_VAL,
                    description = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @PatchMapping(value = SLASH + SYSTEM_ID_PARAMETER,
            consumes = CONTENT_TYPE_JSON_MERGE_PATCH)
    public ResponseEntity<BSMMetadataHateoas> patchBSMMetadata(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of BSM to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @Parameter(name = "PatchMerge",
                    description = "Incoming merge payload",
                    required = true)
            @RequestBody PatchMerge patchMerge) throws NikitaException {
        return (ResponseEntity<BSMMetadataHateoas>)
                bsmMetadataService.handleUpdateRfc7396(systemID, patchMerge);
    }

    // API - All DELETE Requests (CRUD - DELETE)
    // Delete a BSMMetadata
    // DELETE [contextPath][api]/metadata/BSMMetadata/{systemID}
    @Operation(
            summary = "Deletes a single BSMMetadata entity identified by the " +
                    "given systemID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = NO_CONTENT_VAL,
                    description = "BSMMetadata entity deleted"),
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
    public ResponseEntity<String> deleteBSMMetadata(
            @Parameter(name = SYSTEM_ID,
                    description = "systemID of bsmMetadata object to delete.",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        bsmMetadataService.deleteEntity(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
