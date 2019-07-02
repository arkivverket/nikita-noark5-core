package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.PartRole;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.IPartRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.PART_ROLE;
import static nikita.common.config.N5ResourceMappings.SYSTEM_ID;
import static nikita.common.util.CommonUtils.Validation.parseETAG;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * Created by tsodring on 31/1/18.
 */

@RestController
@RequestMapping(
        value = HATEOAS_API_PATH + SLASH + NOARK_METADATA_PATH + SLASH,
        produces = {NOARK5_V5_CONTENT_TYPE_JSON, NOARK5_V5_CONTENT_TYPE_JSON_XML})
@SuppressWarnings("unchecked")
public class PartRoleController {

    private IPartRoleService partyRoleService;

    public PartRoleController(IPartRoleService partyRoleService) {
        this.partyRoleService = partyRoleService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new partrolle
    // POST [contextPath][api]/metadata/partrolle/ny-partrolle
    @ApiOperation(
            value = "Persists a new PartRole object",
            notes = "Returns the newly created PartRole object after it " +
                    "is persisted to the database",
            response = PartRole.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "PartRole " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = PartRole.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 404,
                    message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(
                    code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @RequestMapping(
            method = RequestMethod.POST,
            value = PART_ROLE + SLASH + NEW_PART_ROLE
    )
    public ResponseEntity<MetadataHateoas> createPartRole(
            HttpServletRequest request,
            @RequestBody PartRole partyRole)
            throws NikitaException {

        MetadataHateoas metadataHateoas =
                partyRoleService.createNewPartRole(partyRole);

        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all PartRole
    // GET [contextPath][api]/metadata/partrolle/
    @ApiOperation(
            value = "Retrieves all PartRole ",
            response = PartRole.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "PartRole codes found",
                    response = PartRole.class),
            @ApiResponse(
                    code = 404,
                    message = API_MESSAGE_NOT_FOUND),
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
    @GetMapping(value = PART_ROLE)
    public ResponseEntity<MetadataHateoas> findAll(HttpServletRequest request) {
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(partyRoleService.findAll());
    }

    // Retrieves a given PartRole identified by a systemId
    // GET [contextPath][api]/metadata/partrolle/{systemId}/
    @ApiOperation(
            value = "Gets PartRole identified by its systemId",
            notes = "Returns the requested PartRole object",
            response = PartRole.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "PartRole " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = PartRole.class),
            @ApiResponse(
                    code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 404,
                    message = API_MESSAGE_NOT_FOUND),
            @ApiResponse(
                    code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = PART_ROLE + SLASH + LEFT_PARENTHESIS + SYSTEM_ID +
            RIGHT_PARENTHESIS + SLASH)
    public ResponseEntity<MetadataHateoas> findBySystemId(
            @PathVariable("systemID") final String systemId,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = partyRoleService.find(systemId);

        return ResponseEntity.status(OK)
                .allow(
                        getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // Create a suggested PartRole(like a template) with default values
    // (nothing persisted)
    // GET [contextPath][api]/metadata/ny-partrolle
    @ApiOperation(
            value = "Creates a suggested PartRole",
            response = PartRole.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "PartRole codes found",
                    response = PartRole.class),
            @ApiResponse(
                    code = 404,
                    message = "No PartRole found"),
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
    @RequestMapping(
            method = RequestMethod.GET,
            value = NEW_PART_ROLE
    )
    public ResponseEntity<MetadataHateoas>
    generateDefaultPartRole(HttpServletRequest request) {

        MetadataHateoas metadataHateoas = new MetadataHateoas
                (partyRoleService.generateDefaultPartRole());

        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a partrolle
    // PUT [contextPath][api]/metatdata/partrolle/
    @ApiOperation(
            value = "Updates a PartRole object",
            notes = "Returns the newly updated PartRole object after it " +
                    "is persisted to the database",
            response = PartRole.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "PartRole " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PartRole.class),
            @ApiResponse(
                    code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 404,
                    message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(
                    code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = PART_ROLE + SLASH + PART_ROLE)
    public ResponseEntity<MetadataHateoas> updatePartRole(
            @ApiParam(name = "systemID",
                    value = "systemId of partyRole to update.",
                    required = true)
            @PathVariable("systemID") String systemID,
            @RequestBody PartRole partyRole,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = partyRoleService.handleUpdate
                (systemID, parseETAG(request.getHeader(ETAG)), partyRole);

        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }
}
