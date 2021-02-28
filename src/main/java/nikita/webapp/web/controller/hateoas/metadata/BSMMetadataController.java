package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @ApiOperation(value = "Persists a new BSMMetadata object",
            notes = "Returns the newly created BSMMetadata object after "
                    + "it is persisted to the database",
            response = BSMMetadata.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201,
                    message = "BSMMetadata " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = BSMMetadata.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(code = 400,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping
    public ResponseEntity<BSMMetadataHateoas> createBSMMetadata(
            @RequestBody BSMMetadata bsmMetadata) {
        return bsmMetadataService.save(bsmMetadata);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all BSMMetadata of a given type (entityname)
    // GET [contextPath][api]/metadata/virksomhetsspesifikkeMetadata
    @ApiOperation(value = "Retrieves all BSMMetadataEntity",
            response = BSMMetadata.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "BSMMetadataEntity codes found",
                    response = BSMMetadata.class),
            @ApiResponse(code = 404,
                    message = "No BSMMetadataEntity found"),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping
    public ResponseEntity<BSMMetadataHateoas> findAll() {
        return bsmMetadataService.findAll();
    }

    // Create a suggested fondsStatus(like a template) with default values (nothing persisted)
    // GET [contextPath][api]/metadata/ny-BSMMetadata
    @ApiOperation(value = "Creates a suggested BSMMetadata",
            response = BSMMetadata.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "BSMMetadata codes found",
                    response = BSMMetadata.class),
            @ApiResponse(code = 404,
                    message = "No BSMMetadataEntity found"),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = NEW + "-" + BSM_DEF)
    public ResponseEntity<String>
    getBSMMetadataTemplate(HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body("{}");
    }

    // Update a BSMMetadata with given values
    // PATCH [contextPath][api]/metadata/virksomhetsspesifikkeMetadata/{systemId}
    @ApiOperation(value = "Updates a BSMMetadata identified by a given systemId",
            notes = "Returns the newly updated bsmMetadata",
            response = BSMMetadataHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "BSMMetadata OK",
                    response = BSMMetadataHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type BSMMetadata"),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<BSMMetadataHateoas> getBSMMetadata(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemId of BSM to retrieve",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID)
            throws NikitaException {
        return bsmMetadataService.find(systemID);
    }

    // Update a BSMMetadata identified by a UUID
    // PATCH [contextPath][api]/metadata/virksomhetsspesifikkeMetadata/{systemId}
    @ApiOperation(value = "Updates a BSMMetadata identified by a given systemId",
            notes = "Returns the newly updated bsmMetadata",
            response = BSMMetadataHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "BSMMetadata OK",
                    response = BSMMetadataHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type BSMMetadata"),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PatchMapping(value = SLASH + SYSTEM_ID_PARAMETER,
            consumes = CONTENT_TYPE_JSON_MERGE_PATCH)
    public ResponseEntity<BSMMetadataHateoas> patchBSMMetadata(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of BSM to update",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID,
            @ApiParam(name = "PatchMerge",
                    value = "Incoming merge payload",
                    required = true)
            @RequestBody PatchMerge patchMerge) throws NikitaException {
        return (ResponseEntity<BSMMetadataHateoas>)
                bsmMetadataService.handleUpdateRfc7396(systemID, patchMerge);
    }

    // API - All DELETE Requests (CRUD - DELETE)
    // Delete a BSMMetadata
    // DELETE [contextPath][api]/metadata/BSMMetadata/{systemID}
    @ApiOperation(
            value = "Deletes a single BSMMetadata entity identified by the " +
                    "given systemID",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 204,
                    message = "BSMMetadata entity deleted",
                    response = String.class),
            @ApiResponse(
                    code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @DeleteMapping(value = SLASH + SYSTEM_ID_PARAMETER)
    public ResponseEntity<String> deleteBSMMetadata(
            @ApiParam(name = SYSTEM_ID,
                    value = "systemID of bsmMetadata object to delete.",
                    required = true)
            @PathVariable(SYSTEM_ID) final UUID systemID) {
        bsmMetadataService.deleteEntity(systemID);
        return ResponseEntity.status(NO_CONTENT)
                .body(DELETE_RESPONSE);
    }
}
