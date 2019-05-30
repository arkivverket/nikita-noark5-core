package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.PartyRole;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.IPartyRoleService;
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
public class PartyRoleController {

    private IPartyRoleService partyRoleService;

    public PartyRoleController(IPartyRoleService partyRoleService) {
        this.partyRoleService = partyRoleService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new partrolle
    // POST [contextPath][api]/metadata/partrolle/ny-partrolle
    @ApiOperation(
            value = "Persists a new PartyRole object",
            notes = "Returns the newly created PartyRole object after it " +
                    "is persisted to the database",
            response = PartyRole.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "PartyRole " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = PartyRole.class),
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
    public ResponseEntity<MetadataHateoas> createPartyRole(
            HttpServletRequest request,
            @RequestBody PartyRole partyRole)
            throws NikitaException {

        MetadataHateoas metadataHateoas =
                partyRoleService.createNewPartyRole(partyRole);

        return ResponseEntity.status(CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all PartyRole
    // GET [contextPath][api]/metadata/partrolle/
    @ApiOperation(
            value = "Retrieves all PartyRole ",
            response = PartyRole.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "PartyRole codes found",
                    response = PartyRole.class),
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

    // Retrieves a given PartyRole identified by a systemId
    // GET [contextPath][api]/metadata/partrolle/{systemId}/
    @ApiOperation(
            value = "Gets PartyRole identified by its systemId",
            notes = "Returns the requested PartyRole object",
            response = PartyRole.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "PartyRole " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = PartyRole.class),
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

    // Create a suggested PartyRole(like a template) with default values
    // (nothing persisted)
    // GET [contextPath][api]/metadata/ny-partrolle
    @ApiOperation(
            value = "Creates a suggested PartyRole",
            response = PartyRole.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "PartyRole codes found",
                    response = PartyRole.class),
            @ApiResponse(
                    code = 404,
                    message = "No PartyRole found"),
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
    generateDefaultPartyRole(HttpServletRequest request) {

        MetadataHateoas metadataHateoas = new MetadataHateoas
                (partyRoleService.generateDefaultPartyRole());

        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a partrolle
    // PUT [contextPath][api]/metatdata/partrolle/
    @ApiOperation(
            value = "Updates a PartyRole object",
            notes = "Returns the newly updated PartyRole object after it " +
                    "is persisted to the database",
            response = PartyRole.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "PartyRole " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = PartyRole.class),
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
    public ResponseEntity<MetadataHateoas> updatePartyRole(
            @ApiParam(name = "systemID",
                    value = "systemId of partyRole to update.",
                    required = true)
            @PathVariable("systemID") String systemID,
            @RequestBody PartyRole partyRole,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = partyRoleService.handleUpdate
                (systemID, parseETAG(request.getHeader(ETAG)), partyRole);

        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }
}
